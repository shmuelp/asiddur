/*
 * TextHints.java
 *
 * Created on August 6, 2006, 10:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

/**
 * Hints related to the tefilla text files
 * @author shmuelp
 */
public abstract class TextHints {
   
   /**
    * Returns the resource filename related to a particular text number
    * 
    * @param textNum A text number constant defined in TefillaReader
    * @return the filename of the text
    */
   public static String getTextFilename( final int textNum ) {
      String result;
      
      switch( textNum ) {
         case TefillaReader.SHACHARIT:
             result = "shacharit-ashkenaz.txt";
             break;

         case TefillaReader.MINCHA:
             result = "mincha-ashkenaz.txt";
             break;

         case TefillaReader.MAARIV:
             result = "maariv-ashkenaz.txt";
             break;

         case TefillaReader.OTHER:
             result = "other-ashkenaz.txt";
             break;

         default:
             result = "empty.txt";
      }
      
      return result;
   }
   
   /**
    * Returns the size of a resource file related to a particular text number. 
    * Used to more efficiently allocate buffers
    * 
    * @param textNum A text number defined in TefillaReader
    * @return the size of the text
    */
   public static int getTextSize( final int textNum ) {
      int result;
      
      switch( textNum ) {
         case TefillaReader.SHACHARIT:
             result = 134004;
             break;

         case TefillaReader.MINCHA:
             result = 21050;
             break;

         case TefillaReader.MAARIV:
             result = 30646;
             break;

         case TefillaReader.OTHER:
             result = 24719;
             break;

         default:
             result = 17;
      }
      
      return result;
   }

   /**
    * Returns the human-readable label related to a particular text number
    * 
    * @param textNum A text number constant defined in TefillaReader
    * @return the name of the text
    */
   public static String getTextName( final int textNum ) {
      String result;
      
      switch( textNum ) {
         case TefillaReader.SHACHARIT:
             result = "Shacharit";
             break;

         case TefillaReader.MINCHA:
             result = "Mincha";
             break;

         case TefillaReader.MAARIV:
             result = "Maariv";
             break;

         case TefillaReader.OTHER:
             result = "Other";
             break;

         default:
             result = "empty.txt";
      }
      
      return result;
   }
}
