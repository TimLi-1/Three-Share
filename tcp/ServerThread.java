package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

public class ServerThread extends Thread{
    static Logger logging = Logger.getLogger(ServerThread.class.getName());

    private Socket socket;
//    private List<AddrAndPort> clientLists;
    private List<ServerThread> serverThreadList;
    private AddrAndPort addrAndPort;
    private BufferedWriter writer;

    public ServerThread(Socket socket, List<ServerThread> serverThreadList) throws IOException{
        this.socket = socket;
        this.serverThreadList = serverThreadList;
        this.addrAndPort = new AddrAndPort(socket.getInetAddress(), socket.getPort());
        this.writer =  new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        String buf = null;
        // Receiving the message
        try (BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(socket.getInputStream()))){
            while (true) {
                if ((buf = reader.readLine()) != null) {
                    logging.info("Get meesage " + buf + " from " + socket.getInetAddress());
                    String[] words = buf.split(":");
                    if (words[0].equals("all")) {
                        // 群组群聊新消息
                        String wordsToSend = words[1];
                        // iterate every client in the serverThreadList to use the socket send message
                        for (ServerThread thread: serverThreadList) {
                            thread.getWriter().write(wordsToSend + "\r\n");
                            thread.getWriter().flush();
                        }
                    } else {
                        // 一对一的私聊消息
                        String wordsToSend = words[2];
                        AddrAndPort ipToFind = new AddrAndPort(InetAddress.getByName(words[0]), Integer.parseInt(words[1]));
                        // Sending the message
                        ServerThread receiver = null;
                        for (ServerThread thread: serverThreadList) {
                            if (ipToFind.equals(thread.getAddrAndPort())){
                                receiver = thread;
                            }
                        }
                        if (receiver == null) {
                            logging.info("This ip address and the port not registered yet. Doing the register procedure, this message cannot be sent");
                        } else {
                            receiver.getWriter().write(wordsToSend + "\r\n");
                            receiver.getWriter().flush();
                        }
                    }
                }
            }
        } catch (IOException e) {
            logging.severe("IO exception happened when creating bufferedReader from the socket");
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public List<ServerThread> getServerThreadList() {
        return serverThreadList;
    }

    public AddrAndPort getAddrAndPort() {
        return addrAndPort;
    }

    public BufferedWriter getWriter() {
        return writer;
    }
}
