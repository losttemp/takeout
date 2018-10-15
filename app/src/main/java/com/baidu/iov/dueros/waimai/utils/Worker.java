package com.baidu.iov.dueros.waimai.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Worker {

    private static final String TAG = Worker.class.getSimpleName();

    private ExecutorService mService = Executors.newCachedThreadPool();

    private Worker() {}

    public static Worker getInstance() {
        return Holder.INSTANCE;
    }

    public Future<?> submit(Runnable task) {
        if (null != mService) {
            return mService.submit(task);
        }
        return null;
    }

    public <T> Future<T> submit(Callable<T> task) {
        if (null != mService) {
            return mService.submit(task);
        }
        return null;
    }

    public <T> Future<T> submit(Runnable task, T result) {
        if (null != mService) {
            return mService.submit(task, result);
        }
        return null;
    }

    public void execute(Runnable run) {
        if (null != mService) {
            mService.execute(run);
        }
    }

    public void shutdownAndAwaitTermination() {
        if (null != mService) {
            mService.shutdown();
            try {
                // Wait a while for existing tasks to terminate
                if (!mService.awaitTermination(60, TimeUnit.SECONDS))
                    mService.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!mService.awaitTermination(60, TimeUnit.SECONDS))
                    Lg.getInstance().d(TAG, "shutdownAndAwaitTermination Pool did not terminate");
            } catch (InterruptedException e) {
                // (Re-)Cancel if current thread also interrupted
                mService.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class Holder {
        private static final Worker INSTANCE = new Worker();
    }

}
