-- Crear base de datos para cliente-service
CREATE DATABASE IF NOT EXISTS cliente_db;
USE cliente_db;

-- Tabla personas (superclase)
CREATE TABLE personas (
    persona_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(1) NOT NULL,
    edad INT NOT NULL,
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(200),
    telefono VARCHAR(15)
);

-- Tabla clientes (hereda de personas)
CREATE TABLE clientes (
    persona_id BIGINT PRIMARY KEY,
    cliente_id BIGINT NOT NULL UNIQUE,
    contraseña VARCHAR(100) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (persona_id) REFERENCES personas(persona_id)
);

-- Crear base de datos para cuenta-service
CREATE DATABASE IF NOT EXISTS cuenta_db;
USE cuenta_db;

-- Tabla cuentas
CREATE TABLE cuentas (
    cuenta_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(20) NOT NULL,
    saldo_inicial DECIMAL(18,2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id BIGINT NOT NULL
);

-- Tabla movimientos
CREATE TABLE movimientos (
    movimiento_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor DECIMAL(18,2) NOT NULL,
    saldo DECIMAL(18,2) NOT NULL,
    cuenta_id BIGINT NOT NULL,
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(cuenta_id)
);

-- Tabla para sincronización de información de clientes (en cuenta-service)
CREATE TABLE cliente_info (
    cliente_id BIGINT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    identificacion VARCHAR(20) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_sincronizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Datos de prueba para cliente-service
USE cliente_db;
INSERT INTO personas (nombre, genero, edad, identificacion, direccion, telefono) VALUES
('Jose Lema', 'M', 30, '1234567890', 'Otavalo sn y principal', '098254785'),
('Marianela Montalvo', 'F', 25, '0987654321', 'Amazonas y NNUU', '097548965'),
('Juan Osorio', 'M', 35, '1122334455', '13 junio y Equinoccial', '098874587');

INSERT INTO clientes (persona_id, cliente_id, contraseña, estado) VALUES
(1, 1, '1234', TRUE),
(2, 2, '5678', TRUE),
(3, 3, '1245', TRUE);

-- Datos de prueba para cuenta-service
USE cuenta_db;
INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id) VALUES
('478758', 'Ahorro', 2000.00, TRUE, 1),
('225487', 'Corriente', 100.00, TRUE, 2),
('495878', 'Ahorros', 0.00, TRUE, 3),
('496825', 'Ahorros', 540.00, TRUE, 2);

-- Sincronizar información de clientes
INSERT INTO cliente_info (cliente_id, nombre, identificacion, estado) VALUES
(1, 'Jose Lema', '1234567890', TRUE),
(2, 'Marianela Montalvo', '0987654321', TRUE),
(3, 'Juan Osorio', '1122334455', TRUE);

-- Indices para mejorar performance
CREATE INDEX idx_clientes_cliente_id ON clientes(cliente_id);
CREATE INDEX idx_clientes_identificacion ON personas(identificacion);
CREATE INDEX idx_cuentas_numero ON cuentas(numero_cuenta);
CREATE INDEX idx_cuentas_cliente ON cuentas(cliente_id);
CREATE INDEX idx_movimientos_cuenta ON movimientos(cuenta_id);
CREATE INDEX idx_movimientos_fecha ON movimientos(fecha);