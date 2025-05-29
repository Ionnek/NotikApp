Mobile Android client for an offline notes+checkboxes app built as a learning pet-project.  
(Room + Paging + Jetpack Compose)  
Kotlin • Material 3

> Unlike my chat example, this project is completely local-only: **all data lives on-device**.  
> No network or backend service is required.

---

### Features
| Category | Details |
|----------|---------|
| **Offline persistence** | • Notes stored in **Room** database<br>• Seamless infinite scrolling with **Paging 3** |
| **Rich content** | • Add **photos** to any note (pick from gallery or camera)<br>• Markdown-style formatting ready *(planned)* |
| **Tag system** | • Create / edit / delete tags<br>• **Filter** notes by one or many tags |
| **Adaptive UI** | • **Lazy-Staggered-Grid** layout with **user-configurable column count** (1–4)<br>• Full **Material 3** theming<br>• Smooth **animate[ *]AsDp** & cross-fade transitions |
| **Architecture** | • Clean MVVM<br>• **StateFlow** for reactive state<br>• Single-Activity **Navigation-Compose** |
| **DI & tooling** | • **Hilt** for dependency injection<br>• **Coil** for image loading<br>• StrictMode & LeakCanary enabled in debug |
| **Tests** | • DAO unit tests (in-memory Room)

---

### Tech Stack
Jetpack Compose · Material 3 · Navigation-Compose  
Hilt · Room · Paging 3 · Coil  
Coroutines · StateFlow · Kotlin Serialization *(soon)*

---

### RUN
**Requirements**
* Android Studio **Jellyfish / Flamingo** or newer
* **JDK 17**
* Android SDK **API 24 → 34**

**Guide to start**
1. **Clone** this repo & open in Android Studio.
2. *(Optional)* In `ui/grid/GridSettings.kt` set your default `columns = 2‒4`.
3. Press **Run ▶** — that’s it! No server to configure.

---

*PRs and suggestions are welcome — this is a playground for new ideas!*
