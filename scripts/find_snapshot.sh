#!/bin/bash

echo "Finding SNAPSHOT libraries"
PROJECT_GRADLE="$(egrep -co '.*snapshot' build.gradle)"
APP_MODULE_GRADLE="$(egrep -co '.*snapshot' app/build.gradle)"
if [ $PROJECT_GRADLE -eq 0 ] && [$APP_MODULE_GRADLE -eq 0]; then
  echo Snapshot versions have not been found. It\'s OK
else
  echo Found Snapshots in the app. You should not use snapshot version in this app
  exit 1
fi