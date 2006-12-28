/*
 * TefillaReaderStrategy.java
 *
 * Created on April 4, 2006, 11:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

/**
 * Allows reading of (tefilla) text.  It plays the Strategy role in the 
 * Strategy design pattern.  This allows for multiple methods of reading a text,
 * with the appropriate one "plugged in" at runtime.
 * @author shmuelp
 */
public interface TefillaReaderStrategy {
   
   
   /**
    * Gets a single character at a given position
    * @param position The position of the character to retrieve
    * @return The character at that position
    */
   char getTextChar(int position);

   /**
    * Gets a number of characters into a buffer
    * @param begin The position of the first character to copy
    * @param count The number of characters to copy
    * @param buffer A character buffer into which the data is copied.  It must be 
    * at least <CODE>count</CODE> elements long.
    */
   void getTextChars(int begin, int count, char[] buffer);

   /**
    * Gets the total length of the text
    * @return The length of the text
    */
   int getTextLength();

   /**
    * Gets data from the text as a String
    * @param begin The position of the first character
    * @param count The total length of the string
    * @return A string <CODE>count</CODE> characters long containing the text 
    * data beginning at position <CODE>begin</CODE>
    */
   String getTextString(int begin, int count);
   
}
