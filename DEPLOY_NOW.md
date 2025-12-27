# 🚀 Deploy Inmediato a Render - Guía Paso a Paso

## ⚡ Inicio Rápido (5 minutos)

### Paso 1: Crear Base de Datos PostgreSQL

1. Ve a: https://dashboard.render.com
2. Haz clic en **"New +"** → **"PostgreSQL"**
3. Configura:
   - **Name**: `mamukas-erp-db`
   - **Database**: `mamukas_erp`
   - **User**: `mamukas_user`
   - **Region**: Elige la más cercana
   - **Plan**: Free
4. Haz clic en **"Create Database"**
5. ⚠️ **Espera 2-3 minutos** a que se cree la base de datos
6. Render creará automáticamente la variable `DATABASE_URL`

### Paso 2: Crear Web Service (Backend)

1. En Render Dashboard, haz clic en **"New +"** → **"Web Service"**
2. Conecta tu repositorio de GitHub:
   - Si es la primera vez, autoriza Render a acceder a tus repositorios
   - Selecciona el repositorio que contiene `Mamuka_backend_erp`
3. Render detectará automáticamente el `Dockerfile`
4. Configura:
   - **Name**: `mamukas-erp-backend`
   - **Environment**: `Docker` (debería detectarse automáticamente)
   - **Dockerfile Path**: `./Dockerfile` (debería estar automáticamente)
   - **Plan**: Free
5. **NO hagas clic en "Create Web Service" todavía**

### Paso 3: Configurar Variables de Entorno

Antes de crear el servicio, desplázate hacia abajo a la sección **"Environment Variables"** y agrega:

#### 🔐 Variables Críticas (REQUERIDAS):

1. **JWT_SECRET**
   ```
   VGhpc0lzQVZlcnlTZWN1cmVTZWNyZXRLZXlGb3JKV1RUb2tlbnNNYW11a2FzRVJQ
   ```
   (O genera una nueva clave secreta más segura)

2. **MAIL_USERNAME**
   ```
   helenjhankarlapz83@gmail.com
   ```

3. **MAIL_PASSWORD**
   ```
   szfb jqff eujj xorc
   ```
   ⚠️ **IMPORTANTE**: Esta es una App Password de Gmail, NO tu contraseña normal

4. **FRONTEND_URL**
   ```
   https://edevar.github.io/Mamukas_client/
   ```

5. **ACTIVATION_BASE_URL**
   ```
   https://mamukas-erp-backend.onrender.com/api
   ```
   ⚠️ **NOTA**: Actualiza esto DESPUÉS del deploy con la URL real que Render te dé

6. **PASSWORD_RESET_BASE_URL**
   ```
   https://edevar.github.io/Mamukas_client/reset-password
   ```

#### 📊 Variables Opcionales (Recomendadas para Producción):

7. **LOG_LEVEL**: `INFO`
8. **LOG_LEVEL_WEB**: `INFO`
9. **LOG_LEVEL_SQL**: `WARN`
10. **LOG_LEVEL_SQL_BINDER**: `WARN`
11. **SPRING_JPA_HIBERNATE_DDL_AUTO**: `update` (cambia a `validate` después del primer deploy)

### Paso 4: Conectar Base de Datos

1. En la misma página de configuración del Web Service, desplázate a **"Connections"**
2. Haz clic en **"Link Resource"**
3. Selecciona tu base de datos PostgreSQL (`mamukas-erp-db`)
4. Esto configurará automáticamente `DATABASE_URL`

### Paso 5: Crear y Deployar

1. Haz clic en **"Create Web Service"**
2. Render comenzará a construir tu aplicación
3. Esto puede tardar **5-10 minutos** la primera vez
4. Puedes ver el progreso en los logs en tiempo real

### Paso 6: Obtener URL y Actualizar

1. Una vez completado el deploy, Render te dará una URL como:
   ```
   https://mamukas-erp-backend-XXXX.onrender.com
   ```
2. ⚠️ **IMPORTANTE**: Actualiza `ACTIVATION_BASE_URL`:
   - Ve a tu Web Service → **"Environment"**
   - Busca `ACTIVATION_BASE_URL`
   - Actualiza a: `https://TU-URL-REAL.onrender.com/api`
   - Guarda los cambios
   - Render reiniciará automáticamente el servicio

### Paso 7: Verificar el Deploy

Prueba estos endpoints:

1. **Health Check** (si tienes):
   ```
   https://tu-backend.onrender.com/api/health
   ```

2. **Login**:
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

## ✅ Checklist de Deploy

- [ ] Base de datos PostgreSQL creada
- [ ] Web Service creado
- [ ] Variables de entorno configuradas:
  - [ ] JWT_SECRET
  - [ ] MAIL_USERNAME
  - [ ] MAIL_PASSWORD
  - [ ] FRONTEND_URL
  - [ ] ACTIVATION_BASE_URL (actualizar después)
  - [ ] PASSWORD_RESET_BASE_URL
- [ ] Base de datos conectada al Web Service
- [ ] Deploy completado
- [ ] URL del backend obtenida
- [ ] ACTIVATION_BASE_URL actualizada con URL real
- [ ] Pruebas de endpoints realizadas

## 🎯 URL Final

Tu backend estará disponible en:
- **URL**: `https://mamukas-erp-backend-XXXX.onrender.com`
- **API Base**: `https://mamukas-erp-backend-XXXX.onrender.com/api`

## 🔄 Actualizar Frontend

Después del deploy, actualiza el frontend:

1. Edita: `Mamukas_client/lib/core/constants/api_constants.dart`
2. Cambia:
   ```dart
   static const String baseUrl = 'https://TU-BACKEND-URL.onrender.com';
   ```
3. Haz commit y push para que se actualice en GitHub Pages

## 🐛 Problemas Comunes

### El deploy falla en "Build"
- Verifica que el Dockerfile esté en la raíz del proyecto
- Revisa los logs para ver el error específico

### Error de conexión a base de datos
- Verifica que la base de datos esté conectada (Connections)
- Asegúrate de que `DATABASE_URL` esté configurada automáticamente

### La app no responde
- Verifica los logs del servicio
- Asegúrate de que el puerto esté configurado correctamente (Render lo maneja automáticamente)

¡Listo para deployar! 🚀

