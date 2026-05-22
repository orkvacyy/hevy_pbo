const state = {
    exercises: [],
    search: "",
    muscleFilter: "",
    userId: 0,
};

const $ = (id) => document.getElementById(id);
const tableBody     = $("exerciseTableBody");
const statusMsg     = $("statusMessage");
const searchInput   = $("searchInput");
const muscleSelect  = $("muscleFilter");
const refreshButton = $("refreshButton");
const totalCount    = $("totalCount");
const strengthCount = $("strengthCount");
const cardioCount   = $("cardioCount");

// data fetch
async function loadExercises() {
    statusMsg.textContent = "Memuat library...";
    try {
        const res = await fetch(`/api/exercises?userId=${state.userId}`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        state.exercises = await res.json();
        render();
    } catch {
        state.exercises = [];
        render();
        statusMsg.textContent = "Gagal memuat. Pastikan database aktif.";
    }
}

// custom exercise
async function submitCustomExercise() {
    const name      = $("exName").value.trim();
    const category  = $("exCategory").value;
    const muscle    = $("exMuscle").value;
    const equipment = $("exEquipment").value;
    const errEl     = $("modalError");

    if (!name) { errEl.textContent = "Nama exercise wajib diisi."; return; }
    errEl.textContent = "";

    try {
        const res = await fetch("/api/exercises", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                name,
                category,
                muscleGroup: muscle,
                equipment,
                ownerId: state.userId,
            }),
        });
        const data = await res.json();
        if (!res.ok) { errEl.textContent = data.message || "Gagal menyimpan."; return; }
        closeModal();
        await loadExercises();
    } catch {
        errEl.textContent = "Terjadi kesalahan, coba lagi.";
    }
}

async function deleteExercise(exerciseId) {
    if (!confirm("Hapus exercise ini?")) return;
    try {
        const res = await fetch(`/api/exercises/${exerciseId}?userId=${state.userId}`, {
            method: "DELETE",
        });
        if (!res.ok) {
            const data = await res.json().catch(() => ({}));
            alert(data.message || "Gagal menghapus.");
            return;
        }
        await loadExercises();
    } catch {
        alert("Terjadi kesalahan, coba lagi.");
    }
}

// rend
function render() {
    const keyword = state.search.toLowerCase();
    const muscle  = state.muscleFilter;

    const filtered = state.exercises.filter((ex) => {
        const matchSearch = [ex.name, ex.category, ex.muscleGroup, ex.equipment, ex.scope]
            .some((v) => String(v).toLowerCase().includes(keyword));
        const matchMuscle = !muscle || ex.muscleGroup === muscle;
        return matchSearch && matchMuscle;
    });

    tableBody.innerHTML = "";
    filtered.forEach((ex) => tableBody.append(buildRow(ex)));

    totalCount.textContent    = state.exercises.length;
    strengthCount.textContent = state.exercises.filter((e) => e.category === "strength").length;
    cardioCount.textContent   = state.exercises.filter((e) => e.category === "cardio").length;
    statusMsg.textContent     = filtered.length === 0
        ? "Tidak ada exercise yang cocok."
        : `${filtered.length} exercise ditampilkan.`;
}

function buildRow(ex) {
    const isLocal = ex.scope === "local";
    const tr = document.createElement("tr");
    tr.innerHTML = `
        <td class="font-medium text-white/90">${esc(ex.name)}</td>
        <td><span class="badge badge-${esc(ex.category)}">${esc(ex.category)}</span></td>
        <td>${esc(ex.muscleGroup)}</td>
        <td>${esc(ex.equipment)}</td>
        <td><span class="badge badge-${esc(ex.scope)}">${esc(ex.scope)}</span></td>
        <td>
            ${isLocal
                ? `<button class="btn-delete text-xs px-3 py-1.5 rounded" data-del-id="${esc(ex.id)}">Hapus</button>`
                : `<span class="text-xs text-white/20">—</span>`}
        </td>
    `;
    return tr;
}

// ── Helper ──────────────────────────────────────────────────────────────────
function esc(v) {
    return String(v ?? "")
        .replace(/&/g, "&amp;").replace(/</g, "&lt;")
        .replace(/>/g, "&gt;").replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

// ── Event listeners ─────────────────────────────────────────────────────────
searchInput.addEventListener("input", (e) => { state.search = e.target.value; render(); });
muscleSelect.addEventListener("change", (e) => { state.muscleFilter = e.target.value; render(); });
refreshButton.addEventListener("click", loadExercises);

tableBody.addEventListener("click", (e) => {
    const btn = e.target.closest("[data-del-id]");
    if (btn) deleteExercise(Number(btn.dataset.delId));
});

document.addEventListener("keydown", (e) => {
    if (e.key === "Enter" && !document.getElementById("modal").classList.contains("hidden")) {
        submitCustomExercise();
    }
});


function initLibrary(userId) {
    state.userId = userId;
    loadExercises();
}