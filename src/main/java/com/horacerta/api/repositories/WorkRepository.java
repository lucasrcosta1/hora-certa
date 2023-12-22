package com.horacerta.api.repositories;

import com.horacerta.api.entities.user.User;
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
public interface WorkRepository extends JpaRepository<DailyWorkInfo, Integer> {

    List<DailyWorkInfo> findAll();

    @Transactional
    @Modifying
    @Query("SELECT d FROM DailyWorkInfo d WHERE d.userId = :userId")
    List<DailyWorkInfo> findAllUserOccurences(@Param("userId") int userId);

    @Transactional
    @Modifying
//    @Query(value = "INSERT INTO daily_work_info (user_id, started_at, finished_at, lunch_started_at, lunch_finished_at, is_day_off, is_vacation, created_at, updated_at) " +
//            "VALUES (:userId, :startedAt, :finishedAt, :lunchStartedAt, :lunchFinishedAt, :isDayOff, :isVacation, :createdAt, :updatedAt) ",
//            nativeQuery = true)
    @Query(value = "INSERT INTO daily_work_info (\n" +
            "    user_id, started_at, finished_at,\n" +
            "    lunch_started_at, lunch_finished_at,\n" +
            "    is_day_off, is_vacation, created_at, updated_at\n" +
            ")\n" +
            "VALUES (\n" +
            "    :userId, :startedAt, :finishedAt,\n" +
            "    :lunchStartedAt, :lunchFinishedAt,\n" +
            "    :isDayOff, :isVacation, :createdAt, :updatedAt\n" +
            ")\n" +
            "ON CONFLICT (user_id, started_at) DO NOTHING;",  nativeQuery = true
    )
    int registerDailyWork(
            @Param("userId") int userId,
            @Param("startedAt") Date startedAt,
            @Param("finishedAt") Date finishedAt,
            @Param("lunchStartedAt") Date lunchStartedAt,
            @Param("lunchFinishedAt") Date lunchFinishedAt,
            @Param("isDayOff") boolean isDayOff,
            @Param("isVacation") boolean isVacation,
            @Param("createdAt") Date createdAt,
            @Param("updatedAt") Date updatedAt
    );
}
