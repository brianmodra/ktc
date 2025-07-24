package com.ktc.actor;

import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.actor.typed.javadsl.AskPattern;

import com.ktc.text.DocumentText;
import com.ktc.text.SentenceText;

import java.util.concurrent.CompletionStage;
import java.time.Duration;

public class DocumentSystem {
  ActorSystem<DocumentCommand> documentServiceSystem;
  ActorSystem<DocumentCommand> documentClientSystem;

  public DocumentSystem() {
    documentServiceSystem = ActorSystem.create(DocumentService.create(), "documentService");
    documentClientSystem = ActorSystem.create(DocumentClient.create(documentServiceSystem), "documentClient");
  }

  public void processDocument(DocumentText document) {
    documentClientSystem.tell(document);
  }

  public void processSentence(SentenceText sentence) {
    documentClientSystem.tell(sentence);
  }
}
