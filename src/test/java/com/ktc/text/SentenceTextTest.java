package com.ktc.text;

import com.ktc.nlp.Tokeniser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SentenceTextTest {
  @Test
  public void testQuotedTextAsTokenTexts() {
    TokenText openQuotationToken = TokenText.create("\"");
    TokenText helloToken = TokenText.create("Hello");
    TokenText spaceToken = TokenText.create(" ");
    TokenText worldToken = TokenText.create("World");
    TokenText exclamationToken = TokenText.create("!");
    TokenText closeQuotationToken = TokenText.create("\"");

    assertEquals(NodeBase.QUOTATION_MARK, openQuotationToken.getResource());
    assertEquals(NodeBase.WORD_TYPE, helloToken.getResource());
    assertEquals(NodeBase.SPACE_TOKEN, spaceToken.getResource());
    assertEquals(NodeBase.WORD_TYPE, worldToken.getResource());
    assertEquals(NodeBase.EXCLAMATION_MARK, exclamationToken.getResource());
    assertEquals(NodeBase.QUOTATION_MARK, closeQuotationToken.getResource());

    assertEquals("\"", openQuotationToken.getTokenString());
    assertEquals("Hello", helloToken.getTokenString());
    assertEquals(" ", spaceToken.getTokenString());
    assertEquals("World", worldToken.getTokenString());
    assertEquals("!", exclamationToken.getTokenString());
    assertEquals("\"", closeQuotationToken.getTokenString());

    SentenceText sentence = new SentenceText();
    sentence.addChild(openQuotationToken);
    sentence.addChild(helloToken);
    sentence.addChild(spaceToken);
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

  @Test
  public void testSentenceTokenisation() {
    Tokeniser tokeniser = new Tokeniser();
    String str = "The cat sat on the mat, then the dog ate the cat's food.";
    DocumentText doc = new DocumentText();
    List<SentenceText> sentence = tokeniser.getSentence(str, doc);
    assertEquals(1, sentence.size());
    assertEquals(str, sentence.get(0).getTokensAsString());
  }
}
