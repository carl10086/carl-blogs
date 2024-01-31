package com.cb.it.algo;

public class SubarraySum {


  public int subarraySum(int[] nums, int k) {

    int size = nums.length;

    int result = 0;

    int prefix = 0;
    for (int i = 0; i < size; i++) {
      for (int j = i; j < size; j++) {
        if (i == j) {
          prefix = nums[i];
        } else {
          prefix = prefix + nums[j];
        }

        if (prefix == k) {
          result++;
        }
      }
    }

    return result;
  }

  public static void main(String[] args) {
    SubarraySum solution = new SubarraySum();

    System.out.println(solution.subarraySum(new int[]{1, 2, 3}, 3));
  }

}
