/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import Tool.KoneksiDB;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author RAMA
 */
public class IfrBayarSPP extends javax.swing.JInternalFrame {

    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    private Connection conn = new KoneksiDB().getConnection();
    PreparedStatement pst = null;
    ResultSet res = null;
    String sqlselect, sqlinsert, sqldelete;
    String vid_transaksi, vnis, vid_admin, vtgl_bayar, vbayar_dari_bulan, vbayar_sampai_bulan, vtahun, vtotal_bayar;

    
    SimpleDateFormat tglinput = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat tglview = new SimpleDateFormat("dd-MM-yyyy");
    /**
     * Creates new form IfrBayarSPP
     */
    public IfrBayarSPP() {
        initComponents();
//        autonumber();
        tampil_combo_nama_siswa();
        aturTextField();
    }

//    private void autonumber() {
//        String noNota = "NOTA000";
//        int i = 0;
//        try {
//            //Connection con = _Cnn.koneksiDB();
//            Statement st = conn.createStatement();
//            String sql = "select id_transaksi from tbpembayaran";
//            ResultSet rs = st.executeQuery(sql);
//            while (rs.next()) {
//                noNota = rs.getString("id_transaksi");
//            }
//            noNota = noNota.substring(4);
//            i = Integer.parseInt(noNota) + 1;
//            noNota = "00" + i;
//            noNota = "NOTA" + noNota.substring(noNota.length() - 3);
//            txtIdTransaksi.setText(noNota);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }

