package vn.com.gsoft.transaction.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DrugWarehouseSynthesisRes {
    private Long drugId;
    private String tenThuoc;
    private String tenDonViTinh;
    private String tenNhomThuoc;
    private BigDecimal giaNhap;
    private BigDecimal firstInventoryValue;
    private BigDecimal lastInventoryValue;
    private BigDecimal deliveryInventoryValueInPeriod;
}
