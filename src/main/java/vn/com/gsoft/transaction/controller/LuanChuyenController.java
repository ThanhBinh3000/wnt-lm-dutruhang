package vn.com.gsoft.transaction.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.transaction.constant.PathConstant;
import vn.com.gsoft.transaction.model.dto.PhieuNhapChiTietsReq;
import vn.com.gsoft.transaction.response.BaseResponse;
import vn.com.gsoft.transaction.service.LuanChuyenService;
import vn.com.gsoft.transaction.util.system.ResponseUtils;

@Slf4j
@RestController
@RequestMapping("/luan-chuyen")
public class LuanChuyenController {
    @Autowired
    private LuanChuyenService service;

    @PostMapping(value = PathConstant.URL_SEARCH_PAGE + "-hang-can-han", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> searchPageHangCanHan(@RequestBody PhieuNhapChiTietsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.searchListHangCanHan(objReq)));
    }

    @PostMapping(value = PathConstant.URL_SEARCH_PAGE + "-hang-it-giao-dich", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> searchPageHangItGiaoDich(@RequestBody PhieuNhapChiTietsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.searchListHangItGiaoDich(objReq)));
    }
}
