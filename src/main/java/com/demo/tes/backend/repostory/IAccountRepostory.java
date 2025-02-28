package com.demo.tes.backend.repostory;

import com.demo.tes.backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountRepostory extends JpaRepository<Account, Long> {
}
