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
import java.io.InputStream;
import java.util.Vector;

/**
 * Loads tefillot from a binary file.
 * 
 * The file may be reopened many times in order to mvoe backwards.  The 
 * constructor reads in the header to allow other classes to access the list of 
 * available tefillot.  When a tefilla is selected, the logical text is made 
 * available to clients.  The logical text is made up of many contiguous regions
 * of text in the file, which may be out of order.
 * @author shmuelp
 */
public class BinaryTefillaReaderStrategy implements com.saraandshmuel.asiddur.common.TefillaReaderStrategy {
   
   /**
    * The file from which to retrieve tefilla data
    */
   private static final String filename = "/res/tefillot.bin";
   
    /**
     * When set true, text is converted from CP-1255/ISO-8859-8 to UTF-8
     */
    private boolean convertToUTF = false;
    
    /**
     * The positions of all text blocks, by ID
     */
    private int[] blockPositions = null;
    
    /**
     * The size of all text blocks, by ID
     */
    private short[] blockSizes = null;
    
    /** 
     * The text of the blocks
     */
    private char[][] blockText = null;
    
    /**
     * The ending logical positions of each region of the current tefillah
     */
    private int[] regionLogical = null;
    
    /**
     * The text block associated with each region
     */
    private short[] regionBlocks = null;
    
    /**
     * The start physical position within the text block of the region
     */
    private int[] regionPhysicalStart = null;
    
    /**
     * The end physical position within the text block of the region
     */
    private int[] regionPhysicalEnd = null;
    
    /**
     * The total length of the current tefilla
     */
    private int totalLength;
    
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
     * The underlying stream from which to read the tefilla data
     */
    private SeekableInputStream seekableInputStream = null;
    
//    /**
//     * The stream from which to read the formatted tefilla data
//     */
//    private DataInputStream inputStream = null;
    
    /**
     * Current tefilla data
     *
     * This should be replaced by a proper buffering scheme
     */
    char [] tefillaText = new char[0];
    
