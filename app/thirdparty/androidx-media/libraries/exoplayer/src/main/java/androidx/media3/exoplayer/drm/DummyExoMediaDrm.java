/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.exoplayer.drm;

import android.media.MediaDrmException;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.decoder.CryptoConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** An {@link ExoMediaDrm} that does not support any protection schemes. */
@UnstableApi
public final class DummyExoMediaDrm implements ExoMediaDrm {

  /** Returns a new instance. */
  public static DummyExoMediaDrm getInstance() {
    return new DummyExoMediaDrm();
  }

  @Override
  public void setOnEventListener(@Nullable OnEventListener listener) {
    // Do nothing.
  }

  @Override
  public void setOnKeyStatusChangeListener(@Nullable OnKeyStatusChangeListener listener) {
    // Do nothing.
  }

  @Override
  public void setOnExpirationUpdateListener(@Nullable OnExpirationUpdateListener listener) {
    // Do nothing.
  }

  @Override
  public byte[] openSession() throws MediaDrmException {
    throw new MediaDrmException("Attempting to open a session using a dummy ExoMediaDrm.");
  }

  @Override
  public void closeSession(byte[] sessionId) {
    // Do nothing.
  }

  @Override
  public KeyRequest getKeyRequest(
      byte[] scope,
      @Nullable List<DrmInitData.SchemeData> schemeDatas,
      int keyType,
      @Nullable HashMap<String, String> optionalParameters) {
    // Should not be invoked. No session should exist.
    throw new IllegalStateException();
  }

  @Override
  @Nullable
  public byte[] provideKeyResponse(byte[] scope, byte[] response) {
    // Should not be invoked. No session should exist.
    throw new IllegalStateException();
  }

  @Override
  public ProvisionRequest getProvisionRequest() {
    // Should not be invoked. No provision should be required.
    throw new IllegalStateException();
  }

  @Override
  public void provideProvisionResponse(byte[] response) {
    // Should not be invoked. No provision should be required.
    throw new IllegalStateException();
  }

  @Override
  public Map<String, String> queryKeyStatus(byte[] sessionId) {
    // Should not be invoked. No session should exist.
    throw new IllegalStateException();
  }

  @Override
  public boolean requiresSecureDecoder(byte[] sessionId, String mimeType) {
    // Should not be invoked. No session should exist.
    throw new IllegalStateException();
  }

  @Override
  public void acquire() {
    // Do nothing.
  }

  @Override
  public void release() {
    // Do nothing.
  }

  @Override
  public void restoreKeys(byte[] sessionId, byte[] keySetId) {
    // Should not be invoked. No session should exist.
    throw new IllegalStateException();
  }

  @Override
  @Nullable
  public PersistableBundle getMetrics() {
    return null;
  }

  @Override
  public String getPropertyString(String propertyName) {
    return "";
  }

  @Override
  public byte[] getPropertyByteArray(String propertyName) {
    return Util.EMPTY_BYTE_ARRAY;
  }

  @Override
  public void setPropertyString(String propertyName, String value) {
    // Do nothing.
  }

  @Override
  public void setPropertyByteArray(String propertyName, byte[] value) {
    // Do nothing.
  }

  @Override
  public CryptoConfig createCryptoConfig(byte[] sessionId) {
    // Should not be invoked. No session should exist.
    throw new IllegalStateException();
  }

  @Override
  public @C.CryptoType int getCryptoType() {
    return C.CRYPTO_TYPE_UNSUPPORTED;
  }
}
