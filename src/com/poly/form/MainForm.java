package com.poly.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.poly.Application;
import com.poly.form.other.FormDashboard;

import com.poly.form.giaohang.FormQuanLyGiaoHang;
import com.poly.form.giaohang.FormQuanLyGiaoHangItem1;
import com.poly.form.giaohang.FormQuanLyGiaoHangItem2;

import com.poly.form.nhanvien.FormQuanLyNhanVien;
import com.poly.form.hoadon.FormQuanLyHoaDon;
import com.poly.form.hoadon.FormQuanLyHoaDonItem1;
import com.poly.form.hoadon.FormQuanLyHoaDonItem2;
import com.poly.form.nhanvien.FormQuanLyNhanVienItem1;
import com.poly.form.nhanvien.FormQuanLyNhanVienItem2;
import com.poly.form.sanpham.view.DanhSachSanPham;
import com.poly.form.nhacungcap.view.QuanLyNhaCungCap;
import com.poly.form.bienthesanpham.view.QuanLyBienTheSanPham;
import com.poly.form.doihang.view.ChinhSach;
import com.poly.form.doihang.view.DoiHangChiTiet;
import com.poly.form.doihang.view.DoiHangConfigs;
import com.poly.form.doihang.view.FormQuanLyDoiHang;
import com.poly.form.doihang.view.LichSuChiTiet;
import com.poly.form.doihang.view.LichSuDoiHang;
import com.poly.form.khachhang.view.FormCaiDaiMail;
import com.poly.form.khachhang.view.FormQuanLyKhachHang;
import com.poly.form.khachhang.view.FormQuanLyKhachHangDaXoa;
import com.poly.form.khuyenmai.khuyenmai_sanpham.form.FormQuanLyKhuyenMaiTheoSanPham;
import com.poly.form.thuoctinh.view.QuanLyThuocTinh;
import com.poly.form.theloai.view.QuanLyTheLoai;

import com.poly.form.sanpham.view.ThongKeSanPham;
import com.poly.form.voucher.view.FormLichSuVoucher;
import com.poly.form.voucher.view.FormQuanLyVoucher;
import com.poly.form.voucher.view.FormTrangThaiVoucher;
import com.poly.menu.Menu;
import com.poly.menu.MenuAction;

/**
 *
 * @author Raven
 */
public class MainForm extends JLayeredPane {

    public MainForm() {
        init();
    }

