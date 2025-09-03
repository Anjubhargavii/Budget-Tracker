package com.bud;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BudgetTracker {
    private Map<String, Double> categories;
    private List<Transaction> transactions;
    private double totalBudget;
    
    public BudgetTracker() {
        this.categories = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.totalBudget = 0.0;
    }
    
    public void setBudget(String category, double amount) {
        categories.put(category, amount);
        totalBudget += amount;
    }
    
    public void addTransaction(String description, double amount, String category, LocalDate date) {
        Transaction transaction = new Transaction(description, amount, category, date);
        transactions.add(transaction);
    }
    
    public double getCategorySpending(String category) {
        double total = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getCategory().equals(category)) {
                total += transaction.getAmount();
            }
        }
        return total;
    }
    
    public double getTotalSpending() {
        double total = 0.0;
        for (Transaction transaction : transactions) {
            total += transaction.getAmount();
        }
        return total;
    }
    
    public double getRemainingBudget() {
        return totalBudget - getTotalSpending();
    }
    
    public void printBudgetReport() {
        System.out.println("=== BUDGET REPORT ===");
        System.out.println("Total Budget: $" + String.format("%.2f", totalBudget));
        System.out.println("Total Spent: $" + String.format("%.2f", getTotalSpending()));
        System.out.println("Remaining: $" + String.format("%.2f", getRemainingBudget()));
        System.out.println("\nCategory Breakdown:");
        
        for (Map.Entry<String, Double> entry : categories.entrySet()) {
            String category = entry.getKey();
            double budget = entry.getValue();
            double spent = getCategorySpending(category);
            double remaining = budget - spent;
            
            System.out.println(category + ":");
            System.out.println("  Budget: $" + String.format("%.2f", budget));
            System.out.println("  Spent: $" + String.format("%.2f", spent));
            System.out.println("  Remaining: $" + String.format("%.2f", remaining));
        }
    }
    
    public void printRecentTransactions(int count) {
        System.out.println("\n=== RECENT TRANSACTIONS ===");
        
        // Create a copy of transactions and sort by date (newest first)
        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        Collections.sort(sortedTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                return t2.getDate().compareTo(t1.getDate());
            }
        });
        
        // Print the specified number of transactions
        int limit = Math.min(count, sortedTransactions.size());
        for (int i = 0; i < limit; i++) {
            System.out.println(sortedTransactions.get(i));
        }
    }
    
    public static void main(String[] args) {
        BudgetTracker tracker = new BudgetTracker();
        Scanner scanner = new Scanner(System.in);
        
        // Set up budget categories
        tracker.setBudget("Food", 500.0);
        tracker.setBudget("Transportation", 200.0);
        tracker.setBudget("Entertainment", 150.0);
        tracker.setBudget("Utilities", 300.0);
        
        // Add some sample transactions
        tracker.addTransaction("Grocery shopping", 75.50, "Food", LocalDate.now().minusDays(2));
        tracker.addTransaction("Gas station", 45.00, "Transportation", LocalDate.now().minusDays(1));
        tracker.addTransaction("Movie tickets", 25.00, "Entertainment", LocalDate.now());
        tracker.addTransaction("Electric bill", 120.00, "Utilities", LocalDate.now().minusDays(5));
        
        while (true) {
            System.out.println("\n=== BUDGET TRACKER MENU ===");
            System.out.println("1. Add Transaction");
            System.out.println("2. View Budget Report");
            System.out.println("3. View Recent Transactions");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    tracker.addTransaction(description, amount, category, LocalDate.now());
                    System.out.println("Transaction added successfully!");
                    break;
                    
                case 2:
                    tracker.printBudgetReport();
                    break;
                    
                case 3:
                    System.out.print("How many recent transactions to show? ");
                    int count = scanner.nextInt();
                    tracker.printRecentTransactions(count);
                    break;
                    
                case 4:
                    System.out.println("Goodbye!");
                    scanner.close();
                    System.exit(0);
                    
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}

class Transaction {
    private String description;
    private double amount;
    private String category;
    private LocalDate date;
    
    public Transaction(String description, double amount, String category, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }
    
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("%s | %s | $%.2f | %s", 
                date.format(formatter), category, amount, description);
    }
}
