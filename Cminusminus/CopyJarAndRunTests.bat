@echo off

echo -----------COPY EXEC AND RUN TESTS--------------

call copy /Y .\\dist\\Cminusminus.jar .\\..\\TestFiles

echo -----------EXEC TESTS--------------
CD ..
CD TestFiles

call TESTALL.bat

echo ----------- FINISHED -------------