package com.ktc.actor;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

import com.ktc.text.DocumentBaseText;
import com.ktc.text.DocumentText;
import com.ktc.text.SentenceText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentClient extends AbstractBehavior<DocumentCommand> {
  private static final Logger log = LoggerFactory.getLogger(DocumentClient.class);
  private final ActorRef<DocumentCommand> documentService;
  private final ConcurrentHashMap<UUID, DocumentBaseText> textMap;
  private final LinkedBlockingQueue<DocumentBaseText> textQueue;
  private boolean isRunning = true;
  private Thread thread;
  private static final Object monitor = new Object();

  public static Behavior<DocumentCommand> create(ActorRef<DocumentCommand> documentService) {
    return Behaviors.setup(context -> new DocumentClient(context, documentService));
  }

  private DocumentClient(ActorContext<DocumentCommand> context, ActorRef<DocumentCommand> documentService) {
    super(context);
    this.documentService = documentService;
    this.textMap = new ConcurrentHashMap<UUID, DocumentBaseText>();
    this.textQueue = new LinkedBlockingQueue<DocumentBaseText>();

    // Start a thread that will wait for 1 second and then send the text to the document service
    thread = new Thread(() -> {
      while (isRunning) {
        try {
          synchronized (monitor) {
            monitor.wait();
            if (!isRunning) {
              break;
            }
            if (textMap.isEmpty() && !textQueue.isEmpty()) {
              DocumentBaseText text = textQueue.take();
              textMap.put(text.getId(), text);
              DocumentRequest request = new DocumentRequest(text, context.getSelf());
              documentService.tell(request);
            }
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    thread.start();
  }

  private void stop() {
    isRunning = false;
    thread.interrupt();
  }
  
  @Override
  public Receive<DocumentCommand> createReceive() {
    return newReceiveBuilder()
        .onMessage(SentenceText.class, this::processSentence)
        .onMessage(DocumentText.class, this::processDocument)
        .onMessage(DocumentResult.class, this::processDocumentResult)
        .onMessage(DocumentClientStopCommand.class, this::processDocumentClientStop)
        .build();
  }

  private Behavior<DocumentCommand> processDocumentClientStop(DocumentClientStopCommand stop) {
    stop();
    return this;
  }

  private Behavior<DocumentCommand> processDocumentResult(DocumentResult result) {
    log.debug("Processing document result: {}", result.toString());
    textMap.remove(result.getId());
    synchronized (monitor) {
      monitor.notifyAll();
    }
    if (result.getException() != null) {
      log.error("Exception in service: {}", result.getException().getMessage(), result.getException());
    }
    return this;
  }

  private Behavior<DocumentCommand> processSentence(SentenceText sentence) {
    log.debug("Processing sentence: {}", sentence.toString());
    textQueue.add(sentence);
    synchronized (monitor) {
      monitor.notifyAll();
    }
    return this;
  }

  private Behavior<DocumentCommand> processDocument(DocumentText document) {
    log.debug("Processing document: {}", document.toString());
    textQueue.add(document);
    synchronized (monitor) {
      monitor.notifyAll();
    }
    return this;
  }
}
