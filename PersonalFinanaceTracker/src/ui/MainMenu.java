package ui;

import model.Category;
import model.Expense;
import model.User;
import service.*;
import service.ExpenseService.SortCriteria;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private Scanner sc = new Scanner(System.in);

    private DataManager    dataManager;
    private LoginService   loginService;
    private ExpenseService expenseService;
    private BudgetService  budgetService;

    private List<User> allUsers;
    private User currentUser;

    public MainMenu() {
        dataManager    = new DataManager();
        allUsers       = dataManager.loadUsers();
        loginService   = new LoginService(allUsers);
        expenseService = new ExpenseService();
        budgetService  = new BudgetService(expenseService);
    }

    // Menu Beginning Point

    public void showMenu() {
        printBanner();
        while (true) {
            if (currentUser == null) {
                showGuestMenu();
            } else {
                showUserMenu();
            }
        }
    }

    // Menu before login (guest)

    private void showGuestMenu() {
        System.out.println("\n1. Login");
        System.out.println("2. Exit");
        System.out.print("Choice: ");

        switch (sc.nextLine().trim()) {
            case "1" -> handleLogin();
            case "2" -> exitApp();
            default  -> System.out.println("Invalid choice.");
        }
    }

    // Menu after login (user)

    private void showUserMenu() {
        System.out.println("\n=============================");
        System.out.println("  Hello, " + currentUser.getUsername() + "!");
        System.out.println("=============================");
        System.out.println("1. Add expense");
        System.out.println("2. Set budget limit");
        System.out.println("3. View spending summary");
        System.out.println("4. Sort and view transactions");
        System.out.println("5. Logout (saves data)");
        System.out.print("Choice: ");

        switch (sc.nextLine().trim()) {
            case "1" -> handleAddExpense();
            case "2" -> handleSetBudget();
            case "3" -> handleViewSummary();
            case "4" -> handleSortTransactions();
            case "5" -> handleLogout();
            default  -> System.out.println("Invalid choice.");
        }
    }

    // Login

    public void handleLogin() {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        User user = loginService.login(username, password);

        if (user != null) {
            currentUser = user;
            System.out.println("\nLogin successful! Welcome, " + currentUser.getUsername() + ".");
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    // Add Expense

    public void handleAddExpense() {
        System.out.println("\n-- Add Expense --");

        System.out.print("Description (e.g. Trader Joe's): ");
        String name = sc.nextLine().trim();

        double amount = promptDouble("Amount ($): ");

        System.out.println("Category:");
        Category.printMenu();
        int catChoice = (int) promptDouble("Choose (1-" + Category.values().length + "): ");
       
        Category category = Category.fromChoice(catChoice);
        if (category == null) {
            return;
        }

        System.out.print("Date (YYYY-MM-DD) or press Enter for today: ");
        String dateInput = sc.nextLine().trim();
        LocalDate date = dateInput.isEmpty() ? LocalDate.now() : LocalDate.parse(dateInput);

        // Check budget before adding
        if (budgetService.checkBudget(currentUser, category, amount)) {
            System.out.println("\n  *** WARNING: You have exceeded your monthly budget for "
                    + category.getDisplayName() + "! ***\n");
        }

        Expense expense = new Expense(name, amount, category, date);
        expenseService.addExpense(currentUser, expense);
        System.out.println("Expense added: " + expense);
    }

    // Set Budget

    public void handleSetBudget() {
        System.out.println("\n-- Set Monthly Budget Limit --");
        System.out.println("Categories:");
        Category.printMenu();

        int catChoice = (int) promptDouble("Choose category (1-" + Category.values().length + "): ");
        
        Category category = Category.fromChoice(catChoice);
        if (category == null) {
            return;
        }

        double limit = promptDouble("Monthly limit for " + category.getDisplayName() + " ($): ");
        budgetService.setBudgetLimit(currentUser, category, limit);
        System.out.println("Budget set: $" + String.format("%.2f", limit)
                + " for " + category.getDisplayName());
    }

    // View Summary

    public void handleViewSummary() {
        System.out.println("\n======= SPENDING SUMMARY =======");
        System.out.printf("%-28s %10s %12s %12s%n",
                "Category", "Spent", "Budget", "Remaining");
        System.out.println("-".repeat(64));

        double grandTotal = 0;

        for (Category c : Category.values()) {
            double spent     = expenseService.getTotalByCategory(currentUser, c);
            double remaining = budgetService.getRemainingBudget(currentUser, c);
            double limit     = currentUser.getBudget(c).getLimit();
            grandTotal      += spent;

            String budgetStr    = limit > 0 ? String.format("$%,.2f", limit) : "not set";
            String remainingStr = limit > 0
                    ? (remaining < 0
                        ? String.format("-$%,.2f !!!", Math.abs(remaining))
                        : String.format("$%,.2f", remaining))
                    : "  --";

            System.out.printf("%-28s %10s %12s %12s%n",
                    c.getDisplayName(),
                    String.format("$%,.2f", spent),
                    budgetStr,
                    remainingStr);
        }

        System.out.println("-".repeat(64));
        System.out.printf("%-28s %10s%n", "TOTAL SPENT", String.format("$%,.2f", grandTotal));
    }

    // Sort Transactions

    public void handleSortTransactions() {
        if (currentUser.getExpenses().isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }

        System.out.println("\n-- Sort By (choose an option) --");
        System.out.println("1. Date (newest first)");
        System.out.println("2. Amount (highest first)");
        System.out.println("3. Name (A-Z)");
        System.out.print("Choice: ");

        SortCriteria criteria;
        switch (sc.nextLine().trim()) {
            case "1" -> criteria = SortCriteria.DATE;
            case "2" -> criteria = SortCriteria.AMOUNT;
            case "3" -> criteria = SortCriteria.NAME;
            default  -> { System.out.println("Invalid choice."); return; }
        }

        List<Expense> sorted = expenseService.sortExpenses(currentUser, criteria);

        System.out.println();
        System.out.printf("  %-20s %-26s %10s  %s%n",
                "Description", "Category", "Amount", "Date");
        System.out.println("  " + "-".repeat(70));
        for (Expense e : sorted) {
            System.out.println(e);
        }
    }

    // Logout (saving data)

    public void handleLogout() {
        dataManager.saveUsers(allUsers);
        System.out.println("Data saved. Goodbye, " + currentUser.getUsername() + "!");
        currentUser = null;
    }

    // Exit Program

    private void exitApp() {
        if (currentUser != null) {
            dataManager.saveUsers(allUsers);
        }
        System.out.println("Goodbye!");
        System.exit(0);
    }

    // Helpers

    private double promptDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void printBanner() {
        System.out.println("=========================================");
        System.out.println("     Personal Finance Tracker            ");
        System.out.println("=========================================");
    }
}