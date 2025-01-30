package com.fmi.p_final.repositories;

import com.fmi.p_final.entities.Channel;
import com.fmi.p_final.entities.Message;
import com.fmi.p_final.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySender(User sender);

    List<Message> findByRecipient(User recipient);

    List<Message> findByChannel(Channel channel);

    List<Message> findBySenderAndRecipient(User sender, User recipient);
}
