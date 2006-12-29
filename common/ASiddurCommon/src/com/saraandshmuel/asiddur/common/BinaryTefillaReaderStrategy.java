/*
 * BinaryTefillaReaderStrategy.java
 *
 * Created on December 29, 2006, 12:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author shmuelp
 */
public class BinaryTefillaReaderStrategy implements com.saraandshmuel.asiddur.common.TefillaReaderStrategy {
   
   /**
    * The file from which to retrieve tefilla data
    */
   private static final String filename = "/res/tefillot.bin";
   
   /**
    * When set true, nikkud is included when text is read.  Otherwise, it is 
    * stripped out.
    */
    protected boolean showNikud = false;
    
    /**
     * When set true, text is converted from CP-1255/ISO-8859-8 to UTF-8
     */
    private boolean convertToUTF = false;
    
    /**
     * The positions of all text blocks, by ID
     */
    private long[] blockPositions = null;
    
    /**
     * The read tefillot block IDs
     */
    private short[] tefillotId = null;
    
    /**
     * The list of read tefillot names
     */
    private String[] tefillotNames = null;   

    /**
     * The stream from which to read the tefilla data
     */
    private DataInputStream inputStream = null;
    
    /**
     * Current tefilla data
     *
     * This should be replaced by a proper buffering scheme
     */
    char [] tefillaText = new char[0];
    
   /** Creates a new instance of BinaryTefillaReaderStrategy */
   public BinaryTefillaReaderStrategy() {
        java.io.InputStream is = null;
        FastCharVector readData = new FastCharVector();
        Vector names = new Vector();
        Vector ids = new Vector();
        long position = 0;
        
        try {
            is = getClass().getResourceAsStream(filename);
            
            if ( is != null ) {
                inputStream = new DataInputStream( is );
                if ( inputStream != null )
                {
                   byte[] header = new byte[TefillaConstants.HEADER.length];
                   for (int i = 0; i < header.length; i++) {
                      header[i]=0;
                   }
                   inputStream.readFully(header);
                   boolean valid = true;
                   for (int i = 0; i < header.length; i++) {
                      valid = valid && ( header[i] == TefillaConstants.HEADER[i]);
                   }
                   if ( !valid ) {
                      throw new IOException("Incorrect format of file header");
                   }
                   position += TefillaConstants.HEADER.length;
                   
                   boolean done = false;
                   byte b;
                   String locale = System.getProperty("microedition.locale");
                   while ( !done )
                   {
                      // Save location; read next block ID code
                      is.mark(Integer.MAX_VALUE);
                      inputStream.mark(Integer.MAX_VALUE);
                      b = inputStream.readByte();
                      
                      // Process name of a tefilla
                      if ( b == TefillaConstants.TEFILLA_NAME ) {
                         short id = inputStream.readShort();
                         String language = inputStream.readUTF();
                         String name = inputStream.readUTF();
                         position += 7 + language.length() + name.length();
                         
                         if ( language.equals(locale)) {
                            ids.addElement(new Short(id));
                            names.addElement(name);
                            System.out.println("Adding tefilla " + name);
                         } else {
                            System.out.println("Skipping tefilla " + name + 
                                               " for locale " + language);
                         }
                      }
                      
                      // Process text block position information
                      if ( b == TefillaConstants.TEXT_POSITION ) {
                         short numBlocks = inputStream.readShort();
                         blockPositions = new long[numBlocks];
                         for (int i = 0; i < blockPositions.length; i++) {
                            blockPositions[i] = inputStream.readLong();
                         }
                         position += 3 + 4 * numBlocks;
                      }
                      
                      // Hit the first text block; finish header processing
                      if ( b == TefillaConstants.TEXT_BLOCK ) {
                         done = true;
                         inputStream.reset();
                         
                         // Store located tefillot names and IDs
                         for (int i = 0; i < ids.size(); i++) {
                            tefillotId[i] = ((Byte) ids.elementAt(i)).byteValue();
                            tefillotNames[i] = (String) names.elementAt(i);
                         }
                         
                         System.out.println("Correctly parsed data, header was "
                                            + position + "bytes long");
                         // If mark/reset doesn't work, will need to correct 
                         // position information to add header size
                      }
                   } // End header processing loop
                } // End data input stream initial processing loop
            } else {
             readData.setChars( "Unable to locate resource!!" );
             //sb.append( "Unable to locate resource!!" );
            }
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }
   }
   
   public void setText( short id ) {
      long initialBlockSize = blockPositions[id+1] - blockPositions[id];
      FastCharVector readData = new FastCharVector((int)initialBlockSize);
      int c;
      long l;

      // TODO: Implement this properly
         
      try {
         // Position at beginning of block, relying on mark/reset
         inputStream.reset();
         l = inputStream.skipBytes((int)blockPositions[id]);
         while ( l < blockPositions[id]) {
            inputStream.read();
            ++l;
         }
         
         // Text reading code from TefillaBufferReaderStrategy
         while ((c = inputStream.read()) != -1) {
            if ( convertToUTF && c >= 0xc0 && c <= 0xfa ) {
               if ( showNikud || c>= 0xd3 ) {
                  c += 0x04F0;
               }
            }
            if ( showNikud || c < 0xc0 || c >= 0xd3 ) {
                readData.append( (char) c );
            }
         }
      } catch (IOException ex) {
         ex.printStackTrace();
      }
      
      tefillaText = readData.clone();
   }
   
   public char getTextChar(int position) {
      // TODO: Implement this
      return 0;
   }

   public void getTextChars(int begin, int count, char[] buffer) {
      // TODO: Implement this
   }

   public int getTextLength() {
      // TODO: Implement this
      return 0;
   }

   public String getTextString(int begin, int count) {
      // TODO: Implement this
      return "";
   }

   public void setConvertToUTF(boolean convertToUTF) {
      this.convertToUTF = convertToUTF;
   }

}
