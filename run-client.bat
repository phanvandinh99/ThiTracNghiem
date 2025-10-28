@echo off
REM Run client from NetBeans build output
setlocal
set CP=dist/ThiTracNghiem.jar;dist/lib/*
java -cp %CP% client.formClient
endlocal
