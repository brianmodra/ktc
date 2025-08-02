package com.ktc.text;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class LinkMapTest {
  @Test
  public void testSingleLinkAddedToLinkMap() {
    LinkMap linkMap = new LinkMap();
    TokenText node1 = TokenText.create("node1");
    TokenText node2 = TokenText.create("node2");
    Link link = new Link(Link.NEXT_WORD, node1, node2, "next");
    linkMap.add(link);

    assertEquals(link, linkMap.getLink(Link.NEXT_WORD, "next"));

    List<Link> allLinks = linkMap.getLinks(Link.NEXT_WORD, "next");
    assertEquals(1, allLinks.size());
    assertEquals(link, allLinks.get(0));

    List<Link> links = linkMap.getLinks(Link.NEXT_WORD);
    assertEquals(1, links.size());
    assertEquals(link, links.get(0));
  }

  @Test
  public void testSingleLinksOfDifferingPropertiesAddedToLinkMap() {
    LinkMap linkMap = new LinkMap();
    TokenText node1 = TokenText.create("node1");
    TokenText node2 = TokenText.create("node2");
    Link link1 = new Link(Link.NEXT_WORD, node1, node2, "next");
    Link link2 = new Link(Link.NEXT_PARAGRAPH, node1, node2, "next");
    linkMap.add(link1);
    linkMap.add(link2);

    assertEquals(link1, linkMap.getLink(Link.NEXT_WORD, "next"));
    assertEquals(link2, linkMap.getLink(Link.NEXT_PARAGRAPH, "next"));

    List<Link> links = linkMap.getLinks(Link.NEXT_WORD, "next");
    assertEquals(1, links.size());
    assertEquals(link1, links.get(0));

    links = linkMap.getLinks(Link.NEXT_PARAGRAPH);
    assertEquals(1, links.size());
    assertEquals(link2, links.get(0));
  }

  public void testMultipleLinksAddedToLinkMapCanBeRetriueved() {
    LinkMap linkMap = new LinkMap();
    TokenText node1 = TokenText.create("node1");
    TokenText node2 = TokenText.create("node2");
    Link link1 = new Link(Link.NEXT_WORD, node1, node2, "next");
    Link link2 = new Link(Link.NEXT_WORD, node2, node1, "next");
    Link link3 = new Link(Link.NEXT_WORD, node2, node1, "wrong_key");
    Link link4 = new Link(Link.PREVIOUS_WORD, node2, node1, "previous");
    linkMap.add(link1);
    linkMap.add(link2);
    linkMap.add(link3);
    linkMap.add(link4);

    assertEquals(link1, linkMap.getLink(Link.NEXT_WORD, "next"));
    assertEquals(link4, linkMap.getLink(Link.PREVIOUS_WORD, "previous"));
    assertEquals(link3, linkMap.getLink(Link.NEXT_WORD, "wrong_key"));
    assertEquals(null, linkMap.getLink(Link.PREVIOUS_WORD, "wrong_key"));
    assertEquals(null, linkMap.getLink(Link.PREVIOUS_CHAPTER, "previous"));
    assertEquals(link4, linkMap.getLink(Link.PREVIOUS_WORD, "previous"));

    List<Link> links = linkMap.getLinks(Link.NEXT_WORD, "next");
    assertEquals(2, links.size());
    assertEquals(link1, links.get(0));
    assertEquals(link2, links.get(1));

    links = linkMap.getLinks(Link.NEXT_WORD);
    assertEquals(3, links.size());
    assertEquals(link1, links.get(0));
    assertEquals(link2, links.get(1)); 
    assertEquals(link3, links.get(2));

    links = linkMap.getLinks(Link.PREVIOUS_WORD);
    assertEquals(1, links.size());
    assertEquals(link4, links.get(0));

    assertTrue(linkMap.contains(Link.NEXT_WORD, "next"));
    assertTrue(linkMap.contains(Link.PREVIOUS_WORD, "previous"));
    assertFalse(linkMap.contains(Link.PREVIOUS_WORD, "previous2"));
    assertTrue(linkMap.contains(Link.NEXT_WORD, "wrong_key"));
    assertFalse(linkMap.contains(Link.PREVIOUS_CHAPTER, "previous"));
  }

  @Test
  public void testAddingAndRemovingLinksFromLinkMap() {
    LinkMap linkMap = new LinkMap();
    TokenText node1 = TokenText.create("node1");
    TokenText node2 = TokenText.create("node2");
    Link link = new Link(Link.NEXT_WORD, node1, node2, "next");
    linkMap.add(link);
    assertEquals(link, linkMap.getLink(Link.NEXT_WORD, "next"));
    List<Link> links = linkMap.getLinks(Link.NEXT_WORD);
    assertEquals(1, links.size());
    assertEquals(link, links.get(0));
    List<Link> allLinks = linkMap.getLinks(Link.NEXT_WORD, "next");
    assertEquals(1, allLinks.size());
    assertEquals(link, allLinks.get(0));
    linkMap.remove(link);
    assertEquals(0, linkMap.getLinks(Link.NEXT_WORD).size());
    assertEquals(0, linkMap.getLinks(Link.NEXT_WORD, "next").size());

    linkMap.add(link);
    links = linkMap.getLinks(Link.NEXT_WORD);
    assertEquals(1, links.size());
    linkMap.remove(Link.NEXT_WORD);
    assertEquals(0, linkMap.getLinks(Link.NEXT_WORD).size());

    linkMap.add(link);
    linkMap.remove(Link.NEXT_WORD, "next");
    assertEquals(0, linkMap.getLinks(Link.NEXT_WORD).size());

    Link link2 = new Link(Link.NEXT_WORD, node2, node1, "next");
    Link link3 = new Link(Link.NEXT_CHAPTER, node2, node1, "next");
    linkMap.add(link);
    linkMap.add(link2);
    linkMap.add(link3);
    links = linkMap.getLinks(Link.NEXT_WORD);
    assertEquals(2, links.size());
    assertEquals(link, links.get(0));
    assertEquals(link2, links.get(1));
    linkMap.remove(Link.NEXT_PARAGRAPH);
    assertEquals(2, linkMap.getLinks(Link.NEXT_WORD).size());
    linkMap.remove(Link.NEXT_WORD, "next2");
    assertEquals(2, linkMap.getLinks(Link.NEXT_WORD).size());
    assertTrue(linkMap.contains(Link.NEXT_WORD, "next"));
    assertFalse(linkMap.contains(Link.NEXT_WORD, "next2"));
    assertEquals(1, linkMap.getLinks(Link.NEXT_CHAPTER).size());

    linkMap.remove(link2);
    assertEquals(1, linkMap.getLinks(Link.NEXT_WORD).size());
    links = linkMap.getLinks(Link.NEXT_WORD);
    assertEquals(link, links.get(0));
  }

  @Test
  public void testGettingNonExistentPropertyFromLinkMap() {
    LinkMap linkMap = new LinkMap();
    TokenText node1 = TokenText.create("node1");
    TokenText node2 = TokenText.create("node2");
    Link link = new Link(Link.NEXT_WORD, node1, node2, "next");
    linkMap.add(link);
    assertEquals(null, linkMap.getLink(Link.NEXT_WORD, "wrong_key"));
    assertEquals(null, linkMap.getLink(Link.NEXT_CHAPTER, "next"));
    List<Link> links = linkMap.getLinks(Link.NEXT_CHAPTER);
    assertEquals(0, links.size());
    links = linkMap.getLinks(Link.NEXT_CHAPTER, "next");
    assertEquals(0, links.size()); 
    assertFalse(linkMap.contains(Link.NEXT_WORD, "wrong_key"));
    assertFalse(linkMap.contains(Link.NEXT_CHAPTER, "next"));
    assertFalse(linkMap.remove(Link.NEXT_CHAPTER));
    assertFalse(linkMap.remove(Link.NEXT_CHAPTER, "next"));
    assertFalse(linkMap.remove(Link.NEXT_WORD, "wrong_key"));
  }
}
