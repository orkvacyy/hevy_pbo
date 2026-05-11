# Milestone & Progress Tracker — Hevy PBO

> Dokumen ini mencatat progress pengembangan project akhir Pemrograman Berbasis Objek.
> Update status setiap kali satu item selesai.

---

## Legend
| Simbol | Arti |
|--------|------|
| ✅ | Selesai |
| 🔄 | Sedang dikerjakan |
| ⬜ | Belum dimulai |

---

## Sprint 1 - Core OOP
**Fokus:** Setup arsitektur, database, dan class model dasar.
**Modul OOP:** Modul 2 (Class) + Modul 3 (Encapsulation)

| # | Item | Status | Catatan                                                            |
|---|------|--------|--------------------------------------------------------------------|
| 1 | Generate project Spring Boot via start.spring.io | ✅ | Web, JPA, MySQL Driver                                             |
| 2 | Konfigurasi `application.yml` koneksi MySQL | ✅ | DB: `hevy_pbo`, port 8080                                          |
| 3 | Struktur package (`model`, `repository`, `service`, `controller`) | ✅ | Package `model` dibuat                                             |
| 4 | Class `BaseExercise` (abstract) | ✅ | Private fields, getter/setter manual, abstract `calculateVolume()` |
| 5 | Class `StrengthExercise` extends `BaseExercise` | ✅ | `calculateVolume` = weight x reps x sets                           |
| 6 | Class `CardioExercise` extends `BaseExercise` | ✅ | `calculateVolume` = duration x sets                                |
| 7 | Class `WorkoutSession` | ✅ | `start()`, `finish()`, `isActive()`, `getDurationMinutes()`        |
| 8 | Class `WorkoutExercise` | ✅ | Menyimpan `exerciseNameSnapshot`                                   |
| 9 | Class `WorkoutSet` | ✅ | Validasi `weightKg` dan `reps` tidak boleh negatif                 |
| 10 | Class `User` | ✅ | Validasi password tidak boleh kosong, helper `isAdmin()`           | |

---

## Sprint 2 - Relationship & Logic
**Fokus:** Koneksi database dan DAO layer.
**Modul OOP:** Modul 4 (Inheritance lanjut) + Modul 3 (Encapsulation lanjut)

| # | Item | Status | Catatan |
|---|------|--------|---------|
| 1 | Class `DatabaseConnection` (singleton) | ⬜ | |
| 2 | Interface / Abstract `BaseDao<T>` | ⬜ | Generic CRUD contract |
| 3 | `ExerciseDao` — CRUD exercise (global + local) | ⬜ | Query: Q1 di `query.sql` |
| 4 | `UserDao` — CRUD user | ⬜ | |
| 5 | `WorkoutSessionDao` — simpan & ambil sesi | ⬜ | Query: Q2, Q4 di `query.sql` |
| 6 | `WorkoutExerciseDao` + `WorkoutSetDao` | ⬜ | Query: Q3 di `query.sql` |
| 7 | UI: Tampilkan daftar Library Exercise | ⬜ | HTML + Vanilla JS |
| 8 | Merge Sprint 2 → `main` | ⬜ | |

---

## Sprint 3 - Interactivity
**Fokus:** Polymorphism, Abstraction, dan form dinamis.
**Modul OOP:** Modul 5 (Polymorphism) + Modul 6 (Abstraction)

| # | Item | Status | Catatan |
|---|------|--------|---------|
| 1 | Override `calculateVolume()` diuji via Polymorphism | ⬜ | List`<BaseExercise>` campur Strength & Cardio |
| 2 | Interface `WorkoutAction` (`start`, `pause`, `finish`) | ⬜ | Modul 6 Abstraction |
| 3 | Service layer: `ExerciseService`, `WorkoutService` | ⬜ | Business logic + otorisasi |
| 4 | REST Controller dasar | ⬜ | |
| 5 | UI: Form tambah sesi workout | ⬜ | Dynamic HTML |
| 6 | UI: Pemilihan exercise ke dalam sesi | ⬜ | |
| 7 | Merge Sprint 3 → `main` | ⬜ | |

---

## Sprint 4 - Integration & Polish
**Fokus:** UX, error handling, dan testing.

| # | Item | Status | Catatan |
|---|------|--------|---------|
| 1 | Fitur "Log Set" (input berat & repetisi) | ⬜ | |
| 2 | Fitur Filter exercise (muscle group, equipment) | ⬜ | Index DB sudah siap |
| 3 | Auth: Login & Register | ⬜ | BCrypt untuk password hashing |
| 4 | Otorisasi: user tidak bisa akses data user lain | ⬜ | NFR Security |
| 5 | Refinement UI (mirip hevy.com) | ⬜ | Tailwind CSS |
| 6 | Final bug fixing | ⬜ | |
| 7 | Dokumentasi final | ⬜ | Update README |
| 8 | Merge Sprint 4 → `main` | ⬜ | |

---

## NFR Checklist
| Requirement | Status | Implementasi |
|-------------|--------|--------------|
| Password hashing (BCrypt) | ⬜ | Sprint 4 — Auth service |
| Otorisasi ketat (user tidak bisa akses data user lain) | ⬜ | Sprint 3 — Service layer |
| Page load < 3 detik | ⬜ | Sprint 4 — Testing |
| Data integrity history tidak rusak saat exercise dihapus | ✅ | `exercise_name_snapshot` + soft delete |
| Clean Code & DRY | 🔄 | Ongoing |
| OOP pillars diterapkan | 🔄 | Sprint 1 ✅, Sprint 2-3 ⬜ |