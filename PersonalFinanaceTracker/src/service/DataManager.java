package service;

import model.Budget;
import model.Expense;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String DATA_DIR      = "data/";
    private static final String USERS_FILE    = DATA_DIR + "users.csv";
    private static final String EXPENSES_DIR  = DATA_DIR + "expenses/";
    private static final String BUDGETS_DIR   = DATA_DIR + "budgets/";

    public DataManager() {
        // Create directories if they don't exist
        new File(DATA_DIR).mkdirs();
        new File(EXPENSES_DIR).mkdirs();
        new File(BUDGETS_DIR).mkdirs();
    }

    // ── Load all users from users.csv ─────────────────────────────

    public List<User> loadUsers() {
    	System.out.println("Looking for: " + new File(USERS_FILE).getAbsolutePath());
        List<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                User user = User.fromCSV(line);
                loadExpensesForUser(user);
                loadBudgetsForUser(user);
                users.add(user);
            }
        } catch (IOException e) {
            System.out.println("Warning: could not read users file.");
        }

        return users;
    }

    // ── Save all users to users.csv ───────────────────────────────

    public void saveUsers(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                writer.println(u.toCSV());
                saveExpensesForUser(u);
                saveBudgetsForUser(u);
            }
        } catch (IOException e) {
            System.out.println("Error: could not save users file.");
        }
    }

    // ── Expenses ──────────────────────────────────────────────────

    private void loadExpensesForUser(User user) {
        File file = new File(EXPENSES_DIR + user.getUserId() + ".csv");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) user.addExpense(Expense.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("Warning: could not load expenses for " + user.getUsername());
        }
    }

    private void saveExpensesForUser(User user) {
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(EXPENSES_DIR + user.getUserId() + ".csv"))) {
            for (Expense e : user.getExpenses()) {
                writer.println(e.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Error: could not save expenses for " + user.getUsername());
        }
    }

    // ── Budgets ───────────────────────────────────────────────────

    private void loadBudgetsForUser(User user) {
        File file = new File(BUDGETS_DIR + user.getUserId() + ".csv");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                Budget loaded = Budget.fromCSV(line);
                Budget existing = user.getBudget(loaded.getCategory());
                if (existing != null) {
                    existing.setLimit(loaded.getLimit());
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: could not load budgets for " + user.getUsername());
        }
    }

    private void saveBudgetsForUser(User user) {
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(BUDGETS_DIR + user.getUserId() + ".csv"))) {
            for (Budget b : user.getBudgets()) {
                writer.println(b.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Error: could not save budgets for " + user.getUsername());
        }
    }
}