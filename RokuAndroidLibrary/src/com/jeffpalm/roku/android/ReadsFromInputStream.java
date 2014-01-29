package com.jeffpalm.roku.android;

import java.io.InputStream;

/**
 * An interface for reading an object from an input stream.
 * 
 * @param <T>
 */
public interface ReadsFromInputStream<T> {
  T fromInputStream(InputStream in) throws Exception;
}
