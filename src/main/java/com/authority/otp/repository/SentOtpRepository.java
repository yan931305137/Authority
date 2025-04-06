package com.authority.otp.repository;

import com.authority.otp.dto.SentOtp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * 标记该接口为Spring的Repository，用于数据访问。
 */
@Repository
public interface SentOtpRepository extends CrudRepository<SentOtp, UUID> {
    /**
     * 根据目的地和最后发送时间降序排序，查找第一个SentOtp实体。
     * 
     * @param destination 目的地
     * @return Optional<SentOtp> 包含查找到的SentOtp实体的Optional对象
     */
    Optional<SentOtp> findFirstByDestinationOrderByLastSentAtDesc(String destination);
}
