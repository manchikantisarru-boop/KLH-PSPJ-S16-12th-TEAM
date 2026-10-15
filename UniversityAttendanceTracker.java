
import java.util.Scanner;
import java.io.*;
import java.util.Arrays;

class Student {
    int id;
    String name;
    int present = 0;
    int totalDays = 0;
    int marks = -1;

    Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    void markAttendance(boolean isPresent) {
        totalDays++;
        if (isPresent) {
            present++;
        }
    }

    double getPercentage() {
        if (totalDays == 0) {
            return 0;
        }
        return (double) present / totalDays * 100;
    }

    void display() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.printf("Attendance: %.2f%%%n", getPercentage());

        if (totalDays > 0 && getPercentage() < 85) {
            System.out.println("Attendance below 85%");
        } else if (totalDays > 0) {
            System.out.println("Attendance eligible");
        } else {
            System.out.println("Attendance not recorded");
        }

        if (marks == -1) {
            System.out.println("Marks: Not entered");
        } else {
            System.out.println("Marks: " + marks);

            if (marks >= 90) {
                System.out.println("Grade: A");
            } else if (marks >= 75) {
                System.out.println("Grade: B");
            } else if (marks >= 50) {
                System.out.println("Grade: C");
            } else {
                System.out.println("Grade: F");
            }
        }
    }
}

public class UniversityAttendanceTracker {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        Student[] students = new Student[40];
        int count = 0;
        File file = new File("students.txt");

        // Load saved student data
        if (file.exists()) {
            try (Scanner reader = new Scanner(file)) {
                while (reader.hasNextLine() && count < 40) {
                    String line = reader.nextLine();
                    String[] data = line.split(",", -1);

                    if (data.length == 5) {
                        students[count] = new Student(
                            Integer.parseInt(data[0]),
                            data[1]
                        );

                        students[count].present =
                            Integer.parseInt(data[2]);
                        students[count].totalDays =
                            Integer.parseInt(data[3]);
                        students[count].marks =
                            Integer.parseInt(data[4]);

                        count++;
                    }
                }
            }
            System.out.println("Saved data loaded.");
        }

        int choice;

        do {
            System.out.println("\n1. Register Student");
            System.out.println("2. Mark Attendance");
            System.out.println("3. Enter Marks");
            System.out.println("4. Display Student Details");
            System.out.println("5. Find Students Below 85% Attendance");
            System.out.println("0. Save and Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    if (count >= 40) {
                        System.out.println("Maximum 40 students reached.");
                        break;
                    }

                    System.out.print("Enter student ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter student name: ");
                    String name = sc.nextLine();

                    students[count] = new Student(id, name);
                    count++;

                    System.out.println("Student registered.");
                    break;

                case 2:
                    System.out.print("Enter student ID: ");
                    id = sc.nextInt();

                    boolean found = false;

                    for (int i = 0; i < count; i++) {
                        if (students[i].id == id) {
                            System.out.print(
                                "Present? (1=Yes, 0=No): "
                            );
                            int status = sc.nextInt();

                            if (status == 1 || status == 0) {
                                students[i].markAttendance(status == 1);
                                System.out.println("Attendance recorded.");
                            } else {
                                System.out.println("Enter 1 or 0.");
                            }

                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Student not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter student ID: ");
                    id = sc.nextInt();
                    found = false;

                    for (int i = 0; i < count; i++) {
                        if (students[i].id == id) {
                            System.out.print("Enter marks out of 100: ");
                            int marks = sc.nextInt();

                            if (marks >= 0 && marks <= 100) {
                                students[i].marks = marks;
                                System.out.println("Marks saved.");
                            } else {
                                System.out.println("Invalid marks.");
                            }

                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Student not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter student ID: ");
                    id = sc.nextInt();
                    found = false;

                    for (int i = 0; i < count; i++) {
                        if (students[i].id == id) {
                            students[i].display();
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Student not found.");
                    }
                    break;

                case 5:
                    long belowCount = Arrays.stream(students, 0, count)
                        .filter(s -> s.totalDays > 0
                                  && s.getPercentage() < 85)
                        .count();

                    if (belowCount == 0) {
                        System.out.println(
                            "No students below 85% attendance."
                        );
                    } else {
                        System.out.println(
                            "Students below 85% attendance:"
                        );

                        Arrays.stream(students, 0, count)
                            .filter(s -> s.totalDays > 0
                                      && s.getPercentage() < 85)
                            .forEach(s -> System.out.printf(
                                "ID: %d, Name: %s, Attendance: %.2f%%%n",
                                s.id, s.name, s.getPercentage()
                            ));
                    }
                    break;

                case 0:
                    try (PrintWriter out =
                             new PrintWriter(new FileWriter(file))) {
                        for (int i = 0; i < count; i++) {
                            out.println(
                                students[i].id + "," +
                                students[i].name + "," +
                                students[i].present + "," +
                                students[i].totalDays + "," +
                                students[i].marks
                            );
                        }
                    }

                    System.out.println("Data saved to file.");
                    System.out.println("Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);

        sc.close();
    }
}
```