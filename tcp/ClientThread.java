package tcp;

import udp.ChatClient;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientThread extends Thread{
    static Logger logging = Logger.getLogger(ClientThread.class.getName());

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 10001)){
            logging.info("my local ip:port "+ socket.getLocalAddress()+":"+socket.getLocalPort());
            // Use the readThread to read the messages in the socket
            ReadThread readThread = new ReadThread(socket);
            Thread readThreadA = new Thread(readThread);
            readThreadA.start();
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()))){
                // Send messages
                while (true) {
                    // Collect receiver's ip and port first
                    System.out.println("Enter the ip:port who you want send message to; For broadcasting, enter all");
                    String ipPort = scanner.nextLine();
                    // Collect message and send
                    System.out.println("Enter the Message");
                    String message = scanner.nextLine();
                    writer.write(ipPort + ":" + message+"\r\n");
                    writer.flush();
                }
            } catch (IOException e){
                logging.severe("client end creating output stream exception");
            }
        }catch (IOException e) {
            logging.severe("IOException due to creating socket");
        }
    }

    public static void main(String[] args) {
        logging.info("Client A started.");
        ClientThread clientA = new ClientThread();
        Thread threadA = new Thread(clientA);
        threadA.start();
    }
}
