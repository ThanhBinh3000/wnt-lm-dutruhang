package vn.com.gsoft.transaction.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.gsoft.transaction.model.system.BaseRequest;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class HangDuTruReq extends BaseRequest {
    private Long thuocId;
    private Long nhomThuocId;
}
