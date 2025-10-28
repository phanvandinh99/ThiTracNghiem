/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class formKetQua extends javax.swing.JFrame {
    private String hoTen;
    private String mssv;
    private String soDT;
    private int soDung;
    private int tongCau;
    private float diem;
    public formKetQua(String hoTen, String mssv, String soDT, int soDung, int tongCau, float diem) {
        initComponents();
        setLocationRelativeTo(null);

        // Định dạng ngày thi
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String ngayThi = sdf.format(new Date());

        // Hiển thị dữ liệu
        lblHoTen.setText("Họ và tên: " + hoTen);
        lblMSSV.setText("Mã số sinh viên: " + mssv);
        lblSoDT.setText("Số điện thoại: " + soDT);
        lblNgayThi.setText("Ngày thi: " + ngayThi);
        lblDiem.setText("ĐIỂM CỦA BẠN: " + String.format("%.2f", diem));
        lblSoCauDung.setText("SỐ CÂU ĐÚNG: " + soDung + "/" + tongCau);
    }
    

    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblHoTen = new javax.swing.JLabel();
        lblMSSV = new javax.swing.JLabel();
        lblSoDT = new javax.swing.JLabel();
        lblNgayThi = new javax.swing.JLabel();
        lblDiem = new javax.swing.JLabel();
        lblSoCauDung = new javax.swing.JLabel();
        btnThoat = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("KẾT QUẢ THI");

        lblHoTen.setText("Họ và tên: ");

        lblMSSV.setText("Mã số sinh viên:");

        lblSoDT.setText("Số điện thoại:");

        lblNgayThi.setText("Ngày thi:");

        lblDiem.setText("SỐ CÂU ĐÚNG:");

        lblSoCauDung.setText("ĐIỂM CỦA BẠN:");

        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(179, 179, 179)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMSSV)
                            .addComponent(lblNgayThi)
                            .addComponent(lblHoTen)
                            .addComponent(lblSoDT)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(176, 176, 176)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnThoat, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblSoCauDung)
                                .addComponent(lblDiem)))))
                .addContainerGap(188, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(13, 13, 13)
                .addComponent(lblHoTen)
                .addGap(18, 18, 18)
                .addComponent(lblMSSV)
                .addGap(18, 18, 18)
                .addComponent(lblSoDT)
                .addGap(18, 18, 18)
                .addComponent(lblNgayThi)
                .addGap(11, 11, 11)
                .addComponent(lblDiem)
                .addGap(36, 36, 36)
                .addComponent(lblSoCauDung)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(btnThoat)
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        dispose();
    }//GEN-LAST:event_btnThoatActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(formKetQua.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(formKetQua.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(formKetQua.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(formKetQua.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new formKetQua().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThoat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblDiem;
    private javax.swing.JLabel lblHoTen;
    private javax.swing.JLabel lblMSSV;
    private javax.swing.JLabel lblNgayThi;
    private javax.swing.JLabel lblSoCauDung;
    private javax.swing.JLabel lblSoDT;
    // End of variables declaration//GEN-END:variables
}
