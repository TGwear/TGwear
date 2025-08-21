/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.test.session.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.ViewGroup;

/** An activity used for surface test */
public class SurfaceActivity extends Activity {
  private ViewGroup rootViewGroup;

  private SurfaceView firstSurfaceView;
  private SurfaceHolder firstSurfaceHolder;

  private SurfaceView secondSurfaceView;
  private SurfaceHolder secondSurfaceHolder;

  private TextureView firstTextureView;

  private TextureView secondTextureView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TestUtils.setKeepScreenOn(this);
    setContentView(R.layout.activity_surface);

    rootViewGroup = findViewById(R.id.root_view_group);

    firstSurfaceView = findViewById(R.id.surface_view_first);
    firstSurfaceHolder = firstSurfaceView.getHolder();

    secondSurfaceView = findViewById(R.id.surface_view_second);
    secondSurfaceHolder = secondSurfaceView.getHolder();

    firstTextureView = findViewById(R.id.texture_view_first);

    secondTextureView = findViewById(R.id.texture_view_second);
  }

  public ViewGroup getRootViewGroup() {
    return rootViewGroup;
  }

  public SurfaceView getFirstSurfaceView() {
    return firstSurfaceView;
  }

  public SurfaceHolder getFirstSurfaceHolder() {
    return firstSurfaceHolder;
  }

  public SurfaceView getSecondSurfaceView() {
    return secondSurfaceView;
  }

  public SurfaceHolder getSecondSurfaceHolder() {
    return secondSurfaceHolder;
  }

  public TextureView getFirstTextureView() {
    return firstTextureView;
  }

  public TextureView getSecondTextureView() {
    return secondTextureView;
  }
}
