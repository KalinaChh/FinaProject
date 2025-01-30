package com.fmi.p_final.repositories;

import com.fmi.p_final.entities.Channel;
import com.fmi.p_final.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Optional<Channel> findByName(String name);

    List<Channel> findByOwner(User owner);

    boolean existsByName(String name);
}
