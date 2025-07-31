package com.ktc.text;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class LinkMapTest {
  @Test
  public void testLinkMap() {
    LinkMap linkMap = new LinkMap();
    TokenText node1 = TokenText.create("node1");
    TokenText node2 = TokenText.create("node2");
    Link link = new Link(Link.NEXT_WORD, node1, node2, "next");
    linkMap.add(link);
    assertEquals(link, linkMap.getByKey(Link.NEXT_WORD, "next"));
    List<Link> links = linkMap.get(Link.NEXT_WORD);
    assertEquals(1, links.size());
    assertEquals(link, links.get(0));
    List<Link> allLinks = linkMap.getAllWithKey(Link.NEXT_WORD, "next");
    assertEquals(1, allLinks.size());
    assertEquals(link, allLinks.get(0));
    linkMap.remove(link);
    assertEquals(0, linkMap.get(Link.NEXT_WORD).size());
    assertEquals(0, linkMap.getAllWithKey(Link.NEXT_WORD, "next").size());

    linkMap.add(link);
    links = linkMap.get(Link.NEXT_WORD);
    assertEquals(1, links.size());
    linkMap.removeAllWithProperty(Link.NEXT_WORD);
    assertEquals(0, linkMap.get(Link.NEXT_WORD).size());

    linkMap.add(link);
    linkMap.removeAllWithKey(Link.NEXT_WORD, "next");
    assertEquals(0, linkMap.get(Link.NEXT_WORD).size());

    Link link2 = new Link(Link.NEXT_WORD, node2, node1, "next");
    Link link3 = new Link(Link.NEXT_CHAPTER, node2, node1, "next");
    linkMap.add(link);
    linkMap.add(link2);
    linkMap.add(link3);
    links = linkMap.get(Link.NEXT_WORD);
    assertEquals(2, links.size());
    assertEquals(link, links.get(0));
    assertEquals(link2, links.get(1));
    linkMap.removeAllWithProperty(Link.NEXT_PARAGRAPH);
    assertEquals(2, linkMap.get(Link.NEXT_WORD).size());
    linkMap.removeAllWithKey(Link.NEXT_WORD, "next2");
    assertEquals(2, linkMap.get(Link.NEXT_WORD).size());
    assertTrue(linkMap.contains(Link.NEXT_WORD, "next"));
    assertFalse(linkMap.contains(Link.NEXT_WORD, "next2"));
    assertEquals(1, linkMap.get(Link.NEXT_CHAPTER).size());

    linkMap.remove(link2);
    assertEquals(1, linkMap.get(Link.NEXT_WORD).size());
    links = linkMap.get(Link.NEXT_WORD);
    assertEquals(link, links.get(0));
  }
}
