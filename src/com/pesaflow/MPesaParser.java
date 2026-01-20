package com.pesaflow;

/**
 * MPesaParser - Extracts transaction data from M-Pesa SMS messages
 * This class takes raw SMS text and converts it into Transaction objects
 */
public class MPesaParser {
    
    /**
     * Parses an M-Pesa SMS message and creates a Transaction object
     * 
     * @param smsMessage The raw SMS text from M-Pesa
     * @return Transaction object with extracted data, or null if parsing fails
     */
    public static Transaction parseSMS(String smsMessage) {
        // Check if the message is null or empty
        if (smsMessage == null || smsMessage.trim().isEmpty()) {
            System.out.println("Error: SMS message is empty");
            return null;
        }
        
        try {
            // Extract transaction ID (e.g., "RBK4H8N2M1")
            String transactionId = extractTransactionId(smsMessage);
            
            // Determine transaction type (sent or received)
            String type = determineTransactionType(smsMessage);
            
            // Extract amount (e.g., 500.00 from "Ksh500.00")
            double amount = extractAmount(smsMessage);
            
            // Extract party name (person/business)
            String party = extractParty(smsMessage, type);
            
            // Extract date and time
            String dateTime = extractDateTime(smsMessage);
            
            // Extract new balance
            double balance = extractBalance(smsMessage);
            
            // Create and return the Transaction object
            return new Transaction(transactionId, type, amount, party, dateTime, balance);
            
        } catch (Exception e) {
            System.out.println("Error parsing SMS: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Extracts the transaction ID from SMS
     * Example: "RBK4H8N2M1 Confirmed..." -> "RBK4H8N2M1"
     */
    private static String extractTransactionId(String sms) {
        // Transaction ID is usually the first word before "Confirmed"
        String[] words = sms.split(" ");
        if (words.length > 0) {
            return words[0];
        }
        return "UNKNOWN";
    }
    
    /**
     * Determines if money was sent or received
     * Checks for keywords: "sent to" means expense, "received from" means income
     */
    private static String determineTransactionType(String sms) {
        String lowerSms = sms.toLowerCase();
        
        if (lowerSms.contains("sent to") || lowerSms.contains("paid to")) {
            return "sent";
        } else if (lowerSms.contains("received from")) {
            return "received";
        } else if (lowerSms.contains("withdraw")) {
            return "sent";  // Withdrawal is money going out
        } else if (lowerSms.contains("deposit")) {
            return "received";  // Deposit is money coming in
        }
        
        return "unknown";
    }
    
    /**
     * Extracts the amount from SMS
     * Example: "Ksh500.00 sent" -> 500.00
     */
    private static double extractAmount(String sms) {
        try {
            // Find "Ksh" and extract the number after it
            int kshIndex = sms.indexOf("Ksh");
            if (kshIndex != -1) {
                // Start after "Ksh"
                String afterKsh = sms.substring(kshIndex + 3);
                
                // Extract numbers and decimal point
                StringBuilder amountStr = new StringBuilder();
                for (char c : afterKsh.toCharArray()) {
                    if (Character.isDigit(c) || c == '.') {
                        amountStr.append(c);
                    } else if (c == ',') {
                        // Skip commas in numbers (e.g., "1,500.00")
                        continue;
                    } else {
                        // Stop at first non-number character
                        break;
                    }
                }
                
                return Double.parseDouble(amountStr.toString());
            }
        } catch (Exception e) {
            System.out.println("Error extracting amount: " + e.getMessage());
        }
        return 0.0;
    }
    
    /**
     * Extracts the party (person/business) from SMS
     * Example: "sent to JOHN DOE on" -> "JOHN DOE"
     */
    private static String extractParty(String sms, String type) {
        try {
            String keyword;
            
            // Choose keyword based on transaction type
            if (type.equals("sent")) {
                if (sms.contains("sent to")) {
                    keyword = "sent to";
                } else if (sms.contains("paid to")) {
                    keyword = "paid to";
                } else {
                    return "UNKNOWN";
                }
            } else if (type.equals("received")) {
                keyword = "received from";
            } else {
                return "UNKNOWN";
            }
            
            // Find the keyword and extract text after it
            int startIndex = sms.indexOf(keyword);
            if (startIndex != -1) {
                startIndex += keyword.length();
                
                // Find where the name ends (usually at "on" or ".")
                int endIndex = sms.indexOf(" on ", startIndex);
                if (endIndex == -1) {
                    endIndex = sms.indexOf(".", startIndex);
                }
                
                if (endIndex != -1) {
                    return sms.substring(startIndex, endIndex).trim();
                }
            }
        } catch (Exception e) {
            System.out.println("Error extracting party: " + e.getMessage());
        }
        return "UNKNOWN";
    }
    
    /**
     * Extracts date and time from SMS
     * Example: "on 15/1/26 at 2:30 PM" -> "15/1/26 2:30 PM"
     */
    private static String extractDateTime(String sms) {
        try {
            // Find "on" keyword
            int onIndex = sms.indexOf(" on ");
            if (onIndex != -1) {
                // Start after "on "
                String afterOn = sms.substring(onIndex + 4);
                
                // Extract until "New M-PESA balance" or end of relevant part
                int endIndex = afterOn.indexOf("New M-PESA");
                if (endIndex == -1) {
                    endIndex = afterOn.indexOf(".");
                }
                
                if (endIndex != -1) {
                    String dateTimePart = afterOn.substring(0, endIndex).trim();
                    // Remove "at" if present
                    dateTimePart = dateTimePart.replace(" at ", " ");
                    return dateTimePart;
                }
            }
        } catch (Exception e) {
            System.out.println("Error extracting date/time: " + e.getMessage());
        }
        return "UNKNOWN";
    }
    
    /**
     * Extracts the new balance from SMS
     * Example: "New M-PESA balance is Ksh1,500.00" -> 1500.00
     */
    private static double extractBalance(String sms) {
        try {
            // Find "balance is Ksh"
            int balanceIndex = sms.indexOf("balance is Ksh");
            if (balanceIndex != -1) {
                // Start after "balance is Ksh"
                String afterBalance = sms.substring(balanceIndex + 14);
                
                // Extract numbers and decimal point
                StringBuilder balanceStr = new StringBuilder();
                for (char c : afterBalance.toCharArray()) {
                    if (Character.isDigit(c) || c == '.') {
                        balanceStr.append(c);
                    } else if (c == ',') {
                        // Skip commas
                        continue;
                    } else {
                        // Stop at first non-number character
                        break;
                    }
                }
                
                return Double.parseDouble(balanceStr.toString());
            }
        } catch (Exception e) {
            System.out.println("Error extracting balance: " + e.getMessage());
        }
        return 0.0;
    }
}