package vn.com.gsoft.transaction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.transaction.entity.PhieuDuTruChiTiet;
import vn.com.gsoft.transaction.model.dto.PhieuDuTruChiTietReq;

import java.util.List;

@Repository
public interface PhieuDuTruChiTietRepository extends BaseRepository<PhieuDuTruChiTiet, PhieuDuTruChiTietReq, Long> {
  @Query("SELECT c FROM PhieuDuTruChiTiet c " +
         "WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId})"
          + " AND (:#{#param.maPhieuDuTru} IS NULL OR c.maPhieuDuTru = :#{#param.maPhieuDuTru}) "
          + " AND (:#{#param.maThuoc} IS NULL OR c.maThuoc = :#{#param.maThuoc}) "
          + " AND (:#{#param.maDonViTon} IS NULL OR c.maDonViTon = :#{#param.maDonViTon}) "
          + " AND (:#{#param.soLuongCanhBao} IS NULL OR c.soLuongCanhBao = :#{#param.soLuongCanhBao}) "
          + " AND (:#{#param.tonKho} IS NULL OR c.tonKho = :#{#param.tonKho}) "
          + " AND (:#{#param.duTru} IS NULL OR c.duTru = :#{#param.duTru}) "
          + " AND (:#{#param.maDonViDuTru} IS NULL OR c.maDonViDuTru = :#{#param.maDonViDuTru}) "
          + " AND (:#{#param.donGia} IS NULL OR c.donGia = :#{#param.donGia}) "
          + " ORDER BY c.id desc"
  )
  Page<PhieuDuTruChiTiet> searchPage(@Param("param") PhieuDuTruChiTietReq param, Pageable pageable);
  
  
  @Query("SELECT c FROM PhieuDuTruChiTiet c " +
         "WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId})"
          + " AND (:#{#param.maPhieuDuTru} IS NULL OR c.maPhieuDuTru = :#{#param.maPhieuDuTru}) "
          + " AND (:#{#param.maThuoc} IS NULL OR c.maThuoc = :#{#param.maThuoc}) "
          + " AND (:#{#param.maDonViTon} IS NULL OR c.maDonViTon = :#{#param.maDonViTon}) "
          + " AND (:#{#param.soLuongCanhBao} IS NULL OR c.soLuongCanhBao = :#{#param.soLuongCanhBao}) "
          + " AND (:#{#param.tonKho} IS NULL OR c.tonKho = :#{#param.tonKho}) "
          + " AND (:#{#param.duTru} IS NULL OR c.duTru = :#{#param.duTru}) "
          + " AND (:#{#param.maDonViDuTru} IS NULL OR c.maDonViDuTru = :#{#param.maDonViDuTru}) "
          + " AND (:#{#param.donGia} IS NULL OR c.donGia = :#{#param.donGia}) "
          + " ORDER BY c.id desc"
  )
  List<PhieuDuTruChiTiet> searchList(@Param("param") PhieuDuTruChiTietReq param);

  List<PhieuDuTruChiTiet> findByMaPhieuDuTru(Long maPhieuDuTru);

  void deleteAllByMaPhieuDuTru(Long maPhieuDuTru);
}
