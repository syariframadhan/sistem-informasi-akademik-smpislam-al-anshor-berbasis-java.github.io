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
public class IfrDaftarJadwal extends javax.swing.JInternalFrame {

    
    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    private Connection conn = new KoneksiDB().getConnection();
    PreparedStatement pst = null;
    ResultSet res = null;
    String sqlselect, sqlinsert, sqldelete;
    String vid_jadwal, vkode_kelas, vkode_pelajaran, vnik_guru, vjam_mulai, vjam_selesai, vnama_kelas, vnama_pelajaran, vnama_guru;
    
    
    private DefaultTableModel tbljadwal; 
    /**
     * Creates new form IfrDaftarMataPelajaran
     */
    public IfrDaftarJadwal() {
        initComponents();
        clearInput();
        setTabelJadwal();
        showDataJadwal();
        tampil_combo_kelas();
        tampil_combo_pelajaran();
        tampil_combo_guru();
        jdInputJadwal.setSize(409, 552);
        jdInputJadwal.setLocationRelativeTo(this);
    }
        private void clearInput(){
        cmbNamaGuru.setSelectedIndex(0);
        cmbNamaKelas.setSelectedIndex(0);
        cmbNamaPelajaran.setSelectedIndex(0);
        txtJamMulai.setText(""); 
        txtJamSelesai.setText(""); 
        btnSimpan.setText("Simpan");
        vid_jadwal="";
        }
        
        private void setTabelJadwal(){
        String [] kolom1 = {"Nama Kelas", "Nama Pelajaran", "Nama Guru", "Jam Mulai", "Jam Selesai"};
        tbljadwal = new DefaultTableModel(null, kolom1) {
            Class[] types = new Class[]{
                java.lang.String.class,
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
                int cola = tbljadwal.getColumnCount();
                return (col < cola ) ? false : true;
            }
        };
        tbjadwal.setModel(tbljadwal);
        tbjadwal.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbjadwal.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbjadwal.getColumnModel().getColumn(2).setPreferredWidth(100);
        tbjadwal.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbjadwal.getColumnModel().getColumn(4).setPreferredWidth(100);
    }
    
    private void clearTabelJadwal(){
        int row = tbljadwal.getRowCount();
        for (int i = 0; i < row; i++){
            tbljadwal.removeRow(0);
        }
    }
    
