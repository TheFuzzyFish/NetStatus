import java.util.ArrayList;
import java.util.Scanner;

/**
 * An object to represent a single host with an arbitrary number of corresponding ports.
 * This is meant to be used with an ArrayList implementation of our host lists to eliminate the fixed size limit of our CSV file
 *
 * @author TheFuzzyFish
 */
public class host {
    public String hostname;
    private ArrayList<Integer> ports;

    /**
     * Constructs a new host object based on a CSV list of the format hostname,port,port,port...
     * @param line a CSV of the format hostname,port,port,port...
     */
    public host(String line) {
        Scanner lineScanner = new Scanner(line).useDelimiter(",");

        this.ports = new ArrayList<Integer>();
        this.hostname = lineScanner.next(); // Assumes that the hostname is at the beginning of the CSV

        while (lineScanner.hasNextInt()) {
            this.ports.add(lineScanner.nextInt()); // Adds all the following ports to the ArrayList
        }
    }

    /**
     * Returns a traditional array of the ports corresponding to this host
     * @return a traditional array of the ports corresponding to this host
     */
    public int[] getPorts() {
        int portArray[] = new int[this.ports.size()];

        for (int i = 0; i < this.ports.size(); i++) {
            portArray[i] = this.ports.get(i);
        }

        return portArray;
    }

    /**
     * Returns a port number at a specific index
     * @param index the index of the port to get
     * @return the port number at the specified index
     */
    public int getPort(int index) {
        return this.ports.get(index);
    }

    /**
     * Returns the number of ports that exist for this host
     * @return the number of ports that exist for this host
     */
    public int getNumPorts() {
        return this.ports.size();
    }
}
