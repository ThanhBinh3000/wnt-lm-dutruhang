package vn.com.gsoft.transaction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.com.gsoft.transaction.entity.GiaoDichHangHoa;
import vn.com.gsoft.transaction.model.dto.GiaoDichHangHoaReq;
import vn.com.gsoft.transaction.model.dto.TopMatHangRes;
import vn.com.gsoft.transaction.service.RedisListService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RedisListServiceImpl implements RedisListService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    // Lấy toàn bộ danh sách giá trị
    public List<Object> getGiaoDichHangHoaValues(GiaoDichHangHoaReq req) {
        var fromDate = Double.parseDouble(new SimpleDateFormat("yyyyMMdd").format(req.getFromDate()));
        var toDate = Double.parseDouble(new SimpleDateFormat("yyyyMMdd").format(req.getToDate()));
        Set<Object> results = redisTemplate.opsForZSet().rangeByScore("transactions", fromDate, toDate);
        return Arrays.asList(results.toArray());
    }
    public void pushDataRedis(List<GiaoDichHangHoa> giaoDichHangHoas){
        giaoDichHangHoas.forEach(x->{
            var timestamp = Double.parseDouble(new SimpleDateFormat("yyyyMMdd").format(x.getNgayGiaoDich()));
            redisTemplate.opsForZSet().add("transactions", x, timestamp);
        });
    }
    public List<TopMatHangRes> getAllDataFromRedis() {
        Set<String> keys = redisTemplate.keys("top-sl-3-thang-gan-nhat:*");

        return keys.stream()
                .map(key -> (TopMatHangRes) redisTemplate.opsForValue().get(key))
                .collect(Collectors.toList());
    }
}
