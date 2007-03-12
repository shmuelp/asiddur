/*
 * SeekableInputStream.java
 *
 * Created on January 9, 2007, 10:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author shmuelp
 */
public class SeekableInputStream extends InputStream 
        /* implements DataInput */
{
   
   private InputStreamFactory factory;
   
   private DataInputStream inputStream;
   
   private long currentPosition = 0;
   
   /** Creates a new instance of SeekableInputStream */
   public SeekableInputStream( InputStreamFactory factory ) {
      this.factory = factory;
      this.inputStream = new DataInputStream( factory.createInputStream() );
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
      return false;
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
//      System.out.println("SeekableInputStream.skip(" + n + ") - currently at " +
//              currentPosition);
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      long result = 0;
      // Set to non-zero value to ensure that loop is entered
      long lastSkip = 1;
      
      while ( result < n && lastSkip != 0 ) {
         lastSkip = inputStream.skip(n-result);
         result += lastSkip;
//         System.out.println("Skipped " + lastSkip);
      }
      
      // read in a loop to get any remaining bytes
      while ( result < n ) {
         inputStream.read();
         ++result;
      }
      
      return result;
   }

   public void seek( long pos ) throws IOException {
//      System.out.println("SeekableInputStream.seek(" + pos + ")");
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      if ( pos < 0 ) {
         throw new IOException("Cannot seek a negative distance");
      }
      
//      if ( pos > currentPosition ) {
//         skip( pos - currentPosition );
//      } else if ( pos < currentPosition ) {
         inputStream = new DataInputStream( factory.createInputStream() );
         currentPosition = 0;
         skip( pos );
//      }
   }

   public void mark(int readlimit) {
      // Do nothing
   }

   public void reset() throws IOException {
      // Do nothing
   }

   public boolean readBoolean() throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      
      ++currentPosition;
      return inputStream.readBoolean();
   }

   public byte readByte() throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      
      ++currentPosition;
      return inputStream.readByte();
   }

//   public char readChar() throws IOException {
//      if ( inputStream == null ) {
//         throw new IOException("Underlying input stream is null!");
//      }
//
//      // How to know the size of the char? It depends on what it was!
//   }

   public void readFully(byte[] b) throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      
      inputStream.readFully(b);
      currentPosition += b.length;
   }

   public void readFully(byte[] b, int i, int i0) throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      
      inputStream.readFully(b,i,i0);
      currentPosition += i0;
   }

   public int readInt() throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      
      currentPosition += 4;
      return inputStream.readInt();
   }

   public long readLong() throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      
      currentPosition += 8;
      return inputStream.readLong();
   }

   public short readShort() throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      
      currentPosition += 2;
      return inputStream.readShort();
   }

   public String readUTF() throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      
      String result = inputStream.readUTF();
      currentPosition += result.getBytes().length;

      return result;
   }

   public int readUnsignedByte() throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      
      ++currentPosition;
      return inputStream.readUnsignedByte();
   }

   public int readUnsignedShort() throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      
      currentPosition += 2;
      return inputStream.readUnsignedShort();
   }

   public int skipBytes(int i) throws IOException {
      if ( inputStream == null ) {
         throw new IOException("Underlying input stream is null!");
      }
      
      int result = inputStream.skipBytes(i);
      currentPosition += result;
      return result;
   }

}
