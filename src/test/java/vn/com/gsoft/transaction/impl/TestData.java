package vn.com.gsoft.transaction.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.com.gsoft.transaction.model.dto.PhieuNhapChiTietsReq;
import vn.com.gsoft.transaction.model.system.PaggingReq;
import vn.com.gsoft.transaction.service.LuanChuyenHangService;

@SpringBootTest
@Slf4j
class TestData {
    @Autowired
    private LuanChuyenHangService luanChuyenHangService;
    @Test
    void getData() throws Exception {
        PhieuNhapChiTietsReq req = new PhieuNhapChiTietsReq();
        PaggingReq paggingReq = new PaggingReq();
        paggingReq.setPage(0);
        paggingReq.setLimit(10);
        req.setPaggingReq(paggingReq);
    luanChuyenHangService.searchListHangCanHan(req);
    }
}