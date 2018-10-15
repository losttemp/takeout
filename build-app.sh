#!/bin/bash
echo "==============begin compile apk=============="

ls $ANDROID_HOME/platforms
java -version
gradle -version

echo "readIcodeNumber start"
git fetch origin refs/notes/commits:refs/notes/commits
git notes show | grep 'changeset:' | awk '{print $2}'
git_icode_number=`git notes show | grep 'changeset:' | head -1 | awk '{print $2}'`

echo $git_icode_number

export GIT_ICODE_NUMBER=$git_icode_number

echo $GIT_ICODE_NUMBER
echo "readIcodeNumber end"

echo "gradle project"
rm -rf ./build
rm -rf ./app/build

gradle clean
gradle readBuildNumber

gradle app:assembleDebug --stacktrace|| exit 1

gradle app:assembleRelease --stacktrace|| exit 1

ls -l ./app/build/outputs/apk/
cp ./app/build/outputs/apk/*.apk ./output/

ls -l ./output/

echo "==============end compile apk=============="
