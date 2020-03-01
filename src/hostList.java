import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * An object to scan in and manage the contents of a CSV file.
 * Each line in the CSV file should start with a hostname (IP or domain name), then a list of ports to check on that particular host
 *
 * @author Zachary Kline
 */
public class hostList {
    private String hostList[][] = new String[256][256]; //Array that stores hostnames in column 0 and ports in the rest of each row up to 256 hosts with 256 ports each

    /**
     * Creates a new hostList based on a CSV file. CSV lines should give hostname first, then a list of port numbers
     * @param nameOfCsvFile the path (or just the name, if the file is in the working directory) of the CSV file to manage
     */
    public hostList(String nameOfCsvFile) {
        File file = new File(nameOfCsvFile);
        Scanner scan = null;
        try {
            scan = new Scanner(file).useDelimiter(",|\\n"); //Uses comma and delimeters
        } catch (FileNotFoundException e) {
            System.out.println("Error, no CSV file found. Please create a CSV file with each line containing a new host in the first cell, followed by a list of ports to check");
        }

        int i = 0; // Host index in array
        int j = 1; // Port index in array

        /* -- Scan CSV into hostList[][] array -- */
        while (scan.hasNext()) { //While we haven't reached the end of the file
            hostList[i][0] = scan.next(); //Assume newline, scan in hostname

            while (scan.hasNextInt()) { //Assume hostname already scanned, scan ports until we reach the end of this line
                hostList[i][j] = Integer.toString(scan.nextInt());
                j++;
            }
            i++; //Assume newline, increment row number to start on next host
            j = 1; //Reset port index
        }
    }

    /**
     * If for whatever reason you need the array (you shouldn't)
     * @return a 2D String array identical to the CSV file
     */
    public String[][] returnHostList() {
        return hostList;
    }

    /**
     * Gives the number of hosts detected from the CSV file (should be equal to the number of rows)
     * @return
     */
    public int returnNumHosts() {
        int x = 0;
        while (hostList[x][0] != null) {
            x++;
        }
        return x;
    }

    /**
     * Gives the number of ports a certain host has
     * @param hostIndex the row index of the host to get the number of ports from (starts at 0)
     * @return the number of ports to check for a certain host
     */
    public int returnNumPorts(int hostIndex) {
        int x = 0;
        while (hostList[hostIndex][x + 1] != null) {
            x++;
        }
        return x;
    }

    /**
     * Gives the hostname at a certain index
     * @param hostIndex the row index of the host (starts at 0)
     * @return the name of the host at a certain index
     */
    public String getHostname(int hostIndex) {
        return hostList[hostIndex][0];
    }

    /**
     * Returns the port number at a certain index
     * @param hostIndex the row index of the host (starts at 0)
     * @param portIndex the column index of the port (starts at 0)
     * @return the port number at a certain index
     */
    public int getPort(int hostIndex, int portIndex) {
        return Integer.parseInt(hostList[hostIndex][portIndex + 1]);
    }
}

