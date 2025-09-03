# 🏦 Banking Microservices Project

Sistema bancario distribuido basado en microservicios con Spring Boot, MySQL y RabbitMQ para gestión de clientes, cuentas y transacciones bancarias.

## 🏗️ Arquitectura

Este proyecto implementa una arquitectura de microservicios con:

- **Cliente Service** (Puerto 8081): Gestión de clientes y personas
- **Cuenta Service** (Puerto 8082): Gestión de cuentas y movimientos bancarios
- **MySQL**: Base de datos relacional con esquemas separados
- **RabbitMQ**: Mensajería asíncrona entre servicios
- **Docker**: Containerización de infraestructura

## 🔧 Tecnologías

- **Backend**: Spring Boot 3.1.5, Java 17
- **Base de datos**: MySQL 8.0
- **Mensajería**: RabbitMQ 3
- **ORM**: JPA/Hibernate
- **Build**: Maven
- **Containerización**: Docker & Docker Compose
- **Validation**: Bean Validation
- **Mapping**: ModelMapper

## 📋 Prerrequisitos

- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- Git

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/vanelisponce/banking_proyect.git
cd banking_proyect
```

### 2. Configuración de la infraestructura

El proyecto incluye archivos de configuración para levantar MySQL y RabbitMQ automáticamente:

```bash
# Dar permisos a los scripts
chmod +x start.sh stop.sh test.sh

# Levantar toda la infraestructura y servicios
./start.sh
```

### 3. Configuración manual (opcional)

Si prefieres configurar paso a paso:

```bash
# 1. Levantar infraestructura (MySQL + RabbitMQ)
docker-compose -f docker-compose.yml up -d

# 2. Compilar cliente-service
cd cliente-service
./mvnw clean package -DskipTests

# 3. Compilar cuenta-service  
cd ../cuenta-service
./mvnw clean package -DskipTests

# 4. Ejecutar servicios
cd ../cliente-service
java -jar target/cliente-service-0.0.1-SNAPSHOT.jar &

cd ../cuenta-service  
java -jar target/cuenta-service-0.0.1-SNAPSHOT.jar &
```

## 📊 Base de Datos

### Esquema Cliente DB
- **personas**: Información básica (nombre, género, edad, identificación, etc.)
- **clientes**: Hereda de personas, añade credenciales y estado

### Esquema Cuenta DB
- **cuentas**: Información de cuentas bancarias
- **movimientos**: Historial de transacciones
- **cliente_info**: Cache de información de clientes para sincronización

### Datos de prueba incluidos:

**Clientes:**
- Jose Lema (ID: 1, Pass: 1234)
- Marianela Montalvo (ID: 2, Pass: 5678)  
- Juan Osorio (ID: 3, Pass: 1245)

**Cuentas:**
- 478758 (Ahorro, $2000) - Cliente 1
- 225487 (Corriente, $100) - Cliente 2
- 495878 (Ahorros, $0) - Cliente 3
- 496825 (Ahorros, $540) - Cliente 2

## 🌐 Servicios y Puertos

| Servicio | Puerto | URL Base | Descripción |
|----------|--------|----------|-------------|
| Cliente Service | 8081 | http://localhost:8081 | Gestión de clientes |
| Cuenta Service | 8082 | http://localhost:8082 | Gestión de cuentas |
| MySQL | 3306 | localhost:3306 | Base de datos |
| RabbitMQ | 5672 | localhost:5672 | Message broker |
| RabbitMQ Management | 15672 | http://localhost:15672 | Panel admin (admin/admin123) |

## 🔍 Verificación del Sistema

```bash
# Verificar estado de todos los servicios
./test.sh

# Verificar servicios individualmente
curl http://localhost:8081/actuator/health  # Cliente Service
curl http://localhost:8082/actuator/health  # Cuenta Service

# Conectar a MySQL
docker exec -it banking_mysql mysql -u root -prootpassword

# Acceder a RabbitMQ Management
# http://localhost:15672 (admin/admin123)
```

## 📁 Estructura del Proyecto

```
banking_proyect/
├── cliente-service/           # Microservicio de clientes
│   ├── src/main/java/com/banking/cliente/
│   │   ├── controller/        # REST Controllers
│   │   ├── service/           # Business Logic
│   │   ├── repository/        # Data Access
│   │   ├── entity/            # JPA Entities
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── config/           # Configuration
│   │   ├── exception/        # Exception Handling
│   │   └── events/           # RabbitMQ Events
│   └── src/main/resources/
│       └── application.yml
├── cuenta-service/            # Microservicio de cuentas
│   └── [estructura similar]
├── scripts/
│   └── init.sql              # Script de inicialización DB
├── logs/                     # Logs de aplicación
├── docker-compose.yml
├── start.sh                  # Script de inicio
├── stop.sh                   # Script de parada
└── test.sh                  # Script de verificación
```

## 🧪 Testing

```bash
# Ejecutar tests en cliente-service
cd cliente-service
./mvnw test

# Ejecutar tests en cuenta-service  
cd cuenta-service
./mvnw test

# Tests de integración
./mvnw test -Dtest=*IntegrationTest
```

## 📝 API Endpoints (Planificados)

### Cliente Service (8081)
```
GET    /api/clientes           - Listar clientes
GET    /api/clientes/{id}      - Obtener cliente por ID
POST   /api/clientes           - Crear cliente
PUT    /api/clientes/{id}      - Actualizar cliente
DELETE /api/clientes/{id}      - Eliminar cliente
```

### Cuenta Service (8082)
```
GET    /api/cuentas            - Listar cuentas
GET    /api/cuentas/{numero}   - Obtener cuenta por número
POST   /api/cuentas            - Crear cuenta
PUT    /api/cuentas/{numero}   - Actualizar cuenta
GET    /api/movimientos        - Listar movimientos
POST   /api/movimientos        - Crear movimiento
```

## 🛠️ Scripts de Gestión

```bash
# Iniciar todos los servicios
./start.sh

# Verificar estado
./test.sh  

# Detener todos los servicios
./stop.sh
```

## 🐳 Docker

Los contenedores incluidos:
- `banking_mysql`: MySQL 8.0 con datos inicializados
- `banking_rabbitmq`: RabbitMQ con management plugin

```bash
# Ver logs de contenedores
docker logs banking_mysql
docker logs banking_rabbitmq

# Acceso directo a MySQL
docker exec -it banking_mysql mysql -u root -prootpassword
```

## 🔧 Troubleshooting

### Problemas comunes:

**Puerto ocupado:**
```bash
lsof -i :8081  # Ver qué usa el puerto
lsof -i :8082
```

**MySQL no conecta:**
```bash
docker logs banking_mysql
telnet localhost 3306
```

**Ver logs de aplicación:**
```bash
tail -f logs/cliente-service.log
tail -f logs/cuenta-service.log
```

## 🚀 Despliegue

### Desarrollo
```bash
./start.sh
```

### Producción
Configurar variables de entorno apropiadas en `application.yml` de cada servicio.

## 🤝 Contribuciones

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## 👨‍💻 Autor

**Vanelis Ponce** - [@vanelisponce](https://github.com/vanelisponce)

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ve el archivo [LICENSE](LICENSE) para más detalles.

---
