package com.ktc.actor;

import java.util.UUID;

public class DocumentResult implements DocumentCommand {
  private final UUID id;
  private Exception exception;

  public DocumentResult(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public Exception getException() {
    return exception;
  }

  public void setException(Exception exception) {
    this.exception = exception;
  }
}
