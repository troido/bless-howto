#!/bin/bash

echo "Finding SNAPSHOT libraries..."
# Find word SNAPSHOT(case insensitive) in gradle files and save the output into the variable.
PROJECT_GRADLE="$(egrep -oi '.*snapshot' build.gradle app/build.gradle)"
# If the variable is empty then It's Ok, otherwise print the output and exit with error
if [ -z "$PROJECT_GRADLE" ]; then
  echo Snapshot versions have not been found. It\'s OK
else
  echo "Detected Snapshots: $PROJECT_GRADLE"
  echo Found Snapshots in the app. You should not use snapshot version in this app
  exit 1
fi