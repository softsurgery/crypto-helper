package com.zc.cryptohelper.crypto_helper.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class AsyncTaskService {
    private Future<?> currentTask;
    private final AtomicBoolean isCancelled = new AtomicBoolean(false);

    @Async
    public Future<Boolean> runScrapingTask(Runnable task) {
        isCancelled.set(false);
        FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
            try {
                task.run();
            } catch (Exception e) {
                // Handle exception
            }
            return true;
        });
        currentTask = futureTask;
        new Thread(futureTask).start();
        return futureTask;
    }

    public void cancelTask() {
        if (currentTask != null) {
            isCancelled.set(true);
            currentTask.cancel(true);
        }
    }

    public boolean isCancelled() {
        return isCancelled.get();
    }
}