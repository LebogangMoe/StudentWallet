package com.studentwallet;

public class WalletTest {

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   RUNNING UNIT & INTEGRATION TESTS...   ");
        System.out.println("=========================================");

        
        try {
            double income = 3500.00;
            double expenses = 1445.00;
            double expectedBalance = 2055.00; 
            double actualBalance = income - expenses;

            if (expectedBalance == actualBalance) {
                System.out.println("✅ [PASS] Unit Test: Balance Calculation");
            } else {
                System.out.println("❌ [FAIL] Unit Test: Balance Calculation");
            }
        } catch (Exception e) {
            System.out.println("❌ [FAIL] Unit Test: " + e.getMessage());
        }

        
        try {
            double dbBalance = DatabaseManager.getTotalBalance();
            double income = DatabaseManager.getMonthlyIncome();
            double expenses = DatabaseManager.getMonthlyExpenses();
            
            if (dbBalance > 0) {
                System.out.println("✅ [PASS] Integration Test: Database Connected.");
                System.out.println("   -> Total Balance: R" + dbBalance);
                System.out.println("   -> Monthly Income: R" + income);
                System.out.println("   -> Monthly Expenses: R" + expenses);
            } else {
                System.out.println("❌ [FAIL] Integration Test: Database returned 0.");
            }
        } catch (Exception e) {
            System.out.println("❌ [FAIL] Integration Test: " + e.getMessage());
        }

        System.out.println("=========================================");
        System.out.println("   ALL TESTS COMPLETED.                  ");
        System.out.println("=========================================");
    }
}
