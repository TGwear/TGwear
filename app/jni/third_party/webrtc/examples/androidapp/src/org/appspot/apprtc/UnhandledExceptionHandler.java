/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package org.appspot.apprtc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Singleton helper: install a default unhandled exception handler which shows
 * an informative dialog and kills the app.  Useful for apps whose
 * error-handling consists of throwing RuntimeExceptions.
 * NOTE: almost always more useful to
 * Thread.setDefaultUncaughtExceptionHandler() rather than
 * Thread.setUncaughtExceptionHandler(), to apply to background threads as well.
 */
public class UnhandledExceptionHandler implements Thread.UncaughtExceptionHandler {
  private static final String TAG = "AppRTCMobileActivity";
  private final Activity activity;

  public UnhandledExceptionHandler(final Activity activity) {
    this.activity = activity;
  }

  @Override
  public void uncaughtException(Thread unusedThread, final Throwable e) {
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        String title = "Fatal error: " + getTopLevelCauseMessage(e);
        String msg = getRecursiveStackTrace(e);
        TextView errorView = new TextView(activity);
        errorView.setText(msg);
        errorView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
        ScrollView scrollingContainer = new ScrollView(activity);
        scrollingContainer.addView(errorView);
        Log.e(TAG, title + "\n\n" + msg);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            System.exit(1);
          }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
            .setView(scrollingContainer)
            .setPositiveButton("Exit", listener)
            .show();
      }
    });
  }

  // Returns the Message attached to the original Cause of `t`.
  private static String getTopLevelCauseMessage(Throwable t) {
    Throwable topLevelCause = t;
    while (topLevelCause.getCause() != null) {
      topLevelCause = topLevelCause.getCause();
    }
    return topLevelCause.getMessage();
  }

  // Returns a human-readable String of the stacktrace in `t`, recursively
  // through all Causes that led to `t`.
  private static String getRecursiveStackTrace(Throwable t) {
    StringWriter writer = new StringWriter();
    t.printStackTrace(new PrintWriter(writer));
    return writer.toString();
  }
}
