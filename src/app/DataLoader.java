package app;

import model.Course;
import model.Professor;
import model.Section;

import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public static List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
//        courses.add(new Course("CSI 403", "Algorithms", 3.0));
//        courses.add(new Course("CSI 410", "Database Systems", 3.0));
//        courses.add(new Course("CSI 311", "Principles of Prog Lang", 3.0));
//        courses.add(new Course("CSI 418Y", "Software Engineering", 3.0));
//        courses.add(new Course("CSI 404", "Computer Arch", 3.0));

        // 1. Create the courses
        Course algo = new Course("CSI 403", "Algorithms", 3.0);
        Course db = new Course("CSI 410", "Database Systems", 3.0);
        Course lang = new Course("CSI 311", "Principles of Prog Lang", 4.0);
        Course swe = new Course("CSI 418Y", "Software Engineering", 3.0);
        Course arch = new Course("CSI 404", "Computer Arch", 3.0);

        // 2. Define the Rules (The Logic)

        // Rule: You must take Algorithms BEFORE Databases
        db.addPreRequisite(algo);

        // Rule: You must take databases BEFORE Software Engineering
        swe.addPreRequisite(db);

        // 3. Add them to the master list
        courses.add(algo);
        courses.add(db);
        courses.add(lang);
        courses.add(swe);
        courses.add(arch);

        return courses;
    }

    // This method simulates "SELECT * FROM professors"
    public static List<Professor> loadProfessors() {
        List<Professor> professors = new ArrayList<>();
        professors.add(new Professor("P001", "Dr. Smith", "Computer Science"));
        professors.add(new Professor("P002", "Dr. Jones", "Computer Science"));
        professors.add(new Professor("P003", "Dr.Williams", "Mathematics"));
        return professors;
    }

    public static List<Section> loadSections(List<Course> catalog, List<Professor> faculty) {
        List<Section> sections = new ArrayList<>();

        // Logic: specific Course + specific Professor = Section

        // 1. Algorithms (Item 0) with Dr.Smith (Item 0)
        Section s1 = new Section(
                "CRN-101",      // ID
                catalog.get(0), // Algorithms
                faculty.get(0), // Dr. Smith
                "Mon/Wed",
                "10:00 AM"
        );
        sections.add(s1);

        // 2. Databases (Item 1) with Dr. Jones (Item 1)
        Section s2 = new Section(
                "CRN-102",      // ID
                catalog.get(1), // Databases
                faculty.get(1), // Dr. Jones
                "Tue/Thu",
                "2:00 PM"
        );
        sections.add(s2);

        // 3.Software Engineering (Item 3) with Dr. Smith
        // This shows one professor teaching multiple classes
        Section s3 = new Section(
                "CRN-103",      // ID
                catalog.get(3), // Software Eng
                faculty.get(0), // Dr. Smith
                "Fri",
                "9:00 AM"
        );
        sections.add(s3);

        return sections;
    }

}
