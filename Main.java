import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Complete Animal Record System - All classes combined into one
 * Main class containing all functionality
 */
public class Main {
    // Scanner for input
    private Scanner input;

    // Login file references
    private File staffLoginFile;
    private File ownerLoginFile;

    // Current logged in username
    private String currentStaffUsername;
    private String currentOwnerUsername;

    // File constants
    private static final String ANIMAL_FILE = "A_record.txt";
    private static final String MILK_FILE = "milk_record.txt";
    private static final String STAFF_FILE = "staff_record.txt";
    private static final String STAFF_LOGIN_FILE = "staff_login.txt";
    private static final String OWNER_LOGIN_FILE = "owner_login.txt";

    // Animal fields
    private String animalId;
    private int age;
    private char gender;
    private String purchaseDate;
    private char vaccinated;
    private String feedType;
    private String feedTimes;
    private String animalType;

    // Staff fields
    private String staffName;
    private String workStatus;
    private String workingHours;
    private String salary;
    private String staffType;

    // User fields (for login)
    private String username;
    private String password;

    /**
     * Constructor
     */
    public Main() {
        this.input = new Scanner(System.in);
        this.staffLoginFile = new File(STAFF_LOGIN_FILE);
        this.ownerLoginFile = new File(OWNER_LOGIN_FILE);
        try {
            this.staffLoginFile.createNewFile();
            this.ownerLoginFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Error creating login file: " + e.getMessage());
        }
    }

    /**
     * Main method - Entry point (Launches GUI)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new MainGUI().setVisible(true);
            }
        });
    }

    /**
     * Main program loop
     */
    public void run() throws IOException {
        System.out.println("Welcome to the Animal Record System");

        boolean continueProgram = true;

        while (continueProgram) {
            System.out.println("Enter your role:\n1. Staff\n2. Owner\n3. Exit");
            int role = input.nextInt();

            switch (role) {
                case 1:
                    loginStaff();
                    break;
                case 2:
                    loginOwner();
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option.");
            }

            // Ask if user wants to continue
            boolean validChoice = false;
            while (!validChoice) {
                System.out.println("Do you want to continue the program? (y/n)");
                char choice = input.next().charAt(0);
                input.nextLine(); // consume newline

                if (choice == 'y') {
                    continueProgram = true;
                    validChoice = true;
                } else if (choice == 'n') {
                    System.out.println("Bye Bye");
                    continueProgram = false;
                    validChoice = true;
                } else {
                    System.out.println("Sorry you selected an invalid option! Retry and enter (y/n)");
                }
            }
        }

        input.close();
    }

    // ==================== USER AUTHENTICATION METHODS ====================

    /**
     * Checks if user exists in the login file
     */
    private boolean userExists(String username, File loginFile) throws IOException {
        Scanner checkScanner = new Scanner(loginFile);
        while (checkScanner.hasNextLine()) {
            String[] parts = checkScanner.nextLine().split(",");
            if (parts.length == 2 && parts[0].equals(username)) {
                checkScanner.close();
                return true;
            }
        }
        checkScanner.close();
        return false;
    }

