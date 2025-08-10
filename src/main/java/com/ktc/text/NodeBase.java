package com.ktc.text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.w3c.dom.Node;

/**
 * Base class for all text elements that need UUID management
 */
public abstract class NodeBase {
  // Using RDFS for labels
  public static final Property LABEL = RDFS.label;  // Use standard rdfs:label for text content
  
  // Document type URIs using NIF and standard vocabularies
  public static final Resource DOCUMENT_TYPE = ResourceFactory.createResource("http://purl.org/dc/dcmitype/Text");
  public static final Resource CHAPTER_TYPE = ResourceFactory.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Chapter");
  public static final Resource STRING_TYPE = ResourceFactory.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#String");  
  public static final Resource PARAGRAPH_TYPE = ResourceFactory.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Paragraph");  
  public static final Resource SENTENCE_TYPE = ResourceFactory.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Sentence");
  public static final Resource WORD_TYPE = ResourceFactory.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Word");
  public static final Resource TOKEN_TYPE = ResourceFactory.createResource("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#Token");

  // Standard RDF vocabulary for SPO triples
  public static final Resource SUBJECT_TYPE = ResourceFactory.createResource("http://www.w3.org/1999/02/22-rdf-syntax-ns#subject");
  public static final Resource PREDICATE_TYPE = ResourceFactory.createResource("http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate");
  public static final Resource OBJECT_TYPE = ResourceFactory.createResource("http://www.w3.org/1999/02/22-rdf-syntax-ns#object");
  public static final Resource STATEMENT_TYPE = ResourceFactory.createResource("http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement");

  // Standard RDF for sem
  public static final Resource ACTOR_TYPE = ResourceFactory.createResource("http://www.ontologydesignpatterns.org/ont/semantics.owl#Actor");

  // Document type URIs using OLiA namespace
  public static final Resource PUNCTUATION_TYPE = ResourceFactory.createResource("http://purl.org/olia/olia.owl#Punctuation");
  public static final Resource QUOTATION_MARK = ResourceFactory.createResource("http://purl.org/olia/olia.owl#QuotationMark");
  public static final Resource EXCLAMATION_MARK = ResourceFactory.createResource("http://purl.org/olia/penn.owl#ExclamationMark");
  public static final Resource QUESTION_MARK = ResourceFactory.createResource("http://purl.org/olia/penn.owl#QuestionMark");
  public static final Resource PERIOD = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Period");
  public static final Resource COMMA = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Comma");
  public static final Resource COLON = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Colon");
  public static final Resource SEMICOLON = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Semicolon");
  public static final Resource HYPHEN = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Hyphen");
  public static final Resource LEFT_PARENTHESIS = ResourceFactory.createResource("http://purl.org/olia/penn.owl#LeftParenthesis");
  public static final Resource RIGHT_PARENTHESIS = ResourceFactory.createResource("http://purl.org/olia/penn.owl#RightParenthesis");
  public static final Resource LEFT_BRACKET = ResourceFactory.createResource("http://purl.org/olia/penn.owl#LeftBracket");
  public static final Resource RIGHT_BRACKET = ResourceFactory.createResource("http://purl.org/olia/penn.owl#RightBracket");
  public static final Resource QUOTE = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Quote");
  public static final Resource SLASH = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Slash");
  public static final Resource BACKSLASH = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Backslash");
  public static final Resource ELLIPSIS = ResourceFactory.createResource("http://purl.org/olia/penn.owl#Ellipsis");
  public static final Resource LEFT_BRACE = ResourceFactory.createResource("http://purl.org/olia/penn.owl#LeftBrace");
  public static final Resource RIGHT_BRACE = ResourceFactory.createResource("http://purl.org/olia/penn.owl#RightBrace");

  public static final Property POS = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#PoS");

  // RDF properties for provenance
  public static final Property PROVENANCE_QUALITY_MEASURE = ResourceFactory.createProperty("http://www.w3.org/ns/prov#qualityMeasure");
  public static final Property PROVENANCE_WAS_DERIVED_FROM = ResourceFactory.createProperty("http://www.w3.org/ns/prov#wasDerivedFrom");

  // Standard RDF vocabulary
  public static final Property RDF_TYPE = RDF.type;
  public static final Property RDF_SUBJECT = RDF.subject;
  public static final Property RDF_PREDICATE = RDF.predicate;
  public static final Property RDF_OBJECT = RDF.object;

  protected final Resource type;
  protected final UUID id;
  protected LinkMap links;
  protected CopyOnWriteArrayList<NodeAnnotation> annotations = new CopyOnWriteArrayList<NodeAnnotation>();
  private String parentKey = null;
  private String childKey = null;
  private final String thisKey;

