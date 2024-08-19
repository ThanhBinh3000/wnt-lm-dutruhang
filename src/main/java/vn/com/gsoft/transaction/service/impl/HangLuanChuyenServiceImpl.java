package vn.com.gsoft.transaction.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.transaction.constant.RecordStatusContains;
import vn.com.gsoft.transaction.entity.HangHoaLuanChuyen;
import vn.com.gsoft.transaction.entity.PhieuNhapChiTiets;
import vn.com.gsoft.transaction.entity.Thuocs;
import vn.com.gsoft.transaction.model.dto.HangHoaLuanChuyenReq;
import vn.com.gsoft.transaction.model.system.Profile;
import vn.com.gsoft.transaction.repository.HangHoaLuanChuyenRepository;
import vn.com.gsoft.transaction.repository.PhieuNhapChiTietsRepository;
import vn.com.gsoft.transaction.repository.ThuocsRepository;
import vn.com.gsoft.transaction.service.HangHoaLuanChuyenService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

        var ds = hdrRepo.searchPage(req, pageable);
        //gán thông tin thuốc
        ds.forEach(x->{
            Optional<Thuocs> thuoc = thuocsRepository.findById(Long.valueOf(x.getThuocId()));
            thuoc.ifPresent(thuocs -> x.setTenThuoc(thuocs.getTenThuoc()));
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
        hangLuanChuyens.forEach(x->{
            HangHoaLuanChuyen item = new HangHoaLuanChuyen();
            item.setThuocId(x.getThuocDMCId() > 0 ? x.getThuocDMCId() : x.getThuocThuocId());
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
            item.setSoLuong(x.getSoLuong());
            hdrRepo.save(item);
            //danh dau hang da luan chuyen
            var pn = phieuNhapChiTietsRepository.findByMaPhieuNhapCt(x.getMaPhieuNhapCt());
            pn.setHangLuanChuyen(item.getId() > 0);
            phieuNhapChiTietsRepository.save(pn);
            //kafka gui thong bao toi co so nan can
        });
     return true;
    }

}
