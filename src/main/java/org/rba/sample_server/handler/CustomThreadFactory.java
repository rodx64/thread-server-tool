package org.rba.sample_server.handler;

import org.rba.sample_server.handler.exceptions.ExceptionThreadHandler;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadFactory implements ThreadFactory {
    private static final AtomicInteger count = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread =  new Thread(runnable, "Thread-TaskServer" + count);
        thread.setUncaughtExceptionHandler(new ExceptionThreadHandler());
        count.getAndAdd(1);
        return thread;
    }
}
