import java.io.IOException;

/**
 * Checks to see if a host responds on a certain port withing a timeout frame. Uses David Reilly's TimedSocket class.
 *
 * @author Zachary Kline
 */
public class HostAvailabilityChecker {
    /**
     * Checks if a hostname sends a TCP SYN ACK on a specific port
     * @param hostname the hostname (IP or domain name is fine) to query
     * @param port the port on the hostname to send a TCP SYN packet
     * @param timeout the amount of time to wait before timing out
     * @return boolean; true if connection was successful and the port is open, false if the connection failed and the port is closed or took too long
     */
    public static boolean isHostReachable(String hostname, int port, int timeout) {
        try {
            TimedSocket.getSocket(hostname, port, timeout);
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
