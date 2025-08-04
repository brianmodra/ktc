package com.ktc.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTextTest {
  @Test
  public void testTokenTextBasics() {
    TokenText token = TokenText.create("Hello");
    assertEquals(NodeBase.WORD_TYPE, token.getResource());
    assertEquals("Hello", token.getTokenString());
      assertNull(token.getNext());
      assertNull(token.getPrevious());
      assertNull(token.firstChild());
      assertNull(token.getParent());
  }

  @Test
  public void testQuotedTextAsTokenTexts() {
    TokenText openQuotationToken = TokenText.create("\"");
    TokenText helloToken = TokenText.create("Hello");
    TokenText worldToken = TokenText.create("World");
    TokenText exclamationToken = TokenText.create("!");
    TokenText closeQuotationToken = TokenText.create("\"");

    assertEquals(NodeBase.QUOTATION_MARK, openQuotationToken.getResource());
    assertEquals(NodeBase.WORD_TYPE, helloToken.getResource());
    assertEquals(NodeBase.WORD_TYPE, worldToken.getResource());
    assertEquals(NodeBase.EXCLAMATION_MARK, exclamationToken.getResource());
    assertEquals(NodeBase.QUOTATION_MARK, closeQuotationToken.getResource());

    assertEquals("\"", openQuotationToken.getTokenString());
    assertEquals("Hello", helloToken.getTokenString());
    assertEquals("World", worldToken.getTokenString());
    assertEquals("!", exclamationToken.getTokenString());
    assertEquals("\"", closeQuotationToken.getTokenString());

    SentenceText sentence = new SentenceText();
    sentence.addChild(openQuotationToken);
    sentence.addChild(helloToken);
    sentence.addChild(worldToken);
    sentence.addChild(exclamationToken);
    sentence.addChild(closeQuotationToken);

    assertEquals(openQuotationToken, sentence.firstChildNode());
    assertEquals(helloToken, openQuotationToken.getNextNode());
    assertEquals(worldToken, helloToken.getNextNode());
    assertEquals(exclamationToken, worldToken.getNextNode());
    assertEquals(closeQuotationToken, exclamationToken.getNextNode());

    assertEquals("\"Hello World!\"", sentence.getTokensAsString());
  }
}
