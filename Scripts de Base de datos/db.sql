-- Crear la base de datos
DROP DATABASE IF EXISTS proyecto_mobiles;
-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS proyecto_mobiles;

-- Seleccionar la base de datos
USE proyecto_mobiles;

-- Crear la tabla usuarios
CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(255) NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL
);

-- Crear la tabla catalogo
CREATE TABLE catalogo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre_producto VARCHAR(255) NOT NULL,
    imagenes MEDIUMBLOB,
    precio DECIMAL(10, 2) NOT NULL,
    categoria VARCHAR(50) NOT NULL
);

-- Crear la tabla pedidos con campos de Latitud y Longitud
CREATE TABLE pedidos (
    id_pedido INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT,
    id_repartidor INT,
    latitud Float,
    longitud Float,
    Estado VARCHAR(50),
    latitudC Float,
    longitudC Float,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_repartidor) REFERENCES usuarios(id)
);

-- Crear la tabla detalles_pedido
CREATE TABLE detalles_pedido (
    id_detalle INT PRIMARY KEY AUTO_INCREMENT,
    id_pedido INT,
    id_producto INT,
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES catalogo(id)
);

INSERT INTO usuarios (usuario, contraseña, rol) VALUES ('genericsuser', 'genericsuser', 'genericsuser');
-- Empleado 7
INSERT INTO usuarios (usuario, contraseña, rol) VALUES ('daniel_gutierrez', 'empleado123', 'empleado');

-- Empleado 8
INSERT INTO usuarios (usuario, contraseña, rol) VALUES ('clara_molina', 'claveempleado456', 'empleado');

-- Empleado 9
INSERT INTO usuarios (usuario, contraseña, rol) VALUES ('alejandro_ramirez', '123456empleado', 'empleado');

-- Cliente 7
INSERT INTO usuarios (usuario, contraseña, rol) VALUES ('patricia_sanchez', 'cliente789', 'cliente');

-- Cliente 8
INSERT INTO usuarios (usuario, contraseña, rol) VALUES ('juan_gomez', 'gomezcliente123', 'cliente');

-- Cliente 9
INSERT INTO usuarios (usuario, contraseña, rol) VALUES ('natalia_rojas', 'cliente456', 'cliente');
-- Productos en el catálogo (sección bebidas)
INSERT INTO catalogo (nombre_producto, imagenes, precio, categoria) 
VALUES 
('Botella de agua', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\botella_agua.jpeg'), 13.99, 'bebida'),

('Jugo de naranja', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\jugo_naranja.jpeg'), 16.50, 'bebida'),

('Coca cola', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\coca_cola.jpeg'), 14.75, 'bebida'),

('Pepsi', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\pepsi.jpeg'), 15.25, 'bebida'),

('Sprite', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\sprite.jpeg'), 17.99, 'bebida');

-- Productos en el catálogo (sección ensaladas)
INSERT INTO catalogo (nombre_producto, imagenes, precio, categoria) 
VALUES 
('Ensalada de pasta o arroz', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\epasta.jpg'), 59.99, 'ensalada'),

('Ensalada de patata', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\epatata.jpg'), 65.99, 'ensalada'),

('Ensalada mixta', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\emixta.jpg'), 79.99, 'ensalada'),

('Ensalada Caprese', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\ecaprese.jpg'), 69.99, 'ensalada'),

('Ensalada Waldorf', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\ewaldorf.jpg'), 84.99, 'ensalada'),

('Ensalada rusa', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\erusa.jpg'), 63.99, 'ensalada'),

('Ensalada griega', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\egriega.jpg'), 66.99, 'ensalada'),

('Ensalada de col', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\ecol.jpg'), 60.99, 'ensalada');

-- Productos en el catálogo (sección sándwiches)
INSERT INTO catalogo (nombre_producto, imagenes, precio, categoria) 
VALUES 
('Hamburguesa Clásica', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\hclasica.jpeg'), 10.99, 'sándwich'),

('Hamburguesa de Pollo', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\hpollo.jpeg'), 12.99, 'sándwich'),

('Hamburguesa Vegana', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\hvegana.jpeg'), 8.99, 'sándwich'),

('Hamburguesa de Pavo', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\hpavo.jpeg'), 11.99, 'sándwich'),

('Hamburguesa de Sushi', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\hsushi.jpeg'), 14.99, 'sándwich'),

('Hamburguesa Mexicana', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\hmexicana.jpeg'), 9.99, 'sándwich'),

('Hamburguesa de Salmón', LOAD_FILE('C:\\Users\\eriks\\Documents\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\hsalmon.jpeg'), 15.99, 'sándwich');


