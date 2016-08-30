#/bin/sh

hostname=$hostname
path=$path
ap_id=$acid

/bin/sleep 5


#internet white list
inet_whitelist()
{    
    if [ ! -e /tmp/uwifi/internet_whitelist.txt ]
    then
        exit 1
    fi
        
    while read item
    do
        /bin/echo "/usr/sbin/iptables -I WiFiDog_br-lan_Global -d $item -j ACCEPT">>/tmp/uwifi/iptables.sh
        /bin/echo "/usr/sbin/iptables -t nat -I WiFiDog_br-lan_Global -d $item -j ACCEPT">>/tmp/uwifi/iptables.sh
    done</tmp/uwifi/internet_whitelist.txt
}


/bin/echo "#!/bin/sh">/tmp/uwifi/iptables.sh

#wget internet whitelist from auth server
/usr/bin/wget  -t 3 -T 30 "http://$hostname/$path/acScript/getACDomainRules?acid=$acid&type=0" -O /tmp/uwifi/internet_whitelist.txt
if [ "$?" = "0" ]
then
    inet_whitelist
fi

#update whitelist
/bin/chmod +x /tmp/uwifi/iptables.sh
/bin/sleep 5
/bin/sh /tmp/uwifi/iptables.sh 

