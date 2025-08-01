/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import static java.lang.Math.min;

import android.net.http.UploadDataProvider;
import android.net.http.UploadDataSink;
import android.os.Build;
import androidx.annotation.RequiresExtension;
import java.io.IOException;
import java.nio.ByteBuffer;

/** A {@link UploadDataProvider} implementation that provides data from a {@code byte[]}. */
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
/* package */ final class ByteArrayUploadDataProvider extends UploadDataProvider {

  private final byte[] data;

  private int position;

  public ByteArrayUploadDataProvider(byte[] data) {
    this.data = data;
  }

  @Override
  public long getLength() {
    return data.length;
  }

  @Override
  public void read(UploadDataSink uploadDataSink, ByteBuffer byteBuffer) throws IOException {
    int readLength = min(byteBuffer.remaining(), data.length - position);
    byteBuffer.put(data, position, readLength);
    position += readLength;
    uploadDataSink.onReadSucceeded(false);
  }

  @Override
  public void rewind(UploadDataSink uploadDataSink) throws IOException {
    position = 0;
    uploadDataSink.onRewindSucceeded();
  }
}
