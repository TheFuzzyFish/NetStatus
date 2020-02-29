# NetStatus
A small Java application to monitor your network and report of any down services.
Reads a list of hostnames and ports from a CSV file, then checks each individual port for connectivity.

# Installation
You can either clone this repository and compile the code yourself, or you can just download my precompiled .jar file from /NetStatus-1.0.jar

# Usage
The program reads from ~/hosts.csv by default, but you can easily change that value in src/Main.java on line 12.
The CSV file should read 'hostname,port,port,port' with up to 256 hosts and 256 ports each. For example:
`google.com,80,443
172.16.0.11,22
172.16.0.20,25565`

The program will output something like:
`google.com:
	Port 80:		online
	Port 443:		online
172.16.0.11:
	Port 22:		online
172.16.0.20:
	Port 25565:		online

Network healthy.`

Or alternatively:
`google.com:
	Port 80:		online
	Port 443:		online
172.16.0.11:
	Port 22:		offline
172.16.0.20:
	Port 25565:		online

There are 1 unreachable services in your network.`

This program could relatively easily be used to send you notifications via a service like [PushSafer](https://www.pushsafer.com/) in the form of a crontab and `grep "unreachable services in your network"`

