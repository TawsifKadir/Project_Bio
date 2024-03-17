package com.xplo.code.data.db.room.database;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseExecutors {
    private static final Object LOCK = new Object();
    private static DatabaseExecutors sInstance;

    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private DatabaseExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static DatabaseExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new DatabaseExecutors(
                        Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new BackgroundThreadExecutor()
                );
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkIO() {
        return networkIO;
    }

//    private static class MainThreadExecutor implements Executor {
//        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
//
//        @Override
//        public void execute(@NonNull Runnable command) {
//            mainThreadHandler.post(command);
//        }
//    }

    private static class BackgroundThreadExecutor implements Executor {
        private final Handler backgroundThreadHandler;

        public BackgroundThreadExecutor() {
            HandlerThread handlerThread = new HandlerThread("BackgroundThread");
            handlerThread.start();
            backgroundThreadHandler = new Handler(handlerThread.getLooper());
        }

        @Override
        public void execute(@NonNull Runnable command) {
            backgroundThreadHandler.post(command);
        }
    }

}
