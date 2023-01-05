package org.rba.sample_server.handler.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionThreadHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        log.error(String.format("Exceção na thread %s.", thread.getName()));
        log.error("Exception Message: " + throwable.getMessage());
    }
}