    /**
     * Validates credentials from file
     */
    private boolean validateCredentials(String username, String password, File loginFile) throws IOException {
        Scanner read = new Scanner(loginFile);
        while (read.hasNextLine()) {
            String[] parts = read.nextLine().split(",");
            if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                read.close();
                return true;
            }
        }
        read.close();
        return false;
    }

    // ==================== STAFF LOGIN METHODS ====================

    /**
     * Implementation of staff login method
     */
    private boolean staffLogin() throws IOException {
        System.out.print("Enter Username: ");
        String username = input.next();
        System.out.print("Enter Password: ");
        String password = input.next();

        if (validateCredentials(username, password, staffLoginFile)) {
            System.out.println("Login successful!\n");
            this.currentStaffUsername = username;
            return true;
        } else {
            System.out.println("Incorrect username or password.");
            return false;
        }
    }

    /**
     * Sign up new staff member
     */
    private void staffSignUp() throws IOException {
        System.out.print("Enter Username: ");
        String username = input.next();
        System.out.print("Enter Password: ");
        String password = input.next();

        if (userExists(username, staffLoginFile)) {
            System.out.println("Staff already exist, try another username.");
        } else {
            FileWriter loginWrite = new FileWriter(STAFF_LOGIN_FILE, true);
            loginWrite.write(username + "," + password + "\n");
            loginWrite.close();
            System.out.println("Sign up successfully! Please login now.");
        }
    }

    /**
     * Main staff login interface
     */
    private void loginStaff() throws IOException {
        System.out.println("Welcome Staff\n1. Sign Up\n2. Login\nEnter your choice:");
        int choice = input.nextInt();

        switch (choice) {
            case 1:
                this.staffSignUp();
                break;
            case 2:
                if (this.staffLogin()) {
                    this.showStaffMenu();
                }
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    /**
     * Display staff menu after successful login
     */
    private void showStaffMenu() throws IOException {
        while (true) {
            System.out.println("  Staff Menu\n1. Add animal\n2. Search animal\n3. Add Your Profile\n4. See Your Profile\n5. Show Animals by Type\n6. Add Milk Record\n7. Show Animal Milk Record by Animal ID\n8. Show All Animal Records\n9. Exit\nEnter your choice");
            int staffChoice = input.nextInt();
            input.nextLine(); // consume newline

            switch (staffChoice) {
                case 1:
                    addAnimalRecord();
                    break;
                case 2:
                    System.out.print("Enter Animal ID to search: ");
                    String searchId = input.nextLine();
                    searchAnimal(searchId);
                    break;
                case 3:
                    addOwnProfile(this.currentStaffUsername);
                    break;
                case 4:
                    seeStaffProfile(this.currentStaffUsername);
                    break;
                case 5:
                    System.out.print("Enter animal type to list (e.g., cow, goat): ");
                    String type = input.nextLine();
                    showAnimalsByType(type);
                    break;
                case 6:
                    addMilkRecordPrompt();
                    break;
                case 7:
                    showMilkRecordByAnimalIdPrompt();
                    break;
                case 8:
                    showAllAnimals();
                    break;
                case 9:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid selection");
                    break;
            }
        }
    }

    // ==================== OWNER LOGIN METHODS ====================

    /**
     * Implementation of owner login method
     */
    private boolean ownerLogin() throws IOException {
        System.out.print("Enter Username: ");
        String username = input.next();
        System.out.print("Enter Password: ");
        String password = input.next();

        if (validateCredentials(username, password, ownerLoginFile)) {
            System.out.println("Login successful!\n");
            this.currentOwnerUsername = username;
            return true;
        } else {
            System.out.println("Incorrect username or password.");
            return false;
        }
    }

    /**
     * Main owner login interface
     */
    private void loginOwner() throws IOException {
        System.out.println("Welcome Owner\n1. Login\n");
        int choice = input.nextInt();

        switch (choice) {
            case 1:
                if (this.ownerLogin()) {
                    showOwnerMenu();
                }
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    /**
     * Display and handle owner menu
     */
    private void showOwnerMenu() throws IOException {
        while (true) {
            System.out.println("\nOwner Menu:");
            System.out.println("1. Add Animal\n2. Search Animal\n3. Delete Animal\n4. Add Staff\n5. Show All Staff\n6. Search Staff\n7. Remove Staff\n8. Show Animals by Type\n9. Show Staff by Type\n10. Add Milk Record\n11. Calculate Daily Milk Price\n12. Show All Animal Records\n13. Exit");
            int choice = input.nextInt();
            input.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addAnimalRecord();
                    break;
                case 2:
                    System.out.print("Enter Animal ID to search: ");
                    String searchId = input.nextLine();
                    searchAnimal(searchId);
                    break;
                case 3:
                    System.out.print("Enter Animal ID to delete: ");
                    String deleteId = input.nextLine();
                    deleteAnimal(deleteId);
                    break;
                case 4:
                    manageStaff();
                    break;
                case 5:
                    showAllStaff();
                    break;
                case 6:
                    System.out.print("Enter Staff Name or ID to search: ");
                    String keyword = input.nextLine();
                    searchStaff(keyword);
                    break;
                case 7:
                    System.out.print("Enter Staff Name to remove: ");
                    String nameToDelete = input.nextLine();
                    removeStaff(nameToDelete);
                    break;
                case 8:
                    System.out.print("Enter animal type to list (e.g., cow, goat): ");
                    String animalType = input.nextLine();
                    showAnimalsByType(animalType);
                    break;
                case 9:
                    System.out.print("Enter staff type to list (e.g., doctor, milkman): ");
                    String staffType = input.nextLine();
                    showStaffByType(staffType);
                    break;
                case 10:
                    addMilkRecordPrompt();
                    break;
                case 11:
                    calculateDailyMilkPricePrompt();
                    break;
                case 12:
                    showAllAnimals();
                    break;
                case 13:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    // ==================== ANIMAL METHODS ====================

    /**
     * Add animal record to file
     */
    private void addAnimal() throws IOException {
        FileWriter animalRecord = new FileWriter(ANIMAL_FILE, true);
        animalRecord.write("Animal ID = " + this.animalId + "\n");
        animalRecord.write("Animal Age = " + this.age + "\n");
        animalRecord.write("Animal Gender = " + this.gender + "\n");
        animalRecord.write("Animal Purchase Date = " + this.purchaseDate + "\n");
        animalRecord.write("Feed Type = " + this.feedType + "\n");
        animalRecord.write("Times per day = " + this.feedTimes + "\n");
        animalRecord.write("Vaccination = " + this.vaccinated + "\n");
        animalRecord.write("Animal Type = " + this.animalType + "\n");
        animalRecord.write("=======================================================================================\n");
        animalRecord.close();
        System.out.println("Animal record added.");
    }

    /**
     * Prompt user for animal details and add record
     */
    private void addAnimalRecord() throws IOException {
        System.out.print("Animal ID: ");
        this.animalId = input.nextLine();
        System.out.print("Age: ");
        this.age = input.nextInt();
        input.nextLine();
        System.out.print("Gender (M/F): ");
        this.gender = input.nextLine().charAt(0);
        System.out.print("Purchase Date (DD-MM-YYYY): ");
        this.purchaseDate = input.nextLine();
        System.out.print("Vaccinated? (Y/N): ");
        this.vaccinated = input.nextLine().charAt(0);
        System.out.print("Feed Type: ");
        this.feedType = input.nextLine();
        System.out.print("Feed Times per Day: ");
        this.feedTimes = input.nextLine();
        System.out.print("Add Animal Type: ");
        this.animalType = input.nextLine();
        this.addAnimal();
    }

    /**
     * Search animal by ID
     */
    private void searchAnimal(String searchId) throws IOException {
        File file = new File(ANIMAL_FILE);
        if (!file.exists()) {
            System.out.println("No animal records found.");
            return;
        }
        boolean found = false;
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("Animal ID =") && line.contains(searchId)) {
                found = true;
                System.out.println("\n Animal Found ");
                System.out.println(line);
                while (scanner.hasNextLine()) {
                    String nextLine = scanner.nextLine();
                    if (nextLine.startsWith("===")) break;
                    System.out.println(nextLine);
                }
                break;
            }
        }
        scanner.close();
        if (!found) {
            System.out.println("No record found for Animal ID: " + searchId);
        }
    }

    /**
     * Delete animal by ID
     */
    private void deleteAnimal(String deleteId) throws IOException {
        File original = new File(ANIMAL_FILE);
        File temp = new File("temp.txt");
        FileReader fr = new FileReader(original);
        Scanner reader = new Scanner(fr);
        FileWriter writer = new FileWriter(temp);
        boolean found = false;
        boolean skipBlock = false;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.startsWith("Animal ID =") && line.contains(deleteId)) {
                found = true;
                skipBlock = true;
                continue;
            }
            if (skipBlock) {
                if (line.startsWith("===")) {
                    skipBlock = false;
                }
                continue;
            }
            writer.write(line + "\n");
        }
        reader.close();
        writer.close();
        if (original.delete() && temp.renameTo(original)) {
            if (found) {
                System.out.println("Animal record deleted successfully.");
            } else {
                System.out.println("Animal ID not found.");
            }
        } else {
            System.out.println("Error occurred while deleting animal record.");
        }
    }

    /**
     * Show animals by type
     */
    private void showAnimalsByType(String type) throws IOException {
        File file = new File(ANIMAL_FILE);
        if (!file.exists()) {
            System.out.println("No animal records found.");
            return;
        }
        Scanner reader = new Scanner(file);
        boolean found = false;
        StringBuilder block = new StringBuilder();
        boolean inBlock = false;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.startsWith("Animal ID =")) {
                block = new StringBuilder();
                inBlock = true;
            }
            if (inBlock) {
                block.append(line).append("\n");
            }
            if (line.toLowerCase().startsWith("animal type =") && line.toLowerCase().contains(type.toLowerCase())) {
                found = true;
                System.out.println("\n--- Animal Record Found ---");
                System.out.print(block.toString());
            }
            if (line.startsWith("===")) {
                inBlock = false;
            }
        }
        reader.close();
        if (!found) {
            System.out.println("No animal records found for type: " + type);
        }
    }

    /**
     * Show all animal records
     */
    private void showAllAnimals() throws IOException {
        File file = new File(ANIMAL_FILE);
        if (!file.exists()) {
            System.out.println("No animal records found.");
            return;
        }
        Scanner reader = new Scanner(file);
        System.out.println("\nAll Animal Records:");
        while (reader.hasNextLine()) {
            System.out.println(reader.nextLine());
        }
        reader.close();
    }

    /**
     * Add milk record
     */
    private void addMilkRecord(String date, String animalId, String quantity,
                               String staffName, String pricePerLiter) throws IOException {
        FileWriter milkWriter = new FileWriter(MILK_FILE, true);
        milkWriter.write("Date = " + date + "\n");
        milkWriter.write("Animal ID = " + animalId + "\n");
        milkWriter.write("Milk Quantity = " + quantity + " liters\n");
        milkWriter.write("Staff Name = " + staffName + "\n");
        milkWriter.write("Price per Liter = " + pricePerLiter + "\n");
        milkWriter.write("--------------------------------------------------\n");
        milkWriter.close();
        System.out.println("Milk record added.");
    }

    /**
     * Prompt user and add milk record
     */
    private void addMilkRecordPrompt() throws IOException {
        System.out.print("Enter Date (DD-MM-YYYY): ");
        String date = input.nextLine();
        System.out.print("Enter Animal ID: ");
        String animalId = input.nextLine();
        System.out.print("Enter Milk Quantity (liters): ");
        String quantity = input.nextLine();
        System.out.print("Enter Staff Name: ");
        String staffName = input.nextLine();
        System.out.print("Enter Price per Liter: ");
        String pricePerLiter = input.nextLine();
        addMilkRecord(date, animalId, quantity, staffName, pricePerLiter);
    }

    /**
     * Calculate daily milk price
     */
    private void calculateDailyMilkPrice(String date, double pricePerLiter) throws IOException {
        File file = new File(MILK_FILE);
        if (!file.exists()) {
            System.out.println("No milk records found.");
            return;
        }
        Scanner reader = new Scanner(file);
        double totalLiters = 0;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.startsWith("Date = ") && line.contains(date)) {
                reader.nextLine(); // skip Animal ID
                String qtyLine = reader.nextLine();
                String[] parts = qtyLine.split("=");
                if (parts.length == 2) {
                    String qtyStr = parts[1].replace("liters", "").replace("liter", "").trim();
                    try {
                        totalLiters += Double.parseDouble(qtyStr);
                    } catch (NumberFormatException e) {
                        // skip invalid
                    }
                }
            }
        }
        reader.close();
        double totalPrice = totalLiters * pricePerLiter;
        System.out.println("Total milk for " + date + ": " + totalLiters + " liters");
        System.out.println("Total price for " + date + ": " + totalPrice);

        // Append summary to milk_record.txt
        FileWriter milkWriter = new FileWriter(MILK_FILE, true);
        milkWriter.write("Daily Summary for " + date + ":\n");
        milkWriter.write("Total Milk = " + totalLiters + " liters\n");
        milkWriter.write("Price per liter = " + pricePerLiter + "\n");
        milkWriter.write("Total Price = " + totalPrice + "\n");
        milkWriter.write("==================================================\n");
        milkWriter.close();
        System.out.println("Daily summary appended to milk_record.txt");
    }

    /**
     * Prompt user and calculate daily milk price
     */
    private void calculateDailyMilkPricePrompt() throws IOException {
        File file = new File(MILK_FILE);
        if (!file.exists()) {
            System.out.println("No milk records found.");
            return;
        }
        System.out.print("Enter date to calculate (DD-MM-YYYY): ");
        String date = input.nextLine().trim();
        System.out.print("Enter 1 liter milk price: ");
        double pricePerLiter = input.nextDouble();
        input.nextLine();
        calculateDailyMilkPrice(date, pricePerLiter);
    }