    private void tampil_combo_nama_siswa() {
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            String sql = "select nama_siswa from tbsiswa";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sql);
            while (res.next()) {
                String kelas = res.getString("nama_siswa");
                cmbNamaSiswa.addItem(kelas);
            }

        } catch (Exception e) {
        }
    }



    void aturTextField() {
        txtNIS.setEnabled(false);
    }
    
    private void aksiProses(){
        vid_transaksi = "";
        vnis = txtNIS.getText();
        vtgl_bayar = tglinput.format(dtTanggalBayar.getDate());
        vbayar_dari_bulan = cmbBayarDariBulan.getSelectedItem().toString();
        vbayar_sampai_bulan = cmbBayarDariBulan.getSelectedItem().toString();
        vtahun = cmbTahun.getSelectedItem().toString();
        vtotal_bayar = txtTotalBayar.getText();
                  
            try {
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "INSERT INTO tbnilai values (?,?,?,?,?,?,?)";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, null);
                    stat.setString(2, vnis);
                    stat.setString(3, vtgl_bayar);
                    stat.setString(4, vbayar_dari_bulan); 
                    stat.setString(5, vbayar_sampai_bulan);
                    stat.setString(6, vtahun); 
                    stat.setString(7, vtotal_bayar);
                    stat.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Proses pembayaran selesai",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);                                  
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiProsesPembayaran () : " +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE );
                            
               }
            }
    
        
    

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        cmbNamaSiswa = new javax.swing.JComboBox<>();
        txtNIS = new javax.swing.JTextField();
        cmbBayarDariBulan = new javax.swing.JComboBox<>();
        cmbTahun = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        dtTanggalBayar = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        sss = new javax.swing.JLabel();
        txtKodeBulan1 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmbBayarSampaiBulan = new javax.swing.JComboBox<>();
        txtKodeBulan2 = new javax.swing.JTextField();
        btnHitungBiaya = new javax.swing.JButton();
        txtTotalBayar = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnProses = new javax.swing.JButton();
        btnCetakKwitansi = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Form Pembayaran SPP");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Input Data Transaksi"));
        jPanel4.setOpaque(false);

        cmbNamaSiswa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih" }));
        cmbNamaSiswa.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Siswa :"));
        cmbNamaSiswa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbNamaSiswaItemStateChanged(evt);
            }
        });
        cmbNamaSiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbNamaSiswaActionPerformed(evt);
            }
        });

        txtNIS.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "NIS :"));
        txtNIS.setOpaque(false);

        cmbBayarDariBulan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih", "Januari 2020", "Februari 2020", "Maret 2020", "April 2020", "Mei 2020", "Juni 2020", "Juli 2020", "Agustus 2020", "September 2020", "Oktober 2020", "November 2020", "Desember 2020", "Januari 2021", "Februari 2021", "Maret 2021", "April 2021", "Mei 2021", "Juni 2021", "Juli 2021", "Agustus 2021", "September 2021", "Oktober 2021", "November 2021", "Desember 2021" }));
        cmbBayarDariBulan.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Bayar Dari Bulan:"));
        cmbBayarDariBulan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBayarDariBulanItemStateChanged(evt);
            }
        });
        cmbBayarDariBulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBayarDariBulanActionPerformed(evt);
            }
        });

        cmbTahun.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2020", "2021" }));
        cmbTahun.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tahun :"));
        cmbTahun.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTahunItemStateChanged(evt);
            }
        });
        cmbTahun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTahunActionPerformed(evt);
            }
        });

        jLabel8.setText("# Di isikan otomatis");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("Tanggal Bayar");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("Total Bayar :");

        sss.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        sss.setText("Rp.");

        txtKodeBulan1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Kode Bulan :"));
        txtKodeBulan1.setOpaque(false);

        jLabel10.setText("# Di isikan otomatis");

        jLabel11.setText("# Di isikan otomatis");

        cmbBayarSampaiBulan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih", "Januari 2020", "Februari 2020", "Maret 2020", "April 2020", "Mei 2020", "Juni 2020", "Juli 2020", "Agustus 2020", "September 2020", "Oktober 2020", "November 2020", "Desember 2020", "Januari 2021", "Februari 2021", "Maret 2021", "April 2021", "Mei 2021", "Juni 2021", "Juli 2021", "Agustus 2021", "September 2021", "Oktober 2021", "November 2021", "Desember 2021" }));
        cmbBayarSampaiBulan.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Bayar Sampai Bulan:"));
        cmbBayarSampaiBulan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbBayarSampaiBulanItemStateChanged(evt);
            }
        });
        cmbBayarSampaiBulan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBayarSampaiBulanActionPerformed(evt);
            }
        });

        txtKodeBulan2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Kode Bulan :"));
        txtKodeBulan2.setOpaque(false);

        btnHitungBiaya.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/save-black.png"))); // NOI18N
        btnHitungBiaya.setText("Hitung Biaya");
        btnHitungBiaya.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHitungBiayaActionPerformed(evt);
            }
        });

        txtTotalBayar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtTotalBayar.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(sss)
                                .addGap(18, 18, 18)
                                .addComponent(txtTotalBayar))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel12)
                                        .addGap(18, 18, 18)
                                        .addComponent(dtTanggalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmbBayarDariBulan, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbBayarSampaiBulan, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtKodeBulan1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtKodeBulan2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11))))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(cmbTahun, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnHitungBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(cmbNamaSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNIS, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbNamaSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNIS, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(dtTanggalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbBayarDariBulan, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtKodeBulan1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbBayarSampaiBulan, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtKodeBulan2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel11)
                        .addGap(43, 43, 43)
                        .addComponent(jLabel10)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbTahun, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHitungBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(sss)
                    .addComponent(txtTotalBayar))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Navigasi"));

        btnProses.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/save-black.png"))); // NOI18N
        btnProses.setText("Proses");
        btnProses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProsesActionPerformed(evt);
            }
        });

        btnCetakKwitansi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/aktif-small.png"))); // NOI18N
        btnCetakKwitansi.setText("Cetak Kwitansi");
        btnCetakKwitansi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakKwitansiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnProses)
                .addGap(36, 36, 36)
                .addComponent(btnCetakKwitansi)
                .addGap(123, 123, 123))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnProses, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCetakKwitansi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/small-very-logo.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Form Entry Pembayaran SPP");

        jLabel5.setText("Form ini digunakan untuk transaksi pembayaran SPP");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(0, 193, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbNamaSiswaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbNamaSiswaItemStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_cmbNamaSiswaItemStateChanged

    private void cmbNamaSiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbNamaSiswaActionPerformed
        // TODO add your handling code here:
        String tmp = (String) cmbNamaSiswa.getSelectedItem();
        String sql = "SELECT * FROM tbsiswa WHERE nama_siswa = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, tmp);
            res = pst.executeQuery();
            if (res.next()) {
                String add = res.getString("nis");
                txtNIS.setText(add);

            } else {
                txtNIS.setText("");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Data gagal ditampilkan!");
        }
    }//GEN-LAST:event_cmbNamaSiswaActionPerformed

    private void cmbBayarDariBulanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBayarDariBulanItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbBayarDariBulanItemStateChanged

    private void cmbBayarDariBulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBayarDariBulanActionPerformed
        // TODO add your handling code here:
        if (cmbBayarDariBulan.getSelectedItem().equals("Januari 2020")) {
            txtKodeBulan1.setText("1");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Februari 2020")) {
            txtKodeBulan1.setText("2");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Maret 2020")) {
            txtKodeBulan1.setText("3");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("April 2020")) {
            txtKodeBulan1.setText("4");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Mei 2020")) {
            txtKodeBulan1.setText("5");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Juni 2020")) {
            txtKodeBulan1.setText("6");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Juli 2020")) {
            txtKodeBulan1.setText("7");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Agustus 2020")) {
            txtKodeBulan1.setText("8");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("September 2020")) {
            txtKodeBulan1.setText("9");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Oktober 2020")) {
            txtKodeBulan1.setText("10");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("November 2020")) {
            txtKodeBulan1.setText("11");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Desember 2020")) {
            txtKodeBulan1.setText("12");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Januari 2021")) {
            txtKodeBulan1.setText("13");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Februari 2021")) {
            txtKodeBulan1.setText("14");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Maret 2021")) {
            txtKodeBulan1.setText("15");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("April 2021")) {
            txtKodeBulan1.setText("16");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Mei 2021")) {
            txtKodeBulan1.setText("17");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Juni 2021")) {
            txtKodeBulan1.setText("18");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Juli 2021")) {
            txtKodeBulan1.setText("19");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Agustus 2021")) {
            txtKodeBulan1.setText("20");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("September 2021")) {
            txtKodeBulan1.setText("21");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Oktober 2021")) {
            txtKodeBulan1.setText("22");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("November 2021")) {
            txtKodeBulan1.setText("23");
        } else if (cmbBayarDariBulan.getSelectedItem().equals("Desember 2021")) {
            txtKodeBulan1.setText("24");
        }
    }//GEN-LAST:event_cmbBayarDariBulanActionPerformed

    private void cmbTahunItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTahunItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTahunItemStateChanged

    private void cmbTahunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTahunActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_cmbTahunActionPerformed

    private void btnProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProsesActionPerformed
