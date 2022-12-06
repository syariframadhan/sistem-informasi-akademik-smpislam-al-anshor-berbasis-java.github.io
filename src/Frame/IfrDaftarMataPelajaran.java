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
public class IfrDaftarMataPelajaran extends javax.swing.JInternalFrame {

    
    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    private Connection conn = new KoneksiDB().getConnection();
    PreparedStatement pst = null;
    ResultSet res = null;
    String sqlselect, sqlinsert, sqldelete;
    String vkode_pelajaran, vnama_pelajaran, vKKM;
    
    
    private DefaultTableModel tblpelajaran; 
    /**
     * Creates new form IfrDaftarMataPelajaran
     */
    public IfrDaftarMataPelajaran() {
        initComponents();
        clearInput();
        setTabelPelajaran();
        showDataPelajaran();
        jdInputPelajaran.setSize(450, 450);
        jdInputPelajaran.setLocationRelativeTo(this);
    }
        private void clearInput(){
        txtnama_pelajaran.setText("");
        txtKKM.setText("");        
        btnSimpan.setText("Simpan");
        vkode_pelajaran="";
        }
        
        private void setTabelPelajaran(){
        String [] kolom1 = {"Kode Pelajaran", "Nama Pelajaran","KKM"};
        tblpelajaran = new DefaultTableModel(null, kolom1) {
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
            };
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            
            // agar tabel tidak bisa di edit
            public boolean isCellEditable(int row, int col){
                int cola = tblpelajaran.getColumnCount();
                return (col < cola ) ? false : true;
            }
        };
        tbmatapelajaran.setModel(tblpelajaran);
        tbmatapelajaran.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbmatapelajaran.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbmatapelajaran.getColumnModel().getColumn(2).setPreferredWidth(100);
    }
    
    private void clearTabelSiswa(){
        int row = tblpelajaran.getRowCount();
        for (int i = 0; i < row; i++){
            tblpelajaran.removeRow(0);
        }
    }
    
    private void showDataPelajaran(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelSiswa();
        sqlselect = "select * from tbpelajaran;";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vkode_pelajaran = res.getString("kode_pelajaran");
            vnama_pelajaran = res.getString("nama_pelajaran");
            vKKM = res.getString("kkm");
            Object [] data = {vkode_pelajaran, vnama_pelajaran, vKKM};
            tblpelajaran.addRow(data);
        }
            lbRecord.setText("Record : " + tbmatapelajaran.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method showDataPelajaran() : " + ex );
         }
        
    }
 
    private void cariNamaPelajaran(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelSiswa();
        sqlselect = "SELECT * FROM tbpelajaran where kode_pelajaran like '%"+txtCari.getText()+"%' or nama_pelajaran like '%"+txtCari.getText()+"%' order by kode_pelajaran asc";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vkode_pelajaran = res.getString("kode_pelajaran");
            vnama_pelajaran = res.getString("nama_pelajaran");
            vKKM = res.getString("KKM");
            Object [] data = {vkode_pelajaran, vnama_pelajaran, vKKM};
            tblpelajaran.addRow(data);
        }
            lbRecord.setText("Record : " + tbmatapelajaran.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method cariNamaPelajaran() : " + ex );
         }
    }
    
    
    
    private void getDataMataPelajaran(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "SELECT * FROM tbpelajaran WHERE kode_pelajaran='"+vkode_pelajaran+"'";;
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.first()){
                txtkode_pelajaran.setText(res.getString("kode_pelajaran"));
                txtnama_pelajaran.setText(res.getString("nama_pelajaran"));
                txtKKM.setText(res.getString("KKM"));
                }
        } catch (SQLException ex){
            JOptionPane.showMessageDialog(this, "Error method getDataMataPelajaran ():" +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void aksiSimpan() {
        vkode_pelajaran = txtkode_pelajaran.getText();
        vnama_pelajaran = txtnama_pelajaran.getText();
        vKKM = txtKKM.getText();
        
        if(btnSimpan.getText().equals("Simpan")){
            
            if (vKKM == null ) {
                JOptionPane.showMessageDialog(this, "Harap masukan kode pelajaran ! " );
                jdInputPelajaran.setVisible(true);
            } else {
                try {
                     _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "INSERT INTO tbpelajaran values (?,?,?)";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vkode_pelajaran);
                    stat.setString(2, vnama_pelajaran);
                    stat.setString(3, vKKM);
                    stat.executeUpdate();
                    clearInput();showDataPelajaran();
                    jdInputPelajaran.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);                                  
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan 2 () : " +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE );
                            
               }
            }
            
        } else {
            if (vKKM == null){
                try {
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "update tbpelajaran set nama_pelajaran = ?  where kode_pelajaran = '"+vkode_pelajaran+"'";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vnama_pelajaran);
                    stat.executeUpdate();
                    clearInput();showDataPelajaran();jdInputPelajaran.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);                      
                    }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan 3 () : " +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE );
                            
               }
            } else {
                try {
                    
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "update tbpelajaran set nama_pelajaran = ?, kkm = ?  where kode_pelajaran = '"+vkode_pelajaran+"'";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vnama_pelajaran);
                    stat.setString(2, vKKM);
                    stat.executeUpdate();
                    clearInput();
                    showDataPelajaran();
                    jdInputPelajaran.setVisible(false);
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
                "Apakah anda yakin akan menghapus adata ini ? Nama Pelajaran : " + vnama_pelajaran,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (jawab == JOptionPane.YES_OPTION){
            try {
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                sqldelete = "DELETE FROM tbpelajaran "
                        + "WHERE kode_pelajaran='"+vkode_pelajaran+"'";
                Statement stat = _Cnn.createStatement();
                stat.executeUpdate(sqldelete);
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                clearInput();
                showDataPelajaran();
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

        jdInputPelajaran = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        txtkode_pelajaran = new javax.swing.JTextField();
        txtnama_pelajaran = new javax.swing.JTextField();
        txtKKM = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnHapus2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbmatapelajaran = new javax.swing.JTable();
        lbRecord = new javax.swing.JLabel();

        jdInputPelajaran.setTitle("Input Data Mata Pelajaran");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Form Entry Data Mata Pelajaran");

        jLabel5.setText("Form ini digunakan untuk menginput data mata pelajaran");

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
                .addGap(115, 115, 115)
                .addComponent(btnSimpan)
                .addGap(18, 18, 18)
                .addComponent(btnBatal)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Input Data"));
        jPanel4.setOpaque(false);

        txtkode_pelajaran.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Kode Pelajaran :"));
        txtkode_pelajaran.setOpaque(false);

        txtnama_pelajaran.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Mata Pelajaran :"));
        txtnama_pelajaran.setOpaque(false);

        txtKKM.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "KKM :"));
        txtKKM.setOpaque(false);
        txtKKM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKKMActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtkode_pelajaran, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtnama_pelajaran, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtKKM, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtkode_pelajaran, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(txtnama_pelajaran, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(txtKKM, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/small-very-logo.png"))); // NOI18N

        javax.swing.GroupLayout jdInputPelajaranLayout = new javax.swing.GroupLayout(jdInputPelajaran.getContentPane());
        jdInputPelajaran.getContentPane().setLayout(jdInputPelajaranLayout);
        jdInputPelajaranLayout.setHorizontalGroup(
            jdInputPelajaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jdInputPelajaranLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdInputPelajaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jdInputPelajaranLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jdInputPelajaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(0, 73, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jdInputPelajaranLayout.setVerticalGroup(
            jdInputPelajaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdInputPelajaranLayout.createSequentialGroup()
                .addGroup(jdInputPelajaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdInputPelajaranLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setClosable(true);
        setTitle("Form Daftar Mata Pelajaran");

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

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tabel Data Mata Pelajaran : Klik 2x untuk mengubah/meghapus"));

        tbmatapelajaran.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Kode Pelajaran", "Nama Pelajaran", "KKM"
            }
        ));
        tbmatapelajaran.setRowHeight(25);
        tbmatapelajaran.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbmatapelajaranMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbmatapelajaran);

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
        if (vkode_pelajaran.equals("")){
            JOptionPane.showMessageDialog(this, "Anda belum memilih data yang akan dihapus",
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            aksiHapus();
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        clearInput();
        jdInputPelajaran.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
        cariNamaPelajaran();
    }//GEN-LAST:event_txtCariActionPerformed

    private void btnHapus2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapus2ActionPerformed
        // TODO add your handling code here:
        cariNamaPelajaran();
    }//GEN-LAST:event_btnHapus2ActionPerformed

    private void tbmatapelajaranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbmatapelajaranMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount()==1){
            btnHapus.setEnabled(true);
            vkode_pelajaran = tbmatapelajaran.getValueAt(tbmatapelajaran.getSelectedRow(),0).toString();
            txtkode_pelajaran.setText(vkode_pelajaran);
        } else if (evt.getClickCount()==2 ){
            btnSimpan.setText("Ubah");
            jdInputPelajaran.setVisible(true);
            jdInputPelajaran.setLocationRelativeTo(this);
            getDataMataPelajaran();
        }
    }//GEN-LAST:event_tbmatapelajaranMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if (txtkode_pelajaran.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Data Kode Mata pelajaran harus diisi", "Informasi",
                JOptionPane.INFORMATION_MESSAGE);
        } else if (txtnama_pelajaran.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Data nama pelajaran harus diisi",
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            aksiSimpan();
            jdInputPelajaran.setVisible(false);
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clearInput();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void txtKKMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKKMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKKMActionPerformed


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
    private javax.swing.JDialog jdInputPelajaran;
    private javax.swing.JLabel lbRecord;
    private javax.swing.JTable tbmatapelajaran;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtKKM;
    private javax.swing.JTextField txtkode_pelajaran;
    private javax.swing.JTextField txtnama_pelajaran;
    // End of variables declaration//GEN-END:variables
}
