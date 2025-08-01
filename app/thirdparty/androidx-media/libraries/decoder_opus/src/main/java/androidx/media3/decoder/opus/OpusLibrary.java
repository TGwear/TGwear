/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package androidx.media3.decoder.opus;

import androidx.annotation.Nullable;
import androidx.media3.common.C;
import androidx.media3.common.MediaLibraryInfo;
import androidx.media3.common.util.LibraryLoader;
import androidx.media3.common.util.UnstableApi;

/**
 * Configures and queries the underlying native library.
 */
@UnstableApi
public final class OpusLibrary {

    static {
        MediaLibraryInfo.registerModule("media3.decoder.opus");
    }

    private static final LibraryLoader LOADER =
            new LibraryLoader("opusV2JNI") {
                @Override
                protected void loadLibrary(String name) {
                    System.loadLibrary(name);
                }
            };

    private static @C.CryptoType int cryptoType = C.CRYPTO_TYPE_UNSUPPORTED;

    private OpusLibrary() {
    }

    /**
     * Override the names of the Opus native libraries. If an application wishes to call this method,
     * it must do so before calling any other method defined by this class, and before instantiating a
     * {@link LibopusAudioRenderer} instance.
     *
     * @param cryptoType The {@link C.CryptoType} for which the decoder library supports decrypting
     *                   protected content, or {@link C#CRYPTO_TYPE_UNSUPPORTED} if the library does not support
     *                   decryption.
     * @param libraries  The names of the Opus native libraries.
     */
    public static void setLibraries(@C.CryptoType int cryptoType, String... libraries) {
        OpusLibrary.cryptoType = cryptoType;
        LOADER.setLibraries(libraries);
    }

    /**
     * Returns whether the underlying library is available, loading it if necessary.
     */
    public static boolean isAvailable() {
        return LOADER.isAvailable();
    }

    /**
     * Returns the version of the underlying library if available, or null otherwise.
     */
    @Nullable
    public static String getVersion() {
        return isAvailable() ? opusGetVersion() : null;
    }

    /**
     * Returns whether the library supports the given {@link C.CryptoType}.
     */
    public static boolean supportsCryptoType(@C.CryptoType int cryptoType) {
        return cryptoType == C.CRYPTO_TYPE_NONE
                || (cryptoType != C.CRYPTO_TYPE_UNSUPPORTED && cryptoType == OpusLibrary.cryptoType);
    }

    public static native String opusGetVersion();

    public static native boolean opusIsSecureDecodeSupported();
}
