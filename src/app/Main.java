package app;

import app.dao.CourseDAO;
import app.dao.ProfessorDAO;
import model.Course;
import model.Professor;

import java.util.List;

public class Main {
    static void main(String[] args) {
        System.out.printf("---STARTING PROFESSOR MIGRATION");

        // 1. Load the fake data
        List<Professor> faculty = DataLoader.loadProfessors();

        // 2. Prepare the DAO
        ProfessorDAO profDao = new ProfessorDAO();

        // 3. Save to the real database
        for (Professor p : faculty) {
            profDao.save(p);
        }
        System.out.println("---MIGRATION COMPLETE---");
    }
}
