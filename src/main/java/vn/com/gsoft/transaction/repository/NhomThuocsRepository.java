package vn.com.gsoft.transaction.repository;

import com.google.gson.Gson;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.transaction.entity.NhomThuocs;

import java.util.List;

@Repository
public interface NhomThuocsRepository extends CrudRepository<NhomThuocs, Long> {
}
