package dao;

import app.DatabaseConnection;
import model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // Method to save a single course to the database
    public void save(Course course) {
        String sql = "INSERT INTO courses (course_id, name, credits) VALUES (?, ?, ?)";

        // "Try-with-resources" block - automatically close the connection
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Fill in the '?' blanks
            pstmt.setString(1, course.getCourseId());
            pstmt.setString(2, course.getName());
            pstmt.setDouble(3, course.getCredits());

            pstmt.executeUpdate();
            System.out.println("Saved to DB: " + course.getName());

        } catch (SQLException e) {
            System.out.println("Error saving course: " + e.getMessage());
        }
    }

    public List<Course> loadAll() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Loop through the spreadsheet rows
            while (rs.next()) {
                // 1. Grab data from the current rows
                String id = rs.getString("course_id");
                String name = rs.getString("name");
                double credits = rs.getDouble("credits");

                // 2. Turn it back into a Java Object
                Course c = new Course(id, name, credits);

                // 3. Add to list
                courses.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
        return courses;
    }
}
