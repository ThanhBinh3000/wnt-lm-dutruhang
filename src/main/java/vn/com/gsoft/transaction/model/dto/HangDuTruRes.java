package vn.com.gsoft.transaction.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class HangDuTruRes {
    private Long thuocId;
    private String tenThuoc;
    private String tenNhomThuoc;
    private String tenDonViTinh;
    private BigDecimal deXuatDuTru;
    private BigDecimal soLieuThiTruong;
    private BigDecimal donGia;

    // Constructor with all parameters
    public HangDuTruRes(Long thuocId, String tenThuoc, String tenNhomThuoc, String tenDonViTinh, BigDecimal donGia, BigDecimal deXuatDuTru) {
        this.thuocId = thuocId;
        this.tenThuoc = tenThuoc;
        this.tenNhomThuoc = tenNhomThuoc;
        this.tenDonViTinh = tenDonViTinh;
        this.donGia = donGia;
        this.deXuatDuTru = deXuatDuTru;
    }

    public HangDuTruRes(Long thuocId, String tenThuoc, String tenNhomThuoc, String tenDonViTinh, BigDecimal donGia, BigDecimal deXuatDuTru, BigDecimal soLieuThiTruong) {
        this.thuocId = thuocId;
        this.tenThuoc = tenThuoc;
        this.tenNhomThuoc = tenNhomThuoc;
        this.tenDonViTinh = tenDonViTinh;
        this.donGia = donGia;
        this.deXuatDuTru = deXuatDuTru;
        this.soLieuThiTruong = soLieuThiTruong;
    }
}
