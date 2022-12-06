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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author RAMA
 */
public class IfrCetakNotaPembayaranSPP extends javax.swing.JInternalFrame {
    
    

    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    private Connection conn = new KoneksiDB().getConnection();
    PreparedStatement pst = null;
    ResultSet res = null;
    String sqlselect, sqlinsert, sqldelete;
    String vid_transaksi, vnis,  vtgl_bayar, vbulan_bayar_dari, vbulan_bayar_sampai, vtahun_bayar, vnama_siswa, vtotalbayar;
    int vkodebayar1, vkodebayar2, vtotalkodebayar;
    
    private DefaultTableModel tblpembayaran; 
    SimpleDateFormat tglinput = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat tglview = new SimpleDateFormat("dd-MM-yyyy");
    
    public IfrCetakNotaPembayaranSPP() throws SQLException {
        initComponents();
        setTabelPembayaran(); 
        jdCetakNotaPembayaran.setSize(548, 270);
        jdCetakNotaPembayaran.setLocationRelativeTo(this);
    }

   
    
    private void setTabelPembayaran(){
        String [] kolom1 = {"ID Transaksi","NIS","Nama Siswa", "Tanggal Bayar", "Bayar dari Bulan", "Bayar sampai Bulan","Tahun Bayar"};
        tblpembayaran = new DefaultTableModel(null, kolom1) {
            Class[] types = new Class[]{
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
        tbPembayaran.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbPembayaran.getColumnModel().getColumn(1).setPreferredWidth(50);
        tbPembayaran.getColumnModel().getColumn(2).setPreferredWidth(120);
        tbPembayaran.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbPembayaran.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbPembayaran.getColumnModel().getColumn(5).setPreferredWidth(100); 
        tbPembayaran.getColumnModel().getColumn(6).setPreferredWidth(100); 
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
        sqlselect = "SELECT a.id_transaksi , a.nis , b.nama_siswa, a.tgl_bayar, a.bulan_bayar_dari, a.bulan_bayar_sampai, a.tahun_bayar FROM tbpembayaran a, tbsiswa b WHERE a.nis=b.nis;";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vid_transaksi = res.getString("id_transaksi");
            vnama_siswa = res.getString("nama_siswa");
            vnis = res.getString("nis");
            vtgl_bayar = res.getString("tgl_bayar");
            vbulan_bayar_dari = res.getString("bulan_bayar_dari");
            vbulan_bayar_sampai = res.getString("bulan_bayar_sampai");
            vtahun_bayar = res.getString("tahun_bayar");
            Object [] data = {vid_transaksi,vnis,vnama_siswa, vtgl_bayar, vbulan_bayar_dari , vbulan_bayar_sampai, vtahun_bayar};
            tblpembayaran.addRow(data)
//                    if (vbulan_bayar_dari = )
                    ;
        }
            lbRecord.setText("Record : " + tbPembayaran.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method showDataPembayaran() : " + ex );
         }
        
    }
    
    private void cetakNota (){
        String pth = System.getProperty("user.dir") + "./src/Report/ReportNotaPembayaran.jrxml";
         String reportDest = System.getProperty("user.dir") + "./src/Report/ReportNotaPembayaran.jasper";  
         try {
             HashMap hash = new HashMap();
             _Cnn = null;
             _Cnn = getCnn.getConnection(); 
             hash.put("ParameterIdTransaksi", vid_transaksi);
             JasperReport jrpt = JasperCompileManager.compileReport(pth);
             JasperPrint jprint = JasperFillManager.fillReport(jrpt, hash, _Cnn);
             JasperExportManager.exportReportToHtmlFile (jprint, reportDest);
             JasperViewer.viewReport(jprint, false);
             
         } catch (JRException ex) {
            JOptionPane.showMessageDialog(this, "Error method cetakLaporanDenganParameter() : " + ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void cetakNota2(){
        try {
            String nama_file="ReportNotaPembayaran.jasper";
            HashMap<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("ParameterIdTransaksi", vid_transaksi);
            File report = new File(nama_file);
            JasperReport jasperReport = (JasperReport)JRLoader.loadObject(report);
            JasperPrint  jasperPrint = JasperFillManager.fillReport(jasperReport,parameter,_Cnn);
            JasperViewer.viewReport(jasperPrint,false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);
        } catch (JRException ex) {
            Logger.getLogger(IfrLaporanSiswa.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
    private void getDataPembayaran(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "SELECT * FROM tbpembayaran a, tbsiswa b WHERE a.nis=b.nis and id_transaksi='"+vid_transaksi+"'";;
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);

        } catch (SQLException ex){
            JOptionPane.showMessageDialog(this, "Error method getDataPembayaran():" +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
        
    
    private void cariPembayaran(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelPembayaran();
        sqlselect = "SELECT * FROM tbpembayaran a, tbsiswa b WHERE a.nis=b.nis and a.nis like '%"+txtCari.getText()+"%' order by a.nis asc";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vid_transaksi= res.getString("id_transaksi");
            vnis = res.getString("nis");
            vnama_siswa = res.getString("nama_siswa");
            vtgl_bayar = res.getString("tgl_bayar");
            vbulan_bayar_dari = res.getString("bulan_bayar_dari");
            vbulan_bayar_sampai = res.getString("bulan_bayar_sampai");
            vtahun_bayar = res.getString("tahun_bayar");
            Object [] data = {vid_transaksi,vnis, vnama_siswa, vtgl_bayar, vbulan_bayar_dari, vbulan_bayar_sampai, vtahun_bayar };
            tblpembayaran.addRow(data);
        }
            lbRecord.setText("Record : " + tbPembayaran.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method showDataNilaiSiswa() : " + ex );
         }
    }
       
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jdCetakNotaPembayaran = new javax.swing.JDialog();
        jLabel11 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnCetakKwitansi = new javax.swing.JButton();
        btnSimpan1 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
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
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        jdCetakNotaPembayaran.setTitle("Form Input Data Pembayaran SPP");
        jdCetakNotaPembayaran.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jdCetakNotaPembayaran.setResizable(false);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Form Cetak Nota Pembayaran SPP");

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Input Data :"));
        jPanel8.setOpaque(false);

        btnCetakKwitansi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/aktif-small.png"))); // NOI18N
        btnCetakKwitansi.setText("Cetak Kwitansi");
        btnCetakKwitansi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakKwitansiActionPerformed(evt);
            }
        });

        btnSimpan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/small_logout.png"))); // NOI18N
        btnSimpan1.setText("Tutup");
        btnSimpan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpan1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(btnCetakKwitansi)
                .addGap(62, 62, 62)
                .addComponent(btnSimpan1)
                .addContainerGap(100, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCetakKwitansi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSimpan1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43))
        );

        jLabel17.setText("Form ini digunakan untuk mencetak nota pembayaran SPP");

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/small-very-logo.png"))); // NOI18N

        javax.swing.GroupLayout jdCetakNotaPembayaranLayout = new javax.swing.GroupLayout(jdCetakNotaPembayaran.getContentPane());
        jdCetakNotaPembayaran.getContentPane().setLayout(jdCetakNotaPembayaranLayout);
        jdCetakNotaPembayaranLayout.setHorizontalGroup(
            jdCetakNotaPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdCetakNotaPembayaranLayout.createSequentialGroup()
                .addGroup(jdCetakNotaPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jdCetakNotaPembayaranLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jdCetakNotaPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel17)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jdCetakNotaPembayaranLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jdCetakNotaPembayaranLayout.setVerticalGroup(
            jdCetakNotaPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdCetakNotaPembayaranLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdCetakNotaPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdCetakNotaPembayaranLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17))
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setClosable(true);
        setTitle("Cetak Nota Pembayaran SPP");
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
                .addGap(288, 288, 288)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHapus2)
                .addContainerGap(274, Short.MAX_VALUE))
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
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Transaksi", "NIS", "Nama Siswa", "Tanggal Bayar", "Bayar dari Bulan", "Bayar sampai Bulan", "Tahun Bayar"
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
            tbPembayaran.getColumnModel().getColumn(6).setResizable(false);
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

