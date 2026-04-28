package model;

import java.time.LocalDate;

public class Expense {
    private String name;
    private double amount;
    private Category category;
    private LocalDate date;

    public Expense(String name, double amount, Category category, LocalDate date) {
        this.name     = name;
        this.amount   = amount;
        this.category = category;
        this.date     = date;
    }

    // Getters
    public String getName()        { return name; }
    public double getAmount()      { return amount; }
    public Category getCategory()  { return category; }
    public LocalDate getDate()     { return date; }

    // CSV format: name,amount,CATEGORY_ENUM,date
    public String toCSV() {
        return name + "," + amount + "," + category.name() + "," + date;
    }

    public static Expense fromCSV(String line) {
        String[] parts = line.split(",", 4);
        String name       = parts[0];
        double amount     = Double.parseDouble(parts[1]);
        Category category = Category.valueOf(parts[2]);
        LocalDate date    = LocalDate.parse(parts[3]);
        return new Expense(name, amount, category, date);
    }

    @Override
    public String toString() {
        return String.format("  %-20s | %-26s | $%8.2f | %s", name, category.getDisplayName(), amount, date);
    }
}