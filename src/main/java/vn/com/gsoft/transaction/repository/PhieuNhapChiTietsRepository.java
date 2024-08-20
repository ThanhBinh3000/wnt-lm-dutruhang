package vn.com.gsoft.transaction.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.transaction.entity.PhieuNhapChiTiets;
import vn.com.gsoft.transaction.model.dto.PhieuNhapChiTietsReq;

import java.util.List;

@Repository
public interface PhieuNhapChiTietsRepository extends BaseRepository<PhieuNhapChiTiets, PhieuNhapChiTietsReq, Integer> {
  @Query("SELECT c FROM PhieuNhapChiTiets c " +
         "WHERE 1=1 "
          + " ORDER BY c.maPhieuNhapCt desc"
  )
  Page<PhieuNhapChiTiets> searchPage(@Param("param") PhieuNhapChiTietsReq param, Pageable pageable);
  
  
  @Query("SELECT c FROM PhieuNhapChiTiets c " +
          "WHERE 1=1 "
          + " AND c.recordStatusId = 0"
          + " ORDER BY c.createdDate desc"
  )
  List<PhieuNhapChiTiets> searchList(@Param("param") PhieuNhapChiTietsReq param);

  @Query(value = "SELECT c FROM PhieuNhapChiTiets c " +
          "WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.maPhieuNhapCt = :#{#param.id}) "
          + " AND (:#{#param.nhaThuocMaNhaThuoc} IS NULL OR c.nhaThuoc_MaNhaThuoc = :#{#param.nhaThuocMaNhaThuoc})"
          + " AND (:#{#param.warnDate} IS NULL OR (c.hanDung IS NOT NULL AND c.hanDung <= :#{#param.warnDate}))"
          + " AND c.remainRefQuantity > 0"
          + " AND c.recordStatusId = :#{#param.recordStatusId}"
          + " AND (:#{#param.hangLuanChuyen} IS NULL OR c.hangLuanChuyen = :#{#param.hangLuanChuyen})"
          + " ORDER BY c.createdDate desc"
  )
  Page<PhieuNhapChiTiets> searchPageHangCanHan(@Param("param") PhieuNhapChiTietsReq param, Pageable pageable);

  @Query(value = "SELECT c FROM PhieuNhapChiTiets c " +
          "WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.maPhieuNhapCt = :#{#param.id}) "
          + " AND (:#{#param.nhaThuocMaNhaThuoc} IS NULL OR c.nhaThuoc_MaNhaThuoc = :#{#param.nhaThuocMaNhaThuoc})"
          + " AND (:#{#param.warnDate} IS NULL OR c.createdDate <= :#{#param.warnDate})"
          + " AND c.remainRefQuantity > 0"
          + " AND c.recordStatusId = :#{#param.recordStatusId}"
          + " AND (:#{#param.hangLuanChuyen} IS NULL OR c.hangLuanChuyen = :#{#param.hangLuanChuyen})"
          + " ORDER BY c.createdDate desc"
  )
  Page<PhieuNhapChiTiets> searchPageHangItGiaoDich(@Param("param") PhieuNhapChiTietsReq param, Pageable pageable);

  PhieuNhapChiTiets findByMaPhieuNhapCt(Integer maPhieuNhapCt);
}
