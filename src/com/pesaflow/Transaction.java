package com.pesaflow;

/**
 * Transaction class represents a single M-Pesa transaction
 * This could be money received or money sent
 */
public class Transaction {
    
    // Instance variables - these store data for each transaction
    private String transactionId;    // Unique ID from M-Pesa (e.g., "RBK4H8N2M1")
    private String type;              // Type: "received" or "sent"
    private double amount;            // Amount of money
    private String party;             // Person/business you transacted with
    private String dateTime;          // When the transaction happened
    private double balance;           // Your balance after transaction
    
    // Constructor - this is called when we create a new Transaction object
    // It initializes all the variables
    public Transaction(String transactionId, String type, double amount, 
                      String party, String dateTime, double balance) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.party = party;
        this.dateTime = dateTime;
        this.balance = balance;
    }
    
    // Getter methods - these allow us to READ the private variables
    // We use getters to protect our data and control access
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public String getType() {
        return type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getParty() {
        return party;
    }
    
    public String getDateTime() {
        return dateTime;
    }
    
    public double getBalance() {
        return balance;
    }
    
    // toString method - this is called when we print a Transaction object
    // It makes our output readable instead of showing memory addresses
    @Override
    public String toString() {
        return String.format(
            "Transaction[ID=%s, Type=%s, Amount=Ksh %.2f, Party=%s, Date=%s, Balance=Ksh %.2f]",
            transactionId, type, amount, party, dateTime, balance
        );
    }
    
    // Helper method to check if this is an expense (money going out)
    public boolean isExpense() {
        return type.equalsIgnoreCase("sent");
    }
    
    // Helper method to check if this is income (money coming in)
    public boolean isIncome() {
        return type.equalsIgnoreCase("received");
    }
}
