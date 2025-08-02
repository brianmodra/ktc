package com.ktc.text;

import java.util.ArrayList;
import java.util.UUID;

import org.apache.jena.rdf.model.Resource;

public abstract class StructuredNode<T extends NodeBase, P extends NodeBase, C extends NodeBase> extends NodeBase {

  public StructuredNode(Resource type) {
    super(type);
  }

  protected StructuredNode(Resource type, UUID id) {
    super(type, id);
  }

  protected void setParent(P parent) {
    super.setParentNode(parent);
  }

  public SentenceText getParent() {
    return (SentenceText) super.getParentNode();
  }

  protected void setNext(T next) {
    super.setNextNode(next);
  }

  protected void setPrevious(T previous) {
    super.setPreviousNode(previous);
  }

  public T getNext() {
    return (T) super.getNextNode();
  }

  public T getPrevious() {
    return (T) super.getPreviousNode();
  }

  public C lastChild() {
    return (C) super.lastChildNode();
  }

  public C firstChild() {
    return (C) super.firstChildNode();
  }

  public void removeChild(C child) {
    super.removeChildNode(child);
  }

  public void addChild(C child) {
    super.addChildNode(child);
  }

  public void addChild(int index, C child) {
    super.addChildNode(index, child);
  }

  public ArrayList<C> getChildren() {
    C child = firstChild();
    ArrayList<C> children = new ArrayList<>();
    while (child != null) {
      children.add(child);
      child = (C) child.getNextNode();
    }    
    return children;
  }
}
