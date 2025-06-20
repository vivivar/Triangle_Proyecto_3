/*
 * @(#)SourceFile.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package Triangle.SyntacticAnalyzer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;


public class SourceFile {

    private javax.swing.JEditorPane llvmPane;
    private javax.swing.JScrollPane llvmScroll;
    private BufferedReader charSource;

    private int currentPos;

  public static final char EOL = '\n';
  public static final char EOT = '\u0000';

  java.io.File sourceFile;
  java.io.InputStream source;
  int currentLine;
  
  private BufferedReader reader;
  private boolean eof = false;

  public SourceFile(String filename) {
    try {
      sourceFile = new java.io.File(filename);
      source = new java.io.FileInputStream(sourceFile);
      currentLine = 1;
    }
    catch (java.io.IOException s) {
      sourceFile = null;
      source = null;
      currentLine = 0;
    }
  }
  
  public SourceFile(java.io.InputStream inputStream) {
    try {
        sourceFile = null;
        source = inputStream;
        currentLine = 1;
    } catch (Exception e) {
        source = null;
        currentLine = 0;
    }
}

  char getSource() {
    try {
      int c = source.read();

      if (c == -1) {
        c = EOT;
      } else if (c == EOL) {
          currentLine++;
      }
      return (char) c;
    }
    catch (java.io.IOException s) {
      return EOT;
    }
  }

  int getCurrentLine() {
    return currentLine;
  }


    private void reportError(String message) {
        System.err.println(message);
    }
  
    public SourceFile(java.io.Reader reader) {
    try {
        charSource = new BufferedReader(reader); // sin declarar de nuevo
        currentLine = 1; // no ""
        currentPos = -2;
    } catch (Exception e) {
        System.err.println("Error al leer desde fuente de texto en memoria.");
    }
}


}
