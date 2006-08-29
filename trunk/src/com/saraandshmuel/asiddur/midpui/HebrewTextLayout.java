package com.saraandshmuel.asiddur.midpui;

import com.saraandshmuel.asiddur.common.*;
import javax.microedition.lcdui.Font;


/**
 * Computes the line divisions for tefilla text
 * @note If threading is added, locking will need to be needed
 */
class HebrewTextLayout {

   /** Creates a new instance of HebrewTextLayout */
   public HebrewTextLayout( MidpMediator mediator ) {
      this.mediator= mediator;
   }
   
   /**
    * Validity flag describing the current layout.  Defaults to false, and is 
    * set true after a layout is computed.  Client is responsible for checking 
    * continuing validity using the isValid function.
    */
   public boolean valid = false;
   
   /**
    * Width of the screen for the computed layout
    */
   public int width = 0;
   
   /** 
    * The number of characters in the longest computed line in the layout
    */
   public int maxCharLine = 1;
   
   /**
    * The font used to compute the last layout
    */
   public FontStrategy font = null;
   
   /**
    * Reference to the mediator (needed for the font)
    */
   private MidpMediator mediator;
   
   /**
    * The positions of the ending character of each line in the computed layout
    */
   public FastIntVector lineLengths = new FastIntVector();
   
   /**
    * The vertical tab (navigation mark) character
    */
   final char VT = 11; //'\v'
   
   /**
    * The character positions of each navigation mark
    */
   public FastIntVector navigationMarks = new FastIntVector(10);
   
   /**
    * Determines validity of the computed layout.  A layout is considered to 
    * still be valid if it neither the font nor the width of the screen has 
    * changed since the layout was computed.
    * @param width the width of the screen in pixels
    * @param font the font that will be used to draw the text
    * @return true if the layout is still valid; false otherwise
    */
   public boolean isValid(int width, FontStrategy font) {
      boolean result = valid && this.width == width && this.font == font;
      
//      if (!result)
//      {
//         String why = "";
//         if ( !valid )
//         {
//            why += " !valid";
//         }
//         if ( this.width != width )
//         {
//            why += " !width";
//         }
//         if ( this.font != font )
//         {
//            why += " !font";
//         }
//         
//         Logger.log("HebrewTextLayout: isValid()==false:" + why + '\n');
//      }
      
      return result;
   }
   
   /**
    * Updates the layout.  Saves the parameters used to compute the layout and 
    * calls the utility function computeLineLengths to actually compute the 
    * layout.
    * @param reader the source of the text
    * @param width the width of the screen in pixels
    * @param normalFont the font to be used to draw the text
    */
   public void updateLayout(TefillaReader reader, int width, FontStrategy normalFont) {
      //Logger.log("Layout.updateLayout() Beginning...\n");
      this.width = width;
      this.font = normalFont;
      maxCharLine = 1;
      lineLengths = computeLineLengths( reader, width );
      valid = true;
      //Logger.log("Layout.updateLayout() Ending\n");
   }
   
   /**
    * Computes the layout (line lengths) for a given text.
    * 
    * The text is scanned character by character.  Navigation marks are noted.  
    * When there is not enough room for a character, the line is drawn until 
    * the previous space or dash character.
    *
    * @param reader the source of the text
    * @param width the width of the screen in pixels
    * @return a vector of the positions of the ending characters of each line
    */
   private FastIntVector computeLineLengths(TefillaReader reader, int width) {
      //Logger.log("Layout.computeLineLengths() Beginning...\n");
      
      // Optimization; add in sane value if needed
      //final int minimalRows = 1 + font.charsWidth(text, 0, text.length) / width;
      final int minimalRows = 0;
      //Logger.log("Computed minimal rows = " + minimalRows + "\n");
      //if ( minimalRows < 1 ) {
      //    Logger.log("text.length=" + text.length + ", width=" + width + "\n" );
      //}
      FastIntVector rows = new FastIntVector( minimalRows >= 1 ? minimalRows : 10 );
      navigationMarks = new FastIntVector();
      rows.append(-1);
      
      int row = 1;
      int rowWidth = 0;
      int lastBreak = 0;
      int lastPos = 0;
      //Logger.log("About to enter loop\n");
      for (int c = 0; c < reader.getTextLength(); ++c) {
         char cur = reader.getTextChar(c);
         switch(cur) {
            case VT:
               navigationMarks.append(row);
               break;
               
            case '\n':
               
               rows.append(c);
               ++row;
               maxCharLine = Math.max(maxCharLine, c-lastPos);
               lastPos = c;
               rowWidth = 0;
               break;
               
            case ' ':
            case '-':
               lastBreak = c;
               
               // intentional fall-through
               
            default:
               
               int newWidth = font.charWidth(cur);
               
               // Does this row have room for this character?
               if ( rowWidth + newWidth < width) {
                  rowWidth += newWidth;
               } else {
                  // Look for last breakable point
                  if ( lastBreak > rows.elementAt(row-1) ) {
                     rows.append(lastBreak);
                     ++row;
                     maxCharLine = Math.max(maxCharLine, c-lastPos);
                     lastPos = c;
                     //rowWidth = font.charsWidth( text, lastBreak, c-lastBreak );
                     rowWidth = font.stringWidth( reader.getTextString(lastBreak, c-lastBreak ) );
                  } else {
                     rows.append(c-1);
                     ++row;
                     maxCharLine = Math.max(maxCharLine, c-lastPos);
                     lastPos = c;
                     rowWidth = newWidth;
                  }
               }
               
               break;
         }
      }
      
      if ( rowWidth != 0 ) {
         //Logger.log("Adding final row ending\n");
         rows.append( reader.getTextLength() -1 );
      }
      
      //Logger.log("About to shrink vectors\n");
      rows.shrinkToLength();
      navigationMarks.shrinkToLength();
      
      //Logger.log("Layout.computeLineLengths() Ending\n");
      return rows;
   }
   
    /**
     * Sets all references to null.  Needed to ensure that memory is released 
     * back to system for PalmOS.  See post at: 
     * http://news.palmos.com/read/messages?id=204129
     */
    public void releaseReferences() {
       this.font = null;
       if ( this != null ) { this.lineLengths.releaseReferences(); }
       this.lineLengths = null;
       this.mediator = null;
       if ( this != null ) { this.navigationMarks.releaseReferences(); }
       this.navigationMarks = null;
    }
}

