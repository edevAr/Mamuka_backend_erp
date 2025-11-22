# 🚀 Guía de Deployment en Render

Esta guía te ayudará a desplegar tu aplicación en Render paso a paso.

## 📋 Pre-requisitos

1. ✅ Código migrado a PostgreSQL (✅ COMPLETADO)
2. ✅ Archivo `render.yaml` creado (✅ COMPLETADO)
3. ✅ Repositorio Git (GitHub/GitLab/Bitbucket)
4. ✅ Cuenta en Render (https://render.com)

## 🎯 Pasos para Desplegar

### Paso 1: Subir Código al Repositorio

Si aún no has subido los cambios:

```bash
# Verificar que todos los archivos estén listos
git status

# Agregar todos los cambios
git add .

# Hacer commit
git commit -m "Migrate to PostgreSQL and configure for Render deployment"

# Subir a tu repositorio
git push origin main
# (o git push origin master, dependiendo de tu rama principal)
```

### Paso 2: Crear Cuenta/Iniciar Sesión en Render

1. Ve a https://dashboard.render.com
2. Inicia sesión o crea una cuenta (puedes usar GitHub para login rápido)

### Paso 3: Conectar Repositorio a Render

**Opción A: Usando Blueprint (render.yaml) - RECOMENDADO**

1. En Render Dashboard, haz clic en **"New"** → **"Blueprint"**
2. Conecta tu repositorio (GitHub/GitLab/Bitbucket)
3. Selecciona el repositorio `erpbackend`
4. Render detectará automáticamente el archivo `render.yaml`
5. Haz clic en **"Apply"**
6. Render creará automáticamente:
   - ✅ Base de datos PostgreSQL (`mamukas-erp-db`)
   - ✅ Web Service (`mamukas-erp-backend`)

**Opción B: Crear Servicios Manualmente**

Si prefieres crear los servicios manualmente:

#### 3.1 Crear Base de Datos PostgreSQL

1. Haz clic en **"New"** → **"PostgreSQL"**
2. Configura:
   - **Name**: `mamukas-erp-db`
   - **Database**: `mamukas_erp`
   - **User**: `mamukas_user`
   - **Plan**: Free (o el plan que prefieras)
3. Haz clic en **"Create Database"**
4. ⚠️ **IMPORTANTE**: Anota la **Internal Database URL** (la necesitarás después)

#### 3.2 Crear Web Service

1. Haz clic en **"New"** → **"Web Service"**
2. Conecta tu repositorio
3. Selecciona el repositorio `erpbackend`
4. Configura:
   - **Name**: `mamukas-erp-backend`
   - **Environment**: `Java`
   - **Build Command**: `./gradlew build -x test`
   - **Start Command**: `java -jar build/libs/erpbackend-0.0.1-SNAPSHOT.jar`
   - **Plan**: Free (o el plan que prefieras)

### Paso 4: Configurar Variables de Entorno

Ve a la configuración de tu **Web Service** → **Environment** y agrega:

#### Variables Requeridas (marcadas con ⚠️)

1. **JWT_SECRET** ⚠️
   - Genera una clave secreta segura (puedes usar: https://www.allkeysgenerator.com/Random/Security-Encryption-Key-Generator.aspx)
   - Ejemplo: `MySuperSecretJWTKeyForMamukasERP2024!@#$%`
   - **IMPORTANTE**: Mantén esta clave segura y no la compartas

2. **MAIL_USERNAME** ⚠️
   - Tu email de Gmail: `helenjhankarlapz83@gmail.com`

3. **MAIL_PASSWORD** ⚠️
   - **NO uses tu contraseña normal de Gmail**
   - Necesitas crear un **App Password**:
     1. Ve a tu cuenta de Google: https://myaccount.google.com
     2. Seguridad → Verificación en 2 pasos (debe estar activada)
     3. Busca "Contraseñas de aplicaciones" o "App Passwords"
     4. Genera una nueva contraseña para "Mail"
     5. Copia la contraseña generada (16 caracteres sin espacios)
     6. Úsala aquí

#### Variables Opcionales (ya tienen valores por defecto, pero puedes cambiarlas)

4. **FRONTEND_URL**
   - URL de tu frontend desplegado
   - Ejemplo: `https://tu-frontend.onrender.com`
   - O déjalo como: `http://localhost:3000` si aún no tienes frontend

5. **ACTIVATION_BASE_URL**
   - URL de tu backend en Render
   - Se actualizará automáticamente después del deployment
   - Ejemplo: `https://mamukas-erp-backend.onrender.com/api`
   - ⚠️ Actualiza esto DESPUÉS de que Render te dé la URL

6. **PASSWORD_RESET_BASE_URL**
   - URL de tu frontend para reset de contraseña
   - Ejemplo: `https://tu-frontend.onrender.com/reset-password`

#### Variables de Base de Datos (si creaste la BD manualmente)

Si creaste la base de datos manualmente (Opción B), necesitas:

7. **DATABASE_URL**
   - Copia la **Internal Database URL** de tu servicio PostgreSQL
   - Formato: `postgresql://user:password@host:port/database`
   - ⚠️ Si usaste Blueprint, esto se configura automáticamente

### Paso 5: Conectar Base de Datos al Web Service (Solo si creaste manualmente)

Si creaste los servicios manualmente (Opción B):

1. Ve a tu **Web Service** → **Environment**
2. Haz clic en **"Link Resource"** o busca la sección de **"Linked Resources"**
3. Selecciona tu base de datos PostgreSQL (`mamukas-erp-db`)
4. Esto creará automáticamente la variable `DATABASE_URL`

### Paso 6: Iniciar el Deployment

1. Si usaste **Blueprint**: El deployment comenzará automáticamente
2. Si creaste **manualmente**: 
   - Ve a tu Web Service
   - Haz clic en **"Manual Deploy"** → **"Deploy latest commit"**

### Paso 7: Monitorear el Build

1. Ve a la pestaña **"Logs"** de tu Web Service
2. Verás el progreso del build:
   - ✅ Descarga de dependencias
   - ✅ Compilación con Gradle
   - ✅ Creación del JAR
   - ✅ Inicio de la aplicación

### Paso 8: Verificar el Deployment

1. Espera a que el build termine (puede tomar 5-10 minutos la primera vez)
2. Cuando veas `Started ErpbackendApplication` en los logs, está listo
3. Render te dará una URL como: `https://mamukas-erp-backend.onrender.com`
4. Prueba acceder a: `https://mamukas-erp-backend.onrender.com/api/health` (si tienes endpoint de health)
   - O simplemente: `https://mamukas-erp-backend.onrender.com`

### Paso 9: Actualizar URLs (Después del Deployment)

Una vez que tengas la URL de tu backend:

1. Ve a **Environment Variables** de tu Web Service
2. Actualiza:
   - **ACTIVATION_BASE_URL**: `https://tu-backend-url.onrender.com/api`
   - **FRONTEND_URL**: Tu URL de frontend (si ya la tienes)
   - **PASSWORD_RESET_BASE_URL**: Tu URL de frontend + `/reset-password`

### Paso 10: Verificar que Todo Funcione

1. **Probar la API**:
   ```bash
   # Probar login (usando los usuarios de prueba)
   curl -X POST https://tu-backend.onrender.com/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"admin123"}'
   ```

2. **Verificar logs**:
   - Ve a la pestaña **"Logs"** de tu Web Service
   - Deberías ver logs de la aplicación iniciando
   - Si hay errores, aparecerán aquí

## 🔧 Troubleshooting

### Error: "Build failed"

- **Causa**: Problemas en la compilación
- **Solución**: 
  - Verifica los logs del build
  - Asegúrate de que `build.gradle` esté correcto
  - Verifica que Java 17 esté disponible

### Error: "Cannot connect to database"

- **Causa**: `DATABASE_URL` no configurado o incorrecto
- **Solución**:
  - Verifica que la base de datos esté creada
  - Verifica que `DATABASE_URL` esté en las variables de entorno
  - Si creaste manualmente, asegúrate de haber vinculado el recurso

### Error: "Port already in use"

- **Causa**: Conflicto de puerto
- **Solución**: Render maneja esto automáticamente con la variable `PORT`. Verifica que `application.properties` use `${PORT:8080}`

### Error: "Application failed to start"

- **Causa**: Variables de entorno faltantes o incorrectas
- **Solución**:
  - Verifica que `JWT_SECRET` esté configurado
  - Verifica que `MAIL_USERNAME` y `MAIL_PASSWORD` estén correctos
  - Revisa los logs para ver el error específico

### La aplicación inicia pero no responde

- **Causa**: Puede estar escuchando en el puerto incorrecto
- **Solución**: Verifica que `server.port=${PORT:8080}` esté en `application.properties`

## ✅ Checklist Final

Antes de considerar el deployment completo:

- [ ] Código subido al repositorio
- [ ] Base de datos PostgreSQL creada en Render
- [ ] Web Service creado en Render
- [ ] Variables de entorno configuradas:
  - [ ] `JWT_SECRET`
  - [ ] `MAIL_USERNAME`
  - [ ] `MAIL_PASSWORD`
  - [ ] `FRONTEND_URL` (si aplica)
  - [ ] `ACTIVATION_BASE_URL` (actualizada con URL real)
  - [ ] `PASSWORD_RESET_BASE_URL` (si aplica)
- [ ] Base de datos vinculada al Web Service
- [ ] Build completado exitosamente
- [ ] Aplicación iniciada sin errores
- [ ] URL del backend funcionando
- [ ] API respondiendo correctamente

## 🎉 ¡Listo!

Una vez completados todos los pasos, tu aplicación estará desplegada en Render y accesible desde internet.

**URL de tu backend**: `https://mamukas-erp-backend.onrender.com`

## 📞 Soporte

Si encuentras problemas:
1. Revisa los logs en Render Dashboard
2. Verifica que todas las variables de entorno estén configuradas
3. Asegúrate de que el código esté actualizado en el repositorio

