package vn.com.gsoft.transaction.service;

import org.springframework.data.domain.Page;
import vn.com.gsoft.transaction.entity.*;
import vn.com.gsoft.transaction.model.dto.*;
import vn.com.gsoft.transaction.model.dto.HangDuTruReq;

import java.util.List;

public interface PhieuDuTruService extends BaseService<PhieuDuTru, PhieuDuTruReq, Long> {
    List<HangDuTruRes> searchListHangDuTru(HangDuTruReq objReq) throws Exception;
    List<HangDuTruRes> searchListTop10HangBanChay(HangDuTruReq objReq) throws Exception;
    Page<Thuocs> colectionPageHangDuTru(ThuocsReq objReq) throws Exception;
    List<PhieuDuTru> createNhaCC(List<PhieuDuTruReq> req) throws Exception;
}