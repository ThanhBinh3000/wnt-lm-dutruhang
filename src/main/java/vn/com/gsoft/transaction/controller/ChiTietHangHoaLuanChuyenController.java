package vn.com.gsoft.transaction.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.transaction.constant.PathConstant;
import vn.com.gsoft.transaction.entity.PhieuNhapChiTiets;
import vn.com.gsoft.transaction.model.dto.ChiTietHangLuanChuyenReq;
import vn.com.gsoft.transaction.model.dto.HangHoaLuanChuyenReq;
import vn.com.gsoft.transaction.response.BaseResponse;
import vn.com.gsoft.transaction.service.ChiTietHangHoaLuanChuyenService;
import vn.com.gsoft.transaction.service.HangHoaLuanChuyenService;
import vn.com.gsoft.transaction.util.system.ResponseUtils;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ct-hang-hoa-luan-chuyen")
public class ChiTietHangHoaLuanChuyenController {
    @Autowired
    private ChiTietHangHoaLuanChuyenService service;

    @PostMapping(value = PathConstant.URL_SEARCH_PAGE + "-hang-giao-dich", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> searchPageLichSuDayDi(@RequestBody ChiTietHangLuanChuyenReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.searchPageLichSuGiaoDich(objReq)));
    }

    @PostMapping(value = PathConstant.URL_SEARCH_PAGE + "-hang-quan-tam", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> searchPageLichSuNhan(@RequestBody ChiTietHangLuanChuyenReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.searchPageLichSuNhan(objReq)));
    }

    @PostMapping(value = PathConstant.URL_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> create(@RequestBody ChiTietHangLuanChuyenReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.create(objReq)));
    }

    @PostMapping(value = PathConstant.URL_UPDATE+"-dong-y", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> dongY(@RequestBody ChiTietHangLuanChuyenReq req) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.dongY(req)));
    }

    @PostMapping(value = PathConstant.URL_UPDATE+"-dong-y", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> tuChoi(@RequestBody ChiTietHangLuanChuyenReq req) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.tuChoi(req)));
    }

    @PostMapping(value = PathConstant.URL_DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> delete(@RequestBody ChiTietHangLuanChuyenReq req) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.delete(req.getId())));
    }

    @PostMapping(value = "ket-thuc-giao-dich", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> ketThucGiaoDich(@RequestBody ChiTietHangLuanChuyenReq req) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.ketThucGiaoDich(req)));
    }

    @PostMapping(value = "gui-thong-bao", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> guiThongBao(@RequestBody List<ChiTietHangLuanChuyenReq> req) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.sendNotificationConfirmCoSo(req)));
    }
}
