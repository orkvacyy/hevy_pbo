# Agile Development Roadmap (Hevy)

Proyek ini adalah dibuat atas dasar kebutuhan tugas akhir praktikum Pemrograman Berbasis Objek. Modul yang dimaksud dalam file ini yaitu modul praktikum yang telah saya pelajari.

---

## Sprint Plan

Saya membagi pengembangan ini ke dalam **4 Sprint** (setiap Sprint fokus pada satu set modul OOP dan fitur fungsional).

### Sprint 1: Foundation & Core OOP (Modul 1 & 2)
**Fokus:** Setup Arsitektur, Database, dan Class.
- **Tujuan:** Membuat struktur project dan representasi data dasar.
- **Backlog:**
  - Setup Project.
  - Implementasi **Modul 2 (Class)**: Membuat class `Exercise`, `Workout`, dan `Set`.
  - Implementasi **Modul 3 (Encapsulation)**: Proteksi data dengan private fields dan getter-setter pada model.
  - Setup database MySQL sesuai skema `hevy_pemvis.sql`.
  - UI: Mockup Dashboard sederhana.

### Sprint 2: Relationship & Logic (Modul 3 & 4)
**Fokus:** Implementasi Hirarki dan Koneksi Database.
- **Tujuan:** Menghubungkan antar entitas dan menarik data dari DB.
- **Backlog:**
  - Implementasi **Modul 4 (Inheritance)**: Membuat spesialisasi exercise (Contoh: `StrengthExercise` vs `CardioExercise`).
  - Membuat `DatabaseConnection` class.
  - Membuat Data Access Object untuk CRUD `exercises` dan `workouts`.
  - UI: Menampilkan daftar Library Exercise.

### Sprint 3: Interactivity (Modul 5 & 6)
**Fokus:** Fleksibilitas Kode dan Kontrak Abstraksi.
- **Tujuan:** Membuat sistem yang modular dan reusable.
- **Backlog:**
  - Implementasi **Modul 5 (Polymorphism)**: Overriding method `calculateVolume()` pada tiap tipe exercise yang berbeda.
  - Implementasi **Modul 6 (Abstraction)**: Membuat `interface` atau `abstract class` untuk CRUD.
  - UI: Form tambah sesi workout dan pemilihan exercise (Dynamic HTML).

### Sprint 4: Integration
**Fokus:** UX, Error Handling, dan Blackbox Testing.
- **Tujuan:** Memastikan aplikasi berjalan mulus sesuai dengan ekspetasi pembuat.
- **Backlog:**
  - Integrasi fungsi "Log Set" (Input berat & repetisi).
  - Implementasi Fitur Filter (Muscle Group).
  - Refinement UI agar mirip dengan hevy.com.
  - Final Bug Fixing & Dokumentasi.

---

## 🛠️ Tech Stack Target

| Layer | Teknologi |
|-------|-----------|
| **Backend** | Java 17+|
| **Server** |  - |
| **Database** | MySQL (menggunakan script `hevy_pemvis.sql`) |
| **Frontend** | HTML5, Tailwind CSS, Vanilla JS |

---

## Penerapan Modul Java OOP pada Hevy Web

Berdasarkan modul yang Anda berikan, berikut rencana mapping-nya:
1. **Modul 1 & 2 (Basic/Class):** Membuat objek `WorkoutSession` sebagai blueprint utama.
2. **Modul 3 (Encapsulation):** Mengamankan `weight` dan `reps` agar tidak bisa diinput negatif lewat setter.
3. **Modul 4 (Inheritance):** `WeightedExercise` extends `BaseExercise`.
4. **Modul 5 (Polymorphism):** Menggunakan List `BaseExercise` yang bisa menampung berbagai jenis gerakan.
5. **Modul 6 (Abstraction):** Interface `WorkoutAction` untuk method `start()`, `pause()`, dan `finish()`.