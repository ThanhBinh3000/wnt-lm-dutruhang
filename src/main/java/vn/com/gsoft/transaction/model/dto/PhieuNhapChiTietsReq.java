package vn.com.gsoft.transaction.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.gsoft.transaction.model.system.BaseRequest;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class PhieuNhapChiTietsReq extends BaseRequest {
    private String nhaThuocMaNhaThuoc;
    private Date warnDate;
    private Boolean hangLuanChuyen;
    private Boolean hangChuaLuanChuyen;
}
