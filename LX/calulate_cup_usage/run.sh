#!/bin/bash

#adb disconnect
#adb connect 10.239.51.8
#adb root
#adb connect 10.239.51.8


cat list|while read line
do
    PKG=`aapt d badging ./APK/${line}|grep package|awk -F "'" '{print $2}'`
    P=`adb shell ps|grep ${PKG} |awk '{print $2}'`
    if [[ $P != ""  ]]
    then
        adb shell kill $P
    fi
done
