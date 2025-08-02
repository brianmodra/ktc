package com.ktc.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class ChapterEditor extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Chapter Editor");
        stage.setScene(new Scene(new BorderPane()));

        TextFlow textFlow = new TextFlow();

        Text text = new Text("Hello world!");
        text.setFont(Font.font("Helvetica", FontPosture.ITALIC, 15));
        textFlow.getChildren().add(text);

        Scene scene = new Scene(textFlow, 400, 300);

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String args[])
    {
        launch(args);
    }
}