    private void showDataJadwal(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelJadwal();
        sqlselect="SELECT nama_kelas, nama_pelajaran, nama_guru, jam_mulai, jam_selesai FROM tbjadwal a,tbkelas b, tbguru c, tbpelajaran d                      WHERE a.kode_kelas=b.kode_kelas and a.nik=c.nik and a.kode_pelajaran=d.kode_pelajaran";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vnama_kelas = res.getString("nama_kelas");
            vnama_pelajaran = res.getString("nama_pelajaran");
            vnama_guru = res.getString("nama_guru");
            vjam_mulai = res.getString("jam_mulai");
            vjam_selesai = res.getString("jam_selesai");
            Object [] data = {vnama_kelas, vnama_pelajaran, vnama_guru, vjam_mulai, vjam_selesai};
            tbljadwal.addRow(data);
        }
            lbRecord.setText("Record : " + tbjadwal.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method showDataJadwal() : " + ex );
         }
        
    }
    
    private void tampil_combo_kelas() {
     try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            String sql = "select nama_kelas from tbkelas";
            Statement stat = _Cnn.createStatement();
            ResultSet res=stat.executeQuery(sql);
            while (res.next()) {
                String kelas  = res.getString("nama_kelas"); 
                cmbNamaKelas.addItem(kelas);
            }

        } catch (Exception e) {
        }
    }
    
     private void tampil_combo_pelajaran() {
     try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            String sql = "select nama_pelajaran from tbpelajaran";
            Statement stat = _Cnn.createStatement();
            ResultSet res=stat.executeQuery(sql);
            while (res.next()) {
                String pelajaran  = res.getString("nama_pelajaran"); 
                cmbNamaPelajaran.addItem(pelajaran);
            }

        } catch (Exception e) {
        }
    }
     
    private void tampil_combo_guru() {
     try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            String sql = "select nama_guru from tbguru";
            Statement stat = _Cnn.createStatement();
            ResultSet res=stat.executeQuery(sql);
            while (res.next()) {
                String guru  = res.getString("nama_guru"); 
                cmbNamaGuru.addItem(guru);
            }

        } catch (Exception e) {
        }
    }
 
    private void cariJadwal(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelJadwal();
        sqlselect = "SELECT * FROM tbjadwal a,tbkelas b, tbguru c, tbpelajaran d WHERE a.kode_kelas=b.kode_kelas and a.nik=c.nik and a.kode_pelajaran=d.kode_pelajaran like '%"+txtCari.getText()+"%' or nama_kelas like '%"+txtCari.getText()+"%' order by nama_kelas asc";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vnama_kelas = res.getString("nama_kelas");
            vnama_pelajaran = res.getString("nama_pelajaran");
            vnama_guru = res.getString("nama_guru");
            vjam_mulai = res.getString("jam_mulai");
            vjam_selesai = res.getString("jam_selesai");
            Object [] data = {vnama_kelas, vnama_pelajaran, vnama_guru, vjam_mulai, vjam_selesai};
            tbljadwal.addRow(data);
        }
            lbRecord.setText("Record : " + tbjadwal.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method cariJadwal() : " + ex );
         }
    }
    
    
    
    private void getDataJadwal(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "SELECT * FROM tbjadwal a, tbkelas b , tbpelajaran c, tbguru d WHERE a.kode_kelas=b.kode_kelas and a.kode_pelajaran=c.kode_pelajaran and a.nik=d.nik and id_jadwal='"+vid_jadwal+"'";;
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.first()){
                txtid_jadwal.setText(res.getString("id_jadwal"));
                cmbNamaKelas.setSelectedItem(res.getString("nama_kelas")); 
                cmbNamaPelajaran.setSelectedItem(res.getString("nama_pelajaran")); 
                cmbNamaGuru.setSelectedItem(res.getString("nama_guru")); 
                txtJamMulai.setText(res.getString("jam_mulai"));
                txtJamSelesai.setText(res.getString("jam_selesai"));
                }
        } catch (SQLException ex){
            JOptionPane.showMessageDialog(this, "Error method getDataJadwal ():" +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void aksiSimpan() {
        vid_jadwal = txtid_jadwal.getText();
        vkode_kelas = txtKodeKelas.getText();
        vkode_pelajaran = txtKodePelajaran.getText();
        vnik_guru = txtNIK.getText();
        vjam_mulai = txtJamMulai.getText();
        vjam_selesai = txtJamSelesai.getText();
        
        if(btnSimpan.getText().equals("Simpan")){
            
            if (vid_jadwal == null ) {
                JOptionPane.showMessageDialog(this, "Harap masukan id jadwal ! " );
                jdInputJadwal.setVisible(true);
            } else {
                try {
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "INSERT INTO tbjadwal values (?,?,?,?,?,?)";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vid_jadwal);
                    stat.setString(2, vkode_kelas);
                    stat.setString(3, vkode_pelajaran);
                    stat.setString(4, vnik_guru);
                    stat.setString(5, vjam_mulai);
                    stat.setString(6, vjam_selesai);
                    stat.executeUpdate();
                    clearInput();showDataJadwal();
                    jdInputJadwal.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);                                  
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan 2 () : " +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE );
                            
               }
            }
            
        } else {
            if (vid_jadwal == null){
                JOptionPane.showMessageDialog(this, "Harap masukan id jadwal ! " );
                jdInputJadwal.setVisible(true);
            } else {
                try {                    
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "update tbjadwal set kode_kelas = ?, kode_pelajaran = ?, nik = ?, jam_mulai = ?, jam_selesai = ?  where id_jadwal = '"+vid_jadwal+"'";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vkode_kelas);
                    stat.setString(2, vkode_pelajaran);
                    stat.setString(3, vnik_guru);
                    stat.setString(4, vjam_mulai);
                    stat.setString(5, vjam_selesai);
                    stat.executeUpdate();
                    clearInput();
                    showDataJadwal();
                    jdInputJadwal.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil diubah",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);                      
                    }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan 4 () : " +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE );
                            
               }
            }
        }
        
    }
