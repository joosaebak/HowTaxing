package com.xmonster.howtaxing.repository.house;


import com.xmonster.howtaxing.model.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {

    Optional<House> findByHouseId(String houseId);

    Optional<House> findByUserId(String userId);
}