aksiProses();
    }//GEN-LAST:event_btnProsesActionPerformed

    private void btnCetakKwitansiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakKwitansiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCetakKwitansiActionPerformed

    private void cmbBayarSampaiBulanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbBayarSampaiBulanItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbBayarSampaiBulanItemStateChanged

    private void cmbBayarSampaiBulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBayarSampaiBulanActionPerformed
        // TODO add your handling code here:
        if (cmbBayarSampaiBulan.getSelectedItem().equals("Januari 2020")) {
            txtKodeBulan2.setText("1");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Februari 2020")) {
            txtKodeBulan2.setText("2");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Maret 2020")) {
            txtKodeBulan2.setText("3");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("April 2020")) {
            txtKodeBulan2.setText("4");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Mei 2020")) {
            txtKodeBulan2.setText("5");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Juni 2020")) {
            txtKodeBulan2.setText("6");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Juli 2020")) {
            txtKodeBulan2.setText("7");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Agustus 2020")) {
            txtKodeBulan2.setText("8");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("September 2020")) {
            txtKodeBulan2.setText("9");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Oktober 2020")) {
            txtKodeBulan2.setText("10");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("November 2020")) {
            txtKodeBulan2.setText("11");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Desember 2020")) {
            txtKodeBulan2.setText("12");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Januari 2021")) {
            txtKodeBulan2.setText("13");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Februari 2021")) {
            txtKodeBulan2.setText("14");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Maret 2021")) {
            txtKodeBulan2.setText("15");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("April 2021")) {
            txtKodeBulan2.setText("16");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Mei 2021")) {
            txtKodeBulan2.setText("17");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Juni 2021")) {
            txtKodeBulan2.setText("18");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Juli 2021")) {
            txtKodeBulan2.setText("19");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Agustus 2021")) {
            txtKodeBulan2.setText("20");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("September 2021")) {
            txtKodeBulan2.setText("21");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Oktober 2021")) {
            txtKodeBulan2.setText("22");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("November 2021")) {
            txtKodeBulan2.setText("23");
        } else if (cmbBayarSampaiBulan.getSelectedItem().equals("Desember 2021")) {
            txtKodeBulan2.setText("24");
        }


    }//GEN-LAST:event_cmbBayarSampaiBulanActionPerformed

    private void btnHitungBiayaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHitungBiayaActionPerformed
        // TODO add your handling code here:
        String vkodebulan1, vkodebulan2;
        int vtotal, vtotal_bayar;
        vkodebulan1 = txtKodeBulan1.getText();
        vkodebulan2 = txtKodeBulan2.getText();
        int vkodebulan1Int = Integer.parseInt(vkodebulan1);
        int vkodebulan2Int = Integer.parseInt(vkodebulan2);
        if (vkodebulan2Int - vkodebulan1Int == 0){
            sss.setText("Rp.150.000");
        } else {
            vtotal = (vkodebulan2Int - vkodebulan1Int)+1;
            vtotal_bayar = vtotal * 150000;
            sss.setText("Rp." + vtotal_bayar);
        }
    }//GEN-LAST:event_btnHitungBiayaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetakKwitansi;
    private javax.swing.JButton btnHitungBiaya;
    private javax.swing.JButton btnProses;
    private javax.swing.JComboBox<String> cmbBayarDariBulan;
    private javax.swing.JComboBox<String> cmbBayarSampaiBulan;
    private javax.swing.JComboBox<String> cmbNamaSiswa;
    private javax.swing.JComboBox<String> cmbTahun;
    private com.toedter.calendar.JDateChooser dtTanggalBayar;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel sss;
    private javax.swing.JTextField txtKodeBulan1;
    private javax.swing.JTextField txtKodeBulan2;
    private javax.swing.JTextField txtNIS;
    private javax.swing.JLabel txtTotalBayar;
    // End of variables declaration//GEN-END:variables

}
