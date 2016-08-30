#Paste the following script and change the $hostname,$path,$acid into really value
#this can apply to dd-wrt and openwrt router os

/bin/mkdir -p /tmp/uwifi

auth="1"

#code begin 
if [ "$auth" = "1" ]
then
	res=1
	while [ $res -ne 0 ]
	do
		/usr/bin/wget -t 3 -T 20 "http://$hostname/$path/acScript/getLoaderScript?acid=$acid&acos=$acos" -O /tmp/uwifi/uwifi_loader.sh
		res=$?
		if [ $res -eq 0 ]
		then
			read fl</tmp/uwifi/uwifi_loader.sh
			if [ "$fl" = "#/bin/sh" ]
			then
				res=0
			else
				res=1
				/bin/sleep 10
			fi
		else
			/bin/sleep 10
		fi
	done
	/bin/chmod +x /tmp/uwifi/uwifi_loader.sh
	/bin/sh /tmp/uwifi/uwifi_loader.sh &
fi
#code end

exit 0