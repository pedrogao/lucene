package org.apache.lucene.store;

import java.io.IOException;

/**
 * Abstract base class for input from a file in a {@link Directory}.  A
 * random-access input stream.  Used for all Lucene index input operations.
 * 读文件
 *
 * @see Directory
 * @see OutputStream
 */
public abstract class InputStream implements Cloneable {
  // 缓冲区大小 1024
  static final int BUFFER_SIZE = OutputStream.BUFFER_SIZE;

  private byte[] buffer;
  // 读取字符串所需的chars
  private char[] chars;

  private long bufferStart = 0;        // position in file of buffer
  private int bufferLength = 0;        // end of valid bytes
  private int bufferPosition = 0;      // next byte to read

  protected long length;        // set by subclasses

  /**
   * Reads and returns a single byte.
   * 读取单个字节
   *
   * @see OutputStream#writeByte(byte)
   */
  public final byte readByte() throws IOException {
    if (bufferPosition >= bufferLength)
      refill();
    return buffer[bufferPosition++];
  }

  /**
   * Reads a specified number of bytes into an array at the specified offset.
   *
   * @param b      the array to read bytes into
   * @param offset the offset in the array to start storing bytes
   * @param len    the number of bytes to read
   * @see OutputStream#writeBytes(byte[], int)
   */
  public final void readBytes(byte[] b, int offset, int len) throws IOException {
    if (len < BUFFER_SIZE) {
      for (int i = 0; i < len; i++)      // read byte-by-byte
        b[i + offset] = (byte) readByte();
    } else {            // read all-at-once
      long start = getFilePointer();
      seekInternal(start);
      readInternal(b, offset, len);

      bufferStart = start + len;      // adjust stream variables
      bufferPosition = 0;
      bufferLength = 0;          // trigger refill() on read
    }
  }

  /**
   * Reads four bytes and returns an int.
   * 读取 int
   *
   * @see OutputStream#writeInt(int)
   */
  public final int readInt() throws IOException {
    // 一次读取4个字节
    return ((readByte() & 0xFF) << 24) | ((readByte() & 0xFF) << 16)
      | ((readByte() & 0xFF) << 8) | (readByte() & 0xFF);
  }

  /**
   * Reads an int stored in variable-length format.  Reads between one and
   * five bytes.  Smaller values take fewer bytes.  Negative numbers are not
   * supported.
   * 读取变长 vint
   *
   * @see OutputStream#writeVInt(int)
   */
  public final int readVInt() throws IOException {
    byte b = readByte();
    int i = b & 0x7F;
    for (int shift = 7; (b & 0x80) != 0; shift += 7) {
      b = readByte();
      i |= (b & 0x7F) << shift;
    }
    return i;
  }

  /**
   * Reads eight bytes and returns a long.
   * 读取 long
   *
   * @see OutputStream#writeLong(long)
   */
  public final long readLong() throws IOException {
    return (((long) readInt()) << 32) | (readInt() & 0xFFFFFFFFL);
  }

  /**
   * Reads a long stored in variable-length format.  Reads between one and
   * nine bytes.  Smaller values take fewer bytes.  Negative numbers are not
   * supported.
   * 读取 vlong
   */
  public final long readVLong() throws IOException {
    byte b = readByte();
    long i = b & 0x7F;
    for (int shift = 7; (b & 0x80) != 0; shift += 7) {
      b = readByte();
      i |= (b & 0x7FL) << shift;
    }
    return i;
  }

  /**
   * Reads a string.
   * 读取字符串
   *
   * @see OutputStream#writeString(String)
   */
  public final String readString() throws IOException {
    // 先读取字符串长度
    int length = readVInt();
    // 初始化字符串所需的 chars
    if (chars == null || length > chars.length)
      chars = new char[length];
    readChars(chars, 0, length);
    return new String(chars, 0, length);
  }

  /**
   * Reads UTF-8 encoded characters into an array.
   * 读取 utf8编码的 chars
   *
   * @param buffer the array to read characters into
   * @param start  the offset in the array to start storing characters
   * @param length the number of characters to read
   * @see OutputStream#writeChars(String, int, int)
   */
  public final void readChars(char[] buffer, int start, int length) throws IOException {
    // 得到尾坐标
    final int end = start + length;
    for (int i = start; i < end; i++) {
      byte b = readByte();
      if ((b & 0x80) == 0) // 一个字节
        buffer[i] = (char) (b & 0x7F);
      else if ((b & 0xE0) != 0xE0) { // 2个字节
        buffer[i] = (char) (((b & 0x1F) << 6)
          | (readByte() & 0x3F));
      } else // 3个字节
        buffer[i] = (char) (((b & 0x0F) << 12)
          | ((readByte() & 0x3F) << 6)
          | (readByte() & 0x3F));
    }
  }

  // 重新填充缓冲区
  private void refill() throws IOException {
    long start = bufferStart + bufferPosition;
    long end = start + BUFFER_SIZE;
    // 如果 end 大于 文件的总长度
    if (end > length)          // don't read past EOF
      end = length;
    // 计算缓冲区大小
    bufferLength = (int) (end - start);
    // 如果大小为0
    if (bufferLength == 0)
      throw new IOException("read past EOF");

    if (buffer == null)
      buffer = new byte[BUFFER_SIZE];      // allocate buffer lazily
    // 区间读取
    readInternal(buffer, 0, bufferLength);

    bufferStart = start;
    bufferPosition = 0;
  }

  /**
   * Expert: implements buffer refill.  Reads bytes from the current position
   * in the input.
   * 读取文件的某个区间
   *
   * @param b      the array to read bytes into
   * @param offset the offset in the array to start storing bytes
   * @param length the number of bytes to read
   */
  protected abstract void readInternal(byte[] b, int offset, int length) throws IOException;

  /**
   * Closes the stream to futher operations.
   */
  public abstract void close() throws IOException;

  /**
   * Returns the current position in this file, where the next read will
   * occur.
   *
   * @see #seek(long)
   */
  public final long getFilePointer() {
    return bufferStart + bufferPosition;
  }

  /**
   * Sets current position in this file, where the next read will occur.
   *
   * @see #getFilePointer()
   */
  public final void seek(long pos) throws IOException {
    // 如果位置正在缓冲区
    if (pos >= bufferStart && pos < (bufferStart + bufferLength))
      bufferPosition = (int) (pos - bufferStart);  // seek within buffer
    else {
      // 否则，重新读取缓冲区和文件
      bufferStart = pos;
      bufferPosition = 0;
      bufferLength = 0;          // trigger refill() on read()
      seekInternal(pos);
    }
  }

  /**
   * Expert: implements seek.  Sets current position in this file, where the
   * next {@link #readInternal(byte[], int, int)} will occur.
   *
   * @see #readInternal(byte[], int, int)
   */
  protected abstract void seekInternal(long pos) throws IOException;

  /**
   * The number of bytes in the file.
   */
  public final long length() {
    return length;
  }

  /**
   * Returns a clone of this stream.
   *
   * <p>Clones of a stream access the same data, and are positioned at the same
   * point as the stream they were cloned from.
   *
   * <p>Expert: Subclasses must ensure that clones may be positioned at
   * different points in the input from each other and from the stream they
   * were cloned from.
   */
  public Object clone() {
    InputStream clone = null;
    try {
      clone = (InputStream) super.clone();
    } catch (CloneNotSupportedException e) {
    }

    if (buffer != null) {
      clone.buffer = new byte[BUFFER_SIZE];
      System.arraycopy(buffer, 0, clone.buffer, 0, bufferLength);
    }

    clone.chars = null;

    return clone;
  }

}
