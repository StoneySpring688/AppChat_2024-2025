# AppChat_2024-2025
### A modern, feature-rich chat application powered by Java Swing

---

> [!IMPORTANT]
> ### Requiere :
> - [Java](https://www.oracle.com/es/java/technologies/downloads/)
> - [Maven](https://maven.apache.org/download.cgi)

## 📋 Descripción del proyecto
**AppChat** es una aplicación de mensajería inspirada en aplicaciones populares como **WhatsApp Web** y **Telegram**, diseñada como una aplicación de escritorio en **Java/Swing**.

Esta aplicación permite a los usuarios comunicarse de manera privada y organizada, gestionar listas de contactos y grupos, y realizar búsquedas avanzadas de mensajes. Los usuarios también pueden optar por una cuenta **Premium** para obtener beneficios adicionales.

## 🚀 Funcionalidades principales
- **Login y Registro de usuarios**: Los usuarios pueden registrarse usando su número de teléfono y otros datos personales.
- **Lista de contactos y grupos**: Gestión personalizada de contactos individuales y grupos para un fácil acceso.
- **Mensajería**: Envía mensajes a contactos individuales o grupos, con soporte para emoticonos y un sistema de ordenación cronológica.
- **Cuentas Premium**: Opción de cuenta premium con descuentos basados en la actividad del usuario y la posibilidad de exportar conversaciones en formato PDF.
- **Búsqueda avanzada**: Filtros para encontrar mensajes específicos basados en texto, número de teléfono, nombre del contacto, y más.

## 💻 Arquitectura de la aplicación
La aplicación está estructurada en un modelo de **tres capas**:
1. **Interfáz**: Desarrollada en **Java Swing**
2. **Lógica de Negocio**: Maneja las operaciones principales y la lógica de la aplicación.
3. **Almacenamiento**: Implementa persistencia de datos usando servicios específicos y el patrón **DAO** para desacoplar el acceso a datos.

## 🛠️ Tecnologías y Librerías
- **Java**: Lenguaje principal para la lógica de negocio y la interfaz.
- **Java Swing**: Para construir las interfaces de usuario.
- **Maven**: Gestión de dependencias y construcción del proyecto.
- **iText**: Para la generación de archivos PDF en cuentas premium.
- **JCalendar**: Componente Swing para el manejo de fechas en la interfaz de usuario.

## 🧩 Instalación
**Clonar el repositorio**:
```bash
git clone https://github.com/StoneySpring688/AppChat_2024-2025.git
cd AppChat_2024-2025
```

## 🔧 Construcción

1. Asegúrate de tener **Maven** instalado 
2. Descomprime el archivo si lo has descargado como un **.zip**
3. Ejecuta estos comandos dentro del directorio del proyecto **AppChat_2024-2025**:

```bash
cd AppChat
mvn install:install-file -Dfile=libs/gradient-icon-font.jar -DgroupId=umu.tds -DartifactId=gradient-icon-font -Dversion=1.0 -Dpackaging=jar
mvn clean install
cd ..
```

## ▶️ Ejecutar la aplicación

```bash
java -jar target/AppChat.jar
```


## 📖 Documentación
La documentación detallada del proyecto incluye:
- Modelado de requisitos y diagramas de clases.
- Manual de usuario.
- Explicaciones de diseño y patrones utilizados.

Consulta el archivo `TDS-AppChat-Documentacion.pdf` para más detalles técnicos.

## 🏆 Créditos
Este proyecto ha sido desarrollado como parte del curso de **Tecnologías de Desarrollo de Software** en la Universidad de Murcia (Curso 2024/2025).

### 👥 Equipo de Desarrollo
- [StoneySpring688](https://github.com/StoneySpring688)
- [LuvBluu](https://github.com/LuvBluu)
