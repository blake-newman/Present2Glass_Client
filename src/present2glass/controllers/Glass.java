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
                    Nav.ip.setDisable(true);
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

    /*public void stopPresentation(){
        if(!isConnected) return;
        new Thread(new Runnable() {
            int dropped = 0;
            @Override
            public void run() {
                try{
                    Socket socket = createSocket();
                    DataOutputStream out = createOut(socket);
                    out.writeInt(2);
                    closeSocket(socket, null, out);
                } catch (IOException ignore){
                    if(dropped++ < 20){
                        run();
                    }
                }
            }
        }).start();
    }

    public void endConnection(){
        if(!isConnected) return;
        new Thread(new Runnable() {
            int dropped = 0;
            @Override
            public void run() {
                try{
                    Socket socket = createSocket();
                    DataOutputStream out = createOut(socket);
                    out.writeInt(4);
                    closeSocket(socket, null, out);
                    System.exit(0);
                } catch (IOException ignore){
                    if(dropped++ < 20){
                        run();
                    } else {
                        System.exit(0);
                    }
                }
            }
        }).start();
    }

    public void startPresentation() {
        if(!isConnected) return;
        new Thread(new Runnable() {
            int dropped = 0;
            @Override
            public void run() {
                try{
                    Socket socket = createSocket();
                    DataOutputStream out = createOut(socket);
                    socket.setSoTimeout(1);
                    out.writeInt(1);
                    closeSocket(socket, null, out);
                } catch (IOException ignore){
                    if(dropped++ < 20){
                        run();
                    }
                }
            }
        }).start();
    }


    public void sendData(final String note, final Boolean stream, final Long time) {
        new Thread(new Runnable() {

            int dropped = 0;
            @Override
            public void run() {
                try{
                    Socket socket = createSocket();
                    DataOutputStream out = createOut(socket);
                    socket.setSoTimeout(1);
                    out.writeInt(3);
                    out.writeUTF(note);
                    out.writeBoolean(stream);
                    out.writeLong(time);
                    closeSocket(socket, null, out);
                } catch (IOException ignore){
                    if(dropped++ < 20){
                        run();
                    }
                }
            }
        }).start();
    }*/
}