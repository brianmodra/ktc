package com.ktc.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Paint;

public class StyledRectangle implements StyledShape {
  final private Rectangle shape;
  final private Paint paint;

  public StyledRectangle(Rectangle shape, Paint paint) {
    this.shape = shape;
    this.paint = paint;
  }

  @Override
  public Shape getShape() {
    return shape;
  }

  @Override
  public void draw(GraphicsContext gc) {
    if (shape.getWidth() > 0 && shape.getHeight() > 0) {
      gc.setFill(paint);
      gc.fillRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
    }
  }

  @Override
  public void erase(GraphicsContext gc) {
    if (shape.getWidth() > 0 && shape.getHeight() > 0) {
      gc.clearRect(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
    }
  }
}
