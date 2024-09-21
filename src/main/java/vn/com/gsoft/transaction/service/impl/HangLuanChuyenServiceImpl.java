package vn.com.gsoft.transaction.service.impl;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
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
import vn.com.gsoft.transaction.entity.HangHoaLuanChuyen;
import vn.com.gsoft.transaction.entity.PhieuNhapChiTiets;
import vn.com.gsoft.transaction.entity.Thuocs;
import vn.com.gsoft.transaction.model.dto.DataType;
import vn.com.gsoft.transaction.model.dto.HangHoaLuanChuyenReq;
import vn.com.gsoft.transaction.model.system.Profile;
import vn.com.gsoft.transaction.model.system.WrapData;
import vn.com.gsoft.transaction.repository.HangHoaLuanChuyenRepository;
import vn.com.gsoft.transaction.repository.PhieuNhapChiTietsRepository;
import vn.com.gsoft.transaction.repository.ThuocsRepository;
import vn.com.gsoft.transaction.service.HangHoaLuanChuyenService;
import vn.com.gsoft.transaction.service.KafkaProducer;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@Log4j2
public class HangLuanChuyenServiceImpl extends BaseServiceImpl<HangHoaLuanChuyen, HangHoaLuanChuyenReq, Long> implements HangHoaLuanChuyenService {
    @Autowired
    private HangHoaLuanChuyenRepository hdrRepo;
    @Autowired
    private ThuocsRepository thuocsRepository;
    @Autowired
    private PhieuNhapChiTietsRepository phieuNhapChiTietsRepository;
    @Autowired
    KafkaProducer kafkaProducer;
    @Value("${wnt.kafka.internal.producer.topic.notification}")
    private String topicName;

    @Autowired
    public HangLuanChuyenServiceImpl(HangHoaLuanChuyenRepository hdrRepo
                                     , ThuocsRepository thuocsRepository
                                     , PhieuNhapChiTietsRepository phieuNhapChiTietsRepository
    ) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.thuocsRepository = thuocsRepository;
        this.phieuNhapChiTietsRepository = phieuNhapChiTietsRepository;
    }

    @Override
    public Page<HangHoaLuanChuyen> searchPage(HangHoaLuanChuyenReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Integer[] trangThais = {StatusLuanChuyenContains.CH0, StatusLuanChuyenContains.DANG_XU_LY};
        req.setTrangThais(trangThais);
        var ds = hdrRepo.searchPage(req, pageable);
        //gán thông tin thuốc
        ds.forEach(x->{
            Optional<Thuocs> thuoc = thuocsRepository.findById(Long.valueOf(x.getThuocId()));
            thuoc.ifPresent(thuocs -> x.setTenThuoc(thuocs.getTenThuoc()));
            x.setTenCoSo("Cơ sở đề xuất");
            x.setDiaChi("***");
        });

        return ds;
    }

    public boolean saveHangHoaLuanChuyen(List<PhieuNhapChiTiets> data) throws Exception{
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        var hangLuanChuyens = data.stream().filter(x->x.getHangLuanChuyen() != (null)
                && x.getHangLuanChuyen().equals(true)).toList();
        if(hangLuanChuyens.stream().isParallel()) return false;
        var ids = new ArrayList<Long>();
        hangLuanChuyens.forEach(x->{
            Optional<Thuocs> thuoc = thuocsRepository.findById(Long.valueOf(x.getThuocThuocId()));
            HangHoaLuanChuyen item = new HangHoaLuanChuyen();
            item.setThuocId(Math.toIntExact(x.getThuocDMCId() > 0 ? x.getThuocDMCId() : x.getThuocThuocId()));
            item.setMaCoSo(userInfo.getMaCoSo());
            item.setTenDonVi(x.getTenDonVi());
            item.setCitiId(userInfo.getCitiId());
            item.setRegionId(userInfo.getRegionId());
            item.setWardId(userInfo.getWardId());
            item.setGhiChu(x.getDecscription());
            item.setLoaiHang(x.getLoaiHang());
            item.setSoLo(x.getSoLo());
            item.setHanDung(x.getHanDung());
            item.setTenCoSo(userInfo.getTenNhaThuoc());
            item.setDiaChi(userInfo.getDiaChi());
            item.setSoDienThoai(userInfo.getSoDienThoai());
            item.setCreated(new Date());
            item.setCreatedByUserId(userInfo.getId());
            item.setRecordStatusId(RecordStatusContains.ACTIVE);
            item.setMaPhieuNhapCT(x.getMaPhieuNhapCt());
            item.setSoLuong(BigDecimal.valueOf(x.getRemainRefQuantity()));
            item.setTrangThai(StatusLuanChuyenContains.CH0);
            if(thuoc.isPresent()){
                item.setNhomNganhHangId(thuoc.get().getNhomNganhHangId());
                item.setNhomDuocLyId(thuoc.get().getNhomDuocLyId());
                item.setNhomHoatChatId(thuoc.get().getNhomHoatChatId());
            }
            hdrRepo.save(item);
            ids.add(item.getId());
            //danh dau hang da luan chuyen
            var pn = phieuNhapChiTietsRepository.findByMaPhieuNhapCt(Long.valueOf(x.getMaPhieuNhapCt()));
            pn.setHangLuanChuyen(item.getId() > 0);
            phieuNhapChiTietsRepository.save(pn);
        });
        sendNotificationCoSoLanCan(ids, userInfo.getMaCoSo());
     return true;
    }

    public boolean deleteHangHoaLuanChuyen(Long maPhieuChiTiet) throws Exception{
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        //kiểm tra hàng có trên danh sách luôn chuyển không
        var data = hdrRepo.findByMaPhieuNhapCTAndRecordStatusId(maPhieuChiTiet, RecordStatusContains.ACTIVE);
        if(data.isEmpty()) return false;

        //cập nhật trạng thái xoá
        data.get(0).setRecordStatusId(RecordStatusContains.DELETED);
        data.get(0).setModified(new Date());
        data.get(0).setModifiedByUserId(userInfo.getId());
        hdrRepo.save(data.get(0));
        //cập nhật lại trạng thái phiếu
        var pnct = phieuNhapChiTietsRepository.findByMaPhieuNhapCt(maPhieuChiTiet);
        if(pnct != null){
            pnct.setHangLuanChuyen(null);
            phieuNhapChiTietsRepository.save(pnct);
        }
        return true;
    }

    private void sendNotificationCoSoLanCan(List<Long> ids, String maCoSo) throws ExecutionException, InterruptedException, TimeoutException {
        int size = ids.size();
        int index = 1;
        UUID uuid = UUID.randomUUID();
        String key = maCoSo;
        String bathKey = uuid.toString();
        var object = new DataType();
        object.setType(NotificationContains.THONG_BAO_LIEN_MINH);
        //object.setIds(ids);
        WrapData data = new WrapData();
        data.setBathKey(bathKey);
        data.setCode(JobContains.THONG_BAO);
        data.setSendDate(new Date());
        data.setData(object);
        data.setIndex(index);
        this.kafkaProducer.sendInternal(topicName, key, new Gson().toJson(data));
    }


}
