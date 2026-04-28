package service;

import model.Category;
import model.Expense;
import model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExpenseService {

    // Add an expense to a user's list
    public void addExpense(User user, Expense expense) {
        user.addExpense(expense);
    }

    // Sum all expenses for a given category
    public double getTotalByCategory(User user, Category category) {
        double total = 0;
        for (Expense e : user.getExpensesByCategory(category)) {
            total += e.getAmount();
        }
        return total;
    }

    // Sort by DATE (newest first), AMOUNT (highest first), or CATEGORY (A-Z)
    public List<Expense> sortExpenses(User user, SortCriteria criteria) {
        List<Expense> sorted = new ArrayList<>(user.getExpenses());

        switch (criteria) {
            case DATE:
                sorted.sort(Comparator.comparing(Expense::getDate).reversed());
                break;
            case AMOUNT:
                sorted.sort(Comparator.comparingDouble(Expense::getAmount).reversed());
                break;
            case NAME:
            	sorted.sort(Comparator.comparing(Expense::getName));
                break;
        }

        return sorted;
    }

    // Enum for sort options — safer than passing a raw String
    public enum SortCriteria {
        DATE, AMOUNT, NAME
    }
}