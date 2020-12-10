package org.apache.lucene.store;


import java.io.IOException;

/**
 * A Directory is a flat list of files.
 * Files may be written once, when they are created.
 * 文件只能在创建的时候被写入
 * <p>
 * Once a file is created it may only be opened for read, or deleted.
 * 文件被创建后只能被读或者删除
 * <p>
 * Random access is permitted both when reading and writing.
 * 在读或写的时候允许随机访问
 *
 * <p> Java's i/o APIs not used directly, but rather all i/o is
 * through this API.  This permits things such as: <ul>
 * <li> implementation of RAM-based indices;
 * <li> implementation indices stored in a database, via JDBC;
 * <li> implementation of an index as a single file;
 * </ul>
 *
 * @author Doug Cutting
 */
public abstract class Directory {
  /**
   * Returns an array of strings, one for each file in the directory.
   * 获取所有文件名称
   */
  public abstract String[] list() throws IOException;

  /**
   * Returns true iff a file with the given name exists.
   * 判断文件是否存在
   */
  public abstract boolean fileExists(String name) throws IOException;

  /**
   * Returns the time the named file was last modified.
   * 返回文件最后修改的时间戳
   */
  public abstract long fileModified(String name) throws IOException;

  /**
   * Set the modified time of an existing file to now.
   * 设置已存在文件的修改时间为现在
   */
  public abstract void touchFile(String name) throws IOException;

  /**
   * Removes an existing file in the directory.
   */
  public abstract void deleteFile(String name) throws IOException;

  /**
   * Renames an existing file in the directory.
   * If a file already exists with the new name, then it is replaced.
   * This replacement should be atomic.
   */
  public abstract void renameFile(String from, String to) throws IOException;

  /**
   * Returns the length of a file in the directory.
   */
  public abstract long fileLength(String name) throws IOException;

  /**
   * Creates a new, empty file in the directory with the given name.
   * Returns a stream writing this file.
   */
  public abstract OutputStream createFile(String name) throws IOException;

  /**
   * Returns a stream reading an existing file.
   */
  public abstract InputStream openFile(String name) throws IOException;

  /**
   * Construct a {@link Lock}.
   * 构建一个锁
   *
   * @param name the name of the lock file
   */
  public abstract Lock makeLock(String name);

  /**
   * Closes the store.
   * 关闭
   */
  public abstract void close() throws IOException;
}
