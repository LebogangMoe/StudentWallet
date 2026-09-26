package com.studentwallet;

import java.sql.*;

public class DatabaseManager {
    
    private static final String URL = "jdbc:sqlite:C:\\Users\\CurrentUser\\Documents\\NetBeansProjects\\studentWallet\\wallet.db";

    public static Connection connect() throws SQLException {
        try {
            
            Class.forName("org.sqlite.JDBC"); 
        } catch (ClassNotFoundException e) {
            System.out.println("❌ SQLite JDBC Driver not found!");
        }
        return DriverManager.getConnection(URL);
    }
    
    

    
    public static double getTotalBalance() {
        String sql = "SELECT balance_after FROM transactions ORDER BY id DESC LIMIT 1";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble("balance_after");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    
    public static double getMonthlyIncome() {
        String sql = "SELECT SUM(amount) FROM transactions WHERE type = 'Income' AND strftime('%m', date) = strftime('%m', 'now')";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    
    public static double getMonthlyExpenses() {
        String sql = "SELECT SUM(amount) FROM transactions WHERE type = 'Expense' AND strftime('%m', date) = strftime('%m', 'now')";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    
    public static double getTrendData(String type, String month) {
        String sql = "SELECT SUM(amount) FROM transactions WHERE type = ? AND strftime('%m', date) = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            pstmt.setString(2, month);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }
        
    public static void addTransaction(int userId, int catId, String type, double amount, String desc, String date) {
        String sql = "INSERT INTO transactions(user_id, category_id, type, amount, description, date, balance_after) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, catId);
            pstmt.setString(3, type);
            pstmt.setDouble(4, amount);
            pstmt.setString(5, desc);
            pstmt.setString(6, date);
            // Calculate new balance
            pstmt.setDouble(7, getTotalBalance() + (type.equals("Income") ? amount : -amount));
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    
    public static void deleteLastTransaction() {
        String sql = "DELETE FROM transactions WHERE id = (SELECT MAX(id) FROM transactions)";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) { e.printStackTrace(); }
    }
}