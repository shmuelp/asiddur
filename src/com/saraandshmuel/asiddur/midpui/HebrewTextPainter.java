/*
 * HebrewTextPainter.java
 *
 * Created on March 30, 2006, 10:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.midpui;

import com.saraandshmuel.asiddur.common.TefillaReader;
import javax.microedition.lcdui.Graphics;

/**
 * Draws a line of Hebrew text on the screen
 * @author shmuelp
 */
public class HebrewTextPainter {
   
   /**
    * A vertical tab character (used to encode navigation marks)
    */
   private final char VT = 11; //'\v'
   
   /**
    * Font adapter to draw the font
    */
   private final ImageFont font;
   
   /**
    * Determines whether or not to reorder the lines
    */
   private boolean reorder = true;
   
   /** Creates a new instance of HebrewTextPainter */
   public HebrewTextPainter( ImageFont font ) {
      this.font = font;
   }
   
   /**
    * Draws a string (in a single line) on the screen
    *
    * @param graphics The graphics object on which to draw
    * @param reader The reader from which to get the text
    * @param offset The offset of the text within the reader
    * @param length The length of text to draw
    * @param x The x-position at which to draw
    * @param y The y-position at which to draw
    */
   public void drawChars( Graphics graphics,
           TefillaReader reader,
           int offset,
           int length,
           int x,
           int y) {
      char[] buffer = new char[length];
      reader.getTextChars(offset, length, buffer);
      buffer = logicalToVisual(buffer);
      font.drawChars( graphics, buffer, 0, length, x, y, Graphics.TOP | Graphics.RIGHT );
//      graphics.drawChars( buffer,
//              0,
//              buffer.length,
//              x,
//              y,
//              Graphics.TOP | Graphics.RIGHT);
   }
   
   /**
    * Converts the logical text representation to a visual representation
    * @param text The text to convert
    * @return The converted text
    */
   public final char[] logicalToVisual( final char[] text ) {
      //Logger.log("Layout.layoutLines() Beginning...\n");
      char[] ret = new char[text.length];

      if ( reorder )
      {
         for( int i=0; i < ret.length; ++i ) {
            if ( text[i] == '\n' || text[i] == '\r' || text[i] == VT) {
               ret[ret.length-i-1] = ' ';
            } else {
               ret[ret.length-i-1] = text[i];
            }
         }
      }
      else
      {
         for( int i=0; i < ret.length; ++i ) {
            if ( text[i] == '\n' || text[i] == '\r' || text[i] == VT) {
               ret[i] = ' ';
            } else {
               ret[i] = text[i];
            }
         }
      }
      
      //Logger.log("Layout.layoutLines() Ending\n");
      return ret;
   }
}
