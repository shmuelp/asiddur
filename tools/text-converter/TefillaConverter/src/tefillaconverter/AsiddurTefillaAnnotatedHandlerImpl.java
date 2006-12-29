/*
 * File:           AsiddurTefillaAnnotatedHandlerImpl.java
 * Date:           November 20, 2006  11:02 PM
 *
 * @author  shmuelp
 * @version generated by NetBeans XML module
 */
package tefillaconverter;
import com.saraandshmuel.asiddur.common.TefillaConstants;
import com.saraandshmuel.asiddur.common.TextFunctions;
import com.saraandshmuel.asiddur.common.Prefs;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;
import org.xml.sax.*;
public class AsiddurTefillaAnnotatedHandlerImpl implements AsiddurTefillaAnnotatedHandler {

   public static final boolean DEBUG = false;
   
   private DataOutputStream stream; 
   
   private OutputStreamWriter writer;
   
   private Map textIds = new TreeMap();
   
   private Vector textLengths = new Vector();
   
   private long[] textPositions = null;
   
   private Charset outputCharset = null;
   
   class IfData {
      String ifText = new String();
      String elseText = new String();
      boolean afterElse = false;
      byte headerType = 0;
      byte headerValue = 0;
   }
   
   private Stack ifStack = new Stack();
   
   class LanguageIdName {
      public String language;
      public short id;
      public String name;
   }
   
   private Vector languageIdNames = new Vector();
   
   private short lastId = 0;
   
   private static final TextFunctions functionsObj = new TextFunctions(null);
   
   private static final Prefs prefsObj = new Prefs();
   
   public AsiddurTefillaAnnotatedHandlerImpl( OutputStream os ) {
      SortedMap charsets = Charset.availableCharsets();
      // TODO: Remove "magic" constant
      this.outputCharset = (Charset) charsets.get("windows-1255");
      
      stream = new DataOutputStream( os );
      writer = new OutputStreamWriter( stream, outputCharset );
   }

   public void start_asiddur_annotated(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("start_asiddur_annotated: " + meta);
      
      if ( outputCharset == null ) {
         String message = "Error: The necessary charset \"windows-1255\" is " +
                            "not available; giving up";
         throw new SAXException(message);
      }
      
      try
      {
         stream.write(TefillaConstants.HEADER);
         String version = meta.getValue("format");
         byte versionByte = (byte) (Float.parseFloat(version) * 10);
         stream.writeByte(versionByte);
      }
      catch( IOException ioe )
      {
         System.err.println("Error writing file: ");
         ioe.printStackTrace(System.err);
      }
   }

   public void end_asiddur_annotated() throws SAXException {
      if (DEBUG) System.err.println("end_asiddur_annotated()");
   }

   public void handle_text_header(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_text_header: " + meta);
      
      String name = meta.getValue("name");
      short id = Short.parseShort( meta.getValue("id") );
      short length = Short.parseShort( meta.getValue("length") );
      
      textIds.put( name, new Short(id) );
      if ( textLengths.size() < id+1 )
      {
         textLengths.setSize(id+1);
      }
      textLengths.setElementAt( new Short(length), id );

//      System.out.println("name=" + name + ", id=" + id + ", length=" + length );
   }

