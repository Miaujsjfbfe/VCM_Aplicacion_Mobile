# 📱 OftApp - Portal Clínico Oftalmológico

Aplicación móvil desarrollada en **Jetpack Compose** y **Kotlin** para la gestión clínica oftalmológica, autenticación híbrida de usuarios, visualización interactiva de exámenes y recetas, síntesis de voz, accesibilidad universal, persistencia offline y arquitectura de microservicios.

---

## 👥 Integrantes

* **Axel Levin**
* **Paz Oyarzún**

---

## 🛠️ Tecnologías y Arquitectura

* **Lenguaje:** Kotlin 2.2.10
* **UI Framework:** Jetpack Compose & Material Design 3 (Material3)
* **Navegación & Adaptabilidad:** `NavigationSuiteScaffold` (Material 3 Adaptive: conversión automática entre *Bottom Bar* y *Navigation Rail* en tablets/horizontal).
* **Autenticación en la Nube:** **Insforge Auth** (`dev.insforge:insforge-kotlin:0.1.7`) con fallback a SQLite local.
* **Cliente HTTP & Servidor Clínico:** **Retrofit 2** (`com.squareup.retrofit2`) conectado al servidor público **HAPI FHIR R4**.
* **Visualización de Imágenes Médicas:** **Coil 2.7.0** & **Telephoto 0.19.0** (Pinch-to-zoom, Pan 360° y doble toque en retinografías, topografías corneales y campimetrías).
* **Renderizado de Informes PDF:** **Android PdfRenderer** en alta resolución con visor interactivo de pantalla completa.
* **Accesibilidad & Síntesis de Voz:** Cumplimiento **WCAG 2.2 AA** (alto contraste, áreas táctiles mínimas de 48dp, TalkBack semántico) y **TextToSpeech (TTS)** con control interactivo (reproducir/detener).
* **Persistencia Offline & Sync:** SQLite Local (`OftDatabase`) con tabla de sincronización pendiente (`sincronizado = 0`) y monitoreo de conectividad con `NetworkMonitor`.
* **Backend Microservicio Spring Boot:** Módulo `backend/oftapp-backend` con **Spring Boot 3.2**, **Spring Data JPA**, **H2 Database** y documentación interactiva **Swagger / OpenAPI** (`/swagger-ui/index.html`).
* **Elementos en Tiempo Real:** Reloj digital dinámico segundo a segundo (`RelojEnVivo`), badges de notificaciones y monitoreo de red.
* **Pruebas Unitarias y UI:** **JUnit 5** (`junit-jupiter`), `LoginTest` y `LoginUITest`.
* **Gestor de Dependencias:** Gradle (Kotlin DSL).

---

## 🚀 Características Principales (Actualizaciones 22 - 23 Noviembre)

### 🔐 1. Autenticación Híbrida y Roles
* Inicio de sesión y registro respaldados por **Insforge Auth** en la nube con fallback automático a **SQLite local** (`OftDatabase`).
* Soporte para perfiles y roles con permisos diferenciados: *Tecnólogo/Técnico, Oftalmólogo, Paciente, Administrador*.

### 🏥 2. Integración FHIR en Tiempo Real (HAPI FHIR R4)
* Conexión remota mediante Retrofit 2 con el servidor estándar **HAPI FHIR R4**.
* Sincronización automática de observaciones clínicas y recursos de pacientes directamente en la pantalla de exámenes.

### 🖼️ 3. Visualización Avanzada de Informes PDF e Imágenes Médicas Táctiles
* **Visor PDF (`PdfRenderer` & `VisorPdfDialog`):** Generación multipágina de alta calidad fuera del hilo principal con vista previa y modo pantalla completa.
* **Imágenes Oftalmológicas Interactivas (Coil + Telephoto):** Inspección detallada de retinografías, mapas de topografía corneal y campimetrías con gestos multitáctiles de zoom (*pinch-to-zoom*), desplazamiento (*pan*) y reajuste con doble toque.

### ♿ 4. Accesibilidad Universal WCAG 2.2 AA & Lectura por Voz (TextToSpeech)
* **Criterios WCAG 2.2 AA:** Modo de alto contraste, tipografía reescalable hasta 200%, áreas de toque táctiles de al menos 48x48 dp y etiquetas semánticas para TalkBack.
* **Síntesis de Voz Interactiva (TTS):** Botón con control de alternancia (reproducir/detener) que dicta las indicaciones, diagnósticos y resúmenes médicos en español.

### 📴 5. Persistencia Offline & Sincronización Automática
* Almacenamiento local SQLite (`OftDatabase`) para consultar exámenes e historiales sin conexión a internet.
* Monitoreo dinámico con `NetworkMonitor` (`ConnectivityManager`) que sincroniza automáticamente registros pendientes cuando se recupera la red.

### 🔔 6. Centro de Notificaciones y Componentes en Tiempo Real
* **Modal de Notificaciones (`NotificacionesDialog`):** Indicador/badge numérico no leído (`9+` / `3`), filtro por categorías (*Exámenes, Recetas, Recordatorios, Sistema*) y opciones para marcar como leídas o borrar.
* **Reloj Digital Clínico en Vivo (`RelojEnVivo`):** Reloj dinámico integrado en las cabeceras que actualiza hora, minutos y segundos segundo a segundo.

### 📱 7. Diseño Adaptativo e Identidad Oficial (Logo `off`)
* Migración del rol técnico a **`NavigationSuiteScaffold`**, adaptando la navegación entre *Bottom Bar* (celulares verticales) y *Navigation Rail* (tablets, plegables u horizontal).
* Integración del logo e icono oficial de la app **`off`** en `app_logo.png` y todas las densidades de `mipmap-*`.

### 💻 8. Microservicio Backend Spring Boot & Manual en PDF
* Módulo backend en `backend/oftapp-backend` con Spring Boot 3.2, JPA, H2 y Swagger UI en `http://localhost:8080/swagger-ui/index.html`.
* Script de automatización en Python (`generar_manual_pdf.py`) para generar el **Manual de Desarrollador en PDF** en el escritorio (`Manual_Dev_OftApp.pdf`).

---

## 📋 Requisitos para Ejecutar

* **Android Studio:** Ladybug (2024.2) o superior.
* **JDK:** Versión 17 o superior.
* **SDK Mínimo (minSdk):** API 24 (Android 7.0) o superior.
* **SDK Objetivo (targetSdk / compileSdk):** API 37.
* **Dispositivo:** Emulador Android (AVD) o dispositivo físico con Depuración USB.
