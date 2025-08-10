package com.ktc.ui;

import javafx.scene.shape.Shape;

public class Cursor {
  final private StyledShape cursorShape;
  private Shape focus = null;
  private int focusOffset = 0;

  public Cursor(StyledShape cursorShape) {
    this.cursorShape = cursorShape;
  }

  public StyledShape getCursorShape() {
    return cursorShape;
  }

  public Shape getFocus() {
    return focus;
  }

  public void setFocus(Shape focus) {
    focus.requestFocus();
    this.focus = focus;
  }

  public int getFocusOffset() {
    return focusOffset;
  }

  public void setFocusOffset(int focusOffset) {
    this.focusOffset = focusOffset;
  }
}
