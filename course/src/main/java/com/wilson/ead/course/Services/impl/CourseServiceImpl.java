package com.wilson.ead.course.Services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wilson.ead.course.Services.CourseService;
import com.wilson.ead.course.models.CourseModel;
import com.wilson.ead.course.models.LessonModel;
import com.wilson.ead.course.models.ModuleModel;
import com.wilson.ead.course.repository.CourseRepository;
import com.wilson.ead.course.repository.LessonRepository;
import com.wilson.ead.course.repository.ModuleRepository;

@Service
public class CourseServiceImpl implements CourseService {
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private ModuleRepository moduleRepository;
	
	@Autowired
	private LessonRepository lessonRepository;
	
	@Transactional
	@Override
	public void delete(CourseModel courseModel) {
		
		List<ModuleModel> moduleModelsList = moduleRepository
				.findAllModulesIntoCourse(courseModel.getCourseId());
		
		if(!moduleModelsList.isEmpty()) {
			
			for(ModuleModel module: moduleModelsList) {
				
				List<LessonModel> lessonModelList = lessonRepository
						.findAllLessonsIntoModule(module.getModuleId());
				
				if(!lessonModelList.isEmpty()) {
					lessonRepository.deleteAll(lessonModelList);
				}
			}
			
			moduleRepository.deleteAll(moduleModelsList);
		}
		courseRepository.delete(courseModel);
	}
}
