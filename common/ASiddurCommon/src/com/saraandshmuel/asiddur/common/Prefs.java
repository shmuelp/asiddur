/*
 * Prefs.java
 *
 * Created on December 28, 2006, 12:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

/**
 * The items that can't be figured out automatically
 *
 * @author shmuelp
 */
public class Prefs {
   
   // User preferences
   
   /**
    * Include the phrase "Morid HaTal" when "Mashiv HaRuach" is not said.
    * Only meant to be reference for nusach Ashkenaz
    *
    * The current implementation is a stub for the items needed by the tefilla 
    * markup.  It is intended to be filled in later.
    */
   public static final byte MORID_HATAL = 1;
   
   // Dynamic items
   
   /** 
    * Is the minyan in the house of an avel?
    */
   public static final byte AVEL_HOUSE = 101;
   
   /** Creates a new instance of Prefs */
   public Prefs() {
   }
   
   boolean getPref( byte id ) {
      boolean result = false;
      
      switch ( id ) {
         case MORID_HATAL:
            result = false;
            break;
            
         case AVEL_HOUSE:
            result = true;
            break;
      }
      
      return result;
   }
}
