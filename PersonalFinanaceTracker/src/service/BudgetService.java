package service;

import model.Budget;
import model.Category;
import model.User;

public class BudgetService {

    private ExpenseService expenseService;

    public BudgetService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // Set the monthly limit for a category
    public void setBudgetLimit(User user, Category category, double limit) {
        Budget budget = user.getBudget(category);
        if (budget != null) {
            budget.setLimit(limit);
        }
    }

    // Returns how much is left in the budget (negative = over budget)
    public double getRemainingBudget(User user, Category category) {
        Budget budget = user.getBudget(category);
        if (budget == null || budget.getLimit() <= 0) return -1; // no limit set
        double spent = expenseService.getTotalByCategory(user, category);
        return budget.getLimit() - spent;
    }

    // Returns true if adding newAmount would exceed the budget for that category
    public boolean checkBudget(User user, Category category, double newAmount) {
        Budget budget = user.getBudget(category);
        if (budget == null || budget.getLimit() <= 0) return false; // no limit set
        double spent = expenseService.getTotalByCategory(user, category);
        return (spent + newAmount) > budget.getLimit();
    }
}