   public void handle_get(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_get: " + meta);
      
      byte slot = Byte.parseByte(meta.getValue("slot"));
      
      try {
         stream.writeByte(TefillaConstants.TEXT_GET);
         stream.writeByte(slot);
      } catch (NumberFormatException ex) {
         ex.printStackTrace();
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void start_toc(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("start_toc: " + meta);
   }

   public void end_toc() throws SAXException {
      if (DEBUG) System.err.println("end_toc()");

      try {
         // Write tefilla starting points to stream, including the text ID of 
         // the tefilla, the language of the name, followed by the name itself
         for (Iterator it = languageIdNames.iterator(); it.hasNext();) {
            LanguageIdName elem = (LanguageIdName) it.next();
            stream.writeByte(TefillaConstants.TEFILLA_NAME);
            //stream.writeByte((byte) elem.language.length());
            //stream.writeByte((byte) elem.name.length());
            stream.writeShort(elem.id);
            stream.writeUTF(elem.language);
            stream.writeUTF(elem.name);
         }
         
         // Compute and write out text posisions
         textPositions = new long[textLengths.size()];
         long lastPosition = 0;
         stream.writeByte(TefillaConstants.TEXT_POSITION);
         stream.writeShort(textPositions.length-1);
         for (short i = 1; i < textPositions.length; i++) {
            Short length = (Short) textLengths.get(i);
            if ( length == null ) {
               textPositions[i] = -1;
            } else {
               textPositions[i] = lastPosition;
               // Add previous value + 1 byte for block headerType + 2 bytes for 
               // text id + 1 byte for block terminator
               // TODO: Remove "magic number" from code
               lastPosition += length.shortValue() + 4;
            }
            stream.writeLong(textPositions[i]);
         }
      } catch (IOException ioe) {
         System.err.println("Error writing header information to file: ");
         ioe.printStackTrace(System.err);
      }
   }

   public void handle_include(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_include: " + meta);

      short target = lookupBlockId(meta.getValue("target"));
      
      try {
         stream.writeByte(TefillaConstants.TEXT_INCLUDE);
         stream.writeShort(target);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void handle_p(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_p: " + meta);
      try {
         stream.writeByte(TefillaConstants.TEXT_P);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void handle_function(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_function: " + meta);
      
      byte function = lookupFunction(meta.getValue("name"));
      
      // Increment the function count
      IfData ifData = (IfData) ifStack.peek();
      if ( ifData != null ) {
         ++ifData.headerValue;
      }

      try {         
         stream.writeByte(function);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void start_tefillot(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("start_tefillot: " + meta);
   }

   public void end_tefillot() throws SAXException {
      if (DEBUG) System.err.println("end_tefillot()");
   }

   public void start_text(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("start_text: " + meta);
      
      short id = lookupBlockId(meta.getValue("name"));
      
      try {
         stream.writeByte(TefillaConstants.TEXT_BLOCK);
         stream.writeShort(id);
      } catch (IOException ioe) {
         System.err.println("Error writing text block: ");
         ioe.printStackTrace(System.err);
      }
}

   public void handle_text(final java.lang.String data, final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_text: " + data);

      try {
         writer.write(data);
      } catch (IOException ioe) {
         System.err.println("Error writing text block: ");
         ioe.printStackTrace(System.err);
      }
}

   public void end_text() throws SAXException {
      if (DEBUG) System.err.println("end_text()");

      try {
         stream.writeByte(TefillaConstants.TEXT_BLOCK_END);
      } catch (IOException ioe) {
         System.err.println("Error writing text block: ");
         ioe.printStackTrace(System.err);
      }
   }

   public void handle_set(final java.lang.String data, final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_set: " + data);
      
      byte slot = Byte.parseByte(meta.getValue("slot"));
      short length = (short) data.length();
      try {
         stream.writeByte(TefillaConstants.TEXT_SET);
         stream.writeByte(slot);
         stream.writeShort(length);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void handle_else(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_else: " + meta);

      IfData ifData = (IfData) ifStack.peek();
      ifData.afterElse = true;
}

   public void handle_br(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_br: " + meta);

      try {
         stream.writeByte(TefillaConstants.TEXT_BR);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void start_if(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("start_if: " + meta);

      IfData ifData = new IfData();
      
      if ( meta.getIndex("function") != -1 ) {
         ifData.headerType = TefillaConstants.TEXT_IF_FUNCTION;
         byte function = lookupFunction(meta.getValue("function"));
         ifData.headerValue = function;
      } else if ( meta.getIndex("day") != -1 ) {
         ifData.headerType = TefillaConstants.TEXT_IF_DAY;
         byte day = Byte.parseByte(meta.getValue("day"));
         ifData.headerValue = day;
      } else if ( meta.getIndex("month") != -1 ) {
         ifData.headerType = TefillaConstants.TEXT_IF_MONTH;
         byte month= Byte.parseByte(meta.getValue("month"));
         ifData.headerValue = month;
      } else if ( meta.getIndex("variable") != -1 ) {
         ifData.headerType = TefillaConstants.TEXT_IF_VARIABLE;
         byte variable= lookupVariable(meta.getValue("variable"));
         ifData.headerValue = variable;
      } else if ( meta.getIndex("or") != -1 ) {
         ifData.headerType = TefillaConstants.TEXT_IF_OR;
         // Fill in value later, as the <function/> tags are encoutered
      } else {
         throw new SAXException("Invalid if tag encountered");
      }
      
      ifStack.push(ifData);
   }

   public void handle_if(final java.lang.String data, final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_if: " + data);
      
      if ( data != null )
      {
         IfData ifData = (IfData) ifStack.peek();
         if ( ifData.afterElse )
         {
            ifData.ifText = ifData.ifText.concat(data);
         }
         else
         {
            ifData.elseText = ifData.elseText.concat(data);
         }
      }
   }

   public void end_if() throws SAXException {
      if (DEBUG) System.err.println("end_if()");
      
      IfData ifData = (IfData) ifStack.pop();
      try {
         stream.writeByte(ifData.headerType);
         stream.writeShort((short)ifData.ifText.length());
         writer.write(ifData.ifText);
         if (ifData.afterElse)
         {
            stream.writeByte(TefillaConstants.TEXT_JUMP);
            stream.writeShort((short)ifData.elseText.length());
            writer.write(ifData.elseText);
         }
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void handle_navmark(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_navmark: " + meta);

      try {
         stream.writeByte(TefillaConstants.TEXT_NAVMARK);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void start_texts(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("start_texts: " + meta);
   }

   public void end_texts() throws SAXException {
      if (DEBUG) System.err.println("end_texts()");
   }

   public void handle_name(final java.lang.String data, final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_name: " + data);
      
      LanguageIdName languageIdName = new LanguageIdName();
      languageIdName.id = lastId;
      languageIdName.language = meta.getValue("lang");
      languageIdName.name = data;
      languageIdNames.add(languageIdName);
   }

   public void start_tefilla(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("start_tefilla: " + meta);
      
      lastId = lookupBlockId(meta.getValue("target"));
   }

   public void end_tefilla() throws SAXException {
      if (DEBUG) System.err.println("end_tefilla()");
   }
   
   /** 
    * Looks up the id of a particular function by name.
    *
    * This really belongs in the TextFunctions class, but is kept here because
    * TextFunctions needs to work in J2ME, where there is no reflection.
    */
   private byte lookupFunction( String name ) {
      byte result = 0;
      try {
         
         Class functionsClass = TextFunctions.class;
         Field functionField = functionsClass.getDeclaredField( name );
         result = functionField.getByte( functionsObj );
      } catch (SecurityException ex) {
         ex.printStackTrace();
      } catch (IllegalArgumentException ex) {
         ex.printStackTrace();
      } catch (IllegalAccessException ex) {
         ex.printStackTrace();
      } catch (NoSuchFieldException ex) {
         ex.printStackTrace();
      }
      
      return result;
   }
   
   /** 
    * Looks up the id of a particular function by name.
    *
    * This really belongs in the Prefs class, but is kept here because
    * TextFunctions needs to work in J2ME, where there is no reflection.
    */
   private byte lookupVariable( String name ) {
      byte result = 0;
      try {
         
         Class prefsClass = Prefs.class;
         Field prefsField = prefsClass.getDeclaredField( name );
         result = prefsField.getByte( prefsObj );
      } catch (SecurityException ex) {
         ex.printStackTrace();
      } catch (IllegalArgumentException ex) {
         ex.printStackTrace();
      } catch (IllegalAccessException ex) {
         ex.printStackTrace();
      } catch (NoSuchFieldException ex) {
         ex.printStackTrace();
      }
      
      return result;
   }
   
   /** 
    * Convenience function
    */
   private short lookupBlockId( String name ) {
      Short blockId = (Short) textIds.get(name);
      return blockId.shortValue();
   }
}
