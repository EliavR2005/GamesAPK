# Instrucciones de Restauración - Backup GamesAPK

## Pasos para Restaurar el Proyecto

### 1. Preparar el Entorno
```bash
# Crear directorio para el proyecto restaurado
mkdir GamesAPK_restaurado
cd GamesAPK_restaurado

# Copiar todos los archivos del backup
cp -r /ruta/al/backup_GamesAPK_20250928_204201/* .
```

### 2. Configurar Git (Opcional)
```bash
# Inicializar repositorio Git
git init

# Añadir archivos
git add .
git commit -m "Proyecto restaurado desde backup"

# Conectar con repositorio remoto (opcional)
# git remote add origin https://github.com/usuario/repo.git
# git push -u origin main
```

### 3. Configurar Android Studio
1. Abrir Android Studio
2. Seleccionar "Open an existing Android Studio project"
3. Navegar al directorio `GamesAPK_restaurado`
4. Esperar que Android Studio indexe el proyecto

### 4. Resolver Dependencias
```bash
# Desde la terminal o usando Android Studio
./gradlew clean
./gradlew build
```

### 5. Verificar la Instalación
- Comprobar que se compila sin errores
- Ejecutar tests unitarios: `./gradlew test`
- Ejecutar tests instrumentados: `./gradlew connectedAndroidTest`

### 6. Ejecutar la Aplicación
1. Conectar dispositivo Android o configurar emulador
2. En Android Studio: Run > Run 'app'
3. O desde terminal: `./gradlew installDebug`

## Estructura del Proyecto Restaurado

```
GamesAPK_restaurado/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/games/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── LoginActivity.kt
│   │   │   │   ├── RegisterActivity.kt
│   │   │   │   ├── sudoku/
│   │   │   │   ├── memorama/
│   │   │   │   └── tictactoe/
│   │   │   └── res/
│   │   ├── test/
│   │   └── androidTest/
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Troubleshooting

### Problemas Comunes
1. **Error de versión de Gradle**: Actualizar `gradle/wrapper/gradle-wrapper.properties`
2. **SDK no encontrado**: Configurar `ANDROID_HOME` en variables de entorno
3. **Dependencias faltantes**: Ejecutar `./gradlew --refresh-dependencies`

### Verificar Requisitos
- Android Studio Arctic Fox o superior
- JDK 11 o superior
- Android SDK API 21+
- Kotlin 1.9.0+

## Contacto
Para problemas con la restauración, consultar la documentación original del proyecto o contactar al equipo de desarrollo.

---
**Backup creado**: 2025-09-28 20:42:01 UTC
**Versión del proyecto**: Última versión disponible al momento del backup