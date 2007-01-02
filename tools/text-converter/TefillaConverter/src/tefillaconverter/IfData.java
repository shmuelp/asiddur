/*
 * IfData.java
 *
 * Created on January 1, 2007, 12:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tefillaconverter;

import com.saraandshmuel.asiddur.common.TefillaConstants;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 *
 * @author shmuelp
 */
public class IfData implements TefillaData {
   
   private ByteArrayOutputStream thenArray = new ByteArrayOutputStream();
   private DataOutputStream thenStream = new DataOutputStream(thenArray);
   private OutputStreamWriter thenWriter = new OutputStreamWriter(thenStream, encodingCharset);
   private ByteArrayOutputStream elseArray = new ByteArrayOutputStream();
   private DataOutputStream elseStream = new DataOutputStream(elseArray);
   private OutputStreamWriter elseWriter = new OutputStreamWriter(elseStream, encodingCharset);

   private boolean afterElse = false;
   
   private byte headerType = 0;
   private byte headerValue = 0;

   /** Creates a new instance of IfData */
   public IfData() {
   }

   public void setAfterElse(boolean afterElse) {
      this.afterElse = afterElse;
   }

   public void setHeaderType(byte headerType) {
      this.headerType = headerType;
   }

   public void incrementHeaderValue() {
      ++headerValue;
   }
   
   public void setHeaderValue(byte headerValue) {
      this.headerValue = headerValue;
   }

   public byte[] getBytes() {
      byte[] thenData = thenArray.toByteArray();
      byte[] elseData = elseArray.toByteArray();
      int resultSize = thenData.length + 4;
      if ( afterElse ) {
         resultSize += elseData.length + 3;
      }
      ByteArrayOutputStream array = new ByteArrayOutputStream(resultSize);
      DataOutputStream stream = new DataOutputStream(array);
      try {
         stream.writeByte(headerType);
         stream.writeShort(thenData.length);
         stream.writeByte(headerValue);
         stream.write(thenData);
         if (afterElse)
         {
            stream.writeByte(TefillaConstants.TEXT_JUMP);
            stream.writeShort(elseData.length);
            stream.write(elseData);
         }
      } catch (IOException ex) {
         ex.printStackTrace();
      }
      
      return array.toByteArray();
}

   public void writeByte(byte b) {
      try {
         if ( !afterElse ) {
            thenStream.writeByte(b);
         } else {
            elseStream.writeByte(b);
         }
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void writeBytes(byte[] b) {
      try {
         if ( !afterElse ) {
            thenStream.write(b);
         } else {
            elseStream.write(b);
         }
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void writeInt(int i) {
      try {
         if ( !afterElse ) {
            thenStream.writeInt(i);
         } else {
            elseStream.writeInt(i);
         }
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void writeShort(short s) {
      try {
         if ( !afterElse ) {
            thenStream.writeShort(s);
         } else {
            elseStream.writeShort(s);
         }
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public void writeString(String s) {
      try {
         if ( !afterElse )
         {
            thenWriter.write(s);
            thenWriter.flush();
         }
         else
         {
            elseWriter.write(s);
            elseWriter.flush();
         }
      } catch (java.io.UnsupportedEncodingException ex) {
         ex.printStackTrace();
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

}
