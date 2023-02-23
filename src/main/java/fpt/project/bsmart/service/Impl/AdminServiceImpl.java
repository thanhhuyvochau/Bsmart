package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.request.*;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IAdminService;
import fpt.project.bsmart.util.ObjectUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements IAdminService {

    private final ClassRepository classRepository;
    private final FeedbackRepository feedbackRepository;
    private final RequestRepository requestRepository;

    public AdminServiceImpl(ClassRepository classRepository, FeedbackRepository feedbackRepository, RequestRepository requestRepository) {
        this.classRepository = classRepository;
        this.feedbackRepository = feedbackRepository;
        this.requestRepository = requestRepository;
    }


    @Override
    public FeedBackDto viewStudentFeedbackClass(Long classId) {
        Class fbclass = classRepository.findById(classId)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(("Khong tim thay class") + classId));


        FeedBackDto feedBackDto = new FeedBackDto();
        ClassDto classDto = ObjectUtil.copyProperties(fbclass, new ClassDto(), ClassDto.class);
        feedBackDto.setClassInfo(classDto);

//        List<FeedBack> feedBacks = feedbackRepository.countAllByClazz(fbclass);
        List<FeedBacClassDto> feedBacClassDtoList = new ArrayList<>();
//        List<FeedBacClassDto> collect = feedBacks.stream().map(feedBack -> {
//            FeedBacClassDto feedBacClassDto = ObjectUtil.copyProperties(feedBack, new FeedBacClassDto(), FeedBacClassDto.class);
//            feedBacClassDtoList.add(feedBacClassDto);
//            return feedBacClassDto;
//        }).collect(Collectors.toList());
//        feedBackDto.setFeedBacClass(feedBacClassDtoList);
//        return feedBackDto;
        return null ;
    }




}
