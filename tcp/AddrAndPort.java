package tcp;

import java.net.InetAddress;
import java.util.Objects;

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
//
//    public String toString() {
//        return addr + ":" + port;
//    }

    public boolean equals(AddrAndPort b) {
        return (this.addr.equals(b.getAddr()) && this.port == b.getPort());
    }

    public int hashCode() {
        return Objects.hash(getAddr(), getPort());
    }
}
