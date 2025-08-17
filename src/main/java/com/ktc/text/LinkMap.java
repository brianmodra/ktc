package com.ktc.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Property;

public class LinkMap {
  private final ConcurrentHashMap<Property, CopyOnWriteArrayList<Link>> map;

  public LinkMap() {
    this.map = new ConcurrentHashMap<>();
  } 

  public void add(Link link) {
    map.computeIfAbsent(link.getProperty(), k -> new CopyOnWriteArrayList<>()).add(link);
  }

  public List<Link> getLinks(Property property) {
    final List<Link> list = map.get(property);
    if (list == null) {
      return new ArrayList<>();
    }
    return list;
  }

  public Link getLink(Property property, String key) {
    final List<Link> list = map.get(property);
    if (list == null) {
      return null;
    }
    for (final Link link : list) {
      if (link.getKey().equals(key)) {
        return link;
      }
    }
    return null;
  }

  public boolean contains(Property property, String key) {
    return getLink(property, key) != null;
  }

  public List<Link> getLinks(Property property, String key) {
    final List<Link> list = map.get(property);
    if (list == null) {
      return new ArrayList<>();
    }
    return list.stream().filter(link -> link.getKey().equals(key)).collect(Collectors.toList());
  }

  public boolean remove(Property property) {
    boolean removedSomething = false;
    for (Link link : map.get(property)) {
      removedSomething = true;
      link.unlinkTarget();
    }
    map.remove(property);
    return removedSomething;
  }

  public boolean remove(Property property, String key) {
    List<Link> list = map.get(property);
    if (list == null) {
      return false;
    }
    boolean removedSomething = false;
    for (Link link : list) {
      if (link.getKey().equals(key)) {
        if (list.remove(link)) {
          removedSomething = true;
          link.unlinkTarget();
        }
      }
    }
    return removedSomething;
  }

  public boolean remove(Property property, String key, NodeBase targetNode) {
    boolean removedSomething = false;

    final List<Link> list = map.get(property);
    if (list == null) {
      return false;
    }
    for (Link link : list) {
      if (link.getKey() == key && link.getTarget() == targetNode) {
        if (list.remove(link)) {
          link.unlinkTarget();
          removedSomething = true;
        }
      }
    }
    return removedSomething;
  }

  public boolean remove(NodeBase targetNode) {
    boolean removedSomething = false;
    for (List<Link> list : map.values()) {
      for (Link link : list) {
        if (link.getTarget() == targetNode) {
          if (list.remove(link)) {
            link.unlinkTarget();
            removedSomething = true;
          }
        }
      }
    }
    return removedSomething;
  }

  public boolean remove(String key) {
    boolean removedSomething = false;
    for (List<Link> list : map.values()) {
      for (Link link : list) {
        if (link.getKey() == key) {
          if (list.remove(link)) {
            link.unlinkTarget();
            removedSomething = true;
          }
        }
      }
    }
    return removedSomething;
  }

  public boolean remove(Link link) {
    Property property = link.getProperty();
    if (map.get(property).remove(link)) {
      link.unlinkTarget();
      return true;
    }
    return false;
  }

  public Collection<CopyOnWriteArrayList<Link>> values() {
    return map.values();
  }

  public void clear() {
    map.clear();
  }
}
