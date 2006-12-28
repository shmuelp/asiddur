/*
 * TefillaReader.java
 *
 * Created on March 27, 2006, 5:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

import java.io.DataInputStream;

/**
 * Allows reading of (tefilla) text.  It plays the ConcreteStrategy role in the 
 * Strategy design pattern.  It works by reading a file (from a resource) into a 
 * local array.
 * @author shmuelp
 */
public class TefillaBufferReaderStrategy implements com.saraandshmuel.asiddur.common.TefillaReaderStrategy {
    
   /**
    * The text to be read
    */
    private char[] myText = new char[0];
    
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
    * Creates a new instance of TefillaReader
    * @param text One of the tefilla constants defined in TefillaReader
    */
    public TefillaBufferReaderStrategy(final int text) {
       setText(text);
    }
    
   /**
    * Sets the text to be read
    * @param textNum One of the text number constants defined in TefillaReader
    */
    private void setText( final int textNum ) {
        String textfile = TextHints.getTextFilename(textNum);
        java.io.InputStream is = null;
        java.io.DataInputStream dis = null;
        FastCharVector readData = new FastCharVector(TextHints.getTextSize(textNum));
        
        try {
            is = getClass().getResourceAsStream("/res/" + textfile);
            
            if ( is != null ) {
                dis = new DataInputStream( is );
                if ( dis != null )
                {
                    int c;
                    while ((c = is.read()) != -1) {
                        if ( convertToUTF ) {
                           c += 0x04F0;
                        }
                        if ( showNikud || c < 0xc0 || c >= 0xd3 ) {
                            readData.append( (char) c );
                        }
                    }
                }
                dis.close();
            } else {
             readData.setChars( "Unable to locate resource!!" );
             //sb.append( "Unable to locate resource!!" );
            }
        } catch (java.io.IOException ex) {
            ex.printStackTrace();
        } finally {
            myText = readData.clone();
        }
    }

   /**
    * Gets data from the text as a String
    * @param begin The position of the first character
    * @param count The total length of the string
    * @return A string <CODE>count</CODE> characters long containing the text 
    * data beginning at position <CODE>begin</CODE>
    */
     public String getTextString( int begin, int count ) {
        if ( begin < 0 ) {
            Logger.log("TefillaReader.getTextString: invalid begin index (" + begin + "), setting to 0");
            begin = 0;
        }
        if ( count < 0 ) {
            Logger.log("TefillaReader.getTextString: invalid count (" + count + "), setting to 0");
            count = 0;
        }
        if ( begin + count > myText.length ) {
            Logger.log("TefillaReader.getTextString: invalid end index, setting count to " + (myText.length-1 - begin));
            count = myText.length-1 - begin;
        }

        return String.valueOf( myText, begin, count );
    }

   /**
    * Gets a number of characters into a buffer
    * @param begin The position of the first character to copy
    * @param count The number of characters to copy
    * @param buffer A character buffer into which the data is copied.  It must be 
    * at least <CODE>count</CODE> elements long.
    */
    public void getTextChars( int begin, int count, char[] buffer) {
        if ( begin < 0 ) {
            Logger.log("TefillaReader.getTextChars: invalid begin index (" + begin + "), setting to 0");
            begin = 0;
        }
        if ( count < 0 ) {
            Logger.log("TefillaReader.getTextChars: invalid count (" + count + "), setting to 0");
            count = 0;
        }
        if ( begin + count > myText.length ) {
            Logger.log("TefillaReader.getTextChars: invalid end index, setting count to " + (myText.length-1 - begin));
            count = myText.length-1 - begin;
        }
        if ( buffer.length < count ) {
            Logger.log("TefillaReader.getTextChars: buffer not big enough for full text, filling as much as possible");
            count = buffer.length;
        }
        
        System.arraycopy(myText, begin, buffer, 0, count);
    }
    
   /**
    * Gets a single character at a given position
    * @param position The position of the character to retrieve
    * @return The character at that position
    */
    public char getTextChar( int position ) {
        if ( position < 0 ) {
            Logger.log("TefillaReader.getTextChar: invalid position (" + position + "), setting to 0");
            position = 0;
        }
        if ( position > myText.length ) {
            Logger.log("TefillaReader.getTextChar: invalid position (" + position + "), setting to " + (myText.length-1));
            position = myText.length-1;
        }
        
        return myText[position];
    }
    
   /**
    * Gets the total length of the text
    * @return The length of the text (in bytes)
    */
    public int getTextLength() {
       return myText.length;
    }
    
    /**
     * Sets all references to null.  Needed to ensure that memory is released 
     * back to system for PalmOS.  See post at: 
     * http://news.palmos.com/read/messages?id=204129
     */
    public void releaseReferences() {
       this.myText = null;
    }

   public void setConvertToUTF(boolean convertToUTF) {
      this.convertToUTF = convertToUTF;
   }
}
