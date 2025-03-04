package com.smartcontact.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartcontact.entities.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

}
