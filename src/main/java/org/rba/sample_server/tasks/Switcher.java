package org.rba.sample_server.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rba.sample_server.commands.AwsCaller;
import org.rba.sample_server.commands.BdCaller;
import org.rba.sample_server.commands.BucketCaller;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
public class Switcher implements Runnable{

    private final Socket socket;
    private final TaskServer servidorTarefas;
    private final ExecutorService threadPoolCached;
    private final BlockingQueue<String> commandQueue;

    @Override
    public void run() {
        try{
            InputStream inputStream = socket.getInputStream();
            Scanner fromClient = new Scanner(inputStream);
            PrintStream toClient = new PrintStream(socket.getOutputStream());

            log.info("Distribuindo tarefas para: \n -> " + socket);

            while(fromClient.hasNextLine()){
                String input = fromClient.nextLine();
                final String logMessage = String.format("Comando '%s' recebido.", input);
                final String sentMessage = String.format("Confirmação de comando %s.", input);
                switch (input.trim().toLowerCase()){
                    case "s3": {
                        log.info(logMessage);
                        toClient.println(sentMessage);
                        commandQueue.put(input.trim().toLowerCase());
                        List<Future<String>> futureList = Arrays.asList(
                                threadPoolCached.submit(new AwsCaller(toClient)),
                                threadPoolCached.submit(new BucketCaller(toClient)));
                        threadPoolCached.execute(new JoinFuture(futureList, toClient));
                        break;
                    }
                    case "rds": {
                        log.info(logMessage);
                        toClient.println(sentMessage);
                        commandQueue.put(input.trim().toLowerCase());
                        List<Future<String>> futureList = Arrays.asList(
                                threadPoolCached.submit(new AwsCaller(toClient)),
                                threadPoolCached.submit(new BdCaller(toClient)));
                        threadPoolCached.execute(new JoinFuture(futureList, toClient));
                        break;
                    }
                    case "exit": {
                        log.info("Cliente saiu.");
                        toClient.println("Desconectado do servidor...");
                        servidorTarefas.stop();
                        break;
                    }
                    default: {
                        log.info(logMessage + " [desconhecido].");
                        toClient.println(sentMessage + " [desconhecido].");
                    }
                }
            }
            toClient.close();
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
