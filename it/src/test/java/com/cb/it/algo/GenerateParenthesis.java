package com.cb.it.algo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class GenerateParenthesis {

  private static class Node {

    String val;
    int leftRemain;
    int rightRemain;

    public Node(String val, int leftRemain, int rightRemain) {
      this.val = val;
      this.leftRemain = leftRemain;
      this.rightRemain = rightRemain;
    }

    public Node leftChild() {
      return new Node(val + "(", leftRemain - 1, rightRemain);
    }

    public Node rightChild() {
      return new Node(val + ")", leftRemain, rightRemain - 1);
    }

    public boolean maySuccess() {
      return leftRemain <= rightRemain && leftRemain >= 0 && rightRemain >= 0;
    }
  }

  public List<String> generateParenthesis(int n) {
    Queue<Node> queue = new ArrayDeque<>();

    Node root = new Node("", n, n);

    queue.add(root);

    int level = 0;

    while (!queue.isEmpty()) {
      level++;
      List<Node> levelNodes = new ArrayList<>();
      while (!queue.isEmpty()) {
        levelNodes.add(queue.poll());
      }

      for (Node levelNode : levelNodes) {
        var leftChild = levelNode.leftChild();
        var rightChild = levelNode.rightChild();

        if (leftChild.maySuccess()) {
          queue.add(leftChild);
        }
        if (rightChild.maySuccess()) {
          queue.add(rightChild);
        }
      }

      if (level == 2 * n) {
        return queue.stream().map(x -> x.val).toList();
      }


    }

    return null;

  }


  public static void main(String[] args) {
    System.out.println(new GenerateParenthesis().generateParenthesis(3));
  }


}
