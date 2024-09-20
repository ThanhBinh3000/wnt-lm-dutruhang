package vn.com.gsoft.transaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.apache.kafka.common.protocol.types.Field;
import vn.com.gsoft.transaction.constant.StatusLuanChuyenContains;

import java.math.BigDecimal;
import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ChiTietHangHoaLuanChuyen")
public class ChiTietHangHoaLuanChuyen extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ThuocId")
    private Integer thuocId;
    @Column(name = "MaGiaoDich")
    private String maGiaoDich;
    @Column(name = "SoLuong")
    private BigDecimal soLuong;
    @Column(name = "TrangThai")
    private Integer trangThai;
    @Column(name = "MaCoSoGui")
    private String maCoSoGui;
    @Column(name = "MaCoSoNhan")
    private String maCoSoNhan;
    @Column(name = "IdLuanChuyen")
    private Integer idLuanChuyen;
    @Column(name = "ThoiHan")
    private Date thoiHan;
    @Transient
    private String tenCoSo;
    @Transient
    private String diaChi;
    @Transient
    private String soDienThoai;
    @Transient
    private String tenThuoc;
    @Transient
    private String soLo;
    @Transient
    private Date hanDung;
    @Transient
    private String trangThaiTxt;
    public String getTrangThaiTxt(){
        var val = "";
        if(trangThai != null){
            if(trangThai == StatusLuanChuyenContains.CH0){
                val = "Chờ xác nhận";
            }
            else if(trangThai == StatusLuanChuyenContains.DANG_XU_LY){
                val = "Đang Giao Dịch";
            }
            else if(trangThai == StatusLuanChuyenContains.DA_XU_LY){
                val = "Giao Dịch Hoàn Tất";
            }
        }
        return val;
    }
}

