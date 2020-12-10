package org.apache.lucene.store;

import java.io.IOException;

/**
 * Abstract class for output to a file in a Directory.  A random-access output
 * stream.  Used for all Lucene index output operations.
 * <p>
 * 程序输出流，往文件中输出，所以是写；
 * 随机输出，写文件
 *
 * @see Directory
 * @see InputStream
 */
public abstract class OutputStream {
  // 缓冲区大小
  static final int BUFFER_SIZE = 1024;

  private final byte[] buffer = new byte[BUFFER_SIZE];
  // 当前buffer在文件中的位置
  private long bufferStart = 0;        // position in file of buffer
  // 当前写buffer的位置
  private int bufferPosition = 0;      // position in buffer

  /**
   * Writes a single byte.
   * 写一个字节
   *
   * @see InputStream#readByte()
   */
  public final void writeByte(byte b) throws IOException {
    if (bufferPosition >= BUFFER_SIZE)
      flush();
    buffer[bufferPosition++] = b;
  }

  /**
   * Writes an array of bytes.
   * 写一堆字节
   *
   * @param b      the bytes to write
   * @param length the number of bytes to write
   * @see InputStream#readBytes(byte[], int, int)
   */
  public final void writeBytes(byte[] b, int length) throws IOException {
    for (int i = 0; i < length; i++)
      writeByte(b[i]);
  }

  /**
   * Writes an int as four bytes.
   * 写 int，4个字节
   *
   * @see InputStream#readInt()
   */
  public final void writeInt(int i) throws IOException {
    writeByte((byte) (i >> 24));
    writeByte((byte) (i >> 16));
    writeByte((byte) (i >> 8));
    writeByte((byte) i);
  }

  /**
   * Writes an int in a variable-length format.
   * Writes between one and five bytes.
   * Smaller values take fewer bytes.
   * Negative numbers are not supported.
   * <p>
   * <p>
   * 写 int，变长 int
   * 1~5个字节
   * 不支持负数
   *
   * @see InputStream#readVInt()
   */
  public final void writeVInt(int i) throws IOException {
    while ((i & ~0x7F) != 0) {
      writeByte((byte) ((i & 0x7f) | 0x80));
      i >>>= 7;
    }
    writeByte((byte) i);
  }

  /**
   * Writes a long as eight bytes.
   * 写 long，8个字节
   *
   * @see InputStream#readLong()
   */
  public final void writeLong(long i) throws IOException {
    writeInt((int) (i >> 32));
    writeInt((int) i);
  }

  /**
   * Writes an long in a variable-length format.  Writes between one and five
   * bytes.  Smaller values take fewer bytes.  Negative numbers are not
   * supported.
   * <p>
   * 变长 long
   * 1～5字节
   * 不支持负数
   *
   * @see InputStream#readVLong()
   */
  public final void writeVLong(long i) throws IOException {
    while ((i & ~0x7F) != 0) {
      writeByte((byte) ((i & 0x7f) | 0x80));
      i >>>= 7;
    }
    writeByte((byte) i);
  }

  /**
   * Writes a string.
   * 写 string
   *
   * @see InputStream#readString()
   */
  public final void writeString(String s) throws IOException {
    int length = s.length();
    // 先写入字符串长度
    writeVInt(length);
    // 再写入 chars
    writeChars(s, 0, length);
  }

  /**
   * Writes a sequence of UTF-8 encoded characters from a string.
   * 按照 utf8 编码写入
   *
   * @param s      the source of the characters
   * @param start  the first character in the sequence
   * @param length the number of characters in the sequence
   * @see InputStream#readChars(char[], int, int)
   */
  public final void writeChars(String s, int start, int length) throws IOException {
    // 得到终点坐标
    final int end = start + length;
    for (int i = start; i < end; i++) {
      // 得到编码值
      final int code = (int) s.charAt(i);
      // 如果在1～127之间，写入单字节
      if (code >= 0x01 && code <= 0x7F)
        writeByte((byte) code);
      else if (((code >= 0x80) && (code <= 0x7FF)) || code == 0) {
        // 如果在 128～2047之间，或者 等于 0，写入2个字节
        writeByte((byte) (0xC0 | (code >> 6)));
        writeByte((byte) (0x80 | (code & 0x3F)));
      } else {
        // 其它情况，即 >=2048，写入3个字节
        writeByte((byte) (0xE0 | (code >>> 12)));
        writeByte((byte) (0x80 | ((code >> 6) & 0x3F)));
        writeByte((byte) (0x80 | (code & 0x3F)));
      }
    }
  }

  /**
   * Forces any buffered output to be written.
   * 刷新缓冲区到文件
   */
  protected final void flush() throws IOException {
    flushBuffer(buffer, bufferPosition);
    bufferStart += bufferPosition;
    bufferPosition = 0;
  }

  /**
   * Expert: implements buffer write.  Writes bytes at the current position in
   * the output.
   *
   * @param b   the bytes to write
   * @param len the number of bytes to write
   */
  protected abstract void flushBuffer(byte[] b, int len) throws IOException;

  /**
   * Closes this stream to further operations.
   */
  public void close() throws IOException {
    flush();
  }

  /**
   * Returns the current position in this file, where the next write will
   * occur.
   * <p>
   * 得到当前写入点在文件中的位置
   *
   * @see #seek(long)
   */
  public final long getFilePointer() throws IOException {
    return bufferStart + bufferPosition;
  }

  /**
   * Sets current position in this file, where the next write will occur.
   * 设置在文件中的写入点
   *
   * @see #getFilePointer()
   */
  public void seek(long pos) throws IOException {
    flush();
    bufferStart = pos;
  }

  /**
   * The number of bytes in the file.
   */
  public abstract long length() throws IOException;


}
