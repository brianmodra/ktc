package com.ktc.ui;

import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TokenKeyEventHandler extends TokenEventHelper implements EventHandler<KeyEvent> {
  @Override
  public void handle(KeyEvent event) {
    if (!init(event)) {
      return;
    }

    charIndex = cursor.getFocusOffset();
    if (target != cursor.getFocus()) {
      return;
    }
    switch (event.getCode()) {
      case RIGHT:
        charIndex++;
        break;
      case LEFT:
        charIndex--;
        break;
      default:
        return;
    }

    if (charIndex < 0) {
      return;
    }
    String fullText = text.getText();
    if (charIndex > fullText.length()) {
      return;
    }
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

    event.consume();
  }
}