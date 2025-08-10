package com.ktc.ui;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class TokenEventHelper {
  protected EventTarget target;
  protected TokenShape text;
  protected ChapterEditor chapterEditor;
  protected Canvas canvas;
  protected Cursor cursor;
  protected StyledShape cursorShape;
  protected int charIndex = 0;
  protected double xOffsetToPointer = 0;

  public boolean init(Event event) {
    target = event.getTarget();
    if (!(target instanceof TokenShape)) {
      return false;
    }
    text = (TokenShape) target;
    chapterEditor = text.getChapterEditor();
    canvas = chapterEditor.getCanvas();
    cursor = chapterEditor.getCursor();
    cursorShape = cursor.getCursorShape();

    return true;
  }

  public void redraw() {
    cursor.setFocusOffset(charIndex);

    GraphicsContext gc = canvas.getGraphicsContext2D();
    cursorShape.erase(gc);

    double cursorX = text.getX() + xOffsetToPointer;
    double cursorY = text.getY();
    Rectangle cursorRectangle = (Rectangle)cursorShape.getShape();
    cursorRectangle.setX(cursorX);
    cursorRectangle.setY(cursorY);
    cursorRectangle.setWidth(2);
    cursorRectangle.setHeight(text.getLayoutBounds().getHeight());
    cursorShape.draw(gc);
  }
}
