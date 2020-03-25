#!/usr/bin/python

import os

# Runs NetStatus, checks to see if any services are down, and sends pushSafer.sh notification if so
returnString = os.popen("/usr/bin/java -jar /usr/local/share/scripts/NetStatus-1.3.jar").read()

# If NetStatus returns down services, send notification. Else, print "Network healthy"
if (returnString != "Network healthy.\n"):
	os.system("/usr/local/share/scripts/pushSafer.sh '{0}'".format(returnString))
	print returnString
else:
	print returnString
