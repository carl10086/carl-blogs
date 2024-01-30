package com.cb.it.algo;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TreeNode {

  int val;
  TreeNode left;
  TreeNode right;

  TreeNode() {
  }

  TreeNode(int val) {
    this.val = val;
  }

  TreeNode(int val, TreeNode left, TreeNode right) {
    this.val = val;
    this.left = left;
    this.right = right;
  }

  @Override
  public String toString() {
    return "TreeNode{" +
        "val=" + val +
        '}';
  }

  public static TreeNode of(List<Integer> values) {
    if (values == null || values.isEmpty()) {
      return null;
    }

    TreeNode root = new TreeNode(values.getFirst());
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);

    for (int i = 1; i < values.size(); i += 2) {
      TreeNode parent = queue.poll();

      Integer leftVal = values.get(i);
      if (leftVal != null) {
        TreeNode left = new TreeNode(leftVal);
        parent.left = left;
        queue.offer(left);
      }

      if (i + 1 < values.size()) {
        Integer rightVal = values.get(i + 1);
        if (rightVal != null) {
          TreeNode right = new TreeNode(rightVal);
          parent.right = right;
          queue.offer(right);
        }
      }
    }

    return root;
  }

}
