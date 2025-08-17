package com.ktc.text;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.lang.ref.WeakReference;

import com.ktc.nlp.FineGrainedNERTag;
import com.ktc.nlp.PennTreebankPOSTag;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Property;

public class TokenText extends StructuredNode<TokenText, SentenceText, TokenText> {
  protected WeakReference<Object> uiObject;

  private static final Map<String, Resource> TOKEN_TYPE_MAP = new HashMap<String, Resource>() {{
    put("\"", QUOTATION_MARK);
    put("!", EXCLAMATION_MARK);
    put("?", QUESTION_MARK);
    put(".", PERIOD);
    put(",", COMMA);
    put(":", COLON);
    put(";", SEMICOLON);
    put("-", HYPHEN);
    put("(", LEFT_PARENTHESIS);
    put(")", RIGHT_PARENTHESIS);
    put("[", LEFT_BRACKET);
    put("]", RIGHT_BRACKET);
    put("'", QUOTE);
    put("/", SLASH);
    put("\\", BACKSLASH);
    put("...", ELLIPSIS);
    put("{", LEFT_BRACE);
    put("}", RIGHT_BRACE);
  }};

  private static final Pattern PUNCTUATION_PATTERN = Pattern.compile(
    TOKEN_TYPE_MAP.keySet().stream()
        .map(Pattern::quote)
        .collect(Collectors.joining("|"))
  );

  public static class WhitespaceTraversalResult {
    public boolean allWhitespace = true;
    public int whitespaceCount = 0;
    public int newlineCount = 0;
  }

  public static WhitespaceTraversalResult analyzeWhitespace(String str) {
    if (str == null || str.isEmpty()) {
      throw new IllegalArgumentException("String can't be null or empty");
    }

    final WhitespaceTraversalResult ret = new WhitespaceTraversalResult();

    str.chars().forEach(c -> {
      if (c == '\n') {
        ret.newlineCount++;
      }
      if (Character.isWhitespace(c)) {
        ret.whitespaceCount++;
      } else {
        ret.allWhitespace = false;
      }
    });

    return ret;
  }

  private String tokenString;

  public TokenText(Resource type, String tokenString, UUID id) {
    super(type, id);
    this.tokenString = tokenString;
  }

  public void setUiObject(Object uiObject) {
    this.uiObject = new WeakReference<>(uiObject);
  }

  public Object getUiObject() {
    return uiObject.get();
  }

  public static TokenText create(String tokenString) {
    return create(tokenString, UUID.randomUUID());
  }

  public static TokenText create(String tokenString, UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }
    return new TokenText(getResourceForTokenString(tokenString), tokenString, id);
  }

  public static Resource getResourceForTokenString(String tokenString) {
    if (tokenString == null || tokenString.isEmpty()) {
      throw new IllegalArgumentException("Token string cannot be null or empty");
    }
    if (tokenString.length() == 1) {
      return switch (tokenString) {
        case "\"" -> QUOTATION_MARK;
        case "!" -> EXCLAMATION_MARK;
        case "?" -> QUESTION_MARK;
        case "." -> PERIOD;
        case "," -> COMMA;
        case ":" -> COLON;
        case ";" -> SEMICOLON;
        case "-" -> HYPHEN;
        case "(" -> LEFT_PARENTHESIS;
        case ")" -> RIGHT_PARENTHESIS;
        case "[" -> LEFT_BRACKET;
        case "]" -> RIGHT_BRACKET;
        case "'" -> QUOTE;
        case "/" -> SLASH;
        case "\\" -> BACKSLASH;
        case "..." -> ELLIPSIS;
        case "{" -> LEFT_BRACE;
        case "}" -> RIGHT_BRACE;
        default -> WORD_TYPE;
      };
    }
    if (PUNCTUATION_PATTERN.matcher(tokenString).find()) {
      if (!tokenString.startsWith("'") || tokenString.endsWith("'")) {
        throw new IllegalArgumentException("Token string cannot contain punctuation unless it is one character long, or a posessive");
      }
    }
    WhitespaceTraversalResult whitespaceAnalysis = analyzeWhitespace(tokenString);
    if (whitespaceAnalysis.allWhitespace) {
      if (whitespaceAnalysis.newlineCount == 0) {
        return SPACE_TOKEN;
      }
      return LINE_BREAK;
    }
    if (whitespaceAnalysis.whitespaceCount > 0) {
      return STRING_TYPE;
    }
    return WORD_TYPE;
  }

  public String getTokenString() {
    return tokenString;
  }

  public void setTokenString(String tokenString) {
    this.tokenString = tokenString;
  }

  public boolean isChildOf(List<Class> classes) {
    Property childProperty = getChildPropertyOfParent();
    if (childProperty == null) {
      return false;
    }
    return inwardLinks.stream()
        .filter((Link link) -> link.getProperty() == childProperty && classes.contains(link.getSource().getClass()))
        .findFirst()
        .isPresent();
  }

  public boolean hasNLPInfo() {
    if (annotations.stream()
        .filter((NodeAnnotation annotation) -> annotation instanceof FineGrainedNERTag || annotation instanceof PennTreebankPOSTag)
        .findFirst()
        .isPresent()) {
      return true;
    }
    if (isChildOf(List.of(TripleObject.class, TriplePredicate.class, TripleSubject.class))) {
      return true;
    }
    return false;
  }

  public boolean removeNLP() {
    boolean removedSomething = false;
    for (NodeAnnotation annotation : annotations) {
      if (annotation instanceof FineGrainedNERTag || annotation instanceof PennTreebankPOSTag) {
        if (annotations.remove(annotation)) {
          removedSomething = true;
        }
      }
    };
    Property childProperty = getChildPropertyOfParent();
    if (childProperty == null) {
      return removedSomething;
    }
    List<Class> classes = List.of(TripleObject.class, TriplePredicate.class, TripleSubject.class);
    for (Link link : inwardLinks) {
      if (link.getProperty() == childProperty && classes.contains(link.getSource().getClass())) {
        link.getSource().unlink();
        removedSomething = true;
      }
    }
    return removedSomething;
  }

  public List<NodeBase> getTripleObjectChildren() {
    return getTripleComponentChildren(List.of(TripleObject.class));
  }

  public List<NodeBase> getTriplePredicateChildren() {
    return getTripleComponentChildren(List.of(TriplePredicate.class));
  }

  public List<NodeBase> getTripleSubjectChildren() {
    return getTripleComponentChildren(List.of(TripleSubject.class));
  }

  public List<NodeBase> getTripleChildren() {
    return getTripleComponentChildren(List.of(TripleObject.class, TriplePredicate.class, TripleSubject.class));
  }

  public Property getChildPropertyOfParent() {
    NodeBase parent = getParentNode();
    if (parent == null) {
      return null;
    }
    return parent.getChildProperty();
  }

  public List<NodeBase> getTripleComponentChildren(List<Class> classes) {
    Property childProperty = getChildPropertyOfParent();
    if (childProperty == null) {
      return List.of();
    }
    return inwardLinks.stream()
        .filter((Link link) -> link.getProperty() == childProperty && classes.contains(link.getSource().getClass()))
        .map(Link::getSource)
        .collect(Collectors.toList());
  }

  @Override
  public Property getNextProperty() {
    return Link.NEXT_WORD;
  }

  @Override
  public Property getPreviousProperty() {
    return Link.PREVIOUS_WORD;
  }

  @Override
  public boolean parentCanBe(Class<? extends NodeBase> parentClass) {
    return parentClass == SentenceText.class;
  }

  @Override
  public Property getChildProperty() {
    return null;
  }

  @Override
  public String toString() {
    return tokenString;
  }
}