  /**
   * Constructor that generates a random UUID
   */
  protected NodeBase(Resource type) {
    this.type = type;
    this.id = UUID.randomUUID();
    this.links = new LinkMap();
    this.thisKey = getThisKey();
  }

  /**
   * Constructor with provided UUID
   */
  protected NodeBase(Resource type, UUID id) {
    this.type = type;
    this.id = id;
    this.links = new LinkMap();
    this.thisKey = getThisKey();
  }

  public UUID getId() {
    return id;
  }

  public Resource getResource() {
    return type;
  }

  public String getNameSpace() {
    return type.getNameSpace();
  }

  public String getLocalName() {
    return type.getLocalName();
  }

  public String getURI() {
    return type.getURI();
  }

  public void addLink(Link link) {
    this.links.add(link);
  }

  public List<NodeAnnotation> getAnnotations(Property property) {
    ArrayList<NodeAnnotation> results = new ArrayList<>();
    for (NodeAnnotation annotation : this.annotations) {
      if (annotation.getProperty().equals(property)) {
        results.add(annotation);
      }
    }
    return results;
  }

  public void addAnnotation(NodeAnnotation annotation) {
    this.annotations.add(annotation);
  }

  public boolean removeAnnotation(NodeAnnotation annotation) {
    return this.annotations.remove(annotation);
  }

  public boolean removeAnnotations(Property property) {
    boolean removedSome = false;
    CopyOnWriteArrayList<NodeAnnotation> results = new CopyOnWriteArrayList<>();
    for (NodeAnnotation annotation : this.annotations) {
      if (!annotation.getProperty().equals(property)) {
        results.add(annotation);
      } else {
        removedSome = true;
      }
    }
    this.annotations = results;
    return removedSome;
  }

  private NodeBase getLinkedTargetNode(Property property, String key, boolean mustBeLinkable) {
    if (key == null || property == null) {
      return null;
    }
    List<Link> childLinks = this.links.getLinks(property, key);
    for (Link link : childLinks) {
      NodeBase target = link.getTarget();
      if (mustBeLinkable && target.getNextProperty() == null) {
        continue;
      }
      return target;
    }
    return null;
  }

  public boolean removeLink(Link link) {
    return links.remove(link);
  }

  public boolean removeLink(Property property, String key) {
    return links.remove(property, key);
  }

  public boolean removeLink(Property property, String key, NodeBase targetNode) {
    return links.remove(property, key, targetNode);
  }

  public Link getLink(Property property, String key) {
    return links.getLink(property, key);
  }

  public List<Link> getLinks(Property property) {
    return links.getLinks(property);
  }

  public Link createUniqueLink(NodeBase targetNode, Property property, String key) {
    removeLink(property, key);
    Link link = new Link(property, this, targetNode, key);
    addLink(link);
    return link;
  }

  public void createUniqueTwoWayLink(NodeBase targetNode, Property toProperty, String toKey, Property fromProperty, String fromKey) {
    Link to = createUniqueLink(targetNode, toProperty, toKey);
    Link from = targetNode.createUniqueLink(this, fromProperty, fromKey);
    to.setReverseLink(from);
    from.setReverseLink(to);
  }

  public Link createLink(NodeBase targetNode, Property property, String key) {
    Link link = new Link(property, this, targetNode, key);
    addLink(link);
    return link;
  }

  public void createTwoWayLink(NodeBase targetNode, Property toProperty, String toKey, Property fromProperty, String fromKey) {
    Link to = createLink(targetNode, toProperty, toKey);
    Link from = targetNode.createLink(this, fromProperty, fromKey);
    to.setReverseLink(from);
    from.setReverseLink(to);
  }

  protected void setParentNode(NodeBase parent) {
    if (parent != null && parentKey == null) {
      parentKey = parent.getKey();
    }
    Property parentProperty = getParentProperty();
    if (parent == null) {
      removeLink(parentProperty, parentKey);
      return;
    }
    if (parentKey == null || parentProperty == null) {
      return;
    }
    if (!parentCanBe(parent.getClass())) {
      throw new IllegalArgumentException("Parent of (" + getClass().getSimpleName() + ") cannot be (" + parent.getClass().getSimpleName() + ")");
    }
    if (links.contains(parentProperty, parentKey)) {
      NodeBase p = getParentNode();
      if (p == parent) {
        return;
      }
      throw new IllegalArgumentException("Parent already set");
    }
    createUniqueLink(parent, parentProperty, parentKey);
  }

