/*
 * Copyright (c) 2025 gohj99. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package org.thunderdog.challegram.voip;

import static cn.spacexc.neogram.utils.LogUtilsKt.getLogDir;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import cn.spacexc.neogram.Application;

public class VoIPLogs {
    private static final int KEEP_COUNT = 6;

    public static class Pair {
        public final File logFile;
        public final File statsLogFile;

        public Pair(File logFile, File statsLogFile) {
            this.logFile = logFile;
            this.statsLogFile = statsLogFile;
        }

        public boolean exists() {
            return hasPrimaryLogFile() || hasStatsLogFile();
        }

        public boolean hasPrimaryLogFile() {
            return (logFile.exists() && logFile.length() > 0);
        }

        public boolean hasStatsLogFile() {
            return (statsLogFile.exists() && statsLogFile.length() > 0);
        }
    }


    @Nullable
    public static Pair getNewFile(boolean cleanup) {
        Calendar c = Calendar.getInstance();
        File dir = getLogDir();
        if (cleanup) {
            deleteOldCallLogFiles(dir, KEEP_COUNT);
        }
        File[] files = new File[2];
        for (int i = 0; i < files.length; i++) {
            String logFileName = String.format(Locale.US,
                    "%s%02d_%02d_%04d_%02d_%02d_%02d%s.log",
                    "TGVOIP",
                    c.get(Calendar.DATE),
                    c.get(Calendar.MONTH) + 1,
                    c.get(Calendar.YEAR),
                    c.get(Calendar.HOUR_OF_DAY),
                    c.get(Calendar.MINUTE),
                    c.get(Calendar.SECOND),
                    i == 1 ? ".stats" : ""
            );
            File file = new File(dir, logFileName);
      /*try {
        if (!file.createNewFile()) {
          return null;
        }
      } catch (Throwable t) {
        Log.e(Log.TAG_VOIP, "Unable to create call log file", t);
        return null;
      }*/
            files[i] = file;
        }
        return new Pair(files[0], files[1]);
    }

    public static boolean deleteAllCallLogFiles() {
        return deleteOldCallLogFiles(getLogDir(), 0);
    }

    private static boolean deleteOldCallLogFiles(File dir, int keepCount) {
        if (dir == null) {
            return false;
        }
        File[] callLogs = dir.listFiles((dir1, name) ->
                name.startsWith("TGVOIP") && name.endsWith(".log")
        );
        if (callLogs == null || callLogs.length == 0) {
            return true;
        }
        Arrays.sort(callLogs, (a, b) ->
                Long.compare(a.lastModified(), b.lastModified())
        );
        boolean success = true;
        for (int i = 0; i < Math.max(0, callLogs.length - keepCount); i++) {
            File file = callLogs[i];
            if (!file.delete()) {
                success = false;
            }
        }
        return success;
    }
}
