@echo off

echo Running auth-server/buildDocker.bat
cd auth-server
call buildDocker.bat
cd ..

echo Running credits-service/buildDocker.bat
cd credits-service
call buildDocker.bat
cd ..

echo Running discovery-server/buildDocker.bat
cd discovery-server
call buildDocker.bat
cd ..

echo Running gateway/buildDocker.bat
cd gateway
call buildDocker.bat
cd ..

echo Running interest-service/buildDocker.bat
cd interest-service
call buildDocker.bat
cd ..

echo Running payment-service/buildDocker.bat
cd payment-service
call buildDocker.bat
cd ..

pause
