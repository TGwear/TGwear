/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package org.thunderdog.challegram.voip;

import android.os.SystemClock;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.media3.common.C;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.decoder.ffmpeg.FfmpegLibrary;
import androidx.media3.decoder.flac.FlacLibrary;
import androidx.media3.decoder.opus.OpusLibrary;
import androidx.media3.decoder.vp9.VpxLibrary;

import com.getkeepsafe.relinker.ReLinker;
import com.getkeepsafe.relinker.ReLinkerInstance;
import com.gohj99.tgwear.Application;

import java.util.ArrayList;


public class NLoader implements ReLinker.Logger {

    private static volatile boolean loaded;

    private static volatile NLoader instance;

    public static NLoader instance() {
        if (instance == null) {
            synchronized (NLoader.class) {
                if (instance == null) {
                    instance = new NLoader();
                }
            }
        }
        return instance;
    }

    private static void loadLibraryImpl(ReLinkerInstance reLinker, String library, @Nullable String version) {
        long ms = SystemClock.uptimeMillis();
        reLinker.loadLibrary(Application.Companion.getApplication(), library, version);
        android.util.Log.v("tgx", "Loaded " + library + " in " + (SystemClock.uptimeMillis() - ms) + "ms");
    }

    @OptIn(markerClass = UnstableApi.class)
    public static synchronized boolean loadLibrary() {
        if (!loaded) {
            try {
                ReLinkerInstance reLinker = ReLinker.recursively().log(NLoader.instance());
                loadLibraryImpl(reLinker, "c++_shared", BuildConfig.NDK_VERSION);
                loadLibraryImpl(reLinker, "cryptox", BuildConfig.OPENSSL_VERSION_FULL);
                loadLibraryImpl(reLinker, "sslx", BuildConfig.OPENSSL_VERSION_FULL);
                loadLibraryImpl(reLinker, "tdjni", BuildConfig.TDLIB_VERSION);
                loadLibraryImpl(reLinker, "leveldbjni", BuildConfig.LEVELDB_VERSION);
                loadLibraryImpl(reLinker, "tgcallsjni", BuildConfig.JNI_VERSION /*TODO: separate variable?*/);
                loadLibraryImpl(reLinker, "tgxjni", BuildConfig.JNI_VERSION);
                OpusLibrary.setLibraries(C.CRYPTO_TYPE_UNSUPPORTED);
                VpxLibrary.setLibraries(C.CRYPTO_TYPE_UNSUPPORTED);
                FlacLibrary.setLibraries();
                FfmpegLibrary.setLibraries();
          /*if (BuildConfig.DEBUG) {
            android.util.Log.v("tgx", String.format(Locale.US,
              "leveldb %s, libopus %s, libvpx %s, ffmpeg %s, tgvoip %s, tgcalls %s",
              LevelDB.getVersion(),
              OpusLibrary.getVersion(),
              VpxLibrary.getVersion(),
              FfmpegLibrary.getVersion(),
              VoIPController.getVersion(),
              TextUtils.join("+", N.getTgCallsVersions())
            ));
            VideoCodecInfo[] softwareVideoCodecs = new SoftwareVideoEncoderFactory().getSupportedCodecs();
          }*/
            } catch (Throwable t) {
                RuntimeException e = new IllegalStateException(instance().collectLog() + "\n" + t.getMessage(), t);
                e.setStackTrace(t.getStackTrace());
                throw e;
            }
            loaded = true;
        }
        return loaded;
    }

    private ArrayList<String> messages;

    @Override
    public void log(String message) {
        synchronized (this) {
            if (messages == null) {
                messages = new ArrayList<>();
            }
            messages.add(message);
        }
    }

    public @Nullable String collectLog() {
        String log = null;
        synchronized (this) {
            if (messages != null && !messages.isEmpty()) {
                log = "==== ReLinker ====\n" + TextUtils.join("\n", messages) + "\n==== ReLinker END ====\n";
            }
            messages = null;
        }
        return log;
    }
}
