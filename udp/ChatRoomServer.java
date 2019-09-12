package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

public class ChatRoomServer {
    public static void main (String[] args) {
        try (DatagramSocket socket = new DatagramSocket(9876)) {
            // 定义接收的字节数组
            byte[] receive = new byte[1024];
            System.out.println("Sever started.");
            List<AddrAndPort> registeredClients = new LinkedList<>();
            while(true) {
                try {
                    DatagramPacket packet = new DatagramPacket(receive, receive.length);
                    socket.receive(packet);
                    // 获得发送端的IP、以及data
                    InetAddress senderAddr = packet.getAddress();
                    int senderPort = packet.getPort();
                    String receivedData = new String(packet.getData(), 0, packet.getLength());
                    // 关闭判断
                    if (receivedData.equals("q")) {
                        System.out.println("Sever端关闭");
                        break;
                    }
                    // 判断消息类型，private or public
                    String[] meesageGroup = receivedData.split(" ");
                    if (meesageGroup[0].equals("all")) {
                        // 如果发送端不在registeredList里，添加
                        // 发送给所有clients，接收的数据以及发送端的ip信息
                        boolean notRegisteredYet = true;
                        for (AddrAndPort client: registeredClients) {
                            if (client.getAddr().equals(senderAddr) && client.getPort() == senderPort) {
                                notRegisteredYet = false;
                            } else {
                                //创建要发送的数据包，然后用套接字发送
                                byte[] dataToSend = (senderAddr.toString()+ ": " + senderPort + "\r\n" + receivedData).getBytes();
                                DatagramPacket sendPacket = new DatagramPacket(dataToSend, dataToSend.length, client.getAddr(), client.getPort());
                                //用套接字发送数据包
                                socket.send(sendPacket);
                            }
                        }
                        if (notRegisteredYet) {
                            registeredClients.add(new AddrAndPort(senderAddr, senderPort));
                        }
                    } else {
                        // 从MessageGroup中获得发送方所要求的接收方address port，并且发送
                        String[] addrAndPort = meesageGroup[0].split(":");
                        InetAddress receiverAddr = InetAddress.getByName(addrAndPort[0]);
                        int receiverPort = Integer.parseInt(addrAndPort[1]);
                        byte[] dataToSend = (senderAddr.toString()+ ": " + senderPort + "\r\n" + meesageGroup[1]).getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(dataToSend, dataToSend.length, receiverAddr, receiverPort);
                        socket.send(sendPacket);
                    }

                } catch (IOException e) {}
            }
        } catch (SocketException e) {}
    }
}
