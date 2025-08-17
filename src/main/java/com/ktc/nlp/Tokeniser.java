package com.ktc.nlp;

import com.ktc.text.*;
import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.apache.jena.rdf.model.Property;

import java.util.*;
import java.util.function.Consumer;

public class Tokeniser {
  private StanfordCoreNLP pipeline;

  public Tokeniser() {
    Properties props = new Properties();
    // annotators
    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote,natlog,openie");
    // available models
    props.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/english-caseless-left3words-distsim.tagger");
    props.setProperty("ner.model", "edu/stanford/nlp/models/ner/english.conll.4class.caseless.distsim.crf.ser.gz");
    props.setProperty("ner.useSUTime", "0");
    // use the neural algorithm
    props.setProperty("coref.algorithm", "neural");
    pipeline = new StanfordCoreNLP(props);
  }


  public ArrayList<SentenceText> getSentence(String str, DocumentText documentText) {
    class TokenLabelPair {
      public TokenText tokenText;
      public CoreLabel coreLabel;
      public TokenLabelPair(TokenText tokenText, CoreLabel coreLabel) {
        this.tokenText = tokenText;
        this.coreLabel = coreLabel;
      }
    }

    Map<Integer, TokenText> indexedTokens = new HashMap<Integer, TokenText>();
    ArrayList<TokenLabelPair> tokens = new ArrayList<>();

    Consumer<CoreLabel> printToken = (CoreLabel token) -> System.out.println(token.toString() + " " + token.originalText() + " : " + token.tag() + " : " + token.ner() + " " + token.index());

    class TokenPair {
      public CoreLabel token;
      public TripleComponent component;
      public TokenPair(CoreLabel token, TripleComponent component) {
        this.token = token;
        this.component = component;
      }
      public boolean execute() {
        printToken.accept(token);
        TokenText node = indexedTokens.get(token.index());
        if (node != null) {
          component.addChild(node);
          System.out.println("Created token");
          return true;
        }
        System.err.println("Could not find: " + token.originalText() + ", index=" + token.index());
        return false;
      }
    }

    CoreDocument document = new CoreDocument(str);
    pipeline.annotate(document);

    for (CoreSentence sentence : document.sentences()) {
      System.out.println(sentence);
      for (CoreLabel token : sentence.tokens()) {
        TokenText tokenText = TokenText.create(token.originalText());
        tokenText.addAnnotation(PennTreebankPOSTag.create(token.tag()));
        if (!token.ner().isEmpty()) {
          tokenText.addAnnotation(FineGrainedNERTag.create(token.ner()));
        }
        indexedTokens.put(token.index(), tokenText);
        tokens.add(new TokenLabelPair(tokenText, token));
        printToken.accept(token);
      }

      Collection<RelationTriple> triples = sentence.coreMap().get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
      if (triples != null) {
        for (RelationTriple triple : triples) {
          TripleStatement tripleStatement = new TripleStatement();

          TripleSubject tripleSubject = new TripleSubject(triple.subjectGloss());
          System.out.print("subject: ");
          System.out.println(triple.subjectGloss() + ":");
          triple.subject.forEach((CoreLabel token) -> {
            new TokenPair(token, tripleSubject).execute();
          });

          TriplePredicate triplePredicate = new TriplePredicate(triple.relationGloss());
          System.out.print("predicate: ");
          System.out.println(triple.relationGloss() + ":");
          triple.relation.forEach((CoreLabel token) -> {
            new TokenPair(token, triplePredicate).execute();
          });

          TripleObject tripleObject = new TripleObject(triple.objectGloss());
          System.out.print("object: ");
          System.out.println(triple.relationGloss() + ":");
          triple.object.forEach((CoreLabel token) -> {
            new TokenPair(token, tripleObject).execute();
          });

          tripleStatement.addChild(tripleSubject);
          tripleStatement.addChild(triplePredicate);
          tripleStatement.addChild(tripleObject);
        }
      }
    }

    Map<Integer, CorefChain> corefChains = document.annotation().get(CorefCoreAnnotations.CorefChainAnnotation.class);

    if (corefChains != null) {
      for (Map.Entry<Integer, CorefChain> entry : corefChains.entrySet()) {
        CorefChain chain = entry.getValue();
        CorefChain.CorefMention representative = chain.getRepresentativeMention();

        System.out.println("Representative mention: " + representative.mentionSpan);

        String representativeMention = representative.mentionSpan.toString();
        ActorText actor = null;
        for (CorefChain.CorefMention mention : chain.getMentionsInTextualOrder()) {
          System.out.println(" - Mention: " + mention.mentionSpan +
              " (sentence: " + mention.sentNum +
              ", headIndex: " + mention.headIndex + ")");
          TokenText node = indexedTokens.get(mention.headIndex);
          if (node != null) {
            if (actor == null) {
              representativeMention = mention.mentionSpan.toString();
              for (NodeBase child : documentText.allChildNodes()) {
                if (child instanceof ActorText) {
                  for (NodeAnnotation annotation : child.getAnnotations(NodeBase.LABEL)) {
                    if (annotation.getLiteral() == representativeMention) {
                      actor = (ActorText) child;
                      break;
                    }
                  }
                  if (actor != null) {
                    break;
                  }
                }
              }
            }

            if (actor == null) {
              actor = new ActorText();
              actor.addAnnotation(new LiteralValuedProperty(NodeBase.LABEL, representativeMention));
              documentText.addChildNode(actor);
            }
            node.addLink(new Link(Link.DENOTES, node, actor, "denotes"));
          }
        }
      }
    }

    ArrayList<SentenceText> sentences = new ArrayList<>();
    SentenceText sentence = null;
    TokenLabelPair previousTokenLabelPair = null;
    int lastSentenceNumber = -1;
    for (TokenLabelPair tokenLabelPair : tokens) {
      if (sentence == null || lastSentenceNumber != tokenLabelPair.coreLabel.sentIndex()) {
        lastSentenceNumber = tokenLabelPair.coreLabel.sentIndex();
        sentence = new SentenceText();
        sentences.add(sentence);
      }
      if (previousTokenLabelPair != null) {
        int spaceStart = previousTokenLabelPair.coreLabel.endPosition();
        int spaceEnd = tokenLabelPair.coreLabel.beginPosition();
        if (spaceStart < spaceEnd) {
          TokenText space = TokenText.create(str.substring(spaceStart, spaceEnd));
          sentence.addChild(space);
        }
      }
      sentence.addChild(tokenLabelPair.tokenText);
      previousTokenLabelPair = tokenLabelPair;
    }
    return sentences;
  }
}
