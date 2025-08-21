/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource.Factory;

/**
 * @deprecated Use {@link DefaultDataSource.Factory} instead.
 */
@UnstableApi
@Deprecated
public final class DefaultDataSourceFactory implements Factory {

  private final Context context;
  @Nullable private final TransferListener listener;
  private final DataSource.Factory baseDataSourceFactory;

  /**
   * Creates an instance.
   *
   * @param context A context.
   */
  public DefaultDataSourceFactory(Context context) {
    this(context, /* userAgent= */ (String) null, /* listener= */ null);
  }

  /**
   * Creates an instance.
   *
   * @param context A context.
   * @param userAgent The user agent that will be used when requesting remote data, or {@code null}
   *     to use the default user agent of the underlying platform.
   */
  public DefaultDataSourceFactory(Context context, @Nullable String userAgent) {
    this(context, userAgent, /* listener= */ null);
  }

  /**
   * Creates an instance.
   *
   * @param context A context.
   * @param userAgent The user agent that will be used when requesting remote data, or {@code null}
   *     to use the default user agent of the underlying platform.
   * @param listener An optional listener.
   */
  public DefaultDataSourceFactory(
      Context context, @Nullable String userAgent, @Nullable TransferListener listener) {
    this(context, listener, new DefaultHttpDataSource.Factory().setUserAgent(userAgent));
  }

  /**
   * Creates an instance.
   *
   * @param context A context.
   * @param baseDataSourceFactory A {@link Factory} to be used to create a base {@link DataSource}
   *     for {@link DefaultDataSource}.
   * @see DefaultDataSource#DefaultDataSource(Context, DataSource)
   */
  public DefaultDataSourceFactory(Context context, DataSource.Factory baseDataSourceFactory) {
    this(context, /* listener= */ null, baseDataSourceFactory);
  }

  /**
   * Creates an instance.
   *
   * @param context A context.
   * @param listener An optional listener.
   * @param baseDataSourceFactory A {@link Factory} to be used to create a base {@link DataSource}
   *     for {@link DefaultDataSource}.
   * @see DefaultDataSource#DefaultDataSource(Context, DataSource)
   */
  public DefaultDataSourceFactory(
      Context context,
      @Nullable TransferListener listener,
      DataSource.Factory baseDataSourceFactory) {
    this.context = context.getApplicationContext();
    this.listener = listener;
    this.baseDataSourceFactory = baseDataSourceFactory;
  }

  @Override
  public DefaultDataSource createDataSource() {
    DefaultDataSource dataSource =
        new DefaultDataSource(context, baseDataSourceFactory.createDataSource());
    if (listener != null) {
      dataSource.addTransferListener(listener);
    }
    return dataSource;
  }
}
