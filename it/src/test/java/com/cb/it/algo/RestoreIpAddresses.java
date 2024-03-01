package com.cb.it.algo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RestoreIpAddresses {


  public List<String> restoreIpAddresses(String s) {

    int len = s.length();
    if (len < 4) {
      return new ArrayList<>();
    }

    Queue<List<Integer>> queue = new LinkedList<>();
    /*插入第一个逗号*/
    for (int i = 0; i < 3; i++) {
      String sub = s.substring(0, i + 1);
      if (Integer.parseInt(sub) <= 255) {
        queue.add(List.of(i));
      }
    }

    return new ArrayList<>();
  }

  public static void main(String[] args) {

    RestoreIpAddresses restoreIpAddresses = new RestoreIpAddresses();
    restoreIpAddresses.restoreIpAddresses("101023");

  }

}
