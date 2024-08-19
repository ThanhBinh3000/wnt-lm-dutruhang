package vn.com.gsoft.transaction.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.transaction.entity.DonViTinhs;

import java.util.Optional;

@Repository
public interface DonViTinhsRepository extends CrudRepository<DonViTinhs, Integer> {
   Optional<DonViTinhs> findByMaDonViTinh(Integer maDonViTinh);
}
