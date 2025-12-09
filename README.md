# Wallpaper FTP Actualizable

## 1. Resumen Ejecutivo

### Descripción
Este proyecto es una solución desarrollada en Java encargada de la gestión y actualización automática de fondos de pantalla (wallpapers) mediante conexión FTP. Permite mantener la identidad visual de los equipos de cómputo sincronizada desde una fuente centralizada.

### Problema Identificado
La gestión manual de fondos de pantalla en múltiples equipos consume tiempo y no garantiza la uniformidad. Se requiere una herramienta que automatice este proceso sin intervención constante del usuario final.

### Solución
Una aplicación de escritorio robusta que se conecta a un servidor, verifica la existencia de nuevas imágenes y actualiza el entorno local del usuario automáticamente.

### Arquitectura de la Solución
El flujo de trabajo inicia con la actualización del código en el repositorio, pasando por pruebas automáticas en GitHub Actions antes de ser desplegado.

```mermaid
graph TD
    User((Usuario/Dev)) --> GitHub[Repositorio GitHub]
    GitHub --> Actions[GitHub Actions CI]
    Actions -->|Ejecuta Tests| JUnit[Pruebas JUnit]
    JUnit -->|Éxito| Jar[Generación JAR]
    Jar --> Client[Cliente Desktop]
    Client -->|FTP| Server[Servidor Archivos]
