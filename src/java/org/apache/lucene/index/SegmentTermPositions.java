package org.apache.lucene.index;

import java.io.IOException;

import org.apache.lucene.store.InputStream;

final class SegmentTermPositions extends SegmentTermDocs implements TermPositions {
  private InputStream proxStream;
  private int proxCount;
  private int position;

  SegmentTermPositions(SegmentReader p) throws IOException {
    super(p);
    this.proxStream = (InputStream) parent.proxStream.clone();
  }

  final void seek(TermInfo ti) throws IOException {
    super.seek(ti);
    if (ti != null)
      proxStream.seek(ti.proxPointer);
    proxCount = 0;
  }

  public final void close() throws IOException {
    super.close();
    proxStream.close();
  }

  public final int nextPosition() throws IOException {
    proxCount--;
    return position += proxStream.readVInt();
  }

  protected final void skippingDoc() throws IOException {
    for (int f = freq; f > 0; f--)      // skip all positions
      proxStream.readVInt();
  }

  public final boolean next() throws IOException {
    for (int f = proxCount; f > 0; f--)      // skip unread positions
      proxStream.readVInt();

    if (super.next()) {          // run super
      proxCount = freq;          // note frequency
      position = 0;          // reset position
      return true;
    }
    return false;
  }

  public final int read(final int[] docs, final int[] freqs)
    throws IOException {
    throw new UnsupportedOperationException("TermPositions does not support processing multiple documents in one call. Use TermDocs instead.");
  }


  /**
   * Called by super.skipTo().
   */
  protected void skipProx(long proxPointer) throws IOException {
    proxStream.seek(proxPointer);
    proxCount = 0;
  }

}
