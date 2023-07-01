//package fpt.project.bsmart.service.Impl;
//
//import fpt.project.bsmart.entity.dto.ActivityTypeDto;
//import fpt.project.bsmart.repository.ActivityTypeRepository;
//import fpt.project.bsmart.service.ActivityTypeService;
//import fpt.project.bsmart.util.ConvertUtil;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ActivityTypeServiceImpl implements ActivityTypeService {
//
//    private final ActivityTypeRepository activityTypeRepository;
//
//    public ActivityTypeServiceImpl(ActivityTypeRepository activityTypeRepository) {
//        this.activityTypeRepository = activityTypeRepository;
//    }
//
//    @Override
//    public List<ActivityTypeDto> getAllActivityTypes() {
//        List<ActivityType> activityTypes = activityTypeRepository.findAll();
//        return activityTypes.stream()
//                .map(ConvertUtil::convertActivityTypeToDto)
//                .collect(Collectors.toList());
//    }
//
//
//}
