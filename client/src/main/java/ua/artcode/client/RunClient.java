package ua.artcode.client;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by serhii on 9/17/16.
 */
public class RunClient {

    public static void main(String[] args) {


        try (Socket socket = new Socket("localhost", 8888);
             PrintWriter pw = new PrintWriter(socket.getOutputStream())) {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String serverMessage = bufferedReader.readLine();
            // String(Json) -> object Message

            System.out.println("server message " + serverMessage);

            System.out.println("input message");
            // get message from the console
            String message = new Scanner(System.in).nextLine();

            pw.println(message);
            pw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
