package com.ktc.ui;

import com.ktc.text.TokenText;
import javafx.scene.text.Text;

public class TokenShape extends Text {
  private TokenText token;
  private final ChapterEditor chapterEditor;

  public TokenShape(ChapterEditor chapterEditor) {
    super("");
    this.chapterEditor = chapterEditor;
  }

  public void setToken(TokenText token) {
    this.token = token;
  }

  public TokenText getToken() {
    return token;
  }

  public ChapterEditor getChapterEditor() {
    return chapterEditor;
  }
}
