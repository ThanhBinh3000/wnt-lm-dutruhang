package vn.com.gsoft.transaction.service.impl;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.transaction.constant.BaoCaoContains;
import vn.com.gsoft.transaction.constant.LimitPageConstant;
import vn.com.gsoft.transaction.entity.*;
import vn.com.gsoft.transaction.model.dto.GiaoDichHangHoaReq;
import vn.com.gsoft.transaction.model.dto.HangDuTruRes;
import vn.com.gsoft.transaction.model.dto.TopMatHangRes;
import vn.com.gsoft.transaction.model.system.Profile;
import vn.com.gsoft.transaction.repository.*;
import vn.com.gsoft.transaction.service.GiaoDichHangHoaService;
import vn.com.gsoft.transaction.service.RedisListService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class GiaoDichHangHoaServiceImpl extends BaseServiceImpl<GiaoDichHangHoa, GiaoDichHangHoaReq, Long> implements GiaoDichHangHoaService {


    private GiaoDichHangHoaRepository hdrRepo;
    @Autowired
    private RedisListService redisListService;

    @Autowired
    public GiaoDichHangHoaServiceImpl(GiaoDichHangHoaRepository hdrRepo
                                ) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
    }
    private void pushData(){
        //        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        //        Page<GiaoDichHangHoa> giaoDichHangHoas = hdrRepo.searchPage(req, pageable);
        //        redisListService.pushDataRedis(giaoDichHangHoas.stream().toList());
    }
}
