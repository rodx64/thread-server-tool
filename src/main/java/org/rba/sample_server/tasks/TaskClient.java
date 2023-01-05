package org.rba.sample_server.tasks;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

@Slf4j
public class TaskClient {

    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) throws Exception {
        try(Socket socket = new Socket(HOST, PORT)){
            Thread threadSent = new Thread(() -> {
                try {
                    log.info("Estabelecendo conexão...");
                    log.info("Conexão estabelecida com o servidor.");
                    PrintStream imprimeSaida = new PrintStream(socket.getOutputStream());
                    Scanner entradaTeclado = new Scanner(System.in);

                    while (entradaTeclado.hasNextLine()){
                        String linha = entradaTeclado.nextLine();
                        if(linha.trim().equals("")) {
                            break;
                        }
                        imprimeSaida.println(linha);
                    }

                    imprimeSaida.close();
                    entradaTeclado.close();
                } catch (Exception e) {
                    log.debug(e.getMessage());
                }
            }, "Thread-Envia");

            Thread threadResponse = new Thread(() -> {
                try{
                    Scanner response = new Scanner(socket.getInputStream());
                    log.info("Entre com os dados: ");

                    while(response.hasNextLine()){
                        String line = response.nextLine();
                        log.info(line);
                    }
                    response.close();
                } catch (Exception e) {
                    log.debug(e.getMessage());
                }
            }, "Thread-Response");

        threadResponse.start();
        threadSent.start();
        threadSent.join();
        log.info("Socket fechado.");
        }
    }
}
