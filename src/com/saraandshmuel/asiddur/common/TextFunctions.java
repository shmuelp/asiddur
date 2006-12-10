/*
 * textFunctions.java
 *
 * Created on March 24, 2006, 4:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

import avrom.util.HebrewDate;
import java.util.Calendar;
import avrom.util.RegularHebrewDate;

/**
 * TextFunctions contain the functions that are available to dynamically 
 * generate text.  It is used to display only the appropriate parts of a tefilla.
 * @author shmuelp
 * @note design is a bit ugly, but avoided fancy patterns due to performance 
 * (lots of function calls / object has a relatively high overhead on J2ME).
 * These functions will be used a lot in building the text to be displayed 
 */
public class TextFunctions {
    
   /**
    * Is the current day Rosh Chodesh?
    */
    private static final int IS_ROSH_CHODESH = 0;
   /**
    * Is it Sunday?
    */
    private static final int IS_SUNDAY = 1;
   /**
    * Is it Monday?
    */
    private static final int IS_MONDAY = 2;
   /**
    * Is it Tuesday?
    */
    private static final int IS_TUESDAY = 3;
   /**
    * Is it Wednesday?
    */
    private static final int IS_WEDNESDAY = 4;
   /**
    * Is it Thursday?
    */
    private static final int IS_THURSDAY = 5;
   /**
    * Is it Friday?
    */
    private static final int IS_FRIDAY = 6;
   /**
    * Is it Shabbos?
    */
    private static final int IS_SHABBOS = 7;
   /**
    * Is it a fast day?
    */
    private static final int IS_FAST_DAY = 8;
   /**
    * Is the current day in the Aseret Yimay Teshuva?
    */
    private static final int IN_ASERET_YIMAY_TESHUVA = 9;
   /**
    * Is it between Sukkot and Pesach?  (E.g. say "Mashiv HaRuach")
    */
    private static final int SUKKOT_TO_PESACH = 10;
   /**
    * Is it between Pesach and Sukkot? (e.g. don't say "Mashiv HaRuach" or 
    * say "Morid HaTal").
    */
    private static final int PESACH_TO_SUKKOT = 11;
   /**
    * Is it between Dec. 4th (or 5th in a leap year) and Pesach?  (e.g. mention tal)
    */
    private static final int SAY_TAL_UMATAR = 12;
   /**
    * Say Yaaleh v'yavo?  (E.g. Rosh Chodesh, Chol HaMoed)
    */
    private static final int SAY_YAALEH_VYAVOH = 13;
   /**
    * Is it currently Sukkot?
    */
    private static final int IS_SUKKOT = 14;
   /**
    * Is it currently Pesach?
    */
    private static final int IS_PESACH = 15;
   /**
    * Is it Channuka?
    */
    private static final int IS_CHANNUKAH = 16;
   /**
    * Is it Purim?
    */
    private static final int IS_PURIM = 17;
   /**
    * Should Tachanun be said?
    */
    private static final int SAY_TACHANUN = 18;
   /**
    * Is is between rosh chodesh Elul and Hoshana rabbah?
    */
    private static final int SAY_LDAVID = 19;

   /**
    * The names of the functions
    */
    private String[] functionNames = new String[] {
        "isRoshChodesh",
        "isSunday", 
        "isMonday",
        "isTuesday",
        "isWednesday",
        "isThursday",
        "isFriday",
        "isShabbos",
        "isFastDay",
        "inAseretY'mayTeshuva",
        "betweenSukkotAndPesach",
        "betweenPesachAndSukkot",
        "sayTalUmatar",
        "sayYa'alehV'yavoh",
        "isSukkot",
        "isPesach",
        "isChannukah",
        "isPurim",
        "sayTachanun",
        "sayL'david"
    };
    
   /**
    * Reference to the current mediator
    */
    private ASiddurMediator mediator;
    
    /**
    * Creates a new instance of textFunctions
    * @param mediator The mediator of the current output
    */
    public TextFunctions(ASiddurMediator mediator) {
        this.mediator = mediator;
    }
    
