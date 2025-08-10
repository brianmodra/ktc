package com.ktc.ui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.skin.TextInputControlSkin;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



public class ChapterEditor extends Application {
  private Cursor cursor;
  private ResizableCanvas canvas;
  TokenKeyEventHandler keyHandler = new TokenKeyEventHandler();
  TokenMouseEventHandler mouseHandler = new TokenMouseEventHandler();

  public Cursor getCursor() {
    return cursor;
  }

  public Canvas getCanvas() {
    return canvas;
  }

  @Override
  public void start(Stage stage) {
    canvas = new ResizableCanvas();
    Image backgroundImage = new Image("com/ktc/ui/paper.jpg");
    BackgroundImage bgImage = new BackgroundImage(
        backgroundImage,
        BackgroundRepeat.REPEAT, // or REPEAT, ROUND, SPACE
        BackgroundRepeat.REPEAT,
        BackgroundPosition.DEFAULT, // or specific position
        BackgroundSize.DEFAULT // or specific size like new BackgroundSize(100, 100, true, true, true, false)
    );
    Background background = new Background(bgImage);
    final Rectangle cursorRect = new Rectangle(0, 0);
    final StyledShape cursorShape = new StyledRectangle(cursorRect, Color.BLUE.deriveColor(0, 1.2, 1, 0.5));
    cursor = new Cursor(cursorShape);
    canvas.add(cursorShape);


    stage.setTitle("Chapter Editor");
    stage.setScene(new Scene(new BorderPane()));

    TextFlow textFlow = new TextFlow();

    TokenShape text = new TokenShape(this);
    text.setText("Hello World!");
    text.setFocusTraversable(true);
    text.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
    textFlow.getChildren().add(text);
    text.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseHandler);
    text.addEventFilter(KeyEvent.KEY_PRESSED, keyHandler);

    StackPane stackPane = new StackPane(canvas, textFlow);
    stackPane.setBackground(background);

    canvas.addEventHandler(EventType.ROOT, event -> textFlow.fireEvent(event));

    Scene scene = new Scene(stackPane, 400, 300);

    stage.setScene(scene);

    stage.show();
  }

  public static void main(String args[])
  {
      launch(args);
  }
}
