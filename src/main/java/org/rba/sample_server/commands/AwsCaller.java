package org.rba.sample_server.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;
import java.util.concurrent.Callable;

@Slf4j
@RequiredArgsConstructor
public class AwsCaller implements Callable<String> {

    private final PrintStream printStream;

    @Override
    public String call() throws Exception {
        log.info("Servidor recebeu chamada Aws");
        printStream.println("Acessando ambiente Aws...");
        Thread.sleep(10000);
        return "resAws ";
    }
}
