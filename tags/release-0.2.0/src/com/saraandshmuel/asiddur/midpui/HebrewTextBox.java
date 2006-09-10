/*
 * HebrewTextPainter.java
 *
 * Created on February 7, 2006, 3:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.midpui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;
import com.saraandshmuel.asiddur.common.*;

/**
 * Class responsible for scrolling text and laying it out on the screen.  
 * It will paint the characters using a HebrewTextPainter object.
 * @note This class is not thread-safe.
 * @author shmuelp
 */
public class HebrewTextBox {
    
   /**
    * The red component of the text foreground color
    */
    private int red = 255;
   /**
    * The green component of the text foreground color
    */
    private int green = 255;
   /**
    * The blue component of the text foreground color
    */
    private int blue = 255;
    
    /**
     * A cache of the text to be displayed
     */
    private char[] text = new char[0];
    
//    /**
//     * TefillaReader object from which to read the text
//     */
//    private TefillaReader tefillaReader;
    
    /**
     * Reference to the mediator for the MIDP application
     */
    private MidpMediator mediator;
    
    /**
     * Utility class for computing and storing the line layout of the text
     */
    private HebrewTextLayout layout;
    
    /**
     * Utility class for painting characters
     */
    private HebrewTextPainter painter;
    
    /**
     * The first visible line to show
     */
    private int line = 0;
    
    /**
     * The height of the display area the last time the paintText operation was 
     * called
     */
    private int lastHeight = 0;
    
    /**
    * Creates a new instance of HebrewTextPainter
    * @param font The font to draw text with
    * @param red The red component of the foreground color
    * @param green The green component of the foreground color
    * @param blue The blue component of the foreground color
    */
    public HebrewTextBox(MidpMediator mediator, int red, int green, int blue) {
        this.mediator = mediator;
        this.red=red;
        this.green=green;
        this.blue=blue;
        
        this.layout = new HebrewTextLayout( mediator );
        this.painter = new HebrewTextPainter( mediator );
    }
    
   /**
    * Paints text on the screen.  Computes the line layout if needed.
    * @param graphics The graphics object on which to paint the text
    * @param x1 The x component of the upper left corner
    * @param y1 The y component of the upper left corner
    * @param x2 The x component of the lower right corner
    * @param y2 The y component of the lower right corner
    */
    public void paintText( javax.microedition.lcdui.Graphics graphics, 
                           int x1, 
                           int y1, 
                           int x2,
                           int y2) {
        //Logger.log("Painter.paintText() Beginning...\n");
        
        // Add a 1-pixel border
        ++x1;
        ++y1;
        --x2;
        --y2;
       
        FontStrategy font = mediator.getFont();
        lastHeight = y2-y1;
        graphics.setColor(red, green, blue);
        graphics.setFont( font.getNativeFont() );
        TefillaReader tefillaReader = mediator.getTefillaReader();

        if( !layout.isValid( x2-x1, font ) ) {
            layout.updateLayout( tefillaReader, x2-x1, font);
            if ( text.length < layout.maxCharLine * getVisibleLineCount() ) {
                text = new char[layout.maxCharLine * getVisibleLineCount()];
            }
        }

        //Logger.log("Painter.paintText() Calculating dimensions...\n");
        FastIntVector lineOffsets = layout.lineLengths;
        final int lastLine = Math.min( lineOffsets.size(), 
                                       line + 1 + getVisibleLineCount());
        final int fontHeight = font.getBaselinePosition();
        
        boolean reorder = mediator.isReordered();
        
        //Logger.log("Painter.paintText() Drawing lines...\n");
        //System.out.println("About to draw, line=" + line + ", lastLine=" + lastLine);
        for( int i=line+1; i < lastLine; ++i ) {
            //System.out.println("Printing chars " + lineOffsets.elementAt(i-1) + " to " + lineOffsets.elementAt(i) );
            
            painter.drawChars( graphics,
                               tefillaReader, 
                               font,
                               reorder,
                               lineOffsets.elementAt(i-1)+1, 
                               lineOffsets.elementAt(i)-lineOffsets.elementAt(i-1), 
                               x2, 
                               y1);
            y1 += fontHeight;
        }
        //graphics.drawString("Test string", x2, y1, Graphics.TOP | Graphics.RIGHT);

        //Logger.log("Painter.paintText() Ending\n");
    }
    
