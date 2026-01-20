package com.pesaflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TransactionManager - Manages all transactions and performs analysis
 * This class stores transactions and calculates spending patterns
 */
public class TransactionManager {
    
    // ArrayList to store all transactions
    private List<Transaction> transactions;
    
    // Constructor - initializes the transaction list
    public TransactionManager() {
        this.transactions = new ArrayList<>();
    }
    
    /**
     * Add a new transaction to the list
     * @param transaction The transaction to add
     */
    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
            System.out.println("Transaction added: " + transaction.getTransactionId());
        }
    }
    
    /**
     * Add a transaction by parsing an SMS message
     * @param smsMessage The M-Pesa SMS to parse
     */
    public void addTransactionFromSMS(String smsMessage) {
        Transaction transaction = MPesaParser.parseSMS(smsMessage);
        if (transaction != null) {
            addTransaction(transaction);
        } else {
            System.out.println("Failed to parse SMS message");
        }
    }
    
    /**
     * Get all transactions
     * @return List of all transactions
     */
    public List<Transaction> getAllTransactions() {
        return transactions;
    }
    
    /**
     * Get total number of transactions
     * @return Count of transactions
     */
    public int getTransactionCount() {
        return transactions.size();
    }
    
    /**
     * Calculate total expenses (money going out)
     * @return Total amount spent
     */
    public double getTotalExpenses() {
        double total = 0;
        for (Transaction t : transactions) {
            if (t.isExpense()) {
                total += t.getAmount();
            }
        }
        return total;
    }
    
    /**
     * Calculate total income (money coming in)
     * @return Total amount received
     */
    public double getTotalIncome() {
        double total = 0;
        for (Transaction t : transactions) {
            if (t.isIncome()) {
                total += t.getAmount();
            }
        }
        return total;
    }
    
    /**
     * Calculate net change (income - expenses)
     * @return Net change in balance
     */
    public double getNetChange() {
        return getTotalIncome() - getTotalExpenses();
    }
    
    /**
     * Find which account/person you transact with most frequently
     * @return Map of party names to transaction counts
     */
    public Map<String, Integer> getMostFrequentParties() {
        Map<String, Integer> partyCounts = new HashMap<>();
        
        // Count transactions per party
        for (Transaction t : transactions) {
            String party = t.getParty();
            partyCounts.put(party, partyCounts.getOrDefault(party, 0) + 1);
        }
        
        return partyCounts;
    }
    
    /**
     * Find which account you spend money with most
     * @return Map of party names to total amounts spent
     */
    public Map<String, Double> getSpendingByParty() {
        Map<String, Double> partySpending = new HashMap<>();
        
        // Sum expenses per party
        for (Transaction t : transactions) {
            if (t.isExpense()) {
                String party = t.getParty();
                double currentAmount = partySpending.getOrDefault(party, 0.0);
                partySpending.put(party, currentAmount + t.getAmount());
            }
        }
        
        return partySpending;
    }
    
    /**
     * Analyze spending by hour of day
     * This helps identify when you spend money most
     * @return Map of hours to total spending
     */
    public Map<Integer, Double> getSpendingByHour() {
        Map<Integer, Double> hourlySpending = new HashMap<>();
        
        for (Transaction t : transactions) {
            if (t.isExpense()) {
                // Extract hour from dateTime (format: "15/1/26 2:30 PM")
                int hour = extractHour(t.getDateTime());
                if (hour != -1) {
                    double currentAmount = hourlySpending.getOrDefault(hour, 0.0);
                    hourlySpending.put(hour, currentAmount + t.getAmount());
                }
            }
        }
        
        return hourlySpending;
    }
    
    /**
     * Helper method to extract hour from dateTime string
     * @param dateTime The date time string
     * @return Hour (0-23) or -1 if extraction fails
     */
    private int extractHour(String dateTime) {
        try {
            // Format: "15/1/26 2:30 PM"
            String[] parts = dateTime.split(" ");
            if (parts.length >= 3) {
                String time = parts[1];  // "2:30"
                String period = parts[2]; // "PM"
                
                String[] timeParts = time.split(":");
                int hour = Integer.parseInt(timeParts[0]);
                
                // Convert to 24-hour format
                if (period.equalsIgnoreCase("PM") && hour != 12) {
                    hour += 12;
                } else if (period.equalsIgnoreCase("AM") && hour == 12) {
                    hour = 0;
                }
                
                return hour;
            }
        } catch (Exception e) {
            System.out.println("Error extracting hour: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Get transactions within a date range
     * For now, this is a simplified version
     * @param startDate Start date
     * @param endDate End date
     * @return List of transactions in range
     */
    public List<Transaction> getTransactionsByDateRange(String startDate, String endDate) {
        List<Transaction> filtered = new ArrayList<>();
        
        for (Transaction t : transactions) {
            // Simplified: just check if date contains the pattern
            // In production, you'd parse dates properly
            String transDate = t.getDateTime();
            filtered.add(t);  // For now, return all
        }
        
        return filtered;
    }
    
    /**
     * Print a summary of all transactions
     */
    public void printSummary() {
        System.out.println("\n=== TRANSACTION SUMMARY ===");
        System.out.println("Total Transactions: " + getTransactionCount());
        System.out.println("Total Income: Ksh " + String.format("%.2f", getTotalIncome()));
        System.out.println("Total Expenses: Ksh " + String.format("%.2f", getTotalExpenses()));
        System.out.println("Net Change: Ksh " + String.format("%.2f", getNetChange()));
        
        // Most frequent parties
        System.out.println("\n--- Most Frequent Parties ---");
        Map<String, Integer> frequencies = getMostFrequentParties();
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " transactions");
        }
        
        // Spending by party
        System.out.println("\n--- Spending by Party ---");
        Map<String, Double> spending = getSpendingByParty();
        for (Map.Entry<String, Double> entry : spending.entrySet()) {
            System.out.println(entry.getKey() + ": Ksh " + String.format("%.2f", entry.getValue()));
        }
        
        // Spending by hour
        System.out.println("\n--- Spending by Hour ---");
        Map<Integer, Double> hourly = getSpendingByHour();
        for (Map.Entry<Integer, Double> entry : hourly.entrySet()) {
            System.out.println(entry.getKey() + ":00 - Ksh " + String.format("%.2f", entry.getValue()));
        }
        
        System.out.println("=".repeat(40));
    }
    
    /**
     * Print all transactions
     */
    public void printAllTransactions() {
        System.out.println("\n=== ALL TRANSACTIONS ===");
        for (int i = 0; i < transactions.size(); i++) {
            System.out.println((i + 1) + ". " + transactions.get(i));
        }
        System.out.println("=".repeat(40));
    }
}