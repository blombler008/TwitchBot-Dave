@echo off
SET jar="TwitchBot-1.1.1.jar"
SET args="--discord"
SET flags="-Xms4G -Xmx4G -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions -XX:MaxGCPauseMillis=100 -XX:+DisableExplicitGC -XX:TargetSurvivorRatio=90 -XX:G1NewSizePercent=50 -XX:G1MaxNewSizePercent=80 -XX:G1MixedGCLiveThresholdPercent=35 -XX:+AlwaysPreTouch -XX:+ParallelRefProcEnabled -Dusing.aikars.flags=mcflags.emc.gs"
SET basedir=%~dp0

color 0A
title TwitchBot
cls

SET commandArgs=
(for %%a in (%args%) do ( 
   SET commandArgs=%commandArgs% %%a
))


:start
echo Loading server...

java "%flags%" -server -jar "%basedir%\%jar%" %commandArgs%
TIMEOUT /T 5
cls

goto start