package vn.com.gsoft.transaction.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.gsoft.transaction.model.system.BaseRequest;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class PhieuDuTruChiTietReq extends BaseRequest {
    private Long maPhieuDuTru;
    private Long maThuoc;
    private Long maDonViTon;
    private BigDecimal soLuongCanhBao;
    private BigDecimal tonKho;
    private BigDecimal duTru;
    private Long maDonViDuTru;
    private BigDecimal donGia;
}
