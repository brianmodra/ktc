package com.ktc.text;

import java.util.ArrayList;
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
    return map.remove(property) != null;
  }

  public boolean remove(Property property, String key) {
    List<Link> list = map.get(property);
    if (list == null) {
      return false;
    }
    return list.removeIf(link -> link.getKey().equals(key));
  }

  public boolean remove(Link link) {
    Property property = link.getProperty();
    return map.get(property).remove(link);
  }

}
