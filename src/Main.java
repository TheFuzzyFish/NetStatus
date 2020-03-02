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
        boolean scriptMode = argHandler.getScriptMode();

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


        int numDownTotal = 0; // Keeps track of the number of hosts that report offline
        String scriptModeOutput = "";

        for (int i = 0; i < hosts.returnNumHosts(); i++) { // Loops through the hosts
            int numDownInThisHost = 0;
            String buffer = "";

            if (!scriptMode) {
                System.out.println(hosts.getHostname(i) + ": ");
            }

            for (int j = 0; j < hosts.returnNumPorts(i); j++) { // Loop through the ports of each host
                String portName; // Used to parse aliases if necessary

                // If useAliases=true in the config file, replace portName with the specified alias
                if (useAliases && aliasProp.getProperty(Integer.toString(hosts.getPort(i,j))) != null) {
                    portName = aliasProp.getProperty(Integer.toString(hosts.getPort(i,j)));
                } else {
                    if (scriptMode) {
                        portName = Integer.toString(hosts.getPort(i, j));
                    } else {
                        portName = "port " + Integer.toString(hosts.getPort(i, j));
                    }
                }

                // Checks if host is available. Depending on the value of scriptMode in config.properties, may produce different output
                if (scriptMode) {
                    if (!HostAvailabilityChecker.isHostReachable(hosts.getHostname(i), hosts.getPort(i, j), timeoutMillis)) {
                        buffer = buffer.concat(portName + ",");
                        numDownTotal++;
                        numDownInThisHost++;
                    }
                } else {
                    if (HostAvailabilityChecker.isHostReachable(hosts.getHostname(i), hosts.getPort(i, j), timeoutMillis)) { // Checks the port for connectivity
                        System.out.println("\t" + String.format("%-15s %15s", portName + ": ", "online"));
                    } else {
                        System.out.println("\t" + String.format("%-15s %15s", portName + ": ", "offline"));
                        numDownTotal++;
                    }
                }
            }
            if (numDownInThisHost > 0) {
                buffer = buffer.replaceAll(",$", " "); // Removes the trailing comma from the buffer
                scriptModeOutput = scriptModeOutput.concat("-" + hosts.getHostname(i) + ": " + buffer); // If any services are down for this host, add the hostname and name of the service to the down list
            }
        }

        if (numDownTotal > 0) {
            if (scriptMode) {
                System.out.println(numDownTotal + " unreachable: " + scriptModeOutput);
            } else {
                System.out.println("\n~~~\nThere are " + numDownTotal + " unreachable services in your network.");
            }
        } else if (!scriptMode) {
            System.out.println("\n~~~\nNetwork healthy.");
        } else {
            System.out.println("Network healthy.");
        }
    }
}
