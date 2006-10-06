/*
 * HebrewTextCustomItem.java
 *
 * Created on February 6, 2006, 1:20 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.midpui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Ticker;
import com.saraandshmuel.asiddur.common.*;

/**
 * Draws and handles input on the the Tefilla screen.  Uses a 
 * {@link HebrewTextBox} to actually draw the text.
 * @author shmuelp
 */
public class HebrewTextCanvas extends javax.microedition.lcdui.Canvas {

   /**
    * Reference to the mediator for the midlet
    */
   private MidpMediator mediator = null;
    
    //private TefillaReader reader = null;
    
    /** Class to actually draw the text */
    private HebrewTextBox textBox;
    //private String statusText = "No key was pressed yet";
    //private Font statusFont = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    
    /** 
     * Creates a new instance of HebrewTextCustomItem 
     * @param dummy Dummy string required by NetBeans generated code
     */
    public HebrewTextCanvas(String dummy) {
       super();
       com.saraandshmuel.asiddur.common.Logger.log("Contructed HebrewTextCanvas");
    }
    
    /**
    * Paints the screen
    * @param graphics The graphics object corresponding representing the screen
    */
    protected void paint( Graphics graphics) {
        //Logger.log("Canvas.paint() Beginning...\n");
        
        // paint is called in a new thread context -- catch and log any errors 
        // or exceptions - lack of painting makes debugging hard!
        try {
            // clear screen
            graphics.setColor(255, 255, 255);
            graphics.fillRect(0, 0, getWidth(), getHeight());

            graphics.setColor(0, 0, 0);

            // // Draw status text
            // graphics.setFont(statusFont);
            // graphics.drawString( statusText, 0, getHeight(), Graphics.LEFT | Graphics.BOTTOM );

            // // Draw line
            // graphics.drawLine( 0, getHeight() - statusFont.getHeight(), 
            //                    getWidth(), getHeight() - statusFont.getHeight() );
            
            
            final int scrollBarWidth = mediator.getFont().charWidth('r');

            // Draw top text
            // Fudge left border to prevent drawing off the screen
            textBox.paintText(graphics, 10 + scrollBarWidth, 0, getWidth(), 
                              getHeight() );

            // Draw "scroll bar"
            graphics.setColor( 170, 170, 170 );
            graphics.fillRect( 0, 0, scrollBarWidth, getHeight() );
            graphics.setColor( 0, 0, 0 );
            final int numLines = textBox.getLastLine();
            final int position = textBox.getLine();
            final int numVisible = textBox.getLastVisibleLine() - position;
            final int handleHeight = Math.max(numVisible * getHeight() / numLines, 5);
            graphics.fillRect( 0, position* getHeight() / numLines, 
                               scrollBarWidth, handleHeight );
            
        } catch( NullPointerException npe ) {
            Logger.log("HebrewTextCanvas.paint() - mediator was never set!\n");
        } catch( Throwable t ) {
            Logger.log("HebrewTextCanvas.paint() - unexpected exception: \n" + t.getMessage() + '\n');
            t.printStackTrace();
        }
        //Logger.log("Canvas.paint() Ending\n");
    }

//     private void setStatusText( int keycode, boolean current ) {
//         statusText = textBox.getShownPercentage() + "%" 
//                     + "| "
//                     + getKeyName(keycode)
//                     + " key "
//                     + ( current ? "is" : "was" )
//                     + " pressed"
//                      ;
//         
//         repaint();
//     }

//     protected void keyPressed(int keyCode) {
//         setStatusText(keyCode, true);
//     }

   /**
    * Handle key released events
    * @param keyCode The keycode of the key that was released
    */
    protected void keyPressed(int keyCode) {
        // TODO: command pattern?  mediator pattern?
        switch( getGameAction( keyCode ) )  {
            case DOWN:
                scrollDown();
                break;
            case UP:
                scrollUp();
                break;
            case RIGHT:
                navDown();
                break;
            case LEFT:
                navUp();
                break;
            case FIRE:
                mediator.toggleView();
                break;
        }

//         setStatusText(keyCode, false);
    }
    
   /**
    * Handle key repeat events
    * @param keyCode The keycode of the key that was released
    */
    protected void keyRepeated(int keyCode) {
        // TODO: command pattern?  mediator pattern?
        switch( getGameAction( keyCode ) )  {
            case DOWN:
                scrollDown();
                break;
            case UP:
                scrollUp();
                break;
            case RIGHT:
                navDown();
                break;
            case LEFT:
                navUp();
                break;
            case FIRE:
                mediator.toggleView();
                break;
        }

//         setStatusText(keyCode, false);
    }
    
//   /**
//    * Sets the source of the text
//    * @param tefillaReader The text source
//    */
//    public void setTefillaReader( TefillaReader tefillaReader ) {
//       this.reader = tefillaReader;
//       if ( textBox != null )
//       {
//          textBox.setTefillaReader(tefillaReader);
//       }
//    }

    /**
     * Resets the position and re-lays out the text
     */
    public void resetMainText() {
        textBox.resetText();
    }
    
    /**
     * Scrolls up one screen
     */
    public void scrollUp() {
        textBox.scrollUp();
        repaint();
    }
    
    /**
     * Scrolls down one screen
     */
    public void scrollDown() {
        textBox.scrollDown();
        repaint();
    }
    
    /**
     * Goes up to the previous navigation mark
     */
    public void navUp() {
        textBox.prevNavigationMark();
        repaint();
    }
    
    /**
     * Goes down to the next navigation mark
     */
    public void navDown() {
        textBox.nextNavigationMark();
        repaint();
    }
    
   public void setMediator(MidpMediator mediator) {
      this.mediator = mediator;
      this.textBox = new HebrewTextBox(mediator, 0, 0, 0);
   }

    /**
     * Sets all references to null.  Needed to ensure that memory is released 
     * back to system for PalmOS.  See post at: 
     * http://news.palmos.com/read/messages?id=204129
     */
    public void releaseReferences() {
       this.mediator = null;
       if ( this != null ) { this.textBox.releaseReferences(); }
       this.textBox = null;
    }
}
