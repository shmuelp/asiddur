/*
 * MidpMediator.java
 *
 * Created on March 9, 2006, 6:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.midpui;

import java.util.*;
import javax.microedition.lcdui.*;
import java.io.DataInputStream;

import com.saraandshmuel.asiddur.common.*;

/**
 * Concrete implementation of 
 * {@link com.saraandshmuel.asiddur.common.ASiddurMediator ASiddurMediator} for 
 * MIDP 2.
 * @author shmuelp
 */
public class MidpMediator extends com.saraandshmuel.asiddur.common.ASiddurMediator 
                          implements javax.microedition.lcdui.ItemStateListener{
   
   /**
    * Reference to the main midlet
    */
    private ASiddurMidlet midlet;
    
    /** 
     * Reference to the font to use for drawing
     */
    private FontStrategy font = null;
    
    /**
     * Controls whether or not the text is reordered or not
     */
    private boolean reorder = true;
    
    /**
    * Creates a new instance of MidpMediator
    * @param midlet Reference to the midlet
    */
    public MidpMediator(ASiddurMidlet midlet) {
        this.midlet = midlet;
        Logger.setLog(this);
    }

   /**
    * Implementation of the {@link javax.microedition.lcdui.ItemStateListener 
    * ItemStateListener} interface.  Used to update the form when a new date is 
    * selected.
    *
    * @param item the {@link javax.microedition.lcdui.Item Item} that has 
    * changed.
    */
    public void itemStateChanged(Item item) {
        if( item == midlet.getUseDateField() ) {
            setDate(((javax.microedition.lcdui.DateField)item).getDate());
        } else if ( item == midlet.getFontChoiceGroup() ) {
           String fontName="";
           ChoiceGroup fontChoice = (ChoiceGroup) item;
           boolean reorder = true;
           
           switch( fontChoice.getSelectedIndex() ) {
              case 0:   // Default
                 reorder = false;
                 // Intentional fall-through
                 
              case 1:   // Default (reversed)
                 font = new MidpFontAdapterStrategy();
                 fontName = "Native";
                 break;
                 
              case 2:
                 fontName = "nachlieli-14";
                 break;
                 
              case 3:
                 fontName = "nachlieli-20";
                 break;
//                 
//              case 4:
//                 fontName = "miriam-22";
//                 break;
           }
           
           if ( font == null || font.getFontName().compareTo(fontName)!=0 )
           {
              Logger.log("Loading font " + fontName + '\n');
              // Only use ImageFontStrategy for non-native fonts
              if ( fontName.compareTo("Native") != 0 )
              {
                 this.font = new ImageFontStrategy(fontName);
              }
           }
           this.reorder = reorder;
        }
    }
    
   /**
    * Chooses the text (e.g. tefillah) to display.
    * @param textNum The ID of the text to display.  Valid options are the constants 
    * contained in TefillaReader (e.g. SHACHARIT, MINCHA, MAARIV, and 
    * OTHER).
    */
    public void chooseText(final int textNum) {
        super.chooseText( textNum);
        if ( midlet.getDaavenCanvas() != null ) {
            midlet.getDaavenCanvas().resetMainText();
        }
        if ( this.getTefillaReader().getTextLength() < 50 && textNum != -1 )
        {
           Display.getDisplay(midlet).callSerially( new Runnable() {
              public void run() {
                 //System.out.println("Running a new alert");
                 Alert a = new Alert("Error loading text", 
                         "There was an error loading the tefilla!", 
                         null,
                         AlertType.ERROR);
                 a.setTimeout(2000);
                 Display d = Display.getDisplay(midlet);
                 Displayable form = midlet.get_MainForm();
                 if ( d != null && form != null )
                 {
                    d.setCurrent(a, form);
                 }
              }
           });
        }
    }

   /**
    * Sets the current data.  Keeps the English date, English Calendar, and 
    * Hebrew date in sync
    * @param date The civil date
    */
    public void setDate(Date date) {
        super.setDate(date);
        
        midlet.getUseDateField().setDate(date);

        // Update choice
        int hour = getEnglishCalendar().get(Calendar.HOUR_OF_DAY);
        Logger.log("Current hour of day is: " + hour + '\n');
        midlet.getTefillaChoiceGroup().setSelectedFlags(new boolean[] {
            hour >= 6 && hour < 12,
            hour >= 12 && hour < 19,
            hour >= 19 || hour < 6,
            false
        });
        
        TextFunctions functions = getTextFunctions();
            
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < functions.getNumFunctions(); i++) {
            sb.append(functions.getFunctionLabel(i));
            sb.append("=");
            sb.append(functions.evalFunction(i));
            sb.append('\n');
        }

        midlet.get_choiceString().setText(sb.toString());
        
        midlet.get_hebrewDateString().setText(getHebrewDate().getHebrewDateAsString());

    }
  
  /**
   * Appends a string to the log
   * @param s The string to add
   */
  public void appendLog(String s) {
        StringItem si = midlet.get_debugOutput();
        final String prestring = si.getText();
        if ( s != null ) {
            try {
                si.setText(prestring + s);
            } catch (IllegalArgumentException ile) {
                // Reached maximum length, start again
                si.setText(s);
            }
        } else {
            si.setText(s);
        }
    }
  
  public FontStrategy getFont()
  {
     return font;
  }
  
  public void setFont( FontStrategy font )
  {
     this.font = font;
  }

   public boolean isReordered() {
      return reorder;
   }
   
   public int getDefaultFontIndex() {
      int result;
      
      // Expected to be null; not null in me4se applets
      String version = System.getProperty("java.version");
      System.out.println("version=" + version);
      String locale = "";

      // version is not null on me4se applets, where the following call creates
      // an AccessControlException and halts the applet
      if ( version == null )
      {
         Logger.log("Retrieving locale\n");
         locale = System.getProperty("microedition.locale");
      }
      else
      {
         Logger.log("Skipping locale retrieval\n");
      }

      if ( locale != null && locale.startsWith("he") )
      {
         result = 1;
      }
      else
      {
         final int height = Font.getDefaultFont().getHeight();
         
         if ( height < 19) // halfway between 14 and 22
         {
            result = 2;
         }
         else
         {
            result = 3;
         }
      }
      
      return result;
   }

    /**
     * Sets all references to null.  Needed to ensure that memory is released 
     * back to system for PalmOS.  See post at: 
     * http://news.palmos.com/read/messages?id=204129
     */
    public void releaseReferences() {
       super.releaseReferences();
       if ( font != null ) { font.releaseReferences(); }
       this.font = null;
       if ( midlet != null ) { midlet.releaseReferences(); }
    }
}
