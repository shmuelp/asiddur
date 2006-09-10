/*
 * appendLog.java
 *
 * Created on February 27, 2006, 12:18 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

/**
 * A simple, append-only log interface
 * @author shmuelp
 */
public interface AppendLog {
    
   /**
    * Appends a string to the log
    * @param s The string to add
    */
    public void appendLog( String s );
    /**
     * Sets all references to null.  Needed to ensure that memory is released 
     * back to system for PalmOS.  See post at: 
     * http://news.palmos.com/read/messages?id=204129
     */
    public void releaseReferences();
}
