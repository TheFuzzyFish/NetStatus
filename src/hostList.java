import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * An object to scan in and manage the contents of a CSV file.
 * Each line in the CSV file should start with a hostname (IP or domain name), then a list of ports to check on that particular host
 *
 * @author TheFuzzyFish
 */
public class hostList {
    private ArrayList<host> hostList; //ArrayList that stores an arbitrary number of hosts

    /**
     * Creates a new hostList based on a CSV file. CSV lines should give hostname first, then a list of port numbers
     * @param nameOfCsvFile the path (or just the name, if the file is in the working directory) of the CSV file to manage
     */
    public hostList(String nameOfCsvFile) {
        this.hostList = new ArrayList<host>();

        File file = new File(nameOfCsvFile);
        Scanner scan = null;

        try {
            scan = new Scanner(file).useDelimiter(",|\\n"); //Uses comma and newlines as delimeters

            /* -- Scan CSV into ArrayList -- */
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (line.length() != 0 && line.charAt(0) != '#') { // Avoids usage of line.isBlank() to ensure Java 8 compatibility
                    this.hostList.add(new host(line));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error, no CSV file found. Please create a CSV file with each line containing a new host in the first cell, followed by a list of ports to check");
        }
    }

    /**
     * Gives the number of hosts detected from the CSV file (should be equal to the number of lines)
     * @return the number of hosts detected from the CSV file (should be equal to the number of lines)
     */
    public int returnNumHosts() {
        return this.hostList.size();
    }

    /**
     * Gives the number of ports a certain host has
     * @param hostIndex the row index of the host to get the number of ports from (starts at 0)
     * @return the number of ports to check for a certain host
     */
    public int returnNumPorts(int hostIndex) {
        return this.hostList.get(hostIndex).getNumPorts();
    }

    /**
     * Gives the hostname at a certain index
     * @param hostIndex the row index of the host (starts at 0)
     * @return the name of the host at a certain index
     */
    public String getHostname(int hostIndex) {
        return this.hostList.get(hostIndex).hostname;
    }

    /**
     * Returns the port number at a certain index
     * @param hostIndex the row index of the host (starts at 0)
     * @param portIndex the column index of the port (starts at 0)
     * @return the port number at a certain index
     */
    public int getPort(int hostIndex, int portIndex) {
        return this.hostList.get(hostIndex).getPort(portIndex);
    }
}

