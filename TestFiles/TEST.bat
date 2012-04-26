@ECHO off

IF "%1"=="" GOTO ERROR

ECHO ====TESTING %1====

ECHO == RUNNING java -jar Cminusminus.jar %1 ==
java -jar Cminusminus.jar %1

SLEEP 1

ECHO == RUNNING java -cp ./;%~dp1 %~n1 ==

java -cp ./;%~dp1 %~n1


ECHO ==== FINISHED %1====

GOTO END

:ERROR
ECHO usage Test.bat <file to test>

:END
ECHO.
