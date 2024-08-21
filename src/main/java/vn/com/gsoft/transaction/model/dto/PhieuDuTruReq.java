package vn.com.gsoft.transaction.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.gsoft.transaction.entity.PhieuDuTruChiTiet;
import vn.com.gsoft.transaction.model.system.BaseRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PhieuDuTruReq extends BaseRequest {
    private Integer soPhieu;
    private Date ngayTao;
    private Long createdByUserId;
    private BigDecimal tongTien;
    private String maNhaThuoc;
    private Long recordStatusID;
    private Long supplierId;
    private String linkShare;
    private String loaiIn;
    private String createdByUseText;
    private String supplierText;
    private String maNhaThuocText;
    private String diaChiNhaThuoc;
    private String sdtNhaThuoc;
    private List<PhieuDuTruChiTiet> chiTiets;
}