package vn.com.gsoft.transaction.service;

import vn.com.gsoft.transaction.entity.PhieuDuTru;
import vn.com.gsoft.transaction.model.dto.HangDuTruReq;
import vn.com.gsoft.transaction.model.dto.HangDuTruRes;
import vn.com.gsoft.transaction.model.dto.PhieuDuTruReq;

import java.util.List;

public interface PhieuDuTruService extends BaseService<PhieuDuTru, PhieuDuTruReq, Long> {
    List<HangDuTruRes> searchListHangDuTru(HangDuTruReq objReq) throws Exception;
    List<HangDuTruRes> searchListTop10HangBanChay(HangDuTruReq objReq) throws Exception;
}