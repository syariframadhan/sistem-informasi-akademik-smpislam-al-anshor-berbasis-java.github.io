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
public class IfrDaftarNilai extends javax.swing.JInternalFrame {
    
    

    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    private Connection conn = new KoneksiDB().getConnection();
    PreparedStatement pst = null;
    ResultSet res = null;
    String sqlselect, sqlinsert, sqldelete;
    String vid_nilai, vkode_kelas, vnis, vnik, vkode_pelajaran, vsemester, vUTS, vUAS, vTugas, vnama_kelas, vnama_siswa, vnama_guru, vnama_pelajaran;
    
    private DefaultTableModel tblnilai; 
    SimpleDateFormat tglinput = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat tglview = new SimpleDateFormat("dd-MM-yyyy");
    
    public IfrDaftarNilai() throws SQLException {
        initComponents();
        clearInput();
        setTabelSiswa();
        tampil_combo_kelas();
        tampil_combo_nama_guru();
        tampil_combo_nama_siswa();
        tampil_combo_pelajaran();
        showDataNilai();
        aturTextField();
        jdInputNilai.setSize(600, 590);
        jdInputNilai.setLocationRelativeTo(this);
       
    }
    
    private void clearInput(){
        txtid_nilai.setText("");
        cmbkelas.setSelectedIndex(0);
        cmbSiswa.setSelectedIndex(0);
        cmbGuru.setSelectedIndex(0);
        cmbPelajaran.setSelectedIndex(0);
        cmbSemester.setSelectedIndex(0);
        txtUTS.setText("");
        txtUAS.setText("");
        txtTugas.setText("");
        btnSimpan.setText("Simpan");
        vid_nilai="";
        
    }
    private void setTabelSiswa(){
        String [] kolom1 = {"ID","Kelas", "Nama Siswa","Nama Guru", "Nama Pelajaran", "Semester","Nilai UTS", "Nilai UAS", "Nilai Tugas"};
        tblnilai = new DefaultTableModel(null, kolom1) {
            Class[] types = new Class[]{
                java.lang.String.class,
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
                int cola = tblnilai.getColumnCount();
                return (col < cola ) ? false : true;
            }
        };
        tbnilai.setModel(tblnilai);
        tbnilai.getColumnModel().getColumn(0).setPreferredWidth(20);
        tbnilai.getColumnModel().getColumn(1).setPreferredWidth(30);
        tbnilai.getColumnModel().getColumn(2).setPreferredWidth(150);
        tbnilai.getColumnModel().getColumn(3).setPreferredWidth(150);
        tbnilai.getColumnModel().getColumn(4).setPreferredWidth(150);
        tbnilai.getColumnModel().getColumn(5).setPreferredWidth(70); 
        tbnilai.getColumnModel().getColumn(5).setPreferredWidth(70);  
        tbnilai.getColumnModel().getColumn(6).setPreferredWidth(70);  
        tbnilai.getColumnModel().getColumn(7).setPreferredWidth(70); 
    }
    
    private void clearTabelNilaiSiswa(){
        int row = tblnilai.getRowCount();
        for (int i = 0; i < row; i++){
            tblnilai.removeRow(0);
        }
    }
    
