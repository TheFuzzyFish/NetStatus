import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loops through ~/hosts.csv by default to check if hosts ACK TCP requests, then prints a formatted status output.
 * CSV file should consist of entries that look like 'hostname,port,port,port'
 *
 * @author TheFuzzyFish
 */
public class Main {
    public static void main(String args[]) {

        /* -- Loads settings -- */
        argHandler argHandler = new argHandler(args);
        if (argHandler.doQuit()) { // Triggers if an argument is issued that assumes the program doesn't need to continue
            return;
        }

        String configPath = argHandler.getConfigPath();

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
        String scriptModeOutput = ""; // If you're in script mode, this is used to forge a string that will be printed

        for (int i = 0; i < hosts.returnNumHosts(); i++) { // Loops through the hosts
            int numDownInThisHost = 0; // If you're in script mode, this is used in the background for formatting
            String buffer = ""; // If you're in script mode, this is used to temporarily store a list of all down services before appending them to scriptModeOutput

            if (!argHandler.scriptMode) {
                System.out.println(hosts.getHostname(i) + ": ");
            }

            for (int j = 0; j < hosts.returnNumPorts(i); j++) { // Loop through the ports of each host
                String portName; // Used to parse aliases if necessary

                // If useAliases=true in the config file, replace portName with the specified alias
                if (argHandler.useAliases && aliasProp.getProperty(Integer.toString(hosts.getPort(i,j))) != null) {
                    portName = aliasProp.getProperty(Integer.toString(hosts.getPort(i,j)));
                } else {
                    if (argHandler.scriptMode) {
                        portName = Integer.toString(hosts.getPort(i, j));
                    } else {
                        portName = "port " + Integer.toString(hosts.getPort(i, j));
                    }
                }

                // Checks if host is available. Depending on the value of scriptMode in config.properties, may produce different output
                if (argHandler.scriptMode) {
                    if (!HostAvailabilityChecker.isHostReachable(hosts.getHostname(i), hosts.getPort(i, j), argHandler.timeout)) {
                        buffer = buffer.concat(portName + ", ");
                        numDownTotal++;
                        numDownInThisHost++;
                    }
                } else {
                    if (HostAvailabilityChecker.isHostReachable(hosts.getHostname(i), hosts.getPort(i, j), argHandler.timeout)) { // Checks the port for connectivity
                        System.out.println("\t" + String.format("%-15s %15s", portName + ": ", "online"));
                    } else {
                        System.out.println("\t" + String.format("%-15s %15s", portName + ": ", "offline"));
                        numDownTotal++;
                    }
                }
            }
            if (numDownInThisHost > 0) { // This also only applies if you're in script mode
                buffer = buffer.replaceAll(", $", " "); // Removes the trailing comma from the buffer
                scriptModeOutput = scriptModeOutput.concat("\n~" + hosts.getHostname(i) + ": " + buffer); // If any services are down for this host, add the hostname and name of the service to the down list
            }
        }

        // Outputs the status of the network and sends Discord notification if necessary
        if (numDownTotal > 0) {
            if (argHandler.scriptMode) {
                System.out.println(numDownTotal + " unreachable: " + scriptModeOutput);
            } else {
                System.out.println("\n~~~\nThere are " + numDownTotal + " unreachable services in your network.");
            }

            /* Sends Discord Notification */
            if (argHandler.useDiscord) {
                DiscordWebhook notif = new DiscordWebhook(argHandler.discordWebhookUrl);
                notif.setUsername(argHandler.discordUsername);
                notif.setContent(numDownTotal + " unreachable: " + scriptModeOutput.replaceAll("\n", "\\\\n")); // JSON kinda breaks if you put newlines in it, but replacing them with "\n" works fine. I had to add two slashes here because it was being escaped somewhere else.

                try {
                    notif.execute();
                } catch (IOException e) {
                    System.out.println("Error sending Discord notification");
                    e.printStackTrace();
                }
            }
        } else if (!argHandler.scriptMode) {
            System.out.println("\n~~~\nNetwork healthy.");
        } else {
            System.out.println("Network healthy.");
        }
    }
}
