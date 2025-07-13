/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package org.thunderdog.challegram.voip;

import androidx.annotation.Nullable;

import org.thunderdog.challegram.voip.NetworkStats;
import org.thunderdog.challegram.voip.VoIPInstance;
import org.thunderdog.challegram.voip.annotation.AudioState;
import org.thunderdog.challegram.voip.annotation.CallState;
import org.thunderdog.challegram.voip.annotation.VideoState;

public interface ConnectionStateListener {
    default void onConnectionStateChanged(VoIPInstance context, @CallState int newState) {
    }

    default void onSignalBarCountChanged(int newCount) {
    }

    default void onStopped(VoIPInstance releasedContext, NetworkStats finalStats, @Nullable String debugLog) {
    }

    default void onRemoteMediaStateChanged(VoIPInstance context, @AudioState int audioState, @VideoState int videoState) {
    }

    default void onSignallingDataEmitted(byte[] data) {
    }

    default void onGroupCallKeyReceived(byte[] key) {
    }

    default void onGroupCallKeySent() {
    }

    default void onCallUpgradeRequestReceived() {
    }
}
