# Proyecto Final: App para cafeteria

Proyecto final para la clase de Aplicaciones Moviles de ESCOM (ISC-2020) con el profesor Ulises Saldaña.

La Aplicacion incluye una API personalizada que se encarga de todas las funciones.

Aplicacion para una cafeteria que incluye las siguientes funciones:
- Registrar usuario (empleado/cliente)
- Historial de pedidos
- Carrito de compra
- Seleccionar ubicacion actual
- Crear y Eliminar pedidos



## Instalacion

Se proporcionaron en los archivos un script para crear la base de datos y poblarla con algunos datos de ejemplo. Sin embargo recomendamos encarecidamente que haga todos los pasos para comprobar el funcionamiento correcto del sistema

Orden de ejecucion:
1. Modificar el archivo **Scripts de Base de datos/bd.sql** con la ruta correcta para las imagenes
```
C:\\Ruta\\a\\git\\de\\ProyMoviles\\Scripts de Base de datos\\Imagenes\\imagen.jpg
```
2. Crear la base de datos con el script **bd.sql**, recomendamos usar Xampp para levantar un servidor Mysql. Pero se puede usar cualquier otro servidor.
3. Instalar dependencias para la API
```
pip install mysql-connector-python flask
```
4. Ejecutar el servidor de la API-REST, con el script **ApiMaster.py** de la siguiente manera

```
python -u "Direccion\a\ApiMaster.py"
```
5. Importar la aplicacion a Android studio. Puede hacerlo manualmente o siguiendo este [tutorial](https://stackoverflow.com/questions/25348339/how-to-import-an-existing-project-from-github-into-android-studio)
6. Ejecutar la aplicacion

**Nota: Se espera que todo se ejecute en localhost, y la app esta diseñada para esto** 
    
## Bugs Conocidos

- Al iniciar sesion como cliente puede que aparezca el siguiente mensaje: ![Error: No inicializa Catalogo](/ruta/a/la/imagen.jpg) Se debe salir y volver a entrar de la app, puede mostrar el mensaje varias veces

- Las pantallas no se actualizan dinamicamente, se debe de forzar la recarga de la misma, se puede hacer cerrando e iniciando sesion.

## Autores

- [@Erik Alcantara](https://github.com/ErikAlc-cyber)
- [@Ana P. Santos]()
- [@Luis M. Hernandez](https://github.com/LuisMiguelHernandezGarcia)

