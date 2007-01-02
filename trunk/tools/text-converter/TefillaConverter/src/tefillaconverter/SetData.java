/*
 * SetData.java
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

/**
 *
 * @author shmuelp
 */
public class SetData extends BasicData {
   
   private byte variableNum;
   
   /** Creates a new instance of SetData */
   public SetData() {
   }

   public void setVariableNum(byte variableNum) {
      this.variableNum = variableNum;
   }
   
   public byte[] getBytes() {
      byte[] innerResult = super.getBytes();

      ByteArrayOutputStream array = new ByteArrayOutputStream(innerResult.length+4);
      DataOutputStream result = new DataOutputStream(array);

      try {

         result.writeByte(TefillaConstants.TEXT_SET);
         result.writeByte(variableNum);
         result.writeShort(innerResult.length);
         result.write(innerResult);
      } catch (IOException ex) {
         ex.printStackTrace();
      }

      return array.toByteArray();
   }
   
}
