import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loops through ~/hosts.csv by default to check if hosts ACK TCP requests, useful for checking if a service is online.
 * CSV file should consist of entries that look like 'hostname,port,port,port' with up to 256 hosts and 256 ports each.
 */
public class Main {
    public static void main(String args[]) {

        /* -- Loads settings -- */
        argHandler argHandler = new argHandler(args);
        if (argHandler.doQuit()) { // Triggers if an argument is issued that assumes the program doesn't need to continue
            return;
        }

        String configPath = argHandler.getConfigPath();
        int timeoutMillis = argHandler.getTimeout();
        boolean useAliases = argHandler.getUseAliases();

        hostList hosts = new hostList(configPath + "hosts.csv");

        Properties aliasProp = new Properties();
        InputStream aliasStream = null;
        try {
            aliasStream = new FileInputStream(configPath + "aliases.properties");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to access " + configPath + "aliases.properties. Perhaps it doesn't exist?");
        }
        try {
            aliasProp.load(aliasStream);
        } catch (IOException e) {
            System.out.println("Unable to access " + configPath + "aliases.properties. Perhaps it doesn't exist?");
        }
        /* -- End of settings -- */


        int numDownHosts = 0; // Keeps track of the number of hosts that report offline

        for (int i = 0; i < hosts.returnNumHosts(); i++) { // Loops through the hosts
            System.out.println(hosts.getHostname(i) + ":");

            for (int j = 0; j < hosts.returnNumPorts(i); j++) { // Loop through the ports of each host
                String portName; // Used to parse aliases if necessary

                // If useAliases=true in the config file, replace portName with the specified alias
                if (useAliases) {
                    portName = aliasProp.getProperty(Integer.toString(hosts.getPort(i,j)));
                } else {
                    portName = "Port " + Integer.toString(hosts.getPort(i, j));
                }

                if (HostAvailabilityChecker.isHostReachable(hosts.getHostname(i), hosts.getPort(i, j), timeoutMillis)) { // Checks the port for connectivity
                    System.out.println("\t" + portName + ":\t\tonline");
                } else {
                    System.out.println("\t" + portName + ":\t\toffline");
                    numDownHosts++;
                }
            }
        }

        if (numDownHosts > 0) {
            System.out.println("\nThere are " + numDownHosts + " unreachable services in your network.");
        } else {
            System.out.println("\n~~~\nNetwork healthy.");
        }
    }
}
