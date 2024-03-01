package com.cb.it.algo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class GetNumberFromTheArray {

  /**
   // There is an array generated by a rule.
   // The first item is 1. If k is in the array, then k*3 +1 and k*2+1 are in the array.
   // The array is sorted. There are no duplicate values.
   // Please write a function that accepts an input N. It should return the index N of the array.
   // For example [1, 3, 4, 7, 9, 10, 13, 15, 19, 21, 22, 27, ...] n=10, return 22
   function getNumberFromTheArray(N) {
   return 22;
   }
   console.log(getNumberFromTheArray(5)) // 10;
   console.log(getNumberFromTheArray(10)) // 22;
   *
   * eg:
   *  1, 3, 4, 7, 9, 10, 13, 15, 19, 21, 22, 27,
   *  10->22
   * @param n
   * @return
   */
  public int getNumberFromTheArray(int n) {
    // 1 -> 3,4
    // 3-> 6,7
    // 4->9,10
    // 6 -> 13, 15

    Queue<Integer> queue = new LinkedList<>();

    queue.add(1);
//    List<Integer> bfs = new ArrayList<>();
//    LinkedHashSet<Integer> bfs = new LinkedHashSet<>();
    var bfs = new ArrayList<Integer>();
    var duplicated = new HashSet<Integer>();
    while (true) {
      List<Integer> levelValues = new ArrayList<>();
      while (!queue.isEmpty()) {
        var cur = queue.poll();
        levelValues.add(cur);
        if (!duplicated.contains(cur)) {
          bfs.add(cur);
          duplicated.add(cur);
        }
      }
      if (bfs.size() > (2 * n + 1)) {
        var result = bfs.stream().sorted().toList();
        return result.get(n);
      }

      Set<Integer> buf = new HashSet<>();
      for (Integer levelValue : levelValues) {
        var case1 = levelValue * 2 + 1;
        if (!duplicated.contains(case1)) {
          buf.add(case1);
        }
        var case2 = levelValue * 3 + 1;
        if (!duplicated.contains(case2)) {
          buf.add(case2);
        }
      }
      var sortedBuf = buf.stream().sorted().toList();

      queue.addAll(sortedBuf);
    }


  }

  public static void main(String[] args) {
    var gfa = new GetNumberFromTheArray();
    System.out.println(gfa.getNumberFromTheArray(200));
//    for (int i = 0; i < 101; i++) {
//      System.out.printf("%d->%d\n", i, gfa.getNumberFromTheArray(i));
//    }
    // 1
    // 3  4
    // o(n) + O(nLogN)

  }

}