package udp;

public class ClientB {
    public static void main(String[] args) {
        // client B
        System.out.println("client B started");
        ChatClient clientB = new ChatClient();
        Thread threadB = new Thread(clientB);
        threadB.start();
    }
}
