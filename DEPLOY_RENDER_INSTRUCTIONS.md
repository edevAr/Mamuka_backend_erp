# 🚀 Guía de Deploy del Backend en Render

## 📋 Requisitos Previos

1. ✅ Cuenta en [Render](https://render.com) (gratis)
2. ✅ Repositorio de GitHub con el código del backend
3. ✅ Base de datos PostgreSQL (Render puede crear una automáticamente)

## 🔧 Pasos para Deploy

### Paso 1: Crear Base de Datos PostgreSQL en Render

1. Ve a [Render Dashboard](https://dashboard.render.com)
2. Haz clic en **"New +"** > **"PostgreSQL"**
3. Configura:
   - **Name**: `mamukas-erp-db` (o el nombre que prefieras)
   - **Database**: `mamukas_erp`
   - **User**: `mamukas_user` (o el que prefieras)
   - **Region**: Elige la más cercana a ti
   - **Plan**: Free (para empezar)
4. Haz clic en **"Create Database"**
5. ⚠️ **IMPORTANTE**: Anota la información de conexión que Render te proporciona
6. Render automáticamente creará la variable de entorno `DATABASE_URL` que el backend usará

### Paso 2: Crear el Web Service (Backend)

1. En Render Dashboard, haz clic en **"New +"** > **"Web Service"**
2. Conecta tu repositorio de GitHub:
   - Selecciona el repositorio que contiene el backend
   - O usa el archivo `render.yaml` si está en el repositorio
3. Si usas `render.yaml`:
   - Render detectará automáticamente la configuración
   - Haz clic en **"Apply"**
4. Si configuras manualmente:
   - **Name**: `mamukas-erp-backend`
   - **Environment**: `Docker`
   - **Dockerfile Path**: `./Dockerfile`
   - **Plan**: Free (para empezar)

### Paso 3: Configurar Variables de Entorno

En la sección **"Environment"** del servicio web, agrega las siguientes variables:

#### 🔐 Variables Requeridas:

1. **JWT_SECRET**
   - Genera una clave secreta segura (puedes usar: https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx)
   - Ejemplo: `VGhpc0lzQVZlcnlTZWN1cmVTZWNyZXRLZXlGb3JKV1RUb2tlbnNNYW11a2FzRVJQ`

2. **DATABASE_URL** (si no se configuró automáticamente)
   - Render debería proporcionar esto automáticamente si conectaste la base de datos
   - Formato: `postgresql://user:password@host:port/database`

3. **MAIL_USERNAME**
   - Tu email de Gmail (ej: `tuemail@gmail.com`)

4. **MAIL_PASSWORD**
   - **NO** uses tu contraseña normal de Gmail
   - Necesitas crear una **"App Password"** en Google:
     - Ve a: https://myaccount.google.com
     - Security > 2-Step Verification > App passwords
     - Genera una contraseña para "Mail"
     - Usa esa contraseña aquí

#### 🌐 Variables de URLs (Actualiza con tus URLs reales):

5. **FRONTEND_URL**
   - URL de tu frontend en GitHub Pages
   - Ejemplo: `https://tuusuario.github.io/tu-repo/`
   - O si es un dominio personalizado: `https://tudominio.com`

6. **ACTIVATION_BASE_URL**
   - URL de tu backend + `/api`
   - Se configurará después del deploy: `https://mamukas-erp-backend.onrender.com/api`
   - ⚠️ Actualiza esto DESPUÉS de que Render te dé la URL del servicio

7. **PASSWORD_RESET_BASE_URL**
   - URL de tu frontend + `/reset-password`
   - Ejemplo: `https://tuusuario.github.io/tu-repo/reset-password`

#### 📊 Variables Opcionales (Logging):

8. **LOG_LEVEL**: `INFO` (para producción)
9. **LOG_LEVEL_WEB**: `INFO`
10. **LOG_LEVEL_SQL**: `WARN`
11. **LOG_LEVEL_SQL_BINDER**: `WARN`

#### ⚙️ Variables de JPA:

12. **SPRING_JPA_HIBERNATE_DDL_AUTO**: `update` (para el primer deploy, luego cambia a `validate`)

### Paso 4: Conectar la Base de Datos al Web Service

1. En la configuración del Web Service, ve a **"Connections"**
2. Haz clic en **"Link Resource"**
3. Selecciona tu base de datos PostgreSQL
4. Esto automáticamente configurará `DATABASE_URL`

### Paso 5: Deploy

1. Haz clic en **"Create Web Service"** o **"Save Changes"**
2. Render comenzará a construir y desplegar tu aplicación
3. Esto puede tardar 5-10 minutos la primera vez
4. Verás los logs en tiempo real

### Paso 6: Obtener la URL del Backend

1. Una vez completado el deploy, Render te dará una URL como:
   - `https://mamukas-erp-backend.onrender.com`
2. ⚠️ **IMPORTANTE**: Actualiza la variable `ACTIVATION_BASE_URL` con esta URL:
   - Ve a Environment Variables
   - Actualiza: `ACTIVATION_BASE_URL=https://TU-URL.onrender.com/api`
   - Guarda y reinicia el servicio

### Paso 7: Verificar el Deploy

1. Prueba el endpoint de health (si lo tienes):
   ```
   https://tu-backend.onrender.com/api/health
   ```

2. O prueba el login:
   ```bash
   curl -X POST https://tu-backend.onrender.com/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{
       "usernameOrEmail": "admin",
       "password": "admin123",
       "device": "Browser",
       "ip": "127.0.0.1"
     }'
   ```

## 🔄 Actualizar el Frontend

Después del deploy, actualiza el frontend para que apunte al nuevo backend:

1. Edita `Mamukas_client/lib/core/constants/api_constants.dart`
2. Cambia `baseUrl` a la URL de tu backend en Render:
   ```dart
   static const String baseUrl = 'https://tu-backend.onrender.com';
   ```

## 🐛 Solución de Problemas

### Error: "DATABASE_URL not found"
- Verifica que la base de datos esté conectada al web service
- Ve a Connections y asegúrate de que esté linkeada

### Error: "Port already in use"
- Render maneja el puerto automáticamente
- Asegúrate de que `server.port=${PORT:8080}` esté en `application.properties`

### Error: "Build failed"
- Revisa los logs en Render
- Verifica que el Dockerfile esté correcto
- Asegúrate de que todas las dependencias estén en `build.gradle`

### La aplicación se despliega pero no responde
- Verifica que el puerto esté configurado correctamente
- Revisa los logs para ver errores de conexión a la base de datos
- Asegúrate de que CORS esté configurado para permitir tu frontend

## 📝 Checklist de Deploy

- [ ] Base de datos PostgreSQL creada en Render
- [ ] Web Service creado y conectado a GitHub
- [ ] Variables de entorno configuradas:
  - [ ] JWT_SECRET
  - [ ] DATABASE_URL (automático si está conectado)
  - [ ] MAIL_USERNAME
  - [ ] MAIL_PASSWORD (App Password de Gmail)
  - [ ] FRONTEND_URL
  - [ ] ACTIVATION_BASE_URL
  - [ ] PASSWORD_RESET_BASE_URL
- [ ] Base de datos conectada al Web Service
- [ ] Deploy completado exitosamente
- [ ] URL del backend obtenida
- [ ] ACTIVATION_BASE_URL actualizada con la URL real
- [ ] Frontend actualizado para apuntar al nuevo backend
- [ ] Pruebas de endpoints realizadas

## 🎉 ¡Listo!

Una vez completado, tu backend estará disponible en:
- **URL**: `https://tu-backend.onrender.com`
- **API Base**: `https://tu-backend.onrender.com/api`

¡Tu backend está deployado en Render! 🚀

