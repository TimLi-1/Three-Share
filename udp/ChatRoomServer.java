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
            // Define the bytes for receiving
            byte[] receive = new byte[1024];
            System.out.println("Sever started.");
            List<AddrAndPort> registeredClients = new LinkedList<>();
            while(true) {
                try {
                    DatagramPacket packet = new DatagramPacket(receive, receive.length);
                    socket.receive(packet);
                    // get the sender's ip and data
                    InetAddress senderAddr = packet.getAddress();
                    int senderPort = packet.getPort();
                    String receivedData = new String(packet.getData(), 0, packet.getLength());
                    // whether the connnection end
                    if (receivedData.equals("q")) {
                        System.out.println("Sever端关闭");
                        break;
                    }
                    // justify the mssage type is private or public
                    String[] meesageGroup = receivedData.split(" ");
                    if (meesageGroup[0].equals("all")) {
                        // if sender not registered in registeredList, add it;
                        // send to all clients, data and sender's ip
                        boolean notRegisteredYet = true;
                        for (AddrAndPort client: registeredClients) {
                            if (client.getAddr().equals(senderAddr) && client.getPort() == senderPort) {
                                notRegisteredYet = false;
                            } else {
                                // create the packet; send using socket
                                byte[] dataToSend = (senderAddr.toString()+ ": " + senderPort + "\r\n" + receivedData).getBytes();
                                DatagramPacket sendPacket = new DatagramPacket(dataToSend, dataToSend.length, client.getAddr(), client.getPort());
                                socket.send(sendPacket);
                            }
                        }
                        if (notRegisteredYet) {
                            registeredClients.add(new AddrAndPort(senderAddr, senderPort));
                        }
                    } else {
                        // Get the receiver's address and port from MessageGroup; Send
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
