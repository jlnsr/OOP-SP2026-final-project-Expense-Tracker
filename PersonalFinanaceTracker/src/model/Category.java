package model;

public enum Category {
    FOOD_GROCERIES("Food & Groceries"),
    UTILITIES("Utilities"),
    ENTERTAINMENT("Entertainment"),
    SUBSCRIPTIONS("Auto-renewal Subscriptions");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    // Print all categories as a numbered menu
    public static void printMenu() {
        System.out.println("  1. Food & Groceries");
        System.out.println("  2. Utilities");
        System.out.println("  3. Entertainment");
        System.out.println("  4. Auto-renewal Subscriptions");
    }

    // Convert a menu number (1-based) to a Category
    public static Category fromChoice(int choice) {
        if (choice == 1) {
            return Category.FOOD_GROCERIES;
        } else if (choice == 2) {
            return Category.UTILITIES;
        } else if (choice == 3) {
            return Category.ENTERTAINMENT;
        } else if (choice == 4) {
            return Category.SUBSCRIPTIONS;
        } else {
            System.out.println("Invalid category choice.");
            return null;
        }
    }
}