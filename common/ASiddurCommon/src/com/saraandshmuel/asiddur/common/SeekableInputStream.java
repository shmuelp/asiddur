/*
 * SeekableInputStream.java
 *
 * Created on January 9, 2007, 10:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author shmuelp
 */
public class SeekableInputStream extends InputStream {
   
   private InputStreamFactory factory;
   
   private InputStream inputStream;
   
   private long currentPosition = 0;
   
   private long markPosition = -1;
   
   /** Creates a new instance of SeekableInputStream */
   public SeekableInputStream( InputStreamFactory factory ) {
      this.factory = factory;
      this.inputStream = factory.createInputStream();
   }

   public int read() throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      int result = inputStream.read();
      ++currentPosition;
      return result;
   }

   public boolean markSupported() {
      return true;
   }

   public int read(byte[] b) throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      int result = inputStream.read(b);
      currentPosition += result;
      return result;
   }

   public int read(byte[] b, int off, int len) throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      int result = inputStream.read(b, off, len);
      currentPosition += result;
      return result;
   }

   public long skip(long n) throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      long result = 0;
      // Set to non-zero value to ensure that loop is entered
      long lastSkip = 1;
      
      while ( result < n && lastSkip != 0 ) {
         lastSkip = inputStream.skip(n-result);
         result += lastSkip;
      }
      
      // read in a loop to get any remaining bytes
      while ( result < n ) {
         inputStream.read();
         ++result;
      }
      
      return result;
   }

   public void mark(int readlimit) {
      if ( inputStream.markSupported() ) {
         inputStream.mark(readlimit);
      } else {
         markPosition = currentPosition;         
      }
   }

   public void reset() throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      if ( inputStream.markSupported() ) {
         inputStream.reset();
      } else {
         seek(markPosition);
      }
   }

   public void seek( long pos ) throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      if ( pos < 0 ) {
         throw new IOException("Cannot seek a negative distance");
      }
      
      if ( pos > currentPosition ) {
         skip( pos - currentPosition );
      } else if ( pos < currentPosition ) {
         inputStream = factory.createInputStream();
         currentPosition = 0;
         skip( pos );
      }
   }
}
