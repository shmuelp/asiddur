/*
 * Logger.java
 *
 * Created on February 27, 2006, 2:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

/**
 * Logger presents an interface to use and set a logger (with the AppendLog 
 * interface).  It allows code to log without checking for null each time.  If 
 * a log has been registered via the setLog function, it will be used.  
 * Otherwise, the log mesage will simply be dropped.
 * @author shmuelp
 */
public class Logger {
    
   
   /**
    * The current log interface
    */
    private static AppendLog log;
    
    /**
    * Default constructor declared private to prevent instantiation of Logger.  
    * The static setLog and log functions should be used instead.
    */
    private Logger() {
    }
    
   /**
    * Sets a new log interface
    * @param log The AppendLog interface to set it to
    */
    static public void setLog( AppendLog log ) {
        Logger.log = log;
    }
    
   /**
    * Logs a string.  It correctly handles the case of a null AppendLog interface.
    * @param s The string to log
    */
    static public void log( String s ) {
        if ( log != null ) {
            log.appendLog(s);
        }
    }
}
