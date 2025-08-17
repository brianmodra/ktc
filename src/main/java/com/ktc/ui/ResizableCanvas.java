package com.ktc.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

class ResizableCanvas extends Canvas {
  GraphicsContext gc = getGraphicsContext2D();
  int canvasWidth = 0;
  int canvasHeight = 0;
  int halfCanvasHeight = 0;

  ArrayList<StyledShape> shapes = new ArrayList<>();

  public ResizableCanvas() {
    widthProperty().addListener((observable, oldValue, newValue) -> {
      canvasWidth = (int)widthProperty().get();
    });
    heightProperty().addListener((observable, oldValue, newValue) -> {
      canvasHeight = (int) heightProperty().get();
      halfCanvasHeight = canvasHeight >> 1;
    });
  }

  public void add(StyledShape shape) {
    shapes.add(shape);
  }

  public boolean remove(StyledShape shape) {
    return shapes.remove(shape);
  }

  protected void draw() {
    gc.clearRect(0, 0, canvasWidth, canvasHeight);
    for (StyledShape shape : shapes) {
      shape.draw(gc);
    }
  }

  @Override
  public double minHeight(double width) {
    return 1;
  }

  @Override
  public double maxHeight(double width) {
    return Double.MAX_VALUE;
  }

  @Override
  public double prefHeight(double width) {
    return minHeight(width);
  }

  @Override
  public double minWidth(double height) {
    return 1;
  }

  @Override
  public double maxWidth(double height) {
    return Double.MAX_VALUE;
  }

  @Override
  public boolean isResizable() {
    return true;
  }

  @Override
  public void resize(double width, double height) {
    super.setWidth(width);
    super.setHeight(height);
    draw();
  }
}