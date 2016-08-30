#/bin/sh

writeconfig="1"

hostname=$hostname
path=$path
acid=$acid
acos=$acos
lease_time=$leasetime

/etc/init.d/wifidog restart

if [ "$writeconfig" = "1" ]
then           
    /usr/bin/wget -t 3 -T 20  "http://$hostname/$path/acScript/getWifidogConfigScript?acid=$acid&acos=$acos" -O /tmp/uwifi/wifidog.conf

    if [ "$?" = "0" ]
    then
        #md5 compare config file is updated
        md51="`/usr/bin/md5sum /etc/wifidog.conf|/usr/bin/awk '{print $1}'`"
        md52="`/usr/bin/md5sum /tmp/uwifi/wifidog.conf|/usr/bin/awk '{print $1}'`"
        if [ "$md51" != "$md52" ]
        then
            /bin/cp -f /tmp/uwifi/wifidog.conf /etc/wifidog.conf
            /bin/sleep 1
            killall wifidog
            /bin/sleep 5
            /etc/init.d/wifidog start
            /bin/sleep 5
        fi
    fi
fi

dhcpd_limit="`uci get dhcp.lan.limit`"
if [ "0" != "$dhcpd_limit" ] 
then
    dhcp_limit_flag=1
    uci set dhcp.lan.limit=100
    uci commit
fi

dhcpd_lease_time="`uci get dhcp.lan.leasetime`"
if [ "$dhcpd_lease_time" != "$lease_time" ]
then
	lease_time_flag=1
    uci set dhcp.lan.leasetime=`echo $lease_time"m"`
    uci commit
fi
if [ "$dhcp_limit_flag" = "1" -o "$lease_time_flag" != "1" ]
then
    /etc/init.d/network restart
    /bin/sleep 3
fi
