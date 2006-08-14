/*
 * TefillaNullStrategy.java
 *
 * Created on April 5, 2006, 12:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

/**
 * A default, do-nothing implementation of the TefillaReader interface.  It 
 * plays the role of a ConcreteStrategy in the Strategy design pattern.  Since
 * it doesn't actually do anything, it is useful only as a default or 
 * placeholder where an instantiation of a TefillaReader is needed, but there is 
 * no way to actually accomplish anything meaningful.
 *
 * @author shmuelp
 */
public class TefillaNullStrategy implements TefillaReaderStrategy {
   
   /**
    * Creates a new instance of TefillaNullStrategy
    */
   public TefillaNullStrategy() {
   }

   /**
    * Gets a character.  In this no-nothing implementation, always returns a 
    * space character.
    * @param position The position in the text
    * @return a space
    */
   public char getTextChar(int position) {
      return ' ';
   }

   /**
    * Puts a series of characters in a buffer.  In this do-nothing 
    * implementation, nothing is done.
    * @param begin The beginning position
    * @param count The number of character to return
    * @param buffer The buffer in which to put characters
    */
   public void getTextChars(int begin, int count, char[] buffer) {
   }

   /**
    * Gets the length of the current text.
    * @return The constant 0
    */
   public int getTextLength() {
      return 0;
   }

   /**
    * Returns a series of character from the text.  In this do-nothing 
    * implementation, an empty string is returned.
    * @param begin The beginning position
    * @param count The number of character to return
    * @return An empty string
    */
   public String getTextString(int begin, int count) {
      return "";
   }
}
