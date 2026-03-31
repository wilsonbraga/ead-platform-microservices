package com.wilson.ead.course.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wilson.ead.course.Services.LessonService;
import com.wilson.ead.course.repository.LessonRepository;

@Service
public class LessonServiceImpl implements LessonService {

	
	@Autowired
	private LessonRepository lessonRepository;
}