  public NodeBase getParentNode() {
    Property parentProperty = getParentProperty();
    if (parentKey == null || parentProperty == null) {
      return null;
    }
    return getLinkedTargetNode(parentProperty, parentKey, false);
  }

  protected void setNextNode(NodeBase next) {
    String nextKey = getNextKey();
    Property nextProperty = getNextProperty();
    if (nextKey == null || nextProperty == null) {
      throw new IllegalArgumentException("This type of node (" + getClass().getSimpleName() + ") cannot have next nodes");
    }
    String previousKey = getPreviousKey();
    Property previousProperty = getPreviousProperty();
    if (previousKey == null || previousProperty == null) {
      throw new IllegalArgumentException("This type of node (" + getClass().getSimpleName() + ") cannot have previous nodes");
    }
    if (next == null) {
      removeLink(nextProperty, nextKey);
      return;
    }
    NodeBase existingTarget = getLinkedTargetNode(nextProperty, nextKey, true);
    if (existingTarget != null) {
      if (existingTarget == next) {
        return;
      }
      if (next.getNextNode() != null) {
        throw new IllegalArgumentException("Next must be an unlinked node");
      }
      next.createUniqueTwoWayLink(existingTarget, nextProperty, nextKey, previousProperty, previousKey);
    }
    NodeBase existingPrevious = next.getPreviousNode();
    if (existingPrevious != null) {
      if (existingPrevious == this) {
        return;
      }
      throw new IllegalArgumentException("Next must be an unlinked node");
    }
    createUniqueTwoWayLink(next, nextProperty, nextKey, previousProperty, previousKey);
  }

  protected void setPreviousNode(NodeBase previous) {
    String previousKey = getPreviousKey();
    Property previousProperty = getPreviousProperty();
    if (previousKey == null || previousProperty == null) {
      throw new IllegalArgumentException("This type of node (" + getClass().getSimpleName() + ") cannot have previous nodes");
    }
    String nextKey = getNextKey();
    Property nextProperty = getNextProperty();
    if (nextKey == null || nextProperty == null) {
      throw new IllegalArgumentException("This type of node (" + getClass().getSimpleName() + ") cannot have next nodes");
    }
    if (previous == null) {
      removeLink(previousProperty, previousKey);
      return;
    }
    NodeBase existingTarget = getLinkedTargetNode(previousProperty, previousKey, true);
    if (existingTarget != null) {
      if (existingTarget == previous) {
        return;
      }
      if (previous.getPreviousNode() != null) {
        throw new IllegalArgumentException("Previous must be an unlinked node");
      }
      previous.createUniqueTwoWayLink(existingTarget, previousProperty, previousKey, nextProperty, nextKey);
    }
    NodeBase existingNext = previous.getNextNode();
    if (existingNext != null) {
      if (existingNext == this) {
        return;
      }
      throw new IllegalArgumentException("Previous must be an unlinked node");
    }
    createUniqueTwoWayLink(previous, previousProperty, previousKey, nextProperty, nextKey);
  }

  public NodeBase getNextNode() {
    String nextKey = getNextKey();
    Property nextProperty = getNextProperty();
    if (nextKey == null || nextProperty == null) {
      return null;
    }
    return getLinkedTargetNode(nextProperty, nextKey, true);
  }

  public NodeBase getPreviousNode() {
    String previousKey = getPreviousKey();
    Property previousProperty = getPreviousProperty();
    if (previousKey == null || previousProperty == null) {
      return null;
    }
    return getLinkedTargetNode(previousProperty, previousKey, true);
  }

  public NodeBase lastChildNode() {
    Property childProperty = getChildProperty();
    if (childKey == null || childProperty == null) {
      return null;
    }
    NodeBase lastNode = getLinkedTargetNode(childProperty, childKey, true);
    if (lastNode == null) {
      return null;
    }
    NodeBase nextNode;
    while ((nextNode = lastNode.getNextNode()) != null) {
      lastNode = nextNode;
    }
    return lastNode;
  }

  public List<NodeBase> allChildNodes() {
    Property childProperty = getChildProperty();
    if (childProperty == null) {
      return new ArrayList<>();
    }
    List<Link> childLinks = this.links.getLinks(childProperty);
    List<NodeBase> children = new ArrayList<>();
    for (Link link : childLinks) {
      children.add(link.getTarget());
    }
    return children;
  }

