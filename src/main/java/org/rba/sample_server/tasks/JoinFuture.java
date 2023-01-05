package org.rba.sample_server.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@RequiredArgsConstructor
@Slf4j
public class JoinFuture implements Runnable {

    private final List<Future<String>> futures;
    private final PrintStream printStream;

    @Override
    public void run() {
        String strRes = futures.stream()
                .map(future -> {
                    String res = "";
                    try {
                        res = future.get(15, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error("Timeout aws..");
                        printStream.println("Timeout ao tentar acessar a aws.");
                    }
                    return res;
                })
                .filter(Objects::nonNull)
                .reduce("Result: ", String::concat)
                ;
        printStream.println(strRes);
    }
}
