package vn.com.gsoft.transaction.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.gsoft.transaction.model.system.BaseRequest;

@Data
@NoArgsConstructor
public class HangDuTruReq extends BaseRequest {
    private Long thuocId;
    private Long nhomThuocId;
}
