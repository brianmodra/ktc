package com.ktc.text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

/**
 * Base class for all text elements that need UUID management
 */
public abstract class NodeBase<T extends NodeBase<T>> {
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

  /**
   * Constructor that generates a random UUID
   */
  protected NodeBase(Resource type) {
    this.type = type;
    this.id = UUID.randomUUID();
    this.links = new LinkMap();
  }

  /**
   * Constructor with provided UUID
   */
  protected NodeBase(Resource type, UUID id) {
    this.type = type;
    this.id = id;
    this.links = new LinkMap();
  }

  /**
   * Get the UUID for this text element
   */
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

  /**
   * Set the UUID of the parent text element
   */
  public void addLink(Link link) {
    this.links.add(link);
  }

  public Link getLinkByKey(Property property, String key) {
    return links.getLink(property, key);
  }

  public List<Link> getLinks(Property property) {
    return links.getLinks(property);
  }

  public final void setParentNode(NodeBase parent) {
    if (parent == null) {
      throw new IllegalArgumentException("Parent cannot be null");
    }
    Property parentProperty = getParentProperty();
    if (links.contains(parentProperty, getParentKey())) {
      throw new IllegalArgumentException("Parent already set");
    }
    Link link = new Link(parentProperty, this, parent, getParentKey());
    addLink(link);
    parent.addChildNode(this);
  }

  public final NodeBase getParentNode() {
    Link link = getLinkByKey(getParentProperty(), getParentKey());
    return link.getTarget();
  }

  public final void setNextNode(NodeBase next) {
    if (next == null) {
      throw new IllegalArgumentException("Next cannot be null");
    }
    Property nextProperty = getNextProperty();
    if (links.contains(nextProperty, getNextKey())) {
      throw new IllegalArgumentException("Next already set");
    }
    Link link = new Link(nextProperty, this, next, getNextKey());
    addLink(link);
  }

  public final void setPreviousNode(NodeBase previous) {
    if (previous == null) {
      throw new IllegalArgumentException("Previous cannot be null");
    }
    Property previousProperty = getPreviousProperty();
    if (links.contains(previousProperty, getPreviousKey())) {
      throw new IllegalArgumentException("Previous already set");
    }
    Link link = new Link(previousProperty, this, previous, getPreviousKey());
    addLink(link);
  }

  public final NodeBase getNextNode() {
    Link link = getLinkByKey(getNextProperty(), getNextKey());
    return link.getTarget();
  }

  public final NodeBase getPreviousNode() {
    Link link = getLinkByKey(getPreviousProperty(), getPreviousKey());
    return link.getTarget();
  }

  public final NodeBase lastChildNode() {
    String childKey = getChildKey();
    Property childProperty = getChildProperty();
    if (childKey == null || childProperty == null) {
      return null;
    }
    Link link = getLinkByKey(childProperty, childKey);
    if (link == null) {
      return null;
    }
    NodeBase lastNode = link.getTarget();
    NodeBase nextNode;
    while ((nextNode = lastNode.getNextNode()) != null) {
      lastNode = nextNode;
    }
    return lastNode;
  }

  public final NodeBase firstChildNode() {
    String childKey = getChildKey();
    Property childProperty = getChildProperty();
    if (childKey == null || childProperty == null) {
      return null;
    }
    Link link = getLinkByKey(childProperty, childKey);
    if (link == null) {
      return null;
    }
    NodeBase firstNode = link.getTarget();
    NodeBase previousNode;
    while ((previousNode = firstNode.getPreviousNode()) != null) {
      firstNode = previousNode;
    }
    return firstNode;
  }

  public final <T extends NodeBase> List<T> getChildNodes() {
    Class<T> nodeClass = (Class<T>) getClass();
    NodeBase firstNode = firstChildNode();
    if (firstNode == null) {
      return new ArrayList<>();
    }
    List<T> nodes = new ArrayList<>();
    NodeBase currentNode = firstNode;
    while (currentNode != null) {
      if (nodeClass.isInstance(currentNode)) {
        nodes.add((T) currentNode);
      }
      currentNode = currentNode.getNextNode();
    }
    return nodes;
  }

  public final void removeChildNode(NodeBase child) {
    if (child == null) {
      throw new IllegalArgumentException("Child cannot be null");
    }
    String childKey = child.getKey();
    Property childProperty = getChildProperty();
    if (childKey == null || childProperty == null) {
      throw new IllegalArgumentException("Child key or property cannot be null");
    }
    List<Link> childLinks = links.getLinks(childProperty, childKey);
    for (Link childLink : childLinks) {
      if (childLink.getTarget() == child) {
        links.remove(childLink);
      }
    }
  }

  public final void addChildNode(NodeBase child) {
    if (child == null) {
      throw new IllegalArgumentException("Child cannot be null");
    }
    NodeBase lastChild = lastChildNode();
    addChildNodeAfter(child, lastChild);
  }

  protected void addChildNodeAfter(NodeBase child, NodeBase afterChild) {
    if (child == null) {
      throw new IllegalArgumentException("Child cannot be null");
    }
    child.setParentNode(this);
    if (afterChild == null) {
      return;
    }
    afterChild.setNextNode(child);
    child.setPreviousNode(afterChild);
  }

  public final void addChildNode(int index, NodeBase child) {
    if (child == null) {
      throw new IllegalArgumentException("Child cannot be null");
    }
    if (index < 0) {
      throw new IllegalArgumentException("Index cannot be negative");
    }
    NodeBase afterChild = firstChildNode();
    if (afterChild == null) {
            NodeBase.this.addChildNode(child);
      return;
    }
    int i = 0;
    while (i < index) {
      NodeBase nextChild = afterChild.getNextNode();
      if (nextChild == null) {
        break;
      }
      afterChild = nextChild;
      i++;
    }
    addChildNodeAfter(child, afterChild);
  }

  public abstract Property getNextProperty();
  public abstract Property getPreviousProperty();
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
  public String getParentKey() {
    return "parent";
  }
  public abstract String getKey();
  public abstract String getChildKey();

  /**
   * Get the text representation of this text element
   */
  @Override
  public abstract String toString();
} 