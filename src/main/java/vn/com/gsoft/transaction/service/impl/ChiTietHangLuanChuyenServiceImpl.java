package vn.com.gsoft.transaction.service.impl;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.transaction.constant.JobContains;
import vn.com.gsoft.transaction.constant.NotificationContains;
import vn.com.gsoft.transaction.constant.RecordStatusContains;
import vn.com.gsoft.transaction.constant.StatusLuanChuyenContains;
import vn.com.gsoft.transaction.entity.*;
import vn.com.gsoft.transaction.model.dto.ChiTietHangLuanChuyenReq;
import vn.com.gsoft.transaction.model.dto.DataType;
import vn.com.gsoft.transaction.model.dto.HangHoaLuanChuyenReq;
import vn.com.gsoft.transaction.model.dto.NhaThuocRes;
import vn.com.gsoft.transaction.model.system.Profile;
import vn.com.gsoft.transaction.model.system.WrapData;
import vn.com.gsoft.transaction.repository.*;
import vn.com.gsoft.transaction.service.ChiTietHangHoaLuanChuyenService;
import vn.com.gsoft.transaction.service.HangHoaLuanChuyenService;
import vn.com.gsoft.transaction.service.KafkaProducer;
import vn.com.gsoft.transaction.util.system.DataUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@Log4j2
public class ChiTietHangLuanChuyenServiceImpl extends BaseServiceImpl<ChiTietHangHoaLuanChuyen, ChiTietHangLuanChuyenReq, Long>
        implements ChiTietHangHoaLuanChuyenService {
    @Autowired
    private ChiTietHangHoaLuanChuyenRepository hdrRepo;
    @Autowired
    private HangHoaLuanChuyenRepository hangHoaLuanChuyenRepository;
    @Autowired
    private NhaThuocsRepository nhaThuocsRepository;
    @Autowired
    KafkaProducer kafkaProducer;
    @Value("${wnt.kafka.internal.producer.topic.notification}")
    private String topicName;

    @Autowired
    public ChiTietHangLuanChuyenServiceImpl(ChiTietHangHoaLuanChuyenRepository hdrRepo
                                     , HangHoaLuanChuyenRepository hangHoaLuanChuyenRepository
                                     , NhaThuocsRepository nhaThuocsRepository
    ) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.hangHoaLuanChuyenRepository = hangHoaLuanChuyenRepository;
        this.nhaThuocsRepository = nhaThuocsRepository;
    }

    @Override
    public Page<ChiTietHangHoaLuanChuyen> searchPageLichSuNhan(ChiTietHangLuanChuyenReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        req.setMaCoSoNhan(userInfo.getMaCoSo());
        var ds = hdrRepo.searchPage(req, pageable);
        //gán thông tin thuốc
        ds.forEach(x->{
            Optional<HangHoaLuanChuyen> lc = hangHoaLuanChuyenRepository.findById(Long.valueOf(x.getIdLuanChuyen()));
            if(lc.isPresent()){
                x.setTenThuoc(lc.get().getTenThuoc());
                x.setSoLo(lc.get().getSoLo());
                x.setHanDung(lc.get().getHanDung());
                if(x.getTrangThai() != StatusLuanChuyenContains.CH0){
                    x.setTenCoSo(lc.get().getTenCoSo());
                    x.setDiaChi(lc.get().getDiaChi());
                    x.setSoDienThoai(lc.get().getSoDienThoai());
                }else {
                    x.setTenCoSo("Cơ sở đề xuất");
                    x.setDiaChi("***");
                }
            }

        });

        return ds;
    }

    @Override
    public Page<ChiTietHangHoaLuanChuyen> searchPageLichSuDayDi(ChiTietHangLuanChuyenReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        req.setMaCoSoGui(userInfo.getMaCoSo());
        var ds = hdrRepo.searchPage(req, pageable);
        //gán thông tin thuốc
        ds.forEach(x->{
            Optional<HangHoaLuanChuyen> lc = hangHoaLuanChuyenRepository.findById(Long.valueOf(x.getIdLuanChuyen()));
            NhaThuocs nhaThuoc = nhaThuocsRepository.findByMaNhaThuoc(x.getMaCoSoNhan());
            if(lc.isPresent()){
                x.setTenThuoc(lc.get().getTenThuoc());
                x.setSoLo(lc.get().getSoLo());
                x.setHanDung(lc.get().getHanDung());
            }
            if(nhaThuoc != null){
                x.setTenCoSo(nhaThuoc.getTenNhaThuoc());
                x.setDiaChi(nhaThuoc.getDiaChi());
                x.setSoDienThoai(nhaThuoc.getDienThoai());
            }
        });

        return ds;
    }

    @Override
    public ChiTietHangHoaLuanChuyen create(ChiTietHangLuanChuyenReq req) throws Exception{
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        //kiểm tra xem co so nay da co quan tam chưa
        Optional<ChiTietHangHoaLuanChuyen> ct = hdrRepo.findByIdLuanChuyenAndMaCoSoNhan(req.getIdLuanChuyen(),
                userInfo.getMaCoSo());
        if(ct.isPresent()) throw new Exception("Bad request.");

        ChiTietHangHoaLuanChuyen item = new ChiTietHangHoaLuanChuyen();
        item.setIdLuanChuyen(req.getIdLuanChuyen());
        item.setTrangThai(StatusLuanChuyenContains.CH0);
        item.setMaCoSoGui(userInfo.getMaCoSo());
        item.setMaCoSoNhan(req.getMaCoSoGui());
        item.setCreated(new Date());
        item.setCreatedByUserId(userInfo.getId());
        item.setThuocId(req.getThuocId());
        item.setSoLuong(req.getSoLuong());
        hdrRepo.save(item);
        //gui thong bao
        sendNotificationCoSo(Long.valueOf(item.getId()), item.getMaCoSoNhan(), NotificationContains.YEU_CAU_THONG_TIN);
        return item;
    }

    @Override
    public boolean updateInfo(ChiTietHangLuanChuyenReq req) throws Exception{
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        //kiểm tra xem co so nay da co quan tam chưa
        Optional<ChiTietHangHoaLuanChuyen> ct = hdrRepo.findById(req.getId());
        if(ct.isEmpty()) throw new Exception("Bad request.");
        ChiTietHangHoaLuanChuyen item = new ChiTietHangHoaLuanChuyen();
        BeanUtils.copyProperties(ct.get(), item);
        var maGD = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        item.setMaGiaoDich(maGD);
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date expDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        item.setThoiHan(expDate);
        item.setTrangThai(StatusLuanChuyenContains.DANG_XU_LY);
        hdrRepo.save(item);
        //cập nhật lại trạng thái
        Optional<HangHoaLuanChuyen> hangLuanChuyen = hangHoaLuanChuyenRepository.findById(Long.valueOf(item.getIdLuanChuyen()));
        if(hangLuanChuyen.isPresent()){
            HangHoaLuanChuyen hh = new HangHoaLuanChuyen();
            BeanUtils.copyProperties(hangLuanChuyen.get(), hh);
            hh.setTrangThai(StatusLuanChuyenContains.DANG_XU_LY);
            hangHoaLuanChuyenRepository.save(hh);
        }
        sendNotificationCoSo(Long.valueOf(item.getId()), item.getMaCoSoNhan(), NotificationContains.PHAN_HOI_THONG_TIN);
        return true;
    }

    @Override
    public boolean cancelGD(ChiTietHangLuanChuyenReq req) throws Exception{
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        //kiểm tra xem co so nay da co quan tam chưa
        Optional<ChiTietHangHoaLuanChuyen> ct = hdrRepo.findByIdLuanChuyenAndMaCoSoNhan(req.getIdLuanChuyen(),
                userInfo.getMaCoSo());

        if(ct.isEmpty()) throw new Exception("Bad request.");
        ChiTietHangHoaLuanChuyen item = new ChiTietHangHoaLuanChuyen();
        BeanUtils.copyProperties(ct.get(), item);
        hdrRepo.delete(item);
        //cập nhật lại trạng thái
        Optional<HangHoaLuanChuyen> hangLuanChuyen = hangHoaLuanChuyenRepository.findById(Long.valueOf(item.getIdLuanChuyen()));
        if(hangLuanChuyen.isPresent()){
            HangHoaLuanChuyen hh = new HangHoaLuanChuyen();
            BeanUtils.copyProperties(hangLuanChuyen.get(), hh);
            hh.setTrangThai(StatusLuanChuyenContains.CH0);
            hangHoaLuanChuyenRepository.save(hh);
        }
        return true;
    }

    @Override
    public boolean confirmGD(ChiTietHangLuanChuyenReq req) throws Exception{
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        //kiểm tra xem co so nay da co quan tam chưa
        Optional<ChiTietHangHoaLuanChuyen> ct = hdrRepo.findByIdLuanChuyenAndMaCoSoNhan(req.getIdLuanChuyen(),
                userInfo.getMaCoSo());

        if(ct.isEmpty()) throw new Exception("Bad request.");
        ChiTietHangHoaLuanChuyen item = new ChiTietHangHoaLuanChuyen();
        BeanUtils.copyProperties(ct.get(), item);
        item.setTrangThai(StatusLuanChuyenContains.DA_XU_LY);
        //cập nhật lại trạng thái
        Optional<HangHoaLuanChuyen> hangLuanChuyen = hangHoaLuanChuyenRepository.findById(Long.valueOf(item.getIdLuanChuyen()));
        if(hangLuanChuyen.isPresent()){
            HangHoaLuanChuyen hh = new HangHoaLuanChuyen();
            BeanUtils.copyProperties(hangLuanChuyen.get(), hh);
            hh.setTrangThai(StatusLuanChuyenContains.DA_XU_LY);
            hangHoaLuanChuyenRepository.save(hh);
        }
        return true;
    }

    private void sendNotificationCoSo(Long id, String maCoSoNhan, int type) throws ExecutionException, InterruptedException, TimeoutException {
        var dataType = new DataType();
        Long[] ids = {id};
        dataType.setIds(ids);
        dataType.setType(type);
        int size = Arrays.stream(ids).toList().size();
        int index = 1;
        UUID uuid = UUID.randomUUID();
        String key = maCoSoNhan;
        String bathKey = uuid.toString();
        WrapData data = new WrapData();
        data.setBathKey(bathKey);
        data.setCode(JobContains.THONG_BAO);
        data.setSendDate(new Date());
        data.setData(dataType);
        data.setIndex(index);
        this.kafkaProducer.sendInternal(topicName, key, new Gson().toJson(data));
    }
}
