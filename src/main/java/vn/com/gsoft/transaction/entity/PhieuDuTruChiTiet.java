package vn.com.gsoft.transaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PhieuDuTruChiTiet")
public class PhieuDuTruChiTiet extends BaseEntity {
    @Id
    @Column(name = "MaPhieuDuTruChiTiet")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "MaPhieuDuTru")
    private Long maPhieuDuTru;
    @Column(name = "MaThuoc")
    private Long maThuoc;
    @Column(name = "MaDonViTon")
    private Long maDonViTon;
    @Column(name = "SoLuongCanhBao")
    private BigDecimal soLuongCanhBao;
    @Column(name = "TonKho")
    private BigDecimal tonKho;
    @Column(name = "DuTru")
    private BigDecimal duTru;
    @Column(name = "MaDonViDuTru")
    private Long maDonViDuTru;
    @Column(name = "DonGia")
    private BigDecimal donGia;
    @Transient
    private String maThuocText;
    @Transient
    private String tenThuocText;
    @Transient
    private String maDonViTonText;
    @Transient
    private String maDonViDuTruText;
    @Transient
    private String tenNhomThuoc;
    @Transient
    private String nhaCungCapText;
}