        jLabel1.setText("Langkah-langkah menyetak Nota Pembayaran SPP");

        jLabel3.setText("1. Cari NIS");

        jLabel4.setText("2. Akan tampil hasil pencarian, pilih data yang akan dicetak dengan klik kiri 1x");

        jLabel5.setText("3. Lalu tekan tombol Cetak");

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
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(13, 13, 13)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbRecord)
                .addContainerGap(27, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 276, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 275, Short.MAX_VALUE)))
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
    cariPembayaran();
    }//GEN-LAST:event_btnHapus2ActionPerformed

    private void tbPembayaranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPembayaranMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount()==1){
            vid_transaksi = tbPembayaran.getValueAt(tbPembayaran.getSelectedRow(),0).toString();
        } else if (evt.getClickCount()==2 ){
            jdCetakNotaPembayaran.setVisible(true);
            jdCetakNotaPembayaran.setLocationRelativeTo(this);
            getDataPembayaran();
        }
    }//GEN-LAST:event_tbPembayaranMouseClicked

    private void btnCetakKwitansiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakKwitansiActionPerformed
        // TODO add your handling code here:
        cetakNota2();
    }//GEN-LAST:event_btnCetakKwitansiActionPerformed

    private void btnSimpan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpan1ActionPerformed
        // TODO add your handling code here
        jdCetakNotaPembayaran.setVisible(false);
    }//GEN-LAST:event_btnSimpan1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetakKwitansi;
    private javax.swing.JButton btnHapus1;
    private javax.swing.JButton btnHapus2;
    private javax.swing.JButton btnSimpan1;
    private javax.swing.JButton btnTambah1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JDialog jdCetakNotaPembayaran;
    private javax.swing.JLabel lbRecord;
    private javax.swing.JLabel lbRecord1;
    private javax.swing.JTable tbDataMahasiswa1;
    private javax.swing.JTable tbPembayaran;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtCari1;
    // End of variables declaration//GEN-END:variables
}
