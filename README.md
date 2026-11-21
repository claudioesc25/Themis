<h1 align="center">Themis</h1>

<p align="center"><em>Aplicación móvil Android de documentos legales de Chile: leyes, artículos y apuntes personales.</em></p>

---

## Integrantes

- **[Diego Bolivar]**
- **[Claudio Escudero]**
- **[Karla Miranda]** 

## Funcionalidades

- **Registro e inicio de sesión** 
- **Mi usuario:** 
- **Leyes y Artículos:** 
- **Mis Apuntes:** 
- **Cámara.**
- **Mapa** 
- **Descarga de documentos en PDF.**

## Recursos nativos

**Cámara**, **GPS** y **archivos** (generación de PDF).

## Tecnologías

*Kotlin, Jetpack Compose, Retrofit, Room, y backend en Xano.*

## Endpoints utilizados

**Base URL:** `https://x8ki-letl-twmt.n7.xano.io/api:q1QBI9WH/`

| Método | Endpoint | Descripción |
|---|---|---|
| **POST** | `/auth/signup` | Registrar usuario |
| **POST** | `/auth/login` | Iniciar sesión y obtener token |
| **GET** | `/auth/me` | Datos del usuario autenticado |
| **GET** | `/themis_legal_document` | Listar documentos legales |
| **GET** | `/themis_nota` | Listar las notas del usuario |
| **POST** | `/themis_nota` | Crear nota |
| **PUT** | `/themis_nota/{id}` | Editar nota |
| **DELETE** | `/themis_nota/{id}` | Eliminar nota |

## Pasos para ejecutar

1. Clonar el repositorio: `git clone [URL del repositorio]`
2. Abrir la carpeta `android` en **Android Studio** y esperar a que Gradle sincronice.
3. Ejecutar en un emulador o dispositivo con **Run**. Requiere conexión a internet.

## Pruebas unitarias

Archivo `app/src/test/java/com/Duoc/Themis/viewmodel/AuthViewModelTest.kt`, con **JUnit** y **kotlinx-coroutines-test**:

1. Al ingresar datos inválidos en el registro, se generan mensajes de error y se bloquea el envío.
2. `cerrarSesion` elimina los datos guardados y limpia el formulario.

