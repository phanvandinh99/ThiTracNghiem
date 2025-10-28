/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import server.DBAccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
/**
 *
 * @author Admin        
 */
public class formThi extends javax.swing.JFrame {
    
    private int thoiGianConLai = 30 * 60; 
    private javax.swing.Timer timer;
    private ArrayList<CauHoi> danhSachCauHoi = new ArrayList<>();
    private int chiSoHienTai = 0;
    private int soCauDung = 0;
    private ButtonGroup groupDapAn;
    // thêm vào phần biến thành viên
    private ArrayList<String> dsDapAnChon = new ArrayList<>(); // lưu đáp án user
    private String mssv; 
    private String hoTen ;
    private String soDT ;
    private String ngayThi;
    private String thoiGianThi;

    
    
    /**
     * Creates new form formThi
     */
        // Constructor chính khi mở formThi sau đăng nhập
    public formThi(String hoTen, String mssv, String soDT,String ngayThi,String thoiGianThi) {
        initComponents();
        pnlDanhSachCauHoi.setLayout(new java.awt.GridLayout(5, 5, 5, 5)); // Nếu sai thì xóa dòng này
        setLocationRelativeTo(null);
        this.hoTen = hoTen;
        this.mssv = mssv;
        this.soDT = soDT;
        this.ngayThi=ngayThi;
        this.thoiGianThi=thoiGianThi;
        System.out.println(">>> formThi nhận MSSV: " + mssv);
        // Khởi động đếm ngược
        timer = new javax.swing.Timer(1000, e -> {
            thoiGianConLai--;
            int phut = thoiGianConLai / 60;
            int giay = thoiGianConLai % 60;

            // Cập nhật label
            lblThoiGian.setText(String.format("Thời gian còn lại: %02d:%02d", phut, giay));

            // Khi hết giờ
            if (thoiGianConLai <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Hết giờ làm bài! Hệ thống sẽ tự động nộp bài.");
                btnSubmitActionPerformed(null); // tự động nộp bài
            }
        });
        timer.start();
        groupDapAn = new ButtonGroup();
        groupDapAn.add(rdoA);
        groupDapAn.add(rdoB);
        groupDapAn.add(rdoC);
        groupDapAn.add(rdoD);
//        lblThoiGian = new javax.swing.JLabel();
//        lblThoiGian.setFont(new java.awt.Font("Segoe UI", 1, 16));
//        lblThoiGian.setForeground(new java.awt.Color(255, 0, 0));
//        lblThoiGian.setText("Thời gian còn lại: 30:00");
        add(lblThoiGian);

        loadCauHoiTuDB();
        taoDanhSachNutCauHoi();
        



        // khởi tạo danh sách chọn với kích thước = số câu => ban đầu rỗng
        dsDapAnChon.clear();
        for (int i = 0; i < danhSachCauHoi.size(); i++) {
            dsDapAnChon.add(""); // chỗ để lưu "A"/"B"/"C"/"D"
        }

        if (!danhSachCauHoi.isEmpty()) {
            hienThiCauHoi(0);
        } else {
            JOptionPane.showMessageDialog(this, "Không có câu hỏi nào trong CSDL!");
        }
    }
    


   
     private String getSelectedOption() {
        if (rdoA.isSelected()) return "A";
        if (rdoB.isSelected()) return "B";
        if (rdoC.isSelected()) return "C";
        if (rdoD.isSelected()) return "D";
        return null;
    }
   /*  private void nopBaiTuDong() {
    // Lưu đáp án cuối cùng nếu có chọn
        String selNow = getSelectedOption();
        if (selNow != null && !selNow.isEmpty()) {
            dsDapAnChon.set(chiSoHienTai, selNow);
        }

        // Tự động chấm điểm
        int soDung = 0;
        for (int i = 0; i < danhSachCauHoi.size(); i++) {
            String chon = dsDapAnChon.get(i);
            String dung = danhSachCauHoi.get(i).getDapAnDung();
            if (chon != null && dung != null && chon.equalsIgnoreCase(dung)) {
                soDung++;
            }
        }

        int tongCau = danhSachCauHoi.size();
        float diem = (float) soDung / tongCau * 10;

        // Lưu kết quả vào DB
        try {
            DBAccess db = new DBAccess();
            String sql = "INSERT INTO ketqua (mssv, soCauDung, tongCau, diem) VALUES ('"
                    + mssvDangNhap + "', " + soDung + ", " + tongCau + ", " + diem + ")";
            db.Update(sql);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu kết quả: " + ex.getMessage());
        }

        // Mở form kết quả
        formKetQua f = new formKetQua(mssvDangNhap, hoTen, soDT, soDung, tongCau, diem);
        f.setVisible(true);
        this.dispose();
    }

*/
    private void loadCauHoiTuDB() {
        try {
            DBAccess db = new DBAccess();
            ResultSet rs = db.Query("SELECT * FROM cauhoi");
            while (rs.next()) {
                CauHoi ch = new CauHoi(
                    rs.getInt("id"),
                    rs.getString("noidung"),
                    rs.getString("dapan_a"),
                    rs.getString("dapan_b"),
                    rs.getString("dapan_c"),
                    rs.getString("dapan_d"),
                    rs.getString("dapandung")
                );
                danhSachCauHoi.add(ch);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải câu hỏi: " + e.getMessage());
        }
    }

    private void hienThiCauHoi(int index) {
        if (index >= 0 && index < danhSachCauHoi.size()) {
            CauHoi ch = danhSachCauHoi.get(index);
            lblCauHoi.setText("Câu " + (index + 1) + ": " + ch.getNoiDung());
            rdoA.setText("A. " + ch.getA());
            rdoB.setText("B. " + ch.getB());
            rdoC.setText("C. " + ch.getC());
            rdoD.setText("D. " + ch.getD());

            // hiển thị lựa chọn đã lưu (nếu có)
            groupDapAn.clearSelection();
            String saved = dsDapAnChon.get(index);
            if (saved != null) {
                if (saved.equalsIgnoreCase("A")) rdoA.setSelected(true);
                else if (saved.equalsIgnoreCase("B")) rdoB.setSelected(true);
                else if (saved.equalsIgnoreCase("C")) rdoC.setSelected(true);
                else if (saved.equalsIgnoreCase("D")) rdoD.setSelected(true);
            }
        }
    }
   
    

    private void nextQuestion() {
        // Kiểm tra đáp án trước khi chuyển câu
        String selected = null;
        if (rdoA.isSelected()) selected = "A";
        else if (rdoB.isSelected()) selected = "B";
        else if (rdoC.isSelected()) selected = "C";
        else if (rdoD.isSelected()) selected = "D";

        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 đáp án!");
            return;
        }

        CauHoi ch = danhSachCauHoi.get(chiSoHienTai);
        if (selected.equalsIgnoreCase(ch.getDapAnDung())) {
            soCauDung++;
        }

        chiSoHienTai++;
        if (chiSoHienTai < danhSachCauHoi.size()) {
            hienThiCauHoi(chiSoHienTai);
        } else {
            hienThiKetQua();
            
        }
    }

