/*
 * TestCanvas.java
 *
 * Created on March 16, 2006, 6:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.midpui;

import java.io.*;
import javax.microedition.lcdui.*;
import com.saraandshmuel.asiddur.common.*;
//import pcffont.*;

/**
 * Canvas used to test out new code.  Not currently referenced in built midlet.
 * @author shmuelp
 */
public class TestCanvas extends Canvas {
    //private PCFFont font;
   
   /** The string to draw */
    private String string = "Hello, world - éøùà";
    
    private MidpMediator mediator;
    
    /** Creates a new instance of TestCanvas */
    public TestCanvas() {
        //InputStream is = getClass().getResourceAsStream("/res/clR6x12-ISO8859-8.pcf");
        //try {
        //    font = new PCFFont(is);
        //}
        //catch( IOException ioe ) {
        //    Logger.log("Exception while creating PCFFont: " + ioe.getMessage() + '\n');
        //}
       
       Logger.log("Default: " + fontSizeInfo(Font.getDefaultFont()));
       Logger.log("Input: " + fontSizeInfo(Font.FONT_INPUT_TEXT));
       Logger.log("Static: " + fontSizeInfo(Font.FONT_STATIC_TEXT));
    }

    /**
    * Paints the canvas
    * @param graphics the graphics context used to paint
    */
    protected void paint(Graphics graphics) {
        graphics.setColor(255, 255, 255);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        graphics.setColor(0, 0, 0);

        //font.setCharSpacing(5);
        //font.setBgColor(0x00FFFFFF);
        //font.setFgColor(0x00000000);
        
        //font.drawString(graphics, string, 20, 20 );
        //graphics.drawString(string, 10, 10, Graphics.LEFT | Graphics.TOP);
        
        ImageFont font =  mediator.getFont();

        char []testChars = new char[14];
        
        "éøùà, top".getChars(0,9,testChars,0);
        font.drawChars( graphics, testChars, 0, 9, graphics.getClipWidth()-10, 0, Graphics.TOP | Graphics.RIGHT );
        
        "éøùà, bottom".getChars(0,12,testChars,0);
        font.drawChars( graphics, testChars, 0, 12, graphics.getClipWidth()-10, font.getHeight()*2, Graphics.BOTTOM | Graphics.RIGHT );
        
        "éøùà, baseline".getChars(0,14,testChars,0);
        font.drawChars( graphics, testChars, 0, 14, graphics.getClipWidth()-10, font.getHeight()*4, Graphics.BOTTOM | Graphics.RIGHT );
        
    }

    /**
    * 
    * Accessor for the string to draw
    * @return The string to be drawn
    */
    public String getString() {
        return string;
    }

    /**
    * 
    * Mutator to change the string to paint
    * @param string the new string to draw
    */
    public void setString(String string) {
        this.string = string;
    }
    
    private String fontSizeInfo( int font ) 
    {
       return fontSizeInfo(Font.getFont(font));
    }
    
    private String fontSizeInfo( javax.microedition.lcdui.Font font ) 
    {
       StringBuffer sb = new StringBuffer( "height=");
       sb.append(font.getHeight());
       sb.append(", w[M]=");
       sb.append(font.charWidth('M'));
       sb.append(", w[i]=");
       sb.append(font.charWidth('i'));
       sb.append(", w[ ]=");
       sb.append(font.charWidth(' '));
       sb.append('\n');
       
       return sb.toString();
    }

   public void setMediator(MidpMediator mediator) {
      this.mediator = mediator;
   }

    /**
     * Sets all references to null.  Needed to ensure that memory is released 
     * back to system for PalmOS.  See post at: 
     * http://news.palmos.com/read/messages?id=204129
     */
    public void releaseReferences() {
       this.mediator = null;
       this.string = null;
    }
}
