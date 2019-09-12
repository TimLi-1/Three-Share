package udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ChatClient implements Runnable{
    @Override
    public  void run() {
        // 开启inputStreamReader准备读取用户输入
        BufferedReader userInput = new BufferedReader(
                                new InputStreamReader(System.in));
        try (DatagramSocket clientSocket = new DatagramSocket();) {
            InetAddress currentAddress = InetAddress.getLocalHost();
            byte[] sendData;
            System.out.println("输入all或者发送对象；输入想发送的语句:");
            // 循环发送接收数据包
            while (true) {
                try {
                    byte[] receiveData = new byte[1024];
                    // 读取输入、放入发送packet、发送
                    String target = userInput.readLine();
                    String sentence = userInput.readLine();
                    if (sentence.equals("q")) break;
                    sendData = (target + " " + sentence).getBytes();
                    DatagramPacket sendPacket =
                            new DatagramPacket(sendData, sendData.length, currentAddress, 9876);
                    clientSocket.send(sendPacket);
                    // 创建接收packet、接收
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
