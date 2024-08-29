package vn.com.gsoft.transaction.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.stream.StreamSupport;


@Service
@Log4j2
public class PhieuDuTruServiceImpl extends BaseServiceImpl<PhieuDuTru, PhieuDuTruReq, Long> implements PhieuDuTruService {

    private final NhaThuocsRepository nhaThuocsRepository;
    private PhieuDuTruRepository hdrRepo;
    private PhieuDuTruChiTietRepository dtlRepo;
    private InventoryRepository inventoryRepository;
    private ThuocsRepository thuocsRepository;
    private NhomThuocsRepository nhomThuocsRepository;
    private DonViTinhsRepository donViTinhsRepository;
    private PhieuNhapChiTietsRepository phieuNhapChiTietsRepository;
    private PhieuXuatChiTietsRepository phieuXuatChiTietsRepository;
    @Autowired
    private RedisListService redisListService;
    @Autowired
    private GiaoDichHangHoaServiceImpl giaoDichHangHoaService;

    @Autowired
    public PhieuDuTruServiceImpl(PhieuDuTruRepository hdrRepo,
                                 PhieuDuTruChiTietRepository phieuDuTruChiTietRepository,
                                 InventoryRepository inventoryRepository,
                                 ThuocsRepository thuocsRepository,
                                 NhomThuocsRepository nhomThuocsRepository,
                                 DonViTinhsRepository donViTinhsRepository,
                                 NhaThuocsRepository nhaThuocsRepository,
                                 PhieuNhapChiTietsRepository phieuNhapChiTietsRepository,
                                 PhieuXuatChiTietsRepository phieuXuatChiTietsRepository) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.dtlRepo = phieuDuTruChiTietRepository;
        this.inventoryRepository = inventoryRepository;
        this.thuocsRepository = thuocsRepository;
        this.nhomThuocsRepository = nhomThuocsRepository;
        this.donViTinhsRepository = donViTinhsRepository;
        this.nhaThuocsRepository = nhaThuocsRepository;
        this.phieuNhapChiTietsRepository = phieuNhapChiTietsRepository;
        this.phieuXuatChiTietsRepository = phieuXuatChiTietsRepository;
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
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }

        // Prepare request for GiaoDichHangHoa
        var req = new GiaoDichHangHoaReq();
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, -3);
        req.setToDate(new Date());
        req.setFromDate(date.getTime());

        Map<Long, BigDecimal> receiptQuantities = calculateReceiptQuantities(userInfo.getMaCoSo(), req.getFromDate(), req.getToDate());
        Map<Long, BigDecimal> deliveryQuantities = calculateDeliveryQuantities(userInfo.getMaCoSo(), req.getFromDate(), req.getToDate());

        // Get drugIds
        List<Long> drugIds = getUniqueDrugIds(receiptQuantities, deliveryQuantities);

        Map<Long, DrugWarehouseSynthesisRes> warehouseSyntheses = getWarehouseSyntheses(drugIds, receiptQuantities, deliveryQuantities);

        // Calculate inventory turnover values
        Map<Long, BigDecimal> turnoverValues = getGiaTriVongQuay(warehouseSyntheses);

        // Map the results to HangDuTruRes objects
        List<HangDuTruRes> result = drugIds.stream()
                .map(id -> new HangDuTruRes(
                        id,
                        warehouseSyntheses.containsKey(id) ? warehouseSyntheses.get(id).getTenThuoc() : "",
                        warehouseSyntheses.containsKey(id) ? warehouseSyntheses.get(id).getTenNhomThuoc() : "",
                        warehouseSyntheses.containsKey(id) ? warehouseSyntheses.get(id).getTenDonViTinh() : "",
                        warehouseSyntheses.containsKey(id) ? warehouseSyntheses.get(id).getGiaNhap() : BigDecimal.ZERO,
                        turnoverValues.getOrDefault(id, BigDecimal.ZERO) // Set deXuatDuTru with the turnover value
                ))
                .filter(hangDuTruRes -> hangDuTruRes.getDeXuatDuTru().compareTo(BigDecimal.ZERO) > 0) // Lọc các đối tượng có deXuatDuTru > 0
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

    @Override
    public Page<Thuocs> colectionPageHangDuTru(ThuocsReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        req.setNhaThuocMaNhaThuoc(userInfo.getMaCoSo());

        if (req.getDataDelete() != null) {
            req.setRecordStatusId(req.getDataDelete() ? RecordStatusContains.DELETED : RecordStatusContains.ACTIVE);
        }
        Page<Thuocs> thuocs = thuocsRepository.colectionPagePhieuDuTru(req, pageable);
        thuocs.getContent().forEach(item -> {
            if (item.getNhomThuocMaNhomThuoc() != null) {
                Optional<NhomThuocs> byIdNt = nhomThuocsRepository.findById(item.getNhomThuocMaNhomThuoc());
                byIdNt.ifPresent(nhomThuocs -> item.setTenNhomThuoc(nhomThuocs.getTenNhomThuoc()));
            }
            if (item.getDonViThuNguyenMaDonViTinh() != null) {
                Optional<DonViTinhs> byIdNt = donViTinhsRepository.findByMaDonViTinh(Math.toIntExact(item.getDonViThuNguyenMaDonViTinh()));
                byIdNt.ifPresent(donViTinhs -> item.setTenDonViTinhThuNguyen(donViTinhs.getTenDonViTinh()));
            }
            if (item.getDonViXuatLeMaDonViTinh() != null) {
                Optional<DonViTinhs> byIdNt = donViTinhsRepository.findByMaDonViTinh(Math.toIntExact(item.getDonViXuatLeMaDonViTinh()));
                byIdNt.ifPresent(donViTinhs -> item.setTenDonViTinhXuatLe(donViTinhs.getTenDonViTinh()));
            }
//            InventoryReq inventoryReq = new InventoryReq();
//            inventoryReq.setDrugID(item.getId());
//            inventoryReq.setDrugStoreID(item.getNhaThuocMaNhaThuoc());
//            inventoryReq.setRecordStatusID(RecordStatusContains.ACTIVE);
//            HashMap<Integer, Double> inventory = getTotalInventory(inventoryReq);
//            item.setLastValue(inventory.get(item.getId().intValue()));
        });
        return thuocs;
    }

    @Override
    public List<PhieuDuTru> createNhaCC(List<PhieuDuTruReq> req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null) {
            throw new Exception("Bad request.");
        }
        List<PhieuDuTru> savedRecords = new ArrayList<>();
        for (PhieuDuTruReq data : req) {
            data.setMaNhaThuoc(userInfo.getMaCoSo());
            data.setRecordStatusId(RecordStatusContains.ACTIVE);
            PhieuDuTru hdr = new PhieuDuTru();
            BeanUtils.copyProperties(data, hdr, "id");
            hdr.setCreated(new Date());
            hdr.setCreatedByUserId(userInfo.getId());
            PhieuDuTru save = hdrRepo.save(hdr);
            List<PhieuDuTruChiTiet> phieuNhapChiTiets = saveChildren(save.getId(), data);
            save.setChiTiets(phieuNhapChiTiets);
            savedRecords.add(hdrRepo.save(save));
        }
        return savedRecords;
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
                if(thuocs.getDonViThuNguyenMaDonViTinh() != null && thuocs.getDonViThuNguyenMaDonViTinh() > 0){
                    chiTiet.setMaDonViDuTru(thuocs.getDonViThuNguyenMaDonViTinh());
                }
                else {
                    chiTiet.setMaDonViDuTru(thuocs.getDonViXuatLeMaDonViTinh());
                }
                chiTiet.setSoLuongCanhBao(BigDecimal.valueOf(thuocs.getGioiHan()));
            });

            Optional<Inventory> byIdInventory = inventoryRepository.findFirstByDrugStoreIDAndDrugID(req.getMaNhaThuoc(), chiTiet.getMaThuoc());
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

    private Map<Long, BigDecimal> getGiaTriVongQuay(Map<Long, DrugWarehouseSynthesisRes> drugWarehouseSyntheses) {
        // Calculate inventory turnover for each drug
        return drugWarehouseSyntheses.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            DrugWarehouseSynthesisRes synthesis = entry.getValue();

                            // Calculate average inventory
                            BigDecimal averageInventoryValue = (synthesis.getFirstInventoryValue().add(synthesis.getLastInventoryValue()))
                                    .divide(new BigDecimal(2), BigDecimal.ROUND_HALF_UP);

                            // Avoid division by zero
                            if (averageInventoryValue.compareTo(BigDecimal.ZERO) == 0) {
                                return BigDecimal.ZERO;
                            }

                            // Calculate inventory turnover ratio
                            BigDecimal turnoverRatio = synthesis.getDeliveryInventoryValueInPeriod()
                                    .divide(averageInventoryValue, BigDecimal.ROUND_UP);

                            if (turnoverRatio.compareTo(BigDecimal.ZERO) == 0) {
                                return BigDecimal.ZERO;
                            } else if (turnoverRatio.compareTo(BigDecimal.ONE) < 0) {
                                turnoverRatio = BigDecimal.ONE;
                            }

                            // Calculate 90 / turnoverRatio and round to the nearest integer
                            BigDecimal result = new BigDecimal("90").divide(turnoverRatio, BigDecimal.ROUND_HALF_UP);
                            return result.setScale(0, BigDecimal.ROUND_HALF_UP); // Round to the nearest whole number
                        }
                ));
    }

    private Map<Long, DrugWarehouseSynthesisRes> getWarehouseSyntheses(List<Long> drugIds, Map<Long, BigDecimal> receiptQuantities, Map<Long, BigDecimal> deliveryQuantities) {
        Map<Long, DrugWarehouseSynthesisRes> drugWarehouseSyntheses = new HashMap<>();

        Iterable<Thuocs> validDrugsIterable = thuocsRepository.findAllById(drugIds);
        List<Thuocs> validDrugs = StreamSupport.stream(validDrugsIterable.spliterator(), false)
                .collect(Collectors.toList());

        Map<Long, Thuocs> drugItems = validDrugs.stream()
                .collect(Collectors.toMap(
                        Thuocs::getId, // Assuming getId() method exists to retrieve the ID of Thuocs
                        thuocs -> thuocs
                ));

        for (Thuocs i : drugItems.values()) {
            // Khởi tạo và tính toán FirstInventoryValue
            DrugWarehouseSynthesisRes synthesis = new DrugWarehouseSynthesisRes();
            synthesis.setDrugId(i.getId());
            synthesis.setFirstInventoryValue(i.getSoDuDauKy().multiply(i.getGiaDauKy()));
            synthesis.setTenThuoc(i.getTenThuoc());
            synthesis.setGiaNhap(i.getHeSo() > 1 ? i.getGiaNhap().multiply(BigDecimal.valueOf(i.getHeSo())) : i.getGiaNhap());
            if (i.getNhomThuocMaNhomThuoc() != null) {
                Optional<NhomThuocs> byIdNt = nhomThuocsRepository.findById(i.getNhomThuocMaNhomThuoc());
                byIdNt.ifPresent(nhomThuocs -> synthesis.setTenNhomThuoc(nhomThuocs.getTenNhomThuoc()));
            }
            var maDonViTinh = i.getDonViThuNguyenMaDonViTinh() != null && i.getDonViThuNguyenMaDonViTinh() > 0 ? i.getDonViThuNguyenMaDonViTinh() : i.getDonViXuatLeMaDonViTinh();
            if (maDonViTinh != null) {
                Optional<DonViTinhs> byId = donViTinhsRepository.findByMaDonViTinh(Math.toIntExact(maDonViTinh));
                byId.ifPresent(donViTinhs -> synthesis.setTenDonViTinh(donViTinhs.getTenDonViTinh()));
            }
            // Tính toán receiptQuantity và deliveryQuantity
            BigDecimal receiptQuantity = receiptQuantities.getOrDefault(i.getId(), BigDecimal.ZERO);
            BigDecimal deliveryQuantity = deliveryQuantities.getOrDefault(i.getId(), BigDecimal.ZERO);

            // Tính toán inventoryQuantity
            BigDecimal inventoryQuantity = receiptQuantity.compareTo(BigDecimal.ZERO) > 0
                    ? synthesis.getFirstInventoryValue().divide(receiptQuantity, 3, BigDecimal.ROUND_HALF_UP)
                    : BigDecimal.ZERO;
            BigDecimal inventoryValue = BigDecimal.ZERO;

            // Tính toán LastInventoryValue
            if (inventoryQuantity.compareTo(new BigDecimal("0.001")) > 0) {
                BigDecimal additionalValue = receiptQuantity.multiply(i.getGiaDauKy());
                inventoryValue = inventoryValue.add(additionalValue);
            } else {
                BigDecimal additionalValue = synthesis.getFirstInventoryValue().multiply(i.getGiaDauKy());
                inventoryValue = inventoryValue.add(additionalValue);
            }
            synthesis.setLastInventoryValue(inventoryValue);

            // Tính toán DeliveryInventoryValueInPeriod
            synthesis.setDeliveryInventoryValueInPeriod(deliveryQuantity.multiply(i.getGiaBanLe()));

            // Thêm kết quả vào drugWarehouseSyntheses
            drugWarehouseSyntheses.put(i.getId(), synthesis);
        }

        return drugWarehouseSyntheses;
    }

    private Map<Long, BigDecimal> calculateReceiptQuantities(String storeCode, Date fromDate, Date toDate) {
        // Fetch receipt items based on storeCode, fromDate, toDate
        List<PhieuNhapChiTiets> phieuNhapChiTiets = phieuNhapChiTietsRepository.findByStoreCodeAndDateRange(storeCode, fromDate, toDate);

        // Filter and group receipt quantities by drugId
        return phieuNhapChiTiets.stream()
                .collect(Collectors.groupingBy(
                        PhieuNhapChiTiets::getThuocThuocId,
                        Collectors.reducing(BigDecimal.ZERO, PhieuNhapChiTiets::getSoLuong, BigDecimal::add)
                ));
    }

    private Map<Long, BigDecimal> calculateDeliveryQuantities(String storeCode, Date fromDate, Date toDate) {
        // Fetch delivery items based on storeCode, fromDate, toDate
        List<PhieuXuatChiTiets> phieuXuatChiTiets = phieuXuatChiTietsRepository.findByStoreCodeAndDateRange(storeCode, fromDate, toDate);

        // Filter and group delivery quantities by drugId
        return phieuXuatChiTiets.stream()
                .collect(Collectors.groupingBy(
                        PhieuXuatChiTiets::getThuocThuocId,
                        Collectors.reducing(BigDecimal.ZERO, PhieuXuatChiTiets::getSoLuong, BigDecimal::add)
                ));
    }

    private List<Long> getUniqueDrugIds(Map<Long, BigDecimal> receiptQuantities, Map<Long, BigDecimal> deliveryQuantities) {
        // Use a Set to store unique drugIds from both maps
        Set<Long> drugIdsSet = new HashSet<>();

        // Add all keys from receiptQuantities
        drugIdsSet.addAll(receiptQuantities.keySet());

        // Add all keys from deliveryQuantities (duplicates will be ignored by the Set)
        drugIdsSet.addAll(deliveryQuantities.keySet());

        // Convert the Set to a List
        return new ArrayList<>(drugIdsSet);
    }
}