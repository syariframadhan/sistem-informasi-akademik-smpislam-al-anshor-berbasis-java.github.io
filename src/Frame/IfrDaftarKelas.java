/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Tool.KoneksiDB;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author RAMA
 */
public class IfrDaftarKelas extends javax.swing.JInternalFrame {

    
    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    private Connection conn = new KoneksiDB().getConnection();
    PreparedStatement pst = null;
    ResultSet res = null;
    String sqlselect, sqlinsert, sqldelete;
    String vkode_kelas, vnama_kelas, vkapasitas_kelas, vwali_kelas;
    
    
    private DefaultTableModel tblkelas; 
    /**
     * Creates new form IfrDaftarMataPelajaran
     */
    public IfrDaftarKelas() {
        initComponents();
        clearInput();
        setTabelKelas();
        showDataKelas();
        jdInputKelas.setSize(500, 550);
        jdInputKelas.setLocationRelativeTo(this);
    }
        private void clearInput(){
        txtnama_kelas.setText("");
        txtkapasitas_kelas.setText("");        
        btnSimpan.setText("Simpan");
        vkode_kelas="";
        }
        
        private void setTabelKelas(){
        String [] kolom1 = {"Kode Kelas", "Nama Kelas","Kapasitas Kelas","Wali Kelas"};
        tblkelas = new DefaultTableModel(null, kolom1) {
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
            };
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            
            // agar tabel tidak bisa di edit
            public boolean isCellEditable(int row, int col){
                int cola = tblkelas.getColumnCount();
                return (col < cola ) ? false : true;
            }
        };
        tbkelas.setModel(tblkelas);
        tbkelas.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbkelas.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbkelas.getColumnModel().getColumn(2).setPreferredWidth(100);
        tbkelas.getColumnModel().getColumn(2).setPreferredWidth(100);
    }
    
    private void clearTabelSiswa(){
        int row = tblkelas.getRowCount();
        for (int i = 0; i < row; i++){
            tblkelas.removeRow(0);
        }
    }
    
    private void showDataKelas(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelSiswa();
        sqlselect = "select * from tbkelas";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vkode_kelas = res.getString("kode_kelas");
            vnama_kelas = res.getString("nama_kelas");
            vkapasitas_kelas = res.getString("kapasitas_kelas");
            vwali_kelas = res.getString("wali_kelas");
            Object [] data = {vkode_kelas, vnama_kelas, vkapasitas_kelas, vwali_kelas};
            tblkelas.addRow(data);
        }
            lbRecord.setText("Record : " + tbkelas.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method showDataKelas() : " + ex );
         }
        
    }
 
    private void cariNamaKelas(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelSiswa();
        sqlselect = "SELECT * FROM tbkelas where kode_pelajaran like '%"+txtCari.getText()+"%' or nama_kelas like '%"+txtCari.getText()+"%' or wali_kelas like '%"+txtCari.getText()+"%' order by kode_kelas asc";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vkode_kelas = res.getString("kode_kelas");
            vnama_kelas = res.getString("nama_kelas");
            vkapasitas_kelas = res.getString("kapasitas_kelas");
            vwali_kelas = res.getString("wali_kelas");
            Object [] data = {vkode_kelas, vnama_kelas, vkapasitas_kelas, vwali_kelas};
            tblkelas.addRow(data);
        }
            lbRecord.setText("Record : " + tbkelas.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method cariNamaKelas() : " + ex );
         }
    }
    
    
    
    private void getDataKelas(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "SELECT * FROM tbkelas WHERE kode_kelas='"+vkode_kelas+"'";;
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.first()){
                txtkode_kelas.setText(res.getString("kode_kelas"));
                txtnama_kelas.setText(res.getString("nama_kelas"));
                txtkapasitas_kelas.setText(res.getString("kapasitas_kelas"));
                txtwalikelas.setText(res.getString("wali_kelas"));
                }
        } catch (SQLException ex){
            JOptionPane.showMessageDialog(this, "Error method getDataKelas ():" +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void aksiSimpan() {
        vkode_kelas = txtkode_kelas.getText();
        vnama_kelas = txtnama_kelas.getText();
        vkapasitas_kelas = txtkapasitas_kelas.getText();        
        vwali_kelas = txtwalikelas.getText();
        
        if(btnSimpan.getText().equals("Simpan")){
            
            if (vwali_kelas == null ) {
                JOptionPane.showMessageDialog(this, "Harap masukan nama wali kelas ! " );
                jdInputKelas.setVisible(true);
            } else {
                try {
                     _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "INSERT INTO tbkelas values (?,?,?,?)";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vkode_kelas);
                    stat.setString(2, vnama_kelas);
                    stat.setString(3, vkapasitas_kelas);
                    stat.setString(4, vwali_kelas);
                    stat.executeUpdate();
                    clearInput();showDataKelas();
                    jdInputKelas.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);                                  
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan 2 () : " +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE );
                            
               }
            }
            
        } else {
            if (vwali_kelas == null){
                JOptionPane.showMessageDialog(this, "Harap masukan nama wali kelas ! " );
                jdInputKelas.setVisible(true);
            } else {
                try {
                    
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "update tbkelas set nama_kelas = ?, kapasitas_kelas = ? , wali_kelas = ?  where kode_kelas = '"+vkode_kelas+"'";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vnama_kelas);
                    stat.setString(2, vkapasitas_kelas);
                    stat.setString(2, vwali_kelas);
                    stat.executeUpdate();
                    clearInput();
                    showDataKelas();
                    jdInputKelas.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil diubah",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);                      
                    }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan 4 () : " +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE );
                            
               }
            }
        }
        
    }
    
    private void aksiHapus(){
        int jawab = JOptionPane.showConfirmDialog(this, 
                "Apakah anda yakin akan menghapus adata ini ? Nama Pelajaran : " + vnama_kelas,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (jawab == JOptionPane.YES_OPTION){
            try {
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                sqldelete = "DELETE FROM tbkelas "
                        + "WHERE kode_kelas='"+vkode_kelas+"'";
                Statement stat = _Cnn.createStatement();
                stat.executeUpdate(sqldelete);
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                clearInput();
                showDataKelas();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error method aksiHapus() : " +ex,
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jdInputKelas = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        txtkode_kelas = new javax.swing.JTextField();
        txtnama_kelas = new javax.swing.JTextField();
        txtkapasitas_kelas = new javax.swing.JTextField();
        txtwalikelas = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnHapus2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbkelas = new javax.swing.JTable();
        lbRecord = new javax.swing.JLabel();

        jdInputKelas.setTitle("Input Data Kelas");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Form Entry Data Kelas");

        jLabel5.setText("Form ini digunakan untuk menginput data kelas");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Navigasi"));

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/save-black.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/btn_delete.png"))); // NOI18N
        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addComponent(btnSimpan)
                .addGap(18, 18, 18)
                .addComponent(btnBatal)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Input Data"));
        jPanel4.setOpaque(false);

        txtkode_kelas.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Kode Kelas :"));
        txtkode_kelas.setOpaque(false);

        txtnama_kelas.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Kelas :"));
        txtnama_kelas.setOpaque(false);

        txtkapasitas_kelas.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Kapasitas Kelas :"));
        txtkapasitas_kelas.setOpaque(false);

        txtwalikelas.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Wali Kelas :"));
        txtwalikelas.setOpaque(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtkode_kelas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                    .addComponent(txtnama_kelas, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtkapasitas_kelas, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtwalikelas))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtkode_kelas, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(txtnama_kelas, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(txtkapasitas_kelas, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtwalikelas, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/small-very-logo.png"))); // NOI18N

        javax.swing.GroupLayout jdInputKelasLayout = new javax.swing.GroupLayout(jdInputKelas.getContentPane());
        jdInputKelas.getContentPane().setLayout(jdInputKelasLayout);
        jdInputKelasLayout.setHorizontalGroup(
            jdInputKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdInputKelasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdInputKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jdInputKelasLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jdInputKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(0, 171, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jdInputKelasLayout.setVerticalGroup(
            jdInputKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdInputKelasLayout.createSequentialGroup()
                .addGroup(jdInputKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdInputKelasLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setClosable(true);
        setTitle("Form Daftar Kelas\n");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Navigasi"));

        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/trans-hapus.png"))); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/trans-add.png"))); // NOI18N
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Cari Nama");

        btnHapus2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/report-small1.png"))); // NOI18N
        btnHapus2.setText("Cari");
        btnHapus2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapus2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnTambah)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapus2)
                .addGap(53, 53, 53))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnHapus2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tabel Data Kelas : Klik 2x untuk mengubah/meghapus"));

        tbkelas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Kode Kelas", "Nama Kelas", "Kapasitas Kelas", "Wali Kelas"
            }
        ));
        tbkelas.setRowHeight(25);
        tbkelas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbkelasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbkelas);

        lbRecord.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbRecord.setText("Record : 0 ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 794, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(lbRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGap(4, 4, 4)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 543, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(13, 13, 13)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(lbRecord)
                    .addContainerGap(17, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        if (vkode_kelas.equals("")){
            JOptionPane.showMessageDialog(this, "Anda belum memilih data yang akan dihapus",
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            aksiHapus();
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        clearInput();
        jdInputKelas.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
        cariNamaKelas();
    }//GEN-LAST:event_txtCariActionPerformed

    private void btnHapus2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapus2ActionPerformed
        // TODO add your handling code here:
        cariNamaKelas();
    }//GEN-LAST:event_btnHapus2ActionPerformed

    private void tbkelasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbkelasMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount()==1){
            btnHapus.setEnabled(true);
            vkode_kelas = tbkelas.getValueAt(tbkelas.getSelectedRow(),0).toString();
            txtkode_kelas.setText(vkode_kelas);
        } else if (evt.getClickCount()==2 ){
            btnSimpan.setText("Ubah");
            jdInputKelas.setVisible(true);
            jdInputKelas.setLocationRelativeTo(this);
            getDataKelas();
        }
    }//GEN-LAST:event_tbkelasMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if (txtkode_kelas.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Data Kode Kelas harus diisi", "Informasi",
                JOptionPane.INFORMATION_MESSAGE);
        } else if (txtnama_kelas.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Data nama kelas harus diisi",
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            aksiSimpan();
            jdInputKelas.setVisible(false);
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clearInput();
    }//GEN-LAST:event_btnBatalActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnHapus2;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JDialog jdInputKelas;
    private javax.swing.JLabel lbRecord;
    private javax.swing.JTable tbkelas;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtkapasitas_kelas;
    private javax.swing.JTextField txtkode_kelas;
    private javax.swing.JTextField txtnama_kelas;
    private javax.swing.JTextField txtwalikelas;
    // End of variables declaration//GEN-END:variables
}
