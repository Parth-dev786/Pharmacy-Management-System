@echo off
setlocal
cd /d "%~dp0"
where javac >nul 2>nul
if errorlevel 1 (
  echo JDK compiler javac was not found. Check that JDK 24 bin is on PATH.
  pause
  exit /b 1
)
if not exist out mkdir out
javac -encoding UTF-8 -d out src\com\college\pharmacy\*.java
if errorlevel 1 (
  echo Compilation failed. Please check the error messages above.
  pause
  exit /b 1
)
java -cp out com.college.pharmacy.PharmacyApp
