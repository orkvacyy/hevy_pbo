// Fetch gambar exercise dari ExerciseDB API, fallback ke SVG placeholder per muscle group

const RAPIDAPI_KEY = "7afd38d32bmshebac6b7a6645228p1db527jsne309c7f174c3";
const IMAGE_CACHE  = new Map();

const MUSCLE_ICONS = {
  chest:     `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M12 2C8 2 4 5 4 9c0 2 1 4 2 5l1 8h10l1-8c1-1 2-3 2-5 0-4-4-7-8-7z"/></svg>`,
  back:      `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M12 2v20M6 6l6-4 6 4M6 18l6 4 6-4M4 12h16"/></svg>`,
  shoulders: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M12 8a4 4 0 1 0 0-8 4 4 0 0 0 0 8zM4 12c0-2 3-4 8-4s8 2 8 4"/></svg>`,
  biceps:    `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M8 20V10c0-3 2-6 4-6s4 3 4 6v2c0 2-1 3-2 3H8"/></svg>`,
  triceps:   `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M16 20V10c0-3-2-6-4-6S8 7 8 10v2c0 2 1 3 2 3h6"/></svg>`,
  quad:      `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M8 22V12c0-4 1-8 4-8s4 4 4 8v10M8 16h8"/></svg>`,
  hamstring: `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M8 2v10c0 4 1 8 4 8s4-4 4-8V2M8 8h8"/></svg>`,
  core:      `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><circle cx="12" cy="12" r="8"/><path d="M12 8v8M8 12h8"/></svg>`,
  other:     `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><circle cx="12" cy="12" r="10"/><path d="M12 8v4l3 3"/></svg>`,
};

const MUSCLE_COLORS = {
  chest:     "#e63946",
  back:      "#4cc9f0",
  shoulders: "#f8c848",
  biceps:    "#06d6a0",
  triceps:   "#a855f7",
  quad:      "#fb923c",
  hamstring: "#f472b6",
  core:      "#38bdf8",
  other:     "#94a3b8",
};

// Mapping nama exercise kita ke nama ExerciseDB
const EXERCISE_NAME_MAP = {
  "bench press":            "barbell bench press",
  "barbell row":            "bent over barbell row",
  "deadlift":               "deadlift",
  "pull up":                "pull-up",
  "push up":                "push-up",
  "squat":                  "barbell squat",
  "overhead press":         "barbell overhead press",
  "lateral raise":          "dumbbell lateral raise",
  "leg press":              "leg press",
  "romanian deadlift":      "romanian deadlift",
  "incline dumbbell press": "incline dumbbell press",
  "cycling":                "stationary bike run",
  "treadmill run":          "run on treadmill",
  "jump rope":              "jump rope",
};

// Fetch GIF dari ExerciseDB di memory cache
async function fetchExerciseGif(exerciseName) {
  // Cek mapping dulu, kalau ada pakai nama yang sesuai ExerciseDB
  const mapped = EXERCISE_NAME_MAP[exerciseName.toLowerCase()] ?? exerciseName.toLowerCase();
  const key = mapped;
  if (IMAGE_CACHE.has(key)) return IMAGE_CACHE.get(key);

  try {
    const res = await fetch(
      `https://exercisedb.p.rapidapi.com/exercises/name/${encodeURIComponent(mapped)}?limit=1`,
      // ... sisa kode sama
      {
        headers: {
          "X-RapidAPI-Key":  RAPIDAPI_KEY,
          "X-RapidAPI-Host": "exercisedb.p.rapidapi.com",
        },
      }
    );
    if (!res.ok) throw new Error();
    const data = await res.json();
    const gifUrl = data?.[0]?.gifUrl ?? null;
    IMAGE_CACHE.set(key, gifUrl);
    return gifUrl;
  } catch {
    IMAGE_CACHE.set(key, null);
    return null;
  }
}

// Render thumbnail - GIF kalau ada, SVG placeholder kalau tidak
async function renderExerciseThumbnail(exerciseName, muscleGroup, containerEl) {
  const color = MUSCLE_COLORS[muscleGroup] ?? MUSCLE_COLORS.other;
  const icon  = MUSCLE_ICONS[muscleGroup]  ?? MUSCLE_ICONS.other;

  // Tampilkan placeholder dulu sambil fetch
  containerEl.innerHTML = `
    <div class="exercise-thumb placeholder" style="--accent:${color}">
      <div class="thumb-icon">${icon}</div>
    </div>
  `;

  if (RAPIDAPI_KEY === "7afd38d32bmshebac6b7a6645228p1db527jsne309c7f174c3") return;

  const gifUrl = await fetchExerciseGif(exerciseName);
  if (!gifUrl) return;

  containerEl.innerHTML = `
    <div class="exercise-thumb">
      <img src="${gifUrl}" alt="${exerciseName}" loading="lazy"
        onerror="this.parentElement.innerHTML='<div class=\\'exercise-thumb placeholder\\' style=\\'--accent:${color}\\'><div class=\\'thumb-icon\\'>${icon.replace(/'/g, "\\'")}</div></div>'">
    </div>
  `;
}