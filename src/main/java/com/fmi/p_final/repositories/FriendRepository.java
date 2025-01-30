package com.fmi.p_final.repositories;

import com.fmi.p_final.entities.Friend;
import com.fmi.p_final.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findByUser(User user);

    List<Friend> findByFriend(User friend);

    List<Friend> findByUserAndIsDeletedFalse(User user);


    boolean existsByUserAndFriend(User user, User friend);

    void deleteByUserAndFriend(User user, User friend);
}
