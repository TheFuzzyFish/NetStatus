#!/bin/bash

title="$HOSTNAME"
body="$1"
(/usr/bin/curl https://www.pushsafer.com/api -X POST --silent --output /dev/null -d k="FIXME" -d t="$title" -d m="$body" &)
# FIXME: Insert your PushSafer API key into the FIXME above                            ^^^^^
