package com.authority.totp.repository;

import com.authority.totp.dto.RegisteredTotp;
import org.springframework.data.repository.CrudRepository;

public interface RegisteredTotpRepository extends CrudRepository<RegisteredTotp, String> {

}
