package com.ktc.ui;

import com.ktc.nlp.FineGrainedNERTag;
import com.ktc.nlp.PennTreebankPOSTag;
import com.ktc.text.NodeAnnotation;
import com.ktc.text.NodeBase;
import javafx.animation.PauseTransition;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;
import java.util.stream.Collectors;

public class Cursor {
  final private Rectangle cursorRect;
  Text tooltipText = null;
  Rectangle tooltipRect = null;
  Rectangle otherTooltipRect = null;
  final private ChapterEditor chapterEditor;
  final private Pane pane;
  protected EventTarget target;
  protected TokenShape text = null;
  protected int charIndex = 0;
  protected double xOffsetToPointer = 0;
  protected PauseTransition tooltipPause = new PauseTransition(Duration.seconds(0.3));
  protected PauseTransition nlpPause = new PauseTransition(Duration.seconds(2));

  public Cursor(ChapterEditor chapterEditor, Pane pane) {
    cursorRect = new Rectangle(0, 0);
    cursorRect.setFill(Color.BLUE.deriveColor(0, 1.2, 1.3, 0.75));
    this.pane = pane;
    this.chapterEditor = chapterEditor;
    pane.getChildren().add(cursorRect);

    tooltipPause.setOnFinished(event -> {
      drawTooltip();
    });
    nlpPause.setOnFinished(event -> {
      removeNLPGraphics();
    });
  }

  public int getCharIndex() {
    return charIndex;
  }

  public TokenShape getTarget() {
    return text;
  }

  public boolean prepareToHandleEvent(Event event) {
    target = event.getTarget();
    if (!(target instanceof TokenShape)) {
      return false;
    }
    initialiseTarget((TokenShape) target);
    return true;
  }

  private boolean initialiseTarget(TokenShape text) {
    if (this.text != text) {
      TokenShape previous = this.text;
      this.text = text;
      charIndex = 0;
      xOffsetToPointer = 0;
      nlpPause.pause();
      chapterEditor.removeNLPGraphics();
      chapterEditor.addNLPGraphics(text);
      nlpPause.playFromStart();
      return true;
    }
    return false;
  }

  private void removeNLPGraphics() {
    chapterEditor.removeNLPGraphics();
  }

  public void setFocusToToken(TokenShape text) {
    if (initialiseTarget(text)) {
      text.requestFocus();
      redraw();
    }
  }

  public Bounds getTargetBounds() {
    return text.getLayoutBounds();
  }

  private void drawTooltip() {
    List<NodeAnnotation> annotations = text.getToken().getAnnotations(FineGrainedNERTag.property);
    List<String> strings = annotations.stream()
        .map(NodeAnnotation::getDescription)
        .filter(s -> !s.isEmpty())
        .collect(Collectors.toList());
    annotations = text.getToken().getAnnotations(PennTreebankPOSTag.property);
    List<PennTreebankPOSTag.TagPair> tagPairs = annotations.stream()
        .filter(tag -> tag instanceof PennTreebankPOSTag)
        .map(tag -> ((PennTreebankPOSTag)tag).pairedWith())
        .filter(tagPair -> tagPair != null)
        .collect(Collectors.toList());
    strings.addAll(annotations.stream()
        .map(NodeAnnotation::getDescription)
        .filter(s -> !s.isEmpty())
        .collect(Collectors.toList()));
    annotations = text.getToken().getAnnotations(NodeBase.LABEL);
    strings.addAll(annotations.stream()
        .map(NodeAnnotation::getDescription)
        .filter(s -> !s.isEmpty())
        .collect(Collectors.toList()));
    String str = strings.stream()
        .collect(Collectors.joining(", "));

    Bounds bounds = text.getBoundsInParent();
    double x = bounds.getMinX();
    double y = bounds.getMinY() - 10;
    tooltipText = new Text(str);
    tooltipText.setX(x);
    tooltipText.setY(y);
    Bounds tooltipBounds = tooltipText.getBoundsInParent();
    tooltipRect = new Rectangle(tooltipBounds.getMinX(), tooltipBounds.getMinY(), tooltipBounds.getWidth(), tooltipBounds.getHeight());
    tooltipRect.setFill(Color.WHITE);
    pane.getChildren().add(tooltipRect);
    pane.getChildren().add(tooltipText);

    for (PennTreebankPOSTag.TagPair tagPair : tagPairs) {
      TokenShape other = text.findPair(tagPair);
      if (other == null) {
        continue;
      }
      Bounds otherBounds = other.getBoundsInParent();
      otherTooltipRect = new Rectangle(otherBounds.getMinX(), otherBounds.getMinY(), otherBounds.getWidth(), other.getLayoutBounds().getHeight());
      otherTooltipRect.setFill(Color.RED.deriveColor(0, 1, 2, 0.2));
      pane.getChildren().add(otherTooltipRect);
      break;
    }
  }