//     Show milk record by animal ID

    private void showMilkRecordByAnimalId(String animalId) throws IOException {
        File file = new File(MILK_FILE);
        if (!file.exists()) {
            System.out.println("No milk records found.");
            return;
        }
        Scanner reader = new Scanner(file);
        boolean found = false;
        String date = "", staffName = "", quantity = "", price = "";
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.startsWith("Date = ")) {
                date = line.substring(7).trim();
                staffName = "";
                quantity = "";
                price = "";
            } else if (line.startsWith("Animal ID = ")) {
                String thisId = line.substring(12).trim();
                if (thisId.equals(animalId)) {
                    found = true;
                    if (reader.hasNextLine()) quantity = reader.nextLine().replace("Milk Quantity = ", "").replace("liters", "").replace("liter", "").trim();
                    if (reader.hasNextLine()) staffName = reader.nextLine().replace("Staff Name = ", "").trim();
                    if (reader.hasNextLine()) price = reader.nextLine().replace("Price per Liter = ", "").trim();
                    System.out.println("Date: " + date);
                    System.out.println("Animal ID: " + animalId);
                    System.out.println("milkman Name: " + staffName);
                    System.out.println("Total Milk: " + quantity + " liters");
                    System.out.println("Price per Liter: " + price);
                    double totalPrice = 0.0;
                    try {
                        double qty = Double.parseDouble(quantity);
                        double prc = Double.parseDouble(price);
                        totalPrice = qty * prc;
                    } catch (Exception e) {
                        // ignore parse errors
                    }
                    System.out.println("Total milk Price: " + totalPrice);
                    System.out.println();
                }
            }
        }
        reader.close();
        if (!found) {
            System.out.println("No milk records found for Animal ID: " + animalId);
        }
    }

    /**
     * Prompt user and show milk record by animal ID
     */
    private void showMilkRecordByAnimalIdPrompt() throws IOException {
        File file = new File(MILK_FILE);
        if (!file.exists()) {
            System.out.println("No milk records found.");
            return;
        }
        System.out.print("Enter Animal ID to show milk records: ");
        String animalId = input.nextLine().trim();
        showMilkRecordByAnimalId(animalId);
    }

    // ==================== STAFF METHODS ====================

    /**
     * Add staff record to file
     */
    private void addStaff() throws IOException {
        FileWriter staffWriter = new FileWriter(STAFF_FILE, true);
        staffWriter.write("Staff Name = " + this.staffName + "\n");
        staffWriter.write("Work Status = " + this.workStatus + "\n");
        staffWriter.write("Working Hours = " + this.workingHours + "\n");
        staffWriter.write("Salary = " + this.salary + "\n");
        staffWriter.write("Staff type = " + this.staffType + "\n");
        staffWriter.write("--------------------------------------------------\n");
        staffWriter.close();
        System.out.println("Staff record added successfully.");
    }

    /**
     * Manage staff - prompts for input and creates staff record
     */
    private void manageStaff() throws IOException {
        System.out.print("Enter Staff Name: ");
        this.staffName = input.nextLine();
        System.out.print("Enter Work Status (Full-time / Part-time): ");
        this.workStatus = input.nextLine();
        System.out.print("Enter Working Hours: ");
        this.workingHours = input.nextLine();
        System.out.print("Enter Salary: ");
        this.salary = input.nextLine();
        System.out.print("Enter Staff Type: ");
        this.staffType = input.nextLine();
        this.addStaff();
    }

    /**
     * Show all staff records
     */
    private void showAllStaff() throws IOException {
        File file = new File(STAFF_FILE);
        if (!file.exists()) {
            System.out.println("No staff records found.");
            return;
        }
        Scanner reader = new Scanner(file);
        System.out.println("\nAll Staff Records:");
        while (reader.hasNextLine()) {
            System.out.println(reader.nextLine());
        }
        reader.close();
    }

    /**
     * Search staff by keyword
     */
    private void searchStaff(String keyword) throws IOException {
        File file = new File(STAFF_FILE);
        if (!file.exists()) {
            System.out.println("No staff records found.");
            return;
        }
        Scanner reader = new Scanner(file);
        boolean found = false;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.toLowerCase().contains(keyword.toLowerCase())) {
                found = true;
                System.out.println("\n--- Staff Record Found ---");
                System.out.println(line);
                while (reader.hasNextLine()) {
                    String nextLine = reader.nextLine();
                    if (nextLine.startsWith("---") || nextLine.isBlank()) break;
                    System.out.println(nextLine);
                }
                break;
            }
        }
        reader.close();
        if (!found) {
            System.out.println("No staff record found with keyword: " + keyword);
        }
    }

    /**
     * Remove staff by name
     */
    private void removeStaff(String nameToDelete) throws IOException {
        File original = new File(STAFF_FILE);
        File temp = new File("temp_staff.txt");
        if (!original.exists()) {
            System.out.println("No staff records found.");
            return;
        }
        FileReader fr = new FileReader(original);
        Scanner reader = new Scanner(fr);
        FileWriter writer = new FileWriter(temp);
        boolean found = false;
        boolean skipBlock = false;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.toLowerCase().contains("staff name =") && line.toLowerCase().contains(nameToDelete.toLowerCase())) {
                found = true;
                skipBlock = true;
                continue;
            }
            if (skipBlock) {
                if (line.startsWith("--------------------------------------------------")) {
                    skipBlock = false;
                }
                continue;
            }
            writer.write(line + "\n");
        }
        reader.close();
        writer.close();
        if (original.delete() && temp.renameTo(original)) {
            if (found) {
                System.out.println("Staff record removed successfully.");
            } else {
                System.out.println("Staff name not found.");
            }
        } else {
            System.out.println("Error occurred while deleting staff record.");
        }
    }

    /**
     * Show staff by type
     */
    private void showStaffByType(String type) throws IOException {
        File file = new File(STAFF_FILE);
        if (!file.exists()) {
            System.out.println("No staff records found.");
            return;
        }
        Scanner reader = new Scanner(file);
        boolean found = false;
        StringBuilder block = new StringBuilder();
        boolean inBlock = false;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.startsWith("Staff Name =")) {
                block = new StringBuilder();
                inBlock = true;
            }
            if (inBlock) {
                block.append(line).append("\n");
            }
            if (line.toLowerCase().startsWith("staff type =") && line.toLowerCase().contains(type.toLowerCase())) {
                found = true;
                System.out.println("\n--- Staff Record Found ---");
                System.out.print(block.toString());
            }
            if (line.startsWith("---")) {
                inBlock = false;
            }
        }
        reader.close();
        if (!found) {
            System.out.println("No staff records found for type: " + type);
        }
    }

    // ==================== STAFF PROFILE METHODS ====================

    /**
     * Add own profile for logged-in staff
     */
    private void addOwnProfile(String username) throws IOException {
        FileWriter staffWriter = new FileWriter(STAFF_FILE, true);

        System.out.println("Adding/Updating your profile. Your username will be used as Staff Name.");
        String name = username;

        System.out.print("Enter Work Status (Full-time / Part-time): ");
        String status = input.nextLine();
        System.out.print("Enter Working Hours: ");
        String hours = input.nextLine();
        System.out.print("Enter Salary: ");
        String salary = input.nextLine();
        System.out.print("Enter Staff Type: ");
        String staffType = input.nextLine();

        staffWriter.write("Staff Name = " + name + "\n");
        staffWriter.write("Work Status = " + status + "\n");
        staffWriter.write("Working Hours = " + hours + "\n");
        staffWriter.write("Salary = " + salary + "\n");
        staffWriter.write("Staff type = " + staffType + "\n");
        staffWriter.write("--------------------------------------------------\n");
        staffWriter.close();

        System.out.println("Your profile has been added.");
    }

    /**
     * See staff profile by username
     */
    private void seeStaffProfile(String username) throws IOException {
        File file = new File(STAFF_FILE);
        if (!file.exists()) {
            System.out.println("No staff records found.");
            return;
        }

        Scanner reader = new Scanner(file);
        boolean found = false;
        StringBuilder block = new StringBuilder();
        boolean inBlock = false;

        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.startsWith("Staff Name =")) {
                block = new StringBuilder();
                inBlock = true;
            }
            if (inBlock) {
                block.append(line).append("\n");
            }
            if (line.toLowerCase().startsWith("staff name =") && line.toLowerCase().contains(username.toLowerCase())) {
                found = true;
                System.out.println("\n--- Your Staff Profile ---");
                System.out.print(block.toString());
            }
            if (line.startsWith("---")) {
                inBlock = false;
            }
        }
        reader.close();

        if (!found) {
            System.out.println("No profile found for username: " + username);
        }
    }

    // ==================== GUI-FRIENDLY METHODS (Keep all original methods above) ====================

    /**
     * GUI-friendly: Staff login with parameters
     */
    public boolean staffLoginGUI(String username, String password) throws IOException {
        if (validateCredentials(username, password, staffLoginFile)) {
            this.currentStaffUsername = username;
            return true;
        }
        return false;
    }

    /**
     * GUI-friendly: Staff signup with parameters
     */
    public boolean staffSignUpGUI(String username, String password) throws IOException {
        if (userExists(username, staffLoginFile)) {
            return false;
        } else {
            FileWriter loginWrite = new FileWriter(STAFF_LOGIN_FILE, true);
            loginWrite.write(username + "," + password + "\n");
            loginWrite.close();
            return true;
        }
    }

    /**
     * GUI-friendly: Owner login with parameters
     */
    public boolean ownerLoginGUI(String username, String password) throws IOException {
        if (validateCredentials(username, password, ownerLoginFile)) {
            this.currentOwnerUsername = username;
            return true;
        }
        return false;
    }

    /**
     * GUI-friendly: Add animal with parameters
     */
    public void addAnimalRecordGUI(String animalId, int age, char gender, String purchaseDate,
                                   char vaccinated, String feedType, String feedTimes, String animalType) throws IOException {
        this.animalId = animalId;
        this.age = age;
        this.gender = gender;
        this.purchaseDate = purchaseDate;
        this.vaccinated = vaccinated;
        this.feedType = feedType;
        this.feedTimes = feedTimes;
        this.animalType = animalType;
        this.addAnimal();
    }

    /**
     * GUI-friendly: Search animal returns result string
     */
    public String searchAnimalGUI(String searchId) throws IOException {
        File file = new File(ANIMAL_FILE);
        if (!file.exists()) {
            return "No animal records found.";
        }
        boolean found = false;
        StringBuilder result = new StringBuilder();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("Animal ID =") && line.contains(searchId)) {
                found = true;
                result.append("\n Animal Found \n");
                result.append(line).append("\n");
                while (scanner.hasNextLine()) {
                    String nextLine = scanner.nextLine();
                    if (nextLine.startsWith("===")) break;
                    result.append(nextLine).append("\n");
                }
                break;
            }
        }
        scanner.close();
        if (!found) {
            return "No record found for Animal ID: " + searchId;
        }
        return result.toString();
    }

    /**
     * GUI-friendly: Delete animal returns message
     */
    public String deleteAnimalGUI(String deleteId) throws IOException {
        File original = new File(ANIMAL_FILE);
        File temp = new File("temp.txt");
        FileReader fr = new FileReader(original);
        Scanner reader = new Scanner(fr);
        FileWriter writer = new FileWriter(temp);
        boolean found = false;
        boolean skipBlock = false;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.startsWith("Animal ID =") && line.contains(deleteId)) {
                found = true;
                skipBlock = true;
                continue;
            }
            if (skipBlock) {
                if (line.startsWith("===")) {
                    skipBlock = false;
                }
                continue;
            }
            writer.write(line + "\n");
        }
        reader.close();
        writer.close();
        if (original.delete() && temp.renameTo(original)) {
            if (found) {
                return "Animal record deleted successfully.";
            } else {
                return "Animal ID not found.";
            }
        } else {
            return "Error occurred while deleting animal record.";
        }
    }

    /**
     * GUI-friendly: Show animals by type returns result string
     */
    public String showAnimalsByTypeGUI(String type) throws IOException {
        File file = new File(ANIMAL_FILE);
        if (!file.exists()) {
            return "No animal records found.";
        }
        Scanner reader = new Scanner(file);
        boolean found = false;
        StringBuilder result = new StringBuilder();
        StringBuilder block = new StringBuilder();
        boolean inBlock = false;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.startsWith("Animal ID =")) {
                block = new StringBuilder();
                inBlock = true;
            }
            if (inBlock) {
                block.append(line).append("\n");
            }
            if (line.toLowerCase().startsWith("animal type =") && line.toLowerCase().contains(type.toLowerCase())) {
                found = true;
                result.append("\n--- Animal Record Found ---\n");
                result.append(block.toString());
            }
            if (line.startsWith("===")) {
                inBlock = false;
            }
        }
        reader.close();
        if (!found) {
            return "No animal records found for type: " + type;
        }
        return result.toString();
    }

    /**
     * GUI-friendly: Show all animals returns result string
     */
    public String showAllAnimalsGUI() throws IOException {
        File file = new File(ANIMAL_FILE);
        if (!file.exists()) {
            return "No animal records found.";
        }
        Scanner reader = new Scanner(file);
        StringBuilder result = new StringBuilder("\nAll Animal Records:\n");
        while (reader.hasNextLine()) {
            result.append(reader.nextLine()).append("\n");
        }
        reader.close();
        return result.toString();
    }

    /**
     * GUI-friendly: Add milk record with parameters
     */
    public void addMilkRecordGUI(String date, String animalId, String quantity, String staffName, String pricePerLiter) throws IOException {
        addMilkRecord(date, animalId, quantity, staffName, pricePerLiter);
    }

    /**
     * GUI-friendly: Calculate daily milk price returns result string
     */
    public String calculateDailyMilkPriceGUI(String date, double pricePerLiter) throws IOException {
        File file = new File(MILK_FILE);
        if (!file.exists()) {
            return "No milk records found.";
        }
        Scanner reader = new Scanner(file);
        double totalLiters = 0;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.startsWith("Date = ") && line.contains(date)) {
                reader.nextLine(); // skip Animal ID
                String qtyLine = reader.nextLine();
                String[] parts = qtyLine.split("=");
                if (parts.length == 2) {
                    String qtyStr = parts[1].replace("liters", "").replace("liter", "").trim();
                    try {
                        totalLiters += Double.parseDouble(qtyStr);
                    } catch (NumberFormatException e) {
                        // skip invalid
                    }
                }
            }
        }
        reader.close();
        double totalPrice = totalLiters * pricePerLiter;

        // Append summary to milk_record.txt
        FileWriter milkWriter = new FileWriter(MILK_FILE, true);
        milkWriter.write("Daily Summary for " + date + ":\n");
        milkWriter.write("Total Milk = " + totalLiters + " liters\n");
        milkWriter.write("Price per liter = " + pricePerLiter + "\n");
        milkWriter.write("Total Price = " + totalPrice + "\n");
        milkWriter.write("==================================================\n");
        milkWriter.close();

        return "Total milk for " + date + ": " + totalLiters + " liters\n" +
                "Total price for " + date + ": " + totalPrice + "\n" +
                "Daily summary appended to milk_record.txt";
    }

    /**
     * GUI-friendly: Show milk record by animal ID returns result string
     */
    public String showMilkRecordByAnimalIdGUI(String animalId) throws IOException {
        File file = new File(MILK_FILE);
        if (!file.exists()) {
            return "No milk records found.";
        }
        Scanner reader = new Scanner(file);
        boolean found = false;
        StringBuilder result = new StringBuilder();
        String date = "", staffName = "", quantity = "", price = "";
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.startsWith("Date = ")) {
                date = line.substring(7).trim();
                staffName = "";
                quantity = "";
                price = "";
            } else if (line.startsWith("Animal ID = ")) {
                String thisId = line.substring(12).trim();
                if (thisId.equals(animalId)) {
                    found = true;
                    if (reader.hasNextLine()) quantity = reader.nextLine().replace("Milk Quantity = ", "").replace("liters", "").replace("liter", "").trim();
                    if (reader.hasNextLine()) staffName = reader.nextLine().replace("Staff Name = ", "").trim();
                    if (reader.hasNextLine()) price = reader.nextLine().replace("Price per Liter = ", "").trim();
                    result.append("Date: ").append(date).append("\n");
                    result.append("Animal ID: ").append(animalId).append("\n");
                    result.append("milkman Name: ").append(staffName).append("\n");
                    result.append("Total Milk: ").append(quantity).append(" liters\n");
                    result.append("Price per Liter: ").append(price).append("\n");
                    double totalPrice = 0.0;
                    try {
                        double qty = Double.parseDouble(quantity);
                        double prc = Double.parseDouble(price);
                        totalPrice = qty * prc;
                    } catch (Exception e) {
                        // ignore parse errors
                    }
                    result.append("Total milk Price: ").append(totalPrice).append("\n\n");
                }
            }
        }
        reader.close();
        if (!found) {
            return "No milk records found for Animal ID: " + animalId;
        }
        return result.toString();
    }

    /**
     * GUI-friendly: Manage staff with parameters
     */
    public void manageStaffGUI(String name, String workStatus, String workingHours, String salary, String staffType) throws IOException {
        this.staffName = name;
        this.workStatus = workStatus;
        this.workingHours = workingHours;
        this.salary = salary;
        this.staffType = staffType;
        this.addStaff();
    }

    /**
     * GUI-friendly: Show all staff returns result string
     */
    public String showAllStaffGUI() throws IOException {
        File file = new File(STAFF_FILE);
        if (!file.exists()) {
            return "No staff records found.";
        }
        Scanner reader = new Scanner(file);
        StringBuilder result = new StringBuilder("\nAll Staff Records:\n");
        while (reader.hasNextLine()) {
            result.append(reader.nextLine()).append("\n");
        }
        reader.close();
        return result.toString();
    }

    /**
     * GUI-friendly: Search staff returns result string
     */
    public String searchStaffGUI(String keyword) throws IOException {
        File file = new File(STAFF_FILE);
        if (!file.exists()) {
            return "No staff records found.";
        }
        Scanner reader = new Scanner(file);
        boolean found = false;
        StringBuilder result = new StringBuilder();
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.toLowerCase().contains(keyword.toLowerCase())) {
                found = true;
                result.append("\n--- Staff Record Found ---\n");
                result.append(line).append("\n");
                while (reader.hasNextLine()) {
                    String nextLine = reader.nextLine();
                    if (nextLine.startsWith("---") || nextLine.isBlank()) break;
                    result.append(nextLine).append("\n");
                }
                break;
            }
        }
        reader.close();
        if (!found) {
            return "No staff record found with keyword: " + keyword;
        }
        return result.toString();
    }

    /**
     * GUI-friendly: Remove staff returns message
     */
    public String removeStaffGUI(String nameToDelete) throws IOException {
        File original = new File(STAFF_FILE);
        File temp = new File("temp_staff.txt");
        if (!original.exists()) {
            return "No staff records found.";
        }
        FileReader fr = new FileReader(original);
        Scanner reader = new Scanner(fr);
        FileWriter writer = new FileWriter(temp);
        boolean found = false;
        boolean skipBlock = false;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.toLowerCase().contains("staff name =") && line.toLowerCase().contains(nameToDelete.toLowerCase())) {
                found = true;
                skipBlock = true;
                continue;
            }
            if (skipBlock) {
                if (line.startsWith("--------------------------------------------------")) {
                    skipBlock = false;
                }
                continue;
            }
            writer.write(line + "\n");
        }
        reader.close();
        writer.close();
        if (original.delete() && temp.renameTo(original)) {
            if (found) {
                return "Staff record removed successfully.";
            } else {
                return "Staff name not found.";
            }
        } else {
            return "Error occurred while deleting staff record.";
        }
    }

    /**
     * GUI-friendly: Show staff by type returns result string
     */
    public String showStaffByTypeGUI(String type) throws IOException {
        File file = new File(STAFF_FILE);
        if (!file.exists()) {
            return "No staff records found.";
        }
        Scanner reader = new Scanner(file);
        boolean found = false;
        StringBuilder result = new StringBuilder();
        StringBuilder block = new StringBuilder();
        boolean inBlock = false;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.startsWith("Staff Name =")) {
                block = new StringBuilder();
                inBlock = true;
            }
            if (inBlock) {
                block.append(line).append("\n");
            }
            if (line.toLowerCase().startsWith("staff type =") && line.toLowerCase().contains(type.toLowerCase())) {
                found = true;
                result.append("\n--- Staff Record Found ---\n");
                result.append(block.toString());
            }
            if (line.startsWith("---")) {
                inBlock = false;
            }
        }
        reader.close();
        if (!found) {
            return "No staff records found for type: " + type;
        }
        return result.toString();
    }

    /**
     * GUI-friendly: Add own profile with parameters
     */
    public void addOwnProfileGUI(String username, String status, String hours, String salary, String staffType) throws IOException {
        FileWriter staffWriter = new FileWriter(STAFF_FILE, true);
        String name = username;
        staffWriter.write("Staff Name = " + name + "\n");
        staffWriter.write("Work Status = " + status + "\n");
        staffWriter.write("Working Hours = " + hours + "\n");
        staffWriter.write("Salary = " + salary + "\n");
        staffWriter.write("Staff type = " + staffType + "\n");
        staffWriter.write("--------------------------------------------------\n");
        staffWriter.close();
    }

    /**
     * GUI-friendly: See staff profile returns result string
     */
    public String seeStaffProfileGUI(String username) throws IOException {
        File file = new File(STAFF_FILE);
        if (!file.exists()) {
            return "No staff records found.";
        }
        Scanner reader = new Scanner(file);
        boolean found = false;
        StringBuilder block = new StringBuilder();
        boolean inBlock = false;
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.startsWith("Staff Name =")) {
                block = new StringBuilder();
                inBlock = true;
            }
            if (inBlock) {
                block.append(line).append("\n");
            }
            if (line.toLowerCase().startsWith("staff name =") && line.toLowerCase().contains(username.toLowerCase())) {
                found = true;
            }
            if (line.startsWith("---")) {
                if (found) break;
                inBlock = false;
            }
        }
        reader.close();
        if (!found) {
            return "No profile found for username: " + username;
        }
        return "\n--- Your Staff Profile ---\n" + block.toString();
    }

    /**
     * Get current staff username
     */
    public String getCurrentStaffUsername() {
        return currentStaffUsername;
    }

    /**
     * Get current owner username
     */
    public String getCurrentOwnerUsername() {
        return currentOwnerUsername;
    }
}

