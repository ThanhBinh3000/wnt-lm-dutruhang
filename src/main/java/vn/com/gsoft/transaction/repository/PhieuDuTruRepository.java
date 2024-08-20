package vn.com.gsoft.transaction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.transaction.entity.PhieuDuTru;
import vn.com.gsoft.transaction.model.dto.PhieuDuTruReq;

import java.util.List;

@Repository
public interface PhieuDuTruRepository extends BaseRepository<PhieuDuTru, PhieuDuTruReq, Long> {
    @Query("SELECT c FROM PhieuDuTru c " +
            "WHERE 1=1 "
            + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
            + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId})"
            + " AND (:#{#param.soPhieu} IS NULL OR c.soPhieu = :#{#param.soPhieu}) "
            + " AND (:#{#param.createdByUserId} IS NULL OR c.createdByUserId = :#{#param.createdByUserId}) "
            + " AND (:#{#param.tongTien} IS NULL OR c.tongTien = :#{#param.tongTien}) "
            + " AND (:#{#param.maNhaThuoc} IS NULL OR lower(c.maNhaThuoc) LIKE lower(concat('%',CONCAT(:#{#param.maNhaThuoc},'%'))))"
            + " AND (:#{#param.supplierId} IS NULL OR c.supplierId = :#{#param.supplierId}) "
            + " AND (:#{#param.linkShare} IS NULL OR lower(c.linkShare) LIKE lower(concat('%',CONCAT(:#{#param.linkShare},'%'))))"
            + " ORDER BY c.id desc"
    )
    Page<PhieuDuTru> searchPage(@Param("param") PhieuDuTruReq param, Pageable pageable);


    @Query("SELECT c FROM PhieuDuTru c " +
            "WHERE 1=1 "
            + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
            + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId})"
            + " AND (:#{#param.soPhieu} IS NULL OR c.soPhieu = :#{#param.soPhieu}) "
            + " AND (:#{#param.createdByUserId} IS NULL OR c.createdByUserId = :#{#param.createdByUserId}) "
            + " AND (:#{#param.tongTien} IS NULL OR c.tongTien = :#{#param.tongTien}) "
            + " AND (:#{#param.maNhaThuoc} IS NULL OR lower(c.maNhaThuoc) LIKE lower(concat('%',CONCAT(:#{#param.maNhaThuoc},'%'))))"
            + " AND (:#{#param.supplierId} IS NULL OR c.supplierId = :#{#param.supplierId}) "
            + " AND (:#{#param.linkShare} IS NULL OR lower(c.linkShare) LIKE lower(concat('%',CONCAT(:#{#param.linkShare},'%'))))"
            + " ORDER BY c.id desc"
    )
    List<PhieuDuTru> searchList(@Param("param") PhieuDuTruReq param);

    @Query("SELECT count(c) > 0 FROM PhieuDuTru c WHERE c.maNhaThuoc = :maNhaThuoc AND c.soPhieu = :soPhieu")
    boolean existsByMaNhaThuocAndSoPhieu(@Param("maNhaThuoc") String maNhaThuoc, @Param("soPhieu") int soPhieu);
}
