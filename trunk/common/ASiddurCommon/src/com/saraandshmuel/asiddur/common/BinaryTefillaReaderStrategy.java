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
    private int[] blockPositions = null;
    
    /**
     * The size of the header
     */
    private int headerSize = 0;
    
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
        int position = 0;
        
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
                   
                   byte version = inputStream.readByte();
                   System.out.println("Version of format is " + version);
                   ++position;
                   
                   boolean done = false;
                   byte b;
                   String locale = System.getProperty("microedition.locale");
                   System.out.println("Current locale is " + locale);
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
                         position += 7;
                         position += language.getBytes("UTF8").length;
                         position += name.getBytes("UTF8").length;
                         headerSize = position;
                         
                         if ( locale.startsWith(language)) {
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
                         blockPositions = new int[numBlocks+1];
                         System.out.println("Found text position block:");
                         for (int i = 1; i < blockPositions.length; i++) {
                            blockPositions[i] = inputStream.readInt();
                            System.out.println("block " + i + " at position " + blockPositions[i]);
                         }
                         position += 3 + (4 * numBlocks);
                         headerSize = position;
                      }
                      
//                      // Hit the first text block; finish header processing
//                      if ( b == TefillaConstants.TEXT_BLOCK ) {
//                         done = true;
//                      }
                      
                      if ( b == TefillaConstants.TEXT_BLOCK ) {
                         short blockID = inputStream.readShort();
                         position += 3;
                         if ( position != ( headerSize + 3 + blockPositions[blockID] ) ) {
                            System.err.println("Found block " + blockID + 
                                               " at location " + position + 
                                               " INSTEAD of expected " +
                                               (headerSize + 3 + blockPositions[blockID]) );
                         } else {
                            System.out.println("Found block " + blockID + 
                                               " at expected location " +
                                               (headerSize + 3 + blockPositions[blockID]) );
                         }
                                                  
                         if ( blockID == blockPositions.length - 1 ) {
                            done = true;
                         }
                         
                         boolean innerDone = false;
                         byte temp;
                         while (!innerDone) {
                            temp = inputStream.readByte();
                            ++position;
                            
                            switch( temp ) {
                               case TefillaConstants.TEXT_BLOCK_END:
                                  System.out.print("Position " + position + ": ");
                                  System.out.println("Found end of block");
                                  innerDone= true;
                                  break;
                               
                               case TefillaConstants.TEXT_GET:
                                  byte var = inputStream.readByte();
                                  ++position;
                                  System.out.print("Position " + position + ": ");
                                  System.out.println("Found get with variable " + 
                                                     var);
                                  break;
                                  
                               case TefillaConstants.TEXT_JUMP:
                                  short offset = inputStream.readShort();
                                  position += 2;
                                  System.out.print("Position " + position + ": ");
                                  System.out.println("Found jump with offset " + 
                                                     offset);
                                  break;
                                  
                               case TefillaConstants.TEXT_INCLUDE:
                                  short block = inputStream.readShort();
                                  position += 2;
                                  System.out.print("Position " + position + ": ");
                                  System.out.println("Found include with block " + 
                                                     block);
                                  break;
                                  
                               case TefillaConstants.TEXT_IF_DAY:
                               case TefillaConstants.TEXT_IF_FUNCTION:
                               case TefillaConstants.TEXT_IF_MONTH:
                               case TefillaConstants.TEXT_IF_VARIABLE:
                                  short jump = inputStream.readShort();
                                  byte data = inputStream.readByte();
                                  position += 3;
                                  System.out.print("Position " + position + ": ");
                                  System.out.println("Found if (" + temp + 
                                                     ") with jump " + jump + 
                                                     " and extra data " + data);
                                  break;                                  

                               case TefillaConstants.TEXT_SET:
                                  byte var2 = inputStream.readByte();
                                  short size = inputStream.readShort();
                                  position += 3;
                                  System.out.print("Position " + position + ": ");
                                  System.out.println("Found include with variable " + 
                                                     var2 + " and size " + size);
                                  break;
                                  
                               case TefillaConstants.TEXT_IF_OR:
                                  short jump2 = inputStream.readShort();
                                  byte funcs = inputStream.readByte();
                                  position += 3;
                                  System.out.print("Position " + position + ": ");
                                  System.out.println("Found if (OR) with jump " 
                                                     + jump2 + " and " + funcs +
                                                     " functions:");
                                  for (int i = 0; i < funcs; ++i) {
                                     System.out.println("Function #" + i + 
                                                        inputStream.readByte());
                                     ++position;
                                  }
                                  break;
                            } // end inner parsing switch-case
                         }// end text block read loop
                      }
                   } // End header processing loop
                   
                   // Finished looping
                   inputStream.reset();

                   // Store located tefillot names and IDs
                   tefillotId = new short[ids.size()];
                   tefillotNames = new String[names.size()];
                   for (int i = 0; i < ids.size(); i++) {
                      tefillotId[i] = ((Short) ids.elementAt(i)).shortValue();
                      tefillotNames[i] = (String) names.elementAt(i);
                   }

                   System.out.println("Correctly parsed data, header was "
                                      + position + "bytes long");
                   // If mark/reset doesn't work, will need to correct 
                   // position information to add header size
                } // End data input stream initial processing loop
            } else {
             readData.setChars( "Unable to locate resource!!" );
             //sb.append( "Unable to locate resource!!" );
            }
        } catch (java.io.EOFException ex) {
            ex.printStackTrace();
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        }
   }
   
   public void setText( short id ) {
      int initialBlockSize = blockPositions[id+1] - blockPositions[id];
      FastCharVector readData = new FastCharVector((int)initialBlockSize);
      int c;
      int pos;

      // TODO: Implement this properly
         
      try {
         // Position at beginning of block, relying on mark/reset
         inputStream.reset();
         pos = inputStream.skipBytes((int)blockPositions[id]);
         while ( pos < blockPositions[id]) {
            inputStream.read();
            ++pos;
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
