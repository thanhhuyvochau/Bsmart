package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.TimeTable;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EUserRole;
import fpt.project.bsmart.entity.request.EditClassTimeTableRequest;
import fpt.project.bsmart.repository.TimeTableRepository;
import fpt.project.bsmart.service.ITimeTableService;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.http.HttpStatus;

import java.util.Objects;

public class TimeTableServiceImpl implements ITimeTableService {
    private final TimeTableRepository timeTableRepository;

    public TimeTableServiceImpl(TimeTableRepository timeTableRepository) {
        this.timeTableRepository = timeTableRepository;
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
