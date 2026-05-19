const state = {
    exercises: [],
    search: "",
    userId: 1,
    activeSession: null
};

const tableBody = document.querySelector("#exerciseTableBody");
const statusMessage = document.querySelector("#statusMessage");
const searchInput = document.querySelector("#searchInput");
const userIdInput = document.querySelector("#userIdInput");
const refreshButton = document.querySelector("#refreshButton");
const heroStartButton = document.querySelector("#heroStartButton");
const totalCount = document.querySelector("#totalCount");
const strengthCount = document.querySelector("#strengthCount");
const cardioCount = document.querySelector("#cardioCount");
const startSessionButton = document.querySelector("#startSessionButton");
const finishSessionButton = document.querySelector("#finishSessionButton");
const cancelSessionButton = document.querySelector("#cancelSessionButton");
const sessionIdLabel = document.querySelector("#sessionIdLabel");
const sessionStatusLabel = document.querySelector("#sessionStatusLabel");
const sessionMessage = document.querySelector("#sessionMessage");
const selectedExerciseList = document.querySelector("#selectedExerciseList");
const notesInput = document.querySelector("#notesInput");
const previewStatus = document.querySelector("#previewStatus");
const previewSessionName = document.querySelector("#previewSessionName");
const previewExerciseCount = document.querySelector("#previewExerciseCount");
const previewLibraryCount = document.querySelector("#previewLibraryCount");

async function loadExercises() {
    statusMessage.textContent = "Memuat library...";
    try {
        const response = await fetch(`/api/exercises?userId=${encodeURIComponent(state.userId)}`);
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        state.exercises = await response.json();
        render();
    } catch (error) {
        state.exercises = [];
        render();
        statusMessage.textContent = "Library belum bisa dimuat. Pastikan database aktif dan schema sudah dibuat.";
    }
}

async function loadActiveSession() {
    try {
        const response = await fetch(`/api/workouts/active?userId=${encodeURIComponent(state.userId)}`);
        if (response.status === 204) {
            state.activeSession = null;
            renderSession();
            return;
        }
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        state.activeSession = await response.json();
        renderSession();
    } catch (error) {
        state.activeSession = null;
        renderSession("Sesi aktif belum bisa dimuat.");
    }
}

async function startSession() {
    try {
        const response = await postJson("/api/workouts/start", { userId: state.userId });
        state.activeSession = response;
        renderSession("Sesi aktif siap.");
    } catch (error) {
        renderSession(error.message);
    }
}

async function addExercise(exerciseId) {
    if (!state.activeSession) {
        renderSession("Mulai sesi dulu.");
        return;
    }

    try {
        await postJson(`/api/workouts/${state.activeSession.id}/exercises`, {
            userId: state.userId,
            exerciseId
        });
        await loadActiveSession();
        renderSession("Exercise masuk ke sesi.");
    } catch (error) {
        renderSession(error.message);
    }
}

async function finishSession() {
    if (!state.activeSession) {
        renderSession("Belum ada sesi aktif.");
        return;
    }

    try {
        const response = await postJson(`/api/workouts/${state.activeSession.id}/finish`, {
            userId: state.userId,
            notes: notesInput.value
        });
        state.activeSession = response.active ? response : null;
        notesInput.value = "";
        renderSession("Sesi selesai.");
    } catch (error) {
        renderSession(error.message);
    }
}

async function cancelSession() {
    if (!state.activeSession) {
        renderSession("Belum ada sesi aktif.");
        return;
    }

    try {
        const response = await fetch(`/api/workouts/${state.activeSession.id}?userId=${encodeURIComponent(state.userId)}`, {
            method: "DELETE"
        });
        if (!response.ok) {
            const payload = await readJson(response);
            throw new Error(payload.message || `HTTP ${response.status}`);
        }
        state.activeSession = null;
        notesInput.value = "";
        renderSession("Sesi dibatalkan.");
    } catch (error) {
        renderSession(error.message);
    }
}

