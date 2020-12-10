package org.apache.lucene.store;


import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

/**
 * 进程通信锁
 * An interprocess mutex lock.
 * <p>Typical use might look like:<pre>
 * new Lock.With(directory.makeLock("my.lock")) {
 *     public Object doBody() {
 *       <i>... code to execute while locked ...</i>
 *     }
 *   }.run();
 * </pre>
 *
 * @author Doug Cutting
 * @version $Id: Lock.java,v 1.12 2004/05/11 17:43:28 cutting Exp $
 * @see Directory#makeLock(String)
 */
public abstract class Lock {

  // 锁轮询的间隔
  public static long LOCK_POLL_INTERVAL = 1000;

  /**
   * Attempts to obtain exclusive access and immediately
   * return upon success or failure.
   *
   * @return true iff exclusive access is obtained
   */
  public abstract boolean obtain() throws IOException;

  /**
   * 尝试去获得排他锁
   * Attempts to obtain an exclusive lock within amount
   * of time given. Currently polls once per second until
   * lockWaitTimeout is passed.
   *
   * @param lockWaitTimeout length of time to wait in ms
   * @return true if lock was obtained
   * @throws IOException if lock wait times out or obtain() throws an IOException
   */
  public boolean obtain(long lockWaitTimeout) throws IOException {
    boolean locked = obtain();
    int maxSleepCount = (int) (lockWaitTimeout / LOCK_POLL_INTERVAL);
    int sleepCount = 0;
    while (!locked) {
      if (++sleepCount == maxSleepCount) {
        throw new IOException("Lock obtain timed out: " + this.toString());
      }
      try {
        Thread.sleep(LOCK_POLL_INTERVAL);
      } catch (InterruptedException e) {
        throw new IOException(e.toString());
      }
      locked = obtain();
    }
    return locked;
  }

  /**
   * Releases exclusive access.
   */
  public abstract void release();

  /**
   * Returns true if the resource is currently locked.  Note that one must
   * still call {@link #obtain()} before using the resource.
   */
  public abstract boolean isLocked();


  /**
   * Utility class for executing code with exclusive access.
   * 获得锁以后，可以做的事情
   */
  public abstract static class With {
    private Lock lock;
    private long lockWaitTimeout;

    /**
     * Constructs an executor that will grab the named lock.
     * Defaults lockWaitTimeout to Lock.COMMIT_LOCK_TIMEOUT.
     *
     * @deprecated Kept only to avoid breaking existing code.
     */
    public With(Lock lock) {
      this(lock, IndexWriter.COMMIT_LOCK_TIMEOUT);
    }

    /**
     * Constructs an executor that will grab the named lock.
     */
    public With(Lock lock, long lockWaitTimeout) {
      this.lock = lock;
      this.lockWaitTimeout = lockWaitTimeout;
    }

    /**
     * Code to execute with exclusive access.
     */
    protected abstract Object doBody() throws IOException;

    /**
     * Calls {@link #doBody} while <i>lock</i> is obtained.  Blocks if lock
     * cannot be obtained immediately.  Retries to obtain lock once per second
     * until it is obtained, or until it has tried ten times. Lock is released when
     * {@link #doBody} exits.
     */
    public Object run() throws IOException {
      boolean locked = false;
      try {
        locked = lock.obtain(lockWaitTimeout);
        return doBody();
      } finally {
        if (locked)
          lock.release();
      }
    }
  }

}
