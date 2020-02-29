/**
 * Loops through ~/hosts.csv by default to check if hosts ACK TCP requests, useful for checking if a service is online.
 * CSV file should consist of entries that look like 'hostname,port,port,port' with up to 256 hosts and 256 ports each.
 */
public class Main {
    /**
     * The number of ms to wait before timing out a connection
     */
    public static final int timeout = 1000;

    public static void main(String args[]) {
        hostList hosts = new hostList(System.getProperty("user.home") + "/hosts.csv"); // Uses ~/hosts.csv by default. Feel free to change this

        int numDownHosts = 0; // Keeps track of the number of hosts that report offline

        for (int i = 0; i < hosts.returnNumHosts(); i++) { // Loops through the hosts
            System.out.println(hosts.getHostname(i) + ":");

            for (int j = 0; j < hosts.returnNumPorts(i); j++) { // Loop through the ports of each host
                if (HostAvailabilityChecker.isHostReachable(hosts.getHostname(i), hosts.getPort(i, j), timeout)) {
                    System.out.println("\tPort " + hosts.getPort(i, j) + ":\t\tonline");
                } else {
                    System.out.println("\tPort " + hosts.getPort(i, j) + ":\t\toffline");
                    numDownHosts++;
                }
            }
        }

        if (numDownHosts > 0) {
            System.out.println("\nThere are " + numDownHosts + " unreachable services in your network.");
        } else {
            System.out.println("\nNetwork healthy.");
        }
    }
}