    private void hienThiKetQua() {
        JOptionPane.showMessageDialog(this, 
                "Bài thi hoàn thành!\nSố câu đúng: " + soCauDung + "/" + danhSachCauHoi.size(),
                "Kết quả", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        buttonGroup7 = new javax.swing.ButtonGroup();
        buttonGroup8 = new javax.swing.ButtonGroup();
        buttonGroup9 = new javax.swing.ButtonGroup();
        buttonGroup10 = new javax.swing.ButtonGroup();
        buttonGroup11 = new javax.swing.ButtonGroup();
        buttonGroup12 = new javax.swing.ButtonGroup();
        buttonGroup13 = new javax.swing.ButtonGroup();
        buttonGroup14 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        lblCauHoi = new javax.swing.JLabel();
        rdoA = new javax.swing.JRadioButton();
        btnNext = new javax.swing.JButton();
        btnSubmit = new javax.swing.JButton();
        rdoB = new javax.swing.JRadioButton();
        rdoC = new javax.swing.JRadioButton();
        rdoD = new javax.swing.JRadioButton();
        btnPrev = new javax.swing.JButton();
        lblThoiGian = new javax.swing.JLabel();
        pnlDanhSachCauHoi = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("Thi Trắc Nghiệm");

        lblCauHoi.setText("Câu hỏi");

        rdoA.setText("A");
        rdoA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAActionPerformed(evt);
            }
        });

        btnNext.setText("Sau");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnSubmit.setText("NỘP");
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });

        rdoB.setText("B");
        rdoB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBActionPerformed(evt);
            }
        });

        rdoC.setText("C");

        rdoD.setText("D");

        btnPrev.setText("Trước");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        lblThoiGian.setForeground(new java.awt.Color(255, 0, 0));
        lblThoiGian.setText("Thời gian còn lại: 30:00");

        pnlDanhSachCauHoi.setLayout(new java.awt.GridLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(lblThoiGian)
                        .addGap(359, 359, 359)
                        .addComponent(jLabel1)
                        .addGap(181, 181, 181))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(152, 152, 152)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rdoA)
                                    .addComponent(lblCauHoi)
                                    .addComponent(rdoB)
                                    .addComponent(rdoC)
                                    .addComponent(rdoD)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(88, 88, 88)
                                .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(395, 395, 395)
                                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlDanhSachCauHoi, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(202, 202, 202))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(36, Short.MAX_VALUE)
                        .addComponent(pnlDanhSachCauHoi, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(172, 172, 172))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblThoiGian)))
                        .addGap(39, 39, 39)
                        .addComponent(lblCauHoi)
                        .addGap(63, 63, 63)
                        .addComponent(rdoA)
                        .addGap(80, 80, 80)
                        .addComponent(rdoB)
                        .addGap(69, 69, 69)
                        .addComponent(rdoC)
                        .addGap(78, 78, 78)
                        .addComponent(rdoD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSubmit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnNext, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                        .addComponent(btnPrev, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
            // Lưu đáp án hiện tại (nếu có chọn)
       String selected = getSelectedOption();

       // Nếu chưa chọn thì lưu chuỗi rỗng để tránh lỗi null
       if (selected == null) {
           selected = "";
       }
       dsDapAnChon.set(chiSoHienTai, selected);
       capNhatMauNutCauHoi(chiSoHienTai);


       // Nếu chưa phải câu cuối -> tăng chỉ số và hiển thị câu kế
       if (chiSoHienTai < danhSachCauHoi.size() - 1) {
           chiSoHienTai++;
           hienThiCauHoi(chiSoHienTai);
       } else {
           JOptionPane.showMessageDialog(this, "Bạn đã ở câu cuối. Nhấn NỘP để hoàn tất bài thi.");
       }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
                                        
        if (timer != null) {
            timer.stop();   
        }

        String selNow = getSelectedOption();
        if (selNow != null && !selNow.isEmpty()) {
            dsDapAnChon.set(chiSoHienTai, selNow);
        }

        
        for (int i = 0; i < dsDapAnChon.size(); i++) {
            String a = dsDapAnChon.get(i);
            if (a == null || a.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Bạn chưa chọn đáp án cho câu " + (i + 1) + ". Vui lòng hoàn thành trước khi nộp!");
                chiSoHienTai = i; // Quay lại câu chưa chọn
                hienThiCauHoi(chiSoHienTai);
                return;
            }
        }

       
        int soDung = 0;
        for (int i = 0; i < danhSachCauHoi.size(); i++) {
            String chon = dsDapAnChon.get(i);
            String dung = danhSachCauHoi.get(i).getDapAnDung();
            if (chon != null && dung != null && chon.equalsIgnoreCase(dung)) {
                soDung++;
            }
        }

        int tongCau = danhSachCauHoi.size();
        float diem = (float) soDung / tongCau * 10f; // Thang điểm 10

        
        try {
    DBAccess db = new DBAccess();
    
    String sql = String.format(
    "INSERT INTO ketqua (username, mssv, soCauDung, tongCau, diem, ngayThi, thoigianthi) " +
    "VALUES ('%s', '%s', %d, %d, %.2f, NOW(), NOW())",
    hoTen, mssv, soDung, tongCau, diem
    );
    System.out.println(">>> SQL: " + sql);
    System.out.println(">>> MSSV: " + mssv);
    System.out.println(">>> SQL: " + sql); 
    int rows = db.Update(sql);
    System.out.println(">>> db.Update returned: " + rows);
    if (rows > 0) {
    formKetQua frm = new formKetQua(hoTen, mssv, soDT, soDung, tongCau, diem);
    frm.setVisible(true);
    this.dispose();
    }
} catch (Exception e) {
    e.printStackTrace(); // in stacktrace để xem lỗi chi tiết trong Output
    JOptionPane.showMessageDialog(this, " Lỗi khi lưu kết quả: " + e.getMessage());
    return;
}

        
        formKetQua frm = new formKetQua(hoTen, mssv, soDT, soDung, tongCau, diem);
        frm.setVisible(true);

        
        this.dispose();
    


    }//GEN-LAST:event_btnSubmitActionPerformed

    private void rdoAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoAActionPerformed

    private void rdoBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoBActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // lưu đáp án hiện tại nếu đã chọn
    String sel = getSelectedOption();
    if (sel != null) {
        dsDapAnChon.set(chiSoHienTai, sel);
    }
    if (chiSoHienTai > 0) {
        chiSoHienTai--;
        hienThiCauHoi(chiSoHienTai);
    } else {
        JOptionPane.showMessageDialog(this, "Đây là câu đầu tiên!");
    }
    }//GEN-LAST:event_btnPrevActionPerformed
