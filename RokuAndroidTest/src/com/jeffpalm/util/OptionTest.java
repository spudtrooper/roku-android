package com.jeffpalm.util;

import junit.framework.TestCase;

public class OptionTest extends TestCase {

  public void testFromSuccess() {
    Option o = Option.fromSuccess("A");
    assertEquals("A", o.getSuccess());
    assertNull(o.getFailure());
    assertTrue(o.isSuccess());
  }

  public void testFromFailure() {
    Option o = Option.fromFailure("B");
    assertEquals("B", o.getFailure());
    assertNull(o.getSuccess());
    assertFalse(o.isSuccess());
  }

}
