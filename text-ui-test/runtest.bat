@ECHO OFF

REM create bin directory if it doesn't exist
if not exist ..\bin mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.TXT del ACTUAL.TXT

REM delete test data file from previous run (separate from real user data)
if exist ..\data\julius-test.txt del ..\data\julius-test.txt

REM compile the code into the bin folder
javac  -cp ..\src\main\java -Xlint:none -d ..\bin ..\src\main\java\julius\*.java ..\src\main\java\julius\command\*.java ..\src\main\java\julius\exception\*.java ..\src\main\java\julius\parser\*.java ..\src\main\java\julius\storage\*.java ..\src\main\java\julius\task\*.java ..\src\main\java\julius\ui\*.java
IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    exit /b 1
)
REM no error here, errorlevel == 0

REM run the program with a dedicated test data file so real user data is untouched
java -classpath ..\bin julius.Julius ..\data\julius-test.txt < input.txt > ACTUAL.TXT

REM compare the output to the expected output
FC ACTUAL.TXT EXPECTED.TXT
