package vn.com.gsoft.transaction.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import vn.com.gsoft.transaction.model.system.BaseRequest;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PhieuNhapChiTietsReq extends BaseRequest {
    private String nhaThuocMaNhaThuoc;
    private Date warnDate;
    private Boolean hangLuanChuyen;
}
