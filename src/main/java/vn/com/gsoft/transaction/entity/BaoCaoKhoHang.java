package vn.com.gsoft.transaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "BaoCaoKhoHang")
public class BaoCaoKhoHang {
    @Id
    @Column(name="Id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "MaNhaThuoc")
    private String maNhaThuoc;
    @Column(name = "ThuocId")
    private Integer thuocId;
    @Column(name = "GiaVonHangBan")
    private BigDecimal giaVonHangBan;
    @Column(name = "TonDauKy")
    private BigDecimal tonDauKy;
    @Column(name = "TonCuoiKy")
    private BigDecimal tonCuoiKy;
    @Column(name = "SoVongQuay")
    private BigDecimal soVongQuay;
    @Column(name = "TenThuoc")
    private String tenThuoc;
    @Column(name = "TenNhomThuoc")
    private String tenNhomThuoc;
    @Column(name = "TenDonViTinhDuTru")
    private String tenDonViTinhDuTru;
    @Column(name = "GiaNhap")
    private BigDecimal giaNhap;
}

