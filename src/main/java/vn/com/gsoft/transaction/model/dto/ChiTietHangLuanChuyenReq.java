package vn.com.gsoft.transaction.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.kafka.common.protocol.types.Field;
import vn.com.gsoft.transaction.model.system.BaseRequest;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChiTietHangLuanChuyenReq extends BaseRequest {
    private Long id;
    private Integer thuocId;
    private String maGiaoDich;
    private Integer trangThai;
    private String maCoSoGui;
    private String maCoSoNhan;
    private Integer idLuanChuyen;
    private BigDecimal soLuong;
    private Integer[] trangThais;
    private Integer[] trangThai1s;
    private String ghiChu;
    private Boolean thanhCong;
}
