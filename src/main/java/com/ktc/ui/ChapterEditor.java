package com.ktc.ui;

import com.ktc.nlp.FineGrainedNERTag;
import com.ktc.nlp.PennTreebankPOSTag;
import com.ktc.nlp.Tokeniser;
import com.ktc.text.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;

import java.util.*;
import java.util.stream.Collectors;


public class ChapterEditor extends Application {
  protected ChapterText chapter;
  Pane pane;
  TextFlow textFlow;
  TokenMouseEventHandler mouseHandler;
  TokenKeyEventHandler keyHandler;
  Cursor cursor;
  Tokeniser tokeniser;
  ArrayList<Node> temporaryGraphics = new ArrayList<>();

  @Override
  public void start(Stage stage) {
    Image backgroundImage = new Image("com/ktc/ui/paper.jpg");
    BackgroundImage bgImage = new BackgroundImage(
        backgroundImage,
        BackgroundRepeat.REPEAT, // or REPEAT, ROUND, SPACE
        BackgroundRepeat.REPEAT,
        BackgroundPosition.DEFAULT, // or specific position
        BackgroundSize.DEFAULT // or specific size like new BackgroundSize(100, 100, true, true, true, false)
    );
    Background background = new Background(bgImage);
    pane = new Pane();
    cursor = new Cursor(this, pane);
    tokeniser = new Tokeniser();
    keyHandler = new TokenKeyEventHandler(cursor);
    mouseHandler = new TokenMouseEventHandler(cursor, pane);

    stage.setTitle("Chapter Editor");
    stage.setScene(new Scene(new BorderPane()));

    textFlow = new TextFlow();
    textFlow.setLineSpacing(30.0);
    Insets insets = new Insets(15, 15, 15, 15);
    textFlow.setPadding(insets);
    textFlow.setTextAlignment(TextAlignment.LEFT);
    textFlow.setPadding(new Insets(30, 0, 0, 0));

    String str = "The cat sat on the mat, then the dog ate the cat's food. There was \"peace\" in the house. However, Joe had to clean up the mess.";
    DocumentText doc = new DocumentText();
    ChapterText chapter = new ChapterText();
    doc.addChild(chapter);
    ParagraphText paragraph = new ParagraphText();
    chapter.addChild(paragraph);
    Font font = Font.font("Helvetica", FontPosture.REGULAR, 15);
    ArrayList<SentenceText> sentences = tokeniser.getSentence(str, doc);
    for (SentenceText sentence : sentences) {
      paragraph.addChild(sentence);
      addSentenceText(sentence, font);
    }

    StackPane stackPane = new StackPane(pane, textFlow);
    stackPane.setBackground(background);

    pane.addEventHandler(EventType.ROOT, event -> textFlow.fireEvent(event));

    Scene scene = new Scene(stackPane, 400, 300);

    stage.setScene(scene);

    stage.show();
  }

  public void setChapter(ChapterText chapter, Font font) {
    this.chapter = chapter;
    List<ParagraphText> paragraphs = chapter.getChildren();
    for (ParagraphText paragraph : paragraphs) {
      addParagraphText(paragraph, font);
    }
  }

  public void addParagraphText(ParagraphText paragraph, Font font) {
    List<SentenceText> sentences = paragraph.getChildren();
    for (SentenceText sentence : sentences) {
      addSentenceText(sentence, font);
    }
  }

  public void addSentenceText(SentenceText sentenceText, Font font) {
    List<TokenText> tokens = sentenceText.getChildren();
    for (TokenText token : tokens) {
      addTokenText(token, font, -1);
    }
  }

