# Implementation Plan - Insforge Authentication Integration

Integrate remote authentication via **Insforge** (`dev.insforge:insforge-kotlin:0.1.7`) into `OftApp` using the user's custom `baseURL` and `anonKey`.

## User Review Required

- [IMPORTANT] We will configure the Insforge client with:
  - **Base URL:** `https://9c9qzn7v.us-east.insforge.app/`
  - **Anon Key:** `ik_f57cccb589cea1fc5d63d420e55383e1`
- [IMPORTANT] `LoginViewModel` will be updated to authenticate users remotely via `InsforgeClient.client.auth.signIn(email, password)`.

## Proposed Changes

### [Dependency Layer]
#### [MODIFY] [build.gradle.kts](file:///C:/Users/axell/OneDrive/Desktop/VCM_Aplicacion_Mobile/app/build.gradle.kts)
- Add `implementation("dev.insforge:insforge-kotlin:0.1.7")`.

### [Data Layer]
#### [NEW] [InsforgeClient.kt](file:///C:/Users/axell/OneDrive/Desktop/VCM_Aplicacion_Mobile/app/src/main/java/com/losdebuggers/oftapp/data/InsforgeClient.kt)
- Create singleton object configuring `createInsforgeClient` with the user's URL and anonKey, installing `Auth`.

### [ViewModel Layer]
#### [MODIFY] [LoginViewModel.kt](file:///C:/Users/axell/OneDrive/Desktop/VCM_Aplicacion_Mobile/app/src/main/java/com/losdebuggers/oftapp/viewmodel/LoginViewModel.kt)
- Update login method (`enviar()`) to authenticate using `InsforgeClient.client.auth.signIn(...)`, mapping the authenticated user to our `Usuario` model and updating state.

## Verification Plan

### Automated Tests
- Run `./gradlew app:assembleDebug`
- Run `./gradlew app:testDebugUnitTest`

### Manual Verification
- Deploy to Samsung S25 and test login flow with Insforge credentials.
