# Milestone & Progress Tracker — Hevy PBO

> Update status setiap kali satu item selesai.

---

## Legend
| Simbol | Arti |
|--------|------|
| ✅ | Selesai |
| 🔄 | Sedang dikerjakan |
| ⬜ | Belum dimulai |

---

## Sprint 1 — Foundation & Core OOP
**Modul OOP:** Modul 2 (Class) + Modul 3 (Encapsulation)

| # | Item | Status | Catatan |
|---|------|--------|---------|
| 1 | Generate project Spring Boot | ✅ | Web, JPA, MySQL Driver |
| 2 | Konfigurasi `application.yml` | ✅ | DB: `hevy_pbo`, port 8080 |
| 3 | Struktur package | ✅ | model, repository, service, controller |
| 4 | Class `BaseExercise` (abstract) | ✅ | Private fields, getter/setter manual, abstract `calculateVolume()`, `getCategory()` |
| 5 | Class `StrengthExercise` | ✅ | `calculateVolume` = weight × reps × sets |
| 6 | Class `CardioExercise` | ✅ | `calculateVolume` = duration × sets |
| 7 | Class `WorkoutSession` | ✅ | `start()`, `finish()`, `pause()`, `isActive()`, `getDurationMinutes()` |
| 8 | Class `WorkoutExercise` | ✅ | Menyimpan `exerciseNameSnapshot` |
| 9 | Class `WorkoutSet` | ✅ | Validasi `weightKg` dan `reps` tidak boleh negatif |
| 10 | Class `User` | ✅ | Validasi password tidak boleh kosong, helper `isAdmin()` |
| 11 | Merge Sprint 1 → `main` | ✅ | |

---

## Sprint 2 — Relationship & Logic
**Modul OOP:** Modul 4 (Inheritance) + Modul 3 (Encapsulation lanjut)

| # | Item | Status | Catatan |
|---|------|--------|---------|
| 1 | Class `DatabaseConnection` (Singleton) | ✅ | Auto-reconnect |
| 2 | Interface `BaseDao<T>` | ✅ | Generic CRUD contract, DIP + ISP |
| 3 | `ExerciseDao` | ✅ | Soft delete, `findAllByUser()` |
| 4 | `UserDao` | ✅ | `findByEmail()`, `findByUsername()`, `resetPassword()` |
| 5 | `WorkoutSessionDao` | ✅ | `findByUserId()`, `findActiveByUserId()` |
| 6 | `WorkoutExerciseDao` + `WorkoutSetDao` | ✅ | `findBySessionId()`, `findByWorkoutExerciseId()` |
| 7 | UI: Tampilkan daftar Library Exercise | ✅ | `index.html` |
| 8 | Merge Sprint 2 → `main` | ⬜ | Pending |

---

## Sprint 3 — Interactivity
**Modul OOP:** Modul 5 (Polymorphism) + Modul 6 (Abstraction)

| # | Item | Status | Catatan |
|---|------|--------|---------|
| 1 | Override `calculateVolume()` via Polymorphism | ✅ | `ExercisePolymorphismTests.java` |
| 2 | Interface `WorkoutAction` (`start`, `pause`, `finish`) | ✅ | `WorkoutSession implements WorkoutAction` |
| 3 | `ExerciseService` | ✅ | Business logic, otorisasi, `calculateTotalVolume()` |
| 4 | `WorkoutService` | ✅ | `startSession()`, `finishSession()`, `cancelSession()`, `addExercise()`, `addSet()`, `deleteSet()` |
| 5 | `ExerciseApiController` | ✅ | `GET /api/exercises` |
| 6 | `WorkoutApiController` | ✅ | Start, finish, cancel, add exercise, add set, delete set |
| 7 | UI: Library page | ✅ | Full width table, search, stats |
| 8 | UI: Workout page (`workout.html`) | ✅ | Hevy-style layout, workout kiri library kanan, timer |
| 9 | Merge Sprint 3 → `main` | ✅ |  |

---

## Sprint 4 — Integration & Polish

| # | Item | Status | Catatan |
|---|------|--------|---------|
| 1 | Auth: Login & Register (`login.html`) | ✅ | Tab-based form, BCrypt |
| 2 | `AuthService` dengan BCrypt hashing | ✅ | `register()`, `login()`, session-based auth |
| 3 | `AuthController` | ✅ | `POST /api/auth/login`, `/register`, `/logout`, `GET /me` |
| 4 | Session guard di semua halaman | ✅ | Redirect ke `/login.html` kalau belum auth |
| 5 | userId otomatis dari session | ✅ | Tidak perlu input manual lagi |
| 6 | Log Set (input berat & reps) | ✅ | Add set & delete set per exercise di workout page |
| 7 | History page (`history.html`) | ✅ | Stats, session list, detail panel |
| 8 | Custom Exercise (user bisa tambah exercise sendiri) | ⬜ | Modal di library page |
| 9 | Filter exercise by muscle group | 🔄 | Sudah ada di `workout.html`, belum di `index.html` |
| 10 | Refinement UI | ✅ | Dark mode, Syne + DM Sans, aksen merah |
| 11 | Final bug fixing | 🔄 | Ongoing |
| 12 | Dokumentasi final | ⬜ | Update README |
| 13 | Merge Sprint 4 → `main` | ⬜ | |

---

## NFR Checklist
| Requirement | Status | Implementasi                                         |
|-------------|--------|------------------------------------------------------|
| Password hashing (BCrypt) | ✅ | `AuthService` — `BCryptPasswordEncoder`              |
| Otorisasi user tidak bisa akses data user lain | ✅ | `WorkoutService` + `ExerciseService` — cek ownership |
| Page load < 3 detik | 🔄 | Belum di tes                                         |
| Data integrity history tidak rusak saat exercise dihapus | ✅ | `exercise_name_snapshot` + soft delete               |
| Clean Code & DRY | 🔄 | Ongoing                                              |
| OOP pillars diterapkan | ✅ | Sprint 1–3 selesai                                   |