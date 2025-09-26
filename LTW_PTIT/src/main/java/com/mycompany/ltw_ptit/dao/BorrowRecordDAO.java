package com.mycompany.ltw_ptit.dao;

import com.mycompany.ltw_ptit.model.BorrowRecord;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class cho BorrowRecord
 */
public class BorrowRecordDAO {
    private DatabaseConnection dbConnection;
    
    public BorrowRecordDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    // Tạo borrow record mới
    public boolean createBorrowRecord(BorrowRecord record) {
        String sql = "INSERT INTO borrow_records (user_id, book_id, borrow_date, due_date, status, fine_amount, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, record.getUserId());
            stmt.setInt(2, record.getBookId());
            stmt.setDate(3, Date.valueOf(record.getBorrowDate()));
            stmt.setDate(4, Date.valueOf(record.getDueDate()));
            stmt.setString(5, record.getStatus());
            stmt.setBigDecimal(6, record.getFineAmount());
            stmt.setString(7, record.getNotes());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Lấy borrow record theo ID
    public BorrowRecord getBorrowRecordById(int id) {
        String sql = "SELECT br.*, u.full_name as user_name, b.title as book_title FROM borrow_records br " +
                    "LEFT JOIN users u ON br.user_id = u.id " +
                    "LEFT JOIN books b ON br.book_id = b.id " +
                    "WHERE br.id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBorrowRecord(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Lấy tất cả borrow records
    public List<BorrowRecord> getAllBorrowRecords() {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT br.*, u.full_name as user_name, b.title as book_title FROM borrow_records br " +
                    "LEFT JOIN users u ON br.user_id = u.id " +
                    "LEFT JOIN books b ON br.book_id = b.id " +
                    "ORDER BY br.created_at DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                records.add(mapResultSetToBorrowRecord(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return records;
    }
    
    // Lấy borrow records theo user
    public List<BorrowRecord> getBorrowRecordsByUser(int userId) {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT br.*, u.full_name as user_name, b.title as book_title FROM borrow_records br " +
                    "LEFT JOIN users u ON br.user_id = u.id " +
                    "LEFT JOIN books b ON br.book_id = b.id " +
                    "WHERE br.user_id = ? ORDER BY br.created_at DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                records.add(mapResultSetToBorrowRecord(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return records;
    }
    
    // Lấy borrow records theo book
    public List<BorrowRecord> getBorrowRecordsByBook(int bookId) {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT br.*, u.full_name as user_name, b.title as book_title FROM borrow_records br " +
                    "LEFT JOIN users u ON br.user_id = u.id " +
                    "LEFT JOIN books b ON br.book_id = b.id " +
                    "WHERE br.book_id = ? ORDER BY br.created_at DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                records.add(mapResultSetToBorrowRecord(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return records;
    }
    
    // Lấy borrow records đang mượn
    public List<BorrowRecord> getActiveBorrowRecords() {
        List<BorrowRecord> records = new ArrayList<>();
        String sql = "SELECT br.*, u.full_name as user_name, b.title as book_title FROM borrow_records br " +
                    "LEFT JOIN users u ON br.user_id = u.id " +
                    "LEFT JOIN books b ON br.book_id = b.id " +
                    "WHERE br.status = 'BORROWED' ORDER BY br.due_date ASC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                records.add(mapResultSetToBorrowRecord(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return records;
    }
    
    // Trả sách
    public boolean returnBook(int recordId, LocalDate returnDate, BigDecimal fineAmount, String notes) {
        String sql = "UPDATE borrow_records SET return_date = ?, status = 'RETURNED', fine_amount = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(returnDate));
            stmt.setBigDecimal(2, fineAmount);
            stmt.setString(3, notes);
            stmt.setInt(4, recordId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cập nhật trạng thái quá hạn
    public boolean updateOverdueStatus() {
        String sql = "UPDATE borrow_records SET status = 'OVERDUE' WHERE status = 'BORROWED' AND due_date < CURDATE()";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected >= 0; // Có thể không có record nào cần cập nhật
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Xóa borrow record
    public boolean deleteBorrowRecord(int id) {
        String sql = "DELETE FROM borrow_records WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Map ResultSet to BorrowRecord object
    private BorrowRecord mapResultSetToBorrowRecord(ResultSet rs) throws SQLException {
        BorrowRecord record = new BorrowRecord();
        record.setId(rs.getInt("id"));
        record.setUserId(rs.getInt("user_id"));
        record.setUserName(rs.getString("user_name"));
        record.setBookId(rs.getInt("book_id"));
        record.setBookTitle(rs.getString("book_title"));
        
        Date borrowDate = rs.getDate("borrow_date");
        if (borrowDate != null) {
            record.setBorrowDate(borrowDate.toLocalDate());
        }
        
        Date dueDate = rs.getDate("due_date");
        if (dueDate != null) {
            record.setDueDate(dueDate.toLocalDate());
        }
        
        Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            record.setReturnDate(returnDate.toLocalDate());
        }
        
        record.setStatus(rs.getString("status"));
        record.setFineAmount(rs.getBigDecimal("fine_amount"));
        record.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            record.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            record.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return record;
    }
}
