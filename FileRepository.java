package com.smartcontact.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartcontact.entities.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity,Long>{

}
