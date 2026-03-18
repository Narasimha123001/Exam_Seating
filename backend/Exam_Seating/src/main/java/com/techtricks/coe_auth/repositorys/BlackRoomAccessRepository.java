package com.techtricks.coe_auth.repositorys;

import com.techtricks.coe_auth.models.BlackRoomAccess;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlackRoomAccessRepository extends JpaRepository<BlackRoomAccess, Long> {



      Optional<BlackRoomAccess> findRoomAccessByBlackRoom_BlackRoomId(Long blackRoomId);

    List<BlackRoomAccess> findByUser_id(Long registerNumber);
    Optional<BlackRoomAccess> findByUserIdAndBlackRoomBlackRoomId(Long userId, Long blackRoomId);

    @Modifying
    @Transactional
    @Query("DELETE FROM BlackRoomAccess ra WHERE ra.user.id = :userId AND ra.blackRoom.blackRoomName =:blackRoomName")
    void deleteRoomAccess(@Param("userId")  Long userId, @Param("blackRoomName") String blackRoomName);

     boolean existsByUserIdAndBlackRoomBlackRoomId(Long userId, Long blackRoomId);

}
