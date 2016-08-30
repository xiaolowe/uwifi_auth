#/bin/sh

hostname=$hostname
path=$path
acid=$acid
acos=$acos

/bin/rm -f /tmp/uwifi
/bin/mkdir -p /tmp/uwifi
   
#wifidog.conf config
/usr/bin/wget -t 3 -T 20 "http://$hostname/$path/acScript/getConfigScript?acid=$acid&acos=$acos" -O /tmp/uwifi/config_load.sh
/bin/sh /tmp/uwifi/config_load.sh


/bin/sleep 5
#white list setting
/usr/bin/wget -t 3 -T 20 "http://$hostname/$path/acScript/getWhiteListScript?acid=$acid&acos=$acos" -O /tmp/uwifi/whitelist_load.sh
/bin/sh /tmp/uwifi/whitelist_load.sh
/bin/sleep 5

#speed limited
/usr/bin/wget -t 3 -T 10 "http://$hostname/$path/acScript/getQosScript?acid=$acid&acos=$acos" -O /tmp/uwifi/qos_load.sh
/bin/sh /tmp/uwifi/qos_load.sh