//    
//    private void aksiHapus(){
//        int jawab = JOptionPane.showConfirmDialog(this, 
//                "Apakah anda yakin akan menghapus adata ini ? Nama Pelajaran : " + vnama_kelas,
//                "Konfirmasi", JOptionPane.YES_NO_OPTION);
//        if (jawab == JOptionPane.YES_OPTION){
//            try {
//                _Cnn = null;
//                _Cnn = getCnn.getConnection();
//                sqldelete = "DELETE FROM tbkelas "
//                        + "WHERE kode_kelas='"+vkode_kelas+"'";
//                Statement stat = _Cnn.createStatement();
//                stat.executeUpdate(sqldelete);
//                JOptionPane.showMessageDialog(this, "Data berhasil dihapus",
//                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
//                clearInput();
//                showDataKelas();
//            } catch (SQLException ex) {
//                JOptionPane.showMessageDialog(this, "Error method aksiHapus() : " +ex,
//                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
//    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jdInputJadwal = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        txtid_jadwal = new javax.swing.JTextField();
        cmbNamaKelas = new javax.swing.JComboBox<>();
        txtKodeKelas = new javax.swing.JTextField();
        cmbNamaPelajaran = new javax.swing.JComboBox<>();
        txtKodePelajaran = new javax.swing.JTextField();
        cmbNamaGuru = new javax.swing.JComboBox<>();
        txtNIK = new javax.swing.JTextField();
        txtJamMulai = new javax.swing.JTextField();
        txtJamSelesai = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnHapus2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbjadwal = new javax.swing.JTable();
        lbRecord = new javax.swing.JLabel();

        jdInputJadwal.setTitle("Input Data Jadwal");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Form Entry Data Jadwal");

        jLabel5.setText("Form ini digunakan untuk menginput data jadwal");

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
                .addGap(103, 103, 103)
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

        txtid_jadwal.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Id Jadwal :"));
        txtid_jadwal.setOpaque(false);

        cmbNamaKelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih" }));
        cmbNamaKelas.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Kelas :"));
        cmbNamaKelas.setOpaque(false);
        cmbNamaKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbNamaKelasActionPerformed(evt);
            }
        });

        txtKodeKelas.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Kode Kelas :"));
        txtKodeKelas.setOpaque(false);

        cmbNamaPelajaran.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih" }));
        cmbNamaPelajaran.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Pelajaran :"));
        cmbNamaPelajaran.setOpaque(false);
        cmbNamaPelajaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbNamaPelajaranActionPerformed(evt);
            }
        });

        txtKodePelajaran.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Kode Pelajaran :"));
        txtKodePelajaran.setOpaque(false);

        cmbNamaGuru.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih" }));
        cmbNamaGuru.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Guru :"));
        cmbNamaGuru.setOpaque(false);
        cmbNamaGuru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbNamaGuruActionPerformed(evt);
            }
        });

        txtNIK.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "NIK :"));
        txtNIK.setOpaque(false);

        txtJamMulai.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Jam Mulai :"));
        txtJamMulai.setOpaque(false);

        txtJamSelesai.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Jam Selesai :"));
        txtJamSelesai.setOpaque(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtJamMulai, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtJamSelesai))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cmbNamaGuru, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNIK))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(cmbNamaPelajaran, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtKodePelajaran))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cmbNamaKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtKodeKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtid_jadwal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtid_jadwal, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbNamaKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKodeKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbNamaPelajaran, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKodePelajaran, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbNamaGuru, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNIK, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtJamMulai, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJamSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/small-very-logo.png"))); // NOI18N

        javax.swing.GroupLayout jdInputJadwalLayout = new javax.swing.GroupLayout(jdInputJadwal.getContentPane());
        jdInputJadwal.getContentPane().setLayout(jdInputJadwalLayout);
        jdInputJadwalLayout.setHorizontalGroup(
            jdInputJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdInputJadwalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdInputJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jdInputJadwalLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jdInputJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jdInputJadwalLayout.setVerticalGroup(
            jdInputJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdInputJadwalLayout.createSequentialGroup()
                .addGroup(jdInputJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdInputJadwalLayout.createSequentialGroup()
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
        setTitle("Form Daftar Jadwal");

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

        tbjadwal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Nama Kelas", "Nama Pelajaran", "Nama Guru", "Jam Mulai", "Jam Selesai"
            }
        ));
        tbjadwal.setRowHeight(25);
        tbjadwal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbjadwalMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbjadwal);

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
          //  aksiHapus();
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        clearInput();
        jdInputJadwal.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
       cariJadwal();
    }//GEN-LAST:event_txtCariActionPerformed

    private void btnHapus2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapus2ActionPerformed
        // TODO add your handling code here:
      cariJadwal();
    }//GEN-LAST:event_btnHapus2ActionPerformed

    private void tbjadwalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbjadwalMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount()==1){
            btnHapus.setEnabled(true);
            vid_jadwal = tbjadwal.getValueAt(tbjadwal.getSelectedRow(),0).toString();
            txtid_jadwal.setText(vid_jadwal);
        } else if (evt.getClickCount()==2 ){
            btnSimpan.setText("Ubah");
            jdInputJadwal.setVisible(true);
            jdInputJadwal.setLocationRelativeTo(this);
        getDataJadwal();
        }
    }//GEN-LAST:event_tbjadwalMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if (txtid_jadwal.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Data Kode Mata pelajaran harus diisi", "Informasi",
                JOptionPane.INFORMATION_MESSAGE);
        } else if (txtJamMulai.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Data nama pelajaran harus diisi",
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
        aksiSimpan();
            jdInputJadwal.setVisible(false);
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clearInput();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void cmbNamaKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbNamaKelasActionPerformed
        // TODO add your handling code here:
        String tmp =(String)cmbNamaKelas.getSelectedItem();
        String sql = "SELECT * FROM tbkelas WHERE nama_kelas = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, tmp);
            res = pst.executeQuery();
           if (res.next()){
               String add = res.getString("kode_kelas");
               txtKodeKelas.setText(add);
               
           } else {
               txtKodeKelas.setText("");
           }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data gagal ditampilkan!");
        }
    }//GEN-LAST:event_cmbNamaKelasActionPerformed

    private void cmbNamaPelajaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbNamaPelajaranActionPerformed
        // TODO add your handling code here:
        String tmp =(String)cmbNamaPelajaran.getSelectedItem();
        String sql = "SELECT * FROM tbpelajaran WHERE nama_pelajaran = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, tmp);
            res = pst.executeQuery();
           if (res.next()){
               String add = res.getString("kode_pelajaran");
               txtKodePelajaran.setText(add);
               
           } else {
               txtKodePelajaran.setText("");
           }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data gagal ditampilkan!");
        }
    }//GEN-LAST:event_cmbNamaPelajaranActionPerformed

    private void cmbNamaGuruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbNamaGuruActionPerformed
        // TODO add your handling code here:
        String tmp =(String)cmbNamaGuru.getSelectedItem();
        String sql = "SELECT * FROM tbguru WHERE nama_guru = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, tmp);
            res = pst.executeQuery();
           if (res.next()){
               String add = res.getString("nik");
               txtNIK.setText(add);
               
           } else {
               txtNIK.setText("");
           }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data gagal ditampilkan!");
        }
    }//GEN-LAST:event_cmbNamaGuruActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnHapus2;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox<String> cmbNamaGuru;
    private javax.swing.JComboBox<String> cmbNamaKelas;
    private javax.swing.JComboBox<String> cmbNamaPelajaran;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JDialog jdInputJadwal;
    private javax.swing.JLabel lbRecord;
    private javax.swing.JTable tbjadwal;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtJamMulai;
    private javax.swing.JTextField txtJamSelesai;
    private javax.swing.JTextField txtKodeKelas;
    private javax.swing.JTextField txtKodePelajaran;
    private javax.swing.JTextField txtNIK;
    private javax.swing.JTextField txtid_jadwal;
    // End of variables declaration//GEN-END:variables
}
