/*
 * FastCharVector.java
 *
 * Created on February 13, 2006, 6:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

/**
 * An time-efficient class containing an array of characters.  It is designed so 
 * that appending n elements, one at a time, will take O(log n) rather than O(n)
 * time.  This is done by doubling the capacity each time more memory is needed.
 * It also allows the memory to be trimmed to the size actually needed.
 * 
 * In C++ or Java 5, this class would be generic.
 *
 * @note This implementation is not thread-safe.
 * @author shmuelp
 */
public class FastCharVector {
    
    /** 
     * Creates a new instance of FastCharVector with a default capacity.
     */
    public FastCharVector() {
    }

   /**
    * Creates a new FastCharVector with the specified initial capacity
    * @param capacity The initial capacity
    */
    public FastCharVector(int capacity) {
        if ( capacity < 1 ) {
            capacity = 1;
        }
        this.capacity = capacity;
        this.data = new char[capacity];
    }
    
   /**
    * The current number of elements in the vector.
    */
    private int length = 0;

    /**
     * The current capacity (number of elements that can be stored without a new 
     * allocation.
     */
    private int capacity = 1;

   /**
    * Pointer to the internal array.
    */
    private char[] data = new char[1];

    /**
     * Returns the number of elements in the vector.
     *
     * @return The number of elements.
     */
    public int size() {
        return length;
    }
    
   /**
    * Returns the element at a given index.  Analagous to the [] operator.
    * @param index The element to retrieve
    * @throws java.lang.ArrayIndexOutOfBoundsException if the index is invalid or out of range
    * @return The requested element
    */
    public char elementAt( int index ) throws ArrayIndexOutOfBoundsException {
        if ( index < 0 || index >= length ) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        
        return data[index];
    }

    /**
    * Sets the value of a given index
    * @param element The value to set
    * @param index The index at which to set it
    * @throws java.lang.ArrayIndexOutOfBoundsException if the range is invalid or out of range
    */
    public void setElementAt( char element, int index ) throws ArrayIndexOutOfBoundsException {
        if ( index < 0 || index >= length ) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        
        data[index] = element;
    }

    /**
     * Appends a value to the vector to the end of the vector.  The vector will 
     * be resized if needed.  While individual calls to this function may be 
     * O(n) in the length of the array, in the aggragate, it will perform in
     * O(log n) time
     *
     * @param element the element to add
     */
    public void append( char element ) {
        //System.out.println("pre-append: length=" + length + ", capacity=" + capacity);
        if ( length+1 > capacity ) {
            grow();
        }
        
        this.data[length++] = element;
        //System.out.println("post-append: length=" + length + ", capacity=" + capacity);
    }
    
    /**
     * Utility function to grow the internal array.  The array size is doubled
     * To provide an aggregate O(log n) time.
     */
    private void grow() {
        //System.out.println("pre-grow: this.data.length=" + this.data.length );
        char[] newData = new char[capacity<<1];
        System.arraycopy(data, 0, newData, 0, length);
//        for( int i=0; i<this.length; ++i ) {
//            newData[i] = data[i];
//        }
        this.data = newData;
        capacity <<= 1;
        //System.out.println("post-grow: this.data.length=" + this.data.length );
    }

    /**
     * Shrink the internal array to the minimal size needed
     */
    public void shrinkToLength() {
        if ( length != capacity ) {
            char[] newData = new char[length];
            System.arraycopy(data, 0, newData, 0, length);
//            for( int i=0; i<this.length; ++i ) {
//                newData[i] = data[i];
//            }
            data = newData;
            capacity = length;
        }
    }
    
    /** 
     * Returns a string representation of the char array
     *
     * @return A string representing the character array
     */
    public String getString() {
        return new String(data);
    }
    
    /**
     * Returns a copy of the array
     *
     * @return a copy of the array
     */
    public char[] clone() {
        char[] ret = new char[length];
        System.arraycopy(data, 0, ret, 0, length);
//        for( int i=0; i<length; ++i ) {
//            ret[i] = data[i];
//        }
        return ret;
    }
    
    /**
     * Sets the array to a specific string
     *
     * @param s The new data
     */
    public void setChars( String s) {
        length = s.length();
        data = s.toCharArray();
    }
}
