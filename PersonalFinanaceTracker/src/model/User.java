package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String username;
    private String password;
    private List<Expense> expenses;
    private List<Budget> budgets;

    public User(String userId, String username, String password) {
        this.userId   = userId;
        this.username = username;
        this.password = password;
        this.expenses = new ArrayList<>();
        this.budgets  = new ArrayList<>();

        // Initialize a default $0 budget for every category
        for (Category c : Category.values()) {
            this.budgets.add(new Budget(c, 0.0));
        }
    }

    // Getters
    public String getUserId()          { return userId; }
    public String getUsername()        { return username; }
    public String getPassword()        { return password; }
    public List<Expense> getExpenses() { return expenses; }
    public List<Budget> getBudgets()   { return budgets; }

    // Add an expense to this user's list
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    // Return all expenses for a given category
    public List<Expense> getExpensesByCategory(Category category) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : expenses) {
            if (e.getCategory() == category) {
                result.add(e);
            }
        }
        return result;
    }

    // Return the Budget object for a given category
    public Budget getBudget(Category category) {
        for (Budget b : budgets) {
            if (b.getCategory() == category) {
                return b;
            }
        }
        return null;
    }

    // CSV header line for saving: userId,username,password
    public String toCSV() {
        return userId + "," + username + "," + password;
    }

    public static User fromCSV(String line) {
        String[] parts = line.split(",", 3);
        return new User(parts[0], parts[1], parts[2]);
    }
}