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
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author RAMA
 */
public class IfrDaftarGuru extends javax.swing.JInternalFrame {
    
    

    KoneksiDB getCnn = new KoneksiDB();
    Connection _Cnn;
    private Connection conn = new KoneksiDB().getConnection();
    PreparedStatement pst = null;
    ResultSet res = null;
    String sqlselect, sqlinsert, sqldelete;
    String vnik, 
           vnama_guru, 
           vtempat_lahir_guru,
           vtanggal_lahir_guru,
           vagama_guru,
           vjenis_kelamin_guru,
           vtelepon_guru,
           vemail_guru,
           valamat_guru,
           vpendidikan_guru,
           vstatus_kawin_guru,
           vjabatan,
           vstatus_aktif_guru,
           vfoto
            ;
    File imageFile = null;
    
    private DefaultTableModel tblguru; 
    SimpleDateFormat tglinput = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat tglview = new SimpleDateFormat("dd-MM-yyyy");
    
    public IfrDaftarGuru() throws SQLException {
        initComponents();
        clearInput();
        setTabelGuru();
        // tampil_combo_kelas();
        showDataGuru();
        jdInputGuru.setSize(638, 634);
        jdInputGuru.setLocationRelativeTo(this);
    }
    
    private void clearInput(){
        txtnik.setText("");
        txtnamaguru.setText("");
        txttempatlahirguru.setText("");
        dtTanggalLahir.setDate(new Date());
        cmbagama.setSelectedIndex(0);
        cmbjeniskelamin.setSelectedIndex(0);
        txtnoteleponguru.setText("");
        txtemailguru.setText("");
        txtalamat.setText("");
        cmbpendidikanguru.setSelectedIndex(0);
        cmbstatuskawinguru.setSelectedIndex(0);
        cmbstatusaktifguru.setSelectedIndex(0);
        txtjabatan.setText("");
        lbFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/no-picture.jpg")));
        imageFile = null;
        btnSimpan.setText("Simpan");
        vnik="";
        
    }
    private void setTabelGuru(){
        String [] kolom1 = {"NIK", "Nama Guru","L/P", "Alamat" , "Tempat Lahir" , "Tanggal Lahir", "Telepon", "Pendidikan", "Jabatan", "Status"};
        tblguru = new DefaultTableModel(null, kolom1) {
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
                java.lang.String.class,
            };
            public Class getColumnClass(int columnIndex){
                return types [columnIndex];
            }
            
            // agar tabel tidak bisa di edit
            public boolean isCellEditable(int row, int col){
                int cola = tblguru.getColumnCount();
                return (col < cola ) ? false : true;
            }
        };
        tbguru.setModel(tblguru);
        tbguru.getColumnModel().getColumn(0).setPreferredWidth(50); 
        tbguru.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbguru.getColumnModel().getColumn(2).setPreferredWidth(25);
        tbguru.getColumnModel().getColumn(3).setPreferredWidth(180);
        tbguru.getColumnModel().getColumn(4).setPreferredWidth(150);
        tbguru.getColumnModel().getColumn(5).setPreferredWidth(100);      
        tbguru.getColumnModel().getColumn(6).setPreferredWidth(100);
        tbguru.getColumnModel().getColumn(7).setPreferredWidth(100);
        tbguru.getColumnModel().getColumn(7).setPreferredWidth(100);
        tbguru.getColumnModel().getColumn(8).setPreferredWidth(100);
        
        
        
    }

    private void clearTabelGuru(){
        int row = tblguru.getRowCount();
        for (int i = 0; i < row; i++){
            tblguru .removeRow(0);
        }
    }
    
    private void showDataGuru() throws SQLException{
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelGuru();
        sqlselect = "select * from tbguru;";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vnik = res.getString("nik");
            vnama_guru = res.getString("nama_guru");
            vjenis_kelamin_guru = res.getString("jenis_kelamin_guru");
            valamat_guru = res.getString("alamat_guru");
            vtempat_lahir_guru = res.getString("tempat_lahir_guru");
            vtanggal_lahir_guru = tglview.format(res.getDate("tanggal_lahir_guru"));
            vtelepon_guru = res.getString("telepon_guru");
            vpendidikan_guru = res.getString("pendidikan_guru");
            vjabatan = res.getString("jabatan");
            vstatus_aktif_guru = res.getString("status_aktif_guru");
            Object [] data = {vnik, vnama_guru, vjenis_kelamin_guru, valamat_guru,vtempat_lahir_guru , vtanggal_lahir_guru, vtelepon_guru, vpendidikan_guru,vjabatan, vstatus_aktif_guru};
            tblguru.addRow(data);
        }
            lbRecord.setText("Record : " + tbguru.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method showDataGuru() : " + ex );
         }
        
    }
    
