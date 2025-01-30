package com.fmi.p_final.repositories;

import com.fmi.p_final.entities.Channel;
import com.fmi.p_final.entities.ChannelMember;
import com.fmi.p_final.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {

    List<ChannelMember> findByChannel(Channel channel);

    List<ChannelMember> findByUser(User user);

    Optional<ChannelMember> findByChannelAndUser(Channel channel, User user);

    boolean existsByChannelAndUser(Channel channel, User user);

    void deleteByChannelAndUser(Channel channel, User user);
}
