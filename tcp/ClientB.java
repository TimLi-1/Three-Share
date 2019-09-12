package tcp;

import java.util.logging.Logger;

public class ClientB {
    static Logger logging = Logger.getLogger(ClientThread.class.getName());

    public static void main(String[] args) {
        logging.info("Client B started.");
        ClientThread clientB = new ClientThread();
        Thread threadB = new Thread(clientB);
        threadB.start();
    }
}
