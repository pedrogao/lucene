package org.apache.lucene.store;

/**
 * A memory-resident {@link InputStream} implementation.
 *
 * @version $Id: RAMInputStream.java,v 1.2 2004/03/29 22:48:05 cutting Exp $
 */

class RAMInputStream extends InputStream implements Cloneable {
  private RAMFile file;
  private int pointer = 0;

  public RAMInputStream(RAMFile f) {
    file = f;
    // 文件长度
    length = file.length;
  }

  // 区间读取
  public void readInternal(byte[] dest, int destOffset, int len) {
    int remainder = len;
    int start = pointer;
    while (remainder != 0) {
      int bufferNumber = start / BUFFER_SIZE;
      int bufferOffset = start % BUFFER_SIZE;
      int bytesInBuffer = BUFFER_SIZE - bufferOffset;
      int bytesToCopy = bytesInBuffer >= remainder ? remainder : bytesInBuffer;
      byte[] buffer = (byte[]) file.buffers.elementAt(bufferNumber);
      System.arraycopy(buffer, bufferOffset, dest, destOffset, bytesToCopy);
      destOffset += bytesToCopy;
      start += bytesToCopy;
      remainder -= bytesToCopy;
    }
    pointer += len;
  }

  public void close() {
  }

  public void seekInternal(long pos) {
    pointer = (int) pos;
  }
}
