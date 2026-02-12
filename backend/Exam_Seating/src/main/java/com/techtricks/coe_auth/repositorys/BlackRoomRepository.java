package com.techtricks.coe_auth.repositorys;

import com.techtricks.coe_auth.models.BlackRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackRoomRepository extends JpaRepository<BlackRoom, Long> {

    Optional<BlackRoom> findByBlackRoomId(Long blackRoomId);
}