async function postJson(url, body) {
    const response = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });
    const payload = await readJson(response);
    if (!response.ok) {
        throw new Error(payload.message || `HTTP ${response.status}`);
    }
    return payload;
}

async function readJson(response) {
    const text = await response.text();
    return text ? JSON.parse(text) : {};
}

function render() {
    const filteredExercises = state.exercises.filter((exercise) => {
        const keyword = state.search.toLowerCase();
        return [
            exercise.name,
            exercise.category,
            exercise.muscleGroup,
            exercise.equipment,
            exercise.scope
        ].some((value) => String(value).toLowerCase().includes(keyword));
    });

    tableBody.innerHTML = "";
    for (const exercise of filteredExercises) {
        tableBody.append(createRow(exercise));
    }

    totalCount.textContent = state.exercises.length;
    strengthCount.textContent = state.exercises.filter((exercise) => exercise.category === "strength").length;
    cardioCount.textContent = state.exercises.filter((exercise) => exercise.category === "cardio").length;
    previewLibraryCount.textContent = state.exercises.length;
    statusMessage.textContent = filteredExercises.length === 0
        ? "Tidak ada exercise yang cocok."
        : `${filteredExercises.length} exercise ditampilkan.`;
    renderSession();
}

function createRow(exercise) {
    const row = document.createElement("tr");
    row.innerHTML = `
        <td>${escapeHtml(exercise.name)}</td>
        <td><span class="badge ${escapeHtml(exercise.category)}">${escapeHtml(exercise.category)}</span></td>
        <td>${escapeHtml(exercise.muscleGroup)}</td>
        <td>${escapeHtml(exercise.equipment)}</td>
        <td><span class="badge ${escapeHtml(exercise.scope)}">${escapeHtml(exercise.scope)}</span></td>
        <td><button class="row-button" type="button" data-exercise-id="${escapeHtml(exercise.id)}">Add</button></td>
    `;
    return row;
}

function renderSession(message) {
    const session = state.activeSession;
    sessionIdLabel.textContent = session ? session.id : "-";
    sessionStatusLabel.textContent = session ? "Active" : "Idle";
    previewStatus.textContent = session ? "Live" : "Idle";
    previewSessionName.textContent = session ? `Session #${session.id}` : "Push Session";
    previewExerciseCount.textContent = session ? session.exercises.length : 0;
    sessionMessage.textContent = message || (session ? `${session.exercises.length} exercise dipilih.` : "Belum ada sesi aktif.");

    selectedExerciseList.innerHTML = "";
    const exercises = session ? session.exercises : [];
    for (const exercise of exercises) {
        const item = document.createElement("li");
        item.innerHTML = `
            <span>${escapeHtml(exercise.orderIndex)}. ${escapeHtml(exercise.exerciseNameSnapshot)}</span>
            <small>${escapeHtml(exercise.sets.length)} set</small>
        `;
        selectedExerciseList.append(item);
    }

    finishSessionButton.disabled = !session;
    cancelSessionButton.disabled = !session;
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

searchInput.addEventListener("input", (event) => {
    state.search = event.target.value;
    render();
});

userIdInput.addEventListener("change", async (event) => {
    const nextUserId = Number(event.target.value);
    state.userId = Number.isFinite(nextUserId) && nextUserId > 0 ? nextUserId : 1;
    userIdInput.value = state.userId;
    await loadExercises();
    await loadActiveSession();
});

tableBody.addEventListener("click", (event) => {
    const button = event.target.closest("[data-exercise-id]");
    if (!button) {
        return;
    }
    addExercise(Number(button.dataset.exerciseId));
});

refreshButton.addEventListener("click", async () => {
    await loadExercises();
    await loadActiveSession();
});
startSessionButton.addEventListener("click", startSession);
heroStartButton.addEventListener("click", startSession);
finishSessionButton.addEventListener("click", finishSession);
cancelSessionButton.addEventListener("click", cancelSession);

loadExercises();
loadActiveSession();
