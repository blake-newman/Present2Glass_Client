package present2glass.controllers;

import javafx.application.Platform;
import present2glass.Main;
import present2glass.components.Nav;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{
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
        if(serverSocket != null) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                        Main.presenter.simulateStart();
                        break;
                    case (2):
                        if (Main.presenter.isPresenting) {
                            Main.presenter.simulateStop();
                        }
                        break;
                    case (3):
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        Nav.ip.setText("");
                                    }
                                });
                            }
                        });
                        Main.glass = new Glass();
                        break;
                    case (4):
                        Main.presenter.simulateNext();
                        break;
                    case (5):
                        Main.presenter.simulateNextSlide();
                        break;
                    case (6):
                        Main.presenter.simulatePrevious();
                        break;
                    case (7):
                        Main.presenter.simulatePreviousSlide();
                        break;
                }
            } catch (IOException ignore) {
            } finally {
                if(serverSocket != null) {
                    if (socket != null && !socket.isClosed()) {
                        try {
                            if (in != null) {
                                in.close();
                            }
                            if (out != null) {
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
}