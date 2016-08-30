#!/bin/sh
#designed by zhanghw at 2014/3/31

hostname=$hostname
path=$path
acid=$acid
acos=$acos

writeconfig="0"

wifidog_config()
{
	if [ ! -e /tmp/uwifi/wifidog.txt ]
	then
		exit 1
	fi
	
	while read line
	do
		key=`/bin/echo $line  | awk -F "=" '{ print $1 }'`
		value=`/bin/echo $line  | awk -F "=" '{ print $2 }'`
		echo $key":"$value
		
		if [ "`/usr/sbin/nvram get $key`" != "$value" ]
		then
			/usr/sbin/nvram set $key=$value
			writeconfig="1"
		fi
	done</tmp/uwifi/wifidog.txt
	
	if [ "$writeconfig" = "1" ]
	then
		/usr/sbin/nvram commit
	fi
}

#main

if [ ! -d "/tmp/uwifi/" ]
then
	mkdir -p /tmp/uwifi/
fi

#wget wifidog configure from web server
/usr/bin/wget  -t 3 -T 10 "http://$hostname/$path/acScript/getWifidogConfigScript?acid=$acid&acos=$acos" -O /tmp/uwifi/wifidog.txt
#/bin/cat /tmp/uwifi/wifidog.txt
#setting wifidog configure
if [ "$?" = "0" ]
then
	wifidog_config
fi
