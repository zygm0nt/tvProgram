#!/bin/sh

prefix="~/kingstone_ext_store/android-sdk-linux/tools/"
project=tvProgramTest

case $1 in
    create)
        # create test project 
        $prefix/android create test-project -m .. -n $project -p $project
        ;;
    update) 
        # update test project
        $prefix/android update test-project -m .. -p $project
        ;;
    run)
        # run tests
        $prefix/adb shell am instrument -w $test_package/$runner_class
        ;;
esac
