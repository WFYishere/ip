@echo off
setlocal

rem Clean previous ACTUAL.TXT if exists
del text-ui-test\ACTUAL.TXT 2> NUL

rem Compile into out/
if not exist out mkdir out
javac -cp src\main\java -d out src\main\java\*.java

rem Run with redirected I/O
java -classpath out Quokka < text-ui-test\input.txt > text-ui-test\ACTUAL.TXT

rem Compare using fc; suppress diff output
fc text-ui-test\EXPECTED.TXT text-ui-test\ACTUAL.TXT > NUL
if %errorlevel%==0 (
  echo All tests passed
) else (
  echo Tests failed
  exit /b 1
)

endlocal
