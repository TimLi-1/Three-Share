package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatServer {
    public static void main (String[] args) {
        try (DatagramSocket severSocket = new DatagramSocket(9876)) {
            byte[] sendData;
//            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (true) {
                try {
                    // 收
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    severSocket.receive(receivePacket);
                    // 把收到数据转化为发送数据
                    String sendDataInString = new String(receivePacket.getData(), 0, receivePacket.getLength()).toLowerCase();
                    sendData = sendDataInString.getBytes();
                    // 发
                    InetAddress clientAddr = receivePacket.getAddress();
                    int port = receivePacket.getPort();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddr, port);
                    severSocket.send(sendPacket);
                } catch (IOException e) {}
            }
        } catch (SocketException e) {}
    }
}
