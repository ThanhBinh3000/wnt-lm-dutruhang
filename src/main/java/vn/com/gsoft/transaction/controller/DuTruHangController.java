package vn.com.gsoft.transaction.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.transaction.model.dto.HangDuTruReq;
import vn.com.gsoft.transaction.model.dto.PhieuDuTruReq;
import vn.com.gsoft.transaction.model.dto.ThuocsReq;
import vn.com.gsoft.transaction.response.BaseResponse;
import vn.com.gsoft.transaction.service.PhieuDuTruService;
import vn.com.gsoft.transaction.util.system.ResponseUtils;

@Slf4j
@RestController
@RequestMapping("/du-tru")
public class DuTruHangController {
    @Autowired
    private PhieuDuTruService service;

    @GetMapping(value = "/detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> detail(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.detail(id)));
    }

    @PostMapping(value = "them-moi-phieu-du-tru", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> create(@RequestBody PhieuDuTruReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.create(objReq)));
    }

    @PostMapping(value = "search-list-hang-du-tru", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> searchListHangDuTru(@RequestBody HangDuTruReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.searchListHangDuTru(objReq)));
    }

    @PostMapping(value = "search-list-top-hang-ban-chay", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> searchListTop10HangBanChay(@RequestBody HangDuTruReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.searchListTop10HangBanChay(objReq)));
    }

    @PostMapping(value = "search-page-hang-du-tru", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse> colectionListHangDuTru(@RequestBody ThuocsReq objReq) throws Exception {
        return ResponseEntity.ok(ResponseUtils.ok(service.colectionPageHangDuTru(objReq)));
    }
}
