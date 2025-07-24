package com.ktc.actor;

import org.apache.pekko.actor.typed.ActorRef;

import com.ktc.text.DocumentBaseText;

public class DocumentRequest implements DocumentCommand {
  private final DocumentBaseText text;
  private final ActorRef<DocumentCommand> sender;

  public DocumentRequest(DocumentBaseText text, ActorRef<DocumentCommand> sender) {
    this.text = text;
    this.sender = sender;
  }

  public DocumentBaseText getText() {
    return text;
  }

  public ActorRef<DocumentCommand> getSender() {
    return sender;
  }
}