# NetStatus
A small command-line Java application to monitor your network and report of any down services.
Reads a list of hostnames and ports from a CSV file, then checks each individual port for connectivity.

# Installation
You can either clone this repository and compile the code yourself, or you can just download my precompiled .jar file from /NetStatus-1.0.jar

# Usage
At this time, there is no user interaction, so you just run the Jar with `java -jar NetStatus-1.0.jar`.
The program reads from ~/hosts.csv by default, but you can easily change that value in src/Main.java on line 12. The program will run through the CSV file and attempt to connect to hostnames at given ports.
The CSV file should read 'hostname,port,port,port' with up to 256 hosts and 256 ports each. For example:
<pre>
google.com,80,443
172.16.0.11,22
172.16.0.20,25565
</pre>

The program will output something like:
<pre>
google.com:
	Port 80:		online
	Port 443:		online
172.16.0.11:
	Port 22:		online
172.16.0.20:
	Port 25565:		online

Network healthy.
</pre>

Or alternatively:
<pre>
google.com:
	Port 80:		online
	Port 443:		online
172.16.0.11:
	Port 22:		offline
172.16.0.20:
	Port 25565:		online

There are 1 unreachable services in your network.
</pre>

I use this program on a Raspberry Pi outside of my network to send me text notifications with [PushSafer](https://www.pushsafer.com/). This setup is easily replicatable with a cron job every 15 minutes that greps for "unreachable" and triggers a PushSafer call if a value is returned.

# To-Do
 - Add support for users to specify where the `hosts.csv` file is via a command line argument
 - Add support to run a script if numDownHosts > 0