private ArrayList<JButton> dsNutCauHoi = new ArrayList<>();

    private void taoDanhSachNutCauHoi() {
        pnlDanhSachCauHoi.removeAll();
        dsNutCauHoi.clear();

        for (int i = 0; i < danhSachCauHoi.size(); i++) {
            JButton btn = new JButton(String.valueOf(i + 1));
            btn.setBackground(java.awt.Color.RED);  // mặc định đỏ (chưa làm)
            btn.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12));
            btn.setForeground(java.awt.Color.WHITE);
            final int index = i;
            btn.addActionListener(e -> {
                chiSoHienTai = index;
                hienThiCauHoi(index);
            });
            dsNutCauHoi.add(btn);
            pnlDanhSachCauHoi.add(btn);
        }

        pnlDanhSachCauHoi.revalidate();
        pnlDanhSachCauHoi.repaint();
    }
    private void capNhatMauNutCauHoi(int index) {
        if (index >= 0 && index < dsDapAnChon.size()) {
            String da = dsDapAnChon.get(index);
            JButton btn = dsNutCauHoi.get(index);
            if (da == null || da.isEmpty()) {
                btn.setBackground(java.awt.Color.RED);  // chưa làm
            } else {
                btn.setBackground(java.awt.Color.GREEN); // đã làm
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(formThi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(formThi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(formThi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formThi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formThongTin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSubmit;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup10;
    private javax.swing.ButtonGroup buttonGroup11;
    private javax.swing.ButtonGroup buttonGroup12;
    private javax.swing.ButtonGroup buttonGroup13;
    private javax.swing.ButtonGroup buttonGroup14;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.ButtonGroup buttonGroup7;
    private javax.swing.ButtonGroup buttonGroup8;
    private javax.swing.ButtonGroup buttonGroup9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblCauHoi;
    private javax.swing.JLabel lblThoiGian;
    private javax.swing.JPanel pnlDanhSachCauHoi;
    private javax.swing.JRadioButton rdoA;
    private javax.swing.JRadioButton rdoB;
    private javax.swing.JRadioButton rdoC;
    private javax.swing.JRadioButton rdoD;
    // End of variables declaration//GEN-END:variables
}
