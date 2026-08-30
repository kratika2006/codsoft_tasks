import java.util.*;

class Course {
    private final String courseCode;
    private final String title;
    private final String description;
    private final int capacity;
    private final String schedule;
    private final Set<String> enrolledStudentIds;

    public Course(String courseCode, String title, String description, int capacity, String schedule) {
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.schedule = schedule;
        this.enrolledStudentIds = new HashSet<>();
    }

    public String getCourseCode() { return courseCode; }
    public String getTitle() { return title; }
    public String getSchedule() { return schedule; }
    
    public int getAvailableSlots() { return capacity - enrolledStudentIds.size(); }
    
    public boolean registerStudent(String studentId) {
        if (enrolledStudentIds.size() >= capacity) return false;
        return enrolledStudentIds.add(studentId);
    }

    public boolean dropStudent(String studentId) {
        return enrolledStudentIds.remove(studentId);
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-20s | Slots: %2d/%2d | Schedule: %s", 
                courseCode, title, getAvailableSlots(), capacity, schedule);
    }
}

class Student {
    private final String id;
    private final String name;
    private final List<Course> registeredCourses;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.registeredCourses = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<Course> getRegisteredCourses() { return registeredCourses; }

    public void addCourse(Course course) { registeredCourses.add(course); }
    public void removeCourse(Course course) { registeredCourses.remove(course); }
}

public class ManagementSystem {
    private static final Map<String, Course> courseCatalog = new HashMap<>();
    private static final Map<String, Student> studentRegistry = new HashMap<>();

    public static void main(String[] args) {
        initializeMockDatabase();
        Scanner scanner = new Scanner(System.in);
        int menuChoice;

        System.out.println("================================================");
        System.out.println("     ACADEMIC COURSE REGISTRATION ENGINE        ");
        System.out.println("================================================");

        do {
            System.out.println("\n::: PLATFORM MODULES :::");
            System.out.println("1. Display Course Catalog");
            System.out.println("2. Enroll Student into Course");
            System.out.println("3. Drop Course registration");
            System.out.println("4. View Student Schedule");
            System.out.println("5. Shutdown Engine");
            System.out.print("Provide operation command (1-5): ");

            while (!scanner.hasNextInt()) {
                System.out.print("\u274C Numeric values only: ");
                scanner.next();
            }
            menuChoice = scanner.nextInt();

            switch (menuChoice) {
                case 1 -> displayCatalog();
                case 2 -> handleEnrollment(scanner);
                case 3 -> handleDrop(scanner);
                case 4 -> handleViewSchedule(scanner);
                case 5 -> System.out.println("System runtime terminated. Database state synchronized.");
                default -> System.out.println("\u274C Command unrecognized. Check menu bounds.");
            }
        } while (menuChoice != 5);
        scanner.close();
    }

    private static void initializeMockDatabase() {
        courseCatalog.put("CS101", new Course("CS101", "Advanced Java", "Deep-dive Core Java OOP", 3, "Mon/Wed 09:00 AM"));
        courseCatalog.put("CS102", new Course("CS102", "Database Management", "SQL and NoSQL paradigms", 2, "Tue/Thu 11:00 AM"));
        courseCatalog.put("CS103", new Course("CS103", "Data Structures", "Analysis of Algorithms", 5, "Fri 02:00 PM"));

        studentRegistry.put("S101", new Student("S101", "Aarav Sharma"));
        studentRegistry.put("S102", new Student("S102", "Isha Patel"));
    }

    private static void displayCatalog() {
        System.out.println("\n---------------- CURRENT LIVE CATALOG ----------------");
        for (Course c : courseCatalog.values()) {
            System.out.println(c);
        }
        System.out.println("------------------------------------------------------");
    }

    private static void handleEnrollment(Scanner scanner) {
        System.out.print("Enter structural Student ID (e.g., S101): ");
        String sId = scanner.next().toUpperCase();
        Student student = studentRegistry.get(sId);

        if (student == null) {
            System.out.println("\u274C Database Miss: Student signature not verified.");
            return;
        }

        System.out.print("Enter target Course Code (e.g., CS101): ");
        String cCode = scanner.next().toUpperCase();
        Course course = courseCatalog.get(cCode);

        if (course == null) {
            System.out.println("\u274C Database Miss: Course designator invalid.");
            return;
        }

        if (student.getRegisteredCourses().contains(course)) {
            System.out.println("\u274C Abort: Already mapped to this specific registry index.");
            return;
        }

        if (course.registerStudent(student.getId())) {
            student.addCourse(course);
            System.out.println("\u2714 System Event: Enrollment parameters fully committed.");
        } else {
            System.out.println("\u274C Over-capacity: Registry target reports zero remaining availability.");
        }
    }

    private static void handleDrop(Scanner scanner) {
        System.out.print("Enter structural Student ID: ");
        String sId = scanner.next().toUpperCase();
        Student student = studentRegistry.get(sId);

        if (student == null) {
            System.out.println("\u274C Record missing.");
            return;
        }

        System.out.print("Enter Course Code to decouple: ");
        String cCode = scanner.next().toUpperCase();
        Course course = courseCatalog.get(cCode);

        if (course == null || !student.getRegisteredCourses().contains(course)) {
            System.out.println("\u274C Violation: Target active index registration path not found.");
            return;
        }

        course.dropStudent(student.getId());
        student.removeCourse(course);
        System.out.println("\u2714 System Event: Course mapping successfully deallocated.");
    }

    private static void handleViewSchedule(Scanner scanner) {
        System.out.print("Enter structural Student ID: ");
        String sId = scanner.next().toUpperCase();
        Student student = studentRegistry.get(sId);

        if (student == null) {
            System.out.println("\u274C Profile not in persistence matrix.");
            return;
        }

        System.out.printf("\n=== SCHEDULE FOR %s (%s) ===\n", student.getName().toUpperCase(), student.getId());
        List<Course> courses = student.getRegisteredCourses();
        if (courses.isEmpty()) {
            System.out.println("No active institutional mappings found.");
        } else {
            for (Course c : courses) {
                System.out.printf("- %s : %s (%s)\n", c.getCourseCode(), c.getTitle(), c.getSchedule());
            }
        }
    }
}