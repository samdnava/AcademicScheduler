package dao;

import app.DatabaseConnection;
import org.checkerframework.checker.units.qual.A;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    // 1. REGISTER: Save the link (Student <-> Section)
    public boolean register(String studentId, String crn) {
        String sql = "INSERT INTO enrollments (student_id, section_crn) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.setString(2, crn);
            pstmt.executeUpdate();
            return true; // Success
        } catch (SQLException e) {
            // This happens if they are already registered (Duplicate Key Error)
            System.out.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    // 2. DROP: Delete the link
    public void drop(String studentId, String crn) {
        String sql = "DELETE FROM enrollments WHERE students_id = ? AND section_crn = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.setString(2, crn);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error dropping class: " + e.getMessage());
        }
    }


    // 3. LOAD: Get all CRNs for a specific student
    public List<String> getEnrolledCrns(String studentId) {
        List<String> crns = new ArrayList<>();
        String sql = "SELECT section_crn FROM enrollments WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                crns.add(rs.getString("section_crn"));
            }
        } catch (SQLException e) {
            System.out.println("Error loading enrollments: " + e.getMessage());
        }
        return crns;
    }
}
