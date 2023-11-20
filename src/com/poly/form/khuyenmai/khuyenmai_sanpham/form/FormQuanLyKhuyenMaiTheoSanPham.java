package com.poly.form.khuyenmai.khuyenmai_sanpham.form;

import com.poly.form.khuyenmai.khuyenmai_sanpham.entity.KhuyenMaiTheoSanPhamRequest;
import com.poly.form.khuyenmai.khuyenmai_sanpham.entity.KhuyenMaiTheoSanPham;
import com.formdev.flatlaf.FlatClientProperties;
import com.poly.form.khuyenmai.khuyenmai_sanpham.repository.KhuyenMaiTheoSanPhamReposirory;
import com.poly.util.ph32148.RandomCodeGenerator;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FormQuanLyKhuyenMaiTheoSanPham extends javax.swing.JPanel {

    private List<KhuyenMaiTheoSanPham> listView = new ArrayList<>();

    private DefaultTableModel defaultTableModel;

    private KhuyenMaiTheoSanPhamReposirory repo = new KhuyenMaiTheoSanPhamReposirory();

    private int currentPage = 1;
    private final int pageSize = 10;

    public FormQuanLyKhuyenMaiTheoSanPham() {
        initComponents();

        lb.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$h1.font");

        try {
            this.init();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cấu hình FormQuanLyKhuyenMaiTheoSanPham");
            Logger.getLogger(FormQuanLyKhuyenMaiTheoSanPham.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void init() throws Exception {
        this.listView = this.repo.getData(currentPage);

        this.defaultTableModel = (DefaultTableModel) tblDanhSachKhuyenMai.getModel();

        this.fillTable(listView);

    }

    public void fillTable(List<KhuyenMaiTheoSanPham> list) {
        this.defaultTableModel.setRowCount(0);
        for (KhuyenMaiTheoSanPham khuyenMaiTheoSanPham : list) {
            this.defaultTableModel.addRow(khuyenMaiTheoSanPham.toRowTable());
        }
    }

    public KhuyenMaiTheoSanPham findKhuyenMaiByMa(String ma, List<KhuyenMaiTheoSanPham> list) {
        KhuyenMaiTheoSanPham khuyenMaiTheoSanPham1 = null;

        for (KhuyenMaiTheoSanPham khuyenMaiTheoSanPham : list) {
            if (khuyenMaiTheoSanPham.getMa().equals(ma)) {
                return khuyenMaiTheoSanPham;// Nếu mã khớp, thêm vào danh sách kết quả
            }
        }
        return khuyenMaiTheoSanPham1;
    }

    public void fillTableToForm(String ma, List<KhuyenMaiTheoSanPham> list) {
        KhuyenMaiTheoSanPham khuyenMaiTheoSanPham = findKhuyenMaiByMa(ma, list);
        this.txtTen.setText(khuyenMaiTheoSanPham.getTen());
        this.jlbMa.setText(khuyenMaiTheoSanPham.getMa());
        this.jslGiaTri.setValue(Integer.valueOf(khuyenMaiTheoSanPham.getGiaTri()));
        if (khuyenMaiTheoSanPham.getTrangThai()) {
            this.rdHoatDong.setSelected(true);
        } else {
            this.rdNgungHoatDong.setSelected(true);
        }

        Date ngayBatDauConvert = new Date(khuyenMaiTheoSanPham.getNgayBatDau() * 1000);
        Date ngayKetThucConvert = new Date(khuyenMaiTheoSanPham.getNgayKetThuc() * 1000);
        Date ngayTaoConvert = new Date(khuyenMaiTheoSanPham.getThoiGianTao() * 1000);
        Date ngaySuaConvert = new Date(khuyenMaiTheoSanPham.getThoiGianSua() * 1000);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

        String ngayBatDauConverted = sdf.format(ngayBatDauConvert);
        String ngayKetThucConverted = sdf.format(ngayKetThucConvert);
        String ngayTaoConverted = sdf.format(ngayTaoConvert);
        String ngaySuaConverted = sdf.format(ngaySuaConvert);

        Calendar calNgayBatDau = Calendar.getInstance();
        calNgayBatDau.setTime(ngayBatDauConvert);

        Calendar calNgayKetThuc = Calendar.getInstance();
        calNgayKetThuc.setTime(ngayKetThucConvert);

        jdcNgayBatDau.setCalendar(calNgayBatDau);
        jdcNgayKetThuc.setCalendar(calNgayKetThuc);

        this.jlbNgayBatDau.setText(ngayBatDauConverted);
        this.jlbNgayKetThuc.setText(ngayKetThucConverted);
        this.jlbNgaySua.setText(ngaySuaConverted);
        this.jlbNgayTao.setText(ngayTaoConverted);
    }

    public KhuyenMaiTheoSanPham readForm(String ma) {
        KhuyenMaiTheoSanPham khuyenMaiTheoSanPham = null;

        try {
            String ten = this.txtTen.getText();
            Integer giaTri = Integer.valueOf(this.jslGiaTri.getValue());

            Calendar ngayKetThucCal = jdcNgayBatDau.getCalendar();
            Calendar ngayBatDauCal = jdcNgayKetThuc.getCalendar();

            Long ngayBatDau = ngayBatDauCal.getTimeInMillis() / 1000;
            Long ngayKetThuc = ngayKetThucCal.getTimeInMillis() / 1000;

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date()); // Đặt thời gian cho ngày hiện tại
            Long ngayTaoVaSua = cal.getTime().getTime() / 1000;

            Boolean trangThai;
            if (this.rdHoatDong.isSelected()) {
                trangThai = true;
            } else {
                trangThai = false;
            }

            return new KhuyenMaiTheoSanPham(null, ma, ten, giaTri, ngayBatDau, ngayKetThuc, ngayTaoVaSua, ngayTaoVaSua, trangThai);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Đọc file lỗi");
            e.printStackTrace();
            return khuyenMaiTheoSanPham;
        }
    }

    public KhuyenMaiTheoSanPhamRequest readFormSearch() {
        KhuyenMaiTheoSanPhamRequest khuyenMaiTheoSanPham = null;

        try {
            String input = this.txtTimKiemMaOrTen.getText();      
            
            Integer giaTri = null;           
            if(!this.txtGiaTriTimKiem.getText().trim().equals("")){
                giaTri = Integer.valueOf(this.txtGiaTriTimKiem.getText());
            }          
            Calendar ngayKetThucCal = jdcNgayKetThucTimKiem.getCalendar();
            Calendar ngayBatDauCal = jdcNgayBatDauTimKiem.getCalendar();

            Long ngayBatDau = null;
            Long ngayKetThuc = null;

            if (ngayBatDauCal != null) {
                ngayBatDau = ngayBatDauCal.getTimeInMillis() / 1000;
            }

            if (ngayKetThucCal != null) {
                ngayKetThuc = ngayKetThucCal.getTimeInMillis() / 1000;
            }

            System.out.println("Ngay Bat Dau: " + ngayBatDau);
            System.out.println("Ngay Ket Thuc: " + ngayKetThuc);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date()); // Đặt thời gian cho ngày hiện tại
            Long ngayTaoVaSua = cal.getTime().getTime() / 1000;

            Boolean trangThai;
            if (this.rdHoatDongTimKiem.isSelected()) {
                trangThai = true;
            } else if (this.rdTatCaTimKiem.isSelected()) {
                trangThai = null;
            } else {
                trangThai = false;
            }

            return new KhuyenMaiTheoSanPhamRequest(input, giaTri, ngayBatDau, ngayKetThuc, trangThai, ngayTaoVaSua);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Đọc file lỗi");
            e.printStackTrace();
            return khuyenMaiTheoSanPham;
        }
    }

//    private void updateDataPanel() {
//        // Xóa dữ liệu cũ từ dataPanel
//        this.listView.removeAll(listView);
//
//        try {
//            ResultSet resultSet = repo.getData(currentPage);
//            while (resultSet.next()) {
//                // Xử lý và hiển thị dữ liệu trên giao diện
//                String data = resultSet.getString("column_name");
//                listView.add(new JLabel(data));
//            }
////            listView.revalidate();
////            listView.repaint();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rdgrTrangThaiForm = new javax.swing.ButtonGroup();
        rdgrTrangThaiTimKiem = new javax.swing.ButtonGroup();
        lb = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jlbMa = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jslGiaTri = new javax.swing.JSlider();
        jlbGiaTri = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        rdHoatDong = new javax.swing.JRadioButton();
        rdNgungHoatDong = new javax.swing.JRadioButton();
        jlbNgayBatDau = new javax.swing.JLabel();
        jlbNgayKetThuc = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jlbNgaySua = new javax.swing.JLabel();
        jlbNgayTao = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoaMem = new javax.swing.JButton();
        btnDonForm = new javax.swing.JButton();
        jdcNgayBatDau = new com.toedter.calendar.JDateChooser();
        jdcNgayKetThuc = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDanhSachKhuyenMai = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        btnTrai = new javax.swing.JButton();
        btnPhai = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txtTimKiemMaOrTen = new javax.swing.JTextField();
        rdHoatDongTimKiem = new javax.swing.JRadioButton();
        jLabel16 = new javax.swing.JLabel();
        rdTatCaTimKiem = new javax.swing.JRadioButton();
        rdNgungHoatDongTimKiem = new javax.swing.JRadioButton();
        jLabel19 = new javax.swing.JLabel();
        jlbNgayKetThucTimKiem = new javax.swing.JLabel();
        jlbNgayBatDauTimKiem = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        btnTimKiem = new javax.swing.JButton();
        btnDonFormTimKiem = new javax.swing.JButton();
        jdcNgayBatDauTimKiem = new com.toedter.calendar.JDateChooser();
        jdcNgayKetThucTimKiem = new com.toedter.calendar.JDateChooser();
        txtGiaTriTimKiem = new javax.swing.JTextField();

        lb.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lb.setText("Quản lý đợt khuyến mại");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin khuyến mại"));

        jLabel1.setText("Mã: ");

        jlbMa.setForeground(java.awt.Color.red);
        jlbMa.setText("_");

        jLabel3.setText("Tên:");

        jLabel2.setText("Giá trị:");

        jslGiaTri.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jslGiaTriStateChanged(evt);
            }
        });

        jlbGiaTri.setForeground(java.awt.Color.red);
        jlbGiaTri.setText("50");

        jLabel5.setForeground(java.awt.Color.red);
        jLabel5.setText("%");

        jLabel6.setText("Ngày kết thúc:");

        jLabel7.setText("Ngày bắt đầu:");

        jLabel8.setText("Trạng thái:");

        rdgrTrangThaiForm.add(rdHoatDong);
        rdHoatDong.setSelected(true);
        rdHoatDong.setText("Hoạt động");

        rdgrTrangThaiForm.add(rdNgungHoatDong);
        rdNgungHoatDong.setText("Ngừng hoạt động");

        jlbNgayBatDau.setForeground(java.awt.Color.red);
        jlbNgayBatDau.setText("_");

        jlbNgayKetThuc.setForeground(java.awt.Color.red);
        jlbNgayKetThuc.setText("_");

        jLabel9.setText("Ngày sửa:");

        jlbNgaySua.setForeground(java.awt.Color.red);
        jlbNgaySua.setText("_");

        jlbNgayTao.setForeground(java.awt.Color.red);
        jlbNgayTao.setText("_");

        jLabel14.setText("Ngày tạo:");

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoaMem.setText("Xóa mềm");
        btnXoaMem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaMemActionPerformed(evt);
            }
        });

        btnDonForm.setText("Dọn Form");
        btnDonForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDonFormActionPerformed(evt);
            }
        });

        jdcNgayBatDau.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcNgayBatDauPropertyChange(evt);
            }
        });

        jdcNgayKetThuc.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcNgayKetThucPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel3)))
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(rdHoatDong)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                            .addComponent(rdNgungHoatDong))
                        .addComponent(jlbMa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTen, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jslGiaTri, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlbGiaTri, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jlbNgayKetThuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlbNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel14))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlbNgaySua, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jdcNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jdcNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnThem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoaMem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnDonForm, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(btnSua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(50, 50, 50))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnXoaMem, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jlbMa, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jlbNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7))
                                        .addComponent(jdcNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(19, 19, 19)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel6)
                                            .addComponent(jlbNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jdcNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(6, 6, 6)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jslGiaTri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jlbGiaTri, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5)))
                                    .addGap(18, 18, 18)
                                    .addComponent(rdNgungHoatDong))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel2)
                                            .addGap(28, 28, 28))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jlbNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel14))
                                            .addGap(18, 18, 18)))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jlbNgaySua, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel8)
                                            .addComponent(rdHoatDong)
                                            .addComponent(jLabel9))))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnDonForm, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(1, 1, 1))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách khuyến mại"));

        tblDanhSachKhuyenMai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã", "Tên", "Giá", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"
            }
        ));
        tblDanhSachKhuyenMai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDanhSachKhuyenMaiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDanhSachKhuyenMai);

        jButton4.setText("<<");

        btnTrai.setText("<");
        btnTrai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraiActionPerformed(evt);
            }
        });

        btnPhai.setText(">");
        btnPhai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhaiActionPerformed(evt);
            }
        });

        jButton7.setText(">>");

        jLabel15.setText("Tìm theo mã - tên");

        rdgrTrangThaiTimKiem.add(rdHoatDongTimKiem);
        rdHoatDongTimKiem.setText("Hoạt động");

        jLabel16.setText("Trạng thái:");

        rdgrTrangThaiTimKiem.add(rdTatCaTimKiem);
        rdTatCaTimKiem.setSelected(true);
        rdTatCaTimKiem.setText("Tẩt cả");

        rdgrTrangThaiTimKiem.add(rdNgungHoatDongTimKiem);
        rdNgungHoatDongTimKiem.setText("Ngừng hoạt động");

        jLabel19.setText("Giá trị:");

        jlbNgayKetThucTimKiem.setForeground(java.awt.Color.red);
        jlbNgayKetThucTimKiem.setText("_");

        jlbNgayBatDauTimKiem.setForeground(java.awt.Color.red);
        jlbNgayBatDauTimKiem.setText("_");

        jLabel22.setText("Ngày kết thúc:");

        jLabel21.setText("Ngày bắt đầu:");

        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        btnDonFormTimKiem.setText("Dọn Form");
        btnDonFormTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDonFormTimKiemActionPerformed(evt);
            }
        });

        jdcNgayBatDauTimKiem.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcNgayBatDauTimKiemPropertyChange(evt);
            }
        });

        jdcNgayKetThucTimKiem.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcNgayKetThucTimKiemPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(271, 271, 271)
                        .addComponent(jButton4)
                        .addGap(104, 104, 104)
                        .addComponent(btnTrai)
                        .addGap(103, 103, 103)
                        .addComponent(btnPhai)
                        .addGap(96, 96, 96)
                        .addComponent(jButton7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel19))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTimKiemMaOrTen, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGiaTriTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(18, 18, 18)
                                .addComponent(rdTatCaTimKiem)
                                .addGap(18, 18, 18)
                                .addComponent(rdHoatDongTimKiem)
                                .addGap(18, 18, 18)
                                .addComponent(rdNgungHoatDongTimKiem))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnDonFormTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel21))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlbNgayBatDauTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                            .addComponent(jlbNgayKetThucTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jdcNgayKetThucTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jdcNgayBatDauTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(txtTimKiemMaOrTen, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16)
                                    .addComponent(rdTatCaTimKiem)
                                    .addComponent(rdHoatDongTimKiem)
                                    .addComponent(rdNgungHoatDongTimKiem)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jdcNgayBatDauTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jlbNgayBatDauTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnTimKiem)
                                .addComponent(btnDonFormTimKiem)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jlbNgayKetThucTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtGiaTriTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jdcNgayKetThucTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(btnTrai)
                    .addComponent(btnPhai)
                    .addComponent(jButton7))
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(445, 445, 445)
                .addComponent(lb)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lb, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnXoaMemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaMemActionPerformed
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có xóa không", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            String ma = this.jlbMa.getText();
            Boolean trangThai;
            if (rdHoatDong.isSelected()) {
                trangThai = false;
            } else {
                trangThai = true;
            }
            try {
                this.repo.capNhatTrangThaiKhuyenMaiBangMa(ma, trangThai);
                this.listView = this.repo.getData(currentPage);
                this.fillTable(this.listView);
                this.fillTableToForm(ma, this.listView);
                JOptionPane.showMessageDialog(this, "cập nhật thành công");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "cập nhật thất bại");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Đã hủy cập nhật");
        }
    }//GEN-LAST:event_btnXoaMemActionPerformed

    private void btnDonFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDonFormActionPerformed
        this.txtTen.setText("");
        this.jlbMa.setText("");
        this.jlbNgayBatDau.setText("");
        this.jlbNgayKetThuc.setText("");
        this.jlbNgayTao.setText("");
        this.jlbNgaySua.setText("");
        this.rdHoatDong.setSelected(true);
        this.jslGiaTri.setValue(50);
        this.jdcNgayBatDau.setCalendar(null);
        this.jdcNgayKetThuc.setCalendar(null);
    }//GEN-LAST:event_btnDonFormActionPerformed

    private void jslGiaTriStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jslGiaTriStateChanged
        this.jlbGiaTri.setText(String.valueOf(this.jslGiaTri.getValue()));
    }//GEN-LAST:event_jslGiaTriStateChanged

    private void btnDonFormTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDonFormTimKiemActionPerformed
        this.jlbNgayBatDauTimKiem.setText("");
        this.jlbNgayKetThucTimKiem.setText("");
        this.jdcNgayBatDauTimKiem.setCalendar(null);
        this.jdcNgayKetThucTimKiem.setCalendar(null);
        this.txtTimKiemMaOrTen.setText("");
        this.rdTatCaTimKiem.setSelected(true);
        this.txtGiaTriTimKiem.setText("");
        this.jdcNgayBatDauTimKiem.setCalendar(null);
        this.jdcNgayKetThucTimKiem.setCalendar(null);
        try {
            this.listView = this.repo.getAllDataKhuyenMai();
        } catch (Exception ex) {
            Logger.getLogger(FormQuanLyKhuyenMaiTheoSanPham.class.getName()).log(Level.SEVERE, null, ex);
        }
        fillTable(listView);
    }//GEN-LAST:event_btnDonFormTimKiemActionPerformed

    private void tblDanhSachKhuyenMaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDanhSachKhuyenMaiMouseClicked
        int index = tblDanhSachKhuyenMai.getSelectedRow();
        String ma = (String) tblDanhSachKhuyenMai.getValueAt(index, 0);
        this.fillTableToForm(ma, listView);
    }//GEN-LAST:event_tblDanhSachKhuyenMaiMouseClicked

    private void jdcNgayBatDauPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcNgayBatDauPropertyChange
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        Calendar ngay = this.jdcNgayBatDau.getCalendar();
        if (ngay != null) {
            Calendar ngayConver = ngay;
            ngayConver.set(Calendar.HOUR_OF_DAY, 0);
            ngayConver.set(Calendar.MINUTE, 0);
            ngayConver.set(Calendar.SECOND, 0);
            ngayConver.set(Calendar.MILLISECOND, 0);
            String ngayConverted = sdf.format(ngayConver.getTime());
            this.jlbNgayBatDau.setText(ngayConverted);
        }
    }//GEN-LAST:event_jdcNgayBatDauPropertyChange

    private void jdcNgayKetThucPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcNgayKetThucPropertyChange
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        Calendar ngay = this.jdcNgayKetThuc.getCalendar();
        if (ngay != null) {
            Calendar ngayConver = ngay;
            ngayConver.set(Calendar.HOUR_OF_DAY, 0);
            ngayConver.set(Calendar.MINUTE, 0);
            ngayConver.set(Calendar.SECOND, 0);
            ngayConver.set(Calendar.MILLISECOND, 0);
            String ngayConverted = sdf.format(ngayConver.getTime());
            this.jlbNgayKetThuc.setText(ngayConverted);
        }
    }//GEN-LAST:event_jdcNgayKetThucPropertyChange

    private void jdcNgayBatDauTimKiemPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcNgayBatDauTimKiemPropertyChange
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        Calendar ngay = this.jdcNgayBatDauTimKiem.getCalendar();
        if (ngay != null) {
            Calendar ngayConver = ngay;
            ngayConver.set(Calendar.HOUR_OF_DAY, 0);
            ngayConver.set(Calendar.MINUTE, 0);
            ngayConver.set(Calendar.SECOND, 0);
            ngayConver.set(Calendar.MILLISECOND, 0);
            String ngayConverted = sdf.format(ngayConver.getTime());
            this.jlbNgayBatDauTimKiem.setText(ngayConverted);
        }
    }//GEN-LAST:event_jdcNgayBatDauTimKiemPropertyChange

    private void jdcNgayKetThucTimKiemPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcNgayKetThucTimKiemPropertyChange
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        Calendar ngay = this.jdcNgayKetThucTimKiem.getCalendar();
        if (ngay != null) {
            Calendar ngayConver = ngay;
            ngayConver.set(Calendar.HOUR_OF_DAY, 0);
            ngayConver.set(Calendar.MINUTE, 0);
            ngayConver.set(Calendar.SECOND, 0);
            ngayConver.set(Calendar.MILLISECOND, 0);
            String ngayConverted = sdf.format(ngayConver.getTime());
            this.jlbNgayKetThucTimKiem.setText(ngayConverted);
        }
    }//GEN-LAST:event_jdcNgayKetThucTimKiemPropertyChange

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có muốn thêm", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            String ma = RandomCodeGenerator.generateCode(4);
            try {
                this.repo.themKhuyenMai(readForm(ma));
                this.listView = this.repo.getData(currentPage);
                this.fillTable(listView);
                this.fillTableToForm(ma, listView);
                JOptionPane.showMessageDialog(this, "Thêm Thành công");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Thêm thất bại");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Đã hủy thêm");
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        int choice = JOptionPane.showConfirmDialog(this, "Bạn có sửa không", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            String ma = this.jlbMa.getText();
            try {
                this.repo.suaKhuyenMai(readForm(ma));
                this.listView = this.repo.getData(currentPage);
                this.fillTable(this.listView);
                this.fillTableToForm(ma, this.listView);
                JOptionPane.showMessageDialog(this, "Sửa thành công");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Sửa thất bại");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Đã hủy sửa");
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        int choice = JOptionPane.showConfirmDialog(this, "Bạn muốn tìm không", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                this.listView = this.repo.timKiemTheoNhieuTruong(readFormSearch());
                this.fillTable(this.listView);
                JOptionPane.showMessageDialog(this, "tìm thành công");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "tìm thất bại");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Đã hủy tìm");
        }
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnTraiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraiActionPerformed
        if (currentPage > 1) {
            currentPage--;
            try {
                this.listView = 
                this.repo.getData(currentPage);
                this.fillTable(listView);
            } catch (Exception ex) {
                Logger.getLogger(FormQuanLyKhuyenMaiTheoSanPham.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnTraiActionPerformed

    private void btnPhaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhaiActionPerformed
        try {
            currentPage++;
            try {
                this.listView = 
                this.repo.getData(currentPage);
                this.fillTable(listView);
            } catch (Exception ex) {
                Logger.getLogger(FormQuanLyKhuyenMaiTheoSanPham.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnPhaiActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDonForm;
    private javax.swing.JButton btnDonFormTimKiem;
    private javax.swing.JButton btnPhai;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnTrai;
    private javax.swing.JButton btnXoaMem;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcNgayBatDau;
    private com.toedter.calendar.JDateChooser jdcNgayBatDauTimKiem;
    private com.toedter.calendar.JDateChooser jdcNgayKetThuc;
    private com.toedter.calendar.JDateChooser jdcNgayKetThucTimKiem;
    private javax.swing.JLabel jlbGiaTri;
    private javax.swing.JLabel jlbMa;
    private javax.swing.JLabel jlbNgayBatDau;
    private javax.swing.JLabel jlbNgayBatDauTimKiem;
    private javax.swing.JLabel jlbNgayKetThuc;
    private javax.swing.JLabel jlbNgayKetThucTimKiem;
    private javax.swing.JLabel jlbNgaySua;
    private javax.swing.JLabel jlbNgayTao;
    private javax.swing.JSlider jslGiaTri;
    private javax.swing.JLabel lb;
    private javax.swing.JRadioButton rdHoatDong;
    private javax.swing.JRadioButton rdHoatDongTimKiem;
    private javax.swing.JRadioButton rdNgungHoatDong;
    private javax.swing.JRadioButton rdNgungHoatDongTimKiem;
    private javax.swing.JRadioButton rdTatCaTimKiem;
    private javax.swing.ButtonGroup rdgrTrangThaiForm;
    private javax.swing.ButtonGroup rdgrTrangThaiTimKiem;
    private javax.swing.JTable tblDanhSachKhuyenMai;
    private javax.swing.JTextField txtGiaTriTimKiem;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiemMaOrTen;
    // End of variables declaration//GEN-END:variables
}
