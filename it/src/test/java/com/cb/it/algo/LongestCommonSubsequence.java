package com.cb.it.algo;

public class LongestCommonSubsequence {

  public int longestCommonSubsequence(String text1, String text2) {
    int l1 = text1.length();
    int l2 = text2.length();

    /*代表从 0->i 和 0-> j 的最大长度*/
    int[][] dp = new int[l1][l2];

    if (text1.charAt(0) == text2.charAt(0)) {
      dp[0][0] = 1;
    }
    /*先处理 i=0*/
    for (int j = 1; j < l2; j++) {
      if (text1.charAt(0) == text2.charAt(j)) {
        dp[0][j] = 1;
      } else {
        dp[0][j] = dp[0][j - 1];
      }
    }

    /*再处理 j=0*/
    for (int i = 1; i < l1; i++) {
      if (text1.charAt(i) == text2.charAt(0)) {
        dp[i][0] = 1;
      } else {
        dp[i][0] = dp[i - 1][0];
      }
    }

    /*处理通用情况*/
    for (int i = 1; i < l1; i++) {
      for (int j = 1; j < l2; j++) {
        if (text1.charAt(i) == text2.charAt(j)) {
          /*fixme ? maybe i-1 -> j or i -> j-1*/
          dp[i][j] = dp[i - 1][j - 1] + 1;
        } else {
          int case1 = dp[i - 1][j];
          int case2 = dp[i][j - 1];
          dp[i][j] = Math.max(case2, case1);
        }
      }
    }
    return dp[l1 - 1][l2 - 1];
  }


  public static void main(String[] args) {
    LongestCommonSubsequence longestCommonSubsequence = new LongestCommonSubsequence();
    int result = longestCommonSubsequence.longestCommonSubsequence("abc", "def");
    System.out.println(result);
  }
}
