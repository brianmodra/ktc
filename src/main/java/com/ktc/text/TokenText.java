package com.ktc.text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Property;

public class TokenText extends StructuredNode<TokenText, SentenceText, TokenText> {
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

  private final String tokenString;

  public TokenText(Resource type, String tokenString, UUID id) {
    super(type, id);
    this.tokenString = tokenString;
  }

  public static TokenText create(String tokenString) {
    return create(tokenString, UUID.randomUUID());
  }

  public static TokenText create(String tokenString, UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }
    if (tokenString == null || tokenString.isEmpty()) {
      throw new IllegalArgumentException("Token string cannot be null or empty");
    }
    if (tokenString.length() == 1) {
      Resource type = switch (tokenString) {
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
      return new TokenText(type, tokenString, id);
    }
    if (PUNCTUATION_PATTERN.matcher(tokenString).find()) {
      throw new IllegalArgumentException("Token string cannot contain punctuation unless it is one character long");
    }
    return new TokenText(WORD_TYPE, tokenString, id);
  }

  public String getTokenString() {
    return tokenString;
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