package com.ktc.structure;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

import com.ktc.text.DocumentText;
import com.ktc.text.ChapterText;
import com.ktc.text.ParagraphText;
import com.ktc.text.SentenceText;
import com.ktc.text.PhraseText;
import com.ktc.text.WordText;

import edu.stanford.nlp.ie.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.naturalli.SentenceFragment;
import org.slf4j.Logger;

import com.ktc.text.EnclosingPunctuationEnum;
import com.ktc.text.PunctuationEnum;

import java.util.UUID;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Helper class for creating document structure in RDF using UUIDs
 * Supports hierarchy: Document -> Chapter -> Paragraph -> Sentence -> Word
 * Order is maintained through RDF relationships rather than sequence numbers
 * 
 * Document
 * ├── Chapter (multiple) - can also be direct children
 * |  ├── Paragraph (multiple)
 * |  ├── Sentence (multiple)
 * |  ├── Phrase (also called String) - (multiple)
 * |  └── Word (multiple)
 * └── All elements support both next/previous navigation
 */
public interface DocumentStructureInterface {
  
  public static StanfordCoreNLP createPipeline() {
    Properties props = new Properties();
    //props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,depparse,coref,natlog,openie");
    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,ner,natlog,openie");
    props.setProperty("openie.resolve_coref", "false");
    props.setProperty("openie.triple.all_nominals", "true");
    props.setProperty("openie.triple.strict", "false");
    props.setProperty("openie.max_entailments_per_clause", "1000");
    props.setProperty("openie.splitter.threshold", "0.1");
    props.setProperty("depparse.model", "edu/stanford/nlp/models/parser/nndep/english_UD.gz");
    props.setProperty("outputFormat", "text");
    return new StanfordCoreNLP(props);
  }

