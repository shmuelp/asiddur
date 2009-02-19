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
   
   private int[] textPositions = null;
   
//   private Charset outputCharset = null;
   
   private Stack dataStack = new Stack();
   
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
      
      stream = new DataOutputStream( os );
      writer = new OutputStreamWriter( stream, TefillaData.encodingCharset);
   }

   public void start_asiddur_annotated(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("start_asiddur_annotated: " + meta);
      
//      if ( outputCharset == null ) {
//         String message = "Error: The necessary charset \"windows-1255\" is " +
//                            "not available; giving up";
//         throw new SAXException(message);
//      }
      
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
      
      TefillaData tefillaData = (TefillaData) dataStack.pop();
      if ( !dataStack.empty() ) {
         throw new SAXException("Data stack not empty at end");
      }
      
      try {
         // Compute and write out text posisions
         textPositions = new int[textLengths.size()];
         int lastPosition = 0;
         stream.writeByte(TefillaConstants.TEXT_POSITION);
         stream.writeShort(textPositions.length-1);
         for (short i = 1; i < textPositions.length; i++) {
            Integer length = (Integer) textLengths.get(i);
            if ( length == null ) {
               textPositions[i] = -1;
            } else {
               textPositions[i] = lastPosition;
               lastPosition += length.shortValue();
            }
            stream.writeInt(textPositions[i]);
         }
         stream.write(tefillaData.getBytes());
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void handle_text_header(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_text_header: " + meta);
      
      String name = meta.getValue("name");
      short id = Short.parseShort( meta.getValue("id") );
      
      textIds.put( name, new Short(id) );

//      System.out.println("name=" + name + ", id=" + id );
   }

   public void handle_get(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_get: " + meta);
      
      byte slot = Byte.parseByte(meta.getValue("slot"));
      writeByte(TefillaConstants.TEXT_GET);
      writeByte(slot);
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
      } catch (IOException ioe) {
         System.err.println("Error writing header information to file: ");
         ioe.printStackTrace(System.err);
      }
   }

   public void handle_include(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_include: " + meta);

      short target = lookupBlockId(meta.getValue("target"));
      writeByte(TefillaConstants.TEXT_INCLUDE);
      writeShort(target);
   }

   public void handle_p(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_p: " + meta);

      writeByte(TefillaConstants.TEXT_P);
   }

   public void handle_function(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_function: " + meta);
      
      byte function = lookupFunction(meta.getValue("name"));
      
      // Increment the function count
      IfData ifData = (IfData) dataStack.peek();
      ifData.incrementHeaderValue();

      writeByte(function);
   }

   public void start_tefillot(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("start_tefillot: " + meta);
   }

   public void end_tefillot() throws SAXException {
      if (DEBUG) System.err.println("end_tefillot()");
   }

   public void start_text(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("start_text: " + meta);

      if ( dataStack.empty() ) {
         // Push on basic data objcet for all text blocks
         dataStack.push(new BasicData());
      }
      dataStack.push(new BasicData());
      
      short id = lookupBlockId(meta.getValue("name"));
      lastId = id;
      
      writeByte(TefillaConstants.TEXT_BLOCK);
      writeShort(id);
}

   public void handle_text(final java.lang.String data, final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_text: " + data);

      writeString(data);
}

   public void end_text() throws SAXException {
      if (DEBUG) System.err.println("end_text()");

      writeByte(TefillaConstants.TEXT_BLOCK_END);
      TefillaData tefillaData = (TefillaData) dataStack.pop();
      
      byte[] textData = tefillaData.getBytes();
      textLengths.add(new Integer(textData.length));

      if ( textLengths.size() < lastId+1 )
      {
         textLengths.setSize(lastId+1);
      }
      textLengths.setElementAt( new Integer(textData.length), lastId );
      
      writeBytes(textData);
   }

   public void start_set(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("start_set: " + meta);
      
      SetData setData = new SetData();
      byte slot = Byte.parseByte(meta.getValue("slot"));
      setData.setVariableNum(slot);
      dataStack.push(setData);
   }

   public void handle_set(final java.lang.String data, final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_set: " + data);
      
      TefillaData tefillaData = (TefillaData) dataStack.peek();
      tefillaData.writeString(data);
   }

   public void end_set() throws SAXException {
      if (DEBUG) System.err.println("end_set()");

      TefillaData tefillaData = (TefillaData) dataStack.pop();
      writeBytes(tefillaData.getBytes());
}

   public void handle_else(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_else: " + meta);

      // Potential exception if the else tag is embedded in another tag, but 
      // that shouldn't happen in correct XML
      IfData ifData = (IfData) dataStack.peek();
      ifData.setAfterElse(true);
}

   public void handle_br(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_br: " + meta);

      writeByte(TefillaConstants.TEXT_BR);
   }

   public void start_if(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("start_if: " + meta);

      IfData ifData = new IfData();
      
      if ( meta.getIndex("function") != -1 ) {
         ifData.setHeaderType(TefillaConstants.TEXT_IF_FUNCTION);
         byte function = lookupFunction(meta.getValue("function"));
         ifData.setHeaderValue(function);
      } else if ( meta.getIndex("day") != -1 ) {
         ifData.setHeaderType(TefillaConstants.TEXT_IF_DAY);
         byte day = Byte.parseByte(meta.getValue("day"));
         ifData.setHeaderValue(day);
      } else if ( meta.getIndex("month") != -1 ) {
         ifData.setHeaderType(TefillaConstants.TEXT_IF_MONTH);
         byte month= Byte.parseByte(meta.getValue("month"));
         ifData.setHeaderValue(month);
      } else if ( meta.getIndex("variable") != -1 ) {
         ifData.setHeaderType(TefillaConstants.TEXT_IF_VARIABLE);
         byte variable= lookupVariable(meta.getValue("variable"));
         ifData.setHeaderValue(variable);
      } else if ( meta.getIndex("or") != -1 ) {
         ifData.setHeaderType(TefillaConstants.TEXT_IF_OR);
         // Fill in value later, as the <function/> tags are encoutered
      } else {
         throw new SAXException("Invalid if tag encountered");
      }
      
      dataStack.push(ifData);
   }

   public void handle_if(final java.lang.String data, final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_if: " + data);
      
      if ( data != null )
      {
         TefillaData tefillaData = (TefillaData) dataStack.peek();
         tefillaData.writeString(data);
      }
   }

   public void end_if() throws SAXException {
      if (DEBUG) System.err.println("end_if()");
      
      TefillaData tefillaData = (TefillaData) dataStack.pop();
      writeBytes(tefillaData.getBytes());
   }

   public void handle_navmark(final Attributes meta) throws SAXException {
      if (DEBUG) System.err.println("handle_navmark: " + meta);

      writeByte(TefillaConstants.TEXT_NAVMARK);
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
   
   /**
    * Writes a byte to the output
    */
   private void writeByte( byte b ) {
      if ( dataStack.empty() ) {
         try {
            stream.writeByte(b);
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      } else {
         TefillaData tefillaData = (TefillaData) dataStack.peek();
         tefillaData.writeByte(b);
      }
   }
   
   /**
    * Writes an array of byte to the output
    */
   private void writeBytes( byte[] b ) {
      if ( dataStack.empty() ) {
         try {
            stream.write(b);
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      } else {
         TefillaData tefillaData = (TefillaData) dataStack.peek();
         tefillaData.writeBytes(b);
      }
   }
   
   /**
    * Writes a short to the output
    */
   private void writeShort( short s ) {
      if ( dataStack.empty() ) {
         try {
            stream.writeShort(s);
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      } else {
         TefillaData tefillaData = (TefillaData) dataStack.peek();
         tefillaData.writeShort(s);
      }
   }

   /**
    * Writes an int to the output
    */
   private void writeInt( int i ) {
      if ( dataStack.empty() ) {
         try {
            stream.writeInt(i);
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      } else {
         TefillaData tefillaData = (TefillaData) dataStack.peek();
         tefillaData.writeInt(i);
      }
   }

   /**
    * Writes a string to the output.  It handles properly encoding writeString.
    */
   private void writeString( String s ) {
      if ( dataStack.empty() ) {
         try {
            writer.write(s);
            writer.flush();
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      } else {
         TefillaData tefillaData = (TefillaData) dataStack.peek();
         tefillaData.writeString(s);
      }
   }
}