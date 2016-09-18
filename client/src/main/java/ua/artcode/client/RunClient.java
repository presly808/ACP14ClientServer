package ua.artcode.client;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import ua.artcode.dto.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by serhii on 9/17/16.
 */
public class RunClient {

    public static void main(String[] args) {
        final Gson gson = new Gson();

        try (Socket socket = new Socket("localhost", 8888);
             PrintWriter pw = new PrintWriter(socket.getOutputStream())) {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            while(true){
                String jsonServerMessage = bufferedReader.readLine();
                Message serverMessage = gson.fromJson(jsonServerMessage,Message.class);

                System.out.println("server Message");
                System.out.println(serverMessage);

                // String(Json) -> object Message


                System.out.println("input message");
                // get message from the console
                String message = new Scanner(System.in).nextLine();

                String responseFromServer = gson.toJson(new Message("client", message));
                pw.println(responseFromServer);
                pw.flush();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
