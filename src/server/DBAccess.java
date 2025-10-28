package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;


public class DBAccess {
    private Connection con;
    private Statement stmt;

    public DBAccess() {
        try {
            DBConnection mycon = new DBConnection();
            con = mycon.getConnection();
            if (con == null) {
                System.err.println("DBAccess: getConnection() trả về null!");
            } else {
                stmt = con.createStatement();
            }
        } catch (Exception e) {
            System.err.println("DBAccess constructor lỗi:");
            e.printStackTrace();
            con = null;
            stmt = null;
        }
    }

    // helper: đảm bảo kết nối và statement sẵn sàng
    private boolean ensureConnection() {
        try {
            if (con == null || con.isClosed()) {
                System.out.println("DBAccess: connection null/closed -> thử reconnect...");
                DBConnection mycon = new DBConnection();
                con = mycon.getConnection();
                if (con != null) {
                    stmt = con.createStatement();
                } else {
                    System.err.println("DBAccess: không thể reconnect (getConnection() trả về null)");
                    return false;
                }
            }
            if (stmt == null) {
                stmt = con.createStatement();
            }
            return true;
        } catch (SQLException ex) {
            System.err.println("DBAccess.ensureConnection lỗi:");
            ex.printStackTrace();
            return false;
        } catch (Exception ex) {
            System.err.println("DBAccess.ensureConnection lỗi (khác):");
            ex.printStackTrace();
            return false;
        }
    }

    public int Update(String str) {
        if (!ensureConnection()) {
            System.err.println("DBAccess.Update: không có kết nối để thực hiện SQL.");
            return -1;
        }

        try {
            System.out.println("DBAccess executing SQL (Update): " + str);
            int i = stmt.executeUpdate(str);
            System.out.println("DBAccess: rows affected = " + i);
            return i;
        } catch (SQLException e) {
            System.err.println("DBAccess.SQL exception khi thực thi Update:");
            e.printStackTrace();
            return -1;
        } catch (Exception e) {
            System.err.println("DBAccess.exception khi thực thi Update:");
            e.printStackTrace();
            return -1;
        }
    }

    public ResultSet Query(String srt) {
        if (!ensureConnection()) {
            System.err.println("DBAccess.Query: không có kết nối để thực hiện SQL.");
            return null;
        }

        try {
            System.out.println("DBAccess executing SQL (Query): " + srt);
            ResultSet rs = stmt.executeQuery(srt);
            return rs;
        } catch (SQLException e) {
            System.err.println("DBAccess.SQL exception khi thực thi Query:");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("DBAccess.exception khi thực thi Query:");
            e.printStackTrace();
            return null;
        }
    }

    
    public static void main(String[] args) {
        DBAccess db = new DBAccess();
        if (db == null) {
            System.err.println("Tạo DBAccess trả về null");
            return;
        }

        try {
            //test
            String testSql = "INSERT INTO ketqua (mssv, socaudung, tongcau, diem) VALUES ('TEST001', 1, 1, 1.0)";
            System.out.println("Test SQL: " + testSql);
            int r = db.Update(testSql);
            System.out.println("Test result rows = " + r);
        } catch (Exception ex) {
            System.err.println("Lỗi khi test:");
            ex.printStackTrace();
        }
    }
}
