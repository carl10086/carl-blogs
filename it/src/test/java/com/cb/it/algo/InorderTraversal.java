package com.cb.it.algo;

import java.util.ArrayList;
import java.util.List;

public class InorderTraversal {


  public List<Integer> inorderTraversal(TreeNode root) {
    if (root == null) {
      return new ArrayList<>();
    }
    List<Integer> result = new ArrayList<>();

    result.addAll(inorderTraversal(root.left));
    result.add(root.val);
    result.addAll(inorderTraversal(root.right));
    return result;
  }

  public static void main(String[] args) {
    TreeNode root = new TreeNode(1);
    var n2 = new TreeNode(2);
    var n3 = new TreeNode(3);

    root.right = n2;
    n2.left = n3;

    System.out.println(new InorderTraversal().inorderTraversal(root));
    System.out.println(1);
  }

}