    private void init() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new MainFormLayout());
        menu = new Menu();
        panelBody = new JPanel(new BorderLayout());
        initMenuArrowIcon();
        menuButton.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.button.background;"
                + "arc:999;"
                + "focusWidth:0;"
                + "borderWidth:0");
        menuButton.addActionListener((ActionEvent e) -> {
            setMenuFull(!menu.isMenuFull());
        });
        initMenuEvent();
        setLayer(menuButton, JLayeredPane.POPUP_LAYER);
        add(menuButton);
        add(menu);
        add(panelBody);
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);
        initMenuArrowIcon();
    }

    private void initMenuArrowIcon() {
        if (menuButton == null) {
            menuButton = new JButton();
        }
        String icon = (getComponentOrientation().isLeftToRight()) ? "menu_left.svg" : "menu_right.svg";
        menuButton.setIcon(new FlatSVGIcon("raven/icon/svg/" + icon, 0.8f));
    }

    // Xác định hành động khi menu được kích hoạt
    private void initMenuEvent() {
        menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {

            // index là chỉ mục cha của menu
            // subIndex là chỉ mục con của menu
            // Application.showForm(Form muốn hiển thị) => tạo form bằng cách sao chép và đổi tên từ package "other"
            // Lưu ý: Mỗi form nên có một label, không nên xóa nó, chỉ cần thu nhỏ kích thước nếu cần thiết.
            // Sau đó kéo và thả các phần tử bạn muốn sử dụng trên form và build lại dự án trước khi chạy.
            // Chạy form tương ứng với chỉ mục
            // các element muốn dùng + build lại dự án rồi chay (các file ko còn cái icon cờ lê nữa :)))
            if (index == 0) {
                Application.showForm(new FormDashboard());

            } else if (index == 1) { // tương ứng với menu (Quản lý sản phẩm)
                if (subIndex == 1) { // tương ứng với menu item của (Quản lý sản phẩm)
                    Application.showForm(new DanhSachSanPham());
                } else if (subIndex == 2) {
                    Application.showForm(new ThongKeSanPham());
                } else if (subIndex == 3) {
                    Application.showForm(new QuanLyThuocTinh());
                } else if (subIndex == 4) {
                    Application.showForm(new QuanLyTheLoai());
                } else if (subIndex == 5) {
                    Application.showForm(new QuanLyNhaCungCap());
                } else {
                    action.cancel();
                }
            } else if (index == 2) {
                if (subIndex == 1) {
                    Application.showForm(new FormQuanLyHoaDon());
                } else if (subIndex == 2) {
                    Application.showForm(new FormQuanLyHoaDonItem1());
                } else if (subIndex == 3) {
                    Application.showForm(new FormQuanLyHoaDonItem2());
                }
            } else if (index == 3) {
                if (subIndex == 0) {
                    Application.showForm(new FormQuanLyDoiHang(menu, doiHangConfigs));
                } else if (subIndex == 2) {
                    Application.showForm(new ChinhSach(menu));
                } else if (subIndex == 3) {
                    Application.showForm(new DoiHangChiTiet(doiHangConfigs, menu));
                } else if (subIndex == 4) {
                    Application.showForm(new LichSuDoiHang(menu, doiHangConfigs));
                } else if (subIndex == 5) {
                    Application.showForm(new LichSuChiTiet(menu, doiHangConfigs));
                }
            } else if (index == 4) {
                if (subIndex == 1) {
                    Application.showForm(new FormQuanLyNhanVien());
                } else if (subIndex == 2) {
                    Application.showForm(new FormQuanLyNhanVienItem1());
                } else if (subIndex == 3) {
                    Application.showForm(new FormQuanLyNhanVienItem2());
                }
            } else if (index == 5) {
                if (subIndex == 1) {
                    Application.showForm(new FormQuanLyKhachHang());
                } else if (subIndex == 2) {
                    Application.showForm(new FormQuanLyKhachHangDaXoa());
                } else if (subIndex == 3) {
                    Application.showForm(new FormCaiDaiMail(null));
                }
            } else if (index == 6) {
                if (subIndex == 1) {
                    Application.showForm(new FormQuanLyKhuyenMaiTheoSanPham());
                } else if (subIndex == 2) {
                    Application.showForm(new FormQuanLyVoucher());
                } else if (subIndex == 3) {
                    Application.showForm(new FormLichSuVoucher());
                } else if (subIndex == 4) {
                    Application.showForm(new FormTrangThaiVoucher());
                }
            } else if (index == 7) {
                if (subIndex == 1) {
                    Application.showForm(new FormQuanLyGiaoHang());
                } else if (subIndex == 2) {
                    Application.showForm(new FormQuanLyGiaoHangItem1());
                } else if (subIndex == 3) {
                    Application.showForm(new FormQuanLyGiaoHangItem2());
                }
            } else if (index == 9) {
//                Application.logout();
            } else {
                action.cancel();
            }
        }
        );
    }

    private void setMenuFull(boolean full) {
        String icon;
        if (getComponentOrientation().isLeftToRight()) {
            icon = (full) ? "menu_left.svg" : "menu_right.svg";
        } else {
            icon = (full) ? "menu_right.svg" : "menu_left.svg";
        }
        menuButton.setIcon(new FlatSVGIcon("raven/icon/svg/" + icon, 0.8f));
        menu.setMenuFull(full);
        revalidate();
    }

    public void hideMenu() {
        menu.hideMenuItem();
    }

    public void showForm(Component component) {
        panelBody.removeAll();
        panelBody.add(component);
        panelBody.repaint();
        panelBody.revalidate();
    }

    public void setSelectedMenu(int index, int subIndex) {
        menu.setSelectedMenu(index, subIndex);
    }

    private Menu menu;
    private JPanel panelBody;
    private JButton menuButton;

    private class MainFormLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, 5);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                boolean ltr = parent.getComponentOrientation().isLeftToRight();
                Insets insets = UIScale.scale(parent.getInsets());
                int x = insets.left;
                int y = insets.top;
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int menuWidth = UIScale.scale(menu.isMenuFull() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
                int menuX = ltr ? x : x + width - menuWidth;
                menu.setBounds(menuX, y, menuWidth, height);
                int menuButtonWidth = menuButton.getPreferredSize().width;
                int menuButtonHeight = menuButton.getPreferredSize().height;
                int menubX;
                if (ltr) {
                    menubX = (int) (x + menuWidth - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.3f)));
                } else {
                    menubX = (int) (menuX - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.7f)));
                }
                menuButton.setBounds(menubX, UIScale.scale(30), menuButtonWidth, menuButtonHeight);
                int gap = UIScale.scale(5);
                int bodyWidth = width - menuWidth - gap;
                int bodyHeight = height;
                int bodyx = ltr ? (x + menuWidth + gap) : x;
                int bodyy = y;
                panelBody.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
            }
        }
    }
    private DoiHangConfigs doiHangConfigs = new DoiHangConfigs();
}