   /** Creates a new instance of BinaryTefillaReaderStrategy */
   public BinaryTefillaReaderStrategy() {
        seekableInputStream = new SeekableInputStream( new InputStreamFactory() {
           public InputStream createInputStream() {
              InputStream result = null;
              try {
                 result = getClass().getResourceAsStream(filename);
              } finally {
              }
              return result;
           }
        });
        FastCharVector readData = new FastCharVector();
        Vector names = new Vector();
        Vector ids = new Vector();
        int position = 0;
        
        try {
//             inputStream = new DataInputStream( seekableInputStream );
//             if ( inputStream != null )
//             {
                      byte[] header = new byte[TefillaConstants.HEADER.length];
                for (int i = 0; i < header.length; i++) {
                   header[i]=0;
                }
                seekableInputStream.readFully(header);
                boolean valid = true;
                for (int i = 0; i < header.length; i++) {
                   valid = valid && ( header[i] == TefillaConstants.HEADER[i]);
                }
                if ( !valid ) {
                   throw new IOException("Incorrect format of file header");
                }
                position += TefillaConstants.HEADER.length;

                byte version = seekableInputStream.readByte();
                System.out.println("Version of format is " + version);
                ++position;

                boolean done = false;
                byte b;
                String locale = System.getProperty("microedition.locale");
                System.out.println("Current locale is " + locale);
                while ( !done )
                {
                   b = seekableInputStream.readByte();

                   // Process name of a tefilla
                   if ( b == TefillaConstants.TEFILLA_NAME ) {
                      short id = seekableInputStream.readShort();
                      String language = seekableInputStream.readUTF();
                      String name = seekableInputStream.readUTF();
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
                      short numBlocks = seekableInputStream.readShort();
                      blockPositions = new int[numBlocks+1];
                      blockSizes = new short[numBlocks+1];
                      System.out.println("Found text position block:");
                      blockPositions[0] = -1;
                      blockSizes[0] = -1;
                      for (int i = 1; i < blockPositions.length; i++) {
                         blockPositions[i] = seekableInputStream.readInt();
                         // Note: Assumes blocks will always be in order
                         blockSizes[i] = (short) (blockPositions[i] - blockPositions[i-1]);
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
                      short blockID = seekableInputStream.readShort();
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
                         temp = seekableInputStream.readByte();
                         ++position;

                         switch( temp ) {
                            case TefillaConstants.TEXT_BLOCK_END:
                               System.out.print("Position " + position + ": ");
                               System.out.println("Found end of block");
                               innerDone= true;
                               break;

                            case TefillaConstants.TEXT_GET:
                               byte var = seekableInputStream.readByte();
                               ++position;
                               System.out.print("Position " + position + ": ");
                               System.out.println("Found get with variable " + 
                                                  var);
                               break;

                            case TefillaConstants.TEXT_JUMP:
                               short offset = seekableInputStream.readShort();
                               position += 2;
                               System.out.print("Position " + position + ": ");
                               System.out.println("Found jump with offset " + 
                                                  offset);
                               break;

                            case TefillaConstants.TEXT_INCLUDE:
                               short block = seekableInputStream.readShort();
                               position += 2;
                               System.out.print("Position " + position + ": ");
                               System.out.println("Found include with block " + 
                                                  block);
                               break;

                            case TefillaConstants.TEXT_IF_DAY:
                            case TefillaConstants.TEXT_IF_FUNCTION:
                            case TefillaConstants.TEXT_IF_MONTH:
                            case TefillaConstants.TEXT_IF_VARIABLE:
                               short jump = seekableInputStream.readShort();
                               byte data = seekableInputStream.readByte();
                               position += 3;
                               System.out.print("Position " + position + ": ");
                               System.out.println("Found if (" + temp + 
                                                  ") with jump " + jump + 
                                                  " and extra data " + data);
                               break;                                  

                            case TefillaConstants.TEXT_SET:
                               byte var2 = seekableInputStream.readByte();
                               short size = seekableInputStream.readShort();
                               position += 3;
                               System.out.print("Position " + position + ": ");
                               System.out.println("Found include with variable " + 
                                                  var2 + " and size " + size);
                               break;

                            case TefillaConstants.TEXT_IF_OR:
                               short jump2 = seekableInputStream.readShort();
                               byte funcs = seekableInputStream.readByte();
                               position += 3;
                               System.out.print("Position " + position + ": ");
                               System.out.println("Found if (OR) with jump " 
                                                  + jump2 + " and " + funcs +
                                                  " functions:");
                               for (int i = 0; i < funcs; ++i) {
                                  System.out.println("Function #" + i + 
                                                     seekableInputStream.readByte());
                                  ++position;
                               }
                               break;
                         } // end inner parsing switch-case
                      }// end text block read loop
                   }
                } // End header processing loop

                // Store located tefillot names and IDs
                tefillotId = new short[ids.size()];
                tefillotNames = new String[names.size()];
                for (int i = 0; i < ids.size(); i++) {
                   tefillotId[i] = ((Short) ids.elementAt(i)).shortValue();
                   tefillotNames[i] = (String) names.elementAt(i);
                }
                blockText = new char[blockSizes.length][];

                System.out.println("Correctly parsed data, header was "
                                   + position + " bytes long");
//             }
                
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
      // TODO: Check if id is really the right ID or not
      computeRegions(tefillotId[id]);
   }
   
   public char getTextChar(int position) {
      int region = findRegion(position);
      short blockId = regionBlocks[region];
      if ( blockText[blockId] == null ) {
         readBlock(blockId);
      }
      
      int offset = position;
      if ( region > 0 ) {
         offset -= blockPositions[region-1];
      }
      
      char result = (char) blockText[blockId][offset];
      // The byte is unsigned; make sure it is now positive
      result &= 0x00FF;
      return result;
   }

   public void getTextChars(int begin, int count, char[] buffer) {
      if ( begin < 0 ) {
         Logger.log("TefillaReader.getTextChars: invalid begin index (" + begin + "), setting to 0");
         begin = 0;
      }
      if ( count < 0 ) {
         Logger.log("TefillaReader.getTextChars: invalid count (" + count + "), setting to 0");
         count = 0;
      }
      if ( begin + count > totalLength ) {
         Logger.log("TefillaReader.getTextChars: invalid end index, setting count to " + (totalLength - begin));
         count = totalLength - begin;
      }
      if ( buffer.length < count ) {
         Logger.log("TefillaReader.getTextChars: buffer not big enough for full text, filling as much as possible");
         count = buffer.length;
      }

      int region = findRegion(begin);
      short blockId = regionBlocks[region];
      if ( blockText[blockId] == null ) {
         readBlock(blockId);
      }
      
      int offset = begin;
      if ( region > 0 ) {
         offset -= blockPositions[region-1];
      }
      
      for (int i = 0; i < count; ++i) {
         buffer[i] = (char) blockText[blockId][offset];
         buffer[i] &= 0x00FF;
         
         ++offset;
         
         // Handle hitting end of region
         if ( offset == blockSizes[blockId] ) {
            offset = 0;
            ++region;
            if ( region == regionBlocks.length ) {
               Logger.log( "Could not fill buffer; " + (count-i) + " characters left");
//               break;
            }
            blockId = regionBlocks[region];
            if ( blockText[blockId] == null ) {
               readBlock(blockId);
            }
         } // end of region-end handling code
      } // end of buffer-filling loop
   } // end of getTextChars;

   public int getTextLength() {
      return totalLength;
   }

   public String getTextString(int begin, int count) {
        if ( begin < 0 ) {
            Logger.log("TefillaReader.getTextString: invalid begin index (" + begin + "), setting to 0");
            begin = 0;
        }
        if ( count < 0 ) {
            Logger.log("TefillaReader.getTextString: invalid count (" + count + "), setting to 0");
            count = 0;
        }
        if ( begin + count > totalLength ) {
            Logger.log("TefillaReader.getTextString: invalid end index, setting count to " + (totalLength - begin));
            count = totalLength - begin;
        }

        char buffer[] = new char[count];
        getTextChars(begin, count, buffer);
        return String.valueOf( buffer );
   }

   public void setConvertToUTF(boolean convertToUTF) {
      this.convertToUTF = convertToUTF;
   }
   
   public String[] getTefillaNames( ) {
      return tefillotNames;
   }
   
   /**
    * Ensures that a text block is read into its position in the blockText
    * array.  May cause other text blocks to be discarded.
    *
    * @todo a better algorithm should be used to decide what to discard
    */
   private void readBlock( short blockId) {
      try {
         // Position to beginning of block and ensure that size is valid
         seekableInputStream.seek( blockPositions[blockId]+1+headerSize );
         
         // Sanity check on block ID
         short blockId2 = seekableInputStream.readShort();
         if ( blockId != blockId2 ) {
            throw new RuntimeException("Expected to read block " + blockId + 
                    ", instead found " + blockId2 +
                    ".  File is corrupt or being read incorrectly.");
         }

         byte[] temp;
         try {
            temp = new byte[blockSizes[blockId]];
            blockText[blockId] = new char[blockSizes[blockId]];
         } catch ( OutOfMemoryError oome ) {
            // Discard all buffered data - we really need a smarter algorithm
            Logger.log("Out of memory; freeing buffered data");
            System.out.println("Out of memory; freeing buffered data");
            for (int i = 0; i < blockText.length; ++i) {
               if ( i != blockId ) {
                  blockText[i] = null;
               }
            }
            Logger.log("Reading in block " + blockId);
            System.out.println("Reading in block " + blockId);
            temp = new byte[blockSizes[blockId]];
            blockText[blockId] = new char[blockSizes[blockId]];
         }
         
         seekableInputStream.read(temp);
         for (int i = 0; i < temp.length; ++i) {
            char c = (char) ( temp[i] &  0x00ff);
            
            if ( convertToUTF && c >= 0xc0 && c <= 0xfa ) {
                c += 0x4f0;
            }
            
            blockText[blockId][i] = (char) ( c &  0x00ff );
         }
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }
   
   /**
    * Computes the beginning and end of each region in a tefilla.  May make a 
    * number of recursive calls to scan text blocks that are included in others.
    */
   private void computeRegions( short blockId ) {
      FastShortVector regionBlock = new FastShortVector();
      FastIntVector regionBegin = new FastIntVector();
      FastIntVector regionEnd = new FastIntVector();
      FastIntVector logicalEnd = new FastIntVector();
      
      totalLength = doComputeRegions( blockId, 
                                      regionBlock, 
                                      regionBegin, 
                                      regionEnd, 
                                      logicalEnd);
      
      regionPhysicalStart = regionBegin.clone();
      regionPhysicalEnd = regionEnd.clone();
      regionBlocks = regionBlock.clone();
      regionLogical = logicalEnd.clone();
   }
   
   private int doComputeRegions( short blockId, 
                          FastShortVector regionBlock,
                          FastIntVector regionBegin,
                          FastIntVector regionEnd,
                          FastIntVector logicalEnd ) {
      int length = 0;
      
      if ( blockText[blockId] == null ) {
         readBlock(blockId);
      }
      
      // TODO: Implement this
      regionBlock.append(blockId);
      regionBegin.append(blockPositions[blockId]);
      regionEnd.append(blockPositions[blockId] + blockSizes[blockId]);
      logicalEnd.append(blockSizes[blockId]);
      length = blockSizes[blockId]-1;

//      // Text reading code from TefillaBufferReaderStrategy
//      while ((c = inputStream.read()) != -1) {
//         if ( convertToUTF && c >= 0xc0 && c <= 0xfa ) {
//            c += 0x04F0;
//         }
//         readData.append( (char) c );
//      }
      
      return length;
   }
   
   /**
    * Finds the id of the given region to allow for looking up in the block, 
    * and position begin/end lists.
    *
    * @param The logical position to find
    * @return The entry in the other bookkeeping lists
    */
   private int findRegion( int logicalPosition ) {
      if ( logicalPosition < 0 || 
           logicalPosition > regionLogical[regionLogical.length-1] ) {
         throw new IllegalArgumentException("Position is out of range");
      }
      
      if ( logicalPosition < regionLogical[0] ) {
         return 0;
      }
      
      // Do a binary search, since list is already sorted
      int low = 1;
      int high = regionLogical.length-1;
      int mid;
      while (low <= high) {
         mid=(low+high)/2;
         if ( logicalPosition == regionLogical[mid] ) {
            return mid;
         } else if ( logicalPosition < regionLogical[mid] ) {
            low = mid+1;
         } else {
            high = mid-1;
         }
      }
      if ( low != high+1 ) {
         throw new RuntimeException("Algorithm is incorrect!");
      }
      
      // Still need to check which is the right value; it can be either one.
      // Remember that high is now less than low
      if ( logicalPosition < regionLogical[high]) {
         return high;
      }
      
      return low;
   }
}
