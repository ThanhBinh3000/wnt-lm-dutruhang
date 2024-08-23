package vn.com.gsoft.transaction.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.transaction.constant.LoaiHangContains;
import vn.com.gsoft.transaction.constant.RecordStatusContains;
import vn.com.gsoft.transaction.entity.PhieuNhapChiTiets;
import vn.com.gsoft.transaction.entity.PhieuNhaps;
import vn.com.gsoft.transaction.entity.Thuocs;
import vn.com.gsoft.transaction.model.dto.PhieuNhapChiTietsReq;
import vn.com.gsoft.transaction.model.system.Profile;
import vn.com.gsoft.transaction.repository.*;
import vn.com.gsoft.transaction.service.LuanChuyenService;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@Log4j2
public class LuanChuyenServiceImpl extends BaseServiceImpl<PhieuNhapChiTiets, PhieuNhapChiTietsReq, Integer> implements LuanChuyenService {
    @Autowired
    private PhieuNhapChiTietsRepository hdrRepo;
    @Autowired
    private ThuocsRepository thuocsRepository;
    @Autowired
    private DonViTinhsRepository donViTinhsRepository;
    @Autowired
    private PhieuNhapsRepository phieuNhapsRepository;
    @Autowired
    HangHoaLuanChuyenRepository hoaLuanChuyenRepository;

    @Autowired
    public LuanChuyenServiceImpl(PhieuNhapChiTietsRepository hdrRepo
                                     , ThuocsRepository thuocsRepository
                                     , DonViTinhsRepository donViTinhsRepository
                                     , PhieuNhapsRepository phieuNhapsRepository
                                     , HangHoaLuanChuyenRepository hoaLuanChuyenRepository

    ) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.thuocsRepository = thuocsRepository;
        this.donViTinhsRepository = donViTinhsRepository;
        this.phieuNhapsRepository = phieuNhapsRepository;
        this.hoaLuanChuyenRepository = hoaLuanChuyenRepository;
    }

    @Override
    public Page<PhieuNhapChiTiets> searchPageHangCanHan(PhieuNhapChiTietsReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        //lấy ra danh sách hàng cận hạn
        Calendar warnDate = Calendar.getInstance();
        warnDate.add(Calendar.MONTH, 6);
        req.setNhaThuocMaNhaThuoc("0010");
        req.setWarnDate(warnDate.getTime());
        req.setRecordStatusId(RecordStatusContains.ACTIVE);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

        var dsCanHan = hdrRepo.searchPageHangCanHan(req, pageable);
        //gán thông tin thuốc
        dsCanHan.forEach(x->{
            Optional<Thuocs> thuoc = thuocsRepository.findById(Long.valueOf(x.getThuocThuocId()));
            if(thuoc.isPresent()) {
               x.setTenThuoc(thuoc.get().getTenThuoc());
               x.setThuocDMCId(thuoc.get().getGroupIdMapping());
               x.setSoDangKy(thuoc.get().getRegisteredNo());
               x.setMaThuoc(thuoc.get().getMaThuoc());
            }
            var donViTinh = donViTinhsRepository.findByMaDonViTinh(x.getDonViTinhMaDonViTinh());
            donViTinh.ifPresent(donViTinhs -> x.setTenDonVi(donViTinhs.getTenDonViTinh()));
            x.setLoaiHang(LoaiHangContains.CAN_HAN);
            Optional<PhieuNhaps> pn = phieuNhapsRepository.findById(x.getPhieuNhapMaPhieuNhap());
            if(pn.isPresent()) {
                x.setNgayNhap(pn.get().getNgayNhap());
                x.setSoPhieuNhap(Math.toIntExact(pn.get().getSoPhieuNhap()));
            }
            if(req.getHangLuanChuyen() != null && req.getHangLuanChuyen()){
                var hlc = hoaLuanChuyenRepository
                        .findByMaPhieuNhapCTAndRecordStatusId(Long.valueOf(x.getMaPhieuNhapCt()), RecordStatusContains.ACTIVE);
                if(hlc != null) {
                    x.setDecscription(hlc.get(0).getGhiChu());
                }
            }
        });

        return dsCanHan;
    }

    @Override
    public Page<PhieuNhapChiTiets> searchPageHangItGiaoDich(PhieuNhapChiTietsReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        //lấy ra danh sách hàng cận hạn
        var curentDate = new Date();
        Calendar warnDate = Calendar.getInstance();
        warnDate.add(Calendar.MONTH, -6);
        req.setNhaThuocMaNhaThuoc(userInfo.getMaCoSo());
        req.setWarnDate(warnDate.getTime());
        req.setRecordStatusId(RecordStatusContains.ACTIVE);
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

        var dsCanHan = hdrRepo.searchPageHangItGiaoDich(req, pageable);
        //gán thông tin thuốc
        dsCanHan.forEach(x->{
            Optional<Thuocs> thuoc = thuocsRepository.findById(Long.valueOf(x.getThuocThuocId()));
            if(thuoc.isPresent()) {
                x.setTenThuoc(thuoc.get().getTenThuoc());
                x.setThuocDMCId(thuoc.get().getGroupIdMapping());
                x.setSoDangKy(thuoc.get().getRegisteredNo());
                x.setMaThuoc(thuoc.get().getMaThuoc());
            }
            var donViTinh = donViTinhsRepository.findByMaDonViTinh(x.getDonViTinhMaDonViTinh());
            donViTinh.ifPresent(donViTinhs -> x.setTenDonVi(donViTinhs.getTenDonViTinh()));
            x.setLoaiHang(LoaiHangContains.IT_GIAO_DICH);
            Optional<PhieuNhaps> pn = phieuNhapsRepository.findById(x.getPhieuNhapMaPhieuNhap());
            if(pn.isPresent()) {
                x.setNgayNhap(pn.get().getNgayNhap());
                x.setSoPhieuNhap(Math.toIntExact(pn.get().getSoPhieuNhap()));
            }
            if(req.getHangLuanChuyen() != null && req.getHangLuanChuyen()){
                var hlc = hoaLuanChuyenRepository
                        .findByMaPhieuNhapCTAndRecordStatusId(Long.valueOf(x.getMaPhieuNhapCt()), RecordStatusContains.ACTIVE);
                if(hlc != null) {
                    x.setDecscription(hlc.get(0).getGhiChu());
                }
            }
            //tính ngày
            long day = ChronoUnit.DAYS.between(x.getNgayNhap().toInstant(), curentDate.toInstant());
            x.setSoNgayKhongGiaoDich(day);

        });

        return dsCanHan;
    }

}
