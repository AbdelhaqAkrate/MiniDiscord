package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class User {
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String username;
    public User(Socket socket, String username)
    {
        try{
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;

        }catch (IOException e)
        {
            shutDown(socket, br, bw);
        }
    }
    private void shutDown(Socket socket, BufferedReader br, BufferedWriter bw) {

        try{
            if(br != null)
            {
                br.close();
            }
            if(bw != null)
            {
                bw.close();
            }
            if(socket != null)
            {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage()
    {
        try{
            bw.write(username);
            bw.newLine();
            bw.flush();
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected())
            {
                String message = scanner.nextLine();
                bw.write(username + " : " + message);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            shutDown(socket,br, bw);
        }
    }

    public void serverMessages()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String serverMssgs;
                while(socket.isConnected()) {
                    try {
                        serverMssgs = br.readLine();
                        System.out.println(serverMssgs);
                    } catch (IOException e) {
                        shutDown(socket,br, bw);
                    }
                }
            }
        }).start();

    }
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Your Username : ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1999);
        User user = new User(socket,username);
        user.serverMessages();
        user.sendMessage();
    }

}