// ==================== GUI CLASSES ====================

/**
 * Main GUI Entry Point - Animal Record System
 * Provides role selection interface
 */
class MainGUI extends JFrame {
    private Main mainSystem;

    public MainGUI() {
        mainSystem = new Main();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Animal Record System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Welcome to the Animal Record System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton staffButton = new JButton("Staff");
        staffButton.setFont(new Font("Arial", Font.PLAIN, 18));
        staffButton.setPreferredSize(new Dimension(200, 60));
        staffButton.setBackground(new Color(70, 130, 180));
        staffButton.setForeground(Color.black);
        staffButton.setFocusPainted(false);
        staffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openStaffLogin();
            }
        });

        JButton ownerButton = new JButton("Owner");
        ownerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        ownerButton.setPreferredSize(new Dimension(200, 60));
        ownerButton.setBackground(new Color(34, 139, 34));
        ownerButton.setForeground(Color.black);
        ownerButton.setFocusPainted(false);
        ownerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openOwnerLogin();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 18));
        exitButton.setPreferredSize(new Dimension(200, 60));
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.black);
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(staffButton);
        buttonPanel.add(ownerButton);
        buttonPanel.add(exitButton);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void openStaffLogin() {
        StaffLoginGUI staffLoginGUI = new StaffLoginGUI(this, mainSystem);
        staffLoginGUI.setVisible(true);
    }

    private void openOwnerLogin() {
        OwnerLoginGUI ownerLoginGUI = new OwnerLoginGUI(this, mainSystem);
        ownerLoginGUI.setVisible(true);
    }
}