    /**
     * Evaluates a function.
     * @param functionId the ID of a the function to run
     * @param month the Hebrew month
     * @param day the Hebrew day
     * @return true if the function is true, false otherwise
     */
    public boolean evalFunction( int functionId, int month, int day ) {
        boolean result;
        //RegularHebrewDate hebrewDate = mediator.getHebrewDate();
        //int month = hebrewDate.getHebrewMonth();
        //int day = hebrewDate.getHebrewDate();
        
        switch ( functionId ) {
            case IS_ROSH_CHODESH:
                result = ( day == 1 || day == 30 );
                break;
                
            case IS_SUNDAY:      // intentional pass-through
            case IS_MONDAY:      // intentional pass-through
            case IS_TUESDAY:     // intentional pass-through
            case IS_WEDNESDAY:   // intentional pass-through
            case IS_THURSDAY:    // intentional pass-through
            case IS_FRIDAY:      // intentional pass-through
            case IS_SHABBOS:
                result = ( functionId == mediator.getEnglishCalendar().get(Calendar.DAY_OF_WEEK) );
                break;
                
            case IS_FAST_DAY:
                if ( ( month == 4 && day == 17 ) ||
                     ( month == 5 && day == 9 ) ||
                     ( month == 7 && day == 3 ) ||
                     ( month == 7 && day == 10 ) ||
                     ( month == 10 && day == 10 ) ||
                     ( month == mediator.getHebrewDate().getLastMonthOfHebrewYear() && day == 13 ) ) {
                         result = true;
                } else {
                    result = false;
                }
                break;
                
            case IN_ASERET_YIMAY_TESHUVA:
                if ( month==7 && day < 10 ) {
                    result = true;
                } else {
                    result = false;
                }
                break;
                
            case PESACH_TO_SUKKOT:
                if ( month > 1 && month < 7 ) {
                    result = true;
                } else if ( month > 7 ) {
                    result = false;
                } else if ( month == 1 ) {
                    if ( day < 15 ) {
                        result = false;
                    } else {
                        result = true;
                    }
                } else { // month is 7
                    if ( day < 22 ) {
                        result = true;
                    } else {
                        result = false;
                    }
                }
                break;

            case SUKKOT_TO_PESACH:
                if ( month > 1 && month < 7 ) {
                    result = false;
                } else if ( month > 7 ) {
                    result = true;
                } else if ( month == 1 ) {
                    if ( day < 15 ) {
                        result = true;
                    } else {
                        result = false;
                    }
                } else { // month is 7
                    if ( day < 22 ) {
                        result = false;
                    } else {
                        result = true;
                    }
                }
                break;
                
            case SAY_TAL_UMATAR:
                // diaspora rules
                // TODO!!!
                if ( month == 1 && day < 15 ) {
                    result = true;
                } else if ( month < 7 ||
                            ( month == 7 && day <= 22 ) ) {
                    result = false;
                } else {
                    Calendar engCal= mediator.getEnglishCalendar();
                    int engYear = engCal.get(Calendar.YEAR) + 1;
                    boolean engLeap = ( engYear % 4 == 0 && ( engYear % 100 != 0 || engYear % 1000 == 0 ) );
                    Calendar cutoff = Calendar.getInstance();
                    cutoff.set(Calendar.MONTH, 12);
                    cutoff.set(Calendar.DAY_OF_MONTH, engLeap ? 5 : 4 );
                    HebrewDate cutoffDate = new HebrewDate(cutoff);
                    if ( mediator.getHebrewDate().compareTo(cutoffDate) < 0 ) {
                        result = false;
                    } else {
                        result = true;
                    }
                }
                break;
                
            case SAY_YAALEH_VYAVOH:
                if ( day == 1 || 
                     day == 30 ||
                     ( month == 1 && ( day >=15 && day <= 22 ) ) ||
                     ( month == 3 && ( day == 6 || day == 7 ) ) ||
                     ( month == 7 && ( day >=15 && day <= 23 ) ) ) {
                    result = true;
                } else {
                    result = false;
                }
                break;
                
            case IS_SUKKOT:
                if ( month == 7 && ( day >= 15 || day <= 23 ) ) {
                    result = true;
                } else {
                    result = false;
                }
                break;
                
            case IS_PESACH:
                if ( month == 1 && ( day >= 15 || day <= 22) ) {
                    result = true;
                } else {
                    result = false;
                }
                break;
                
            case IS_CHANNUKAH:
                if ( month == 9 && day >= 25 ) {
                    result = true;
                } else if ( month == 10 ) {
                    if ( day <= 2 ) {
                        result = true;
                    } else if ( day ==3 && mediator.getHebrewDate().getLastDayOfHebrewMonth(9)==29 ) {
                        result = true;
                    } else {
                        result = false;
                    }
                } else {
                    result = false;
                }
                break;
                
            case IS_PURIM:
                if ( month == mediator.getHebrewDate().getLastMonthOfHebrewYear() && day == 14 ) {
                    result = true;
                } else {
                    result = false;
                }
                break;
                
            case SAY_TACHANUN:
                // TODO!!!!
                result = true;
                break;
                
            case SAY_LDAVID:
                if ( month == 6 || 
                     ( month== 7 && day <= 21 ) ) {
                    result = true;
                } else {
                    result = false;
                }
                break;
                
            default:
                result = false;
        }
        
        return result;
    }
    
