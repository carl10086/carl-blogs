package com.cb.it.algo;

import java.util.ArrayList;
import java.util.List;

public class ReverseWords {

  public String reverseWords(String s) {
    List<String> words = new ArrayList<>();

    char[] charArray = s.toCharArray();

    var firstChar = charArray[0];

    boolean spaceState = !(firstChar == ' ');

    StringBuilder sb = new StringBuilder();

    for (char c : charArray) {
      boolean currentState = (c == ' ');
      /*状态相同加入*/
      if (spaceState == currentState) {
        sb.append(c);
      } else {
        /*状态不同就切换*/
        spaceState = currentState;
        words.add(sb.toString());
        sb = new StringBuilder();
        sb.append(c);
      }
    }

    if (!sb.isEmpty()) {
      words.add(sb.toString());
    }

    StringBuilder result = new StringBuilder();

    for (int i = words.size() - 1; i >= 0; i--) {
      String word = words.get(i);
      if (!word.isEmpty() && word.charAt(0) != ' ') {
        result.append(word).append(' ');
      }
    }
    result.deleteCharAt(result.length() - 1);
    return result.toString();
  }


  public static void main(String[] args) {
    ReverseWords solution = new ReverseWords();
    solution.reverseWords("  hello world  ");
  }

}
