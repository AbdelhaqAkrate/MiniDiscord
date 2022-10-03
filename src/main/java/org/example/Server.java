package org.example;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void lanceServer() throws IOException {
        try{
            while (!serverSocket.isClosed())
            {
                Socket mySocket = serverSocket.accept();
                Client client = new Client(mySocket);
                System.out.println("New User Hopped Into The Server. !");
                Thread thread = new Thread(client);
                thread.start();
            }
        }catch (IOException e){
            if(serverSocket != null)
            {
                serverSocket.close();
            }
            else {
                e.printStackTrace();
            }
        }
    }
    public static void main (String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1999);
        Server server = new Server(serverSocket);
        server.lanceServer();
    }
}

