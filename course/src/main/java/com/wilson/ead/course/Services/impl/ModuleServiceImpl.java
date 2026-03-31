package com.wilson.ead.course.Services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wilson.ead.course.Services.ModuleService;
import com.wilson.ead.course.models.LessonModel;
import com.wilson.ead.course.models.ModuleModel;
import com.wilson.ead.course.repository.LessonRepository;
import com.wilson.ead.course.repository.ModuleRepository;

@Service
public class ModuleServiceImpl implements ModuleService {

	@Autowired
	private ModuleRepository moduleRepository;
	
	@Autowired
	private LessonRepository lessonRepository;

	@Transactional
	@Override
	public void delete(ModuleModel moduleModel) {
		
		List<LessonModel> lessonModelList = lessonRepository
				.findAllLessonsIntoModule(moduleModel.getModuleId());
		
		if(!lessonModelList.isEmpty()) {
			lessonRepository.deleteAll(lessonModelList);
		}
		moduleRepository.delete(moduleModel);
	}
}
