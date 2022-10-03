package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;

public class Client implements Runnable{
    private static ArrayList<Client> connectedClients = new ArrayList<>();
    private String username;
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;

    public Client(Socket socket)
    {
        try{
            this.socket = socket;
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = br.readLine();
            connectedClients.add(this);
            messageBox("A Wild " + this.username + " Appeared .");
        } catch (IOException e) {
            shutDown(socket, br, bw);
        }
    }
    public void removeClient()
    {
        connectedClients.remove(this);
        messageBox("JoyBoy : " + username + " Has Escaped From The Server !" );
    }

    private void shutDown(Socket socket, BufferedReader br, BufferedWriter bw) {
        removeClient();
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

    private void messageBox(String s) {
        for (Client client : connectedClients)
        {
            try{
                if(!client.username.equals(username))
                {
                    client.bw.write(s);
                    client.bw.newLine();
                    client.bw.flush();
                }
            } catch (IOException e) {
                shutDown(socket, br, bw);
            }
        }
    }

    @Override
    public void run() {
    String  userMessage;
    while (socket.isConnected())
    {
        try{
            userMessage = br.readLine();
            messageBox(userMessage);
        } catch (IOException e)
        {
            shutDown(socket, br, bw);
            break;
        }
    }
    }
}
