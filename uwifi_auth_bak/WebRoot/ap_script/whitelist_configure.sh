#!/bin/sh
#designed by zhanghw at 2014/5/15
#version  0.9
hostname=$hostname
path=$path
ap_id=$acid

mac_whitelist_config()
{
        trustedchain="WiFiDog_`/usr/sbin/nvram get lan_ifname`_Trusted"
        
        if [ ! -e /tmp/uwifi/mac_whitelist.txt ]
        then
                exit 1
        fi
        /bin/sed 's/ /\n/g' /tmp/uwifi/mac_whitelist.txt>/tmp/uwifi/mac_whitelist.tmp
        
        while read line
        do
                /bin/echo $line
                if [ "$line" != "" ]   
                then                                                    
                        /bin/echo "/usr/sbin/iptables -t mangle -A $trustedchain -m mac --mac-source $line -j MARK --set-mark 2">>/tmp/uwifi/iptables.sh
                fi
        done</tmp/uwifi/mac_whitelist.tmp
}

inet_whitelist_config()
{
        globalchain="WiFiDog_`/usr/sbin/nvram get lan_ifname`_Global"
        
        if [ ! -e /tmp/uwifi/internet_whitelist.txt ]
        then
                exit 1
        fi
        while read line
        do
                /bin/echo "/usr/sbin/iptables -t filter -A $globalchain -d $line -j ACCEPT">>/tmp/uwifi/iptables.sh
                /bin/echo "/usr/sbin/iptables -t nat -A $globalchain -d $line -j ACCEPT">>/tmp/uwifi/iptables.sh
        done</tmp/uwifi/internet_whitelist.txt
}

#main

if [ ! -d "/tmp/uwifi/" ]
then
        mkdir -p /tmp/uwifi/
fi

#remove old whitelist
#/bin/rm -f /tmp/uwifi/iptables.sh
/bin/echo "#!/bin/sh">/tmp/uwifi/iptables.sh

#wget mac whitelist from auth server
/usr/bin/wget  -t 3 -T 10 "http://$hostname/$path/acScript/getACMacRules?acid=$acid&type=0" -O /tmp/uwifi/mac_whitelist.txt
if [ "$?" = "0" ]
then
        mac_whitelist_config
fi

#wget internet whitelist from auth server
/usr/bin/wget  -t 3 -T 10 "http://$hostname/$path/acScript/getACDomainRules?acid=$acid&type=0" -O /tmp/uwifi/internet_whitelist.txt
if [ "$?" = "0" ]
then
        inet_whitelist_config
fi

#update whitelist
/bin/chmod +x /tmp/uwifi/iptables.sh
/bin/sleep 10
/bin/sh /tmp/uwifi/iptables.sh 