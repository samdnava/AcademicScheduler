package app;

import model.Course;
import model.Professor;
import model.Section;
import model.Student;

import java.util.List;
import java.util.Scanner;

public class Main {
    static void main(String[] args) {

        // 1. Setup Data
        List<Course> catalog = DataLoader.laodCourses();
        List<Professor> faculty = DataLoader.loadProfessors();
        List<Section> schedule = DataLoader.loadSections(catalog, faculty);

        //2. Set up the User (We will pretend we are this student)
        Student me = new Student("007", "James", "Bond", "jbond@mi6.gov");
        // Let's give him some prerequisites so he can register for things
        me.addCompletedCourse(catalog.get(0)); // Algorithms
        me.addCompletedCourse(catalog.get(1)); // Databases

        // 3. Set up the Scanner (The Ear)
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Academic Scheduler.");
        System.out.println("You are logged in as: " + me.getFirstName());

        // 4. The "Game Loop"
        // while(true) runs forever until we command it to stop.
        while (true) {
            System.out.println("\n------------------------------------");
            System.out.println("Available Commands: ");
            System.out.println("1. [list] - Show all available sections");
            System.out.println("2. [my]   - Show my current schedule");
            System.out.println("3. [add]  - Register for a class");
            System.out.println("4. [drop] - Drop a class");
            System.out.println("5. [exit] - Close the app");
            System.out.println("Enter command: ");

            // Wait for user input
            String input = scanner.nextLine();

            // Handle the command
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break; // Kills the loop
            } else if (input.equalsIgnoreCase("list")) {
                for (Section s : schedule) {
                    System.out.println(s);
                }
            } else if (input.equalsIgnoreCase("my")) {
                List<Section> myClasses = me.getCurrentSchedule();
                if (myClasses.isEmpty()) {
                    System.out.println("You are not registered for anything");
                } else {
                    for (Section s : myClasses) {
                        System.out.println(" - " + s);
                    }
                }
            } else if (input.equalsIgnoreCase("add")) {
                System.out.println("Enter CRN: ");
                String targetCRN = scanner.nextLine();

                // The Search Logic
                boolean found = false;
                for (Section s : schedule) {
                    // Check if CRN matches (Using the getter you just added)
                    if (s.getCrn().equalsIgnoreCase(targetCRN)) {
                        me.registerForSection(s);
                        found = true;
                        break;
                    }
                }
                if (!found) System.out.println("CRN not found.");

            } else if (input.equalsIgnoreCase("drop")) {
                System.out.println("Enter CRN to drop: ");
                String targetCrn = scanner.nextLine();

                // Step 1: Find the section in YOUR schedule
                Section sectionToDrop = null;

                for (Section s : me.getCurrentSchedule()) {
                    if (s.getCrn().equalsIgnoreCase(targetCrn)) {
                        sectionToDrop = s;
                        break;
                    }
                }

                // Step 2: Remove it (Safe way)
                if (sectionToDrop != null) {
                    me.dropSection(sectionToDrop);
                } else {
                    System.out.println("Error: You are not registered for CRN " + targetCrn);
                }


            } else {
                System.out.println("Unknown Command. Please try again.");
            }


        }
    }
}
