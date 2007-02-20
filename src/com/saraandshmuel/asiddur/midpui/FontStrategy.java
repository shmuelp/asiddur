/*
 * FontStrategy.java
 *
 * Created on August 29, 2006, 12:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.midpui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * Abstracts the font interface used by ASiddur to allow for usage of either a 
 * built-in font or one defined by a series of images.
 * @author shmuelp
 */
public interface FontStrategy {

   int getBaselinePosition();

   /**
    * Sets all references to null.  Needed to ensure that memory is released 
    * back to system for PalmOS.  See post at: 
    * http://news.palmos.com/read/messages?id=204129
    */
   void releaseReferences();
   
   /**
    * Gets the advance width of the specified character in this Font.
    */
   public int charWidth( char ch );
   
   /** 
    * Returns the advance width of the characters in ch, starting at the 
    * specified offset and for the specified number of characters (length).
    */
    public int charsWidth(char[] ch, int offset, int length);
   
//    /**
//     * Gets the total advance width for showing the specified String in this Font.
//     */
//    public int stringWidth(String str);
    
    /**
     * Gets the standard height of a line of text in this font.
     */
    public int getHeight();

    /**
     * Draws the specified character.
     */
    public void drawChar(Graphics graphics, char character, int x, int y, int anchor);

    /**
     * Draws the specified characters.
     */
    public void drawChars(Graphics graphics, char[] data, int offset, int length, int x, int y, int anchor);

    public String getFontName();
    
    /**
     * Gets the corresponding native font
     */
    public Font getNativeFont();
}
