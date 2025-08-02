package com.ktc.text;

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

  public Link getLink(Property property, String key) {
    return links.getLink(property, key);
  }

  public List<Link> getLinks(Property property) {
    return links.getLinks(property);
  }

  protected void setParentNode(NodeBase parent) {
    Property parentProperty = getParentProperty();
    String parentKey = getParentKey();
    if (parent == null) {
      links.remove(parentProperty, parentKey);
      return;
    }
    if (links.contains(parentProperty, parentKey)) {
      NodeBase p = getParentNode();
      if (p == parent) {
        return;
      }
      throw new IllegalArgumentException("Parent already set");
    }
    createUniqueLink(this, parent, parentProperty, parentKey);
  }

  public NodeBase getParentNode() {
    Link link = getLink(getParentProperty(), getParentKey());
    if (link == null) {
      return null;
    }
    return link.getTarget();
  }

  private static void createUniqueLink(NodeBase node, NodeBase otherNode, Property property, String key) {
    node.links.remove(property, key);
    Link link = new Link(property, node, otherNode, key);
    node.addLink(link);
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
      links.remove(nextProperty, nextKey);
      return;
    }
    Link existingNext = links.getLink(nextProperty, nextKey);
    if (existingNext != null) {
      NodeBase existingTarget = existingNext.getTarget();
      if (existingTarget == next) {
        return;
      }
      if (next.getNextNode() != null) {
        throw new IllegalArgumentException("Next must be an unlinked node");
      }
      createUniqueLink(next, existingTarget, nextProperty, nextKey);
      createUniqueLink(existingTarget, next, previousProperty, previousKey);
    }
    NodeBase existingPrevious = next.getPreviousNode();
    if (existingPrevious != null) {
      if (existingPrevious == this) {
        return;
      }
      throw new IllegalArgumentException("Next must be an unlinked node");
    }
    createUniqueLink(this, next, nextProperty, nextKey);
    createUniqueLink(next, this, previousProperty, previousKey);
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
      links.remove(previousProperty, previousKey);
      return;
    }
    Link existingPrevious = links.getLink(previousProperty, previousKey);
    if (existingPrevious != null) {
      NodeBase existingTarget = existingPrevious.getTarget();
      if (existingTarget == previous) {
        return;
      }
      if (previous.getPreviousNode() != null) {
        throw new IllegalArgumentException("Previous must be an unlinked node");
      }
      createUniqueLink(previous, existingTarget, previousProperty, previousKey);
      createUniqueLink(existingTarget, previous, nextProperty, nextKey);
    }
    NodeBase existingNext = previous.getNextNode();
    if (existingNext != null) {
      if (existingNext == this) {
        return;
      }
      throw new IllegalArgumentException("Previous must be an unlinked node");
    }
    createUniqueLink(this, previous, previousProperty, previousKey);
    createUniqueLink(previous, this, nextProperty, nextKey);
  }

  public NodeBase getNextNode() {
    String nextKey = getNextKey();
    Property nextProperty = getNextProperty();
    if (nextKey == null || nextProperty == null) {
      return null;
    }
    Link link = getLink(nextProperty, nextKey);
    if (link == null) {
      return null;
    }
    return link.getTarget();
  }

  public NodeBase getPreviousNode() {
    String previousKey = getPreviousKey();
    Property previousProperty = getPreviousProperty();
    if (previousKey == null || previousProperty == null) {
      return null;
    }
    Link link = getLink(previousProperty, previousKey);
    if (link == null) {
      return null;
    }
    return link.getTarget();
  }

  public NodeBase lastChildNode() {
    String childKey = getChildKey();
    Property childProperty = getChildProperty();
    if (childKey == null || childProperty == null) {
      return null;
    }
    Link link = getLink(childProperty, childKey);
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

  public NodeBase firstChildNode() {
    String childKey = getChildKey();
    Property childProperty = getChildProperty();
    if (childKey == null || childProperty == null) {
      return null;
    }
    Link link = getLink(childProperty, childKey);
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

  public void removeChildNode(NodeBase child) {
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
        break;
      }
    }
    NodeBase previousChild = child.getPreviousNode();
    NodeBase nextChild = child.getNextNode();
    if (previousChild != null) {
      if (nextChild != null) {
        createUniqueLink(previousChild, nextChild, previousChild.getNextProperty(), previousChild.getNextKey());
        createUniqueLink(nextChild, previousChild, nextChild.getPreviousProperty(), nextChild.getPreviousKey());
      } else {
        previousChild.setNextNode(null);
      }
    } else if (nextChild != null) {
      nextChild.setPreviousNode(null);
    }
    child.setParentNode(null);
    child.setNextNode(null);
    child.setPreviousNode(null);
  }

  public void addChildNode(NodeBase child) {
    if (child == null) {
      throw new IllegalArgumentException("Child cannot be null");
    }
    NodeBase lastChild = lastChildNode();
    addChildNodeInOrder(child, lastChild);
  }

  protected void addChildNodeInOrder(NodeBase child, NodeBase afterChild) {
    if (child == null) {
      throw new IllegalArgumentException("Child cannot be null");
    }
    String childKey = getChildKey();
    Property childProperty = getChildProperty();
    if (childKey == null || childProperty == null) {
      throw new IllegalArgumentException("This type of node (" + getClass().getSimpleName() + ") cannot have children");
    }
    if (afterChild == null) {
      NodeBase beforeChild = firstChildNode();
      if (beforeChild != null) {
        createUniqueLink(child, beforeChild, child.getNextProperty(), child.getNextKey());
        createUniqueLink(beforeChild, child, beforeChild.getPreviousProperty(), beforeChild.getPreviousKey());
      }
    } else {
      afterChild.setNextNode(child);
      child.setPreviousNode(afterChild);
    }
    // have to add the parent last, otherwise it may find itself and link itself to itself.
    child.setParentNode(this);
    Link link = new Link(childProperty, this, child, childKey);
    addLink(link);
  }

  public void addChildNode(int index, NodeBase child) {
    if (child == null) {
      throw new IllegalArgumentException("Child cannot be null");
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
    String className = getClass().getSimpleName();
    if (className.endsWith("Text")) {
      return className.substring(0, className.length() - 4).toLowerCase();
    }
    return className;
  }

  public abstract Property getNextProperty();

  public abstract Property getPreviousProperty();

  public abstract String getParentKey();

  public abstract String getChildKey();

  /**
   * Get the text representation of this text element
   */
  @Override
  public String toString() {
    String className = getClass().getSimpleName();
    return className + "(" + getId().toString() + ")";
  }
}
