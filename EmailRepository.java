package com.smartcontact.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartcontact.entities.Emails;
import com.smartcontact.entities.User;

@Repository
public interface EmailRepository extends JpaRepository<Emails,String>{
    Page<Emails> findByUser(User user,Pageable pageable);

    @Query("SELECT e FROM Emails e WHERE e.user.userId = :userId")
    List<Emails> findByUserId(@Param("userId") String userId);

    Page<Emails> findByUserAndDestinationnameContaining(User user,String name, Pageable pageable);
    Page<Emails> findByUserAndDestinationemailContaining(User user,String email,Pageable pageable);

    @Query("SELECT COUNT(e) FROM Emails e WHERE e.user.userId = :userId")
    long countByUserId(@Param("userId") String userId);

    Emails findByEmailid(String emailid);
}
