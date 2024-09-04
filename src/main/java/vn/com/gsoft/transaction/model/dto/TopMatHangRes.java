package vn.com.gsoft.transaction.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopMatHangRes {
    private String tenThuoc;
    private String tenNhomNganhHang;
    private String tenDonVi;
    private BigDecimal soLieuThiTruong;
    private BigDecimal soLieuCoSo;
    private Long ThuocId;
    private BigDecimal GB;
    private BigDecimal GN;
    private BigDecimal GBCS;
    private BigDecimal GNCS;
}
