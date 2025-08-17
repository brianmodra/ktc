package com.ktc.ui;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class StyledText implements StyledShape {
  final private Text shape;

  public StyledText(Text shape) {
    this.shape = shape;
  }

  @Override
  public Shape getShape() {
    return shape;
  }

  @Override
  public void draw(GraphicsContext gc) {
    String str = shape.getText();
    if (str == null || str.isEmpty()) {
      return;
    }
    gc.setFill(shape.getFill());
    gc.setFont(shape.getFont());
    gc.fillText(str, shape.getX(), shape.getY());
  }

  @Override
  public void erase(GraphicsContext gc) {
    String str = shape.getText();
    if (str == null || str.isEmpty()) {
      return;
    }
    Bounds bounds = shape.getLayoutBounds();
    gc.clearRect(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight());
  }
}
