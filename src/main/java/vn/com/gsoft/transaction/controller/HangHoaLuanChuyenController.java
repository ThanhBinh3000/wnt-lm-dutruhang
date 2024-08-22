package vn.com.gsoft.transaction.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.transaction.constant.PathConstant;
import vn.com.gsoft.transaction.entity.PhieuNhapChiTiets;
import vn.com.gsoft.transaction.model.dto.HangHoaLuanChuyenReq;
import vn.com.gsoft.transaction.response.BaseResponse;
import vn.com.gsoft.transaction.service.HangHoaLuanChuyenService;
import vn.com.gsoft.transaction.util.system.ResponseUtils;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/hang-hoa-luan-chuyen")
public class HangHoaLuanChuyenController {
    @Autowired
    private HangHoaLuanChuyenService service;

    @PostMapping(value = PathConstant.URL_SEARCH_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> searchPage(@RequestBody HangHoaLuanChuyenReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.searchPage(objReq)));
    }

    @PostMapping(value = PathConstant.URL_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> create(@RequestBody List<PhieuNhapChiTiets> objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.saveHangHoaLuanChuyen(objReq)));
    }

    @PostMapping(value = PathConstant.URL_DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@RequestBody PhieuNhapChiTiets req) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.deleteHangHoaLuanChuyen(Long.valueOf(req.getMaPhieuNhapCt()))));
    }
}
