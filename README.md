# Mamukas ERP Backend

Backend del sistema ERP desarrollado con Spring Boot y PostgreSQL.

## 🚀 Características

- **Framework**: Spring Boot 3.5.7
- **Base de Datos**: PostgreSQL
- **Java**: 17
- **Autenticación**: JWT (JSON Web Tokens)
- **Seguridad**: Spring Security con permisos granulares
- **API REST**: Documentación completa disponible

## 📋 Requisitos Previos

- Java 17 o superior
- PostgreSQL 12 o superior
- Gradle 7.x o superior

## 🛠️ Configuración Local

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd erpbackend
```

### 2. Configurar Base de Datos PostgreSQL

Crear una base de datos PostgreSQL:

```sql
CREATE DATABASE mamukas_erp;
```

### 3. Configurar Variables de Entorno (Opcional)

Puedes configurar las siguientes variables de entorno o usar los valores por defecto en `application.properties`:

```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/mamukas_erp
export DB_USERNAME=postgres
export DB_PASSWORD=tu_password
```

### 4. Ejecutar la Aplicación

```bash
./gradlew bootRun
```

La aplicación estará disponible en `http://localhost:8080`

## 📦 Build

Para construir el JAR:

```bash
./gradlew build
```

El JAR se generará en `build/libs/erpbackend-0.0.1-SNAPSHOT.jar`

## 🚀 Deployment en Render

### Opción 1: Usando render.yaml (Recomendado)

1. **Conectar tu repositorio a Render**:
   - Ve a [Render Dashboard](https://dashboard.render.com)
   - Click en "New" → "Blueprint"
   - Conecta tu repositorio de GitHub/GitLab
   - Render detectará automáticamente el archivo `render.yaml`

2. **Crear Base de Datos PostgreSQL**:
   - Render creará automáticamente la base de datos PostgreSQL según la configuración en `render.yaml`

3. **Configurar Variables de Entorno**:
   - Ve a la configuración de tu servicio web en Render
   - Configura las siguientes variables de entorno (las marcadas como `sync: false` en render.yaml):
     - `JWT_SECRET`: Genera una clave secreta segura para JWT
     - `MAIL_USERNAME`: Tu email de Gmail
     - `MAIL_PASSWORD`: Tu App Password de Gmail (no tu contraseña normal)
     - `FRONTEND_URL`: URL de tu frontend desplegado
     - `ACTIVATION_BASE_URL`: URL de tu backend (ej: `https://mamukas-erp-backend.onrender.com/api`)
     - `PASSWORD_RESET_BASE_URL`: URL de tu frontend para reset de contraseña

4. **Desplegar**:
   - Render desplegará automáticamente cuando hagas push a tu repositorio
   - El build command ejecutará: `./gradlew build -x test`
   - El start command ejecutará: `java -jar build/libs/erpbackend-0.0.1-SNAPSHOT.jar`

### Opción 2: Configuración Manual

1. **Crear Base de Datos PostgreSQL**:
   - En Render Dashboard, ve a "New" → "PostgreSQL"
   - Anota la información de conexión (Render proporciona `DATABASE_URL` automáticamente)

2. **Crear Web Service**:
   - Ve a "New" → "Web Service"
   - Conecta tu repositorio
   - Configura:
     - **Build Command**: `./gradlew build -x test`
     - **Start Command**: `java -jar build/libs/erpbackend-0.0.1-SNAPSHOT.jar`
     - **Environment**: Java

3. **Configurar Variables de Entorno**:
   - `DATABASE_URL`: Render lo proporciona automáticamente si conectaste la base de datos
   - `JWT_SECRET`: Clave secreta para JWT
   - `MAIL_USERNAME`: Email de Gmail
   - `MAIL_PASSWORD`: App Password de Gmail
   - `FRONTEND_URL`: URL de tu frontend
   - `ACTIVATION_BASE_URL`: URL de tu backend
   - `PASSWORD_RESET_BASE_URL`: URL de tu frontend para reset
   - `LOG_LEVEL`: `INFO` (para producción)

### Notas Importantes para Render

- **Puerto**: Render establece automáticamente la variable `PORT`. La aplicación está configurada para usarla.
- **DATABASE_URL**: Render proporciona `DATABASE_URL` en formato `postgresql://user:password@host:port/database`. La clase `DatabaseConfig` lo parsea automáticamente.
- **Logs**: En producción, configura `LOG_LEVEL=INFO` para reducir el volumen de logs.
- **JPA DDL**: Después del despliegue inicial, considera cambiar `spring.jpa.hibernate.ddl-auto` a `validate` o `none` en producción.

## 🔐 Configuración de Email (Gmail)

Para usar Gmail SMTP, necesitas:

1. Habilitar "Acceso de aplicaciones menos seguras" o mejor aún, usar **App Passwords**:
   - Ve a tu cuenta de Google
   - Seguridad → Verificación en 2 pasos (debe estar activada)
   - App Passwords → Generar nueva contraseña
   - Usa esta contraseña en `MAIL_PASSWORD` (no tu contraseña normal)

2. Configurar en Render:
   - `MAIL_USERNAME`: Tu email de Gmail
   - `MAIL_PASSWORD`: El App Password generado

## 📚 Documentación de API

- **API General**: Ver `API_DOCUMENTATION.md`
- **API de Seguridad**: Ver `SECURITY_API_DOCS.md`
- **API de Tiendas**: Ver `STORE_API_DOCUMENTATION.md`
- **API de Almacenes**: Ver `WAREHOUSE_API_DOCUMENTATION.md`
- **Ejemplos**: Ver `API_EXAMPLES.json`

## 🧪 Usuarios de Prueba

La aplicación crea automáticamente los siguientes usuarios de prueba:

- **Admin**: 
  - Username: `admin`
  - Password: `admin123`
  
- **Test Admin**:
  - Username: `testadmin`
  - Password: `admin123`
  
- **Test Employee**:
  - Username: `testemployee`
  - Password: `employee123`
  
- **Test Customer**:
  - Username: `testcustomer`
  - Password: `customer123`

⚠️ **IMPORTANTE**: Cambia estas contraseñas en producción.

## 🏗️ Arquitectura

Ver `ARQUITECTURA_CLEAN.md` para detalles sobre la arquitectura del proyecto.

## 📝 Estructura del Proyecto

```
erpbackend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/mamukas/erp/erpbackend/
│   │   │       ├── application/     # Capa de aplicación (DTOs, Services)
│   │   │       ├── domain/          # Capa de dominio (Entidades)
│   │   │       └── infrastructure/ # Capa de infraestructura (JPA, Controllers, Config)
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── database/                        # Scripts SQL
├── build.gradle
├── render.yaml                      # Configuración para Render
└── README.md
```

## 🔧 Troubleshooting

### Error de conexión a la base de datos

- Verifica que PostgreSQL esté corriendo
- Verifica las credenciales en `application.properties` o variables de entorno
- En Render, verifica que `DATABASE_URL` esté configurado correctamente

### Error en el build

- Verifica que tengas Java 17 instalado: `java -version`
- Limpia el build: `./gradlew clean build`

### Error de puerto en Render

- Render asigna automáticamente el puerto. La aplicación usa la variable `PORT` automáticamente.

## 📄 Licencia

Este proyecto es parte del Trabajo Final de Maestría.

## 👥 Autores

Mamukas ERP Team
