#!/bin/bash

PWD=`pwd`
LIST=${PWD}/list
TMP=${PWD}/tmp
RESULT=${PWD}/result
ACTION="ACTION=android.intent.action.MAIN"
AAPT="aapt d badging"
AM="adb shell am"
TOP="adb shell top -m 5 -s cpu -n 20"
echo -n "Please input the device's ip:"
read ip
ADB_CONNECT="adb connect ${ip}"
ADB_DISCONNECT="adb disconnect"

${ADB_DISCONNECT}
${ADB_CONNECT}
adb root
sleep 2
${ADB_CONNECT}
adb remount
sleep 1

rm -rf result/*
rm -rf tmp/*
 
./run.sh

i=0
while [[ i -le 2 ]]
do

    cat ${LIST}|while read line
    do
        #app=`${AAPT} ${PWD}/APK/${line}|grep "application: label"|awk -F "'" '{print $2}'`
        pkg=`${AAPT} ${PWD}/APK/${line}|grep package|awk -F "'" '{print $2}'`
        app=${pkg}
        activity=`${AAPT} ${PWD}/APK/${line}|grep launchable-activity|awk -F "'" '{print $2}'`

        echo ""
        echo "------------- Runing ${app} -------------"
        echo ""

        ${AM} start -a $ACTION -n ${pkg}/${activity}
        sleep 5

        ${TOP} |tee ${TMP}/${app}_${i}.log

        cat ${TMP}/${app}_${i}.log |grep ${pkg} |awk '{print $3}' > ${TMP}/${app}_${i}.csv
        sleep 1

        pid=`adb shell ps |grep ${pkg}|awk '{print $2}'`
        adb shell kill ${pid}
        sleep 3

        if [[ ! -f ${TMP}/result_${app}_tmp.csv ]]
        then
            cat ${TMP}/${app}_${i}.csv > ${TMP}/result_${app}_tmp.csv
        else
            paste -d , ${TMP}/result_${app}_tmp.csv ${TMP}/${app}_${i}.csv > ${RESULT}/result_${app}.csv
            cat ${RESULT}/result_${app}.csv > ${TMP}/result_${app}_tmp.csv
        fi
        echo ""
        echo "----------- Finished ${app} -----------"
        sleep 5
    done
    let i=i+1
    ./run.sh
done

