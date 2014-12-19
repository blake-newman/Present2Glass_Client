package present2glass.controllers;

import javafx.application.Platform;
import present2glass.Main;
import present2glass.components.Nav;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Glass {

    public Boolean isConnected = false;

    public Glass(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Nav.ip.setDisable(false);
                    }
                });
            }
        });
        checkConnection();
    }

    private void checkConnection(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) {
                }

                try{
                    Socket socket = createSocket();
                    closeSocket(socket, null, null);
                    isConnected = true;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Nav.ip.setDisable(true);
                        }
                    });
                } catch (IOException e){
                    isConnected = false;
                    run();
                }
            }

        }).start();
    }

    private Socket createSocket() throws IOException {
        String ip = Main.nav.getIP();
        ip = Net.validateIP(ip) ? ip : "255.255.255.0";
        Socket socket = new Socket();
        socket.setSoLinger(false, 0);
        socket.setReuseAddress(true);
        socket.setPerformancePreferences(1, 0, 0);
        socket.connect(new InetSocketAddress(ip, 7628), 500);
        return socket;
    }

    private DataOutputStream createOut(Socket socket) throws IOException {
        return new DataOutputStream(socket.getOutputStream());
    }

    private void closeSocket(Socket socket, DataInputStream in, DataOutputStream out) throws IOException{
        if(socket != null && !socket.isClosed()){
            if(out != null){
                out.flush();
                out.close();
            }
            if(in != null){
                 in.close();
            }
            socket.close();
        } else {
            throw new IOException("SOCKET NOT EXISTS");
        }
    }
}