package com.ktc.text;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

import com.ktc.actor.DocumentCommand;

/**
 * Base class for all text elements that need UUID management
 */
public abstract class DocumentBaseText implements DocumentCommand {
  protected final UUID id;
  protected ConcurrentHashMap<Class<? extends DocumentBaseText>, UUID> parentIds;

  /**
   * Constructor that generates a random UUID
   */
  protected DocumentBaseText() {
    this.id = UUID.randomUUID();
    this.parentIds = new ConcurrentHashMap<Class<? extends DocumentBaseText>, UUID>();
  }

  /**
   * Constructor with provided UUID
   */
  protected DocumentBaseText(UUID id) {
    this.id = id;
    this.parentIds = new ConcurrentHashMap<Class<? extends DocumentBaseText>, UUID>();
  }

  /**
   * Get the UUID for this text element
   */
  public UUID getId() {
    return id;
  }

  /**
   * Get the UUID of the parent text element
   */
  public ConcurrentHashMap<Class<? extends DocumentBaseText>, UUID> getParentIds() {
    return parentIds;
  }

  /**
   * Set the UUID of the parent text element
   */
  public void setParent(DocumentBaseText parent) {
    this.parentIds.put(parent.getClass(), parent.getId());
  }

  public UUID getParentId(Class<? extends DocumentBaseText> parentClass) {
    return parentIds.get(parentClass);
  }

  /**
   * Get the text representation of this text element
   */
  public abstract String toString();
} 