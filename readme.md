# Project Akhir PBO
Proyek ini adalah dibuat atas dasar kebutuhan tugas akhir praktikum Pemrograman Berbasis Objek. Modul yang dimaksud dalam file ini yaitu modul praktikum yang telah saya pelajari.

## 🛠️ Tech Stack Target

| Layer | Teknologi |
|-------|-----------|
| **Backend** | Java 17+|
| **Server** |  - |
| **Database** | MySQL (menggunakan script `hevy_pemvis.sql`) |
| **Frontend** | HTML5, Tailwind CSS, Vanilla JS |

## Functional Requirements

### 1. Admin
*   **Dapat Mengelola database exercise global**
    *   Sistem dapat menampilkan database global pada admin dan user biasa
    *   Sistem memungkinkan admin melakukan CRUD pada exercise global.
    *   Sistem dapat menyimpan perubahan pada CRUD yang telah dilakukan admin di exercise global.
    *   Sistem dapat melakukan CRUD pada database exercise local juga (database exercise per user yang dibuat custom).
*   **Dapat Mengelola User**
    *   Sistem dapat menampilkan *list* akun *user* yang terdaftar.
    *   Sistem dapat melakukan CRUD pada *user* (contoh : menonaktifkan akundan mereset *password*).
    *   Sistem dapat menyimpan perubahan data akun *user*.

### 2. User
*   **Dapat Mengelola Akun**
    *   Sistem memungkinkan *user* untuk melakukan registrasi akun baru.
    *   Sistem memungkinkan *user* untuk melakukan *login* ke dalam sistem.
*   **Dapat Mengelola Database Exercise Local (Custom Exercise)**
    *   Sistem dapat menampilkan daftar *exercise* (gabungan dari *exercise* global dan *exercise local* yang dibuat oleh *user* tersebut).
    *   Sistem memungkinkan *user* melakukan CRUD pada *exercise local* miliknya sendiri.
    *   Sistem mencegah *user* untuk mengedit atau menghapus *exercise* global maupun *exercise local* milik *user* lain.
*   **Dapat Mengelola Sesi Latihan (Workout Session)**
    *   Sistem memungkinkan *user* untuk memulai sesi *workout* baru.
    *   Sistem memungkinkan *user* untuk mencari dan memilih *exercise* (global maupun *local*) ke dalam sesi *workout* yang sedang aktif.
    *   Sistem memungkinkan *user* melakukan CRUD pada *Set* latihan (mencatat berat beban dalam kg dan jumlah repetisi) pada tiap *exercise* di sesi tersebut.
    *   Sistem memungkinkan *user* untuk menyelesaikan dan menyimpan sesi *workout* beserta seluruh *set*-nya ke dalam *database*.
    *   Sistem memungkinkan *user* untuk membatalkan sesi *workout* yang sedang berjalan tanpa menyimpannya.
*   **Dapat Melihat Riwayat Latihan (History/Dashboard)**
    *   Sistem dapat menampilkan daftar riwayat sesi *workout* yang pernah diselesaikan oleh *user* tersebut.
    *   Sistem dapat menampilkan detail informasi dari riwayat *workout* (durasi, daftar *exercise*, dan log per *set*).
---

# Agile Development Roadmap (Hevy)
Saya membagi pengembangan ini ke dalam **4 Sprint** (setiap Sprint fokus pada satu set modul OOP dan fitur fungsional).

---


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

## Penerapan Modul Java OOP pada Hevy Web
Berdasarkan modul yang Anda berikan, berikut rencana mapping-nya:
1. **Modul 1 & 2 (Basic/Class):** Membuat objek `WorkoutSession` sebagai blueprint utama.
2. **Modul 3 (Encapsulation):** Mengamankan `weight` dan `reps` agar tidak bisa diinput negatif lewat setter. Penggunaan *Access Modifier* (`private`, `public`, `protected`) dan *Getter/Setter*.
3. **Modul 4 (Inheritance):** `WeightedExercise` extends `BaseExercise`.
4. **Modul 5 (Polymorphism):** Menggunakan List `BaseExercise` yang bisa menampung berbagai jenis gerakan.
5. **Modul 6 (Abstraction):** Interface `WorkoutAction` untuk method `start()`, `pause()`, dan `finish()`.