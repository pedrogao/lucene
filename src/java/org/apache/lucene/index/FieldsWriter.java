package org.apache.lucene.index;

import java.util.Enumeration;
import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.OutputStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

final class FieldsWriter {
  private FieldInfos fieldInfos;
  private OutputStream fieldsStream;
  private OutputStream indexStream;

  FieldsWriter(Directory d, String segment, FieldInfos fn) throws IOException {
    fieldInfos = fn;
    fieldsStream = d.createFile(segment + ".fdt");
    indexStream = d.createFile(segment + ".fdx");
  }

  final void close() throws IOException {
    fieldsStream.close();
    indexStream.close();
  }

  final void addDocument(Document doc) throws IOException {
    indexStream.writeLong(fieldsStream.getFilePointer());

    int storedCount = 0;
    Enumeration fields = doc.fields();
    while (fields.hasMoreElements()) {
      Field field = (Field) fields.nextElement();
      if (field.isStored())
        storedCount++;
    }
    fieldsStream.writeVInt(storedCount);

    fields = doc.fields();
    while (fields.hasMoreElements()) {
      Field field = (Field) fields.nextElement();
      if (field.isStored()) {
        fieldsStream.writeVInt(fieldInfos.fieldNumber(field.name()));

        byte bits = 0;
        if (field.isTokenized())
          bits |= 1;
        fieldsStream.writeByte(bits);

        fieldsStream.writeString(field.stringValue());
      }
    }
  }
}