  public static Model createModel() {
    Model model = ModelFactory.createDefaultModel();
    model.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    model.setNsPrefix("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
    model.setNsPrefix("dcterms", "http://purl.org/dc/terms/");
    model.setNsPrefix("prov", "http://www.w3.org/ns/prov#");
    model.setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
    model.setNsPrefix("nif", "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#");
    model.setNsPrefix("olia", "http://purl.org/olia/olia.owl#");
    model.setNsPrefix("ktc", "http://ktc.com/");  // For domain-specific concepts
    return model;
  }

  public static String getDocumentUri(UUID documentId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#document_" + documentId.toString();
  }
  
  public static String getChapterUri(UUID chapterId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#chapter_" + chapterId.toString();
  }
  
  public static String getParagraphUri(UUID paragraphId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#paragraph_" + paragraphId.toString();
  }
  
  public static String getSentenceUri(UUID sentenceId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#sentence_" + sentenceId.toString();
  }
  
  public static String getStringUri(UUID stringId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#string_" + stringId.toString();
  }
  
  public static String getWordUri(UUID wordId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#word_" + wordId.toString();
  }

  public static String getTokenUri(UUID sentenceId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#token_" + sentenceId.toString();
  }

  public static String getSubjectUri(String subjectName) {
   return "http://www.w3.org/1999/02/22-rdf-syntax-ns#subject_" + subjectName.replace(" ", "_") + "_" + UUID.randomUUID().toString();
  }

  public static String getPredicateUri(String predicateName) {
    return "http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate_" + predicateName.replace(" ", "_") + "_" + UUID.randomUUID().toString();
  }

  public static String getObjectUri(String objectName) {
    return "http://www.w3.org/1999/02/22-rdf-syntax-ns#object_" + objectName.replace(" ", "_") + "_" + UUID.randomUUID().toString();
  }

  // Standard RDF Properties for document structure 
  // Using Dublin Core Terms for containment relationships
  public static final Property HAS_PART = ResourceFactory.createProperty("http://purl.org/dc/terms/hasPart");
  public static final Property IS_PART_OF = ResourceFactory.createProperty("http://purl.org/dc/terms/isPartOf");
  
  // Using RDFS for labels
  public static final Property LABEL = RDFS.label;  // Use standard rdfs:label for text content
  
  // NIF-based properties for text sequencing and ordering
  public static final Property NEXT_CHAPTER = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#nextChapter");
  public static final Property NEXT_PARAGRAPH = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#nextParagraph");
  public static final Property NEXT_SENTENCE = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#nextSentence");
  public static final Property NEXT_WORD = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#nextWord");
  public static final Property PREVIOUS_CHAPTER = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#previousChapter");
  public static final Property PREVIOUS_PARAGRAPH = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#previousParagraph");
  public static final Property PREVIOUS_SENTENCE = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#previousSentence");
  public static final Property PREVIOUS_WORD = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#previousWord");
  public static final Property ANCHOR_OF = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#anchorOf");
  
  // KTC-based properties for dramatic structure sequencing
  public static final Property NEXT_STRING = ResourceFactory.createProperty("http://ktc.com/nextString");
  public static final Property PREVIOUS_STRING = ResourceFactory.createProperty("http://ktc.com/previousString");
  
  // Document type URIs using NIF and standard vocabularies
  public static final Resource DOCUMENT_TYPE = ResourceFactory.createResource("http://purl.org/dc/dcmitype/Text");
  public static final Resource CHAPTER_TYPE = ResourceFactory.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Chapter");
  public static final Resource STRING_TYPE = ResourceFactory.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#String");  
  public static final Resource PARAGRAPH_TYPE = ResourceFactory.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Paragraph");  
  public static final Resource SENTENCE_TYPE = ResourceFactory.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Sentence");
  public static final Resource WORD_TYPE = ResourceFactory.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Word");
  public static final Resource TOKEN_TYPE = ResourceFactory.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Token");

  // Document type URIs using OLiA namespace
  public static final Resource PUNCTUATION_TYPE = ResourceFactory.createResource("http://purl.org/olia/olia.owl#Punctuation");
  public static final Resource QUOTATION_MARK = ResourceFactory.createResource("http://purl.org/olia/olia.owl#QuotationMark");
  public static final Property POS = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#PoS");
  public static final Resource EXCLAMATION_MARK = ResourceFactory.createResource("http://purl.org/olia/penn.owl#ExclamationMark");
  public static final Resource QUESTION_MARK = ResourceFactory.createResource("http://purl.org/olia/penn.owl#QuestionMark");
  public static final Resource PERIOD = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Period");
  public static final Resource COMMA = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Comma");
  public static final Resource COLON = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Colon");
  public static final Resource SEMICOLON = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Semicolon");
  public static final Resource HYPHEN = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Hyphen");
  public static final Resource LEFT_PARENTHESIS = ResourceFactory.createResource("http://purl.org/olia/penn.owl#LeftParenthesis");
  public static final Resource RIGHT_PARENTHESIS = ResourceFactory.createResource("http://purl.org/olia/penn.owl#RightParenthesis");
  public static final Resource LEFT_BRACKET = ResourceFactory.createResource("http://purl.org/olia/penn.owl#LeftBracket");
  public static final Resource RIGHT_BRACKET = ResourceFactory.createResource("http://purl.org/olia/penn.owl#RightBracket");
  public static final Resource QUOTE = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Quote");
  public static final Resource SLASH = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Slash");
  public static final Resource BACKSLASH = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Backslash");
  public static final Resource ELLIPSIS = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Ellipsis");
  public static final Resource LEFT_BRACE = ResourceFactory.createResource("http://purl.org/olia/penn.owl#LeftBrace");
  public static final Resource RIGHT_BRACE = ResourceFactory.createResource("http://purl.org/olia/penn.owl#RightBrace");

  // RDF properties for provenance
  public static final Property PROVENANCE_QUALITY_MEASURE = ResourceFactory.createProperty("http://www.w3.org/ns/prov#qualityMeasure");
  public static final Property PROVENANCE_WAS_DERIVED_FROM = ResourceFactory.createProperty("http://www.w3.org/ns/prov#wasDerivedFrom");

  // Standard RDF vocabulary
  public static final Property RDF_TYPE = RDF.type;
  public static final Property RDF_SUBJECT = RDF.subject;
  public static final Property RDF_PREDICATE = RDF.predicate;
  public static final Property RDF_OBJECT = RDF.object;

  public static Resource createDocument(Model model, DocumentText documentText) {
    Resource document = model.createResource(getDocumentUri(documentText.getId()));
    document.addProperty(RDF.type, DOCUMENT_TYPE);
    return document;
  }

  public static Resource createChapter(Model model, Resource document, ChapterText chapterText, Resource previousChapter) {
    Resource chapter = model.createResource(getChapterUri(chapterText.getId()));
    chapter.addProperty(RDF.type, CHAPTER_TYPE);
    document.addProperty(HAS_PART, chapter);
    chapter.addProperty(IS_PART_OF, document);
    if (previousChapter != null) {
      previousChapter.addProperty(NEXT_CHAPTER, chapter);
      chapter.addProperty(PREVIOUS_CHAPTER, previousChapter);
    }
    return chapter;
  }

  public static Resource createParagraph(Model model, Resource chapter, ParagraphText paragraphText, Resource previousParagraph) {
    Resource paragraph = model.createResource(getParagraphUri(paragraphText.getId()));
    paragraph.addProperty(RDF.type, PARAGRAPH_TYPE);
    chapter.addProperty(HAS_PART, paragraph);
    paragraph.addProperty(IS_PART_OF, chapter);
    if (previousParagraph != null) {
      previousParagraph.addProperty(NEXT_PARAGRAPH, paragraph);
      paragraph.addProperty(PREVIOUS_PARAGRAPH, previousParagraph);
    }
    return paragraph;
  }

  public static Resource createSentence(Model model, Resource paragraph, SentenceText sentenceText, Resource previousSentence) {
    Resource sentence = model.createResource(getSentenceUri(sentenceText.getId()));
    sentence.addProperty(RDF.type, SENTENCE_TYPE);
    paragraph.addProperty(HAS_PART, sentence);
    sentence.addProperty(IS_PART_OF, paragraph);
    if (previousSentence != null) {
      previousSentence.addProperty(NEXT_SENTENCE, sentence);
      sentence.addProperty(PREVIOUS_SENTENCE, previousSentence);
    }
    return sentence;
  }

  public static Resource createString(Model model, Resource sentence, PhraseText phraseText, Resource previousString) {
    Resource string = model.createResource(getStringUri(phraseText.getId()));
    string.addProperty(RDF.type, STRING_TYPE);
    sentence.addProperty(HAS_PART, string);
    string.addProperty(IS_PART_OF, sentence);
    if (previousString != null) {
      previousString.addProperty(NEXT_STRING, string);
      string.addProperty(PREVIOUS_STRING, previousString);
    }
    return string;
  }

  public static Resource addOpeningPunctuationToSentence(Model model, Resource sentence, UUID phraseId, EnclosingPunctuationEnum punctuation) {
    if (punctuation == EnclosingPunctuationEnum.NONE) {
      return null;
    }
    Resource punctuationResource = model.createResource(getTokenUri(phraseId));
    punctuationResource.addProperty(RDF.type, TOKEN_TYPE);
    punctuationResource.addProperty(RDF.type, PUNCTUATION_TYPE);
    switch (punctuation) {
      case QUOTATION_MARK:
        punctuationResource.addProperty(POS, QUOTE);
        break;
      case PARENTHESIS:
        punctuationResource.addProperty(POS, LEFT_PARENTHESIS);
        break;
      case BRACKETS:
        punctuationResource.addProperty(POS, LEFT_BRACKET);
        break;
      case BRACES:
        punctuationResource.addProperty(POS, LEFT_BRACE);
        break;
      default:
        throw new IllegalArgumentException("Invalid punctuation: " + punctuation);
    }
    sentence.addProperty(HAS_PART, punctuationResource);
    punctuationResource.addProperty(IS_PART_OF, sentence);
    return punctuationResource;
  }

  public static Resource addClosingPunctuationToSentence(Model model, Resource sentence, UUID phraseId, EnclosingPunctuationEnum punctuation) {
    if (punctuation == EnclosingPunctuationEnum.NONE) {
      return null;
    }
    Resource punctuationResource = model.createResource(getTokenUri(phraseId));
    punctuationResource.addProperty(RDF.type, TOKEN_TYPE);
    punctuationResource.addProperty(RDF.type, PUNCTUATION_TYPE);
    switch (punctuation) {
      case QUOTATION_MARK:
        punctuationResource.addProperty(POS, DocumentStructureInterface.QUOTE);
        break;
      case PARENTHESIS:
        punctuationResource.addProperty(POS, DocumentStructureInterface.RIGHT_PARENTHESIS);
        break;
      case BRACKETS:
        punctuationResource.addProperty(POS, DocumentStructureInterface.RIGHT_BRACKET);
        break;
      case BRACES:
        punctuationResource.addProperty(POS, DocumentStructureInterface.RIGHT_BRACE);
        break;
      default:
        throw new IllegalArgumentException("Invalid punctuation: " + punctuation);
    }
    sentence.addProperty(HAS_PART, punctuationResource);
    punctuationResource.addProperty(IS_PART_OF, sentence);
    return punctuationResource;
  }

  public static Resource addPunctuationToSentence(Model model, Resource sentence, UUID sentenceId, PunctuationEnum punctuation) {
    if (punctuation == PunctuationEnum.NONE) {
      return null;
    }
    Resource punctuationResource = model.createResource(getTokenUri(sentenceId));
    punctuationResource.addProperty(RDF.type, TOKEN_TYPE);
    punctuationResource.addProperty(RDF.type, PUNCTUATION_TYPE);
    switch (punctuation) {
      case PERIOD:
        punctuationResource.addProperty(POS, DocumentStructureInterface.PERIOD);
        break;
      case QUESTION_MARK:
        punctuationResource.addProperty(POS, DocumentStructureInterface.QUESTION_MARK);
        break;
      case EXCLAMATION_MARK:
        punctuationResource.addProperty(POS, DocumentStructureInterface.EXCLAMATION_MARK);
        break;
      case COMMA:
        punctuationResource.addProperty(POS, DocumentStructureInterface.COMMA);
        break;
      case COLON:
        punctuationResource.addProperty(POS, DocumentStructureInterface.COLON);
        break;
      case SEMICOLON:
        punctuationResource.addProperty(POS, DocumentStructureInterface.SEMICOLON);
        break;
      case DASH:
        punctuationResource.addProperty(POS, DocumentStructureInterface.HYPHEN);
        break;
      case SLASH:
        punctuationResource.addProperty(POS, DocumentStructureInterface.SLASH);
        break;
      case BACKSLASH:
        punctuationResource.addProperty(POS, DocumentStructureInterface.BACKSLASH);
        break;
      case ELLIPSIS:
        punctuationResource.addProperty(POS, DocumentStructureInterface.ELLIPSIS);
        break;
      default:
        throw new IllegalArgumentException("Invalid punctuation: " + punctuation);
    }
    sentence.addProperty(HAS_PART, punctuationResource);
    punctuationResource.addProperty(IS_PART_OF, sentence);
    return punctuationResource;
  }

  /**
   * Expands a list of core tokens (e.g., noun tokens) to include preceding determiners and modifiers
   * @param coreTokens The core tokens from OpenIE extraction
   * @param allTokens All tokens in the sentence
   * @return Extended list including determiners, adjectives, and other modifiers
   */
  private static List<CoreLabel> expandWithDeterminers(List<CoreLabel> coreTokens, List<CoreLabel> allTokens) {
    if (coreTokens.isEmpty()) {
      return coreTokens;
    }
    
    List<CoreLabel> expandedTokens = new ArrayList<>();
    
    // Get the index of the first core token
    int firstCoreIndex = coreTokens.get(0).index();
    
    // Look backwards from the first core token to find determiners, adjectives, etc.
    for (int i = firstCoreIndex - 1; i >= 1; i--) { // token indices are 1-based
      CoreLabel token = allTokens.get(i - 1); // allTokens list is 0-based
      String pos = token.tag();
      
      // Include determiners (DT), adjectives (JJ, JJR, JJS), and possessive pronouns (PRP$)
      if (pos.equals("DT") || pos.startsWith("JJ") || pos.equals("PRP$")) {
        expandedTokens.add(0, token); // Add to beginning
      } else {
        // Stop if we hit something that's not a modifier (like a verb, noun, etc.)
        break;
      }
    }
    
    // Add all the original core tokens
    expandedTokens.addAll(coreTokens);
    
    return expandedTokens;
  }

  public static void addRelationTriplesToPhrase(Model model, StringStructure stringStructure, PhraseText phraseText, StanfordCoreNLP pipeline, Logger log) {
    String text = phraseText.getWordsAsString();
    Resource stringResource = stringStructure.getStringResource();

    // Create annotation using the shared pipeline
    Annotation document = new Annotation(text);
    pipeline.annotate(document);

    ArrayList<WordStructure> wordStructures = new ArrayList<>();
    WordStructure previousWord = null;  
    for (WordText wordText : phraseText.getWords()) {
      WordStructure wordStructure = new WordStructure(model, stringStructure, wordText, previousWord);
      previousWord = wordStructure;
      wordStructures.add(wordStructure);
    }

    synchronized (model) {
      for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
        List<CoreLabel> allTokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        log.debug("Processing sentence: '{}'", sentence.get(CoreAnnotations.TextAnnotation.class));
      
        Collection<SentenceFragment> entailedSentences = sentence.get(NaturalLogicAnnotations.EntailedSentencesAnnotation.class);

        for (SentenceFragment fragment : entailedSentences) {
          String clauseText = fragment.toString().trim();
        
          // Skip very short or empty clauses
          if (clauseText.length() < 5) {
            continue;
          }
        
          // Add proper sentence ending if missing
          if (!clauseText.endsWith(".") && !clauseText.endsWith("!") && !clauseText.endsWith("?")) {
            clauseText = clauseText + ".";
          }
        
          log.debug("Processing entailed sentence: '{}'", clauseText);
        
          // Create annotation for this clause
          Annotation clauseDoc = new Annotation(clauseText);
          pipeline.annotate(clauseDoc);
        
          // Extract triples from this simplified clause
          for (CoreMap clauseSentence : clauseDoc.get(CoreAnnotations.SentencesAnnotation.class)) {
            Collection<RelationTriple> clauseTriples = clauseSentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
          
            if (clauseTriples != null && !clauseTriples.isEmpty()) {
              log.debug("Found {} triples in entailed sentence: '{}'", clauseTriples.size(), clauseText);
            
              for (RelationTriple triple : clauseTriples) {
                log.debug("Entailed sentence triple: {} | {} | {} (confidence: {})", 
                  triple.subjectLemmaGloss(), triple.relationLemmaGloss(), 
                  triple.objectLemmaGloss(), triple.confidence);
                
                // Get the actual tokens for subject, relation, and object
                List<CoreLabel> subjTokens = expandWithDeterminers(triple.subject, allTokens);
                List<CoreLabel> relTokens = triple.relation;
                List<CoreLabel> objTokens = expandWithDeterminers(triple.object, allTokens);

                String subject = subjTokens.stream()
                  .map(CoreLabel::word)  // or .lemma() for lemmatized form
                  .reduce("", (a, b) -> a.isEmpty() ? b : a + " " + b);
                String predicate = relTokens.stream()
                  .map(CoreLabel::word)
                  .reduce("", (a, b) -> a.isEmpty() ? b : a + " " + b);
                String object = objTokens.stream()
                  .map(CoreLabel::word)
                  .reduce("", (a, b) -> a.isEmpty() ? b : a + " " + b);

                log.debug("{}\t{}\t{}\t{}", subject, predicate, object, triple.confidence);

                // spoTriple represents a Subject-Predicate-Object triple
                Resource spoTriple = model.createResource();
                Resource subjNode = model.createResource(DocumentStructureInterface.getSubjectUri(subject));
                Resource objNode = model.createResource(DocumentStructureInterface.getObjectUri(object));
                Property predNode = model.createProperty(DocumentStructureInterface.getPredicateUri(predicate));

                // Log detailed token information
                log.debug("Subject tokens:");
                for (CoreLabel token : subjTokens) {
                    log.debug("  '{}' (lemma: '{}', pos: '{}', index: {})", 
                      token.word(), token.lemma(), token.tag(), token.index());
                    WordStructure wordStructure = wordStructures.get(token.index() - 1);
                    addContextToWord(wordStructure.getWordResource(), subjNode);
                }

                log.debug("Relation tokens:");
                for (CoreLabel token : relTokens) {
                    log.debug("  '{}' (lemma: '{}', pos: '{}', index: {})", 
                      token.word(), token.lemma(), token.tag(), token.index());
                    WordStructure wordStructure = wordStructures.get(token.index() - 1);
                    addContextToWord(wordStructure.getWordResource(), predNode);
                }

                log.debug("Object tokens:");
                for (CoreLabel token : objTokens) {
                    log.debug("  '{}' (lemma: '{}', pos: '{}', index: {})", 
                      token.word(), token.lemma(), token.tag(), token.index());
                    WordStructure wordStructure = wordStructures.get(token.index() - 1);
                    addContextToWord(wordStructure.getWordResource(), objNode);
                }
                
                // Add labels
                subjNode.addProperty(RDFS.label, subject);
                subjNode.addProperty(IS_PART_OF, stringResource);
                predNode.addProperty(RDFS.label, predicate);
                predNode.addProperty(IS_PART_OF, stringResource);
                objNode.addProperty(RDFS.label, object);
                objNode.addProperty(IS_PART_OF, stringResource);
              
                // Create the reified triple structure using standard vocabularies
                spoTriple.addProperty(RDF.subject, subjNode)
                  .addProperty(RDF.predicate, predNode)
                  .addProperty(RDF.object, objNode)
                  .addProperty(DocumentStructureInterface.PROVENANCE_QUALITY_MEASURE, 
                              model.createTypedLiteral(triple.confidence))
                  .addProperty(DocumentStructureInterface.PROVENANCE_WAS_DERIVED_FROM, stringResource);
              
                log.debug("Added triple to knowledge base: {}", spoTriple.toString());
              }
            }
          }
        }
      }
    }
  }

  public static Resource createWord(Model model, Resource string, WordText wordText, Resource previousWord) {
    Resource wordResource = model.createResource(getWordUri(wordText.getId()));
    wordResource.addProperty(RDF.type, WORD_TYPE);
    string.addProperty(HAS_PART, wordResource);
    wordResource.addProperty(IS_PART_OF, string);
    wordResource.addProperty(ANCHOR_OF, wordText.getWordString());
    if (previousWord != null) {
      previousWord.addProperty(NEXT_WORD, wordResource);
      wordResource.addProperty(PREVIOUS_WORD, previousWord);
    }
    return wordResource;
  }

  public static Resource addContextToWord(Resource wordResource, Resource context) {
    wordResource.addProperty(IS_PART_OF, context);
    context.addProperty(HAS_PART, wordResource);
    return wordResource;
  }
} 