package vn.com.gsoft.transaction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.transaction.entity.GiaoDichHangHoa;
import vn.com.gsoft.transaction.entity.HangHoaLuanChuyen;
import vn.com.gsoft.transaction.model.dto.GiaoDichHangHoaReq;
import vn.com.gsoft.transaction.model.dto.HangHoaLuanChuyenReq;

import java.util.List;
import java.util.Optional;

@Repository
public interface HangHoaLuanChuyenRepository extends BaseRepository<HangHoaLuanChuyen, HangHoaLuanChuyenReq, Long> {
    @Query(value = "SELECT * FROM HangHoaLuanChuyen c "
            + "WHERE 1=1 "
            + " AND ((:#{#param.thuocId} IS NULL) OR (c.thuocId = :#{#param.thuocId})) "
            + " AND ((:#{#param.citiId} IS NULL) OR (c.citiId = :#{#param.citiId})) "
            + " AND ((:#{#param.regionId} IS NULL) OR (c.regionId = :#{#param.regionId})) "
            + " AND ((:#{#param.wardId} IS NULL) OR (c.wardId = :#{#param.wardId})) "
            + " AND ((:#{#param.loaiHang} IS NULL) OR (c.loaiHang = :#{#param.loaiHang})) "
            + " AND ((:#{#param.recordStatusId} IS NULL) OR (c.recordStatusId = :#{#param.recordStatusId})) "
            + " ORDER BY c.id", nativeQuery = true
    )
    Page<HangHoaLuanChuyen> searchPage(@Param("param") HangHoaLuanChuyenReq param, Pageable pageable);

    @Query(value = "SELECT * FROM HangHoaLuanChuyen c "
            + "WHERE 1=1 "
            + " AND ((:#{#param.thuocId} IS NULL) OR (c.thuocId = :#{#param.thuocId})) "
            + " AND ((:#{#param.citiId} IS NULL) OR (c.citiId = :#{#param.citiId})) "
            + " AND ((:#{#param.regionId} IS NULL) OR (c.regionId = :#{#param.regionId})) "
            + " AND ((:#{#param.wardId} IS NULL) OR (c.wardId = :#{#param.wardId})) "
            + " AND ((:#{#param.loaiHang} IS NULL) OR (c.loaiHang = :#{#param.loaiHang})) "
            + " AND ((:#{#param.recordStatusId} IS NULL) OR (c.recordStatusId = :#{#param.recordStatusId})) "
            + " ORDER BY c.id", nativeQuery = true
    )
    List<HangHoaLuanChuyen> searchList(@Param("param") HangHoaLuanChuyenReq param);

    List<HangHoaLuanChuyen> findByMaPhieuNhapCTAndRecordStatusId(Long maPhieuChiTiet, Long recordStatusId);
}
