package app;

import dao.EnrollmentDAO;
import dao.StudentDAO;
import model.Section;
import model.Student;

import dao.SectionDAO;

import java.util.List;
import java.util.Scanner;

public class SchedulerApp {
    private Scanner scanner;
    private Student user;
    private StudentDAO studentDao;
    private EnrollmentDAO enrollmentDao;
    private List<Section> masterSchedule;

    // Constructor: Sets up the data
    public SchedulerApp() {
        this.scanner = new Scanner(System.in);
        this.studentDao = new StudentDAO();
        this.enrollmentDao = new EnrollmentDAO();

//        ---OLD FAKE DATA---
//
//        // Load the data
//        List<Course> catalog = DataLoader.loadCourses();
//        List<Professor> faculty = DataLoader.loadProfessors();
//        this.masterSchedule = DataLoader.loadSections(catalog, faculty);
//
//        // Set up the user
//        this.user = new Student("007", "James", "Bond", "jbond@mi6.gov");
//        user.addCompletedCourse(catalog.get(0)); // Algo
//        user.addCompletedCourse(catalog.get(1)); // Db

//      --- NEW REAL DATABASE ---
        System.out.println("Connecting to Postgres...");
        // 1. Create the DAO
        SectionDAO sectionDao = new SectionDAO();
        // 2. Load the data using the JOIN query
        this.masterSchedule = sectionDao.loadAll();
        System.out.println("Data Loaded. Found " + masterSchedule.size() + " sections.");

        // --- THE LOGIN LOGIC STARTS HERE
        // Instead of new Student ("007" ...), we ask the user.
        this.user = handleLoginScreen();
    }

    // The Game Loop
    public void run() {
        System.out.println("Welcome to the Academic Scheduler.");
        System.out.println("You are logged in as: " + user.getFirstName() + " " + user.getLastName());

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

        // 1. Find the section object in the master list
        Section targetSection = null;
        for (Section s : masterSchedule) {
            if (s.getCrn().equalsIgnoreCase(targetCrn)) {
                targetSection = s;
                break;
            }
        }

        if (targetSection == null) {
            System.out.println("CRN not found.");
            return;
        }

        // 2. Try to save to Database FIRST
        // We use user.getId() to get current student's ID
        boolean saved = enrollmentDao.register(user.getId(), targetSection.getCrn());

        // 3. If DB save works, update Java memory so we see it immediately
        if (saved) {
            user.registerForSection(targetSection);
        }
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
            // 1. Remove from DB
            enrollmentDao.drop(user.getId(), sectionToDrop.getCrn());

            // 2. Remove from Java Memory
            user.dropSection(sectionToDrop);
        } else {
            System.out.println("Error: You are not registered for CRN " + targetCrn);
        }
    }

    private Student handleLoginScreen() {
        System.out.println("\n=== LOGIN SCREEN ===");
        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Register new student");
            System.out.println("Choose option: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.println("Enter email: ");
                String email = scanner.nextLine();

                // Use the DAO to look them up
                Student found = studentDao.findByEmail(email);
                if (found != null) {
                    // 1. Ask DB for list of CRNs this student has
                    List<String> savedCrns = enrollmentDao.getEnrolledCrns(found.getId());

                    // 2. Match those CRNs to real Section objects in our master schedule
                    for (String crn : savedCrns) {
                        for (Section s : masterSchedule) {
                            if (s.getCrn().equals(crn)) {
                                // Add to the student's active schedule
                                found.registerForSection(s);
                            }
                        }
                    }
                    return found;
                } else {
                    System.out.println("Error: User not found.");
                }
            } else if (choice.equals("2")) {
                System.out.println("Enter ID (e.g. 900): ");
                String id = scanner.nextLine();
                System.out.println("Enter First Name: ");
                String first = scanner.nextLine();
                System.out.println("Enter Last Name: ");
                String last = scanner.nextLine();
                System.out.println("Enter Email: ");
                String email = scanner.nextLine();

                Student newStudent = new Student(id, first, last, email);
                studentDao.save(newStudent);

                return newStudent;
            } else {
                System.out.println("Invalid option.");
            }

        }
    }

}
