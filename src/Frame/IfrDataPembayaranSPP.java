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
public class IfrDataPembayaranSPP extends javax.swing.JInternalFrame {
    
    

    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    private Connection conn = new KoneksiDB().getConnection();
    PreparedStatement pst = null;
    ResultSet res = null;
    String sqlselect, sqlinsert, sqldelete;
    String vid_transaksi, vnis, vid_admin, vtgl_bayar, vbulan_bayar_dari, vbulan_bayar_sampai, vtahun_bayar, vnama_siswa, vnama_admin;
    
    private DefaultTableModel tblpembayaran; 
    SimpleDateFormat tglinput = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat tglview = new SimpleDateFormat("dd-MM-yyyy");
    
    public IfrDataPembayaranSPP() throws SQLException {
        initComponents();
        setTabelPembayaran();  
        showDataPembayaran();
    }
   
    
    private void setTabelPembayaran(){
        String [] kolom1 = {"ID Transaksi","NIS","Nama Siswa", "Admin","Tanggal Bayar", "Bayar dari Bulan", "Bayar sampai Bulan","Tahun Bayar"};
        tblpembayaran = new DefaultTableModel(null, kolom1) {
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
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
                int cola = tblpembayaran.getColumnCount();
                return (col < cola ) ? false : true;
            }
        };
        tbPembayaran.setModel(tblpembayaran);
        tbPembayaran.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbPembayaran.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbPembayaran.getColumnModel().getColumn(2).setPreferredWidth(100);
        tbPembayaran.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbPembayaran.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbPembayaran.getColumnModel().getColumn(5).setPreferredWidth(100); 
        tbPembayaran.getColumnModel().getColumn(6).setPreferredWidth(100); 
        tbPembayaran.getColumnModel().getColumn(7).setPreferredWidth(100); 
    }
    
    private void clearTabelPembayaran(){
        int row = tblpembayaran.getRowCount();
        for (int i = 0; i < row; i++){
            tblpembayaran.removeRow(0);
        }
    }
    
    private void showDataPembayaran(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelPembayaran();
        sqlselect = "SELECT a.id_transaksi , a.nis , c.namasiswa, b.nama_admin, a.tgl_bayar, a.bulan_bayar_dari, a.bulan_bayar_sampai, a.tahun_bayar FROM tbpembayaran a, tbadmin b, tbsiswa c WHERE a.id_admin=b.id_admin and a.nis=c.nis;";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vid_transaksi = res.getString("id_transaksi");
            vnama_siswa = res.getString("namasiswa");
            vnis = res.getString("nis");
            vnama_admin = res.getString("nama_admin");
            vtgl_bayar = res.getString("tgl_bayar");
            vbulan_bayar_dari = res.getString("bulan_bayar_dari");
            vbulan_bayar_sampai = res.getString("bulan_bayar_sampai");
            vtahun_bayar = res.getString("tahun_bayar");
            Object [] data = {vid_transaksi,vnis,vnama_siswa, vnama_admin, vtgl_bayar, vbulan_bayar_dari , vbulan_bayar_sampai, vtahun_bayar};
            tblpembayaran.addRow(data)
//                    if (vbulan_bayar_dari = )
                    ;
        }
            lbRecord.setText("Record : " + tbPembayaran.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method showDataNilai() : " + ex );
         }
        
    }
     /*
    private void tampil_combo_kelas() {
     try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            String sql = "select nama_kelas from tbkelas";
            Statement stat = _Cnn.createStatement();
            ResultSet res=stat.executeQuery(sql);
            while (res.next()) {
                String kelas  = res.getString("nama_kelas"); 
                cmbkelas.addItem(kelas);
            }

        } catch (Exception e) {
        }
    }
    private void tampil_combo_nama_siswa() {
     try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            String sql = "select namasiswa from tbsiswa";
            Statement stat = _Cnn.createStatement();
            ResultSet res=stat.executeQuery(sql);
            while (res.next()) {
                String siswa  = res.getString("namasiswa"); 
                cmbSiswa.addItem(siswa);
            }

        } catch (Exception e) {
        }
    }
    
    private void tampil_combo_nama_guru() {
     try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            String sql = "select nama_guru from tbguru";
            Statement stat = _Cnn.createStatement();
            ResultSet res=stat.executeQuery(sql);
            while (res.next()) {
                String guru  = res.getString("nama_guru"); 
                cmbGuru.addItem(guru);
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
                cmbPelajaran.addItem(pelajaran);
            }

        } catch (Exception e) {
        }
    }
    
     void aturTextField() {
    txtKodeKelas.setEnabled(false);
    txtNis.setEnabled(false);
    txtKodeNamaPelajaran.setEnabled(false);
    txtNik.setEnabled(false);
    txtid_nilai.setEnabled(false);
}

    
   
    private void cariNilaiSiswa(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelNilaiSiswa();
        sqlselect = "Select nama_kelas, namasiswa, nama_guru, nama_pelajaran, nilai_uts, nilai_uas, nilai_tugas FROM\n" +
"tbnilai a, tbkelas b, tbsiswa c, tbpelajaran d , tbguru e WHERE a.kode_kelas=b.kode_kelas and a.nis=c.nis and a.nik=e.nik and a.kode_pelajaran=d.kode_pelajaran and nis like '%"+txtCari.getText()+"%' order by nis asc";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vnama_kelas = res.getString("nama_kelas");
            vnama_siswa = res.getString("namasiswa");
            vnama_guru = res.getString("nama_guru");
            vUTS = res.getString("nilai_uts");
            vUAS = res.getString("nilai_uas");
            vTugas = res.getString("nilai_tugas");
            Object [] data = {vnama_kelas, vnama_siswa, vnama_guru, vUTS, vUAS, vTugas };
            tblnilai.addRow(data);
        }
            lbRecord.setText("Record : " + tbnilai.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method showDataNilaiSiswa() : " + ex );
         }
    }
    
    
   
    private void getDataNilai(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "SELECT * FROM tbnilai a, tbkelas b, tbsiswa c, tbguru d, tbpelajaran e WHERE a.kode_kelas=b.kode_kelas and a.nis=c.nis and a.nik=d.nik and a.kode_pelajaran=e.kode_pelajaran and id_nilai='"+vid_nilai+"'";;
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.first()){
                cmbkelas.setSelectedItem(res.getString("nama_kelas"));               
                cmbSiswa.setSelectedItem(res.getString("namasiswa")); 
                cmbGuru.setSelectedItem(res.getString("nama_guru")); 
                cmbPelajaran.setSelectedItem(res.getString("nama_pelajaran")); 
                
                txtUTS.setText(res.getString("nilai_uts"));
                txtUAS.setText(res.getString("nilai_uas"));
                txtTugas.setText(res.getString("nilai_tugas"));
                }
        } catch (SQLException ex){
            JOptionPane.showMessageDialog(this, "Error method getDataNilai():" +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
        

   
    
    private void aksiSimpan(){
        vid_nilai = txtid_nilai.getText();      
        vkode_kelas = txtKodeKelas.getText();
        vnis = txtNis.getText();
        vnik = txtNik.getText();
        vkode_pelajaran = txtKodeNamaPelajaran.getText();
        vnama_kelas = cmbkelas.getSelectedItem().toString();
        vnama_siswa = cmbSiswa.getSelectedItem().toString();
        vnama_guru = cmbGuru.getSelectedItem().toString();
        vnama_pelajaran = cmbPelajaran.getSelectedItem().toString();
        vUTS= txtUTS.getText();    
        vUAS = txtUAS.getText();    
        vTugas = txtTugas.getText();    
        if(btnSimpan.getText().equals("Simpan")){
            
            if (vUAS == null ) {
                JOptionPane.showMessageDialog(this, "Belum lengkap ! " );
                jdInputNilai.setVisible(true);
            } else {
                try {
                    vid_nilai=null;
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "INSERT INTO tbnilai values (?,?,?,?,?,?,?,?)";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vid_nilai);
                    stat.setString(2, vkode_kelas);
                    stat.setString(3, vnis);
                    stat.setString(4, vnik); 
                    stat.setString(5, vkode_pelajaran);
                    stat.setString(6, vUTS);                 
                    stat.setString(7, vUAS);
                    stat.setString(8, vTugas);
                    stat.executeUpdate();
                    clearInput();showDataNilai();
                    jdInputNilai.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);                                  
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan 2 () : " +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE );
                            
               }
            }
            
        } else {
            if (vid_nilai == null){
                    JOptionPane.showMessageDialog(this, "Belum lengkap ! " );
                    jdInputNilai.setVisible(true);
            } else {
                try {
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "update tbnilai set kode_kelas = ?, nis = ?,  nik = ? , kode_pelajaran = ?, nilai_uts = ?, nilai_uas = ?, nilai_tugas = ? where id_nilai = '"+vid_nilai+"'";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vkode_kelas);
                    stat.setString(2, vnis);
                    stat.setString(3, vnik); 
                    stat.setString(4, vkode_pelajaran);
                    stat.setString(5, vUTS);                 
                    stat.setString(6, vUAS);
                    stat.setString(7, vTugas);
                    stat.executeUpdate();
                    clearInput();
                    showDataNilai();
                    jdInputNilai.setVisible(false);
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
                "Apakah anda yakin akan menghapus adata ini ? ID : " + vnis,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (jawab == JOptionPane.YES_OPTION){
            try {
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                sqldelete = "DELETE FROM tbnilai "
                        + "WHERE id_nilai='"+vid_nilai+"'";
                Statement stat = _Cnn.createStatement();
                stat.executeUpdate(sqldelete);
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                clearInput();
                showDataNilai();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error method aksiHapus() : " +ex,
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    }
    
    
    */
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtCari = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnHapus2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbPembayaran = new javax.swing.JTable();
        lbRecord = new javax.swing.JLabel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel5 = new javax.swing.JPanel();
        btnHapus1 = new javax.swing.JButton();
        btnTambah1 = new javax.swing.JButton();
        txtCari1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDataMahasiswa1 = new javax.swing.JTable();
        lbRecord1 = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Data Pembayaran SPP");
        setAutoscrolls(true);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin-Schoolar-Icon.png"))); // NOI18N
        setVisible(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Pencarian Data"));

        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Cari NIS");

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
                .addGap(245, 245, 245)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapus2)
                .addContainerGap(317, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btnHapus2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tabel Data Pembayaran SPP"));

        tbPembayaran.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Transaksi", "NIS", "Nama Siswa", "Admin", "Tanggal Bayar", "Bayar dari Bulan", "Bayar sampai Bulan", "Tahun Bayar"
            }
        ));
        tbPembayaran.setRowHeight(25);
        tbPembayaran.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPembayaranMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbPembayaran);
        if (tbPembayaran.getColumnModel().getColumnCount() > 0) {
            tbPembayaran.getColumnModel().getColumn(0).setResizable(false);
            tbPembayaran.getColumnModel().getColumn(7).setResizable(false);
        }

        lbRecord.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbRecord.setText("Record : 0 ");

        jInternalFrame1.setClosable(true);
        jInternalFrame1.setMaximizable(true);
        jInternalFrame1.setTitle("Form Mahasiswa");
        jInternalFrame1.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin-Schoolar-Icon.png"))); // NOI18N

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Navigasi"));

        btnHapus1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/trans-hapus.png"))); // NOI18N
        btnHapus1.setText("Hapus");
        btnHapus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapus1ActionPerformed(evt);
            }
        });

        btnTambah1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/trans-add.png"))); // NOI18N
        btnTambah1.setText("Tambah");
        btnTambah1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambah1ActionPerformed(evt);
            }
        });

        txtCari1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCari1ActionPerformed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Cari Nama");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnTambah1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapus1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 411, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCari1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCari1)
                        .addComponent(jLabel6))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHapus1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tabel Data Mahasiswa : Klik 2x untuk mengubah/meghapus"));

        tbDataMahasiswa1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "NIM", "Nama Mahasiswa", "L/P", "Tempat Lahir", "Tgl Lahir", "Alamat", "No Telepon"
            }
        ));
        tbDataMahasiswa1.setRowHeight(25);
        tbDataMahasiswa1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDataMahasiswa1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbDataMahasiswa1);

        lbRecord1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbRecord1.setText("Record : 0 ");

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbRecord1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbRecord1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 499, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 500, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbRecord)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 266, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 265, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
       // cariNilaiSiswa();
    }//GEN-LAST:event_txtCariActionPerformed

    private void btnHapus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapus1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHapus1ActionPerformed

    private void btnTambah1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambah1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTambah1ActionPerformed

    private void txtCari1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCari1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCari1ActionPerformed

    private void tbDataMahasiswa1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDataMahasiswa1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbDataMahasiswa1MouseClicked

    private void btnHapus2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapus2ActionPerformed
        // TODO add your handling code here:
    //    cariNilaiSiswa();
    }//GEN-LAST:event_btnHapus2ActionPerformed

    private void tbPembayaranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPembayaranMouseClicked
        // TODO add your handling code here:
//        if (evt.getClickCount()==1){
//            btnHapus.setEnabled(true);
//            vid_nilai = tbPembayaran.getValueAt(tbPembayaran.getSelectedRow(),0).toString();
//            txtid_nilai.setText(vid_nilai);
//        } else if (evt.getClickCount()==2 ){
//            btnSimpan.setText("Ubah");
//            jdInputSiswa.setVisible(true);
//            jdInputSiswa.setLocationRelativeTo(this);
//            getDataNilai();
//        }
    }//GEN-LAST:event_tbPembayaranMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapus1;
    private javax.swing.JButton btnHapus2;
    private javax.swing.JButton btnTambah1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbRecord;
    private javax.swing.JLabel lbRecord1;
    private javax.swing.JTable tbDataMahasiswa1;
    private javax.swing.JTable tbPembayaran;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtCari1;
    // End of variables declaration//GEN-END:variables
}
