/*
 * BasicData.java
 *
 * Created on January 1, 2007, 12:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tefillaconverter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author shmuelp
 */
public class BasicData implements TefillaData {
   
   private ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
   
   private DataOutputStream dataOut = new DataOutputStream(outputBuffer);
   
   private OutputStreamWriter writerOut = new OutputStreamWriter(dataOut, encodingCharset);
   
   /** Creates a new instance of BasicData */
   public BasicData() {
   }

   public byte[] getBytes() {
      return outputBuffer.toByteArray();
   }

   public void writeByte(byte b) {
      try {
         dataOut.writeByte(b);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void writeBytes(byte[] b) {
      try {
         dataOut.write(b);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void writeInt(int i) {
      try {
         dataOut.writeInt(i);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void writeShort(short s) {
      try {
         dataOut.writeShort(s);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void writeString(String s) {
      try {
         writerOut.write(s);
         writerOut.flush();
      } catch (java.io.UnsupportedEncodingException ex) {
         ex.printStackTrace();
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

}
