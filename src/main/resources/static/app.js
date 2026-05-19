const state = {
  exercises: [],
  search: "",
  userId: Number(document.getElementById("userIdInput").value) || 1,
  activeSession: null,
};

const $ = (id) => document.getElementById(id);
const tableBody      = $("exerciseTableBody");
const statusMsg      = $("statusMessage");
const searchInput    = $("searchInput");
const refreshButton  = $("refreshButton");
const totalCount     = $("totalCount");
const strengthCount  = $("strengthCount");
const cardioCount    = $("cardioCount");
const startBtn       = $("startSessionButton");
const finishBtn      = $("finishSessionButton");
const cancelBtn      = $("cancelSessionButton");
const sessionIdLabel = $("sessionIdLabel");
const sessionStatus  = $("sessionStatusLabel");
const sessionMsg     = $("sessionMessage");
const exerciseList   = $("selectedExerciseList");
const notesInput     = $("notesInput");

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

async function loadActiveSession() {
  try {
    const res = await fetch(`/api/workouts/active?userId=${state.userId}`);
    if (res.status === 204) { state.activeSession = null; renderSession(); return; }
    if (!res.ok) throw new Error();
    state.activeSession = await res.json();
    renderSession();
  } catch {
    state.activeSession = null;
    renderSession();
  }
}

// session

async function startSession() {
  try {
    state.activeSession = await postJson("/api/workouts/start", { userId: state.userId });
    renderSession("Sesi dimulai.");
  } catch (e) { renderSession(e.message); }
}

async function addExercise(exerciseId) {
  if (!state.activeSession) { renderSession("Mulai sesi dulu."); return; }
  try {
    await postJson(`/api/workouts/${state.activeSession.id}/exercises`, {
      userId: state.userId,
      exerciseId,
    });
    await loadActiveSession();
    renderSession("Exercise ditambahkan.");
  } catch (e) { renderSession(e.message); }
}

async function addSet(workoutExerciseId, weightKg, reps) {
  try {
    await postJson(`/api/workouts/exercises/${workoutExerciseId}/sets`, {
      userId: state.userId,
      weightKg,
      reps,
    });
    await loadActiveSession();
    renderSession("Set ditambahkan.");
  } catch (e) { renderSession(e.message); }
}

async function deleteSet(setId) {
  try {
    const res = await fetch(
      `/api/workouts/exercises/sets/${setId}?userId=${state.userId}`,
      { method: "DELETE" }
    );
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    await loadActiveSession();
    renderSession("Set dihapus.");
  } catch (e) { renderSession(e.message); }
}

async function finishSession() {
  if (!state.activeSession) return;
  try {
    const res = await postJson(`/api/workouts/${state.activeSession.id}/finish`, {
      userId: state.userId,
      notes: notesInput.value,
    });
    state.activeSession = res.active ? res : null;
    notesInput.value = "";
    renderSession("Sesi selesai.");
  } catch (e) { renderSession(e.message); }
}

async function cancelSession() {
  if (!state.activeSession) return;
  try {
    const res = await fetch(
      `/api/workouts/${state.activeSession.id}?userId=${state.userId}`,
      { method: "DELETE" }
    );
    if (!res.ok) {
      const p = await res.json().catch(() => ({}));
      throw new Error(p.message || `HTTP ${res.status}`);
    }
    state.activeSession = null;
    notesInput.value = "";
    renderSession("Sesi dibatalkan.");
  } catch (e) { renderSession(e.message); }
}

// ─── Render ───────────────────────────────────────────────────────────────────

function render() {
  const keyword = state.search.toLowerCase();
  const filtered = state.exercises.filter((ex) =>
    [ex.name, ex.category, ex.muscleGroup, ex.equipment, ex.scope].some(
      (v) => String(v).toLowerCase().includes(keyword)
    )
  );

  tableBody.innerHTML = "";
  filtered.forEach((ex) => tableBody.append(buildRow(ex)));

  totalCount.textContent    = state.exercises.length;
  strengthCount.textContent = state.exercises.filter((e) => e.category === "strength").length;
  cardioCount.textContent   = state.exercises.filter((e) => e.category === "cardio").length;
  statusMsg.textContent     = filtered.length === 0
    ? "Tidak ada exercise yang cocok."
    : `${filtered.length} exercise ditampilkan.`;

  renderSession();
}

function buildRow(ex) {
  const tr = document.createElement("tr");
  tr.innerHTML = `
    <td class="font-medium text-white/90">${esc(ex.name)}</td>
    <td><span class="badge badge-${esc(ex.category)}">${esc(ex.category)}</span></td>
    <td>${esc(ex.muscleGroup)}</td>
    <td>${esc(ex.equipment)}</td>
    <td><span class="badge badge-${esc(ex.scope)}">${esc(ex.scope)}</span></td>
    <td><button class="btn-add" data-id="${esc(ex.id)}">+ Add</button></td>
  `;
  return tr;
}

