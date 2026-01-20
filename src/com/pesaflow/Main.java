package com.pesaflow;

/**
 * Main class - Entry point of our application
 * Testing TransactionManager with multiple transactions
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=== PesaFlow - M-Pesa Transaction Tracker ===\n");
        
        // Create a TransactionManager
        TransactionManager manager = new TransactionManager();
        
        // Sample M-Pesa SMS messages
        String[] smsMessages = {
            "RBK4H8N2M1 Confirmed. Ksh500.00 sent to JOHN DOE on 15/1/26 at 2:30 PM. New M-PESA balance is Ksh5,000.00",
            "SAL9K3P7X4 Confirmed. Ksh1,200.00 received from JANE SMITH on 15/1/26 at 10:15 AM. New M-PESA balance is Ksh6,200.00",
            "TXN5M2K9L1 Confirmed. Ksh300.50 sent to SAFARICOM LTD on 15/1/26 at 6:45 PM. New M-PESA balance is Ksh5,899.50",
            "MKL2P8R3V7 Confirmed. Ksh250.00 sent to JOHN DOE on 14/1/26 at 1:20 PM. New M-PESA balance is Ksh5,649.50",
            "PLW9X4N6M2 Confirmed. Ksh800.00 sent to NAIVAS SUPERMARKET on 14/1/26 at 5:30 PM. New M-PESA balance is Ksh4,849.50",
            "QRT7Y2K5J9 Confirmed. Ksh2,500.00 received from SALARY DEPOSIT on 13/1/26 at 9:00 AM. New M-PESA balance is Ksh7,349.50",
            "WXY3L6P8N4 Confirmed. Ksh150.00 sent to SAFARICOM LTD on 13/1/26 at 3:15 PM. New M-PESA balance is Ksh7,199.50",
            "ZQM8R4T2V1 Confirmed. Ksh600.00 sent to NAIVAS SUPERMARKET on 12/1/26 at 7:00 PM. New M-PESA balance is Ksh6,599.50"
        };
        
        // Add all SMS messages to manager
        System.out.println("--- Adding Transactions ---");
        for (String sms : smsMessages) {
            manager.addTransactionFromSMS(sms);
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Print all transactions
        manager.printAllTransactions();
        
        // Print summary with analysis
        manager.printSummary();
        
        // Custom analysis
        System.out.println("\n=== CUSTOM ANALYSIS ===");
        
        // Find peak spending hour
        var hourlySpending = manager.getSpendingByHour();
        int peakHour = -1;
        double maxSpending = 0;
        
        for (var entry : hourlySpending.entrySet()) {
            if (entry.getValue() > maxSpending) {
                maxSpending = entry.getValue();
                peakHour = entry.getKey();
            }
        }
        
        if (peakHour != -1) {
            String timeOfDay;
            if (peakHour < 12) {
                timeOfDay = peakHour + ":00 AM (Morning)";
            } else if (peakHour == 12) {
                timeOfDay = "12:00 PM (Noon)";
            } else {
                timeOfDay = (peakHour - 12) + ":00 PM (Afternoon/Evening)";
            }
            
            System.out.println("Peak Spending Time: " + timeOfDay);
            System.out.println("Amount Spent: Ksh " + String.format("%.2f", maxSpending));
        }
        
        // Find party you spend most with
        var spendingByParty = manager.getSpendingByParty();
        String topParty = "";
        double topAmount = 0;
        
        for (var entry : spendingByParty.entrySet()) {
            if (entry.getValue() > topAmount) {
                topAmount = entry.getValue();
                topParty = entry.getKey();
            }
        }
        
        if (!topParty.isEmpty()) {
            System.out.println("\nYou spend most with: " + topParty);
            System.out.println("Total spent: Ksh " + String.format("%.2f", topAmount));
        }
        
        System.out.println("\n=== Analysis Complete ===");
    }
}