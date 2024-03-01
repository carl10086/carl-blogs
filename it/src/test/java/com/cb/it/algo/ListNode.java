package com.cb.it.algo;

public class ListNode {

  public int val;
  public ListNode next;

  public ListNode() {
  }

  public ListNode(int val) {
    this.val = val;
  }

  public ListNode(int val, ListNode next) {
    this.val = val;
    this.next = next;
  }


  @Override
  public String toString() {
    return "ListNode{" +
        "val=" + val +
        '}';
  }

  public static ListNode fromArray(int... nums) {

    ListNode dummy = new ListNode();

    var current = dummy;

    for (int num : nums) {
      current.next = new ListNode(num);
      current = current.next;
    }
    return dummy.next;
  }

}
