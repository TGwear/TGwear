/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package org.thunderdog.challegram.voip;

import android.os.SystemClock;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import org.drinkless.tdlib.TdApi;

import org.thunderdog.challegram.voip.annotation.CallNetworkType;
import org.thunderdog.challegram.voip.annotation.CallState;

public abstract class VoIPInstance implements Destroyable {
    /*protected final Tdlib tdlib;*/
    protected final TdApi.Call call;
    protected final CallConfiguration configuration;
    protected final CallOptions options;
    protected final @NonNull ConnectionStateListener connectionStateListener;

    public VoIPInstance(/*@NonNull Tdlib tdlib,*/
            @NonNull TdApi.Call call,
            @NonNull CallConfiguration configuration,
            @NonNull CallOptions options,
            @NonNull ConnectionStateListener stateListener) {
        /*this.tdlib = tdlib;*/
        this.call = call;
        this.configuration = configuration;
        this.options = options;
        this.connectionStateListener = stateListener;
    }

    public abstract void initializeAndConnect();

    // Getters

  /*public final Tdlib tdlib () {
    return tdlib;
  }*/

    public final @NonNull TdApi.Call getCall() {
        return call;
    }

    public final @NonNull CallConfiguration getConfiguration() {
        return configuration;
    }

    public final @NonNull CallOptions getOptions() {
        return options;
    }

    // Connection state

    private long callStartTime;

    protected final void dispatchCallStateChanged(@CallState int state) {
        // this.callState = state;
        if (state == CallState.ESTABLISHED && callStartTime == 0) {
            callStartTime = SystemClock.elapsedRealtime();
        }
        connectionStateListener.onConnectionStateChanged(this, state);
    }

    public static long DURATION_UNKNOWN = -1;

    public final long getCallDuration() {
        return callStartTime != 0 ? SystemClock.elapsedRealtime() - callStartTime : DURATION_UNKNOWN;
    }

    // Setters

    public final void setAudioOutputGainControlEnabled(boolean isEnabled) {
        options.audioGainControlEnabled = isEnabled;
        handleAudioOutputGainControlEnabled(isEnabled);
    }

    protected abstract void handleAudioOutputGainControlEnabled(boolean isEnabled);

    public final void setEchoCancellationStrength(int strength) {
        options.echoCancellationStrength = strength;
        handleEchoCancellationStrengthChange(strength);
    }

    protected abstract void handleEchoCancellationStrengthChange(int strength);

    public final void setMicDisabled(boolean isDisabled) {
        options.isMicDisabled = isDisabled;
        handleMicDisabled(isDisabled);
    }

    protected abstract void handleMicDisabled(boolean isDisabled);

    public void setNetworkType(@CallNetworkType int type) {
        options.networkType = type;
        handleNetworkTypeChange(type);
    }

    protected abstract void handleNetworkTypeChange(@CallNetworkType int type);

    // Getters

    public abstract CharSequence collectDebugLog();

    public abstract long getConnectionId();

    public abstract void getNetworkStats(NetworkStats out);

    public abstract String getLibraryName();

    public abstract String getLibraryVersion();

    // called from native code

    @Keep
    protected final void handleStateChange(@CallState int state) {
        dispatchCallStateChanged(state);
    }

    @Keep
    protected final void handleSignalBarsChange(int count) {
        connectionStateListener.onSignalBarCountChanged(count);
    }

    @Keep
    protected final void handleEmittedSignalingData(byte[] buffer) {
        connectionStateListener.onSignallingDataEmitted(buffer);
    }

    // called from TDLib

    public abstract void handleIncomingSignalingData(byte[] buffer);
}
