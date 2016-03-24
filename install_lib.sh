#!/bin/bash
rm -rf lib_result
while read line
do
	adb root
	m=$(adb shell ls /data/app/$line-1/lib)
	echo "$m" >> lib_result
done < lib_list
	#if ls == "arm"
     # echo ("This app install arm lib")