/**
 * Staff Login GUI - Handles staff signup and login
 */
class StaffLoginGUI extends JDialog {
    private Main mainSystem;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean loginSuccessful = false;

    public StaffLoginGUI(Frame parent, Main mainSystem) {
        super(parent, "Staff Login", true);
        this.mainSystem = mainSystem;
        initializeGUI();
    }

    private void initializeGUI() {
        setSize(450, 350);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Welcome Staff", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.PLAIN, 14));
        signUpButton.setPreferredSize(new Dimension(100, 35));
        signUpButton.setBackground(new Color(70, 130, 180));
        signUpButton.setForeground(Color.black);
        signUpButton.setFocusPainted(false);
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 14));
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.setBackground(new Color(34, 139, 34));
        loginButton.setForeground(Color.black);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.black);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(signUpButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (mainSystem.staffSignUpGUI(username, password)) {
                JOptionPane.showMessageDialog(this, "Sign up successfully! Please login now.", "Success", JOptionPane.INFORMATION_MESSAGE);
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Staff already exists, try another username.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (mainSystem.staffLoginGUI(username, password)) {
                loginSuccessful = true;
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                StaffDashboardGUI dashboard = new StaffDashboardGUI((JFrame) getParent(), mainSystem);
                dashboard.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}

/**
 * Owner Login GUI - Handles owner login
 */
class OwnerLoginGUI extends JDialog {
    private Main mainSystem;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean loginSuccessful = false;

    public OwnerLoginGUI(Frame parent, Main mainSystem) {
        super(parent, "Owner Login", true);
        this.mainSystem = mainSystem;
        initializeGUI();
    }

    private void initializeGUI() {
        setSize(450, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Welcome Owner", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 14));
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.setBackground(new Color(34, 139, 34));
        loginButton.setForeground(Color.black);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.black);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (mainSystem.ownerLoginGUI(username, password)) {
                loginSuccessful = true;
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                OwnerDashboardGUI dashboard = new OwnerDashboardGUI((JFrame) getParent(), mainSystem);
                dashboard.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}

/**
 * Staff Dashboard GUI - Main interface for staff operations
 */
class StaffDashboardGUI extends JFrame {
    private Main mainSystem;
    private JTextArea outputArea;

    public StaffDashboardGUI(JFrame parent, Main mainSystem) {
        super("Staff Dashboard");
        this.mainSystem = mainSystem;
        initializeGUI();
    }

    private void initializeGUI() {
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(40, 99, 148));

        JLabel titleLabel = new JLabel("Staff Menu", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(new Color(0, 0, 0));

        createButton(buttonPanel, "1. Add Animal", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddAnimalDialog();
            }
        });

        createButton(buttonPanel, "2. Search Animal", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchAnimalDialog();
            }
        });

        createButton(buttonPanel, "3. Add Your Profile", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddProfileDialog();
            }
        });

        createButton(buttonPanel, "4. See Your Profile", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSeeProfileDialog();
            }
        });

        createButton(buttonPanel, "5. Show Animals by Type", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAnimalsByTypeDialog();
            }
        });

        createButton(buttonPanel, "6. Add Milk Record", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddMilkRecordDialog();
            }
        });

        createButton(buttonPanel, "7. Show Milk Record by Animal ID", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMilkRecordByAnimalIdDialog();
            }
        });

        createButton(buttonPanel, "8. Show All Animal Records", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllAnimalsDialog();
            }
        });

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBackground(Color.white);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        scrollPane.setPreferredSize(new Dimension(0, 150));
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exitPanel.setBackground(new Color(240, 248, 255));
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.setPreferredSize(new Dimension(100, 35));
        exitButton.setBackground(new Color(220, 20, 60));
        exitButton.setForeground(Color.black);
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exitPanel.add(exitButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(scrollPane, BorderLayout.CENTER);
        southPanel.add(exitPanel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void createButton(JPanel panel, String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.black);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        panel.add(button);
    }

    private void showAddAnimalDialog() {
        AddAnimalDialog dialog = new AddAnimalDialog(this, mainSystem);
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            outputArea.append("Animal record added successfully.\n");
        }
    }

    private void showSearchAnimalDialog() {
        String animalId = JOptionPane.showInputDialog(this, "Enter Animal ID to search:", "Search Animal", JOptionPane.QUESTION_MESSAGE);
        if (animalId != null && !animalId.trim().isEmpty()) {
            try {
                String result = mainSystem.searchAnimalGUI(animalId.trim());
                outputArea.append(result + "\n");
                DisplayDialog.showResult(this, "Search Result", result);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAddProfileDialog() {
        AddProfileDialog dialog = new AddProfileDialog(this, mainSystem, mainSystem.getCurrentStaffUsername());
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            outputArea.append("Your profile has been added.\n");
        }
    }

    private void showSeeProfileDialog() {
        try {
            String result = mainSystem.seeStaffProfileGUI(mainSystem.getCurrentStaffUsername());
            outputArea.append(result + "\n");
            DisplayDialog.showResult(this, "Your Staff Profile", result);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAnimalsByTypeDialog() {
        String type = JOptionPane.showInputDialog(this, "Enter animal type to list (e.g., cow, goat):", "Show Animals by Type", JOptionPane.QUESTION_MESSAGE);
        if (type != null && !type.trim().isEmpty()) {
            try {
                String result = mainSystem.showAnimalsByTypeGUI(type.trim());
                outputArea.append(result + "\n");
                DisplayDialog.showResult(this, "Animals by Type", result);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAddMilkRecordDialog() {
        AddMilkRecordDialog dialog = new AddMilkRecordDialog(this, mainSystem);
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            outputArea.append("Milk record added.\n");
        }
    }

    private void showMilkRecordByAnimalIdDialog() {
        String animalId = JOptionPane.showInputDialog(this, "Enter Animal ID to show milk records:", "Milk Records", JOptionPane.QUESTION_MESSAGE);
        if (animalId != null && !animalId.trim().isEmpty()) {
            try {
                String result = mainSystem.showMilkRecordByAnimalIdGUI(animalId.trim());
                outputArea.append(result + "\n");
                DisplayDialog.showResult(this, "Milk Records", result);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAllAnimalsDialog() {
        try {
            String result = mainSystem.showAllAnimalsGUI();
            outputArea.append(result + "\n");
            DisplayDialog.showResult(this, "All Animal Records", result);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

/**
 * Owner Dashboard GUI - Main interface for owner operations
 */
class OwnerDashboardGUI extends JFrame {
    private Main mainSystem;
    private JTextArea outputArea;

    public OwnerDashboardGUI(JFrame parent, Main mainSystem) {
        super("Owner Dashboard");
        this.mainSystem = mainSystem;
        initializeGUI();
    }

    private void initializeGUI() {
        setSize(900, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Owner Menu", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        createButton(buttonPanel, "1. Add Animal", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddAnimalDialog();
            }
        });

        createButton(buttonPanel, "2. Search Animal", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchAnimalDialog();
            }
        });

        createButton(buttonPanel, "3. Delete Animal", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDeleteAnimalDialog();
            }
        });

        createButton(buttonPanel, "4. Add Staff", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddStaffDialog();
            }
        });

        createButton(buttonPanel, "5. Show All Staff", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllStaffDialog();
            }
        });

        createButton(buttonPanel, "6. Search Staff", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchStaffDialog();
            }
        });

        createButton(buttonPanel, "7. Remove Staff", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRemoveStaffDialog();
            }
        });

        createButton(buttonPanel, "8. Show Animals by Type", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAnimalsByTypeDialog();
            }
        });

        createButton(buttonPanel, "9. Show Staff by Type", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStaffByTypeDialog();
            }
        });

        createButton(buttonPanel, "10. Add Milk Record", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddMilkRecordDialog();
            }
        });

        createButton(buttonPanel, "11. Calculate Daily Milk Price", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCalculateDailyMilkPriceDialog();
            }
        });

        createButton(buttonPanel, "12. Show All Animal Records", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllAnimalsDialog();
            }
        });

        createButton(buttonPanel, "13. Exit", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        scrollPane.setPreferredSize(new Dimension(0, 150));
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void createButton(JPanel panel, String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 13));
        button.setPreferredSize(new Dimension(200, 45));
        button.setBackground(new Color(34, 139, 34));
        button.setForeground(Color.black);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        panel.add(button);
    }

    private void showAddAnimalDialog() {
        AddAnimalDialog dialog = new AddAnimalDialog(this, mainSystem);
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            outputArea.append("Animal record added successfully.\n");
        }
    }

    private void showSearchAnimalDialog() {
        String animalId = JOptionPane.showInputDialog(this, "Enter Animal ID to search:", "Search Animal", JOptionPane.QUESTION_MESSAGE);
        if (animalId != null && !animalId.trim().isEmpty()) {
            try {
                String result = mainSystem.searchAnimalGUI(animalId.trim());
                outputArea.append(result + "\n");
                DisplayDialog.showResult(this, "Search Result", result);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showDeleteAnimalDialog() {
        String animalId = JOptionPane.showInputDialog(this, "Enter Animal ID to delete:", "Delete Animal", JOptionPane.QUESTION_MESSAGE);
        if (animalId != null && !animalId.trim().isEmpty()) {
            try {
                String result = mainSystem.deleteAnimalGUI(animalId.trim());
                outputArea.append(result + "\n");
                JOptionPane.showMessageDialog(this, result, "Delete Animal", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAddStaffDialog() {
        AddStaffDialog dialog = new AddStaffDialog(this, mainSystem);
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            outputArea.append("Staff record added successfully.\n");
        }
    }

    private void showAllStaffDialog() {
        try {
            String result = mainSystem.showAllStaffGUI();
            outputArea.append(result + "\n");
            DisplayDialog.showResult(this, "All Staff Records", result);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showSearchStaffDialog() {
        String keyword = JOptionPane.showInputDialog(this, "Enter Staff Name or ID to search:", "Search Staff", JOptionPane.QUESTION_MESSAGE);
        if (keyword != null && !keyword.trim().isEmpty()) {
            try {
                String result = mainSystem.searchStaffGUI(keyword.trim());
                outputArea.append(result + "\n");
                DisplayDialog.showResult(this, "Search Result", result);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showRemoveStaffDialog() {
        String name = JOptionPane.showInputDialog(this, "Enter Staff Name to remove:", "Remove Staff", JOptionPane.QUESTION_MESSAGE);
        if (name != null && !name.trim().isEmpty()) {
            try {
                String result = mainSystem.removeStaffGUI(name.trim());
                outputArea.append(result + "\n");
                JOptionPane.showMessageDialog(this, result, "Remove Staff", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAnimalsByTypeDialog() {
        String type = JOptionPane.showInputDialog(this, "Enter animal type to list (e.g., cow, goat):", "Show Animals by Type", JOptionPane.QUESTION_MESSAGE);
        if (type != null && !type.trim().isEmpty()) {
            try {
                String result = mainSystem.showAnimalsByTypeGUI(type.trim());
                outputArea.append(result + "\n");
                DisplayDialog.showResult(this, "Animals by Type", result);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showStaffByTypeDialog() {
        String type = JOptionPane.showInputDialog(this, "Enter staff type to list (e.g., doctor, milkman):", "Show Staff by Type", JOptionPane.QUESTION_MESSAGE);
        if (type != null && !type.trim().isEmpty()) {
            try {
                String result = mainSystem.showStaffByTypeGUI(type.trim());
                outputArea.append(result + "\n");
                DisplayDialog.showResult(this, "Staff by Type", result);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAddMilkRecordDialog() {
        AddMilkRecordDialog dialog = new AddMilkRecordDialog(this, mainSystem);
        dialog.setVisible(true);
        if (dialog.isSuccess()) {
            outputArea.append("Milk record added.\n");
        }
    }

    private void showCalculateDailyMilkPriceDialog() {
        String date = JOptionPane.showInputDialog(this, "Enter date to calculate (DD-MM-YYYY):", "Calculate Daily Milk Price", JOptionPane.QUESTION_MESSAGE);
        if (date != null && !date.trim().isEmpty()) {
            String priceStr = JOptionPane.showInputDialog(this, "Enter 1 liter milk price:", "Calculate Daily Milk Price", JOptionPane.QUESTION_MESSAGE);
            if (priceStr != null && !priceStr.trim().isEmpty()) {
                try {
                    double pricePerLiter = Double.parseDouble(priceStr.trim());
                    String result = mainSystem.calculateDailyMilkPriceGUI(date.trim(), pricePerLiter);
                    outputArea.append(result + "\n");
                    DisplayDialog.showResult(this, "Daily Milk Price Calculation", result);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showAllAnimalsDialog() {
        try {
            String result = mainSystem.showAllAnimalsGUI();
            outputArea.append(result + "\n");
            DisplayDialog.showResult(this, "All Animal Records", result);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

/**
 * Display Dialog - Helper class for displaying results in a scrollable text area
 */
class DisplayDialog extends JDialog {
    public DisplayDialog(Frame parent, String title, String content) {
        super(parent, title, true);
        initializeGUI(content);
    }

    private void initializeGUI(String content) {
        setSize(700, 500);
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(Color.WHITE);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.setBackground(new Color(70, 130, 180));
        closeButton.setForeground(Color.black);
        closeButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    public static void showResult(Component parent, String title, String content) {
        DisplayDialog dialog = new DisplayDialog((Frame) SwingUtilities.getWindowAncestor(parent), title, content);
        dialog.setVisible(true);
    }
}

/**
 * Add Animal Dialog - Form for adding animal records
 */
class AddAnimalDialog extends JDialog {
    private Main mainSystem;
    private boolean success = false;
    private JTextField animalIdField, ageField, purchaseDateField, feedTypeField, feedTimesField, animalTypeField;
    private JComboBox<String> genderCombo, vaccinatedCombo;

    public AddAnimalDialog(Frame parent, Main mainSystem) {
        super(parent, "Add Animal Record", true);
        this.mainSystem = mainSystem;
        initializeGUI();
    }

    private void initializeGUI() {
        setSize(500, 500);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Animal ID:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        animalIdField = new JTextField(20);
        formPanel.add(animalIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        ageField = new JTextField(20);
        formPanel.add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Gender (M/F):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        genderCombo = new JComboBox<>(new String[]{"M", "F"});
        formPanel.add(genderCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Purchase Date (DD-MM-YYYY):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        purchaseDateField = new JTextField(20);
        formPanel.add(purchaseDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Vaccinated? (Y/N):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        vaccinatedCombo = new JComboBox<>(new String[]{"Y", "N"});
        formPanel.add(vaccinatedCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Feed Type:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        feedTypeField = new JTextField(20);
        formPanel.add(feedTypeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Feed Times per Day:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        feedTimesField = new JTextField(20);
        formPanel.add(feedTimesField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Animal Type:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        animalTypeField = new JTextField(20);
        formPanel.add(animalTypeField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(100, 35));
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.black);
        addButton.setFocusPainted(false);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAdd();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.black);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void handleAdd() {
        try {
            String animalId = animalIdField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            char gender = ((String) genderCombo.getSelectedItem()).charAt(0);
            String purchaseDate = purchaseDateField.getText().trim();
            char vaccinated = ((String) vaccinatedCombo.getSelectedItem()).charAt(0);
            String feedType = feedTypeField.getText().trim();
            String feedTimes = feedTimesField.getText().trim();
            String animalType = animalTypeField.getText().trim();

            if (animalId.isEmpty() || purchaseDate.isEmpty() || feedType.isEmpty() || feedTimes.isEmpty() || animalType.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            mainSystem.addAnimalRecordGUI(animalId, age, gender, purchaseDate, vaccinated, feedType, feedTimes, animalType);
            success = true;
            JOptionPane.showMessageDialog(this, "Animal record added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid age format. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSuccess() {
        return success;
    }
}

/**
 * Add Milk Record Dialog - Form for adding milk records
 */
class AddMilkRecordDialog extends JDialog {
    private Main mainSystem;
    private boolean success = false;
    private JTextField dateField, animalIdField, quantityField, staffNameField, pricePerLiterField;

    public AddMilkRecordDialog(Frame parent, Main mainSystem) {
        super(parent, "Add Milk Record", true);
        this.mainSystem = mainSystem;
        initializeGUI();
    }

    private void initializeGUI() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Date (DD-MM-YYYY):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        dateField = new JTextField(20);
        formPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Animal ID:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        animalIdField = new JTextField(20);
        formPanel.add(animalIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Milk Quantity (liters):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        quantityField = new JTextField(20);
        formPanel.add(quantityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Staff Name:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        staffNameField = new JTextField(20);
        formPanel.add(staffNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Price per Liter:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        pricePerLiterField = new JTextField(20);
        formPanel.add(pricePerLiterField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(100, 35));
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.black);
        addButton.setFocusPainted(false);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAdd();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.black);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void handleAdd() {
        try {
            String date = dateField.getText().trim();
            String animalId = animalIdField.getText().trim();
            String quantity = quantityField.getText().trim();
            String staffName = staffNameField.getText().trim();
            String pricePerLiter = pricePerLiterField.getText().trim();

            if (date.isEmpty() || animalId.isEmpty() || quantity.isEmpty() || staffName.isEmpty() || pricePerLiter.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            mainSystem.addMilkRecordGUI(date, animalId, quantity, staffName, pricePerLiter);
            success = true;
            JOptionPane.showMessageDialog(this, "Milk record added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSuccess() {
        return success;
    }
}

/**
 * Add Staff Dialog - Form for adding staff records
 */
class AddStaffDialog extends JDialog {
    private Main mainSystem;
    private boolean success = false;
    private JTextField nameField, workStatusField, workingHoursField, salaryField, staffTypeField;

    public AddStaffDialog(Frame parent, Main mainSystem) {
        super(parent, "Add Staff", true);
        this.mainSystem = mainSystem;
        initializeGUI();
    }

    private void initializeGUI() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Staff Name:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Work Status (Full-time/Part-time):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        workStatusField = new JTextField(20);
        formPanel.add(workStatusField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Working Hours:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        workingHoursField = new JTextField(20);
        formPanel.add(workingHoursField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        salaryField = new JTextField(20);
        formPanel.add(salaryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Staff Type:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        staffTypeField = new JTextField(20);
        formPanel.add(staffTypeField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(100, 35));
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.black);
        addButton.setFocusPainted(false);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAdd();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.black);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void handleAdd() {
        try {
            String name = nameField.getText().trim();
            String workStatus = workStatusField.getText().trim();
            String workingHours = workingHoursField.getText().trim();
            String salary = salaryField.getText().trim();
            String staffType = staffTypeField.getText().trim();

            if (name.isEmpty() || workStatus.isEmpty() || workingHours.isEmpty() || salary.isEmpty() || staffType.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            mainSystem.manageStaffGUI(name, workStatus, workingHours, salary, staffType);
            success = true;
            JOptionPane.showMessageDialog(this, "Staff record added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSuccess() {
        return success;
    }
}

/**
 * Add Profile Dialog - Form for staff to add their own profile
 */
class AddProfileDialog extends JDialog {
    private Main mainSystem;
    private String username;
    private boolean success = false;
    private JTextField workStatusField, workingHoursField, salaryField, staffTypeField;

    public AddProfileDialog(Frame parent, Main mainSystem, String username) {
        super(parent, "Add Your Profile", true);
        this.mainSystem = mainSystem;
        this.username = username;
        initializeGUI();
    }

    private void initializeGUI() {
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel infoLabel = new JLabel("Adding/Updating your profile. Your username (" + username + ") will be used as Staff Name.", JLabel.CENTER);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(infoLabel, BorderLayout.NORTH);


        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Work Status (Full-time/Part-time):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        workStatusField = new JTextField(20);
        formPanel.add(workStatusField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Working Hours:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        workingHoursField = new JTextField(20);
        formPanel.add(workingHoursField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        salaryField = new JTextField(20);
        formPanel.add(salaryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Staff Type:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        staffTypeField = new JTextField(20);
        formPanel.add(staffTypeField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(0, 0, 0));

        JButton addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(100, 35));
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.black);
        addButton.setFocusPainted(false);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAdd();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.black);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void handleAdd() {
        try {
            String status = workStatusField.getText().trim();
            String hours = workingHoursField.getText().trim();
            String salary = salaryField.getText().trim();
            String staffType = staffTypeField.getText().trim();

            if (status.isEmpty() || hours.isEmpty() || salary.isEmpty() || staffType.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            mainSystem.addOwnProfileGUI(username, status, hours, salary, staffType);
            success = true;
            JOptionPane.showMessageDialog(this, "Your profile has been added!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSuccess() {
        return success;
    }
}
