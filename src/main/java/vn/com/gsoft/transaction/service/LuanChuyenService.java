package vn.com.gsoft.transaction.service;

import org.springframework.data.domain.Page;
import vn.com.gsoft.transaction.entity.PhieuNhapChiTiets;
import vn.com.gsoft.transaction.model.dto.PhieuNhapChiTietsReq;

public interface LuanChuyenService extends BaseService<PhieuNhapChiTiets, PhieuNhapChiTietsReq, Integer>{
    Page<PhieuNhapChiTiets> searchPageHangCanHan(PhieuNhapChiTietsReq req) throws Exception;

    Page<PhieuNhapChiTiets> searchPageHangItGiaoDich(PhieuNhapChiTietsReq req) throws Exception;
}

