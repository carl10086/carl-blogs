package com.cb.it.algo;


public class RemoveNthFromEnd {


  private ListNode moveSteps(ListNode target, int steps) {
    var current = target;

    for (int i = 0; i < steps; i++) {
      if (current == null) {
        return null;
      }
      current = current.next;

    }
    return current;
  }

  public ListNode removeNthFromEnd(ListNode head, int n) {
    var dummy = new ListNode();
    dummy.next = head;
    var fast = dummy;
    var slow = dummy;

    fast = moveSteps(fast, n);
    ListNode prev = null;

    while (true) {
      fast = moveSteps(fast, 1);
      prev = slow;
      slow = moveSteps(slow, 1);
      if (fast == null) {
        break;
      }
    }

    ListNode cur = null;
    ListNode next = null;
    if (prev != null) {
      cur = prev.next;
    }
    if (cur != null) {
      next = cur.next;
    }
    prev.next = next;

    return dummy.next;
  }

  public static void main(String[] args) {
    RemoveNthFromEnd target = new RemoveNthFromEnd();

    ListNode listNode = ListNode.fromArray(1, 2);

    ListNode result = target.removeNthFromEnd(listNode, 2);

    System.out.println(result);
  }
}
