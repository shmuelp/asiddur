/*
 * MidpFontAdapterStrategy.java
 *
 * Created on August 29, 2006, 12:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.midpui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author shmuelp
 */
public class MidpFontAdapterStrategy implements FontStrategy {
   
   private Font font;
   
   /** Creates a new instance of MidpFontAdapterStrategy */
   public MidpFontAdapterStrategy( Font font ) {
      this.font = font;
   }

   /** Creates a new instance of MidpFontAdapterStrategy */
   public MidpFontAdapterStrategy() {
      this.font = Font.getDefaultFont();
   }

   public Font getFont() {
      return font;
   }

   public void setFont(Font font) {
      this.font = font;
   }

   public int getBaselinePosition() {
      return font.getBaselinePosition();
   }

   public void releaseReferences() {
      font = null;
   }

   public int charWidth(char ch) {
      return font.charWidth(ch);
   }

   public int charsWidth(char[] ch, int offset, int length) {
      return font.charsWidth( ch, offset, length );
   }

   public void drawChar(Graphics graphics, char character, int x, int y, int anchor) {
      graphics.drawChar( character, x, y, anchor );
   }

   public void drawChars(Graphics graphics, char[] data, int offset, int length, int x, int y, int anchor) {
      graphics.drawChars( data, offset, length, x, y, anchor);
   }

   public int getHeight() {
      return font.getHeight();
   }

   public int stringWidth(String str) {
      return font.stringWidth(str);
   }

   public String getFontName() {
      return "Native";
   }

   public Font getNativeFont() {
      return font;
   }   
}
