@echo off
REM Run server from NetBeans build output
setlocal
set CP=dist/ThiTracNghiem.jar;dist/lib/*
java -cp %CP% server.Server
endlocal