    /**
     * Returns the name of a function.  Can be used to build the full list of 
     * functions at run-time
     *
     * @param functionId the ID of the function
     * @return the name of the function
     */
    public String getFunctionLabel( int functionId ) {
        String result = "";
        
        if ( functionId >= 0 && functionId < functionNames.length ) {
            result = functionNames[functionId];
        }
        
        return result;
    }
    
    /**
     * Returns the total number of functions
     *
     * @return The number of functions
     */
    public int getNumFunctions() {
        return functionNames.length;
    }
    
    public String getReminderString() {
       StringBuffer result = new StringBuffer();
       
       RegularHebrewDate hebrewDate = mediator.getHebrewDate();
       int month = hebrewDate.getHebrewMonth();
       int day = hebrewDate.getHebrewDate();

        Calendar engCal= mediator.getEnglishCalendar();
        int engYear = engCal.get(Calendar.YEAR) + 1;
        int engMonth = engCal.get(Calendar.MONTH);
        int engDay = engCal.get(Calendar.DAY_OF_MONTH);
        boolean engLeap = ( engYear % 4 == 0 && ( engYear % 100 != 0 || engYear % 1000 == 0 ) );
        if ( engMonth == Calendar.DECEMBER && 
             engDay <= 19 && 
             engDay >= (engLeap ? 5 : 4) ) {
           result.append("V'Sein Tal Umatar");
        }
       
        
       if ( ( month == 7 && day >= 22 ) ||
            ( month == 8 && day <= 7 ) ) {
          result.append("Mashiv Haruach / V'Sein B'racha ");
       }
       
       if ( evalFunction(IS_FAST_DAY, month, day) ) {
          result.append("Aneinu");
       }
       
       if ( evalFunction(IN_ASERET_YIMAY_TESHUVA, month, day) ) {
          result.append("Hemelech Hakadosh ");
       }
       
       if ( evalFunction(IS_CHANNUKAH, month, day)  || 
            evalFunction(IS_PURIM, month, day)) {
          result.append("Al Hanisim ");
       }
       
       if ( evalFunction(SAY_YAALEH_VYAVOH, month, day) ) {
          result.append("Ya'aleh V'Yavo ");
       }
        
       return result.toString();
    }

    /**
     * Sets all references to null.  Needed to ensure that memory is released 
     * back to system for PalmOS.  See post at: 
     * http://news.palmos.com/read/messages?id=204129
     */
    public void releaseReferences() {
       this.functionNames = null;
    }
}
