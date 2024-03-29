package com.finalproject.walletforex.repository;

import com.finalproject.walletforex.model.Kurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KursRepository extends JpaRepository<Kurs, Integer> {
    List<Kurs> findByCcy1AndCcy2(String ccy1, String ccy2);
    Kurs findByCcy2(String ccy);
    @Query("SELECT k FROM Kurs k WHERE k.date = (SELECT MAX(ku.date) FROM Kurs ku) AND k.ccy1= :curency1 AND k.ccy2= :curency2")
    Kurs findNewestKurs(
            @Param("curency1") String ccy1,
            @Param("curency2") String ccy2
    );
}
