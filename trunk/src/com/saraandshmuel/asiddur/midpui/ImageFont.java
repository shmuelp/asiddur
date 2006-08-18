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
   private Font passthrough = Font.getDefaultFont();
   
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
    * Cached widths of the characters
    */
   int[] glyphWidths = new int[256];
   
   /**
    * Inter-character spacing
    */
   int spacing = 2;
   
   /**
    * The height of the font
    */
   int height = 0;
   
   /** Creates a new instance of ImageFont */
   public ImageFont(String fontName) {
      this.fontName = fontName;
      height = passthrough.getHeight();
      
      String fullPrefix = prefix + fontName + '/';
      for (int i = 0; i < glyphImages.length; i++) {
         try
         {
            glyphImages[i] = Image.createImage(fullPrefix + Integer.toString(i) + ".png");
            glyphWidths[i] = glyphImages[i].getWidth() + spacing;
            height = Math.max( height, glyphImages[i].getHeight() + spacing );
         }
         catch( java.io.IOException ioe)
         {
            glyphImages[i] = null;
            glyphWidths[i] = passthrough.charWidth((char)i);
         }
      }
   }
   
   /**
    * Gets the advance width of the specified character in this Font.
    */
   int charWidth( char ch )
   {
      return glyphWidths[ch];
   }
   
   /** 
    * Returns the advance width of the characters in ch, starting at the 
    * specified offset and for the specified number of characters (length).
    */
    int	charsWidth(char[] ch, int offset, int length)
    {
       int result = 0;
       
       for (int i = 0; i < offset; i++) {
          result += glyphWidths[ch[i]];
       }
       
       return result;
    }
    
    /**
     * Gets the total advance width for showing the specified String in this Font.
     */
    int	stringWidth(String str)
    {
//       char[] chars = new char[str.length()];
//       str.getChars(0, str.length(), chars, 0);
//       
//       return charsWidth(chars, 0, chars.length);
       
       int result = 0;
       
       for (int i = 0; i < str.length(); i++) {
          result += glyphWidths[str.charAt(i)];
       }
       
       return result;
    }
    
    /**
     * Gets the standard height of a line of text in this font.
     */
    int	getHeight()
    {
       return height;
    }
    
    /**
     * Draws the specified character.
     */
    void drawChar(Graphics graphics, char character, int x, int y, int anchor)
    {
       if ( glyphImages[character] != null )
       {
          graphics.drawImage( glyphImages[character], x, y, anchor );
       }
       else
       {
          graphics.drawChar( character, x, y, anchor );
       }
    }

    /**
     * Draws the specified characters.
     */
    void drawChars(Graphics graphics, char[] data, int offset, int length, int x, int y, int anchor)
    {
       if ( ( anchor & Graphics.LEFT ) != 0 )
       {
          for (int i = offset; i < length; x+=glyphWidths[data[i]], ++i) {
             drawChar( graphics, data[i], x, y, anchor );
          }
       }
       else if ( ( anchor & Graphics.RIGHT) != 0 )
       {
          for (int i = offset+length-1; i >= offset; x-=glyphWidths[data[i]], --i) {
             drawChar( graphics, data[i], x, y, anchor );
          }
       }
       else // anchor must be Graphics.CENTER
       {
          int width = 0;
          for (int i = offset; i < length; i++) {
             width += glyphWidths[data[i]];
          }

          x = x-(width/2);
          for (int i = offset; i < length; x+=glyphWidths[data[i]], ++i) {
             drawChar( graphics, data[i], x, y, anchor );
          }
       }
    }

   public Font getPassthrough() {
      return passthrough;
   }

   public void setPassthrough(Font passthrough) {
      this.passthrough = passthrough;
   }
}
