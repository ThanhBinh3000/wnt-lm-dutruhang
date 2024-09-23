package vn.com.gsoft.transaction.service;

import org.springframework.data.domain.Page;
import vn.com.gsoft.transaction.entity.ChiTietHangHoaLuanChuyen;
import vn.com.gsoft.transaction.entity.HangHoaLuanChuyen;
import vn.com.gsoft.transaction.entity.PhieuNhapChiTiets;
import vn.com.gsoft.transaction.model.dto.ChiTietHangLuanChuyenReq;
import vn.com.gsoft.transaction.model.dto.HangHoaLuanChuyenReq;

import java.util.List;

public interface ChiTietHangHoaLuanChuyenService extends BaseService<ChiTietHangHoaLuanChuyen, ChiTietHangLuanChuyenReq, Long>{
    Page<ChiTietHangHoaLuanChuyen> searchPageLichSuGiaoDich(ChiTietHangLuanChuyenReq req) throws Exception;
    Page<ChiTietHangHoaLuanChuyen> searchPageLichSuNhan(ChiTietHangLuanChuyenReq req) throws Exception;
    boolean dongY(ChiTietHangLuanChuyenReq req) throws Exception;
    boolean tuChoi(ChiTietHangLuanChuyenReq req) throws Exception;
    boolean cancelGD(ChiTietHangLuanChuyenReq req) throws Exception;
    boolean confirmGD(ChiTietHangLuanChuyenReq req) throws Exception;
    boolean ketThucGiaoDich(ChiTietHangLuanChuyenReq req) throws Exception;
    boolean sendNotificationConfirmCoSo(List<ChiTietHangLuanChuyenReq> items) throws Exception;
}

