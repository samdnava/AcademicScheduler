package app;

import model.Course;
import model.Professor;
import model.Section;
import model.Student;

import java.util.List;
import java.util.Scanner;

public class SchedulerApp {
    // These are "Instance Variables" now, not local variables.
    // This means every method in this class can use them.
    private Scanner scanner;
    private Student user;
    private List<Section> masterSchedule;

    // Constructor: Sets up the date
    public SchedulerApp() {
        this.scanner = new Scanner(System.in);

        // Load the date
        List<Course> catalog = DataLoader.laodCourses();
        List<Professor> faculty = DataLoader.loadProfessors();
        this.masterSchedule = DataLoader.loadSections(catalog, faculty);

        // Set up the user
        this.user = new Student("007", "James", "Bond", "jbond@mi6.gov");
        user.addCompletedCourse(catalog.get(0)); // Algo
        user.addCompletedCourse(catalog.get(1)); // Db
    }

    // The Game Loop
    public void run() {
        System.out.println("Welcome to the Academic Scheduler.");
        System.out.println("You are logged in as: " + user.getFirstName() + user.getLastName());

        while (true) {
            System.out.println("\n--- Commands: [list] [my] [add] [drop] [exit] ---");
            System.out.print("Enter command: ");

            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            } else if (input.equalsIgnoreCase("list")) {
                handleList(); // <-- Look how clean
            } else if (input.equalsIgnoreCase("my")) {
                handleMySchedule(); // <-- Look how clean
            } else if (input.equalsIgnoreCase("add")) {
                handleRegistration(); // <-- Look how clean
            } else if (input.equalsIgnoreCase("drop")) {
                handleCourseDrop(); // <-- Look how clean
            } else {
                System.out.println("Unknown command.");
            }
        }
    }

    // --- HELPER METHODS (The "Functions") ---

    private void handleList() {
        for (Section s : masterSchedule) {
            System.out.println(s);
        }
    }

    private void handleMySchedule() {
        if (user.getCurrentSchedule().isEmpty()) {
            System.out.println("You are not registered for anything.");
        } else {
            for (Section s : user.getCurrentSchedule()) {
                System.out.println(" - " + s);
            }
        }
    }

    private void handleRegistration() {
        System.out.print("Enter CRN: ");
        String targetCrn = scanner.nextLine();

        boolean found = false;
        for (Section s : masterSchedule) {
            if (s.getCrn().equalsIgnoreCase(targetCrn)) {
                user.registerForSection(s);
                found = true;
                break;
            }
        }
        if (!found) System.out.println("CRN not found.");
    }

    private void handleCourseDrop() {
        System.out.print("Enter CRN to drop: ");
        String targetCrn = scanner.nextLine();

        Section sectionToDrop = null;
        for (Section s : user.getCurrentSchedule()) {
            if (s.getCrn().equalsIgnoreCase(targetCrn)) {
                sectionToDrop = s;
                break;
            }
        }
        if (sectionToDrop != null) {
            user.dropSection(sectionToDrop);
        } else {
            System.out.println("Error: You are not registered for CRN " + targetCrn);
        }
    }


}
