SHOW ENGINES;

create database hevy_pbo;
use hevy_pbo;

-- user
create table users(
	id BIGINT not null auto_increment,
	username varchar(50) not null,
	email varchar(100) not null,
	password varchar(255) not null,
	role ENUM('admin', 'user') not null default 'user',
	is_active boolean not null default true,
	created_at timestamp not null default CURRENT_TIMESTAMP,
	
	primary key(id),
	unique key uq_users_email(email),
	UNIQUE KEY uq_users_username(username)
	);
	
-- exec
create table exercises (
  id BIGINT not null auto_increment,
  name varchar(100) not null,
  category ENUM('strength', 'cardio') not null,
  muscle_group enum(
    'chest',
    'back',
    'shoulders',
    'biceps',
    'triceps',
    'quad',
    'hamstring',
    'core',
    'other'
  ) not null default 'other',
  equipment enum(
    'barbell',
    'dumbbell',
    'machine',
    'cable',
    'bodyweight',
    'other'
  ) not null default 'other',
  owner_id bigint null default null,
  is_deleted boolean not null default false,
  created_at timestamp not null default CURRENT_TIMESTAMP,
  primary key (id),
  constraint fk_exercise_owner foreign key (owner_id) references users (id) on delete set null on update CASCADE
);
	
	
-- workout
create table workout_sessions (
  id bigint not null AUTO_INCREMENT,
  user_id BIGINT not NULL,
  notes TEXT NULL,
  started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  finished_at TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_sessions_user foreign key (user_id) REFERENCES users (id) on delete cascade
  ON UPDATE CASCADE
);

CREATE TABLE workout_exercises (
  id BIGINT NOT NULL AUTO_INCREMENT,
  session_id BIGINT NOT NULL,
  exercise_id BIGINT NOT NULL,
  exercise_name_snapshot VARCHAR(100) NOT NULL, -- just in case dihapus di tabel utama
  order_index INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  CONSTRAINT fk_we_session FOREIGN KEY (session_id) REFERENCES workout_sessions (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_we_exercise FOREIGN KEY (exercise_id) REFERENCES exercises (id) ON DELETE RESTRICT 
  ON UPDATE CASCADE
);

-- sets

create table sets (
  id BIGINT NOT NULL AUTO_INCREMENT,
  workout_exercise_id BIGINT NOT NULL,
  set_number INT NOT NULL,
  weight_kg DECIMAL(6, 2) NOT NULL DEFAULT 0.00 CHECK (weight_kg >= 0),
  reps INT NOT NULL DEFAULT 0 CHECK (reps >= 0), 
  PRIMARY KEY (id),
  CONSTRAINT fk_sets_we FOREIGN KEY (workout_exercise_id) REFERENCES workout_exercises (id) ON DELETE CASCADE ON UPDATE CASCADE
);

