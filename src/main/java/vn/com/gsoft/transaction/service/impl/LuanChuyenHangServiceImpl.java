package vn.com.gsoft.transaction.service.impl;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.transaction.entity.DonViTinhs;
import vn.com.gsoft.transaction.entity.PhieuNhapChiTiets;
import vn.com.gsoft.transaction.entity.Thuocs;
import vn.com.gsoft.transaction.model.dto.HangHoaLuanChuyenRes;
import vn.com.gsoft.transaction.model.dto.PhieuNhapChiTietsReq;
import vn.com.gsoft.transaction.model.system.Profile;
import vn.com.gsoft.transaction.repository.DonViTinhsRepository;
import vn.com.gsoft.transaction.repository.PhieuNhapChiTietsRepository;
import vn.com.gsoft.transaction.repository.ThuocsRepository;
import vn.com.gsoft.transaction.service.LuanChuyenHangService;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class LuanChuyenHangServiceImpl extends BaseServiceImpl<PhieuNhapChiTiets, PhieuNhapChiTietsReq, Integer> implements LuanChuyenHangService {
    @Autowired
    private PhieuNhapChiTietsRepository hdrRepo;
    @Autowired
    private ThuocsRepository thuocsRepository;
    @Autowired
    private DonViTinhsRepository donViTinhsRepository;

    @Autowired
    public LuanChuyenHangServiceImpl(PhieuNhapChiTietsRepository hdrRepo
                                     , ThuocsRepository thuocsRepository
                                     , DonViTinhsRepository donViTinhsRepository
    ) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.thuocsRepository = thuocsRepository;
        this.donViTinhsRepository = donViTinhsRepository;
    }

    public Page<PhieuNhapChiTiets> searchListHangCanHan(PhieuNhapChiTietsReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        //lấy ra danh sách hàng cận hạn
        Calendar warnDate = Calendar.getInstance();
        warnDate.add(Calendar.MONTH, 6);
        req.setNhaThuocMaNhaThuoc("0010");
        req.setWarnDate(warnDate.getTime());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

        var dsCanHan = hdrRepo.searchPageHangCanHan(req, pageable);
        //gán thông tin thuốc
        dsCanHan.forEach(x->{
            Optional<Thuocs> thuoc = thuocsRepository.findById(x.getThuocThuocId());
            if(thuoc.isPresent()) {
               x.setTenThuoc(thuoc.get().getTenThuoc());
               x.setThuocDMCId(thuoc.get().getGroupIdMapping());
            }
            var donViTinh = donViTinhsRepository.findByMaDonViTinh(x.getDonViTinhMaDonViTinh());
            donViTinh.ifPresent(donViTinhs -> x.setTenDonVi(donViTinhs.getTenDonViTinh()));
        });

        return dsCanHan;
    }

    public Page<PhieuNhapChiTiets> searchListHangItGiaoDich(PhieuNhapChiTietsReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        //lấy ra danh sách hàng cận hạn
        Calendar warnDate = Calendar.getInstance();
        warnDate.add(Calendar.MONTH, -6);
        req.setNhaThuocMaNhaThuoc(userInfo.getMaCoSo());
        req.setWarnDate(warnDate.getTime());
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

        var dsCanHan = hdrRepo.searchPageHangItGiaoDich(req, pageable);
        //gán thông tin thuốc
        dsCanHan.forEach(x->{
            Optional<Thuocs> thuoc = thuocsRepository.findById(x.getThuocThuocId());
            if(thuoc.isPresent()) {
                x.setTenThuoc(thuoc.get().getTenThuoc());
                x.setThuocDMCId(thuoc.get().getGroupIdMapping());
            }
            var donViTinh = donViTinhsRepository.findByMaDonViTinh(x.getDonViTinhMaDonViTinh());
            donViTinh.ifPresent(donViTinhs -> x.setTenDonVi(donViTinhs.getTenDonViTinh()));
        });

        return dsCanHan;
    }
}
