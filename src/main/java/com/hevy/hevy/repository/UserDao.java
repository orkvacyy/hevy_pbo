package com.hevy.hevy.repository;

import com.hevy.hevy.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao implements BaseDao<User> {

    private final Connection connection;

    public UserDao() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }