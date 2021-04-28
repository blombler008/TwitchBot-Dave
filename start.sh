#!/bin/sh
jar="TwitchBot-1.1.1.jar"
args="--discord --web"
flags="-Xms4G -Xmx4G -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:MaxGCPauseMillis=100 -XX:+DisableExplicitGC -XX:TargetSurvivorRatio=90 -XX:G1NewSizePercent=50 -XX:G1MaxNewSizePercent=80 -XX:G1MixedGCLiveThresholdPercent=35 -XX:+AlwaysPreTouch -XX:+ParallelRefProcEnabled -Dusing.aikars.flags=mcflags.emc.gs"
basedir="$(dirname $(realpath $0))"

function loop() {
    java $flags -server -jar "$basedir/$jar" $args
    RESULT=$?
    if [ $RESULT -eq 0 ];  then
        exit 0;
    else
        exit 1;
	 fi
}

loop | tee output.txt