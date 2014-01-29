package com.jeffpalm.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class TestUtils {

  private final static File dataDir = new File("src/test/data");

  /**
   * Reads the contents of the file at <code>filePath</code> relative to the
   * data directory.
   * 
   * @throws IOException
   */
  public static String readDataFile(String filePath) throws IOException {
    File file = new File(dataDir, filePath);
    BufferedReader in = new BufferedReader(new FileReader(file));
    StringBuilder sb = new StringBuilder();
    String newline = System.getProperty("line.separator");
    try {
      String line;
      while ((line = in.readLine()) != null) {
        sb.append(line);
        sb.append(newline);
      }
    } finally {
      if (in != null) {
        in.close();
      }
    }
    return sb.toString();
  }

  /**
   * Opens the input stream for the file relative to the data directory.
   * 
   * @throws FileNotFoundException
   */
  public static InputStream open(String filePath) throws FileNotFoundException {
    File file = new File(dataDir, filePath);
    return new FileInputStream(file);
  }
}
