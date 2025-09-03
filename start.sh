#!/bin/bash
echo "Iniciando servicios..."
docker-compose up -d
echo "Servicios iniciados!"
echo "Cliente Service: http://localhost:8081"
echo "Cuenta Service: http://localhost:8082"
echo "RabbitMQ Management: http://localhost:15672"
echo "MySQL: localhost:3306"