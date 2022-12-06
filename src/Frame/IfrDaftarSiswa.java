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
public class IfrDaftarSiswa extends javax.swing.JInternalFrame {
    
    

    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    private Connection conn = new KoneksiDB().getConnection();
    PreparedStatement pst = null;
    ResultSet res = null;
    String sqlselect, sqlinsert, sqldelete;
    String vnis, vnisn, vnama_siswa, vnama_panggilan, vjk, vtmp_lahir, vtgl_lahir,valamat,vnama_ayah, vnama_ibu,vfoto, vkelas;
    File imageFile = null;
    
    private DefaultTableModel tblsiswa; 
    SimpleDateFormat tglinput = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat tglview = new SimpleDateFormat("dd-MM-yyyy");
    
    public IfrDaftarSiswa() throws SQLException {
        initComponents();
        clearInput();
        setTabelSiswa();
        tampil_combo_kelas();
        showDataSiswa();
        aturTextField();
        jdInputSiswa.setSize(638, 580);
        jdInputSiswa.setLocationRelativeTo(this);
       
    }
    
    private void clearInput(){
        txtNim.setText("");
        txtNisn.setText("");
        txtNamaSiswa.setText("");
        txtNamaPanggilan.setText("");
        txtTempatLahir.setText("");
        dtTanggalLahir.setDate(new Date());
        cmbJenisKelamin.setSelectedIndex(0);
        txtNamaAyah.setText("");
        txtNamaIbu.setText("");
        txtAlamat.setText("");
        cmbkelas.setSelectedIndex(0);
        lbFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/no-picture.jpg")));
        imageFile = null;
        btnSimpan.setText("Simpan");
        vnis="";
        
    }
    private void setTabelSiswa(){
        String [] kolom1 = {"NIS", "NISN","Nama Siswa", "Tempat Lahir", "Tanggal Lahir", "L/P", "Alamat","Kelas"};
        tblsiswa = new DefaultTableModel(null, kolom1) {
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
                int cola = tblsiswa.getColumnCount();
                return (col < cola ) ? false : true;
            }
        };
        tbsiswa.setModel(tblsiswa);
        tbsiswa.getColumnModel().getColumn(0).setPreferredWidth(75);
        tbsiswa.getColumnModel().getColumn(1).setPreferredWidth(75);
        tbsiswa.getColumnModel().getColumn(2).setPreferredWidth(150);
        tbsiswa.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbsiswa.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbsiswa.getColumnModel().getColumn(5).setPreferredWidth(75);      
        tbsiswa.getColumnModel().getColumn(6).setPreferredWidth(200);
    }
    
    private void clearTabelSiswa(){
        int row = tblsiswa.getRowCount();
        for (int i = 0; i < row; i++){
            tblsiswa.removeRow(0);
        }
    }
    
    private void showDataSiswa() throws SQLException{
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelSiswa();
        sqlselect = "select a.nis, a.nisn, a.nama_siswa, a.nama_panggilan_siswa, a.jenis_kelamin_siswa, a.tempat_lahir_siswa, a.tanggal_lahir_siswa, a.alamat_siswa , b.nama_kelas from tbsiswa a inner join tbkelas b where a.kode_kelas = b.kode_kelas;";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vnis = res.getString("nis");
            vnisn = res.getString("nisn");
            vnama_siswa = res.getString("nama_siswa");
            vnama_panggilan = res.getString("nama_panggilan_siswa");
            vjk = res.getString("jenis_kelamin_siswa");
            vtmp_lahir = res.getString("tempat_lahir_siswa");
            vtgl_lahir = tglview.format(res.getDate("tanggal_lahir_siswa"));
            valamat = res.getString("alamat_siswa");
            vkelas = res.getString("nama_kelas");
            Object [] data = {vnis, vnisn, vnama_siswa, vtmp_lahir, vtgl_lahir, vjk,valamat, vkelas};
            tblsiswa.addRow(data);
        }
            lbRecord.setText("Record : " + tbsiswa.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method showDataSiswa() : " + ex );
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
     
     void aturTextField() {
    txtKodeKelas.setEnabled(false);
}

    
    
    private void cariNamaSiswa(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelSiswa();
        sqlselect = "SELECT * FROM tbsiswa a, tbkelas b "
                    + "WHERE a.kode_kelas=b.kode_kelas and nis like '%"+txtCari.getText()+"%' order by nis asc";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vnis = res.getString("nis");
            vnisn = res.getString("nisn");
            vnama_siswa = res.getString("nama_siswa");
            vnama_panggilan = res.getString("nama_panggilan_siswa");
            vjk = res.getString("jenis_kelamin_siswa");
            vtmp_lahir = res.getString("tempat_lahir_siswa");
            vtgl_lahir = tglview.format(res.getDate("tanggal_lahir_siswa"));
            valamat = res.getString("alamat_siswa");
            vkelas = res.getString("nama_kelas");
            Object [] data = {vnis, vnisn, vnama_siswa, vtmp_lahir, vtgl_lahir, vjk,valamat, vkelas };
            tblsiswa.addRow(data);
        }
            lbRecord.setText("Record : " + tbsiswa.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method showDataSiswa() : " + ex );
         }
    }
    
    
    
    private void getDataSiswa(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "SELECT * FROM tbsiswa a, tbkelas b WHERE a.kode_kelas=b.kode_kelas and nis='"+vnis+"'";;
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.first()){
                txtNisn.setText(res.getString("nisn"));
                txtNamaSiswa.setText(res.getString("nama_siswa"));
                txtNamaPanggilan.setText(res.getString("nama_panggilan_siswa"));
                
                vjk = res.getString("jenis_kelamin_siswa");
                if (vjk.equals("L")){
                    cmbJenisKelamin.setSelectedItem("Laki-laki");
                } else {
                    cmbJenisKelamin.setSelectedItem("Perempuan");
                }
                
                cmbkelas.setSelectedItem(res.getString("nama_kelas"));               
                txtTempatLahir.setText(res.getString("tempat_lahir_siswa"));
                dtTanggalLahir.setDate(res.getDate("tanggal_lahir_siswa"));
                txtNamaAyah.setText(res.getString("nama_ayah"));
                txtNamaIbu.setText(res.getString("nama_ibu"));
                txtAlamat.setText(res.getString("alamat_siswa"));
                
                
                if (res.getBlob("foto").getBinaryStream() == null ){
                    lbFoto.setIcon(new javax.swing.ImageIcon(getClass().
                            getResource("/Image/no-picture.jpg")));
                } else {
                    InputStream is = res.getBlob("foto").getBinaryStream();
                        ImageIcon icon = new ImageIcon(resize(ImageIO.read(is),
                                lbFoto.getWidth(), lbFoto.getHeight()));
                        lbFoto.setIcon(icon);
                }
            }
        } catch (SQLException ex){
            JOptionPane.showMessageDialog(this, "Error method getDataMahasiswa():" +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex){
            Logger.getLogger(IfrDaftarSiswa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
   String [] KeyKelas;
   private void listKelas() throws SQLException{
       try {
           _Cnn = null;
           _Cnn = getCnn.getConnection();
           sqlselect = "SELECT * FROM tbkelas order by kode_kelas asc";
           Statement stat = _Cnn.createStatement();
           ResultSet res = stat.executeQuery(sqlselect);
           cmbkelas.removeAllItems();
           cmbkelas.repaint();
           cmbkelas.addItem("--Pilih--");
           int i = 1;
           while(res.next()){
               cmbkelas.addItem(res.getString(2));
               i++;
           }
           res.first();
           KeyKelas = new String[i+1];
           for (Integer x = 1; x < 1; x++){
               KeyKelas[x] = res.getString(1);
               res.next();
           }
       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(this, "Error method listKelas() : "
                + ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
       }
   
   }
    
    
    private void aksiSimpan(){
        vnis = txtNim.getText();        
        vnisn = txtNisn.getText();
        vnama_siswa = txtNamaSiswa.getText();
        vnama_panggilan = txtNamaPanggilan.getText();
        vjk = cmbJenisKelamin.getSelectedItem().toString();
        vkelas = txtKodeKelas.getText();
        vtmp_lahir = txtTempatLahir.getText();
        vtgl_lahir = tglinput.format(dtTanggalLahir.getDate());
        vnama_ayah = txtNamaAyah.getText();
        vnama_ibu = txtNamaIbu.getText();
        valamat = txtAlamat.getText();
        
        if(btnSimpan.getText().equals("Simpan")){
            
            if (imageFile == null ) {
                JOptionPane.showMessageDialog(this, "Harap masukan poto siswa ! " );
                jdInputSiswa.setVisible(true);
            } else {
                try {
                    InputStream is = new FileInputStream(imageFile);
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "INSERT INTO tbsiswa values (?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vnis);
                    stat.setString(2, vnisn);
                    stat.setString(3, vnama_siswa);
                    stat.setString(4, vnama_panggilan); 
                    stat.setString(5, vtmp_lahir);
                    stat.setString(6, vtgl_lahir);                 
                    stat.setString(7, vjk);
                    stat.setString(8, vnama_ayah);
                    stat.setString(9, vnama_ibu); 
                    stat.setString(10, valamat);
                    stat.setString(11, vkelas);
                    stat.setBlob(12, is);
                    stat.executeUpdate();
                    clearInput();showDataSiswa();
                    jdInputSiswa.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);                                  
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan 2 () : " +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE );
                            
               } catch (FileNotFoundException ex) {
                    Logger.getLogger(IfrDaftarSiswa.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } else {
            if (imageFile == null){
                try {
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "update tbsiswa set nisn = ?, nama_siswa = ?, nama_panggilan_siswa = ?,  tempat_lahir_siswa = ? ,tanggal_lahir_siswa = ?, jenis_kelamin_siswa = ?, nama_ayah = ?,nama_ibu = ?, alamat_siswa = ? , kode_kelas=? where nis = '"+vnis+"'";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vnisn);
                    stat.setString(2, vnama_siswa);
                    stat.setString(3, vnama_panggilan); 
                    stat.setString(4, vtmp_lahir);
                    stat.setString(5, vtgl_lahir);                 
                    stat.setString(6, vjk);
                    stat.setString(7, vnama_ayah);
                    stat.setString(8, vnama_ibu); 
                    stat.setString(9, valamat);
                    stat.setString(10, vkelas);
                    stat.executeUpdate();
                    clearInput();showDataSiswa();jdInputSiswa.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);                      
                    }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan 3 () : " +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE );
                            
               }
            } else {
                try {
                    InputStream is = new FileInputStream(imageFile);
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "update tbsiswa set nisn = ?, nama_siswa = ?, nama_panggilan_siswa = ?,  tempat_lahir_siswa = ? ,tanggal_lahir_siswa = ?, jenis_kelamin_siswa = ?, nama_ayah = ?,nama_ibu = ?, alamat_siswa = ? , kode_kelas=? , foto=? where nis = '"+vnis+"'";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vnisn);
                    stat.setString(2, vnama_siswa);
                    stat.setString(3, vnama_panggilan); 
                    stat.setString(4, vtmp_lahir);
                    stat.setString(5, vtgl_lahir);                 
                    stat.setString(6, vjk);
                    stat.setString(7, vnama_ayah);
                    stat.setString(8, vnama_ibu); 
                    stat.setString(9, valamat);
                    stat.setString(10, vkelas);
                    stat.setBlob(11, is);
                    stat.executeUpdate();
                    clearInput();
                    showDataSiswa();
                    jdInputSiswa.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil diubah",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);                      
                    }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan 4 () : " +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE );
                            
               } catch (FileNotFoundException ex) {
                    Logger.getLogger(IfrDaftarSiswa.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    private void aksiHapus(){
        int jawab = JOptionPane.showConfirmDialog(this, 
                "Apakah anda yakin akan menghapus adata ini ? NIS : " + vnis,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (jawab == JOptionPane.YES_OPTION){
            try {
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                sqldelete = "DELETE FROM tbsiswa "
                        + "WHERE nis='"+vnis+"'";
                Statement stat = _Cnn.createStatement();
                stat.executeUpdate(sqldelete);
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                clearInput();
                showDataSiswa();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error method aksiHapus() : " +ex,
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    }
    
    public static BufferedImage loadImage (String ref) {
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new File(ref));
        } catch (IOException e) {          
        }
        return bimg;
    }
    
    public static BufferedImage resize(BufferedImage img, int newW, int newH){
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;                
    }
    
    private void ambilGambar(){
        javax.swing.JFileChooser jfc = new JFileChooser();
        FileFilter jpgFilter, gifFilter, bothFilter;
        jpgFilter = new FileNameExtensionFilter("Gambar JPEG", "jpg");
        gifFilter = new FileNameExtensionFilter("Gambar GIF", "gif");
        bothFilter = new FileNameExtensionFilter("Gambar JPEG dan GIF", "jpg","gif");
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.addChoosableFileFilter(jpgFilter);
        jfc.addChoosableFileFilter(gifFilter);
        jfc.addChoosableFileFilter(bothFilter);
        if (jfc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
            vfoto = jfc.getSelectedFile().toString();
            imageFile = jfc.getSelectedFile();
            BufferedImage loadImg = loadImage(vfoto);
            ImageIcon imageIcon = new ImageIcon(resize(loadImg, lbFoto.getWidth(), lbFoto.getHeight()));
        }
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jdInputSiswa = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lbFoto = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        txtNim = new javax.swing.JTextField();
        txtNamaSiswa = new javax.swing.JTextField();
        cmbJenisKelamin = new javax.swing.JComboBox<>();
        txtTempatLahir = new javax.swing.JTextField();
        dtTanggalLahir = new com.toedter.calendar.JDateChooser();
        txtNamaAyah = new javax.swing.JTextField();
        txtNamaIbu = new javax.swing.JTextField();
        aa = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JEditorPane();
        cmbkelas = new javax.swing.JComboBox<>();
        txtNamaPanggilan = new javax.swing.JTextField();
        txtNisn = new javax.swing.JTextField();
        txtKodeKelas = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnHapus2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbsiswa = new javax.swing.JTable();
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

        jdInputSiswa.setTitle("Form Input Data Siswa");
        jdInputSiswa.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jdInputSiswa.setResizable(false);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/small-very-logo.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Form Entry Data Siswa");

        jLabel5.setText("Form ini digunakan untuk menginput data siswa");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Poto"));

        lbFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/no-picture.jpg"))); // NOI18N
        lbFoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbFotoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbFoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbFoto)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(200, Short.MAX_VALUE)
                .addComponent(btnSimpan)
                .addGap(18, 18, 18)
                .addComponent(btnBatal)
                .addGap(226, 226, 226))
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

        txtNim.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "NIS :"));
        txtNim.setOpaque(false);

        txtNamaSiswa.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Siswa :"));
        txtNamaSiswa.setOpaque(false);

        cmbJenisKelamin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --", "Laki-laki", "Perempuan" }));
        cmbJenisKelamin.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Jenis Kelamin :"));
        cmbJenisKelamin.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbJenisKelaminItemStateChanged(evt);
            }
        });
        cmbJenisKelamin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbJenisKelaminActionPerformed(evt);
            }
        });

        txtTempatLahir.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tempat :"));
        txtTempatLahir.setOpaque(false);

        dtTanggalLahir.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tanggal Lahir"));
        dtTanggalLahir.setOpaque(false);

        txtNamaAyah.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Ayah :"));
        txtNamaAyah.setOpaque(false);

        txtNamaIbu.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Ibu :"));
        txtNamaIbu.setOpaque(false);

        aa.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Alamat :"));
        aa.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        aa.setOpaque(false);
        aa.setViewportView(txtAlamat);

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

        txtNamaPanggilan.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Panggilan :"));
        txtNamaPanggilan.setOpaque(false);

        txtNisn.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "NISN :"));
        txtNisn.setOpaque(false);

        txtKodeKelas.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Kode Kelas :"));
        txtKodeKelas.setOpaque(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtNamaAyah, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNamaIbu))
                    .addComponent(aa)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtTempatLahir, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dtTanggalLahir, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtNim, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNamaSiswa, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbJenisKelamin, javax.swing.GroupLayout.Alignment.LEADING, 0, 193, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNamaPanggilan)
                            .addComponent(txtNisn)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(cmbkelas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtKodeKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNisn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNamaSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaPanggilan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cmbkelas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbJenisKelamin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtKodeKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTempatLahir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dtTanggalLahir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNamaAyah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNamaIbu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aa, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jdInputSiswaLayout = new javax.swing.GroupLayout(jdInputSiswa.getContentPane());
        jdInputSiswa.getContentPane().setLayout(jdInputSiswaLayout);
        jdInputSiswaLayout.setHorizontalGroup(
            jdInputSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdInputSiswaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdInputSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jdInputSiswaLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jdInputSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)))
                    .addGroup(jdInputSiswaLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jdInputSiswaLayout.setVerticalGroup(
            jdInputSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdInputSiswaLayout.createSequentialGroup()
                .addGroup(jdInputSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdInputSiswaLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jdInputSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setClosable(true);
        setTitle("Form Data Siswa");
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
        jLabel2.setText("Cari NIS Siswa");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 304, Short.MAX_VALUE)
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

        tbsiswa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NIS", "NISN", "Nama Siswa", "Tempat Lahir", "Tanggal Lahir", "L/P", "Alamat", "Kelas"
            }
        ));
        tbsiswa.setRowHeight(25);
        tbsiswa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbsiswaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbsiswa);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbRecord)
                .addContainerGap(15, Short.MAX_VALUE))
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
        jdInputSiswa.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        if (vnis.equals("")){
            JOptionPane.showMessageDialog(this, "Anda belum memilih data yang akan dihapus",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            aksiHapus();
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
        //cariNamaSiswa();
    }//GEN-LAST:event_txtCariActionPerformed

    private void tbsiswaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbsiswaMouseClicked
        // TODO add your handling code here:
       if (evt.getClickCount()==1){
           btnHapus.setEnabled(true);
           vnis = tbsiswa.getValueAt(tbsiswa.getSelectedRow(),0).toString();
           txtNim.setText(vnis);
       } else if (evt.getClickCount()==2 ){
           btnSimpan.setText("Ubah");
           jdInputSiswa.setVisible(true);
           jdInputSiswa.setLocationRelativeTo(this);
           getDataSiswa();
       }
    }//GEN-LAST:event_tbsiswaMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if (txtNim.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Data NIM harus diisi", "Informasi",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (txtNamaSiswa.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Data nama mahasiswa harus diisi",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            aksiSimpan();
            jdInputSiswa.setVisible(false);
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clearInput();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void lbFotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbFotoMouseClicked
       ambilGambar();
    }//GEN-LAST:event_lbFotoMouseClicked

    private void cmbJenisKelaminItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbJenisKelaminItemStateChanged
    if (cmbJenisKelamin.getSelectedItem().equals("Laki-laki")){
           vjk = "L";
       } else {
           vjk = "P";
       }       
    }//GEN-LAST:event_cmbJenisKelaminItemStateChanged

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

    private void cmbJenisKelaminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbJenisKelaminActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbJenisKelaminActionPerformed

    private void btnHapus2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapus2ActionPerformed
        // TODO add your handling code here:
        cariNamaSiswa();
    }//GEN-LAST:event_btnHapus2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane aa;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnHapus1;
    private javax.swing.JButton btnHapus2;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTambah1;
    private javax.swing.JComboBox<String> cmbJenisKelamin;
    private javax.swing.JComboBox<String> cmbkelas;
    private com.toedter.calendar.JDateChooser dtTanggalLahir;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JDialog jdInputSiswa;
    private javax.swing.JLabel lbFoto;
    private javax.swing.JLabel lbRecord;
    private javax.swing.JLabel lbRecord1;
    private javax.swing.JTable tbDataMahasiswa1;
    private javax.swing.JTable tbsiswa;
    private javax.swing.JEditorPane txtAlamat;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtCari1;
    private javax.swing.JTextField txtKodeKelas;
    private javax.swing.JTextField txtNamaAyah;
    private javax.swing.JTextField txtNamaIbu;
    private javax.swing.JTextField txtNamaPanggilan;
    private javax.swing.JTextField txtNamaSiswa;
    private javax.swing.JTextField txtNim;
    private javax.swing.JTextField txtNisn;
    private javax.swing.JTextField txtTempatLahir;
    // End of variables declaration//GEN-END:variables
}
