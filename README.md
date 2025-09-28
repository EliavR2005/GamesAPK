# NeoGames

NeoGames es una aplicación móvil desarrollada en **Android Studio** con **Kotlin** y **Jetpack Compose**, diseñada para ofrecer entretenimiento seguro y atractivo mediante una estética oscura y moderna. Incluye tres juegos clásicos integrados en una interfaz intuitiva y fácil de usar.

---

## Descripción General

NeoGames combina entretenimiento, diseño atractivo y seguridad. Su propósito es ofrecer una plataforma donde los usuarios puedan disfrutar de juegos mientras su información está protegida mediante **autenticación biométrica** (huella digital), correo y contraseña.

El diseño busca captar la atención del usuario mediante colores brillantes sobre un fondo oscuro, mejorando la experiencia visual y manteniendo una navegación fluida.

---

## Objetivo

Desarrollar una aplicación móvil que:
- Ofrezca una experiencia visual atractiva y coherente.
- Integre múltiples juegos interactivos.
- Garantice seguridad mediante métodos modernos de autenticación.
- Sea modular y escalable para futuras expansiones.

---

## Características Principales

- **Interfaz Dark Neon:** diseño moderno con colores brillantes y tipografía legible.
- **Autenticación segura:** inicio de sesión con correo, contraseña y huella digital.
- **Juegos incluidos:**
  - **Memorama:** juego de memoria con tablero dinámico, cartas con efecto visual glow, contador de movimientos y notificación de victoria.
  - **Sudoku:** tablero dinámico 9×9 con selección de dificultad, pad numérico, controles de deshacer/rehacer y opción para resolver automáticamente.
  - **TicTacToe:** versión estilizada del clásico juego de “Gato”, con diseño moderno, marcador dinámico, mensajes emergentes y opciones de reinicio.

---

## Arquitectura del Proyecto

El proyecto está dividido en módulos:
- **LoginActivity:** acceso seguro con huella digital.
- **RegisterActivity:** registro con validación de datos.
- **MainActivity:** menú principal para elegir categorías.
- **PuzzleActivity:** pantalla principal para acceder a los juegos.
- **MemoramaActivity, SudokuActivity, TicTacToeActivity:** implementaciones individuales de cada juego.

---

## Seguridad

- Autenticación mediante **BiometricPrompt**.
- Validaciones en registro y login.
- Almacenamiento cifrado de credenciales.
- Control de acceso basado en roles.

---

## Escalabilidad

NeoGames puede evolucionar hacia:
- Integración con bases de datos remotas (Firebase, MySQL, Supabase).
- Soporte multiplataforma (iOS, Progressive Web App).
- Gamificación del login (logros, recompensas).
- Integración con APIs externas y sistema de pagos.

---

## Reseñas de Usuarios

- Brayan: “Los juegos están bien producidos, se siente muy profesional.”
- María G.: “Me encantó el diseño, es diferente a lo que normalmente se ve en apps.”
- Johan H.: “El registro fue fácil y rápido, sin complicaciones.”
- Ana P.: “La interfaz es sencilla pero futurista, intuitiva.”
- Omar T.: “La implementación de la huella es excelente.”

---

## Conclusión

NeoGames es una aplicación que combina entretenimiento, seguridad y diseño moderno, ofreciendo una experiencia atractiva con tres juegos clásicos listos para disfrutar. El proyecto está preparado para crecer hacia una plataforma robusta y versátil.

