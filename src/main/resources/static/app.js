const state = {
  exercises: [],
  search: "",
  userId: Number(document.getElementById("userIdInput").value) || 1,
};

const $ = (id) => document.getElementById(id);
const tableBody     = $("exerciseTableBody");
const statusMsg     = $("statusMessage");
const searchInput   = $("searchInput");
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
}

function buildRow(ex) {
  const tr = document.createElement("tr");
  tr.innerHTML = `
    <td class="font-medium text-white/90">${esc(ex.name)}</td>
    <td><span class="badge badge-${esc(ex.category)}">${esc(ex.category)}</span></td>
    <td>${esc(ex.muscleGroup)}</td>
    <td>${esc(ex.equipment)}</td>
    <td><span class="badge badge-${esc(ex.scope)}">${esc(ex.scope)}</span></td>
  `;
  return tr;
}

// helper

function esc(v) {
  return String(v ?? "")
    .replace(/&/g, "&amp;").replace(/</g, "&lt;")
    .replace(/>/g, "&gt;").replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}


searchInput.addEventListener("input", (e) => { state.search = e.target.value; render(); });
refreshButton.addEventListener("click", loadExercises);


loadExercises();