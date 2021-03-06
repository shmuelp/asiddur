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
     * The tefilla object needed to get the tefilla names and tefilla text
     */
    private BinaryTefillaReaderStrategy binaryTefilla = new BinaryTefillaReaderStrategy();

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
    public static final int BRACHOT = 3;
    /**
     * Dynamic Tefilla
     */
    private static final int DYNAMIC = 4;
    
    //private char[] myText = new char[0];
        
    /**
     * When set true, text is converted from CP-1255/ISO-8859-8 to UTF-8
     */
    private boolean convertToUTF = false;
    
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
        String[] result = new String[5];
        
        result[SHACHARIT] = TextHints.getTextName(SHACHARIT);
        result[MINCHA] = TextHints.getTextName(MINCHA);
        result[MAARIV] = TextHints.getTextName(MAARIV);
        result[BRACHOT] = TextHints.getTextName(BRACHOT);
        result[DYNAMIC] = binaryTefilla.getTefillaNames()[0];
        
        return result;
    }
    
   /**
    * Selects a tefilla
    * @param textNum The number of the tefilla to select.  Must be one of the 
    * constants defined in the class.
    */
    public void setText( final int textNum ) {
       if ( textNum == SHACHARIT || 
            textNum == MINCHA || 
            textNum == MAARIV || 
            textNum == BRACHOT ) {
          strategy = new TefillaBufferReaderStrategy( textNum );
          strategy.setConvertToUTF(convertToUTF);
       } else if ( textNum >= DYNAMIC ) {
          // TODO: determine from textNum argument
          binaryTefilla.setText( (short)(0) );
          strategy = binaryTefilla;
          strategy.setConvertToUTF(convertToUTF);
       }
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

    public void setConvertToUTF(boolean convertToUTF) {
       this.convertToUTF = convertToUTF;
    }

    /**
     * Sets all references to null.  Needed to ensure that memory is released 
     * back to system for PalmOS.  See post at: 
     * http://news.palmos.com/read/messages?id=204129
     */
    public void releaseReferences() {
       if ( strategy instanceof TefillaBufferReaderStrategy ) {
          TefillaBufferReaderStrategy tbrs = (TefillaBufferReaderStrategy) strategy;
          if ( tbrs != null ) { tbrs.releaseReferences(); }
       }
       this.strategy = null;
    }
}
