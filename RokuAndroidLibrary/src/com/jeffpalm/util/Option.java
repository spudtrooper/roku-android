package com.jeffpalm.util;

/**
 * A result that have a success value and a failure value.
 * 
 * @param <A>
 *          Success type
 * @param <B>
 *          Failure type
 */
public class Option<A, B> {

  private final A success;
  private final B failure;

  private Option(A success, B failure) {
    assert (success != null || failure != null);
    this.success = success;
    this.failure = failure;
  }

  public A getSuccess() {
    return success;
  }

  public B getFailure() {
    return failure;
  }

  public boolean isSuccess() {
    return success != null;
  }

  public static <A, B> Option<A, B> fromSuccess(A success) {
    return new Option<A, B>(success, null);
  }

  public static <A, B> Option<A, B> fromFailure(B failure) {
    return new Option<A, B>(null, failure);
  }
}
