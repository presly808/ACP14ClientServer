package ua.artcode.server;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import ua.artcode.dto.*;

/**
 * Created by serhii on 9/17/16.
 */
public class RunServer {

    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("server did not start");
        }
        System.out.println("server started");

        Gson gson = new Gson();

        while (true) {
            System.out.println("Waiting for new connection");

            try (Socket socket = serverSocket.accept();
                PrintWriter pw = new PrintWriter(socket.getOutputStream(),true)) {

                //String serverMessage = gson.toJson(new Message("server", "Hello from server"));
                pw.println("message");

                String messageFromClient = IOUtils.toString(socket.getInputStream());
                System.out.println("client message " + messageFromClient);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
