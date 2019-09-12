package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class ReadThread extends Thread {
    static Logger logging = Logger.getLogger(ReadThread.class.getName());

    private Socket socket;

    public ReadThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run(){
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()))){
            String buf;
            while ((buf=reader.readLine())!=null){
                System.out.println("Receive the message from "+socket.getInetAddress()+":" + socket.getPort() +": \r\n"+buf);
            }
        } catch (IOException e) {
            logging.severe("IOexception happened when creating the buffer reader of the readthread");
        }
    }
}
