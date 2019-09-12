package udp;

import java.net.InetAddress;

public class AddrAndPort {
    private InetAddress addr;
    private int port;

    public AddrAndPort(InetAddress addr, int port) {
        this.addr = addr;
        this.port = port;
    }

    public InetAddress getAddr() {
        return addr;
    }

    public int getPort() {
        return port;
    }

    public String toString() {
        return addr + ":" + port;
    }
}
