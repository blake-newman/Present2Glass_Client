package presenttoglass.controllers;

import presenttoglass.Main;
import presenttoglass.components.Nav;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{
    private int maxConnections = 50;
    public static ServerSocket serverSocket;


    public Server(){
        if(serverSocket != null) return;
        try {
            serverSocket = new ServerSocket(7629);
            serverSocket.setPerformancePreferences(1,0,0);
            serverSocket.setReuseAddress(true);
            int num = 0;
            while(num++ < 50){
                addConnection();
            }
        } catch (IOException e) {
            System.out.println("ERROR - NOT ABLE TO START SERVER ANOTHER CLIENT IS OPEN");
            System.exit(0);
        }
    }

    private void addConnection(){
        Connection thread = new Connection();
        thread.start();
    }

    public void destroy(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Connection extends Thread{
        public Socket socket;
        public DataOutputStream out;
        public DataInputStream in;

        @SuppressWarnings("InfiniteRecursion")
        public void run(){
            try {
                socket = serverSocket.accept();
                socket.setReuseAddress(true);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                int code = in.readInt();
                switch(code) {
                    case (1):
                        Main.presenter.startPresentation();
                        break;
                    case (2):
                        if (Main.presenter.isPresenting) {
                            Main.presenter.stopPresentation();
                        }
                        Main.glass = new Glass();
                        break;
                    case (3):
                        break;
                    case (4):
                        break;
                    case (6):
                        break;
                    case (7):
                        break;
                }
            } catch (IOException ignore) {
            } finally {
                if(socket != null && !socket.isClosed()){
                    try {
                        if(in != null){
                            in.close();
                        }
                        if(out != null){
                            out.close();
                        }
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                run();
            }
        }

    }
}