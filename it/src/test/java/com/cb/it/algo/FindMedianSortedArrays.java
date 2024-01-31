package com.cb.it.algo;

public class FindMedianSortedArrays {

  public double findMedianSortedArrays(int[] nums1, int[] nums2) {
    int length1 = nums1.length;
    int length2 = nums2.length;

    int total = length1 + length2;
    boolean even = total % 2 == 0;
    int mid = total / 2;

    int count = 0;
    int left = 0;
    int right = 0;

    double midValue = 0;
    double midPlus = 0;
    double v = 0;

    while (true) {
      boolean useLeft = true;
      if (left == length1) {
        useLeft = false;
      } else if (right == length2) {
        useLeft = true;
      } else {
        useLeft = nums1[left] < nums2[right];
      }

      if (useLeft) {
        v = nums1[left];
        /*use left*/
        left++;
      } else {
        v = nums2[right];
        right++;
      }
      count++;

      if (count == mid) {
        midValue = v;
      } else if (count == mid + 1) {
        midPlus = v;
        break;
      }
    }

    if (even) {
      return (midValue + midPlus) / 2;
    } else {
      return midPlus;
    }
  }

  public static void main(String[] args) {

    System.out.println(
        new FindMedianSortedArrays().findMedianSortedArrays(new int[]{1, 3}, new int[]{2})
    );

  }

}
