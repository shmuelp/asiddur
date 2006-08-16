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
        graphics.drawString(string, 10, 10, Graphics.LEFT | Graphics.TOP);
        
        java.io.InputStream is = getClass().getResourceAsStream("/res/aleph.png");
        if ( is == null )
        {
           Logger.log("There is no aleph\n");
        }

        try 
        {
           Image aleph = Image.createImage("/res/aleph.png");
           Image yud = Image.createImage("/res/yud.png");
           Image resh = Image.createImage("/res/resh.png");
           Image shin = Image.createImage("/res/shin.png");
           
           int position = graphics.getClipWidth() - 10;
           final int SPACING = 2;

           graphics.drawImage( aleph, position, 50, Graphics.TOP | Graphics.RIGHT );
           position -= aleph.getWidth();
           position -= SPACING;

           graphics.drawImage( shin, position, 50, Graphics.TOP | Graphics.RIGHT );
           position -= shin.getWidth();
           position -= SPACING;

           graphics.drawImage( resh, position, 50, Graphics.TOP | Graphics.RIGHT );
           position -= resh.getWidth();
           position -= SPACING;

           graphics.drawImage( yud, position, 50, Graphics.TOP | Graphics.RIGHT );
           position -= SPACING;
           //position -= yud.getWidth();

        }
        catch( java.io.IOException ioe )
        {
           Logger.log("Exception while creating images from resources: " + ioe.toString());
        }
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

}
