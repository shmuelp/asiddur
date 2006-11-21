/*
 * Main.java
 *
 * Created on October 6, 2006, 12:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tefillaconverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author shmuelp
 */
public class Main {
   
   /** Creates a new instance of Main */
   public Main( URL input, OutputStream output ) {
      InputSource is = new InputSource();
      AsiddurTefillaAnnotatedHandler handler = 
              new AsiddurTefillaAnnotatedHandlerImpl();
      AsiddurTefillaAnnotatedParser parser = 
              new AsiddurTefillaAnnotatedParser( handler, null );
      
      try {
         parser.parse( input );
      }
      catch( SAXException saxe ) {
         System.err.println("Unable to parse input XML:");
         saxe.printStackTrace(System.err);
      }
      catch( ParserConfigurationException pce ) {
         System.err.println("Unable to parse input XML - invalid parser configuration: ");
         pce.printStackTrace(System.err);
      }
      catch( IOException ioe ) {
         System.err.println("Unable to parse input XML - IO error: ");
         ioe.printStackTrace(System.err);
      }
   }
   
   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      // TODO code application logic here
      if ( args.length != 2 ) {
         printUsage();
         System.exit(1);
      }
      
      String inputName = args[0];
      String outputName = args[1];
      URL inputURL;
      OutputStream output;

      try {
         inputURL = new File(inputName).toURL();
      }
      catch (IOException ioe) {
         System.err.println("Unable to open input file \'" + inputName + 
                 "\', aborting.");
         ioe.printStackTrace(System.err);
         System.exit(2);
         return;        // Ensure compiler knows that exception path terminates
      }

      try {
         output = new FileOutputStream(outputName);
      }
      catch (IOException ioe) {
         System.err.println("Unable to open output file \'" + inputName + 
                 "\', aborting.");
         ioe.printStackTrace(System.err);
         System.exit(2);
         return;        // Ensure compiler knows that exception path terminates
      }
      
      Main main = new Main( inputURL, output );
   }
   
   public static void printUsage() {
      System.out.println("Usage: TefillaConverter <input file> <output file>");
   }
}
