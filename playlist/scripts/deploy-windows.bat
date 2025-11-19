@echo off
echo Iniciando deploy...

REM Crear carpeta de deploy si no existe
if not exist C:\deploy (
    mkdir C:\deploy
)

REM Ruta al jar en target
set JAR_SOURCE=..\target\playlist-0.0.1-SNAPSHOT.jar
set JAR_DEST=C:\deploy\playlist.jar

REM Verificar que el jar existe
if not exist "%JAR_SOURCE%" (
    echo ERROR: No se encontro %JAR_SOURCE%
    exit /b 1
)

REM Copiar jar nuevo
copy "%JAR_SOURCE%" "%JAR_DEST%" /Y

echo Deploy completado correctamente. El jar esta en %JAR_DEST%

