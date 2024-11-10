package com.example.backend.repository;
import com.example.backend.entity.Friend;
import com.example.backend.entity.FriendStatus;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {


 @Query("SELECT f FROM Friend f WHERE f.sender = :sender AND f.receiver = :receiver")
 Friend getFriendInvitation(@Param("sender") String senderName, @Param("receiver") String receiverName);

 //@Query("SELECT f FROM Friend f WHERE f.receiver = :receiver AND f.status = :status")
 //List<Friend> getFriendsReceiver(@Param("receiver") String receiverName, @Param("status") FriendStatus friendStatus);

 @Query("SELECT f From Friend f WHERE (f.receiver = :username OR f.sender = :username) AND f.status = :status")
 List<Friend> getFriends(@Param("username") String username, @Param("status") FriendStatus friendStatus);

 @Query("SELECT f From Friend f WHERE (f.receiver = :username OR f.sender = :username) AND f.status = :status")
 List<Friend> getPendingFriendList(@Param("username") String username, @Param("status") FriendStatus friendStatus);


// List<Friend> findAll();


}

