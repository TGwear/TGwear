/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.datasource;

import androidx.media3.common.C;
import androidx.media3.common.PriorityTaskManager;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource.Factory;

/**
 * @deprecated Use {@link PriorityDataSource.Factory}.
 */
@Deprecated
@UnstableApi
public final class PriorityDataSourceFactory implements Factory {

  private final Factory upstreamFactory;
  private final PriorityTaskManager priorityTaskManager;
  private final @C.Priority int priority;

  /**
   * @param upstreamFactory A {@link DataSource.Factory} to be used to create an upstream {@link
   *     DataSource} for {@link PriorityDataSource}.
   * @param priorityTaskManager The priority manager to which PriorityDataSource task is registered.
   * @param priority The {@link C.Priority} of the {@link PriorityDataSource} task.
   */
  public PriorityDataSourceFactory(
      Factory upstreamFactory, PriorityTaskManager priorityTaskManager, @C.Priority int priority) {
    this.upstreamFactory = upstreamFactory;
    this.priorityTaskManager = priorityTaskManager;
    this.priority = priority;
  }

  @Override
  public PriorityDataSource createDataSource() {
    return new PriorityDataSource(
        upstreamFactory.createDataSource(), priorityTaskManager, priority);
  }
}
