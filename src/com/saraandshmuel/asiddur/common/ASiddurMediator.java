/*
 * ASiddurMediator.java
 *
 * Created on March 13, 2006, 4:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

/**
 * ASiddurMediator is an interface that allows communication between different 
 * components of the ASiddur application.  It plays the role of the 
 * <CODE>Mediator</CODE> interface in the <CODE>Mediator</CODE> design pattern.
 * As such, the various components in the application communicate only with the 
 * mediator, which forwards to the appropriate objects.  The different 
 * components are then decoupled.
 * @author shmuelp
 */
public abstract class ASiddurMediator implements AppendLog {
    
    /** Creates a new instance of ASiddurMediator */
    public ASiddurMediator() {
        hebrewDate.setIsraeli(false);
        setHebrewDate(englishDate);
        textFunctions = new TextFunctions(this);
        tefillaReader = new TefillaReader(this);
    }
    
   /**
    * The current civil date
    */
    private java.util.Date englishDate = new java.util.Date();
    
   /**
    * The current Civil calendar
    */
    private java.util.Calendar englishCalendar = java.util.Calendar.getInstance();
    
   /**
    * The current Date in the Hebrew calendar
    */
    private avrom.util.RegularHebrewDate hebrewDate = new avrom.util.RegularHebrewDate();
    
   /**
    * The current TefillaReader
    */
    private TefillaReader tefillaReader = null;
    
   /**
    * A TextFunctions object
    */
    private TextFunctions textFunctions = null;
    
   /**
    * Returns the text function labels
    * @return An array containing the label of every function at the point of 
    * its ID
    */
    public String[] getTextLabels() {
        return tefillaReader.getTextNames();
    }

   /**
    * Returns the current English date
    * @return the current English date
    */
    public final java.util.Date getEnglishDate() {
        return englishDate;
    }

   /**
    * Sets the current data.  Keeps the English date, English Calendar, and 
    * Hebrew date in sync
    *
    * @param englishDate The date to set
    */
    public void setDate(java.util.Date englishDate) {
        this.englishDate = englishDate;
        englishCalendar.setTime(englishDate);
        setHebrewDate(englishDate);
    }

   /**
    * Gets the current Hebrew date
    * 
    * @return The current Hebrew date
    */
    public final avrom.util.RegularHebrewDate getHebrewDate() {
        return hebrewDate;
    }

   /**
    * Sets the Hebrew date.
    * @param date The new English date
    * @note this function is currently only used by the setDate function and is
    * not yet intended to be used to set the English and Hebrew dates.  That 
    * functionality will be added if needed
    */
    private void setHebrewDate(java.util.Date date) {
        hebrewDate.setDate(englishCalendar);
    }

   /**
    * Chooses the text (e.g. tefillah) to display
    * @param textNum The ID of the text to display.  Valid options are the constants 
    * contained in TefillaReader (e.g. SHACHARIT, MINCHA, MAARIV, and 
    * OTHER).
    */
    public void chooseText(final int textNum) {
        tefillaReader.setText( textNum);
    }

   /**
    * Gets the current English Calendar
    * @return the current English Calendar
    */
    public final java.util.Calendar getEnglishCalendar() {
        return englishCalendar;
    }
    
    /**
     * Gets a reference to a TextFunctions object
     * @return a TextFunctions object
     */
    public final TextFunctions getTextFunctions() {
        return textFunctions;
    }

   /**
    * Gets a tefilla reader
    * @return a TefillaReader object
    */
    public TefillaReader getTefillaReader() {
        return tefillaReader;
    }
    
    /**
     * Sets all references to null.  Needed to ensure that memory is released 
     * back to system for PalmOS.  See post at: 
     * http://news.palmos.com/read/messages?id=204129
     */
    public void releaseReferences() {
       this.englishCalendar = null;
       this.englishDate = null;
       this.hebrewDate = null;
       tefillaReader.releaseReferences();
       this.tefillaReader = null;
       textFunctions.releaseReferences();
       this.textFunctions = null;
    }
}
