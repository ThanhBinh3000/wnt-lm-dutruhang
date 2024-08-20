package vn.com.gsoft.transaction.model.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NhaThuocs {
    private Long id;
    private String maNhaThuoc;
    private String tenNhaThuoc;
    private String diaChi;
    private String dienThoai;
    private String nguoiDaiDien;
    private String email;
    private String mobile;
    private String duocSy;
    private Boolean hoatDong;
    private Long tinhThanhId;
    private Boolean isConnectivity;
    private String description;
    private Long regionId;
    private Long cityId;
    private Long wardId;
    private Long entityId;
}

