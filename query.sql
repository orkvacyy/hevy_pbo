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
create table exercise (
	id BIGINT not null auto_increment,
	name varchar(100) not null,
	category ENUM('strenght', 'cardio') not null,
	muscle_group enum('chest','back','shoulders','biceps','triceps','quad','hamstring','core','other') not null default 'other',
	equipment enum('barbell', 'dumbbell','machine','cable','bodyweight','other') not null default 'other',
	owner_id bigint null default null,
	is_deleted boolean not null default false, -- soft delete
	created_at timestamp not null default CURRENT_TIMESTAMP,
	
	primary key(id),
	constraint fk_exercdise_owner foreign key (owner_id) references users(id) on delete set null
	on update CASCADE
	);