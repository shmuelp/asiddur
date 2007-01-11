/*
 * InputStreamFactory.java
 *
 * Created on January 9, 2007, 10:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.saraandshmuel.asiddur.common;

import java.io.InputStream;

/**
 * Interface to describe the creation of an InputStream.  Used by 
 * SeekableInputStream.
 *
 * @author shmuelp
 */
public interface InputStreamFactory {
   public InputStream createInputStream();
}
