package vn.com.gsoft.transaction.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.transaction.constant.RecordStatusContains;
import vn.com.gsoft.transaction.entity.*;
import vn.com.gsoft.transaction.model.dto.*;
import vn.com.gsoft.transaction.model.system.Profile;
import vn.com.gsoft.transaction.repository.*;
import vn.com.gsoft.transaction.service.GiaoDichHangHoaService;
import vn.com.gsoft.transaction.service.PhieuDuTruService;
import vn.com.gsoft.transaction.service.RedisListService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Log4j2
public class PhieuDuTruServiceImpl extends BaseServiceImpl<PhieuDuTru, PhieuDuTruReq, Long> implements PhieuDuTruService {

    private final NhaThuocsRepository nhaThuocsRepository;
    private PhieuDuTruRepository hdrRepo;
    private PhieuDuTruChiTietRepository dtlRepo;
    private InventoryRepository inventoryRepository;
    private ThuocsRepository thuocsRepository;
    //private NhomThuocsRepository nhomThuocsRepository;
    private DonViTinhsRepository donViTinhsRepository;
    @Autowired
    private RedisListService redisListService;
    @Autowired
    private GiaoDichHangHoaServiceImpl giaoDichHangHoaService;

    @Autowired
    public PhieuDuTruServiceImpl(PhieuDuTruRepository hdrRepo,
                                 PhieuDuTruChiTietRepository phieuDuTruChiTietRepository,
                                InventoryRepository inventoryRepository,
                                 ThuocsRepository thuocsRepository,
                                 //NhomThuocsRepository nhomThuocsRepository,
                                 DonViTinhsRepository donViTinhsRepository,
                                 NhaThuocsRepository nhaThuocsRepository) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.dtlRepo = phieuDuTruChiTietRepository;
        this.inventoryRepository = inventoryRepository;
        this.thuocsRepository = thuocsRepository;
        //this.nhomThuocsRepository = nhomThuocsRepository;
        this.donViTinhsRepository = donViTinhsRepository;
        this.nhaThuocsRepository = nhaThuocsRepository;
    }

    @Override
    public PhieuDuTru create(PhieuDuTruReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        var storeCode = userInfo.getMaCoSo();
        req.setMaNhaThuoc(storeCode);
        req.setRecordStatusId(RecordStatusContains.ACTIVE);
        PhieuDuTru hdr = new PhieuDuTru();
        BeanUtils.copyProperties(req, hdr, "id");
        hdr.setCreated(new Date());
        hdr.setNgayTao(new Date());
        hdr.setCreatedByUserId(getLoggedUser().getId());
        hdr.setSoPhieu(getReserveNoteNumber(storeCode));
        PhieuDuTru save = hdrRepo.save(hdr);
        List<PhieuDuTruChiTiet> chiTiets = saveChildren(save.getId(), req);
        save.setChiTiets(chiTiets);
        return save;
    }

    @Override
    public PhieuDuTru detail(Long id) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");

        Optional<PhieuDuTru> optional = hdrRepo.findById(id);
        if (optional.isEmpty()) {
            throw new Exception("Không tìm thấy dữ liệu.");
        } else {
            if (optional.get().getRecordStatusId() != RecordStatusContains.ACTIVE) {
                throw new Exception("Không tìm thấy dữ liệu.");
            }
        }
        PhieuDuTru phieuDuTru = optional.get();
        if (phieuDuTru.getCreatedByUserId() != null && phieuDuTru.getCreatedByUserId() > 0) {
            phieuDuTru.setCreatedByUserText(userInfo.getFullName());
        }
        phieuDuTru.setChiTiets(dtlRepo.findByMaPhieuDuTru(phieuDuTru.getId()));
        for (PhieuDuTruChiTiet ct : phieuDuTru.getChiTiets()) {
            if (ct.getMaThuoc() != null && ct.getMaThuoc() > 0) {
                Optional<Thuocs> thuocs = thuocsRepository.findById(ct.getMaThuoc());
                if (thuocs.isPresent()) {
                    ct.setMaThuocText(thuocs.get().getMaThuoc());
                    ct.setTenThuocText(thuocs.get().getTenThuoc());
                }
            }
            if (ct.getMaDonViDuTru() != null && ct.getMaDonViDuTru() > 0) {
                Optional<DonViTinhs> donViTinhs = donViTinhsRepository.findById(Math.toIntExact(ct.getMaDonViDuTru()));
                donViTinhs.ifPresent(viTinhs -> ct.setMaDonViDuTruText(viTinhs.getTenDonViTinh()));
            }
            if (ct.getMaDonViTon() != null && ct.getMaDonViTon() > 0) {
                Optional<DonViTinhs> donViTinhs = donViTinhsRepository.findById(Math.toIntExact(ct.getMaDonViTon()));
                donViTinhs.ifPresent(viTinhs -> ct.setMaDonViTonText(viTinhs.getTenDonViTinh()));
            }
        }
        return phieuDuTru;
    }

    @Override
    public List<HangDuTruRes> searchListHangDuTru(HangDuTruReq objReq) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        var req = new GiaoDichHangHoaReq();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, -3);
        req.setToDate(new Date());
        req.setFromDate(date.getTime());
        List<GiaoDichHangHoa> giaoDichHangHoas = redisListService.getGiaoDichHangHoaValues(req).stream()
                .map(element->(GiaoDichHangHoa) element)
                .filter(thuoc -> userInfo.getMaCoSo().equals(thuoc.getMaCoSo()))
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                GiaoDichHangHoa::getThuocId,
                                thuoc -> thuoc,
                                (existing, replacement) -> existing),  // Giữ thuốc đầu tiên nếu trùng thuocId
                        map -> new ArrayList<>(map.values())
                ));
        List<HangDuTruRes> result = giaoDichHangHoas.stream()
                .map(thuoc -> new HangDuTruRes(thuoc.getThuocId(), thuoc.getTenThuoc(), thuoc.getTenNhomThuoc(), thuoc.getTenDonVi(), thuoc.getGiaNhap(), BigDecimal.ZERO))
                .toList();
        return result;
    }

    @Override
    public List<HangDuTruRes> searchListTop10HangBanChay(HangDuTruReq objReq) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        var req = new GiaoDichHangHoaReq();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, -3);
        req.setToDate(new Date());
        req.setFromDate(date.getTime());
        req.setPageSize(10);
        List<HangDuTruRes> topSoLuongBanChays = giaoDichHangHoaService.topSoLuongBanChay(req);
        return topSoLuongBanChays;
    }

    private List<PhieuDuTruChiTiet> saveChildren(Long idHdr, PhieuDuTruReq req) {
        // save chi tiết
        dtlRepo.deleteAllByMaPhieuDuTru(idHdr);
        for (PhieuDuTruChiTiet chiTiet : req.getChiTiets()) {
            chiTiet.setMaDonViTon(0L);
            chiTiet.setTonKho(BigDecimal.ZERO);
            chiTiet.setSoLuongCanhBao(BigDecimal.ZERO);
            Optional<Thuocs> byIdNt = thuocsRepository.findById(chiTiet.getMaThuoc());
            byIdNt.ifPresent(thuocs -> {
                if(thuocs.getDonViThuNguyenMaDonViTinh() > 0){
                    chiTiet.setMaDonViDuTru(thuocs.getDonViThuNguyenMaDonViTinh());
                }
                else {
                    chiTiet.setMaDonViDuTru(thuocs.getDonViXuatLeMaDonViTinh());
                }
                chiTiet.setSoLuongCanhBao(BigDecimal.valueOf(thuocs.getGioiHan()));
            });

            Optional<Inventory> byIdInventory = inventoryRepository.findByDrugStoreIDAndDrugID(req.getMaNhaThuoc(), chiTiet.getMaThuoc());
            byIdInventory.ifPresent(inventory -> {
                chiTiet.setMaDonViTon(Long.valueOf(inventory.getDrugUnitID()));
                chiTiet.setTonKho(BigDecimal.valueOf(inventory.getLastValue()));
            });
            chiTiet.setMaPhieuDuTru(idHdr);
            chiTiet.setRecordStatusId(RecordStatusContains.ACTIVE);
        }
        this.dtlRepo.saveAll(req.getChiTiets());
        return req.getChiTiets();
    }

    private int getReserveNoteNumber(String storeCode) {
        int result = 1;
        while (hdrRepo.existsByMaNhaThuocAndSoPhieu(storeCode, result)) {
            result++;
        }
        return result;
    }
}