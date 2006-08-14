/*
 * TefillaReader.java
 *
 * Created on March 27, 2006, 5:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

import java.io.DataInputStream;

/**
 * Facade to the tefilla reading code (reference the Facade pattern).  Handles 
 * the creation of and access to appropriate TefillaReaderStrategy functions.  
 * Also can enumerate and give the labels for each tefilla.
 * @author shmuelp
 */
public class TefillaReader {
    
   /**
    * The mediator of the Siddur application
    */
    private ASiddurMediator mediator;
    
   /**
    * The TefillaReaderStrategy currently in use
    */
    private TefillaReaderStrategy strategy = new TefillaNullStrategy();

   /**
    * The tefilla of Schacharit
    */
    public static final int SHACHARIT = 0;
   /**
    * The tefilla of Mincha
    */
    public static final int MINCHA = 1;
   /**
    * The tefilla of Ma'ariv
    */
    public static final int MAARIV = 2;
   /**
    * Other tefillot / brachot
    */
    public static final int OTHER = 3;
    
    //private char[] myText = new char[0];
        
    /** 
     * If true, nikkud will shown.  Otherwise, it will be stripped out
     */
    protected boolean showNikud = false;
    
    /**
    * Creates a new instance of TefillaReader
    * @param mediator The application mediator
    */
    public TefillaReader(ASiddurMediator mediator) {
        this.mediator = mediator;
    }
    
   /**
    * Gets the names of each tefilla
    * @return An array containing the tefilla names
    */
    public String[] getTextNames() {
        String[] result = new String[4];
        
        result[SHACHARIT] = TextHints.getTextName(SHACHARIT);
        result[MINCHA] = TextHints.getTextName(MINCHA);
        result[MAARIV] = TextHints.getTextName(MAARIV);
        result[OTHER] = TextHints.getTextName(OTHER);
        
        return result;
    }
    
   /**
    * Selects a tefilla
    * @param textNum The number of the tefilla to select.  Must be one of the 
    * constants defined in the class.
    */
    public void setText( final int textNum ) {
       strategy = new TefillaBufferReaderStrategy( textNum );
    }

   /**
    * Gets data from the text as a String
    * @param begin The position of the first character
    * @param count The total length of the string
    * @return A string <CODE>count</CODE> characters long containing the text 
    * data beginning at position <CODE>begin</CODE>
    */
     public String getTextString( int begin, int count ) {
        return strategy.getTextString( begin, count );
    }

   /**
    * Gets a number of characters into a buffer
    * @param begin The position of the first character to copy
    * @param count The number of characters to copy
    * @param buffer A character buffer into which the data is copied.  It must be 
    * at least <CODE>count</CODE> elements long.
    */
    public void getTextChars( int begin, int count, char[] buffer) {
       strategy.getTextChars( begin, count, buffer );
    }
    
   /**
    * Gets a single character at a given position
    * @param position The position of the character to retrieve
    * @return The character at that position
    */
    public char getTextChar( int position ) {
       return strategy.getTextChar( position );
    }
    
   /**
    * Gets the total length of the text
    * @return The length of the text
    */
    public int getTextLength() {
       return strategy.getTextLength();
    }
}
