import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Statement sqlSt; // Run SQL
        String output;
        ResultSet result; // Holds the output from SQL
        Connection dbConnect = null;
        String dbURL = "jdbc:mysql://localhost:3306/myDB";

        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");  // Updated driver class
            dbConnect = DriverManager.getConnection(dbURL, "root", "Joseph77");
            sqlSt = dbConnect.createStatement(); // Allows SQL to be executed

            while (true) {
                System.out.println("Hospital Database Menu");
                System.out.println("1. List all patients");
                System.out.println("2. List all admitted patients");
                System.out.println("3. List rooms that are occupied");
                System.out.println("4. List rooms that are unoccupied");
                System.out.println("5. List all rooms (with patients if occupied)");
                System.out.println("6. List diagnoses and their occurrences");
                System.out.println("7. List employees in ascending order");
                System.out.println("8. List primary doctors with high admission rate");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        // Query 2.1: List all patients
                        String SQL1 = "SELECT * FROM Patient";
                        result = sqlSt.executeQuery(SQL1);
                        while (result.next()) {
                            output = "ID: " + result.getInt("PatientID") + " " +
                                     result.getString("FirstName") + " " +
                                     result.getString("LastName") + " | " +
                                     "Emergency Contact: " + result.getString("EmergencyContact") + " | " +
                                     "Insurance: " + result.getString("InsurancePolicy");
                            System.out.println(output);
                        }
                        break;

                    case 2:
                        // Query 2.2: List all patients currently admitted
                        String SQL2 = "SELECT Patient.PatientID, Patient.FirstName, Patient.LastName " +
                                      "FROM Admission " +
                                      "JOIN Patient ON Admission.PatientID = Patient.PatientID " +
                                      "WHERE Admission.DischargeDate IS NULL";
                        result = sqlSt.executeQuery(SQL2);
                        while (result.next()) {
                            output = result.getInt("PatientID") + " " +
                                     result.getString("FirstName") + " " +
                                     result.getString("LastName");
                            System.out.println(output);
                        }
                        break;

                    case 3:
                        // Query 1.1: List the rooms that are occupied
                        String SQL3 = "SELECT Room.RoomNumber, Patient.FirstName, Patient.LastName, Admission.AdmissionDate " +
                                      "FROM Room " +
                                      "JOIN Admission ON Room.RoomNumber = Admission.RoomNumber " +
                                      "JOIN Patient ON Admission.PatientID = Patient.PatientID " +
                                      "WHERE Room.Occupied = TRUE";
                        result = sqlSt.executeQuery(SQL3);
                        while (result.next()) {
                            output = "Room: " + result.getInt("RoomNumber") + " | " +
                                     result.getString("FirstName") + " " + result.getString("LastName") + " | " +
                                     "Admission Date: " + result.getDate("AdmissionDate");
                            System.out.println(output);
                        }
                        break;

                    case 4:
                        // Query 1.2: List the rooms that are currently unoccupied
                        String SQL4 = "SELECT RoomNumber FROM Room WHERE Occupied = FALSE";
                        result = sqlSt.executeQuery(SQL4);
                        while (result.next()) {
                            output = "Room: " + result.getInt("RoomNumber") + " | " + "Status: Unoccupied";
                            System.out.println(output);
                        }
                        break;

                    case 5:
                        // Query 1.3: List all rooms in the hospital, along with patient names and admission dates for those that are occupied
                        String SQL5 = "SELECT Room.RoomNumber, Patient.FirstName, Patient.LastName, Admission.AdmissionDate " +
                                      "FROM Room " +
                                      "LEFT JOIN Admission ON Room.RoomNumber = Admission.RoomNumber " +
                                      "LEFT JOIN Patient ON Admission.PatientID = Patient.PatientID";
                        result = sqlSt.executeQuery(SQL5);
                        while (result.next()) {
                            if (result.getString("FirstName") != null) {
                                output = "Room: " + result.getInt("RoomNumber") + " | " +
                                         result.getString("FirstName") + " " + result.getString("LastName") + " | " +
                                         "Admission Date: " + result.getDate("AdmissionDate");
                            } else {
                                output = "Room: " + result.getInt("RoomNumber") + " | " + "Status: Unoccupied";
                            }
                            System.out.println(output);
                        }
                        break;

                    case 6:
                        // Query 3.1: List diagnoses and their occurrences
                        String SQL6 = "SELECT Diagnosis.DiagnosisID, Diagnosis.Description, COUNT(*) AS TotalOccurrences " +
                                      "FROM Admission " +
                                      "JOIN Diagnosis ON Admission.DiagnosisID = Diagnosis.DiagnosisID " +
                                      "GROUP BY Diagnosis.DiagnosisID, Diagnosis.Description " +
                                      "ORDER BY TotalOccurrences DESC";
                        result = sqlSt.executeQuery(SQL6);
                        while (result.next()) {
                            output = "Diagnosis ID: " + result.getInt("DiagnosisID") + " | " +
                                     result.getString("Description") + " | " +
                                     "Occurrences: " + result.getInt("TotalOccurrences");
                            System.out.println(output);
                        }
                        break;

                    case 7:
                        // Query 4.1: List employees in ascending order
                        String SQL7 = "SELECT FirstName, LastName, JobCategory FROM Employee ORDER BY LastName ASC, FirstName ASC";
                        result = sqlSt.executeQuery(SQL7);
                        while (result.next()) {
                            output = result.getString("FirstName") + " " + result.getString("LastName") + " | " +
                                     result.getString("JobCategory");
                            System.out.println(output);
                        }
                        break;

                    case 8:
                        // Query 4.2: List primary doctors with high admission rate
                        String SQL8 = "SELECT PrimaryDoctorID, COUNT(*) AS AdmissionCount " +
                                      "FROM Admission " +
                                      "WHERE AdmissionDate BETWEEN '2025-01-01' AND '2025-12-31' " +
                                      "GROUP BY PrimaryDoctorID " +
                                      "HAVING AdmissionCount >= 4";
                        result = sqlSt.executeQuery(SQL8);
                        while (result.next()) {
                            output = "Doctor ID: " + result.getInt("PrimaryDoctorID") + " | Admissions: " +
                                     result.getInt("AdmissionCount");
                            System.out.println(output);
                        }
                        break;

                    case 0:
                        // Exit the program
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid option, please try again.");
                        break;
                }
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Class not found, check the JAR");
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SQL IS BAD! " + ex.getMessage());
        } finally {
            try {
                if (dbConnect != null) {
                    dbConnect.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
