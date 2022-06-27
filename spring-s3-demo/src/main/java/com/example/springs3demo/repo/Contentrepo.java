package com.example.springs3demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springs3demo.model.ContentDetails;

@Repository
public interface Contentrepo extends JpaRepository<ContentDetails,Integer>{


}
