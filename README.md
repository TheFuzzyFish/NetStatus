# NetStatus
A small command-line Java application to monitor your network and report of any down services.
Reads a list of hostnames and ports from a CSV file, then checks each individual port for connectivity.
By default, configuration files are put into ~/NetStatus/. You can specify the absolute path of a folder with the --config flag.

# Installation
You can either clone this repository and compile the code yourself, or you can just download my precompiled .jar file from [NetStatus-1.1.jar](https://github.com/TheFuzzyFish/NetStatus/blob/master/NetStatus-1.1.jar)

# Usage
<pre>
Usage: java -jar NetStatus.jar [OPTIONS...]
NetStatus is a program to monitor your network infrastructure and help notify you when there's a problem.
Examples:
	java -jar NetStatus.jar -c /usr/local/share/NetStatus/

Options:
	-h,--help	Displays this handy dandy help file
	-v,--version	Displays the program version number
	-c,--config	The path of the config directory

Files
	config.properties	Stores basic config options
	hosts.csv		Stores the hosts that NetStatus will check in 'hostname,port,port,port' format
	aliases.properties	Stores port aliases so that NetStatus will tell you what the service is instead of just the port number
</pre>

You can run in default mode with just `java -jar NetStatus-1.1.jar`, but if you want to specify a configuration folder with its own hosts.csv and settings, use the -c (or --config) flag to specify an absolute path to the folder.
The hosts.csv file should read 'hostname,port,port,port' with up to 256 hosts and 256 ports each. For example:
<pre>
google.com,80,443
172.16.0.11,22
172.16.0.20,25565
</pre>

If you want to customize the output of NetStatus, you can use the aliases.properties file to specify service names for certain ports. It has a few defaults:
<pre>
22=SSH
80=HTTP
443=HTTPS
</pre>
Feel free to add your own port aliases, such as FTP or SMTP. NetStatus will substitute them accordingly.
Additionally, you can toggle whether or not aliases are used (along with the timeout of TCP connections) in config.properties.
I use this program on a Raspberry Pi outside of my network to send me text notifications with [PushSafer](https://www.pushsafer.com/). This setup is easily replicatable with a cron job every 15 minutes that greps for "unreachable" and triggers a PushSafer call if a value is returned. I made this program to create a clean solution to an awful conglomeration of inefficiency that was my previous solution.

# To-Do
 - Add support to run a script if numDownHosts > 0
 - Add UDP support
