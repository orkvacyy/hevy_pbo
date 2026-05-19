package com.hevy.hevy.service;

import com.hevy.hevy.model.User;
import com.hevy.hevy.repository.UserDao;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService() {
        this.userDao         = new UserDao();
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // register, hash sebelum masuk db
    public User register(String username, String email, String password) {
        if (userDao.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username sudah digunakan");
        }
        if (userDao.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email sudah digunakan");
        }

        User user = new User(username, email, passwordEncoder.encode(password));
        userDao.save(user);
        return user;
    }

    // login ; verif with bcrypt
    public User login(String email, String password) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email tidak ditemukan"));

        if (!user.isActive()) {
            throw new IllegalArgumentException("Akun tidak aktif");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Password salah");
        }

        return user;
    }
}