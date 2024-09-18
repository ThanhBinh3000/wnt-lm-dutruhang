package vn.com.gsoft.transaction.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.transaction.entity.BaoCaoKhoHang;
import vn.com.gsoft.transaction.entity.DonViTinhs;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaoCaoKhoHangRepository extends CrudRepository<BaoCaoKhoHang, Integer> {
   List<BaoCaoKhoHang> findByMaNhaThuoc(String maNhaThuoc);
}
