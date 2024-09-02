#!/bin/sh
exec java -Denvironment=production -server ${JAVA_OPTS} -jar app.jar