package com.horacerta.api.repositories;

import com.horacerta.api.entities.work.DailyWorkInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<DailyWorkInfo, Integer> {

    @Query("SELECT d FROM DailyWorkInfo d WHERE d.userId = :userId AND CAST(d.startedAt AS date) = CAST(:day AS date)")
    public DailyWorkInfo findUserWorkByGivenDay(@Param("userId") int userId, @Param("day") Date day);

//    public List<DailyWorkInfo> findUserWorkByGivenInterval (@Param("userId") int userId, @Param("start") Date start, @Param("end") Date end);

}
