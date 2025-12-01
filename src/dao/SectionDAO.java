package dao;

import app.DatabaseConnection;
import model.Course;
import model.Professor;
import model.Section;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SectionDAO {

    public void save(Section section) {
        String sql = "INSERT INTO sections (crn, course_id, professor_id, day_of_week, time_of_day) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 1. The CRN (Primary Key)
            pstmt.setString(1, section.getCrn());

            // 2. The Course ID (Foreign Key) - Extracting from the object
            pstmt.setString(2, section.getCourse().getCourseId());

            // 3. The Professor ID (Foreign Key) - Extracting from the object
            pstmt.setString(3, section.getInstructor().getId());

            // 4. Day and Time
            pstmt.setString(4, section.getDay());
            pstmt.setString(5, section.getTime());

            pstmt.executeUpdate();
            System.out.println("Saved Section: " + section.getCrn());


        } catch (SQLException e) {
            System.out.println("Error saving section: " + e);
        }
    }

    public List<Section> loadAll() {
        List<Section> sections = new ArrayList<>();

        // The Big Join Query
        // We grab columns from all 3 tables at once
        String sql =
                "SELECT s.crn, s.day_of_week, s.time_of_day, " +
                        "c. course_id, c.name, c.credits, " +
                        "p.id AS prof_id, p.name AS prof_name, p.department " +
                        "FROM sections s " +
                        "JOIN courses c ON s.course_id = c.course_id " +
                        "JOIN professors p ON s.professor_id = p.id";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // 1. Rebuild the Course Object from the row
                Course c = new Course(
                        rs.getString("course_id"),
                        rs.getString("name"),
                        rs.getDouble("credits")
                );

                // 2. Rebuild the Professor Object
                Professor p = new Professor(
                        rs.getString("prof_id"),
                        rs.getString("prof_name"),
                        rs.getString("department")
                );

                // 3. Rebuild the Section Object using the pieces above
                Section s = new Section(
                        rs.getString("crn"),
                        c,
                        p,
                        rs.getString("day_of_week"),
                        rs.getString("time_of_day")
                );

                sections.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Error loading sections: " + e.getMessage());
        }
        return sections;
    }

}
