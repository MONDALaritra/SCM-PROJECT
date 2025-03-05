package com.smartcontact.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smartcontact.entities.Contacts;
import com.smartcontact.entities.User;

@Repository
public interface ContactRepository extends JpaRepository<Contacts,String>{
    //find the contacts by user
    Page<Contacts> findByUser(User user,Pageable pageable);

    @Query("SELECT c FROM Contacts c WHERE c.user.userId = :userId")
    List<Contacts> findByUserId(@Param("userId") String userId);

    Page<Contacts> findByUserAndNameContaining(User user,String name, Pageable pageable);
    Page<Contacts> findByUserAndEmailContaining(User user,String email,Pageable pageable);
    Page<Contacts> findByUserAndPhoneContaining(User user,String phone,Pageable pageable);

    @Query("SELECT COUNT(c) FROM Contacts c WHERE c.user.userId = :userId")
    long countByUserId(@Param("userId") String userId);

    @Query("SELECT COUNT(c) FROM Contacts c WHERE c.user IS NOT NULL AND c.user.userId = :userId AND c.gender = 'female'")
    long countFemaleContacts(@Param("userId") String userId);

    @Query("SELECT COUNT(c) FROM Contacts c WHERE c.user IS NOT NULL AND c.user.userId = :userId AND c.gender = 'male'")
    long countMaleContacts(@Param("userId") String userId);

    @Query("SELECT COUNT(c) FROM Contacts c WHERE c.user IS NOT NULL AND c.user.userId = :userId AND c.gender = 'others'")
    long countOtherContacts(@Param("userId") String userId);


    @Query("SELECT COUNT(c) FROM Contacts c WHERE c.user IS NOT NULL AND c.user.userId = :userId AND c.favourite = TRUE")
    long countFavContacts(@Param("userId") String userId);

    Contacts findByEmail(String email);

    Contacts findByPhone(String phone);

    Contacts findByUserAndEmail(User user, String email);

    Contacts findByUserAndPhone(User user, String phone);
}
