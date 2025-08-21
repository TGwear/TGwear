/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.session;

import android.support.v4.media.session.MediaSessionCompat;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/** TestRule for releasing {@link MediaSession} instances after use. */
public class MediaSessionTestRule implements TestRule {
  private final List<MediaSession> sessions;
  private final List<MediaSessionCompat> sessionCompats;

  MediaSessionTestRule() {
    sessions = new CopyOnWriteArrayList<>();
    sessionCompats = new CopyOnWriteArrayList<>();
  }

  @Override
  public Statement apply(Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        try {
          base.evaluate();
        } finally {
          cleanUpSessions();
        }
      }
    };
  }

  /** Ensures that release() is called after the test. */
  public <T extends MediaSession> T ensureReleaseAfterTest(T session) {
    sessions.add(session);
    return session;
  }

  /** Ensures that release() is called after the test. */
  public MediaSessionCompat ensureReleaseAfterTest(MediaSessionCompat session) {
    sessionCompats.add(session);
    return session;
  }

  private void cleanUpSessions() {
    for (int i = 0; i < sessions.size(); i++) {
      sessions.get(i).release();
    }
    sessions.clear();

    for (int i = 0; i < sessionCompats.size(); i++) {
      sessionCompats.get(i).release();
    }
    sessionCompats.clear();
  }
}
