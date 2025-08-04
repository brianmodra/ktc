package com.ktc.text;

import java.util.UUID;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class SentenceText extends StructuredNode<SentenceText, ParagraphText, TokenText> {
  public SentenceText() {
    super(SENTENCE_TYPE);
  }

  public SentenceText(UUID sentenceId) {
    super(SENTENCE_TYPE, sentenceId);
  }

  @Override
  public Property getNextProperty() {
    return Link.NEXT_SENTENCE;
  }

  @Override
  public Property getPreviousProperty() {
    return Link.PREVIOUS_SENTENCE;
  }

  @Override
  public boolean parentCanBe(Class<? extends NodeBase> parentClass) {
    return parentClass == ParagraphText.class;
  }

  public final String getTokensAsString() {
    StringBuilder sb = new StringBuilder();
    TokenText lastToken = null;
    int quotationCount = 0;
    TokenText token = firstChild();
    while (token != null) {
      if (token.getResource() == NodeBase.QUOTATION_MARK) {
        quotationCount++;
      }
      if (lastToken != null) {
        Resource lastResource = lastToken.getResource();
        Resource resource = token.getResource();
        if (lastResource == NodeBase.WORD_TYPE) {
          if (resource == NodeBase.QUOTATION_MARK) {
            if (quotationCount % 2 != 0) {
              sb.append(" ");
            }
          } else if (!(resource == NodeBase.EXCLAMATION_MARK ||
          resource == NodeBase.QUESTION_MARK ||
          resource == NodeBase.PERIOD||
          resource == NodeBase.COMMA ||
          resource == NodeBase.COLON ||
          resource == NodeBase.SEMICOLON ||
          resource == NodeBase.HYPHEN ||
          resource == NodeBase.RIGHT_PARENTHESIS ||
          resource == NodeBase.RIGHT_BRACKET ||
          resource == NodeBase.RIGHT_BRACE)) {
            sb.append(" ");
          }
        } else if (lastResource == NodeBase.QUOTATION_MARK) {
          if (quotationCount % 2 == 0) {
            sb.append(" ");
          }
        } else if (lastResource == NodeBase.EXCLAMATION_MARK ||
        lastResource == NodeBase.QUESTION_MARK ||
        lastResource == NodeBase.PERIOD||
        lastResource == NodeBase.COMMA ||
        lastResource == NodeBase.COLON ||
        lastResource == NodeBase.SEMICOLON ||
        lastResource == NodeBase.HYPHEN ||
        lastResource == NodeBase.RIGHT_PARENTHESIS ||
        lastResource == NodeBase.RIGHT_BRACKET ||
        lastResource == NodeBase.RIGHT_BRACE) {
          if (resource == NodeBase.WORD_TYPE) {
            sb.append(" ");
          }
        }
      }
      sb.append(token.getTokenString());
      lastToken = token;
      token = token.getNext();
    }
    return sb.toString();
  }
}
