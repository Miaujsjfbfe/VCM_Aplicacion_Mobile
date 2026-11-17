# 📱 OftApp - Portal Clínico Oftalmológico

Aplicación móvil desarrollada en **Jetpack Compose** y **Kotlin** para la gestión clínica oftalmológica, autenticación de usuarios, consultas de exámenes, recetas y perfiles clínicos.

---

## 👥 Integrantes

* **Axel Levin**
* **Paz Oyarzún**

---

## 🛠️ Tecnologías y Arquitectura

* **Lenguaje:** Kotlin
* **UI Framework:** Jetpack Compose & Material Design 3 (Material3)
* **Navegación:** Jetpack Navigation Compose / Navigation 3
* **Persistencia:** SQLite Local (`OftDatabase`)
* **Diseño Adaptativo:** `NavigationSuiteScaffold` (Adaptación automática entre teléfono y tablets/plegables)
* **Pruebas Unitarias:** **JUnit 5** (`junit-jupiter` y `junit-platform-launcher`)
* **Gestor de Dependencias:** Gradle (Kotlin DSL)

---

## 🚀 Características Principales

* **Autenticación y Roles:** Inicio de sesión y registro con persistencia local en SQLite (roles: *Tecnólogo, Oftalmólogo, Paciente, Admin*).
* **Navegación Adaptativa:** Interfaz optimizada que se adapta dinámicamente entre barra de navegación inferior (*Bottom Bar*) y riel lateral (*Navigation Rail*) según el dispositivo.
* **Recursos Nativos Integrados (`NativeResourcesHelper`):**
  * **Cámara:** Captura de fotos mediante `FileProvider` y contratos nativos.
  * **Galería y Documentos:** Selección segura de imágenes y archivos PDF.
  * **Fecha y Hora:** Selectores nativos de Material 3.
  * **Conectividad:** Monitoreo en tiempo real de la conexión a internet con `ConnectivityManager`.
* **Pruebas Unitarias con JUnit 5:** Cobertura de pruebas unitarias y parametrizadas ejecutadas sobre la plataforma JUnit 5.

---

## 📋 Requisitos para Ejecutar

* **Android Studio:** Ladybug o superior recomendado.
* **JDK:** Versión 17 o superior.
* **SDK Mínimo (minSdk):** API 24 (Android 7.0) o superior.
* **SDK Objetivo (targetSdk / compileSdk):** API 37.
* **Dispositivo:** Emulador de Android (AVD) o dispositivo físico con Depuración USB activada.
