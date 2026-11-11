# 📱 PreveeApp

Esta app busca hacer una app que permita la preveecion de desastres naturales dando consejos
mediantes guías y funciones que permitan una mayo facilidad al hacer estas preveenciones.

---

- Característica 1: Autenticación con Firebase
- Característica 2: App basica rapida y facil de usar
- Característica 3: Colores fries para no dañar la vista en un desastre natural

---

* **Lenguaje:Java  
* **Diseño de Interfaz:Vistas XML
* **Arquitectura:** [MVVM / Clean Architecture]
* **Conexión a Red:** [Retrofit / Ktor]
* **Base de Datos:** [Room / SQLite]

---

* **Android Studio:** Versión Jellyfish / Ladybug o superior
* **Versión de Java:** JDK 17+
* **SDK Mínimo:** API (Android 7.0)
* **SDK Objetivo:** API (Android 15)

---

1. Clona este repositorio en tu máquina local:
   ```bash
   git clone https://github.com
   ```
2. Abre **Android Studio** y selecciona la opción **Abrir** para cargar el proyecto.
3. Espera a que termine la sincronización inicial de **Gradle** (**Archivo** > **Sincronizar
   proyecto con archivos Gradle**).
4. Conecta un dispositivo físico o inicia un emulador y haz clic en el botón **Ejecutar** (ícono de
   play verde).

---

```text
app/
├── src/
│   ├── main/
│   │   ├── java/com/ejemplo/app/
│   │   │   ├── data/       # Repositorios y fuentes de datos (Local/Remoto)
│   │   │   ├── domain/     # Casos de uso y modelos de negocio
│   │   │   └── ui/         # Pantallas, componentes visuales y ViewModels
│   │   └── res/            # Recursos del sistema (imágenes, textos, temas)
```

---

¡Las contribuciones y sugerencias son bienvenidas! Si deseas mejorar el proyecto, sigue estos pasos:

1. Haz un **Fork** de este repositorio.
2. Crea una rama para tu nueva función o corrección (`git checkout -b feature/NuevaMejora`).
3. Guarda tus cambios en un commit descriptivo (`git commit -m 'Añade nueva mejora'`).
4. Sube la rama a tu repositorio remoto (`git push origin feature/NuevaMejora`).
5. Abre una solicitud de **Pull Request** detallando tus cambios.

---

Este proyecto está bajo la Licencia **MIT**. Para más información, consulta el archivo `LICENSE`.
