package com.ktc.text;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NodeBaseTest {
  @Test
  public void testNodeBase() {
    class TestChildNode extends NodeBase {
      public TestChildNode(Resource type) {
        super(type);
      }
      @Override
      public Property getNextProperty() {
        return Link.NEXT_WORD;
      }
    
      @Override
      public Property getPreviousProperty() {
        return Link.PREVIOUS_WORD;
      }

      @Override
      public boolean parentCanBe(Class<? extends NodeBase> parentClass) {
        return true;
      }
    }
    class TestParentNode extends NodeBase {
      public TestParentNode(Resource type) {
        super(type);
      }
      @Override
      public Property getNextProperty() {
        return Link.NEXT_SENTENCE;
      }
    
      @Override
      public Property getPreviousProperty() {
        return Link.PREVIOUS_SENTENCE;
      }

      @Override
      public boolean parentCanBe(Class<? extends NodeBase> parentClass) {
        return false;
      }
    }
    TestParentNode node1 = new TestParentNode(NodeBase.SENTENCE_TYPE);
    TestParentNode node2 = new TestParentNode(NodeBase.SENTENCE_TYPE);
    TestParentNode node3 = new TestParentNode(NodeBase.SENTENCE_TYPE);
    TestChildNode child1 = new TestChildNode(NodeBase.TOKEN_TYPE);
    TestChildNode child2 = new TestChildNode(NodeBase.TOKEN_TYPE);
    TestChildNode child3 = new TestChildNode(NodeBase.TOKEN_TYPE);
    node1.setNextNode(node2);
    node1.setPreviousNode(node3);
    node1.addChildNode(child1);

    assertEquals(node1, child1.getParentNode());
    assertEquals(node2, node1.getNextNode());
    assertEquals(node3, node1.getPreviousNode());
    assertEquals(child1, node1.firstChildNode());

    child3.setParentNode(node2);
    assertEquals(node2, child3.getParentNode());
    assertEquals(null, node2.firstChildNode());

    child3.setParentNode(null);
    assertEquals(null, child3.getParentNode());

    node1.removeChildNode(child1);
    assertEquals(null, node1.firstChildNode());

    node1.addChildNode(child1);
    node1.addChildNode(child2);
    node1.addChildNode(child3);

    assertEquals(child1, node1.firstChildNode());
    assertEquals(child2, child1.getNextNode());
    assertEquals(child3, child2.getNextNode());
    assertEquals(null, child3.getNextNode());
    assertEquals(child2, child3.getPreviousNode());
    assertEquals(child1, child2.getPreviousNode());
    assertEquals(null, child1.getPreviousNode());
    assertEquals(child3, node1.lastChildNode());

    node1.removeChildNode(child1);
    assertEquals(child2, node1.firstChildNode());
    assertEquals(child3, node1.lastChildNode());

    assertEquals(child3, child2.getNextNode());
    assertEquals(null, child3.getNextNode());
    assertEquals(child2, child3.getPreviousNode());
    assertEquals(null, child2.getPreviousNode());

    node1.addChildNode(0, child1);
    assertEquals(child1, node1.firstChildNode());
    assertEquals(child2, child1.getNextNode());
    assertEquals(child3, child2.getNextNode());
    assertEquals(null, child3.getNextNode());
    assertEquals(child2, child3.getPreviousNode());
    assertEquals(child1, child2.getPreviousNode());
    assertEquals(null, child1.getPreviousNode());
    assertEquals(child3, node1.lastChildNode());

    node1.removeChildNode(child2);
    assertEquals(null, child2.getPreviousNode());
    assertEquals(null, child2.getNextNode());
    assertEquals(null, child2.getParentNode());

    assertEquals(child1, node1.firstChildNode());
    assertEquals(child3, node1.lastChildNode());

    assertEquals(child3, child1.getNextNode());
    assertEquals(null, child3.getNextNode());
    assertEquals(child1, child3.getPreviousNode());
    assertEquals(null, child1.getPreviousNode());

    node1.addChildNode(1, child2);
    assertEquals(child1, node1.firstChildNode());
    assertEquals(child2, child1.getNextNode());
    assertEquals(child3, child2.getNextNode());
    assertEquals(null, child3.getNextNode());
    assertEquals(child2, child3.getPreviousNode());
    assertEquals(child1, child2.getPreviousNode());
    assertEquals(null, child1.getPreviousNode());
    assertEquals(child3, node1.lastChildNode());

    node1.removeChildNode(child3);
    assertEquals(child1, node1.firstChildNode());
    assertEquals(child2, node1.lastChildNode());

    assertEquals(child2, child1.getNextNode());
    assertEquals(null, child2.getNextNode());
    assertEquals(child1, child2.getPreviousNode());
    assertEquals(null, child1.getPreviousNode());
  }
}
