/*
 * LogAlertAdapter.java
 *
 * Created on August 28, 2006, 11:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.midpui;

import javax.microedition.lcdui.Alert;
import com.saraandshmuel.asiddur.common.AppendLog;
import com.saraandshmuel.asiddur.common.Logger;

/**
 *
 * @author shmuelp
 */
public class LogAlertAdapter implements AppendLog {
   
   private Alert alert;
   
   final private String prefix = "ASiddur is loading...\n";
   
   /** Creates a new instance of LogAlertAdapter */
   public LogAlertAdapter( Alert alert ) {
      this.alert = alert;
      Logger.setLog(this);
   }

   public void appendLog(String s) {
      alert.setString( prefix + s );
   }

   public void releaseReferences() {
      alert = null;
   }
}
