package dao;

import app.DatabaseConnection;
import model.Professor;
import org.w3c.dom.ls.LSProgressEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {
    public void save(Professor professor) {
        // The SQL command to insert data
        String sql = "INSERT INTO professors (id, name, department) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // We are mapping the object fields to the ? marks
            pstmt.setString(1, professor.getId());
            pstmt.setString(2, professor.getName());
            pstmt.setString(3, professor.getDepartment());

            pstmt.executeUpdate();
            System.out.println("Saved Professor: " + professor.getName());

        } catch (SQLException e) {
            System.out.println("Error saving professor: " + e.getMessage());
        }
    }

    // Method to read all professors back from the database
    public List<Professor> loadAll() {
        List<Professor> professors = new ArrayList<>();
        String sql = "SELECT * FROM professors";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Professor p = new Professor(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("department")
                        );
                professors.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Error loading professors: " + e.getMessage());
        }
        return professors;
    }
}
