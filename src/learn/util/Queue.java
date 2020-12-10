package util;

import java.util.Arrays;

/**
 * >>：带符号右移。正数右移高位补0，负数右移高位补1。比如：
 * <p>
 * 4 >> 1，结果是2；-4 >> 1，结果是-2。-2 >> 1，结果是-1。
 * <p>
 * >>>：无符号右移。无论是正数还是负数，高位通通补0。
 * <p>
 * 对于正数而言，>>和>>>没区别。
 * <p>
 * 对于负数而言，-2 >>> 1，结果是2147483647（Integer.MAX_VALUE），-1 >>> 1，结果是2147483647（Integer.MAX_VALUE）。
 */
public class Queue {

    public static void main(String[] args) {
        int i = 1 >>> 1;
        int j = 1 >>> 2;
        int k = 1 >>> 3;
        int l = 1 >>> 4;
        System.out.println(Arrays.toString(new int[]{i, j, k, l}));

        int m = 2 >>> 1;
        int n = 9 >>> 1;
        System.out.println(Arrays.toString(new int[]{m, n}));
    }
}
