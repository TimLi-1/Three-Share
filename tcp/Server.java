package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Server {
    static Logger logging =  Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
//        List<AddrAndPort> clientLists = new ArrayList<>();
        List<ServerThread> serverThreadLists = new ArrayList<>();
        try ( ServerSocket serverSocket = new ServerSocket(10001) ) {
            logging.info("Server main thread started at port 10001");
            int threadCount = 0;
            while (true) {
                // Listen the request, create new thread to assign the task
                Socket socket = serverSocket.accept();
                ServerThread thread = new ServerThread(socket, serverThreadLists);
                serverThreadLists.add(thread);
                threadCount ++;
                logging.info("Allocate the task into sub-thread " +threadCount);
//                // If this client have not registered yet, update the clientLists
//                boolean notRegistered = true;
//                for (AddrAndPort client: clientLists) {
//                    if (client.getAddr() == socket.getInetAddress() && client.getPort() == socket.getPort()) {
//                        notRegistered = false;
//                    }
//                }
//                if (notRegistered) {
//                    clientLists.add(new AddrAndPort(socket.getInetAddress(), socket.getPort()));
//                }
                // start the thread
                thread.start();
            }
        } catch (IOException e) {
            logging.severe("IOE exceoption, happened when creating sever socket at port 10001");
        }
    }
}
