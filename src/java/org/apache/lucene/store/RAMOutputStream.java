package org.apache.lucene.store;

import java.io.IOException;

/**
 * A memory-resident {@link OutputStream} implementation.
 * 基于内存的程序输出流
 *
 * @version $Id: RAMOutputStream.java,v 1.2 2004/03/29 22:48:05 cutting Exp $
 */

public class RAMOutputStream extends OutputStream {
  // 内存文件
  private RAMFile file;
  // 指针
  private int pointer = 0;

  /**
   * Construct an empty output buffer.
   */
  public RAMOutputStream() {
    this(new RAMFile());
  }

  RAMOutputStream(RAMFile f) {
    file = f;
  }

  /**
   * Copy the current contents of this buffer to the named output.
   */
  public void writeTo(OutputStream out) throws IOException {
    flush();
    final long end = file.length;
    long pos = 0;
    int buffer = 0;
    while (pos < end) {
      int length = BUFFER_SIZE;
      long nextPos = pos + length;
      if (nextPos > end) {                        // at the last buffer
        length = (int) (end - pos);
      }
      out.writeBytes((byte[]) file.buffers.elementAt(buffer++), length);
      pos = nextPos;
    }
  }

  /**
   * Resets this to an empty buffer.
   */
  public void reset() {
    try {
      seek(0);
    } catch (IOException e) {                     // should never happen
      throw new RuntimeException(e.toString());
    }

    file.length = 0;
  }

  public void flushBuffer(byte[] src, int len) {
    int bufferNumber = pointer / BUFFER_SIZE;
    int bufferOffset = pointer % BUFFER_SIZE;
    int bytesInBuffer = BUFFER_SIZE - bufferOffset;
    int bytesToCopy = bytesInBuffer >= len ? len : bytesInBuffer;

    if (bufferNumber == file.buffers.size())
      file.buffers.addElement(new byte[BUFFER_SIZE]);

    byte[] buffer = (byte[]) file.buffers.elementAt(bufferNumber);
    System.arraycopy(src, 0, buffer, bufferOffset, bytesToCopy);

    if (bytesToCopy < len) {        // not all in one buffer
      int srcOffset = bytesToCopy;
      bytesToCopy = len - bytesToCopy;      // remaining bytes
      bufferNumber++;
      if (bufferNumber == file.buffers.size())
        file.buffers.addElement(new byte[BUFFER_SIZE]);
      buffer = (byte[]) file.buffers.elementAt(bufferNumber);
      System.arraycopy(src, srcOffset, buffer, 0, bytesToCopy);
    }
    pointer += len;
    if (pointer > file.length)
      file.length = pointer;

    file.lastModified = System.currentTimeMillis();
  }

  public void close() throws IOException {
    super.close();
  }

  public void seek(long pos) throws IOException {
    super.seek(pos);
    pointer = (int) pos;
  }

  public long length() {
    return file.length;
  }
}
