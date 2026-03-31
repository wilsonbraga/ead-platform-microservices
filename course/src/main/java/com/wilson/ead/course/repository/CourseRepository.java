package com.wilson.ead.course.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wilson.ead.course.models.CourseModel;

@Repository
public interface CourseRepository extends JpaRepository<CourseModel, UUID> {
	
}
