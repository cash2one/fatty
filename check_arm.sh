#!/bin/bash
#set -x

if [ -e temp.txt -o -e result.txt -o -e arm.txt ];then
	rm -rf temp.txt
	rm -rf result.txt
	rm -rf arm.txt
fi

adb root  
adb remount

adb shell ls /data/app/ > temp.txt
apk=`cat temp.txt | awk '{print $1}' | tr -d "\r"`

for i in $apk
do
	lib=`adb shell ls /data/app/${i}/lib/`
	echo "${i},${lib}" >> result.txt
	lib_so=`cat result.txt | tail -n 1 | awk -F"," '{print $2}' | tr -d "\r"`
	if [ "$lib_so" = "arm" ];then
		echo "${i},${lib_so}" >> arm.txt
	fi
done
