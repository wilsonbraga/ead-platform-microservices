package com.wilson.ead.course.dtos;

import java.util.UUID;

import com.wilson.ead.course.enums.CourseLevel;
import com.wilson.ead.course.enums.CourseStatus;

import lombok.Data;

@Data
public class CourseDto {
	
	private String name;
	private String description;
	private String imageUrl;
	private CourseStatus courseStatus;
	private UUID userInstructor;
	private CourseLevel courseLevel;
	
}
