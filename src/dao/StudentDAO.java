package dao;

import app.DatabaseConnection;
import model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDAO {

    // 1. REGISTER: Save a new student
    public void save(Student student) {
        String sql = "INSERT INTO students (id, first_name, last_name, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getId());
            pstmt.setString(2, student.getFirstName());
            pstmt.setString(3, student.getLastName());
            pstmt.setString(4, student.getEmail());

            pstmt.executeUpdate();
            System.out.println("Saved Student: " + student.getEmail());

        } catch (SQLException e) {
            System.out.println("Error saving student: " + e.getMessage());
        }
    }

    // 2. LOGIN: Find a student by their email
    public Student findByEmail(String email) {
        String sql = "SELECT * FROM students WHERE email = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // If found, reconstruct the Student object
                return new Student(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error finding student: " + e.getMessage());
        }
        return null; // Return null if user doesn't exist
    }
}
