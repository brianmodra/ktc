package com.ktc.ui;

import com.ktc.nlp.PennTreebankPOSTag;
import com.ktc.text.NodeBase;
import com.ktc.text.SentenceText;
import com.ktc.text.TokenText;
import javafx.scene.text.Text;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class TokenShape extends Text {
  private WeakReference<TokenText> token;
  private final ChapterEditor chapterEditor;

  public TokenShape(ChapterEditor chapterEditor) {
    super("");
    this.chapterEditor = chapterEditor;
  }

  public void setToken(TokenText token) {
    super.setText(token.getTokenString());
    this.token = new WeakReference<>(token);
  }

  public void changeText(String str) {
    super.setText(str);
    if (this.token != null) {
      this.token.get().removeNLP();
      this.token.get().setTokenString(str);
    }
  }

  public boolean removeNLP() {
    if (this.token == null) {
      return false;
    }
    SentenceText parent = this.token.get().getParent();
    if (parent == null) {
      return false;
    }
    return parent.removeNLP();
  }

  public boolean unparent() {
    if (this.token.get() == null) {
      return false;
    }
    SentenceText parent = this.token.get().getParent();
    if (parent == null) {
      return false;
    }
    parent.removeChildNode(this.token.get());
    return true;
  }

  public TokenText getToken() {
    return token.get();
  }

  public ChapterEditor getChapterEditor() {
    return chapterEditor;
  }

  public boolean isWordOrUnclassified() {
    if (this.token.get() == null) {
      return true;
    }
    return this.token.get().getResource() == NodeBase.WORD_TYPE;
  }

  public TokenShape findPair(PennTreebankPOSTag.TagPair pair) {
    ChapterEditor chapterEditor = getChapterEditor();
    TokenShape tokenShape = pair.otherToTheRight()? chapterEditor.getTokenAfter(this) : chapterEditor.getTokenBefore(this);
    while (tokenShape != null) {
      Optional<PennTreebankPOSTag> found = tokenShape.getToken().getAnnotations(PennTreebankPOSTag.property).stream()
          .filter(tag -> tag instanceof PennTreebankPOSTag)
          .map(tag -> (PennTreebankPOSTag) tag)
          .filter(tag -> tag == pair.tag() || tag == pair.other())
          .findFirst();
      if (found.isPresent()) {
        if (found.get() == pair.tag()) {
          return null;
        }
        if  (found.get() == pair.other()) {
          return tokenShape;
        }
      }
      tokenShape = pair.otherToTheRight()? chapterEditor.getTokenAfter(tokenShape) : chapterEditor.getTokenBefore(tokenShape);
    }
    return null;
  }
}
