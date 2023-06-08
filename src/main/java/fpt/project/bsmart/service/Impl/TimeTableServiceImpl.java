package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.TimeTable;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.request.EditClassTimeTableRequest;
import fpt.project.bsmart.entity.response.TimeTableResponse;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.TimeTableRepository;
import fpt.project.bsmart.service.ITimeTableService;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.PageUtil;
import fpt.project.bsmart.util.SecurityUtil;
import fpt.project.bsmart.validator.ClassValidator;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Service
@Transactional
public class TimeTableServiceImpl implements ITimeTableService {
    private final TimeTableRepository timeTableRepository;
    private final ClassRepository classRepository;

    public TimeTableServiceImpl(TimeTableRepository timeTableRepository, ClassRepository classRepository) {
        this.timeTableRepository = timeTableRepository;
        this.classRepository = classRepository;
    }

    @Override
    public Boolean editTimeTable(EditClassTimeTableRequest request) {
        TimeTable timeTable = getOrThrow(request.getTimeTableId());
        Class clazz = timeTable.getClazz();

        if (isValidTimeTable(timeTable, clazz)) {

        } else {
            throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Dữ liệu chỉnh sửa thời khóa biểu không hợp lệ vui lòng thử lại");
        }

        return null;
    }

    @Override
    public ApiPage<TimeTableResponse> getTimeTableByClass(Long id, Pageable pageable) {
        Class clazz = classRepository.findById(id).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy lớp với id:" + id));
        User currentUser = SecurityUtil.getUserOrThrowException(SecurityUtil.getCurrentUserOptional());
        if (ClassValidator.isMentorOfClass(currentUser, clazz) || ClassValidator.isUserIsStudentOfClass(clazz, currentUser)) {
            List<TimeTable> timeTables = clazz.getTimeTables();
            List<TimeTableResponse> timeTableResponses = new ArrayList<>();
            for (TimeTable timeTable : timeTables) {
                TimeTableResponse timeTableResponse = ConvertUtil.convertTimeTableToResponse(timeTable);
                timeTableResponses.add(timeTableResponse);
            }
            return PageUtil.convert(new PageImpl<>(timeTableResponses, pageable, timeTableResponses.size()));
        }
        return new ApiPage<>();
    }


    private TimeTable getOrThrow(Long id) {
        return timeTableRepository.findById(id)
                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Thời khóa biểu không tồn tại, vui lòng thử lại!"));
    }

    private Boolean isValidTimeTable(TimeTable timeTable, Class clazz) {
        return this.isHasRightToEdit(clazz) && this.isClazzValidOfTimeTable(timeTable, clazz);
    }

    private Boolean isHasRightToEdit(Class clazz) {
        User currentUser = SecurityUtil.getCurrentUser();
        User mentor = clazz.getSubCourse().getMentor();
        boolean isManager = currentUser.getRoles().stream().anyMatch(role -> Objects.equals(role.getCode(), EUserRole.MANAGER));

        if (Objects.equals(currentUser.getId(), mentor.getId())) {
            return true;
        } else if (isManager) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean isClazzValidOfTimeTable(TimeTable timeTable, Class clazz) {
        return Objects.equals(timeTable.getClazz().getId(), clazz.getId());
    }
}
