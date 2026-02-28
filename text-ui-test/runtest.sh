#!/usr/bin/env bash

# create bin directory if it doesn't exist
if [ ! -d "../bin" ]
then
    mkdir ../bin
fi

# delete output from previous run
if [ -e "./ACTUAL.TXT" ]
then
    rm ACTUAL.TXT
fi

# delete test data file from previous run (separate from real user data)
if [ -e "../data/julius-test.txt" ]
then
    rm ../data/julius-test.txt
fi

# compile the code into the bin folder, terminates if error occurred
if ! javac -cp ../src/main/java -Xlint:none -d ../bin ../src/main/java/julius/*.java ../src/main/java/julius/command/*.java ../src/main/java/julius/exception/*.java ../src/main/java/julius/parser/*.java ../src/main/java/julius/storage/*.java ../src/main/java/julius/task/*.java ../src/main/java/julius/ui/*.java
then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# run the program with a dedicated test data file so real user data is untouched
java -classpath ../bin julius.Julius ../data/julius-test.txt < input.txt > ACTUAL.TXT

# convert to UNIX format
cp EXPECTED.TXT EXPECTED-UNIX.TXT
dos2unix ACTUAL.TXT EXPECTED-UNIX.TXT

# compare the output to the expected output
diff ACTUAL.TXT EXPECTED-UNIX.TXT
if [ $? -eq 0 ]
then
    echo "Test result: PASSED"
    exit 0
else
    echo "Test result: FAILED"
    exit 1
fi