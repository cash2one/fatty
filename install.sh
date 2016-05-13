!#/bin/bash
i=1
while read line
do
  adb install $line.apk
  echo $((i++))
done < list
