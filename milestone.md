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
| 3 | Struktur package | ✅ | model, repository, service, controller, dto |
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
| 7 | UI: Library page awal | ✅ | `index.html` |
| 8 | Merge Sprint 2 → `main` | ⬜ | Pending — merge setelah Sprint 4 selesai |

---

## Sprint 3 — Interactivity
**Modul OOP:** Modul 5 (Polymorphism) + Modul 6 (Abstraction)

| # | Item | Status | Catatan |
|---|------|--------|---------|
| 1 | Override `calculateVolume()` via Polymorphism | ✅ | `ExercisePolymorphismTests.java` |
| 2 | Interface `WorkoutAction` (`start`, `pause`, `finish`) | ✅ | `WorkoutSession implements WorkoutAction` |
| 3 | `ExerciseService` | ✅ | Business logic, otorisasi, `calculateTotalVolume()` |
| 4 | `WorkoutService` | ✅ | `startSession()`, `finishSession()`, `cancelSession()`, `addExercise()`, `addSet()`, `deleteSet()` |
| 5 | `ExerciseApiController` | ✅ | GET, POST, PUT, DELETE `/api/exercises` |
| 6 | `WorkoutApiController` | ✅ | Start, finish, cancel, add exercise, add/delete set |
| 7 | UI: Library page | ✅ | Full width table, search, stats |
| 8 | UI: Workout page (`workout.html`) | ✅ | Hevy-style layout, workout kiri library kanan, timer |
| 9 | Merge Sprint 3 → `main` | ⬜ | |

---

## Sprint 4 — Integration & Polish

| # | Item | Status | Catatan |
|---|------|--------|---------|
| 1 | Auth: Login & Register (`login.html`) | ✅ | Tab-based form, BCrypt |
| 2 | `AuthService` dengan BCrypt hashing | ✅ | `register()`, `login()`, session-based auth |
| 3 | `AuthController` | ✅ | POST `/login`, `/register`, `/logout`, GET `/me` |
| 4 | Session guard di semua halaman | ✅ | Redirect ke `/login.html` kalau belum auth |
| 5 | userId otomatis dari session | ✅ | Tidak perlu input manual lagi |
| 6 | DTO layer | ✅ | `dto/request` dan `dto/response` untuk semua endpoint |
| 7 | Refactor semua controller pakai DTO | ✅ | `AuthController`, `ExerciseApiController`, `WorkoutApiController` |
| 8 | Log Set (input berat & reps) | ✅ | Add set & delete set per exercise di `workout.html` |
| 9 | History page (`history.html`) | ✅ | Stats, session list, detail panel kanan |
| 10 | Custom Exercise | ✅ | Modal di library, POST `/api/exercises` dengan `ownerId`, tombol hapus untuk exercise local |
| 11 | Filter muscle group | ✅ | Sudah ada di `workout.html`, belum di `index.html` |
| 12 | Final bug fixing | 🔄 | Ongoing |
| 13 | Dokumentasi README final | ⬜ | |
| 14 | Merge Sprint 2, 3, 4 → `main` | ⬜ | |

---

## NFR Checklist
| Requirement | Status | Implementasi |
|-------------|--------|--------------|
| Password hashing (BCrypt) | ✅ | `AuthService` — `BCryptPasswordEncoder` |
| Otorisasi user tidak bisa akses data user lain | ✅ | `WorkoutService` + `ExerciseService` — cek ownership |
| Page load < 3 detik | 🔄 | Belum di-test formal |
| Data integrity history tidak rusak saat exercise dihapus | ✅ | `exercise_name_snapshot` + soft delete |
| Clean Code & DRY | ✅ | DTO layer, service layer, BaseDao generic |
| OOP pillars diterapkan | ✅ | Semua modul Sprint 1–3 selesai |