//package com.example.wines_app.service;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;
//
//@Service
//public class DatabaseInitializer {
//
//    public DatabaseInitializer() {
//        System.out.println("🚀 DatabaseInitializer is running...");
//        createDatabaseIfNotExists();
//    }
//
//    public void createDatabaseIfNotExists() {
//        System.out.println("🔍 Checking if database exists...");
//
//        String dbName = System.getenv("DB_NAME");
//        if (dbName == null || dbName.isBlank()) {
//            dbName = "wines_development"; // Default DB
//        }
//
//        String dbUser = System.getenv("DB_USER");
//        String dbPass = System.getenv("DB_PASS");
//        String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
//        String dbPort = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "5432";
//
//        String postgresUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/postgres";
//
//        try (Connection conn = DriverManager.getConnection(postgresUrl, dbUser, dbPass);
//             Statement stmt = conn.createStatement()) {
//
//            // Check if the database exists
//            String checkQuery = "SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'";
//            var resultSet = stmt.executeQuery(checkQuery);
//
//            if (!resultSet.next()) {
//                // If the database does not exist, create it
//                System.out.println("✅ Database '" + dbName + "' does not exist. Creating...");
//                stmt.executeUpdate("CREATE DATABASE " + dbName);
//                System.out.println("✅ Database '" + dbName + "' created successfully!");
//            } else {
//                System.out.println("✔️ Database '" + dbName + "' already exists.");
//            }
//
//        } catch (Exception e) {
//            System.err.println("❌ Error creating database: " + e.getMessage());
//        }
//    }
//}