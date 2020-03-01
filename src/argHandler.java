import java.io.*;
import java.util.Properties;

/**
 * Object used to handle arguments passed in through the command line.
 * Always be sure to quit the program if argHandler.doQuit() == true.
 * This can only handle 1 flag at a time
 *
 * @author Zachary Kline
 */
public class argHandler {
    private boolean doQuit;
    private String configPath;
    private boolean useAliases;
    private int timeout;

    /**
     * Parses input flags
     * @param args input flags
     */
    public argHandler(String args[]) {
        if (args.length > 0) {
            switch (args[0]) {
                case "-h":
                case "--help":
                case "help":
                    System.out.println("Usage: java -jar NetStatus.jar [OPTIONS...]\n" +
                                    "NetStatus is a program to monitor your network infrastructure and help notify\n" +
                                    "you when there's a problem.\n" +

                                    "Examples:\n" +
                                    "\tjava -jar NetStatus.jar -c /usr/local/share/NetStatus/\n" +

                                    "\nOptions:\n" +
                                    "\t-h,--help\tDisplays this handy dandy help file\n" +
                                    "\t-v,--version\tDisplays the program version number\n" +
                                    "\t-c,--config\tThe path of the config directory\n" +

                                    "\nFiles\n" +
                                    "\tconfig.properties\tStores basic config options\n" +
                                    "\thosts.csv\t\tStores the hosts that NetStatus will check in 'hostname,port,port,port' format\n" +
                                    "\taliases.properties\tStores port aliases so that NetStatus will tell you what the service is instead of just the port number\n");
                    doQuit = true;
                    break;
                case "-v":
                case "--version":
                case "version":
                    System.out.println("NetStatus.jar Version: 1.1\nLicense: GNU General Public License v3.0\nThis is free software, you are free to change and redistribute it.\nThere is NO WARRANTY, to the extent permitted by law.\n\nWritten by Zachary Kline\nhttps://github.com/TheFuzzyFish/NetStatus");
                    doQuit = true;
                    break;
                case "-c":
                case "--config":
                case "config":
                    configPath = args[1];
                    if (configPath.charAt(configPath.length() - 1) != '/') { // If they didn't end the config path with a backslash, append one
                        configPath = configPath.concat("/");
                    }
                    break;
            }
        }

        this.init();
    }

    /**
     * Check this directly after you declare a new argHandler(). Your program should quit without doing anything if a flag like "--help" is issued.
     * @return boolean whether your should quit or not
     */
    public boolean doQuit() {
        return doQuit;
    }

    /**
     * Returns the path of the config directory (with the tailing backslash)
     * @return the path of the config directory (with the tailing backslash)
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * Reads from the configuration files and loads the appropriate settings.
     * If the configuration directory or files don't exist, this creates them and gives some defaults
     */
    public void init() {
        if (configPath == null) {
            configPath = System.getProperty("user.home") + "/NetStatus/"; // If the user didn't set the config path, set the default ~/NetStatus/
        }
        File configDir = new File(configPath);
        File hostList = new File(configPath + "hosts.csv");
        File configFile = new File(configPath + "config.properties");
        File aliases = new File(configPath + "aliases.properties");

        configDir.mkdir();

        if (!hostList.exists()) {
            try {
                hostList.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(configPath + "hosts.csv"));
                writer.write(
                        "example.com,80,443\n");
                writer.close();
            } catch (IOException e) {
                System.out.println("Error creating new " + configPath + "hosts.csv. Are your permissions messed up?");
            }
        }
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(configPath + "config.properties"));
                writer.write(
                        "#NetStatus v1.1 config.properties\n\n" +
                                "# useAliases tells NetStatus whether or not to ignore the contents of the aliases.conf file.\n" +
                                "# Aliases are used to alter the output of the program. By default, when NetStatus checks a port, it is\n" +
                                "# listed as 'Port xxxx'. With aliases, you can set a port number equal to some string, and NetStatus\n" +
                                "# will use that string to reference the port in its output.\n" +
                                "useAliases=true\n\n" +
                                "# timeout tells the NetStatus how long to wait while attempting to connect to a port. By\n" +
                                "# default, this is 1 second, but you may need to extend it slightly if your network or\n" +
                                "# the hosts you're checking are slow, or you start getting false positive.\n" +
                                "# This variable is represented in milliseconds.\n" +
                                "timeout=1000\n");
                writer.close();
            } catch (IOException e) {
                System.out.println("Error creating new " + configPath + "config.properties. Are your permissions messed up?");
            }
        }
        if (!aliases.exists()) {
            try {
                aliases.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(configPath + "aliases.properties"));
                writer.write(
                        "#NetStatus v1.1 aliases.properties\n\n" +
                                "# By default, when NetStatus checks a port, it is listed as 'Port xxxx'. With aliasing, you can\n" +
                                "# alter the output of NetStatus by replacing 'Port xxxx' with your own custom text better identify\n" +
                                "# what that port actually runs. I'll get you started with some basics, but feel free to define\n" +
                                "# your own:\n" +
                                "22=SSH\n" +
                                "80=HTTP\n" +
                                "443=HTTPS\n");
                writer.close();
            } catch (IOException e) {
                System.out.println("Error creating new " + configPath + "aliases.properties. Are your permissions messed up?");
            }
        }

        Properties configProp = new Properties();
        InputStream stream = null;

        try {
            stream = new FileInputStream(configPath + "config.properties");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to access " + configPath + "config.properties. Perhaps it doesn't exist?");
        }
        try {
            configProp.load(stream);
        } catch (IOException e) {
            System.out.println("Unable to access " + configPath + "config.properties. Perhaps it doesn't exist?");
        }

        useAliases = Boolean.parseBoolean(configProp.getProperty("useAliases"));
        timeout = Integer.parseInt(configProp.getProperty("timeout"));
    }

    /**
     * Returns the timeout value in milliseconds loaded from the configuration files
     * @return the timeout value in milliseconds loaded from the configuration files
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Returns whether or not you should use aliases loaded from the configuration files
     * @return whether or not you should use aliases loaded from the configuration files
     */
    public boolean getUseAliases() {
        return useAliases;
    }
}