//     private void tampil_combo_kelas() {
//     try {
//            _Cnn = null;
//            _Cnn = getCnn.getConnection();
//            String sql = "select nama_kelas from tbkelas";
//            Statement stat = _Cnn.createStatement();
//            ResultSet res=stat.executeQuery(sql);
//            while (res.next()) {
//                String kelas  = res.getString("nama_kelas"); 
//                cmbkelas.addItem(kelas);
//            }
//
//        } catch (Exception e) {
//        }
//    }

    
    
   private void cariNamaGuru(){
        try {
        _Cnn = null;
        _Cnn = getCnn.getConnection();
        clearTabelGuru();
        sqlselect = "SELECT * FROM tbguru WHERE nik like '%"+txtCari.getText()+"%' or nama_guru like '%"+txtCari.getText()+"%' order by nik asc";
        
        Statement stat = _Cnn.createStatement();
        ResultSet res = stat.executeQuery(sqlselect);
        while (res.next()){
            vnik = res.getString("nik");
            vnama_guru = res.getString("nama_guru");
            vjenis_kelamin_guru = res.getString("jenis_kelamin_guru");
            valamat_guru = res.getString("alamat_guru");
            vtempat_lahir_guru = res.getString("tempat_lahir_guru");
            vtanggal_lahir_guru = tglview.format(res.getDate("tanggal_lahir_guru"));
            vtelepon_guru = tglview.format(res.getDate("telepon_guru"));
            vpendidikan_guru = res.getString("pendidikan_guru");
            vjabatan = res.getString("jabatan");
            vstatus_aktif_guru = res.getString("status_aktif_guru");
            Object [] data = {vnik, vnama_guru,  valamat_guru,vjenis_kelamin_guru,vtempat_lahir_guru , vtanggal_lahir_guru, vtelepon_guru, vpendidikan_guru,vjabatan, vstatus_aktif_guru};
            tblguru.addRow(data);
        }
            lbRecord.setText("Record : " + tbguru.getRowCount());
            
        } catch (SQLException ex ){
                JOptionPane.showMessageDialog(this, "Error method cariNamaGuru() : " + ex );
         }
    }
    
    
    
    private void getDataGuru(){
        try {
            _Cnn = null;
            _Cnn = getCnn.getConnection();
            sqlselect = "SELECT * FROM tbguru WHERE nik='"+vnik+"'";
            Statement stat = _Cnn.createStatement();
            ResultSet res = stat.executeQuery(sqlselect);
            if (res.first()){
                txtnik.setText(res.getString("nik"));
                txtnamaguru.setText(res.getString("nama_guru"));
                txttempatlahirguru.setText(res.getString("tempat_lahir_guru"));
                dtTanggalLahir.setDate(res.getDate("tanggal_lahir_guru"));
                
                vjenis_kelamin_guru = res.getString("jenis_kelamin_guru");
                if (vjenis_kelamin_guru.equals("Laki-laki")){
                    cmbjeniskelamin.setSelectedItem("Laki-laki");
                } else {
                    cmbjeniskelamin.setSelectedItem("Perempuan");
                }
                
                vagama_guru = res.getString("agama_guru");
                if (vagama_guru.equals("Muslim")){
                    cmbagama.setSelectedItem("Muslim");
                } else {
                    cmbagama.setSelectedItem("Non Muslim");
                }
                
                txtnoteleponguru.setText(res.getString("telepon_guru"));
                txtemailguru.setText(res.getString("email_guru"));
                
                vpendidikan_guru = res.getString("pendidikan_guru");
                if (vpendidikan_guru.equals("SMK/SMA")){
                    cmbpendidikanguru.setSelectedItem("SMK/SMA");
                }  if (vpendidikan_guru.equals("S1")){
                    cmbpendidikanguru.setSelectedItem("S1");
                }  if (vpendidikan_guru.equals("S2")){
                    cmbpendidikanguru.setSelectedItem("S2");
                } else {
                    cmbpendidikanguru.setSelectedItem("S3");
                }
                
                vstatus_kawin_guru = res.getString("status_kawin_guru");
                if (vstatus_kawin_guru.equals("Kawin")){
                    cmbstatuskawinguru.setSelectedItem("Kawin");
                } else {
                    cmbstatuskawinguru.setSelectedItem("Belum Kawin");
                }
                
                vstatus_aktif_guru = res.getString("status_aktif_guru");
                if (vstatus_aktif_guru.equals("Aktif")){
                    cmbstatusaktifguru.setSelectedItem("Aktif");
                } else {
                    cmbstatusaktifguru.setSelectedItem("Tidak Aktif");
                }
 
                txtalamat.setText(res.getString("alamat_guru"));
                txtjabatan.setText(res.getString("jabatan"));
                
                
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
            JOptionPane.showMessageDialog(this, "Error method getDataGuru():" +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex){
            Logger.getLogger(IfrDaftarGuru.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    
    
    private void aksiSimpan(){
        vnik = txtnik.getText();        
        vnama_guru = txtnamaguru.getText();
        vtempat_lahir_guru = txttempatlahirguru.getText();
        vtanggal_lahir_guru = tglinput.format(dtTanggalLahir.getDate());
        vagama_guru = cmbagama.getSelectedItem().toString();
        vjenis_kelamin_guru = cmbjeniskelamin.getSelectedItem().toString();
        vtelepon_guru = txtnoteleponguru.getText();
        vemail_guru = txtemailguru.getText();
        valamat_guru = txtemailguru.getText();
        vpendidikan_guru = cmbpendidikanguru.getSelectedItem().toString();
        vstatus_kawin_guru = cmbstatuskawinguru.getSelectedItem().toString();
        vjabatan = txtjabatan.getText();
        vstatus_aktif_guru = cmbstatusaktifguru.getSelectedItem().toString();
        
        if(btnSimpan.getText().equals("Simpan")){
            
            if (imageFile == null ) {
                JOptionPane.showMessageDialog(this, "Harap masukan poto guru ! " );
                jdInputGuru.setVisible(true);
            } else {
                try {
                    InputStream is = new FileInputStream(imageFile);
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "INSERT INTO tbguru values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vnik);
                    stat.setString(2, vnama_guru);
                    stat.setString(3, vtempat_lahir_guru);
                    stat.setString(4, vtanggal_lahir_guru); 
                    stat.setString(5, vagama_guru);
                    stat.setString(6, vjenis_kelamin_guru);                 
                    stat.setString(7, vtelepon_guru);
                    stat.setString(8, vemail_guru);
                    stat.setString(9, valamat_guru); 
                    stat.setString(10, vpendidikan_guru);
                    stat.setString(11, vstatus_kawin_guru);
                    stat.setString(12, vjabatan);
                    stat.setString(13, vstatus_aktif_guru);
                    stat.setBlob(14, is);
                    stat.executeUpdate();
                    clearInput();showDataGuru();
                    jdInputGuru.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);                                  
                }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan 2 () : " +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE );
                            
               } catch (FileNotFoundException ex) {
                    Logger.getLogger(IfrDaftarGuru.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } else {
            if (imageFile == null){
                try {
                    _Cnn = null;
                    _Cnn = getCnn.getConnection();
                    sqlinsert = "update tbguru set nama_guru = ?, tempat_lahir_guru = ?, tanggal_lahir_guru = ? ,agama_guru = ?, jenis_kelamin_guru = ?, telepon_guru = ?,email_guru = ?, alamat_guru = ? , pendidikan_guru =?, status_kawin_guru =?, jabatan =?, status_aktif_guru =?  where nik = '"+vnik+"'";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vnama_guru);
                    stat.setString(2, vtempat_lahir_guru);
                    stat.setString(3, vtanggal_lahir_guru); 
                    stat.setString(4, vagama_guru);
                    stat.setString(5, vjenis_kelamin_guru);                 
                    stat.setString(6, vtelepon_guru);
                    stat.setString(7, vemail_guru);
                    stat.setString(8, valamat_guru); 
                    stat.setString(9, vpendidikan_guru);
                    stat.setString(10, vstatus_kawin_guru);
                    stat.setString(11, vjabatan);
                    stat.setString(12, vstatus_aktif_guru);
                    stat.executeUpdate();
                    clearInput();showDataGuru();jdInputGuru.setVisible(false);
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
                    sqlinsert = "update tbguru set nama_guru = ?, "
                                + "tempat_lahir_guru = ?,  "
                                + "tanggal_lahir_guru = ? ,"
                                + "agama_guru = ?, "
                                + "jenis_kelamin_guru = ?, "
                                + "telepon_guru = ?, "
                                + "email_guru = ?, "
                                + "alamat_guru = ? , "
                                + "pendidikan_guru =?, "
                                + "status_kawin_guru =?, "
                                + "jabatan =?, "
                                + "status_aktif_guru =?, "
                                + "foto =? "
                                + "where nik = '"+vnik+"'";
                    PreparedStatement stat = _Cnn.prepareStatement(sqlinsert);
                    stat.setString(1, vnama_guru);
                    stat.setString(2, vtempat_lahir_guru);
                    stat.setString(3, vtanggal_lahir_guru); 
                    stat.setString(4, vagama_guru);
                    stat.setString(5, vjenis_kelamin_guru);                 
                    stat.setString(6, vtelepon_guru);
                    stat.setString(7, vemail_guru);
                    stat.setString(8, valamat_guru); 
                    stat.setString(9, vpendidikan_guru);
                    stat.setString(10, vstatus_kawin_guru);
                    stat.setString(11, vjabatan);
                    stat.setString(12, vstatus_aktif_guru);
                    stat.setBlob(13, is);
                    stat.executeUpdate();
                    clearInput();
                    showDataGuru();
                    jdInputGuru.setVisible(false);
                    JOptionPane.showMessageDialog(this, "Data berhasil diubah",
                            "Informasi", JOptionPane.INFORMATION_MESSAGE);                      
                    }catch (SQLException ex){
                    JOptionPane.showMessageDialog(this, "Error method aksiSimpan 4 () : " +ex, "Informasi", JOptionPane.INFORMATION_MESSAGE );
                            
               } catch (FileNotFoundException ex) {
                    Logger.getLogger(IfrDaftarGuru.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    private void aksiHapus(){
        int jawab = JOptionPane.showConfirmDialog(this, 
                "Apakah anda yakin akan menghapus data ini ? NIK : " + vnik,
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (jawab == JOptionPane.YES_OPTION){
            try {
                _Cnn = null;
                _Cnn = getCnn.getConnection();
                sqldelete = "DELETE FROM tbguru "
                        + "WHERE nik='"+vnik+"'";
                Statement stat = _Cnn.createStatement();
                stat.executeUpdate(sqldelete);
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus",
                        "Informasi", JOptionPane.INFORMATION_MESSAGE);
                clearInput();
                showDataGuru();
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

        jdInputGuru = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lbFoto = new javax.swing.JLabel();
        cmbstatusaktifguru = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        txtnik = new javax.swing.JTextField();
        txttempatlahirguru = new javax.swing.JTextField();
        cmbjeniskelamin = new javax.swing.JComboBox<>();
        txtemailguru = new javax.swing.JTextField();
        dtTanggalLahir = new com.toedter.calendar.JDateChooser();
        aa = new javax.swing.JScrollPane();
        txtalamat = new javax.swing.JEditorPane();
        txtnamaguru = new javax.swing.JTextField();
        txtnoteleponguru = new javax.swing.JTextField();
        cmbagama = new javax.swing.JComboBox<>();
        cmbpendidikanguru = new javax.swing.JComboBox<>();
        cmbstatuskawinguru = new javax.swing.JComboBox<>();
        txtjabatan = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnHapus2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbguru = new javax.swing.JTable();
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

        jdInputGuru.setTitle("Input Data Siswa");
        jdInputGuru.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jdInputGuru.setResizable(false);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/small-very-logo.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Form Entry Data Guru");

        jLabel5.setText("Form ini digunakan untuk menginput data guru");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Poto"));

        lbFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/no-picture.jpg"))); // NOI18N
        lbFoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbFotoMouseClicked(evt);
            }
        });

        cmbstatusaktifguru.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --", "Aktif", "Tidak Aktif" }));
        cmbstatusaktifguru.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Status Aktif/Tidak Aktif :"));
        cmbstatusaktifguru.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbstatusaktifguruItemStateChanged(evt);
            }
        });
        cmbstatusaktifguru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbstatusaktifguruActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbFoto, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cmbstatusaktifguru, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbFoto)
                .addGap(18, 18, 18)
                .addComponent(cmbstatusaktifguru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        txtnik.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "NIK :"));
        txtnik.setOpaque(false);

        txttempatlahirguru.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tempat :"));
        txttempatlahirguru.setOpaque(false);

        cmbjeniskelamin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --", "Laki-laki", "Perempuan" }));
        cmbjeniskelamin.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Jenis Kelamin :"));
        cmbjeniskelamin.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbjeniskelaminItemStateChanged(evt);
            }
        });
        cmbjeniskelamin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbjeniskelaminActionPerformed(evt);
            }
        });

        txtemailguru.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Email  :"));
        txtemailguru.setOpaque(false);

        dtTanggalLahir.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tanggal Lahir"));
        dtTanggalLahir.setOpaque(false);

        aa.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Alamat :"));
        aa.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        aa.setOpaque(false);
        aa.setViewportView(txtalamat);

        txtnamaguru.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nama Lengkap :"));
        txtnamaguru.setOpaque(false);

        txtnoteleponguru.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "No. Telepon :"));
        txtnoteleponguru.setOpaque(false);

        cmbagama.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --", "Muslim", "Non Muslim" }));
        cmbagama.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Agama :"));
        cmbagama.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbagamaItemStateChanged(evt);
            }
        });
        cmbagama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbagamaActionPerformed(evt);
            }
        });

        cmbpendidikanguru.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --", "SMK/SMA", "S1", "S2", "S3" }));
        cmbpendidikanguru.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Pendidikan terakhir :"));
        cmbpendidikanguru.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbpendidikanguruItemStateChanged(evt);
            }
        });
        cmbpendidikanguru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbpendidikanguruActionPerformed(evt);
            }
        });

        cmbstatuskawinguru.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --", "Kawin", "Belum Kawin" }));
        cmbstatuskawinguru.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Status Kawin :"));
        cmbstatuskawinguru.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbstatuskawinguruItemStateChanged(evt);
            }
        });
        cmbstatuskawinguru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbstatuskawinguruActionPerformed(evt);
            }
        });

        txtjabatan.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Jabatan :"));
        txtjabatan.setOpaque(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(aa, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txttempatlahirguru, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtnoteleponguru)
                            .addComponent(cmbagama, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(dtTanggalLahir, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtemailguru)
                            .addComponent(cmbjeniskelamin, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtnik, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtnamaguru))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cmbpendidikanguru, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbstatuskawinguru, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(txtjabatan))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnamaguru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dtTanggalLahir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txttempatlahirguru))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbjeniskelamin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbagama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtemailguru, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnoteleponguru, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(aa, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbpendidikanguru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbstatuskawinguru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(txtjabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jdInputGuruLayout = new javax.swing.GroupLayout(jdInputGuru.getContentPane());
        jdInputGuru.getContentPane().setLayout(jdInputGuruLayout);
        jdInputGuruLayout.setHorizontalGroup(
            jdInputGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdInputGuruLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdInputGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jdInputGuruLayout.createSequentialGroup()
                        .addGroup(jdInputGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jdInputGuruLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jdInputGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)))
                            .addGroup(jdInputGuruLayout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jdInputGuruLayout.setVerticalGroup(
            jdInputGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdInputGuruLayout.createSequentialGroup()
                .addGroup(jdInputGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdInputGuruLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jdInputGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setClosable(true);
        setTitle("Form Data Guru");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 369, Short.MAX_VALUE)
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

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Tabel Data Guru : Klik 2x untuk mengubah/meghapus"));

        tbguru.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NIK", "Nama Guru", "Alamat", "L/P", "Tempat Lahir", "Tanggal Lahir", "Telepon", "Pendidikan", "Jabatan", "Status"
            }
        ));
        tbguru.setRowHeight(25);
        tbguru.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbguruMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbguru);

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
                    .addGap(0, 532, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 532, Short.MAX_VALUE)))
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
        jdInputGuru.setVisible(true);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        if (vnik.equals("")){
            JOptionPane.showMessageDialog(this, "Anda belum memilih data yang akan dihapus",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
        aksiHapus();
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
        // TODO add your handling code here:
        cariNamaGuru();
    }//GEN-LAST:event_txtCariActionPerformed

    private void tbguruMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbguruMouseClicked
        // TODO add your handling code here:
       if (evt.getClickCount()==1){
           btnHapus.setEnabled(true);
           vnik = tbguru.getValueAt(tbguru.getSelectedRow(),0).toString();
           txtnik.setText(vnik);
       } else if (evt.getClickCount()==2 ){
           btnSimpan.setText("Ubah");
           jdInputGuru.setVisible(true);
           jdInputGuru.setLocationRelativeTo(this);
            getDataGuru();
       }
    }//GEN-LAST:event_tbguruMouseClicked

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        if (txtnik.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Data NIK harus diisi", "Informasi",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (txttempatlahirguru.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Data nama guru harus diisi",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
        aksiSimpan();
            jdInputGuru.setVisible(false);
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        clearInput();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void lbFotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbFotoMouseClicked
       ambilGambar();
    }//GEN-LAST:event_lbFotoMouseClicked

    private void cmbjeniskelaminItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbjeniskelaminItemStateChanged
