package com.cb.it.algo;


public class LRUCache {

  static class Node {

    private int key;
    private int value;

    private Node prev;
    private Node next;

    public Node(int key, int value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public String toString() {
      return "Node{" +
          "key=" + key +
          ", value=" + value +
          '}';
    }
  }

  private java.util.HashMap<Integer, Node> storage;
  private Node head;
  private Node tail;

  private int size = 0;

  private int capacity;

  public LRUCache(int capacity) {
    this.storage = new java.util.HashMap<>(capacity);
    this.capacity = capacity;
  }


  public int get(int key) {
    if (storage.containsKey(key)) {
      /*todo refactor, move existed node to tail*/
      Node node = this.storage.get(key);
      if (this.size == 1 || this.tail == node) {
        return node.value;
      }
      Node next = node.next;
      if (head == node) {
        this.head = next;
        next.prev = null;
      } else {
        Node prev = node.prev;
        if (prev != null) {
          prev.next = next;
          next.prev = prev;
        }
      }
      Node oldTail = this.tail;
      oldTail.next = node;
      node.next = null;
      node.prev = oldTail;
      this.tail = node;
      /*move node to tail*/
      return node.value;
    } else {
      return -1;
    }
  }

  public void put(int key, int value) {
    if (storage.containsKey(key)) {
      Node node = this.storage.get(key);
      node.value = value;
      /*move node to tail*/
      if (this.size > 1 && this.tail != node) {
        /*next must existed*/
        Node next = node.next;
        if (this.head == node) {
          this.head = next;
          next.prev = null;
        } else {
          Node prev = node.prev;
          if (prev != null) {
            prev.next = next;
            next.prev = prev;
          }
        }

        Node oldTail = this.tail;
        oldTail.next = node;
        node.next = null;
        node.prev = oldTail;
        this.tail = node;
      }
    } else if (isEmpty()) {
      Node node = new Node(key, value);
      this.storage.put(key, node);
      this.head = node;
      this.tail = node;
      this.size++;
    } else if (isSingle()) {
      this.storage.remove(this.head.key);
      Node node = new Node(key, value);
      this.storage.put(key, node);
      this.head = node;
      this.tail = node;
      this.size = 1;
    } else {
      if (isFull()) {
        this.storage.remove(this.head.key);
        this.head = this.head.next;
        this.size--;
      }
      Node node = new Node(key, value);
      this.storage.put(key, node);
      Node oldTail = this.tail;
      oldTail.next = node;
      this.tail = node;
      this.tail.prev = oldTail;
      this.size++;
    }
  }

  public boolean isSingle() {
    return this.capacity == 1;
  }

  private boolean isEmpty() {
    return this.size == 0;
  }

  private boolean isFull() {
    return this.size == this.capacity;
  }


  public static void main(String[] args) {
    LRUCache lruCache = new LRUCache(2);
    lruCache.put(2, 1);
    lruCache.put(3, 2);
    System.out.println(lruCache.get(3));
    System.out.println(lruCache.get(2));
    lruCache.put(4, 3);
    System.out.println(lruCache.get(2));
    System.out.println(lruCache.get(3));
    System.out.println(lruCache.get(4));

    System.out.println("for debug");
  }
}