  public NodeBase firstChildNode() {
    Property childProperty = getChildProperty();
    if (childKey == null || childProperty == null) {
      return null;
    }
    NodeBase firstNode = getLinkedTargetNode(childProperty, childKey, true);
    if (firstNode == null) {
      return null;
    }
    NodeBase previousNode;
    while ((previousNode = firstNode.getPreviousNode()) != null) {
      firstNode = previousNode;
    }
    return firstNode;
  }

  public void removeChildNode(NodeBase child) {
    if (child == null) {
      throw new IllegalArgumentException("Child cannot be null");
    }
    if (childKey == null) {
      childKey = child.getKey();
    }
    Property childProperty = getChildProperty();
    if (childKey == null || childProperty == null) {
      throw new IllegalArgumentException("Child key or property cannot be null");
    }
    removeLink(childProperty, childKey, child);
    NodeBase previousChild = child.getPreviousNode();
    NodeBase nextChild = child.getNextNode();
    if (previousChild != null) {
      if (nextChild != null) {
        previousChild.createUniqueTwoWayLink(nextChild, previousChild.getNextProperty(), previousChild.getNextKey(), nextChild.getPreviousProperty(), nextChild.getPreviousKey());
      } else {
        previousChild.setNextNode(null);
      }
    } else if (nextChild != null) {
      nextChild.setPreviousNode(null);
    }
    if (!parentCanBe(child.getClass())) {
      child.setParentNode(null);
    }
    if (child.getNextNode() != null) {
      child.setNextNode(null);
    }
    if (child.getPreviousNode() != null) {
      child.setPreviousNode(null);
    }
  }

  public void addChildNode(NodeBase child) {
    if (child == null) {
      throw new IllegalArgumentException("Child cannot be null");
    }
    Property childProperty = getChildProperty();
    if (childProperty == null) {
      throw new IllegalArgumentException("This type of node (" + getClass().getSimpleName() + ") cannot have children");
    }
    if (child.parentCanBe(getClass())) {
      NodeBase lastChild = lastChildNode();
      addChildNodeInOrder(child, lastChild);
      return;  
    }
    createLink(child, childProperty, child.getKey());
  }

  protected void addChildNodeInOrder(NodeBase child, NodeBase afterChild) {
    if (child == null) {
      throw new IllegalArgumentException("Child cannot be null");
    }
    if (childKey == null) {
      childKey = child.getKey();
    }
    Property childProperty = getChildProperty();
    if (childKey == null || childProperty == null) {
      throw new IllegalArgumentException("This type of node (" + getClass().getSimpleName() + ") cannot have children");
    }
    if (afterChild == null) {
      NodeBase beforeChild = firstChildNode();
      if (beforeChild != null) {
        child.createUniqueTwoWayLink(beforeChild, child.getNextProperty(), child.getNextKey(), beforeChild.getPreviousProperty(), beforeChild.getPreviousKey());
      }
    } else {
      afterChild.setNextNode(child);
      child.setPreviousNode(afterChild);
    }
    // have to add the parent last, otherwise it may find itself and link itself to itself.
    child.setParentNode(this);
    createLink(child, childProperty, childKey);
  }

  public void addChildNode(int index, NodeBase child) {
    if (child == null) {
      throw new IllegalArgumentException("Child cannot be null");
    }
    if (childKey == null) {
      childKey = child.getKey();
    }
    if (index < 0) {
      throw new IllegalArgumentException("Index cannot be negative");
    }
    if (index == 0) {
      addChildNodeInOrder(child, null);
      return;
    }
    NodeBase afterChild = firstChildNode();
    for (int i = 1; i < index; i++) {
      NodeBase nextChild = afterChild.getNextNode();
      if (nextChild == null) {
        break;
      }
      afterChild = nextChild;
    }
    addChildNodeInOrder(child, afterChild);
  }

  public Property getParentProperty() {
    return Link.IS_PART_OF;
  }

  public Property getChildProperty() {
    return Link.HAS_PART;
  }

  public String getNextKey() {
    return "next";
  }

  public String getPreviousKey() {
    return "previous";
  }

  public String getKey() {
    return thisKey;
  }

  public abstract Property getNextProperty();

  public abstract Property getPreviousProperty();

  public abstract boolean parentCanBe(Class<? extends NodeBase> parentClass);

  private String getThisKey() {
    String className = getClass().getSimpleName();
    if (className.endsWith("Text")) {
      return className.substring(0, className.length() - 4).toLowerCase();
    }
    return className;
  }

  /**
   * Get the text representation of this text element
   */
  @Override
  public String toString() {
    String className = getClass().getSimpleName();
    return className + "(" + getId().toString() + ")";
  }
}
