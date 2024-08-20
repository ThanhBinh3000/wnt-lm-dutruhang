package vn.com.gsoft.transaction.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.com.gsoft.transaction.model.system.BaseRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApplicationSettingReq extends BaseRequest {
    private String settingKey;
    private String settingValue;
    private String settingDisplayName;
    private Boolean isReadOnly;
    private String drugStoreId;
    private String description;
    private Boolean activated;
    private Long roleId;
    private Long typeId;
}
