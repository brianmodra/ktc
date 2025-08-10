package com.ktc.ui;

import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TokenMouseEventHandler extends TokenEventHelper implements EventHandler<MouseEvent> {
  @Override
  public void handle(MouseEvent event) {
    if (!init(event)) {
      return;
    }
    double x = event.getX();
    cursor.setFocus(text);
    String fullText = text.getText();
    Font font = text.getFont();
    xOffsetToPointer = 0.0;
    for (charIndex = 0; charIndex < text.getText().length(); charIndex++) {
      Text upToChar = new Text(fullText.substring(0, charIndex + 1));
      upToChar.setFont(font);
      double widthToChar = upToChar.getLayoutBounds().getWidth();
      if (x < widthToChar) {
        break;
      }
      xOffsetToPointer = widthToChar;
    }

    redraw();

    event.consume();
  }
}