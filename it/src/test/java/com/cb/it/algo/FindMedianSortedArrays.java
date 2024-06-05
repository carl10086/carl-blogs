package com.cb.it.algo;


public class FindMedianSortedArrays {

  public double findMedianSortedArrays(int[] nums1, int[] nums2) {
    int l1 = nums1.length;
    int l2 = nums2.length;
    int half = (l1 + l2) / 2;

    /*奇数的话应该仅仅有1个值，偶数有2个*/
    boolean isOdd = (l1 + l2) % 2 != 0;

    java.util.List<Integer> result = new java.util.ArrayList<>();

    int idx1 = 0;
    int idx2 = 0;

    int count = 0;

    int smallValue = 0;

    while (true) {

      count++;
      if (idx1 == l1) {
        smallValue = nums2[idx2];
        idx2++;
      } else if (idx2 == l2) {
        smallValue = nums1[idx1];
        idx1++;
      } else {
        int v1 = nums1[idx1];
        int v2 = nums2[idx2];

        if (v1 < v2) {
          smallValue = v1;
          idx1++;
        } else {
          smallValue = v2;
          idx2++;
        }
      }

      if (isOdd) {
        if (count == half + 1) {
          return smallValue;
        }
      } else {
        if (count == half) {
          result.add(smallValue);
        } else if (count == half + 1) {
          result.add(smallValue);
          break;
        }
      }
    }

    return (result.get(0).doubleValue() + result.get(1).doubleValue()) / 2.0;
  }

  public static void main(String[] args) {
    FindMedianSortedArrays findMedianSortedArrays = new FindMedianSortedArrays();
    System.out.println(findMedianSortedArrays.findMedianSortedArrays(new int[]{1, 3}, new int[]{2}) == 1);
//    System.out.println(findMedianSortedArrays.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4}) == 2.5);
//    System.out.println(findMedianSortedArrays.findMedianSortedArrays(new int[]{1, 3}, new int[]{2}) == 2);
  }
}
