#/bin/sh
#=======================================================#
# Author: Hugh Zhang                                    #
# Date  : 2014-11-18                                    #
# Desc  : implement uwifi gw speed limit                #
#=======================================================#
total_upload=$totalUpload
total_download=$totalDownload
speed=$clientSpeed

ipd=`uci show network.lan.ipaddr | /usr/bin/awk -F= '{print $2}' | /usr/bin/awk -F. '{print $1"."$2"."$3}'`
istar="`uci get dhcp.lan.start`"
ilimit="`uci get dhcp.lan.limit`"
range=`expr $istar + $ilimit`
echo $range
/bin/echo "`date`:speed is $speed"
insmod cls_u32
insmod em_u32
insmod act_connmark
insmod act_mirred
insmod sch_ingress
insmod cls_fw
insmod sch_htb
insmod sch_sfq

ifconfig eth0 up
tc qdisc del dev ifb0 root 2> /dev/null 
tc qdisc add dev ifb0 root handle 1: htb default 1
tc class add dev ifb0 parent 1: classid 1:10 htb rate ${total_upload}kbit ceil ${total_upload}kbit 

ifconfig ifb0 up
tc qdisc del dev br-lan root 2> /dev/null
tc qdisc add dev br-lan root handle 1: htb default 1
tc class add dev br-lan parent 1: classid 1:1 htb rate ${total_download}kbit ceil ${total_download}kbit 
tc qdisc del dev br-lan ingress
tc qdisc add dev br-lan ingress
tc filter add dev br-lan parent ffff: protocol ip prio 1 u32 match u32 0 0 flowid 1:1 action mirred egress redirect dev ifb0
/bin/echo "init limit speed ok" 
i=$istar
while [ $i -lt $range ]
do
{
	#echo $i 
	ip=$ipd"."$i
	tc class add dev ifb0  parent 1:1 classid 1:$i htb rate ${speed}kbit ceil ${speed}kbit
	tc qdisc add dev ifb0 parent 1:$i handle 1$i: sfq perturb 2 limit 80000 
	tc filter add dev ifb0 parent 1: protocol ip u32 match ip src $ip/32 flowid 1:$i

	tc class add dev br-lan  parent :1 classid 1:$i htb rate ${speed}kbit ceil ${speed}kbit
	tc qdisc add dev br-lan parent 1:$i handle 1$i: sfq perturb 2 limit 80000 
	tc filter add dev br-lan parent 1: protocol ip u32 match ip dst $ip/32 flowid 1:$i
	let i++
}
done
/bin/echo "`date`:limit speed ok" 