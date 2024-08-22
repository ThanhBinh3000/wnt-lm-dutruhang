package vn.com.gsoft.transaction.service;

import vn.com.gsoft.transaction.entity.HangHoaLuanChuyen;
import vn.com.gsoft.transaction.entity.PhieuNhapChiTiets;
import vn.com.gsoft.transaction.model.dto.HangHoaLuanChuyenReq;

import java.util.List;

public interface HangHoaLuanChuyenService extends BaseService<HangHoaLuanChuyen, HangHoaLuanChuyenReq, Long>{
    boolean saveHangHoaLuanChuyen(List<PhieuNhapChiTiets> data) throws Exception;
    boolean deleteHangHoaLuanChuyen(Long maPhieuChiTiet) throws Exception;
}

