package vn.com.gsoft.transaction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "DonViTinhs")
public class DonViTinhs {
    @Id
    @Column(name="maDonViTinh")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer maDonViTinh;
    @Column(name = "TenDonViTinh")
    private String tenDonViTinh;
    @Column(name = "MaNhaThuoc")
    private String maNhaThuoc;
    @Column(name = "ReferenceId")
    private Long referenceId;
    @Column(name = "ArchivedId")
    private Long archivedId;
    @Column(name = "StoreId")
    private Long storeId;
}

