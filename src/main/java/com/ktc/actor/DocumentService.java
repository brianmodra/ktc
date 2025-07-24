package com.ktc.actor;

import java.util.*;
import java.util.function.Function;

import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior;
import org.apache.pekko.actor.typed.javadsl.ActorContext;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.actor.typed.javadsl.Receive;

import com.ktc.text.DocumentBaseText;
import com.ktc.text.DocumentText;
import com.ktc.text.ChapterText;
import com.ktc.text.ParagraphText;
import com.ktc.text.SentenceText;
import com.ktc.text.PhraseText;
import com.ktc.text.WordText;

import edu.stanford.nlp.pipeline.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktc.structure.DocumentStructureInterface;
import com.ktc.structure.StringStructure;
import com.ktc.structure.SentenceStructure;

public class DocumentService extends AbstractBehavior<DocumentCommand> {
  private final Logger log = LoggerFactory.getLogger(DocumentService.class);
  private final Map<Class<?>, Function<DocumentBaseText, Behavior<DocumentCommand>>> textProcessors;
  
  static Behavior<DocumentCommand> create() {
    return Behaviors.setup(DocumentService::new);
  }

  private DocumentService(ActorContext<DocumentCommand> context) {
    super(context);
    
    // Map to handle different text types without long if-else chains
    textProcessors = Map.of(
      DocumentText.class, text -> this.processDocument((DocumentText) text),
      ChapterText.class, text -> this.processChapter((ChapterText) text),
      ParagraphText.class, text -> this.processParagraph((ParagraphText) text),
      SentenceText.class, text -> this.processSentence((SentenceText) text),
      // PhraseText.class, text -> this.processPhrase((PhraseText) text),
      WordText.class, text -> this.processWord((WordText) text)
    );
  }

  @Override
  public Receive<DocumentCommand> createReceive() {
    return newReceiveBuilder()
        .onMessage(DocumentRequest.class, this::processRequest)
        .build();
  }

  private Behavior<DocumentCommand> processRequest(DocumentRequest request) {
    log.debug("Processing request: {}", request.getText());
    DocumentResult response = new DocumentResult(request.getText().getId());
    
    // Clean lookup-based processing instead of long if-else chain
    DocumentBaseText text = request.getText();
    Function<DocumentBaseText, Behavior<DocumentCommand>> processor = textProcessors.get(text.getClass());
    
    Behavior<DocumentCommand> ret;
    if (processor != null) {
      ret = processor.apply(text);
    } else {
      response.setException(new IllegalArgumentException("Unknown text type: " + text.getClass()));
      ret = this;
    }
    
    request.getSender().tell(response);
    return ret;
  }

  private Behavior<DocumentCommand> processDocument(DocumentText documentText) {
    log.debug("Processing document with {} chapters", documentText.getChapters().size());
    
    // Process each chapter in the document
    for (ChapterText chapter : documentText.getChapters()) {
      processChapter(chapter);
    }
    
    return this;
  }

  private Behavior<DocumentCommand> processChapter(ChapterText chapterText) {
    log.debug("Processing chapter with {} paragraphs", chapterText.getParagraphs().size());
    
    // Process each paragraph in the chapter
    for (ParagraphText paragraph : chapterText.getParagraphs()) {
      processParagraph(paragraph);
    }
    
    return this;
  }

  private Behavior<DocumentCommand> processParagraph(ParagraphText paragraphText) {
    log.debug("Processing paragraph with {} sentences", paragraphText.getSentences().size());
    
    // Process each sentence in the paragraph
    for (SentenceText sentence : paragraphText.getSentences()) {
      processSentence(sentence);
    }
    
    return this;
  }

  private Behavior<DocumentCommand> processSentence(SentenceText sentenceText) {
    log.debug("Sentence: {}", sentenceText.getPhrasesAsString());

    return this;
  }

  private Behavior<DocumentCommand> processPhrase(SentenceStructure sentenceStructure, PhraseText phraseText, StringStructure previousString) {
    Model model = DocumentStructureInterface.createModel();
    StanfordCoreNLP pipeline = DocumentStructureInterface.createPipeline();
    StringStructure stringStructure = new StringStructure(model, sentenceStructure, phraseText, previousString);
    DocumentStructureInterface.addRelationTriplesToPhrase(model, stringStructure, phraseText, pipeline, log);

    String modelString = modelToString(model);
    log.debug("Knowledge base: {}", modelString);

    return this;
  }

  private Behavior<DocumentCommand> processWord(WordText wordText) {
    log.debug("Word: {}", wordText.getWordString());
    
    return this;
  }
  
  private String modelToString(Model model) {
    try {
      java.io.StringWriter writer = new java.io.StringWriter();
      model.write(writer, "TURTLE");
      String result = writer.toString();
      writer.close();
      log.debug("Knowledge base converted to string, {} characters", result.length());
      return result;
    } catch (java.io.IOException e) {
      log.error("Error converting knowledge base to string: {}", e.getMessage(), e);
      return "";
    }
  }
  
  // Method to query the knowledge base (for future use)
  public void queryKnowledgeBase(String sparqlQuery, Model model) {
    synchronized (model) {
      QueryExecution qexec = QueryExecutionFactory.create(QueryFactory.create(sparqlQuery), model);
      try {
        ResultSet results = qexec.execSelect();
        ResultSetFormatter.out(System.out, results, model);
      } finally {
        qexec.close();
      }
    }
  }
}
