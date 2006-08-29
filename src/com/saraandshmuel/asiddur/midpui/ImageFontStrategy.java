/*
 * ImageFontStrategy.java
 *
 * Created on August 17, 2006, 12:05 AM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.midpui;

import java.io.InputStream;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import com.saraandshmuel.asiddur.common.Logger;

/**
 * Represents a font whose glyphs are made up of image files stored as resources
 * in the jar.  Any glyphs that are missing are rendered by a configurable font.
 * @author shmuelp
 */
public class ImageFontStrategy implements FontStrategy {
   
   /**
    * The font to pass through to for unknown glyphs
    */
   private Font nativeFont = Font.getDefaultFont();
   
   /**
    * Prefix to resources
    */
   private String prefix = "/res/";
   
   /**
    * The name of the font
    */
   private String fontName;
   
   /**
    * Cached images of the glyphs; null if not present
    */
   private Image[] glyphImages = new Image[256];
   
   /**
    * Cached widths of the characters
    */
   private byte[] glyphWidths = new byte[256];
   
   /** 
    * The baseline (offset from bottom in pixels) of the font
    */
   private int baseline=-1;
   
   /**
    * 
    * The baseline of the font, compared to the nativeFont baseline
    */
   private int baselineOffset=-1;
   
   /**
    * Offset from left
    */
   private byte[] leftOffsets = new byte[256];
   
   /**
    * Offset from right
    */
   private byte[] rightOffsets = new byte[256];
   
   /**
    * Offset from top
    */
   private byte[] topOffsets = new byte[256];
   
   /**
    * Offset from left
    */
   private byte[] bottomOffsets = new byte[256];
   
   /**
    * The max height of the font
    */
   private int height=-1;
   
   /**
    * Creates a new instance of ImageFontStrategy
    */
   public ImageFontStrategy(String fontName) {
      this.fontName = fontName;
      height = nativeFont.getHeight();
      
      String fullPrefix = prefix + fontName + '/';
      
      InputStream metrics = getClass().getResourceAsStream(fullPrefix + "metrics.bin");
      if ( metrics == null )
      {
         // Font does not exist
         Logger.log("Warning, font \"fontName\" does not exist, passing through");
         height = nativeFont.getHeight();
      }
      else
      {
         try
         {
            height = metrics.read();
            height = Math.max(height, nativeFont.getHeight());
            metrics.read();   //ignore
            baseline = height - metrics.read();
            
            // loop until file is done; exception handler will handle that case
            // Assume that file format is OK
            boolean done = false;
            while (!done)
            {
               // No loss of precision unless font is larger than 127 pixels
               int glyph = metrics.read();
               if ( glyph == -1 )
               {
                  done = true;
               }
               else
               {
                  leftOffsets[glyph] = (byte) metrics.read();
                  rightOffsets[glyph] = (byte) metrics.read();
                  topOffsets[glyph] = (byte) metrics.read();
                  bottomOffsets[glyph] = (byte) metrics.read();
               }
            }
         }
         catch( java.io.IOException ioe )
         {
         }
      }
      
      for (int i = 0; i < glyphImages.length; i++) {
         try
         {
            glyphImages[i] = Image.createImage(fullPrefix + Integer.toString(i) + ".png");
            glyphWidths[i] = (byte) (glyphImages[i].getWidth() + leftOffsets[i] + rightOffsets[i]);
         }
         catch( java.io.IOException ioe)
         {
            glyphImages[i] = null;
            glyphWidths[i] = (byte) nativeFont.charWidth((char)i);
         }
      }
      
      baselineOffset = baseline - nativeFont.getBaselinePosition();
   }
   
   /**
    * Gets the advance width of the specified character in this Font.
    */
   public int charWidth( char ch )
   {
      return glyphWidths[ch];
   }
   
   /** 
    * Returns the advance width of the characters in ch, starting at the 
    * specified offset and for the specified number of characters (length).
    */
    public int charsWidth(char[] ch, int offset, int length)
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
    public int stringWidth(String str)
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
    public int getHeight()
    {
       return height;
    }
    
    /**
     * Draws the specified character.
     */
    public void drawChar(Graphics graphics, char character, int x, int y, int anchor)
    {
       if ( glyphImages[character] != null )
       {
          // Adjust vertical padding to line up along baseline
          if ( (anchor & Graphics.TOP) != 0 )
          {
             y += height - bottomOffsets[character];
             anchor ^= (Graphics.TOP|Graphics.BOTTOM);
          }
          else if ( (anchor & Graphics.BASELINE) != 0 )
          {
             y -= bottomOffsets[character] + (height - baseline);
             anchor ^= (Graphics.BASELINE|Graphics.BOTTOM);
          }
          else if ( (anchor & Graphics.BOTTOM) != 0 )
          {
             y -= bottomOffsets[character];
          }
          
          // Add appropriate horizontal padding
          if ( (anchor & Graphics.LEFT) != 0 )
          {
             x += leftOffsets[character];
          }
          else if ( (anchor & Graphics.HCENTER) != 0 )
          {
             x += ( leftOffsets[character] - rightOffsets[character])/2;
          }
          else if ( (anchor & Graphics.RIGHT) != 0 )
          {
             x -= rightOffsets[character];
          }
          
          graphics.drawImage( glyphImages[character], x, y, anchor );
       }
       else
       {
          if ( (anchor & Graphics.TOP) != 0 )
          {
             y += nativeFont.getBaselinePosition() + baselineOffset;
             anchor ^= (Graphics.BASELINE|Graphics.TOP);
          }
          else if ( (anchor & Graphics.BASELINE) != 0 )
          {
             y += baselineOffset;
          }
          else if ( (anchor & Graphics.BOTTOM) != 0 )
          {
             y -= ( height - baseline ) + baselineOffset;
             anchor ^= (Graphics.BASELINE|Graphics.BOTTOM);
          }
          graphics.drawChar( character, x, y, anchor );
       }
    }

    /**
     * Draws the specified characters.
     */
    public void drawChars(Graphics graphics, char[] data, int offset, int length, int x, int y, int anchor)
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
       else if ( ( anchor & Graphics.RIGHT) != 0 ) // anchor must be Graphics.CENTER
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
       else
       {
          Logger.log("Unknown anchor used in ImageFont.drawChars!");
       }
    }

   public Font getNativeFont() {
      return nativeFont;
   }

   public void setNativeFont(Font nativeFont) {
      this.nativeFont = nativeFont;
   }

   public int getBaselinePosition() {
      return baseline;
   }

   /**
     * Sets all references to null.  Needed to ensure that memory is released 
     * back to system for PalmOS.  See post at: 
     * http://news.palmos.com/read/messages?id=204129
     */
    public void releaseReferences() {
       this.bottomOffsets = null;
       this.fontName = null;
       this.glyphImages = null;
       this.glyphWidths = null;
       this.leftOffsets = null;
       this.nativeFont = null;
       this.prefix = null;
       this.rightOffsets = null;
       this.topOffsets = null;
    }

   public String getFontName() {
      return fontName;
   }

}
