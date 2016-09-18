package ua.artcode.server;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import ua.artcode.dto.*;

/**
 * Created by serhii on 9/17/16.
 */
public class Server {

    private ServerSocket serverSocket;
    private Gson gson;
    private WeatherApi weatherApi;

    public Server() {
        try {
            serverSocket = new ServerSocket(
                    Integer.parseInt(PropertiesHolder.get("server.port")));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("server did not start");
        }
        System.out.println("server started");

        gson = new Gson();
        weatherApi = new WeatherApiImpl();

    }

    public void runServer() {

        while (true) {
            System.out.println("Waiting for new connection");

            try {

                while (true){
                    Socket clientSocket = serverSocket.accept();

                    new Thread(new MyClientThread(clientSocket)).start();// asynch , fork
                    //createNewThread(clientSocket); //

                }



            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    private void createNewThread(Socket clientSocket) {
        PrintWriter outToClient = null;
        try {
            outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
            InputStream inputFromClient = clientSocket.getInputStream();
            BufferedReader bufferedReaderFromClient = new BufferedReader(new InputStreamReader(inputFromClient));

            runClientConnection(serverSocket, gson, weatherApi, clientSocket, outToClient, bufferedReaderFromClient);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private class MyClientThread implements Runnable {

        private Socket clientSocket;

        public MyClientThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            createNewThread(clientSocket);
        }
    }

    private void runClientConnection(ServerSocket serverSocket, Gson gson, WeatherApi weatherApi, Socket clientSocket, PrintWriter outToClient, BufferedReader bufferedReaderFromClient) throws IOException {
        try {

            String serverHostName = serverSocket.getInetAddress().toString();

            String serverMessage = gson.toJson(
                    new Message(serverHostName, "Hello message"));
            outToClient.println(serverMessage);

            while (true) {
                // send message


                //read message
                String jsonMessageFromClient = bufferedReaderFromClient.readLine();
                Message clientMessage = gson.fromJson(jsonMessageFromClient, Message.class);
                System.out.println(clientMessage);

                String message = clientMessage.getMessage().trim();
                if ("exit".equals(message)) {
                    System.out.println("End connection with the user " + clientSocket.getInetAddress());
                    break;
                } else if (message.contains("weather")) {
                    String city = message.substring(message.indexOf(' ') + 1);

                    String weatherInfo = weatherApi.get(city);

                    outToClient.println(gson.toJson(new Message(serverHostName, weatherInfo)));
                }

            }
        }catch (Throwable a){
            a.printStackTrace();
            System.out.println("Closed client connection due a error");
        }
    }
}
