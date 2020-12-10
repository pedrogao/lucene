package org.apache.lucene.util;



import java.util.Date;
import java.util.Random;

import junit.framework.TestCase;

public class TestPriorityQueue extends TestCase {

    public TestPriorityQueue(String name) {
        super(name);
    }

    private static class IntegerQueue extends PriorityQueue {
        public IntegerQueue(int count) {
            super();
            initialize(count);
        }

        protected boolean lessThan(Object a, Object b) {
            // 当时竟然还没有自动拆箱、装箱
            return ((Integer) a).intValue() < ((Integer) b).intValue();
        }
    }

    public void testPQ() throws Exception {
        testPQ(10000);
    }

    public static void testPQ(int count) {
        PriorityQueue pq = new IntegerQueue(count);
        Random gen = new Random();
        int sum = 0, sum2 = 0;

        Date start = new Date();

        for (int i = 0; i < count; i++) {
            int next = gen.nextInt();
            sum += next;
            pq.put(new Integer(next));
        }

        Date end = new Date();
        System.out.print(((float) (end.getTime() - start.getTime()) / count) * 1000);
        System.out.println(" microseconds/put");
        start = new Date();

        int last = Integer.MIN_VALUE;
        for (int i = 0; i < count; i++) {
            Integer next = (Integer) pq.pop();
            assertTrue(next.intValue() >= last);
            last = next.intValue();
            sum2 += last;
        }
        assertEquals(sum, sum2);
        end = new Date();
        System.out.print(((float) (end.getTime() - start.getTime()) / count) * 1000);
        System.out.println(" microseconds/pop");
    }

    public void testClear() {
        PriorityQueue pq = new IntegerQueue(3);
        pq.put(new Integer(2));
        pq.put(new Integer(3));
        pq.put(new Integer(1));
        assertEquals(3, pq.size());
        pq.clear();
        assertEquals(0, pq.size());
    }

    public void testFixedSize() {
        PriorityQueue pq = new IntegerQueue(3);
        pq.insert(new Integer(2));
        pq.insert(new Integer(3));
        pq.insert(new Integer(1));
        pq.insert(new Integer(5));
        pq.insert(new Integer(7));
        pq.insert(new Integer(1));
        assertEquals(3, pq.size());
        assertEquals(3, ((Integer) pq.top()).intValue());
    }
}
