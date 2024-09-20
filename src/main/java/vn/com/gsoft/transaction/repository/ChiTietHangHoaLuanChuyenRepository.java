package vn.com.gsoft.transaction.repository;

import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.transaction.entity.ChiTietHangHoaLuanChuyen;
import vn.com.gsoft.transaction.entity.HangHoaLuanChuyen;
import vn.com.gsoft.transaction.model.dto.ChiTietHangLuanChuyenReq;
import vn.com.gsoft.transaction.model.dto.HangHoaLuanChuyenReq;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietHangHoaLuanChuyenRepository extends BaseRepository<ChiTietHangHoaLuanChuyen, ChiTietHangLuanChuyenReq, Long> {
    @Query(value = "SELECT * FROM ChiTietHangHoaLuanChuyen c "
            + "WHERE 1=1 "
            + " AND ((:#{#param.thuocId} IS NULL) OR (c.thuocId = :#{#param.thuocId})) "
            + " AND ((:#{#param.maGiaoDich} IS NULL) OR (c.maGiaoDich = :#{#param.maGiaoDich})) "
            + " AND ((:#{#param.trangThai} IS NULL) OR (c.trangThai = :#{#param.trangThai})) "
            + " AND ((:#{#param.maCoSoNhan} IS NULL) OR (c.maCoSoNhan = :#{#param.maCoSoNhan})) "
            + " AND (:#{#param.fromDate} IS NULL OR c.created >= :#{#param.fromDate}) "
            + " AND (:#{#param.toDate} IS NULL OR c.created <= :#{#param.toDate})" +
            " ORDER BY c.id DESC", nativeQuery = true
    )
    Page<ChiTietHangHoaLuanChuyen> searchPage(@Param("param") ChiTietHangLuanChuyenReq param, Pageable pageable);


    @Query(value = "SELECT * FROM ChiTietHangHoaLuanChuyen c "
            + "WHERE 1=1 "
            + " AND ((:#{#param.thuocId} IS NULL) OR (c.thuocId = :#{#param.thuocId})) "
            + " AND ((:#{#param.maGiaoDich} IS NULL) OR (c.maGiaoDich = :#{#param.maGiaoDich})) "
            + " AND ((:#{#param.trangThai} IS NULL) OR (c.trangThai = :#{#param.trangThai})) "
            + " AND ((:#{#param.maCoSoNhan} IS NULL) OR (c.maCoSoNhan = :#{#param.maCoSoNhan})) "
            + " AND (:#{#param.fromDate} IS NULL OR c.created >= :#{#param.fromDate}) "
            + " AND (:#{#param.toDate} IS NULL OR c.created <= :#{#param.toDate})" +
            " ORDER BY c.id DESC", nativeQuery = true
    )
    Page<ChiTietHangHoaLuanChuyen> searchPageQuanTam(@Param("param") ChiTietHangLuanChuyenReq param, Pageable pageable);

    @Query(value = "SELECT * FROM ChiTietHangHoaLuanChuyen c "
            + "WHERE 1=1 "
            + " AND ((:#{#param.thuocId} IS NULL) OR (c.thuocId = :#{#param.thuocId})) "
            + " AND ((:#{#param.maGiaoDich} IS NULL) OR (c.maGiaoDich = :#{#param.maGiaoDich})) "
            + " AND ((:#{#param.trangThai} IS NULL) OR (c.trangThai = :#{#param.trangThai})) "
            + " AND ((:#{#param.maCoSoGui} IS NULL) OR (c.maCoSoGui = :#{#param.maCoSoGui})) "
            + " AND (:#{#param.fromDate} IS NULL OR c.created >= :#{#param.fromDate}) "
            + " AND (:#{#param.toDate} IS NULL OR c.created <= :#{#param.toDate})" +
            " ORDER BY c.id DESC", nativeQuery = true
    )
    Page<ChiTietHangHoaLuanChuyen> searchPageGiaoDich(@Param("param") ChiTietHangLuanChuyenReq param, Pageable pageable);

    @Query(value = "SELECT * FROM ChiTietHangHoaLuanChuyen c "
            + "WHERE 1=1 "
            + " AND ((:#{#param.thuocId} IS NULL) OR (c.thuocId = :#{#param.thuocId})) "
            + " AND ((:#{#param.maGiaoDich} IS NULL) OR (c.maGiaoDich = :#{#param.maGiaoDich})) "
            + " AND ((:#{#param.trangThai} IS NULL) OR (c.trangThai = :#{#param.trangThai})) "
            + " AND (:#{#param.fromDate} IS NULL OR c.created >= :#{#param.fromDate}) "
            + " AND (:#{#param.toDate} IS NULL OR c.created <= :#{#param.toDate})" +
            " ORDER BY c.id DESC", nativeQuery = true
    )
    List<ChiTietHangHoaLuanChuyen> searchList(@Param("param") ChiTietHangLuanChuyenReq param);

    Optional<ChiTietHangHoaLuanChuyen> findByIdLuanChuyenAndMaCoSoNhan(Integer id, String maCoSo);
}
