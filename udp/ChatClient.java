package udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ChatClient implements Runnable{
    @Override
    public  void run() {
        // initialize inputStreamReader, ready for reading the input from user
        BufferedReader userInput = new BufferedReader(
                                new InputStreamReader(System.in));
        try (DatagramSocket clientSocket = new DatagramSocket();) {
            InetAddress currentAddress = InetAddress.getLocalHost();
            byte[] sendData;
            System.out.println("Enter all or ip:port；Enter the message content:");
            // Loop for sending and receiving scokets
            while (true) {
                try {
                    byte[] receiveData = new byte[1024];
                    // Read input; put them into the packet; send
                    String target = userInput.readLine();
                    String sentence = userInput.readLine();
                    if (sentence.equals("q")) break;
                    sendData = (target + " " + sentence).getBytes();
                    DatagramPacket sendPacket =
                            new DatagramPacket(sendData, sendData.length, currentAddress, 9876);
                    clientSocket.send(sendPacket);
                    // create packet for receiving mesaage; receive
                    DatagramPacket receivePacket =
                            new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);
                    String receivedChat = new String (receivePacket.getData(), 0 , receivePacket.getLength());
                    System.out.println("=========================");
                    System.out.println("FROM: " + receivedChat);
                    System.out.println("(I\'m " +  clientSocket.getLocalAddress().toString() + ":" + clientSocket.getLocalPort());
                    System.out.println("=========================");
                } catch (IOException e) {}
            }
        } catch (SocketException e){}
        catch (UnknownHostException e){}
    }

    public static void main(String[] args) {
        // client A
        System.out.println("Client A started");
        ChatClient clientA = new ChatClient();
        Thread threadA = new Thread(clientA);
        threadA.start();
    }
}
