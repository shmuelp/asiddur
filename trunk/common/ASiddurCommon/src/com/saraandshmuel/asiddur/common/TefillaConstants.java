/*
 * TefillaConstants.java
 *
 * Created on December 25, 2006, 4:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

/**
 * The constants used in the binary representation of the tefilla
 *
 * TODO: Document the format better
 *
 * @author shmuelp
 */
public class TefillaConstants {
   
   public static final byte[] HEADER = new byte[] {
      (byte) 0,
      (byte) 0xa5,
      (byte) 0x1d,
      (byte) 0xd8,
      (byte) 0x7b 
   };
   
   // Format: 01 <target text block ID (short)> <language code (UTF String)> <name (UTF String)
   public static final byte TEFILLA_NAME = 1;
   
   // Format: 02 <number of blocks (short)> <position of text block 1 (int)> <position of text block 2 (int)> ... <position of text block n (int)>
   // int in Java means 4 bytes (= long in C++)
   public static final byte TEXT_POSITION = 2;
   
   // Format: 03 <text block ID (short)> <text block data (cp-1255 string)> <0 (byte)>
   public static final byte TEXT_BLOCK = 3;
   public static final byte TEXT_BLOCK_END = 0;
   
   // Format: 10
   public static final byte TEXT_BR = 0x10;
   
   // Format: 11
   public static final byte TEXT_NAVMARK = 0x11;

   // Format: 12
   public static final byte TEXT_P = 0x12;

   // Format: 13 <variable number (byte)>
   public static final byte TEXT_GET = 0x13;

   // Format: 14 <# of bytes to jump (short)>
   public static final byte TEXT_JUMP = 0x14;
   
   // Format: 15 <id of text block to include (short)>
   public static final byte TEXT_INCLUDE = 0x15;
   
   // Format: 16 <# of bytes to jump if function is false (short)> <id of the function to check (byte)> <text data (displayed if function is true)>
   public static final byte TEXT_IF_FUNCTION = 0x16;
   
   // Format: 17 <# of bytes to jump if function is false (short)> <variable number (byte)> <text data (displayed if function is true)>
   public static final byte TEXT_IF_VARIABLE = 0x17;
   
   // Format: 18 <variable number (byte)> <number of bytes of variable text (short)> <text data>
   public static final byte TEXT_SET = 0x18;

   // Format: 19 <# of bytes to jump if function is false (short)> <day number (byte)> <text data (displayed if function is true)>
   public static final byte TEXT_IF_DAY = 0x19;

   // Format: 1A <# of bytes to jump if function is false (short)> <month number (byte)> <text data (displayed if function is true)>
   public static final byte TEXT_IF_MONTH = 0x1A;

   // Format: 1B <# of bytes to jump if function is false (short)> <# of functions to check (byte)> <FUNCTION 1> <FUNCTION 2> ... <FUNCTION n> <text data (displayed if function is true)>
   public static final byte TEXT_IF_OR = 0x1B;
}
