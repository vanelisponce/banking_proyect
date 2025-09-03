@echo off
echo Construyendo microservicios...

echo Construyendo cliente-service...
cd cliente-service
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo Error compilando cliente-service
    pause
    exit /b 1
)
cd ..

echo Construyendo cuenta-service...
cd cuenta-service
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo Error compilando cuenta-service
    pause
    exit /b 1
)
cd ..

echo Construccion completada!

echo Iniciando servicios con Docker Compose...
docker-compose up --build -d

echo Servicios iniciados!
echo Cliente Service: http://localhost:8081
echo Cuenta Service: http://localhost:8082
echo RabbitMQ Management: http://localhost:15672
echo MySQL: localhost:3306

pause