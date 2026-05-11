package com.hevy.hevy.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    // sesuain ama db masing2
    private static final String URL      = "jdbc:mysql://localhost:3306/hevy_pbo";
    private static final String USERNAME = "root"; // gaboleh diubah di runtime
    private static final String PASSWORD = "";

    // cons private (singleton)
    private DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Berhasil tersambung");
        } catch (SQLException e) {
            System.err.println("gagal terkoneksi, coba lagi " + e.getMessage());
            throw new RuntimeException("Gagal tersambung ke database", e);
        }
    }

    // instance (buat kalau belum ada0
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // take connection for DAO
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) { // reconnect logic
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Koneksi terputus dan gagal reconnect", e);
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Koneksi database diputuskn");
            }
        } catch (SQLException e) {
            System.err.println("Gagal memutuskan koneksi " + e.getMessage());
        }
    }
}