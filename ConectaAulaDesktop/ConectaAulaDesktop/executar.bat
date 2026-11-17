@echo off
chcp 65001 >nul
java -jar "%~dp0ConectaAula.jar"
if errorlevel 1 (
  echo.
  echo Nao foi possivel iniciar. Verifique se o Java 17 ou superior esta instalado.
  pause
)
