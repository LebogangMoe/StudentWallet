package com.studentwallet;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("--- Testing Database Connection ---");
        
        
        try {
            DatabaseManager.connect();
            System.out.println("✅ Connected to SQLite successfully!");
        } catch (Exception e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }

        
        double balance = DatabaseManager.getTotalBalance();
        double income = DatabaseManager.getMonthlyIncome();
        
        System.out.println("Current Balance: R" + balance);
        System.out.println("Monthly Income: R" + income);
    }
}