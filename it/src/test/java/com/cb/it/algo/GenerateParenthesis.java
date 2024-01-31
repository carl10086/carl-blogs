package com.cb.it.algo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GenerateParenthesis {

  private static class Node {

    private String value;

    /**
     * 剩余的 left
     */
    private int left;

    /**
     * 剩余的 right
     */
    private int right;


    @Override
    public String toString() {
      return "Node{" +
          "value='" + value + '\'' +
          ", left=" + left +
          ", right=" + right +
          '}';
    }

    public Node(String value, int left, int right) {
      this.value = value;
      this.left = left;
      this.right = right;
    }

    private boolean mayValid() {
      return left >= 0 && right >= 0 && left <= right;
    }

    private boolean valid() {
      return left == 0 && right == 0;

    }
  }

  public List<String> generateParenthesis(int n) {
    Queue<Node> bfs = new LinkedList<>();
    bfs.add(new Node("", n, n));
    int level = 0;
    List<String> result = new ArrayList<>();

    while (!bfs.isEmpty()) {
      List<Node> levelNodes = new ArrayList<>();
      while (!bfs.isEmpty()) {
        levelNodes.add(bfs.poll());
      }

      for (Node node : levelNodes) {
        String value = node.value;
        Node leftChild = new Node(
            value + "(",
            node.left - 1,
            node.right
        );

        Node rightChild = new Node(
            value + ")",
            node.left,
            node.right - 1
        );

        if (leftChild.valid()) {
          result.add(leftChild.value);
        } else if (leftChild.mayValid()) {
          bfs.add(leftChild);
        }

        if (rightChild.valid()) {
          result.add(rightChild.value);
        } else if (rightChild.mayValid()) {
          bfs.add(rightChild);
        }

      }

      level++;
    }

    return result;
  }

  public static void main(String[] args) {
    System.out.println(new GenerateParenthesis().generateParenthesis(2));
  }


}
