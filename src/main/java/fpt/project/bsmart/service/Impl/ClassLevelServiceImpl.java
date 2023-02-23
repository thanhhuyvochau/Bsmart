package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.ClassLevel;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.ClassLevelRequest;
import fpt.project.bsmart.entity.response.ClassLevelResponse;
import fpt.project.bsmart.repository.ClassLevelRepository;
import fpt.project.bsmart.service.IClassLevelService;
import fpt.project.bsmart.util.ObjectUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClassLevelServiceImpl implements IClassLevelService {
    private final ClassLevelRepository  classLevelRepository;

    public ClassLevelServiceImpl(ClassLevelRepository classLevelRepository) {
        this.classLevelRepository = classLevelRepository;
    }


    @Override
    public ClassLevelResponse create(ClassLevelRequest classLevelRequest) {
        ClassLevel classType = ObjectUtil.copyProperties(classLevelRequest, new ClassLevel(), ClassLevel.class, true);
        classType = classLevelRepository.save(classType);
        return ObjectUtil.copyProperties(classType, new ClassLevelResponse(), ClassLevelResponse.class, true);
    }

    @Override
    public ClassLevelResponse update(ClassLevelRequest classLevelRequest, Long id) {
        ClassLevel newClassType = ObjectUtil.copyProperties(classLevelRequest, new ClassLevel(), ClassLevel.class, true);
        ClassLevel oldClassType = classLevelRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Class Type Not Found!"));
        oldClassType = ObjectUtil.copyProperties(newClassType, oldClassType, ClassLevel.class, true);
        return ObjectUtil.copyProperties(oldClassType, new ClassLevelResponse(), ClassLevelResponse.class, true);
    }

    @Override
    public Boolean delete(Long id) {
        ClassLevel classType = classLevelRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Class Type Not Found!"));
        classLevelRepository.delete(classType);
        return true;
    }

    @Override
    public List<ClassLevelResponse> getAll() {
        List<ClassLevel> classTypes = classLevelRepository.findAll();
        List<ClassLevelResponse> classLevelRespons = classTypes.stream().map(classType -> {
            ClassLevelResponse classLevelResponse = ObjectUtil.copyProperties(classType, new ClassLevelResponse(), ClassLevelResponse.class, true);
            return classLevelResponse;
        }).collect(Collectors.toList());
        return classLevelRespons;
    }
}
