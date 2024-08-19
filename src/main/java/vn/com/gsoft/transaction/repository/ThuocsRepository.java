package vn.com.gsoft.transaction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.transaction.entity.Thuocs;
import vn.com.gsoft.transaction.entity.ApplicationSetting;
import vn.com.gsoft.transaction.model.dto.ThuocsReq;

import java.util.List;

@Repository
public interface ThuocsRepository extends CrudRepository<Thuocs, Long> {
}
