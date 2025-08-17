package com.ktc.ui;

import com.ktc.nlp.Tokeniser;
import com.ktc.text.*;
import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TokenKeyEventHandler implements EventHandler<KeyEvent> {
  protected PauseTransition retokenisePause = new PauseTransition(Duration.seconds(5));

  Cursor cursor;
  boolean capsLock = false;

  public TokenKeyEventHandler(Cursor cursor) {
    this.cursor = cursor;
  }

  @Override
  public void handle(KeyEvent event) {
    if (!cursor.prepareToHandleEvent(event)) {
      return;
    }

    if (event.getEventType() == KeyEvent.KEY_RELEASED) {
      if (event.getCode() == KeyCode.CAPS) {
        capsLock = false;
      }
      return;
    } else if (event.getCode() == KeyCode.CAPS) {
      capsLock = true;
      return;
    }

    switch (event.getCode()) {
      case RIGHT: {
        if (cursor.updateRight()) {
          break;
        }
        TokenShape text = cursor.getTarget();
        TokenShape next = text.getChapterEditor().getTokenAfter(text);
        if (next == text) {
          break;
        }
        cursor.setFocusToToken(next);
        cursor.updateFromKeyPress(0);
        break;
      }
      case LEFT: {
        if (cursor.updateLeft()) {
          break;
        }
        TokenShape text = cursor.getTarget();
        TokenShape prev = text.getChapterEditor().getTokenBefore(text);
        if (prev == text) {
          break;
        }
        cursor.setFocusToToken(prev);
        cursor.updateFromKeyPress(prev.getText().length() - 1);
        break;
      }
      default: {
        KeyCode keyCode = event.getCode();
        if (keyCode.isDigitKey() || keyCode.isLetterKey()) {
          TokenShape text = cursor.getTarget();
          if (text.removeNLP()) {
            scheduleRemakeNLP(text);
          }
          boolean shifted = event.isShiftDown() || capsLock;
          String c = shifted? keyCode.getChar() : keyCode.getChar().toLowerCase();
          int charIndex = cursor.getCharIndex();
          String start = text.getText().substring(0, charIndex);
          String end = text.getText().substring(charIndex);
          String newString = start + c + end;
          text.changeText(newString);
        } else if (keyCode == KeyCode.BACK_SPACE) {
          TokenShape text = cursor.getTarget();
          if (text.removeNLP()) {
            scheduleRemakeNLP(text);
          }
          String str = text.getText();
          if (str.length() == 1) {
            ChapterEditor editor = text.getChapterEditor();
            TokenShape next = editor.getTokenAfter(text);
            TokenShape prev = editor.getTokenBefore(text);
            if (prev == null && next == null) {
              return;
            }
            text.removeNLP();
            text.unparent();
            editor.remove(text);
            if (prev != null && next != null && prev.isWordOrUnclassified() && next.isWordOrUnclassified()) {
              String prevStr = prev.getText();
              prev.changeText(prevStr + next.getText());
              cursor.setFocusToToken(prev);
              cursor.updateFromKeyPress(prevStr.length());
              next.removeNLP();
              next.unparent();
              editor.remove(next);
            }
          } else if (text.isWordOrUnclassified()){
            int charIndex = cursor.getCharIndex();
            String start = charIndex > 0? str.substring(0, charIndex - 1) : "";
            String end = str.substring(charIndex);
            String newString = start + end;
            text.changeText(newString);
            text.removeNLP();
            if (charIndex == 0) {
              TokenShape prev = text.getChapterEditor().getTokenBefore(text);
              cursor.setFocusToToken(prev);
              cursor.updateFromKeyPress(prev.getText().length() - 1);
            } else {
              cursor.updateFromKeyPress(charIndex - 1);
            }
          } else {
            return;
          }
        }
        return;
      }
    }

    event.consume();
  }

  private void scheduleRemakeNLP(TokenShape removed) {
    TokenText token = removed.getToken();
    if (token == null) {
      return;
    }
    SentenceText thisSentence = token.getParent();
    if (thisSentence == null) {
      return;
    }
    ParagraphText paragraph = thisSentence.getParent();
    if (paragraph == null) {
      return;
    }
    ChapterText chapter = paragraph.getParent();
    if (chapter == null) {
      return;
    }
    DocumentText document = chapter.getParent();
    if (document == null) {
      return;
    }
    ChapterEditor chapterEditor = removed.getChapterEditor();
    Tokeniser tokeniser = chapterEditor.getTokeniser();
    TokenText firstToken = thisSentence.firstChild();
    if (firstToken == null) {
      return;
    }
    Object uiObject = firstToken.getUiObject();
    if (uiObject == null || !(uiObject instanceof TokenShape)) {
      return;
    }
    TokenShape firstShape = (TokenShape) uiObject;
    int index = chapterEditor.getIndex(firstShape);

    retokenisePause.pause();
    retokenisePause.setOnFinished(actionEvent -> {
      ArrayList<SentenceText> sentences = tokeniser.getSentence(thisSentence.getTokensAsString(), document);
      chapterEditor.removeSentence(thisSentence);
      SentenceText lastSentence = thisSentence;
      for (SentenceText sentence: sentences) {
        paragraph.addChildNodeInOrder(sentence, lastSentence);
        chapterEditor.addSentenceTextAfter(sentence, index, removed.getFont());
        lastSentence = sentence;
      }
      paragraph.removeChild(thisSentence);
      thisSentence.unlink();
    });
    retokenisePause.play();
  }
}