//    if (cmbjeniskelamin.getSelectedItem().equals("Laki-laki")){
//           vjenis_kelamin_guru = "L";
//       } else {
//           vjenis_kelamin_guru = "P";
//       }       
    }//GEN-LAST:event_cmbjeniskelaminItemStateChanged

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

    private void cmbjeniskelaminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbjeniskelaminActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbjeniskelaminActionPerformed

    private void btnHapus2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapus2ActionPerformed
        // TODO add your handling code here:
        cariNamaGuru();
    }//GEN-LAST:event_btnHapus2ActionPerformed

    private void cmbagamaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbagamaItemStateChanged
        // TODO add your handling code here:
       if (cmbagama.getSelectedItem().equals("Muslim")){
           vagama_guru = "Muslim";
       } else {
           vagama_guru = "Non Muslim";
       }   
    }//GEN-LAST:event_cmbagamaItemStateChanged

    private void cmbagamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbagamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbagamaActionPerformed

    private void cmbpendidikanguruItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbpendidikanguruItemStateChanged
        // TODO add your handling code here:
       if (cmbpendidikanguru.getSelectedItem().equals("SMA/SMK")){
           vpendidikan_guru = "SMK/SMA";
       } if (cmbpendidikanguru.getSelectedItem().equals("S1")){
           vpendidikan_guru = "S1";
       } if (cmbpendidikanguru.getSelectedItem().equals("S2")){
           vpendidikan_guru = "S2";
       } else {
           vpendidikan_guru = "S3";
       }   
    }//GEN-LAST:event_cmbpendidikanguruItemStateChanged

    private void cmbpendidikanguruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbpendidikanguruActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbpendidikanguruActionPerformed

    private void cmbstatuskawinguruItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbstatuskawinguruItemStateChanged
        // TODO add your handling code here:
       if (cmbstatuskawinguru.getSelectedItem().equals("Kawin")){
           vstatus_kawin_guru = "Kawin";
       } else {
           vstatus_kawin_guru = "Tidak Kawin";
       }   
    }//GEN-LAST:event_cmbstatuskawinguruItemStateChanged

    private void cmbstatuskawinguruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbstatuskawinguruActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbstatuskawinguruActionPerformed

    private void cmbstatusaktifguruItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbstatusaktifguruItemStateChanged
        // TODO add your handling code here:    
       if (cmbstatusaktifguru.getSelectedItem().equals("Aktif")){
           vstatus_aktif_guru = "Aktif";
       } else {
           vstatus_aktif_guru = "Tidak Aktif";
       }   
    }//GEN-LAST:event_cmbstatusaktifguruItemStateChanged

    private void cmbstatusaktifguruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbstatusaktifguruActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbstatusaktifguruActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane aa;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnHapus1;
    private javax.swing.JButton btnHapus2;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTambah1;
    private javax.swing.JComboBox<String> cmbagama;
    private javax.swing.JComboBox<String> cmbjeniskelamin;
    private javax.swing.JComboBox<String> cmbpendidikanguru;
    private javax.swing.JComboBox<String> cmbstatusaktifguru;
    private javax.swing.JComboBox<String> cmbstatuskawinguru;
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
    private javax.swing.JDialog jdInputGuru;
    private javax.swing.JLabel lbFoto;
    private javax.swing.JLabel lbRecord;
    private javax.swing.JLabel lbRecord1;
    private javax.swing.JTable tbDataMahasiswa1;
    private javax.swing.JTable tbguru;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtCari1;
    private javax.swing.JEditorPane txtalamat;
    private javax.swing.JTextField txtemailguru;
    private javax.swing.JTextField txtjabatan;
    private javax.swing.JTextField txtnamaguru;
    private javax.swing.JTextField txtnik;
    private javax.swing.JTextField txtnoteleponguru;
    private javax.swing.JTextField txttempatlahirguru;
    // End of variables declaration//GEN-END:variables
}
