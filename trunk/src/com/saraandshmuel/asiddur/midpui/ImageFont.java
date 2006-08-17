/*
 * ImageFont.java
 *
 * Created on August 17, 2006, 12:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.midpui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * Represents a font whose glyphs are made up of image files stored as resources
 * in the jar.  Any glyphs that are missing are rendered by a configurable font.
 * @author shmuelp
 */
public class ImageFont {
   
   /**
    * The font to pass through to for unknown glyphs
    */
   Font passthrough = Font.getDefaultFont();
   
   /**
    * Prefix to resources
    */
   String prefix = "/res/";
   
   /**
    * The name of the font
    */
   String fontName;
   
   /**
    * Cached images of the glyphs; null if not present
    */
   Image[] glyphImages = new Image[256];
   
   /**
    * Inter-character spacing
    */
   int spacing = 2;
   
   /** Creates a new instance of ImageFont */
   public ImageFont(String fontName) {
      this.fontName = fontName;
      
      String fullPrefix = prefix + fontName + '/';
      for (int i = 0; i < glyphImages.length; i++) {
         try
         {
            glyphImages[i] = Image.createImage(fullPrefix + Integer.toString(i) + ".png");
         }
         catch( java.io.IOException ioe)
         {
            glyphImages[i] = null;
         }
      }
   }
   
   /**
    * Gets the advance width of the specified character in this Font.
    */
   int charWidth( char ch )
   {
      int result;
      
      if ( glyphImages[ch] != null )
      {
         result = glyphImages[ch].getWidth() + spacing;
      }
      else
      {
         result = passthrough.charWidth(ch);
      }
      
      return result;
   }
   
   /** 
    * Returns the advance width of the characters in ch, starting at the 
    * specified offset and for the specified number of characters (length).
    */
    int	charsWidth(char[] ch, int offset, int length)
    {
       int result = 0;
       
       for (int i = 0; i < offset; i++) {
          result += this.charWidth(ch[offset+i]);
       }
       
       return result;
    }
    
    /**
     * Gets the total advance width for showing the specified String in this Font.
     */
    int	stringWidth(String str)
    {
       char[] chars = new char[str.length()];
       str.getChars(0, str.length()-1, chars, 0);
       
       return charsWidth(chars, 0, chars.length);
    }
    
    /**
     * Gets the standard height of a line of text in this font.
     */
    int	getHeight()
    {
       return passthrough.getHeight();
    }
    
    /**
     * Draws the specified character.
     */
    void drawChar(Graphics graphics, char character, int x, int y, int anchor)
    {
       graphics.drawChar( character, x, y, anchor );
    }

    /**
     * Draws the specified characters.
     */
    void drawChars(Graphics graphics, char[] data, int offset, int length, int x, int y, int anchor)
    {
       // Have to start loop and change loop condition based on anchor
    }
}