  public void addTokenText(TokenText token, Font font, int index) {
    TokenShape text = new TokenShape(this);
    text.setToken(token);
    token.setUiObject(text);
    text.setFocusTraversable(true);
    text.setFont(font);
    ObservableList<Node> children = textFlow.getChildren();
    if (index == -1) {
      children.add(text);
    } else {
      children.add(index, text);
    }
    text.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseHandler);
    text.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseHandler);
    text.addEventFilter(MouseEvent.MOUSE_EXITED, mouseHandler);
    text.addEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
    text.addEventFilter(KeyEvent.KEY_RELEASED, keyHandler);
    text.focusedProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        cursor.setFocusToToken(text);
        cursor.updateFromFocus();
      }
    });
  }

  public boolean addSentenceTextAfter(SentenceText sentence, int index, Font font) {
    if (index == -1) {
      return false;
    }

    List<TokenText> tokens = sentence.getChildren();
    for (TokenText token : tokens) {
      addTokenText(token, font, index);
      index++;
    }

    return true;
  }

  public boolean removeSentence(SentenceText sentence) {
    boolean removedSome = false;
    boolean notAllFound = false;
    List<TokenText> tokens = sentence.getChildren();
    for (TokenText token : tokens) {
      Object uiObject = token.getUiObject();
      if (uiObject == null || !(uiObject instanceof TokenShape)) {
        notAllFound = true;
        continue;
      }
      TokenShape tokenShape = (TokenShape) uiObject;
      if (remove(tokenShape)) {
        removedSome = true;
      } else {
        notAllFound = true;
      }
    }
    return removedSome && !notAllFound;
  }

  public TokenShape getTokenAfter(TokenShape text) {
    ObservableList<Node> children = textFlow.getChildren();
    int i = children.indexOf(text);
    if (i < 0) {
      return text;
    }
    while (i < children.size() - 2) {
      i++;
      Node n = children.get(i);
      if (n instanceof TokenShape) {
        return (TokenShape) n;
      }
    }
    return text;
  }

  public TokenShape getTokenBefore(TokenShape text) {
    ObservableList<Node> children = textFlow.getChildren();
    int i = children.indexOf(text);
    if (i < 0) {
      return text;
    }
    while (i > 0) {
      i--;
      Node n = children.get(i);
      if (n instanceof TokenShape) {
        return (TokenShape) n;
      }
    }
    return text;
  }

  public boolean remove(TokenShape text) {
    return textFlow.getChildren().remove(text);
  }

  public int getIndex(TokenShape tokenShape) {
    return textFlow.getChildren().indexOf(tokenShape);
  }

  static HashMap<Class, Color> nlpColours = new HashMap();
  static {
    nlpColours.put(TripleSubject.class, Color.BLUE);
    nlpColours.put(TriplePredicate.class, Color.GREEN);
    nlpColours.put(TripleObject.class, Color.ORANGE);
  }

  public void addNLPGraphics(TokenShape text) {

    Bounds textBounds = text.getBoundsInParent();

    double x = textBounds.getMinX();
    double y = textBounds.getMinY() + textBounds.getHeight() / 5;

    List<NodeBase> tripleChildren = text.getToken().getTripleChildren();
    for (NodeBase node : tripleChildren) {
      NodeBase statementNode = node.getParentNode();
      if (statementNode == null || ! (statementNode instanceof TripleStatement)) {
        return;
      }
      TripleStatement statement = (TripleStatement) statementNode;
      List<NodeBase> tripleComponents = statement.allChildNodes();
      for (NodeBase tripleComponent : tripleComponents) {
        if (!(tripleComponent instanceof TripleComponent)) {
          continue;
        }
        List<TokenShape> shapes = ((TripleComponent) tripleComponent).getChildren()
            .stream()
            .map(token -> {
              Object uiObject = token.getUiObject();
              if (uiObject == null || !(uiObject instanceof TokenShape)) {
                return null;
              }
              return (TokenShape) uiObject;
            })
            .filter(token -> token != null)
            .collect(Collectors.toList());
        for (TokenShape shape : shapes) {
          Color paint = nlpColours.get(tripleComponent.getClass());
          if (paint != null) {
            underline(shape, paint);
          }
        }
      }
    }
  }

  public void underline(TokenShape text, Color paint) {
    Bounds textBounds = text.getBoundsInParent();

    double x = textBounds.getMinX();
    double y = textBounds.getMinY() + text.getLayoutBounds().getHeight() * 1.2;
    double width = textBounds.getWidth();

    ArrayList graphics = new ArrayList();
    Line line = new Line(x, y, x + width, y);
    line.setStroke(paint);
    line.setStrokeWidth(2);
    graphics.add(line);
    temporaryGraphics.addAll(graphics);
    pane.getChildren().add(line);
  }

  public void removeNLPGraphics() {
    for (Node node : temporaryGraphics) {
      pane.getChildren().remove(node);
    }
    temporaryGraphics.clear();
  }

  public Tokeniser getTokeniser() {
    return tokeniser;
  }

  public static void main(String args[])
  {
    launch(args);
  }
}
