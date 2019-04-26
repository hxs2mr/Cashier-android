package com.gykj.jxfvlibrary.manager;


import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程管理工具，用于处理一步任务
 * @author
 *
 */
public class FvThreadManager {


    private final int THREAD_FIXED = 3;
    private static FvThreadManager sInstance = null;
    private ExecutorService mExecutorService;

    private FvThreadManager() {
        this.mExecutorService = Executors.newFixedThreadPool(THREAD_FIXED);
    }

    public static synchronized FvThreadManager getInstance() {
        if (sInstance == null) {
            sInstance = new FvThreadManager();
        }
        return sInstance;
    }

    public void submit(Runnable task) {
        this.mExecutorService.submit(task);
    }

    public void submitListRunnable(List<Runnable> taskList){
        for (Runnable runnable : taskList){
            submit(runnable);
        }
    }

    public Future<Integer> submit(Callable<Integer> task) {
        return this.mExecutorService.submit(task);
    }

    public void shutdown() {
        if (!this.mExecutorService.isShutdown())
            this.mExecutorService.shutdownNow();
    }
}