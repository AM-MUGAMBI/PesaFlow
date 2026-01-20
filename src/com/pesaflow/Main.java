package com.pesaflow;

/**
 * Main class - Entry point of our application
 * We'll use this to test our Transaction and MPesaParser classes
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("=== PesaFlow - M-Pesa Transaction Parser ===\n");
        
        // Sample M-Pesa SMS messages (examples of what we'll receive)
        String sms1 = "RBK4H8N2M1 Confirmed. Ksh500.00 sent to JOHN DOE on 15/1/26 at 2:30 PM. New M-PESA balance is Ksh1,500.00";
        String sms2 = "SAL9K3P7X4 Confirmed. Ksh1,200.00 received from JANE SMITH on 14/1/26 at 10:15 AM. New M-PESA balance is Ksh2,700.00";
        String sms3 = "TXN5M2K9L1 Confirmed. Ksh300.50 sent to SAFARICOM LTD on 13/1/26 at 6:45 PM. New M-PESA balance is Ksh2,399.50";
        
        // Test 1: Parse first SMS (money sent)
        System.out.println("Test 1 - Parsing sent transaction:");
        System.out.println("Original SMS: " + sms1);
        Transaction transaction1 = MPesaParser.parseSMS(sms1);
        
        if (transaction1 != null) {
         
            if (transaction1.getType().equals("sent")) {
                System.out.println("Transaction Id: " + transaction1.getTransactionId());
                System.out.println("Amount spent: Ksh " + transaction1.getAmount());
                System.out.println("Paid to: " + transaction1.getParty());
                System.out.println("Balance after: Ksh " + transaction1.getBalance());
            }
               if (transaction1.isIncome()) {
                System.out.println("This is income.");
            }
            else if (transaction1.isExpense()) {
                System.out.println("This is an expense.");
            }
          
        } else {
            System.out.println("Failed to parse transaction");
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Test 2: Parse second SMS (money received)
        System.out.println("Test 2 - Parsing received transaction:");
        System.out.println("Original SMS: " + sms2);
        Transaction transaction2 = MPesaParser.parseSMS(sms2);
        
        if (transaction2 != null) {
            
            if 
                (transaction2.getType().equals("received")) {
                    System.out.println("Transaction Id: " + transaction2.getTransactionId());
                 System.out.println("Amount received: Ksh " + transaction2.getAmount());
                 System.out.println("Received from: " + transaction2.getParty());
                 System.out.println("Balance after: Ksh " + transaction2.getBalance());
                }
            if (transaction2.isIncome()) {
                System.out.println("This is income.");
            }
            else if (transaction2.isExpense()) {
                System.out.println("This is an expense.");
            }
        } else {
            System.out.println("Failed to parse transaction");
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Test 3: Parse third SMS (another expense)
        System.out.println("Test 3 - Parsing another expense:");
        System.out.println("Original SMS: " + sms3);
        Transaction transaction3 = MPesaParser.parseSMS(sms3);
        
        if (transaction3 != null) {
        
            if (transaction3.getType().equals("received")) {
                    System.out.println("Transaction Id: " + transaction3.getTransactionId());
                 System.out.println("Amount received: Ksh " + transaction3.getAmount());
                 System.out.println("Received from: " + transaction3.getParty());
                 System.out.println("Balance after: Ksh " + transaction3.getBalance());
                }
            if (transaction3.isIncome()) {
                System.out.println("This is income.");
            }
            else if (transaction3.isExpense()) {
                System.out.println("This is an expense.");
            }

            System.out.println("Amount spent: Ksh " + transaction3.getAmount());
            System.out.println("Paid to: " + transaction3.getParty());
            System.out.println("Balance after: Ksh " + transaction3.getBalance());
        } else {
            System.out.println("Failed to parse transaction");
        }
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Test 4: Calculate total expenses and income
        System.out.println("Summary:");
        double totalExpenses = 0;
        double totalIncome = 0;
        
        if (transaction1 != null && transaction1.isExpense()) {
            totalExpenses += transaction1.getAmount();
        }
        if (transaction2 != null && transaction2.isIncome()) {
            totalIncome += transaction2.getAmount();
        }
        if (transaction3 != null && transaction3.isExpense()) {
            totalExpenses += transaction3.getAmount();
        }
        
        System.out.println("Total Expenses: Ksh " + totalExpenses);
        System.out.println("Total Income: Ksh " + totalIncome);
        System.out.println("Net Change: Ksh " + (totalIncome - totalExpenses));
        
        System.out.println("\n=== Tests Complete ===");
    }
}