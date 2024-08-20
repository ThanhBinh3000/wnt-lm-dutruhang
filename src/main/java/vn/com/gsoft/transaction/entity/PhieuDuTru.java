package vn.com.gsoft.transaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PhieuDuTru")
public class PhieuDuTru extends BaseEntity{
    @Id
    @Column(name = "MaPhieu")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "SoPhieu")
    private Integer soPhieu;
    @Column(name = "NgayTao")
    private Date ngayTao;
    @Column(name = "TongTien")
    private BigDecimal tongTien;
    @Column(name = "MaNhaThuoc")
    private String maNhaThuoc;
    @Column(name = "SupplierId")
    private Long supplierId;
    @Column(name = "LinkShare")
    private String linkShare;

    @Transient
    private String createdByUserText;
    @Transient
    private String supplierText;
    @Transient
    private String maNhaThuocText;
    @Transient
    private String diaChiNhaThuoc;
    @Transient
    private String sdtNhaThuoc;
    @Transient
    private List<PhieuDuTruChiTiet> chiTiets;
}