    /**
     * Resets the text position and causes a recompute of the layout
     */
    public void resetText() {
        line = 0;
        layout.valid = false;
    }

    /**
     * Scrolls the visible window down one screenful
     */
    public void scrollDown() {
        //Logger.log("Pre-scrollDown: line = " + line + '\n');
        final int visibleLines = getVisibleLineCount();
        final int lastLine = layout.lineLengths.size() - 1 - visibleLines;
        line += visibleLines;
        if ( line >= lastLine) {
            line = lastLine;
        }
        //Logger.log("Post-scrollDown: line = " + line + '\n');
    }
    
    /**
     * Scrolls the visible window up one screenful
     */
    public void scrollUp() {
        //Logger.log("Pre-scrollUp: line = " + line + '\n');
        line -= getVisibleLineCount();
        if ( line < 0) {
            line = 0;
        }
        //Logger.log("Post-scrollUp: line = " + line + '\n');
    }
    
    /** 
     * Scrolls the visible window down to the next off-screen mark.  Any marks 
     * currently visible are ignored.
     */
    public void nextNavigationMark() {
        Logger.log("nextNavigationMark\n");
        int newLine = 0;
        for( int i = 0; newLine==0 && i < layout.navigationMarks.size(); ++i ) {
            if ( layout.navigationMarks.elementAt(i) > getLastVisibleLine() ) {
                newLine = layout.navigationMarks.elementAt(i);
            }
        }
        if ( newLine > 0 ) {
            setLine(newLine);
        } else {
            setLine( getLastLine()-getVisibleLineCount() );
        }
    }
    
    /**
     * Scrolls the visible window up to the previous off-screen mark.  Any marks
     * currently visible are ignored.
     */
    public void prevNavigationMark() {
        Logger.log("prevNavigationMark\n");
        int newLine = 0;
        for( int i = layout.navigationMarks.size()-1; newLine==0 && i >= 0 ; --i ) {
            if ( layout.navigationMarks.elementAt(i) < line ) {
                newLine = layout.navigationMarks.elementAt(i);
            }
        }
        if ( newLine > 0 ) {
            setLine(newLine);
        } else {
            setLine( 0 );
        }
    }
    
   /**
    * Gets the number of lines visible on the screen at once
    * @return the number of lines
    */
    private final int getVisibleLineCount() {
        FontStrategy font = mediator.getFont();
        return lastHeight / font.getBaselinePosition();
    }
    
   /**
    * Gets the number of the last visible line
    * @return The last visible line
    */
    public final int getLastVisibleLine() {
        return Math.min(line + getVisibleLineCount(), 
                        layout.lineLengths.size() - 1);
    }
    
   /**
    * Gets the last line of the text (which may not be visible)
    * @return The last line
    */
    public final int getLastLine() {
        return layout.lineLengths.size()-1;
    }
    
   /**
    * Gets the percentage of the text currently being shown
    * @return The percentage
    */
    public final int getShownPercentage() {
        return getLastVisibleLine() * 100 / getLastLine();
    }
    
   /**
    * Sets a new position of the visible window
    * @param line The line number of the first line to show on the screen
    */
    public void setLine(int line) {
        this.line = line;
        
        if ( line < 0 ) {
            line = 0;
        }
        
        if ( line >= layout.lineLengths.size() ) {
            line = layout.lineLengths.size() - 1;
        }
    }
    
   /**
    * Gets the current position of the visible window
    * @return THe line number of the first visible line
    */
    public int getLine() {
        return line;
    }

//   /**
//    * Sets the TefillaReader from which to read the text
//    * @param tefillaReader the TefillaReader from which to read the text
//    */
//    public void setTefillaReader(TefillaReader tefillaReader) {
//        this.tefillaReader = tefillaReader;
//    }

    /**
     * Sets all references to null.  Needed to ensure that memory is released 
     * back to system for PalmOS.  See post at: 
     * http://news.palmos.com/read/messages?id=204129
     */
    public void releaseReferences() {
       if ( this != null ) { this.layout.releaseReferences(); }
       this.layout = null;
       this.mediator = null;
       if ( this != null ) { this.painter.releaseReferences(); }
       this.painter = null;
       this.text = null;
    }
}
