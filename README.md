Proyecto Star Wars API Integration

-Este proyecto consiste en una aplicación Java 8 que se integra con la API de Star Wars para listar y filtrar datos de People, Films, Starships y Vehicles. La aplicación también incluye un sistema de autenticación seguro y está completamente documentada con Swagger. Además, cuenta con pruebas unitarias e de integración para garantizar su funcionamiento correcto.


-Para ejecutar este proyecto localmente, asegúrese de tener instalado Java 8 y Maven.

Debe clonar el repositorio del proyecto
https://github.com/majo1988/conexa.git

Instalar Dependencias:

mvn clean install

La aplicación estará disponible en http://localhost:8080.

Documentación
La documentación de la API está disponible a través de Swagger. 

Autenticación
La aplicación utiliza un sistema de autenticación basado en token JWT. Para obtener un token de acceso, haga una solicitud POST a:

http://localhost:8080/auth/login
con las credenciales de usuario. El token generado debe incluirse en la cabecera Authorization para acceder a las rutas protegidas.
Se creo un usuario de prueba:
Username: conexa
Password: carmelita23

Pruebas
Ejecute las pruebas unitarias e de integración con el siguiente comando:
mvn test

Se encuentras desarrolladas actualmente 4 dashboards:
/films
/starships
/vehicles
/people


En la versión 1.0.1:
se agregara dashboard de bienvenida con redirección 
Se podrá guardar en base de datos toda la información adquirida de la API 

Despliegue
Recomendamos desplegar la aplicación en un servidor como Heroku. Asegúrese de configurar las variables de entorno necesarias y seguir las guías de despliegue específicas del proveedor.