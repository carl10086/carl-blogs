package com.cb.it.algo;

import java.util.Arrays;
import java.util.List;

public class DeleteNode {

//  public TreeNode doDeleteNode(TreeNode parent, TreeNode cur, int key) {
//    if (cur == null) {
//      return null;
//    }
//    int val = cur.val;
//
//    if (val > key) {
//      return doDeleteNode(parent, cur.left, key);
//    } else if (val < key) {
//      return doDeleteNode(parent, cur.right, key);
//    } else {
//      /*following */
//      TreeNode left = cur.left;
//      TreeNode right = cur.right;
//
//      if (left == null && right == null) {
//
//      }
//
//    }
//
//  }

  public TreeNode deleteNode(TreeNode root, int key) {
    if (root == null) {
      return null;
    }
    int val = root.val;

    if (val > key) {
      root.left = deleteNode(root.left, key);
      return root;
    } else if (val < key) {
      root.right = deleteNode(root.right, key);
      return root;
    } else {
      /*following */
      TreeNode left = root.left;
      TreeNode right = root.right;

      if (left == null && right == null) {
        /*当前是叶子节点*/
        return null;
      } else if (left == null) {
        return right;
      } else if (right == null) {
        return left;
      } else {
        /*非叶子节点而且 儿子都存在的情况下，寻找左儿子节点的最大值，或者右儿子的最小值?*/

        TreeNode min = right;
        TreeNode p = root;
        while (true) {
          if (min.left != null) {
            p = min;
            min = min.left;
          } else {
            break;
          }
        }
        if (p == root) {
          p.right = deleteNode(min, min.val);
        } else {
          p.left = deleteNode(min, min.val);
        }
        root.val = min.val;
        return root;
      }
    }
  }

  public static void main(String[] args) {
    List<Integer> input = Arrays.asList(5, 3, 6, 2, 4, null, 7);

    var root = TreeNode.of(input);

    DeleteNode solution = new DeleteNode();
    var result = solution.deleteNode(root, 5);

    System.out.println(1);
  }
}
