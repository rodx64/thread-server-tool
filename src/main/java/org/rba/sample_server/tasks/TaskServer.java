package org.rba.sample_server.tasks;

import lombok.extern.slf4j.Slf4j;
import org.rba.sample_server.handler.CustomThreadFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.Executors.newCachedThreadPool;

@Slf4j
@Configuration
public class TaskServer {

    private final ExecutorService threadPoolCached;
    private final ServerSocket serverSocket;
    private final AtomicBoolean isRunning;
    private final BlockingQueue<String> commandQueue;
    private final int maxConsumers = 10;

    public TaskServer() throws IOException {
        log.info("-- INICIANDO SERVIDOR --");
        final int port = 12345;
        this.threadPoolCached = newCachedThreadPool(new CustomThreadFactory());
        this.serverSocket = new ServerSocket(port);
        this.isRunning = new AtomicBoolean(true);
        this.commandQueue = new ArrayBlockingQueue<>(maxConsumers);
        startConsumer();
    }

    private void startConsumer() {
        for (int i = 0; i < maxConsumers; i++) {
            this.threadPoolCached.execute(new Consumer(commandQueue));
        }
    }

    public void exec(){
        while (isRunning.get()){
            try{
                Socket socket = serverSocket.accept();
                log.info("Recebendo novo cliente na porta " + socket.getPort());
                threadPoolCached.execute(new Switcher(socket, this, threadPoolCached, commandQueue));
            } catch (IOException e){
                log.debug(e.getMessage());
            }
        }
    }

    public void stop() {
        try {
            isRunning.set(false);
            threadPoolCached.shutdown();
            serverSocket.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
