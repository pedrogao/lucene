package org.apache.lucene.util;



/** A PriorityQueue maintains a partial ordering of its elements such that the
 least element can always be found in constant time.  Put()'s and pop()'s
 require log(size) time.
 其实就是 堆 实现的优先队列，实现方式参考 算法4
 */
public abstract class PriorityQueue {
    private Object[] heap;
    private int size;
    private int maxSize;

    /** Determines the ordering of objects in this priority queue.  Subclasses
     must define this one method. */
    protected abstract boolean lessThan(Object a, Object b);

    /** Subclass constructors must call this. */
    protected final void initialize(int maxSize) {
        size = 0;
        // 堆的实现，默认不使用 0 元素
        int heapSize = maxSize + 1;
        heap = new Object[heapSize];
        this.maxSize = maxSize;
    }

    /**
     * Adds an Object to a PriorityQueue in log(size) time.
     * If one tries to add more objects than maxSize from initialize
     * a RuntimeException (ArrayIndexOutOfBound) is thrown.
     */
    public final void put(Object element) {
        size++;
        heap[size] = element;
        upHeap();
    }

    /**
     * Adds element to the PriorityQueue in log(size) time if either
     * the PriorityQueue is not full, or not lessThan(element, top()).
     * @param element
     * @return true if element is added, false otherwise.
     */
    public boolean insert(Object element) {
        // 队列未满的时候，直接添加
        if (size < maxSize) {
            put(element);
            return true;
        } else if (size > 0 && !lessThan(element, top())) {
            // 如果队列已满，且队列中有元素，则直接将放到堆顶
            heap[1] = element;
            adjustTop();
            return true;
        } else
            return false;
    }

    /** Returns the least element of the PriorityQueue in constant time. */
    public final Object top() {
        if (size > 0)
            return heap[1];
        else
            return null;
    }

    /** Removes and returns the least element of the PriorityQueue in log(size)
     time. */
    public final Object pop() {
        if (size > 0) {
            Object result = heap[1];              // save first value
            heap[1] = heap[size];              // move last to first
            heap[size] = null;              // permit GC of objects
            size--;
            downHeap();                  // adjust heap
            return result;
        } else
            return null;
    }

    /** Should be called when the Object at top changes values.  Still log(n)
     * worst case, but it's at least twice as fast to <pre>
     *  { pq.top().change(); pq.adjustTop(); }
     * </pre> instead of <pre>
     *  { o = pq.pop(); o.change(); pq.push(o); }
     * </pre>
     */
    public final void adjustTop() {
        downHeap();
    }


    /** Returns the number of elements currently stored in the PriorityQueue. */
    public final int size() {
        return size;
    }

    /** Removes all entries from the PriorityQueue. */
    public final void clear() {
        for (int i = 0; i <= size; i++)
            heap[i] = null;
        size = 0;
    }

    private final void upHeap() {
        int i = size;
        Object node = heap[i];              // save bottom node
        int j = i >>> 1; // 无符号右移，想当如 x/2，例如 2 >>> 1 = 1，9 >>> 1 = 4
        while (j > 0 && lessThan(node, heap[j])) {
            heap[i] = heap[j];              // shift parents down
            i = j;
            j = j >>> 1;
        }
        heap[i] = node;                  // install saved node
    }

    private final void downHeap() {
        int i = 1;
        Object node = heap[i];              // save top node
        int j = i << 1;                  // find smaller child
        int k = j + 1;
        if (k <= size && lessThan(heap[k], heap[j])) {
            j = k;
        }
        while (j <= size && lessThan(heap[j], node)) {
            heap[i] = heap[j];              // shift up child
            i = j;
            j = i << 1;
            k = j + 1;
            if (k <= size && lessThan(heap[k], heap[j])) {
                j = k;
            }
        }
        heap[i] = node;                  // install saved node
    }
}
