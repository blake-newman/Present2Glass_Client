package present2glass.controllers;

import present2glass.Main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Server{
    public static ServerSocket serverSocket;
    public Boolean connected = false;
    public int timeout = 0;
    private Timer connectionTimer = new Timer();
    public String outNote = null;
    public Long outTime = Long.parseLong("-1");
    private Boolean outStart = false;
    private Boolean outStop = false;

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

            connectionTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    timeout++;
                    if (timeout == 2) {
                        connected = false;
                    }
                }
            }, 0, 500);
        } catch (IOException e) {
            System.out.println("ERROR - NOT ABLE TO START SERVER ANOTHER CLIENT IS OPEN");
            System.exit(0);
        }
    }

    private void addConnection(){
        Connection thread = new Connection();
        thread.start();
    }


    public void startPresentation() {
        outStart = true;
    }

    public void stopPresentation() {
        outStop = false;
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
                connected = true;
                timeout = 0;
                int code = in.readInt();
                switch(code) {
                    case (0):
                        if (outStart) {
                            out.writeInt(1);
                            outStart = false;
                        } else if (outStop) {
                            out.writeInt(2);
                            outStop = false;
                            outNote = null;
                            outTime = Long.parseLong("-1");
                        } else if (outNote != null && outTime != Long.parseLong("-1")){
                            out.writeInt(3);
                            out.writeUTF(outNote);
                            out.writeLong(outTime);
                            outNote = null;
                            outTime = Long.parseLong("-1");
                        }  else {
                            out.writeInt(999);
                        }
                    case (1):
                        Main.presenter.simulateStart();
                        break;
                    case (2):
                        if (Main.presenter.isPresenting) {
                            Main.presenter.simulateStop();
                        }
                        break;
                    case (3):
                        Main.presenter.simulateNext();
                        break;
                    case (4):
                        Main.presenter.simulateNextSlide();
                        break;
                    case (5):
                        Main.presenter.simulatePrevious();
                        break;
                    case (6):
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