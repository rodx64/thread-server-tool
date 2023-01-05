package org.rba.sample_server.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

@Slf4j
@RequiredArgsConstructor
public class Consumer implements Runnable{

    private final BlockingQueue<String> commandQueue;

    @Override
    public void run() {
        try{
            String command = null;
            while ((command = commandQueue.take()) != null){
                log.info("Comando consumido: " + command + " thread " + Thread.currentThread().getName());
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
