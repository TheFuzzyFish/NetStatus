# NetStatus
A small command-line Java application to monitor your network and report of any down services.
Reads a list of hostnames and ports from a CSV file, then checks each individual port for connectivity.
By default, configuration files are put into ~/NetStatus/. You can specify the absolute path of a folder with the --config flag.

# Installation
You can either clone this repository and compile the code yourself (not recommended), or you can just download my precompiled .jar file (recommended) from [NetStatus-1.2.1.jar](https://github.com/TheFuzzyFish/NetStatus/raw/master/NetStatus-1.2.1.jar)

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

You can run in default mode with just `java -jar NetStatus-1.2.1.jar`, but if you want to specify a configuration folder with its own hosts.csv and settings, use the -c (or --config) flag to specify an absolute path to the folder.
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

# What's the deal with helper-scripts?
I use this program on a Raspberry Pi outside of my network to send me text notifications with [PushSafer](https://www.pushsafer.com/). The two scripts in helper-scripts are the exact scripts that I use for this implementation. I simply run the python script as a cron job every 10 minutes, which calls pushSafer.sh. If you want to use that exact setup, just add your API key into the pushSafer.sh script. Otherwise, feel free to use the python script to call whatever other script you want.

# To-Do
 - Add support to run a script if numDownHosts > 0 (see helper-scripts in the meantime)
 - Add UDP support
 
 # Known Issues
 - If a TCP SYN is sent to a server and no response is provided, NetStatus will successfully produce an output and close the main thread, but the thread in charge of that TCP socket will not close. As far as I can tell, this is a fundamental issue with Java's method of dealing with sockets, and the TimedSocket implementation I'm using does not avoid it. I'm working on a solution. In the meantime, the python script in helper-scripts will properly exit in this scenario.