  protected void eraseTooltip() {
    if (tooltipText != null) {
      pane.getChildren().remove(tooltipText);
      tooltipText = null;
    }
    if (tooltipRect != null) {
      pane.getChildren().remove(tooltipRect);
      tooltipRect = null;
    }
    if (otherTooltipRect != null) {
      pane.getChildren().remove(otherTooltipRect);
      otherTooltipRect = null;
    }
  }

  protected void updateTooltip() {
    tooltipPause.pause();
    eraseTooltip();
    tooltipPause.playFromStart();
  }

  public void updatePosition(double x) {
    text.requestFocus();
    String fullText = text.getText();
    Font font = text.getFont();
    xOffsetToPointer = 0.0;
    int textLength = fullText.length();
    for (charIndex = 0; charIndex < textLength; charIndex++) {
      Text upToChar = new Text(fullText.substring(0, charIndex + 1));
      upToChar.setFont(font);
      double widthToChar = upToChar.getLayoutBounds().getWidth();
      if (x < widthToChar) {
        break;
      }
      xOffsetToPointer = widthToChar;
    }
    if (charIndex == 0) {
      TokenShape prev = text.getChapterEditor().getTokenBefore(text);
      if (prev != null) {
        text = prev;
        text.requestFocus();
        charIndex = prev.getText().length();
        xOffsetToPointer = text.getLayoutBounds().getWidth();
      }
    }

    redraw();
  }

  public boolean updateLeft() {
    if (charIndex == 0) {
      return false;
    }
    charIndex--;
    updateFromKeyPress();
    return true;
  }

  public boolean updateRight() {
    if (charIndex >= text.getText().length() - 1) {
      return false;
    }
    charIndex++;
    updateFromKeyPress();
    return true;
  }

  protected void updateFromKeyPress() {
    updateFromKeyPress(-1);
  }

  protected void updateFromKeyPress(int newCharIndex) {
    String fullText = text.getText();
    if (newCharIndex >= 0) {
      charIndex = newCharIndex;
    }
    if (charIndex >= fullText.length()) {
      charIndex = fullText.length() - 1;
    } else if (charIndex < 0) {
      charIndex = 0;
    }

    text.requestFocus();

    Font font = text.getFont();
    xOffsetToPointer = 0;
    if (charIndex > 0) {
      if (charIndex == fullText.length()) {
        xOffsetToPointer = text.getLayoutBounds().getWidth();
      } else {
        Text upToChar = new Text(fullText.substring(0, charIndex));
        upToChar.setFont(font);
        xOffsetToPointer = upToChar.getLayoutBounds().getWidth();
      }
    }

    redraw();
  }

  protected void updateFromFocus() {
    updateFromKeyPress(-1);
  }

  protected void redraw() {
    Bounds bounds = text.getBoundsInParent();
    double cursorX = bounds.getMinX() + xOffsetToPointer;
    double cursorY = bounds.getMinY();

    cursorRect.setX(cursorX);
    cursorRect.setY(cursorY);
    cursorRect.setWidth(2);
    cursorRect.setHeight(text.getLayoutBounds().getHeight());
  }
}
