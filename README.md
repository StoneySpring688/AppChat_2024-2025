# AppChat_2024-2025
### A modern, feature-rich chat application powered by Java

---

> [!IMPORTANT]
> ### Requiere :
> - [Java17 o posterior](https://www.oracle.com/es/java/technologies/downloads/)
> - [Maven (para desarrollo)](https://maven.apache.org/download.cgi)

## 📋 Descripción del proyecto
**AppChat** es una aplicación de mensajería inspirada en aplicaciones populares como **WhatsApp Web**, **Telegram** y **Discord**, diseñada como una aplicación de escritorio en **Java/Swing**.

Esta aplicación permite a los usuarios comunicarse de manera privada y organizada, gestionar listas de contactos y grupos, y realizar búsquedas avanzadas de mensajes. Los usuarios también pueden optar por una cuenta **Premium** para obtener beneficios adicionales.

## 🚀 Funcionalidades principales
- **Login y Registro de usuarios**: Los usuarios pueden registrarse usando su número de teléfono y otros datos personales.
- **Lista de contactos y grupos**: Gestión personalizada de contactos individuales y grupos para un fácil acceso.
- **Mensajería**: Envía mensajes a contactos individuales o grupos, con soporte para emoticonos y un sistema de ordenación cronológica.
- **Cuentas Premium**: Opción de cuenta premium con descuentos y la posibilidad de exportar conversaciones en formato PDF.
- **Búsqueda avanzada**: Filtros para encontrar mensajes específicos basados en texto, número de teléfono y nombre del contacto.

## 💻 Arquitectura de la aplicación
La aplicación está estructurada en un modelo de **tres capas**:
1. **Interfáz**: Desarrollada en **Java Swing**
2. **Lógica de Negocio**: Maneja las operaciones principales y la lógica de la aplicación.
3. **Persistencia**: Implementa persistencia de datos usando servicios específicos y el patrón **DAO** para desacoplar el acceso a datos.

## 🛠️ Tecnologías y Librerías

- **Java 17** – Lenguaje principal del proyecto.  
- **Swing** – Interfaz gráfica de usuario.  
- **Maven** – Gestión de dependencias y compilación.  
- **iText 7** – Exportación de mensajes a PDF para usuarios premium.  
- **SLF4J + Logback** – Sistema de registro de eventos (logging).  
- **H2 Database** – Base de datos H2.


## 🧩 Instalación
### Para desarrollo:
**Clonar el repositorio**:
```bash
git clone https://github.com/StoneySpring688/AppChat_2024-2025.git
cd AppChat_2024-2025
```
### Para ejecución:
**[descargar la última versión de la app](https://github.com/StoneySpring688/AppChat_2024-2025/releases)**

## 🔧 Construcción (solo para desarrollo)

1. Asegúrate de tener **Maven** instalado 
2. Descomprime el archivo si lo has descargado como un **.zip**
3. Utilizando el **pom.xml** del proyecto haz *build* e *install*

## ▶️ Ejecutar la aplicación
1. asegurate de estar en el directorio **release** de la aplicación
2. Ejecuta el siguiente comando :

```bash
java -jar ServidorPersistenciaH2/ServidorPersistenciaH2/ServidorPersistenciaH2.jar
```
3. Ejecuta la aplicación java ya sea con doble click o en otra terminal usando :
```bash
java -jar AppChat-*.jar
```


## 📖 Documentación
Documentación detallada del proyecto:
**[documentación](Documentacion/AppChat.pdf)**

## 🏆 Créditos
Este proyecto ha sido desarrollado como parte del curso de **Tecnologías de Desarrollo de Software** en la Universidad de Murcia (Curso 2024/2025).