    private void showDataNilai(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelNilaiSiswa();
        sqlselect = "Select id_nilai, nama_kelas, nama_siswa, nama_guru, nama_pelajaran, semester, nilai_uts, nilai_uas, nilai_tugas FROM\n" +
"tbnilai a, tbkelas b, tbsiswa c, tbpelajaran d , tbguru e WHERE a.kode_kelas=b.kode_kelas and a.nis=c.nis and a.nik=e.nik and a.kode_pelajaran=d.kode_pelajaran;";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vid_nilai = res.getString("id_nilai");
            vnama_kelas = res.getString("nama_kelas");
            vnama_siswa = res.getString("nama_siswa");
            vnama_guru = res.getString("nama_guru");
            vnama_pelajaran = res.getString("nama_pelajaran");
            vsemester = res.getString("semester");
            vUTS = res.getString("nilai_uts");
            vUAS = res.getString("nilai_uas");
            vTugas = res.getString("nilai_tugas");
            Object [] data = {vid_nilai,vnama_kelas, vnama_siswa, vnama_guru, vnama_pelajaran , vsemester, vUTS, vUAS , vTugas};
            tblnilai.addRow(data);
        }
            lbRecord.setText("Record : " + tbnilai.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method showDataNilai() : " + ex );
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
                cmbkelas.addItem(kelas);
            }

        } catch (Exception e) {
        }
    }
    private void tampil_combo_nama_siswa() {
     try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            String sql = "select nama_siswa from tbsiswa";
            Statement stat = _Cnn.createStatement();
            ResultSet res=stat.executeQuery(sql);
            while (res.next()) {
                String siswa  = res.getString("nama_siswa"); 
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

    
   
    private void cariNilai(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelNilaiSiswa();
        sqlselect = "Select id_nilai , nama_kelas, nama_siswa, nama_guru, nama_pelajaran, semester, nilai_uts, nilai_uas, nilai_tugas FROM\n" +
"tbnilai a, tbkelas b, tbsiswa c, tbpelajaran d , tbguru e WHERE a.kode_kelas=b.kode_kelas and a.nis=c.nis and a.nik=e.nik and a.kode_pelajaran=d.kode_pelajaran and nis like '%"+txtCari.getText()+"%' order by nis asc";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vid_nilai = res.getString("id_nilai");
            vnama_kelas = res.getString("nama_kelas");
            vnama_siswa = res.getString("nama_siswa");
            vnama_guru = res.getString("nama_guru");
            vnama_pelajaran = res.getString("nama_pelajaran");
            vsemester = res.getString("semester");
            vUTS = res.getString("nilai_uts");
            vUAS = res.getString("nilai_uas");
            vTugas = res.getString("nilai_tugas");
            Object [] data = {vid_nilai,vnama_kelas, vnama_siswa, vnama_guru, vnama_pelajaran , vsemester, vUTS, vUAS , vTugas};
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
                cmbSiswa.setSelectedItem(res.getString("nama_siswa")); 
                cmbGuru.setSelectedItem(res.getString("nama_guru")); 
                cmbPelajaran.setSelectedItem(res.getString("nama_pelajaran")); 
                cmbSemester.setSelectedItem(res.getString("semester"));
                
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
        vsemester = cmbSemester.getSelectedItem().toString();
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
                    sqlinsert = "INSERT INTO tbnilai values (?,?,?,?,?,?,?,?,?)";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vid_nilai);
                    stat.setString(2, vkode_kelas);
                    stat.setString(3, vnis);
                    stat.setString(4, vnik); 
                    stat.setString(5, vkode_pelajaran);
                    stat.setString(6, vsemester);   
                    stat.setString(7, vUTS);                 
                    stat.setString(8, vUAS);
                    stat.setString(9, vTugas);
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
                    sqlinsert = "update tbnilai set kode_kelas = ?, nis = ?,  nik = ? , kode_pelajaran = ?, semester =? , nilai_uts = ?, nilai_uas = ?, nilai_tugas = ? where id_nilai = '"+vid_nilai+"'";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vkode_kelas);
                    stat.setString(2, vnis);
                    stat.setString(3, vnik); 
                    stat.setString(4, vkode_pelajaran);
                    stat.setString(5, vsemester);  
                    stat.setString(6, vUTS);                 
                    stat.setString(7, vUAS);
                    stat.setString(8, vTugas);
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
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jdInputNilai = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        cmbkelas = new javax.swing.JComboBox<>();
        txtKodeKelas = new javax.swing.JTextField();
        cmbSiswa = new javax.swing.JComboBox<>();
        txtNis = new javax.swing.JTextField();
        txtNik = new javax.swing.JTextField();
        cmbGuru = new javax.swing.JComboBox<>();
        txtKodeNamaPelajaran = new javax.swing.JTextField();
        cmbPelajaran = new javax.swing.JComboBox<>();
        txtUTS = new javax.swing.JTextField();
        txtUAS = new javax.swing.JTextField();
        txtTugas = new javax.swing.JTextField();
        txtid_nilai = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmbSemester = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnHapus2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbnilai = new javax.swing.JTable();
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

        jdInputNilai.setTitle("Form Input Data Siswa");
        jdInputNilai.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jdInputNilai.setResizable(false);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/small-very-logo.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Form Entry Nilai Data Siswa");

        jLabel5.setText("Form ini digunakan untuk menginput nilai data siswa");

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
                .addGap(167, 167, 167)
                .addComponent(btnSimpan)
                .addGap(18, 18, 18)
                .addComponent(btnBatal)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Input Data"));
        jPanel4.setOpaque(false);

        cmbkelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih" }));
        cmbkelas.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Kelas :"));
        cmbkelas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbkelasItemStateChanged(evt);
            }
        });
        cmbkelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbkelasActionPerformed(evt);
            }
        });

        txtKodeKelas.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Kode Kelas :"));
        txtKodeKelas.setOpaque(false);

        cmbSiswa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih" }));
        cmbSiswa.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Siswa :"));
        cmbSiswa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSiswaItemStateChanged(evt);
            }
        });
        cmbSiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSiswaActionPerformed(evt);
            }
        });

        txtNis.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "NIS :"));
        txtNis.setOpaque(false);

        txtNik.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "NIK :"));
        txtNik.setOpaque(false);

        cmbGuru.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih" }));
        cmbGuru.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Guru :"));
        cmbGuru.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbGuruItemStateChanged(evt);
            }
        });
        cmbGuru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbGuruActionPerformed(evt);
            }
        });

        txtKodeNamaPelajaran.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Kode Mata Pelajaran :"));
        txtKodeNamaPelajaran.setOpaque(false);

        cmbPelajaran.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih" }));
        cmbPelajaran.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Mata Pelajaran :"));
        cmbPelajaran.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPelajaranItemStateChanged(evt);
            }
        });
        cmbPelajaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPelajaranActionPerformed(evt);
            }
        });

        txtUTS.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "UTS :"));
        txtUTS.setOpaque(false);

        txtUAS.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "UAS :"));
        txtUAS.setOpaque(false);

        txtTugas.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tugas :"));
        txtTugas.setOpaque(false);

        txtid_nilai.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "ID Nilai :"));
        txtid_nilai.setOpaque(false);

        jLabel7.setText("# Di isikan otomatis");

        jLabel8.setText("# Di isikan otomatis");

        jLabel9.setText("# Di isikan otomatis");

        jLabel10.setText("# Di isikan otomatis");

        jLabel11.setText("# Di isikan otomatis");

        cmbSemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Pilih-", "1", "2" }));
        cmbSemester.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Semester :"));
        cmbSemester.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSemesterItemStateChanged(evt);
            }
        });
        cmbSemester.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSemesterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtUTS, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtUAS, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(txtTugas, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cmbPelajaran, 0, 261, Short.MAX_VALUE)
                                .addComponent(cmbGuru, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbSiswa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbkelas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtid_nilai, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNik, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                                    .addComponent(txtKodeNamaPelajaran)
                                    .addComponent(txtNis)
                                    .addComponent(txtKodeKelas))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtid_nilai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbkelas, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKodeKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNis, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbGuru, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNik, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbPelajaran, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKodeNamaPelajaran, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtUTS, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUAS, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTugas, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jdInputNilaiLayout = new javax.swing.GroupLayout(jdInputNilai.getContentPane());
        jdInputNilai.getContentPane().setLayout(jdInputNilaiLayout);
        jdInputNilaiLayout.setHorizontalGroup(
            jdInputNilaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdInputNilaiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdInputNilaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jdInputNilaiLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jdInputNilaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jdInputNilaiLayout.setVerticalGroup(
            jdInputNilaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdInputNilaiLayout.createSequentialGroup()
                .addGroup(jdInputNilaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdInputNilaiLayout.createSequentialGroup()
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
        setTitle("Form Data Nilai Siswa");
        setAutoscrolls(true);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Admin-Schoolar-Icon.png"))); // NOI18N
        setVisible(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Navigasi"));

        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/trans-hapus.png"))); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/trans-add.png"))); // NOI18N
        btnTambah.setText("Tambah Data Nilai");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 256, Short.MAX_VALUE)
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

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tabel Data Siswa : Klik 2x untuk mengubah/meghapus"));

        tbnilai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Kelas", "Nama Siswa", "Nama Guru", "Nama Pelajaran", "Semester", "Nilai UTS", "Nilai UAS", "Nilai Tugs"
            }
        ));
        tbnilai.setRowHeight(25);
        tbnilai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbnilaiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbnilai);
        if (tbnilai.getColumnModel().getColumnCount() > 0) {
            tbnilai.getColumnModel().getColumn(0).setResizable(false);
            tbnilai.getColumnModel().getColumn(6).setResizable(false);
            tbnilai.getColumnModel().getColumn(8).setResizable(false);
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

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        clearInput();
        jdInputNilai.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
        cariNilai();
    }//GEN-LAST:event_txtCariActionPerformed

    private void tbnilaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbnilaiMouseClicked
        // TODO add your handling code here:
       if (evt.getClickCount()==1){
           btnHapus.setEnabled(true);
           vid_nilai = tbnilai.getValueAt(tbnilai.getSelectedRow(),0).toString();
           txtid_nilai.setText(vid_nilai);
       } else if (evt.getClickCount()==2 ){
           btnSimpan.setText("Ubah");
           jdInputNilai.setVisible(true);
           jdInputNilai.setLocationRelativeTo(this);
         getDataNilai();
       }
    }//GEN-LAST:event_tbnilaiMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if (txtUAS.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Data nilai UAS harus diisi", "Informasi",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (txtUTS.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Data nilai UTS harus diisi",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } else if (cmbSemester.getSelectedItem().equals("-Pilih-")){
            JOptionPane.showMessageDialog(this, "Masukan Semester",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
        aksiSimpan();
            jdInputNilai.setVisible(false);
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clearInput();
    }//GEN-LAST:event_btnBatalActionPerformed

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

    private void cmbkelasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbkelasItemStateChanged
        // TODO add your handling code here:
       
    }//GEN-LAST:event_cmbkelasItemStateChanged

    private void cmbkelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbkelasActionPerformed
        // TODO add your handling code here:
        String tmp =(String)cmbkelas.getSelectedItem();
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
    }//GEN-LAST:event_cmbkelasActionPerformed

    private void btnHapus2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapus2ActionPerformed
        // TODO add your handling code here:
        cariNilai();
    }//GEN-LAST:event_btnHapus2ActionPerformed

    private void cmbSiswaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSiswaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSiswaItemStateChanged

    private void cmbSiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSiswaActionPerformed
        // TODO add your handling code here:
        String tmp =(String)cmbSiswa.getSelectedItem();
        String sql = "SELECT * FROM tbsiswa WHERE nama_siswa = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, tmp);
            res = pst.executeQuery();
           if (res.next()){
               String add = res.getString("nis");
               txtNis.setText(add);
               
           } else {
               txtNis.setText("");
           }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data gagal ditampilkan!");
        }
    }//GEN-LAST:event_cmbSiswaActionPerformed

    private void cmbGuruItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbGuruItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbGuruItemStateChanged

    private void cmbGuruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbGuruActionPerformed
        // TODO add your handling code here:
        String tmp =(String)cmbGuru.getSelectedItem();
        String sql = "SELECT * FROM tbguru WHERE nama_guru = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, tmp);
            res = pst.executeQuery();
           if (res.next()){
               String add = res.getString("nik");
               txtNik.setText(add);
               
           } else {
               txtNik.setText("");
           }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data gagal ditampilkan!");
        }
    }//GEN-LAST:event_cmbGuruActionPerformed

    private void cmbPelajaranItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbPelajaranItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPelajaranItemStateChanged

    private void cmbPelajaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPelajaranActionPerformed
        // TODO add your handling code here:
        String tmp =(String)cmbPelajaran.getSelectedItem();
        String sql = "SELECT * FROM tbpelajaran WHERE nama_pelajaran = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, tmp);
            res = pst.executeQuery();
           if (res.next()){
               String add = res.getString("kode_pelajaran");
               txtKodeNamaPelajaran.setText(add);
               
           } else {
               txtKodeNamaPelajaran.setText("");
           }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data gagal ditampilkan!");
        }
    }//GEN-LAST:event_cmbPelajaranActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        if (vid_nilai.equals("")){
            JOptionPane.showMessageDialog(this, "Anda belum memilih data yang akan dihapus",
                "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            aksiHapus();
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void cmbSemesterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSemesterItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSemesterItemStateChanged

    private void cmbSemesterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSemesterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSemesterActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnHapus1;
    private javax.swing.JButton btnHapus2;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTambah1;
    private javax.swing.JComboBox<String> cmbGuru;
    private javax.swing.JComboBox<String> cmbPelajaran;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JComboBox<String> cmbSiswa;
    private javax.swing.JComboBox<String> cmbkelas;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JDialog jdInputNilai;
    private javax.swing.JLabel lbRecord;
    private javax.swing.JLabel lbRecord1;
    private javax.swing.JTable tbDataMahasiswa1;
    private javax.swing.JTable tbnilai;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtCari1;
    private javax.swing.JTextField txtKodeKelas;
    private javax.swing.JTextField txtKodeNamaPelajaran;
    private javax.swing.JTextField txtNik;
    private javax.swing.JTextField txtNis;
    private javax.swing.JTextField txtTugas;
    private javax.swing.JTextField txtUAS;
    private javax.swing.JTextField txtUTS;
    private javax.swing.JTextField txtid_nilai;
    // End of variables declaration//GEN-END:variables
}
