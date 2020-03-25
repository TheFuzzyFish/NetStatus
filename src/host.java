import java.util.ArrayList;
import java.util.Scanner;

public class host {
    public String hostname;
    private ArrayList<Integer> ports;

    public host(String hostname, String ports) {
        this.ports = new ArrayList<Integer>();
        this.hostname = hostname;

        Scanner portScanner = new Scanner(ports).useDelimiter(",");

        while (portScanner.hasNext()) {
            this.ports.add(portScanner.nextInt());
        }
    }

    public int[] getPorts() {
        int portArray[] = new int[this.ports.size()];

        for (int i = 0; i < this.ports.size(); i++) {
            portArray[i] = this.ports.get(i);
        }

        return portArray;
    }

    public int getPort(int index) {
        return this.ports.get(index);
    }
}