function renderSession(msg) {
  const s = state.activeSession;
  sessionIdLabel.textContent = s ? s.id : "-";
  sessionStatus.textContent  = s ? (s.paused ? "Paused" : "Active") : "Idle";
  sessionMsg.textContent     = msg || (s ? `${s.exercises.length} exercise dipilih.` : "Belum ada sesi aktif.");

  exerciseList.innerHTML = "";
  (s?.exercises ?? []).forEach((ex) => {
    exerciseList.append(buildExerciseItem(ex));
  });

  finishBtn.disabled = !s;
  cancelBtn.disabled = !s;
}

function buildExerciseItem(ex) {
  const li = document.createElement("li");
  li.className = "px-5 py-3";
  li.innerHTML = `
    <div class="flex items-center justify-between mb-2">
      <span class="text-sm font-semibold text-white/90">${esc(ex.orderIndex)}. ${esc(ex.exerciseNameSnapshot)}</span>
      <span class="text-xs text-white/30">${esc(ex.sets.length)} set</span>
    </div>

    <!-- Daftar set yang sudah ada -->
    <ul class="space-y-1 mb-2">
      ${ex.sets.map((set) => `
        <li class="flex items-center justify-between text-xs text-white/50 bg-white/5 rounded px-2 py-1">
          <span>Set ${esc(set.setNumber)}: ${esc(set.weightKg)} kg × ${esc(set.reps)} reps</span>
          <button class="text-red-400/60 hover:text-red-400 transition-colors" data-delete-set="${esc(set.id)}">✕</button>
        </li>
      `).join("")}
    </ul>

    <!-- Form tambah set -->
    <div class="flex gap-1.5 items-center">
      <input type="number" placeholder="kg" min="0" step="0.5"
        class="set-weight w-16 bg-white/5 border border-white/10 rounded px-2 py-1 text-xs text-white placeholder-white/20 focus:outline-none focus:border-[#e63946]/60"
        data-we-id="${esc(ex.id)}">
      <input type="number" placeholder="reps" min="1"
        class="set-reps w-16 bg-white/5 border border-white/10 rounded px-2 py-1 text-xs text-white placeholder-white/20 focus:outline-none focus:border-[#e63946]/60"
        data-we-id="${esc(ex.id)}">
      <button class="btn-add-set flex-1 text-xs py-1 rounded" data-we-id="${esc(ex.id)}">+ Set</button>
    </div>
  `;
  return li;
}

// helper

async function postJson(url, body) {
  const res = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  const text = await res.text();
  const data = text ? JSON.parse(text) : {};
  if (!res.ok) throw new Error(data.message || `HTTP ${res.status}`);
  return data;
}

function esc(v) {
  return String(v ?? "")
    .replace(/&/g, "&amp;").replace(/</g, "&lt;")
    .replace(/>/g, "&gt;").replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}


searchInput.addEventListener("input", (e) => { state.search = e.target.value; render(); });

tableBody.addEventListener("click", (e) => {
  const btn = e.target.closest("[data-id]");
  if (btn) addExercise(Number(btn.dataset.id));
});

// event tombol session panel
exerciseList.addEventListener("click", (e) => {
  //hapus set
  const deleteBtn = e.target.closest("[data-delete-set]");
  if (deleteBtn) { deleteSet(Number(deleteBtn.dataset.deleteSet)); return; }

  // add set
  const addSetBtn = e.target.closest("[data-we-id].btn-add-set");
  if (!addSetBtn) return;

  const weId     = Number(addSetBtn.dataset.weId);
  const weightEl = exerciseList.querySelector(`.set-weight[data-we-id="${weId}"]`);
  const repsEl   = exerciseList.querySelector(`.set-reps[data-we-id="${weId}"]`);

  const weightKg = parseFloat(weightEl?.value ?? 0);
  const reps     = parseInt(repsEl?.value ?? 0);

  if (!reps || reps < 1) { renderSession("Reps wajib diisi."); return; }
  if (weightKg < 0)      { renderSession("Berat tidak boleh negatif."); return; }

  addSet(weId, weightKg, reps);
});

refreshButton.addEventListener("click", async () => {
  await loadExercises();
  await loadActiveSession();
});

startBtn.addEventListener("click", startSession);
finishBtn.addEventListener("click", finishSession);
cancelBtn.addEventListener("click", cancelSession);


loadExercises();
loadActiveSession();