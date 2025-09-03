#!/bin/bash
echo "Ejecutando pruebas unitarias..."

# Pruebas cliente-service
echo "Ejecutando pruebas para cliente-service..."
cd cliente-service
./mvnw test
cd ..

# Pruebas cuenta-service
echo "Ejecutando pruebas para cuenta-service..."
cd cuenta-service
./mvnw test
cd ..

echo "Todas las pruebas completadas!"