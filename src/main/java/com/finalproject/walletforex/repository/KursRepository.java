package com.finalproject.walletforex.repository;

import com.finalproject.walletforex.model.Kurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KursRepository extends JpaRepository<Kurs, Integer> {
    Kurs findByCcy(String ccy);
}
