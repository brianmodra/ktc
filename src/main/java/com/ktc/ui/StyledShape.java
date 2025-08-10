package com.ktc.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Paint;

public interface StyledShape {
  public void draw(GraphicsContext gc);
  public void erase(GraphicsContext gc);
  public Shape getShape();
}
