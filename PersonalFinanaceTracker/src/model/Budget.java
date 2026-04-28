package model;

public class Budget {
    private Category category;
    private double monthlyLimit;

    public Budget(Category category, double monthlyLimit) {
        this.category     = category;
        this.monthlyLimit = monthlyLimit;
    }

    // Getters and setters
    public Category getCategory()          { return category; }
    public double getLimit()               { return monthlyLimit; }
    public void setLimit(double limit)     { this.monthlyLimit = limit; }

    // CSV format: CATEGORY_ENUM,monthlyLimit
    public String toCSV() {
        return category.name() + "," + monthlyLimit;
    }

    public static Budget fromCSV(String line) {
        String[] parts    = line.split(",", 2);
        Category category = Category.valueOf(parts[0]);
        double limit      = Double.parseDouble(parts[1]);
        return new Budget(category, limit);
    }
}