#!/bin/sh

ap_id=$1
hostname=$2
path=$3

#load and apply wifiap and wifidog setting
/usr/bin/wget -t 3 -T 20 "http://$hostname/$path/getAPScript?ap_id=$ap_id&script=wifiap_configure.sh" -O /tmp/wifiap/wifiap_configure.sh
/bin/chmod +x /tmp/wifiap/wifiap_configure.sh  
/bin/sh /tmp/wifiap/wifiap_configure.sh $ap_id $hostname $path

#load and apply whitelist setting
/usr/bin/wget -t 3 -T 20 "http://$hostname/$path/getAPScript?ap_id=$ap_id&script=whitelist_configure.sh" -O /tmp/wifiap/whitelist_configure.sh
/bin/chmod +x /tmp/wifiap/whitelist_configure.sh  
/bin/sh /tmp/wifiap/whitelist_configure.sh $ap_id $hostname $path

