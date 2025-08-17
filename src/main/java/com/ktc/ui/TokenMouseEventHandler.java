package com.ktc.ui;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class TokenMouseEventHandler implements EventHandler<MouseEvent> {
  Cursor cursor;
  Pane pane;
  ArrayList<Node> shapes = new ArrayList<>();

  public TokenMouseEventHandler(Cursor cursor, Pane pane) {
    this.cursor = cursor;
    this.pane = pane;
  }

  @Override
  public void handle(MouseEvent event) {
    if (!cursor.prepareToHandleEvent(event)) {
      return;
    }
    if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
      cursor.updatePosition(event.getX());
    } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
      TokenShape text = cursor.getTarget();
      Bounds bounds = text.getBoundsInParent();
      Rectangle rect = new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), text.getLayoutBounds().getHeight());
      Color fill = Color.GREEN.deriveColor(0, 1, 2, 0.2);
      rect.setFill(fill);
      pane.getChildren().add(rect);
      shapes.add(rect);
      cursor.updateTooltip();
    } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
      shapes.forEach(shape -> { pane.getChildren().remove(shape); });
      shapes.clear();
      cursor.eraseTooltip();
    }

    event.consume();
  }
}