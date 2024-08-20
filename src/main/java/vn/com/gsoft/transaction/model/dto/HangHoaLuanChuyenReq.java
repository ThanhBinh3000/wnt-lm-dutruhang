package vn.com.gsoft.transaction.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.gsoft.transaction.model.system.BaseRequest;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class HangHoaLuanChuyenReq extends BaseRequest {
    private Long id;
    private String maCoSo;
    private String tenCoSo;
    private Integer thuocId;
    private String tenDonVi;
    private BigDecimal soLuong;
    private String soLo;
    private Date hanDung;
    private Integer loaiHang;
    private String ghiChu;
    private String diaChi;
    private Long regionId;
    private Long citiId;
    private Long wardId;
}
