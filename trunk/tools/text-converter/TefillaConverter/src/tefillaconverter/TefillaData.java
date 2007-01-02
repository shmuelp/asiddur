/*
 * TefillaData.java
 *
 * Created on January 1, 2007, 12:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tefillaconverter;

import java.nio.charset.Charset;

/**
 * Class to contain data intended for inclusion in a tefilla binary file.  It 
 * abstracts out the mechanics of adding data to allow for more complicated 
 * processing for certain structures (e.f. if/else)
 * @author shmuelp
 */
public interface TefillaData {
   
   public static final String STRING_ENCODING = "windows-1255";
   
   public static Charset encodingCharset = 
           (Charset) Charset.availableCharsets().get(STRING_ENCODING);

   /**
    * Adds a byte
    */
   public void writeByte( byte b );
   
   /**
    * Adds an array of bytes
    */
   public void writeBytes( byte[] b );
   
   /**
    * Adds a short
    */
   public void writeShort( short s );

   /**
    * Adds an int
    */
   public void writeInt( int i );

   /**
    * Adds a string.  It handles properly encoding writeString.
    */
   public void writeString( String s );
   
   /** 
    * Returns the byte representation of the data
    */
   public byte[] getBytes();
}
