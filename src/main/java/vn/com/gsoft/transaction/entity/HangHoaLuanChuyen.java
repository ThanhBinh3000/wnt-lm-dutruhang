package vn.com.gsoft.transaction.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HangHoaLuanChuyen")
public class HangHoaLuanChuyen extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "MaCoSo")
    private String maCoSo;
    @Column(name = "TenCoSo")
    private String tenCoSo;
    @Column(name = "ThuocId")
    private Integer thuocId;
    @Column(name = "TenDonVi")
    private String tenDonVi;
    @Column(name = "SoLuong")
    private BigDecimal soLuong;
    @Column(name = "SoLo")
    private String soLo;
    @Column(name = "HanDung")
    private Date hanDung;
    @Column(name = "LoaiHang")
    private Integer loaiHang;
    @Column(name = "GhiChu")
    private String ghiChu;
    @Column(name = "DiaChi")
    private String diaChi;
    @Column(name = "RegionId")
    private Long regionId;
    @Column(name = "CitiId")
    private Long citiId;
    @Column(name = "WardId")
    private Long wardId;
    @Column(name = "SoDienThoai")
    private String soDienThoai;
    @Column(name = "MaPhieuNhapCT")
    private Integer maPhieuNhapCT;
    @Column(name = "TrangThai")
    private Integer trangThai;
    @Transient
    private String tenThuoc;
    @Column(name = "NhomHoatChatId")
    private Integer nhomHoatChatId;
    @Column(name = "NhomNganhHangId")
    private Integer nhomNganhHangId;
    @Column(name = "NhomDuocLyId")
    private Integer nhomDuocLyId;
}

