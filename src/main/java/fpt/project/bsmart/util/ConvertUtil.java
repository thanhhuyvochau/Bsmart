package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.*;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.dto.feedback.FeedbackTemplateDto;
import fpt.project.bsmart.entity.dto.mentor.TeachInformationDTO;
import fpt.project.bsmart.entity.request.activity.LessonDto;
import fpt.project.bsmart.entity.response.*;
import fpt.project.bsmart.entity.response.course.ManagerGetCourse;
import fpt.project.bsmart.repository.ActivityHistoryRepository;
import fpt.project.bsmart.repository.ClassRepository;
import fpt.project.bsmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ConvertUtil {
    private static MessageUtil messageUtil;

    private static ClassRepository staticClassRepository;
    private static String successIcon;

    private static String failIcon;

    private static ActivityHistoryRepository staticActivityHistoryRepository;

    public ConvertUtil(ClassRepository classRepository, UserRepository userRepository, ActivityHistoryRepository activityHistoryRepository) {
        staticClassRepository = classRepository;
        staticActivityHistoryRepository = activityHistoryRepository;
    }

    @Value("${icon.success}")
    public void setSuccessIconUrl(String url) {
        successIcon = url;
    }

    @Value("${icon.fail}")
    public void setFailIconUrl(String url) {
        failIcon = url;
    }

    public static CategoryDto convertCategoryToCategoryDto(Category category) {
        return ObjectUtil.copyProperties(category, new CategoryDto(), CategoryDto.class);
    }

    public static SubjectDto convertSubjectToSubjectDto(Subject subject) {
        SubjectDto subjectDto = ObjectUtil.copyProperties(subject, new SubjectDto(), SubjectDto.class, true);
        Set<Category> categories = subject.getCategories();
        if (!categories.isEmpty()) {
            for (Category category : categories) {
                subjectDto.getCategoryIds().add(category.getId());
            }
        }
        return subjectDto;
    }


//    public static SectionDto convertSectionToSectionDto(Section section) {
//        SectionDto sectionDto = ObjectUtil.copyProperties(section, new SectionDto(), SectionDto.class);
//        if (section.getCourse() != null) {
//            sectionDto.setCourseId(section.getCourse().getId());
//        }
//        if (!section.getModules().isEmpty()) {
//            List<ModuleDto> moduleDtoList = new ArrayList<>();
//
//            sectionDto.setModules(moduleDtoList);
//        }
//        return sectionDto;
//    }


    public static WalletDto convertWalletToWalletDto(Wallet wallet) {
        WalletDto walletDto = ObjectUtil.copyProperties(wallet, new WalletDto(), WalletDto.class);
        if (wallet.getOwner() != null) {
            walletDto.setOwner_id(wallet.getOwner().getId());
        }
        return walletDto;
    }

    public static ImageDto convertImageToImageDto(Image image) {
        return ObjectUtil.copyProperties(image, new ImageDto(), ImageDto.class);
    }

    public static ImageDto convertUserImageToUserImageDto(UserImage userImage) {
        ImageDto imageDto = ObjectUtil.copyProperties(userImage, new ImageDto(), ImageDto.class);
        if (userImage.getUser() != null) {
//            imageDto.setUserId(image.getUser().getId());
        }
        return imageDto;
    }

    public static ImageDto convertClassImageToImageDto(ClassImage classImage) {
        ImageDto imageDto = ObjectUtil.copyProperties(classImage, new ImageDto(), ImageDto.class);
        return imageDto;
    }

    public static RoleDto convertRoleToRoleDto(Role role) {
        RoleDto roleDto = ObjectUtil.copyProperties(role, new RoleDto(), RoleDto.class);
        roleDto.setName(role.getCode().getName());
        return roleDto;
    }

    public static SlotDto convertSlotToSlotDto(Slot slot) {
        SlotDto dto = new SlotDto();
        dto.setId(slot.getId());
        dto.setName(slot.getName());
        dto.setCode(slot.getCode());
        dto.setStartTime(slot.getStartTime());
        dto.setEndTime(slot.getEndTime());
        return dto;
    }

    public static Slot convertSlotDtoToSlot(SlotDto dto) {
        Slot entity = new Slot();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        return entity;
    }


    public static DayOfWeekDTO convertDayOfWeekToDto(DayOfWeek dayOfWeek) {
        return new DayOfWeekDTO(dayOfWeek.getId(), dayOfWeek.getName(), dayOfWeek.getCode());
    }

    public static SimpleClassDto convertClassToSimpleDto(Class clazz) {
        return ObjectUtil.copyProperties(clazz, new SimpleClassDto(), SimpleClassDto.class, true);
    }

    public static TimeInWeekDTO convertTimeInWeekToDto(TimeInWeek timeInWeek) {
        Slot slot = timeInWeek.getSlot();
        DayOfWeek dayOfWeek = timeInWeek.getDayOfWeek();
        DayOfWeekDTO dayOfWeekDTO = null;
        if (dayOfWeek != null) {
            dayOfWeekDTO = convertDayOfWeekToDto(dayOfWeek);
        }
        SlotDto slotDto = new SlotDto();
        if (slot != null) {
            slotDto = convertSlotToSlotDto(slot);
        }
        return new TimeInWeekDTO(dayOfWeekDTO, slotDto);
    }


    public static UserDto convertUsertoUserDto(User user) {
        UserDto userDto = ObjectUtil.copyProperties(user, new UserDto(), UserDto.class);
        if (!user.getRoles().isEmpty()) {
            List<RoleDto> roleDtoList = new ArrayList<>();
            for (Role role : user.getRoles()) {
                roleDtoList.add(convertRoleToRoleDto(role));
            }
            userDto.setRoles(roleDtoList);
        }
        if (!user.getUserImages().isEmpty()) {
            List<ImageDto> imageDtoList = new ArrayList<>();
            for (UserImage image : user.getUserImages()) {
                if (image.isStatus()) {
                    imageDtoList.add(convertUserImageToUserImageDto(image));
                }

                userDto.setUserImages(imageDtoList);
            }
            userDto.setUserImages(imageDtoList);
        }


        if (user.getWallet() != null) {
            userDto.setWallet(convertWalletToWalletDto(user.getWallet()));
        }
        if (user.getMentorProfile() != null) {
            userDto.setMentorProfile(convertMentorProfileToMentorProfileDto(user.getMentorProfile()));
            TeachInformationDTO teachingInformation = MentorUtil.getTeachingInformation(user);
            userDto.setTeachInformation(teachingInformation);
        }

        return userDto;
    }
//
//    public static ModuleDto convertModuleToModuleDto(Module module) {
//        ModuleDto moduleDto = ObjectUtil.copyProperties(module, new ModuleDto(), ModuleDto.class);
//        return moduleDto;
//    }


    public static CourseDto convertCourseToCourseDTO(Course course) {
        CourseDto courseDto = ObjectUtil.copyProperties(course, new CourseDto(), CourseDto.class);
        if (course.getSubject() != null) {
            courseDto.setSubject(convertSubjectToSubjectDto(course.getSubject()));
        }
        return courseDto;
    }

    public static CourseDetailResponse convertCourseToCourseDetailResponse(Course course) {
        CourseDetailResponse response = ObjectUtil.copyProperties(course, new CourseDetailResponse(), CourseDetailResponse.class);

        if (course.getSubject() != null) {
            response.setSubject(convertSubjectToSubjectDto(course.getSubject()));
            Set<Category> categories = course.getSubject().getCategories();
            if (!categories.isEmpty()) {
                for (Category category : categories) {
                    response.getCategoryDtoList().add(convertCategoryToCategoryDto(category));
                }
            }
        }


        return response;
    }

//    public static SubCourseDetailResponse convertSubCourseToSubCourseDetailResponse(User userLogin, SubCourse subCourse) {
//        SubCourseDetailResponse subCourseDetailResponse = ObjectUtil.copyProperties(subCourse, new SubCourseDetailResponse(), SubCourseDetailResponse.class);
//
//        if (userLogin != null) {
//            List<Order> orders = userLogin.getOrder();
//            orders.forEach(order -> {
//                List<OrderDetail> orderDetails = order.getOrderDetails();
//                orderDetails.forEach(orderDetail -> {
//                    SubCourse subCourse1 = orderDetail.getSubCourse();
//                    if (subCourse1.equals(subCourse)) {
//                        subCourseDetailResponse.setPurchase(true);
//                    }
//                });
//            });
//        }


//        subCourseDetailResponse.setImage(ObjectUtil.copyProperties(subCourse.getImage(), new ImageDto(), ImageDto.class));
//        List<TimeInWeek> timeInWeeks = subCourse.getTimeInWeeks();
//
//        List<TimeInWeekDTO> timeInWeekDTOS = new ArrayList<>();
//        timeInWeeks.forEach(timeInWeek -> {
//            timeInWeekDTOS.add(convertTimeInWeekToDto(timeInWeek));
//        });
//        subCourseDetailResponse.setTimeInWeeks(timeInWeekDTOS);
//        return subCourseDetailResponse;
//    }

    public static CourseSubCourseDetailResponse convertCourseSubCourseToCourseSubCourseDetailResponse(Course course) {
        CourseSubCourseDetailResponse response = ObjectUtil.copyProperties(course, new CourseSubCourseDetailResponse(), CourseSubCourseDetailResponse.class);

        Subject subject = course.getSubject();
        if (subject != null) {
            response.setSubject(convertSubjectToSubjectDto(subject));
            Set<Category> categories = subject.getCategories();
            if (!categories.isEmpty()) {
                for (Category category : categories) {
                    response.getCategoryDtoList().add(convertCategoryToCategoryDto(category));
                }

            }
        }

        return response;
    }

//    public static CourseSubCourseResponse subCourseToCourseSubCourseResponseConverter(SubCourse subCourse) {
//        Course course = subCourse.getCourse();
//        CourseSubCourseResponse response = new CourseSubCourseResponse();
//        if (course != null) {
//            response.setCourseId(course.getId());
//            response.setCourseCode(course.getCode());
//            response.setCourseName(course.getName());
//            response.setCourseDescription(course.getDescription());
//            response.setCourseType(course.getType());
//            Subject subject = course.getSubject();
//            if (subject != null) {
//                SubjectDto subjectDto = convertSubjectToSubjectDto(subject);
//                response.setSubject(subjectDto);
//                Set<Category> categories = subject.getCategories();
//                if (!categories.isEmpty()) {
//                    for (Category category : categories) {
//                        CategoryDto categoryDto = convertCategoryToCategoryDto(category);
//                        response.getCategoryDtoList().add(categoryDto);
//                    }
//                }
//            }
//        }

//        response.setSubCourseId(ObjectUtils.defaultIfNull(subCourse.getId(), null));
//        response.setStatus(ObjectUtils.defaultIfNull(subCourse.getStatus(), null));
//        response.setTypeLearn(ObjectUtils.defaultIfNull(subCourse.getTypeLearn(), null));
//        response.setSubCourseTitle(ObjectUtils.defaultIfNull(subCourse.getTitle(), null));
//        response.setLevel(ObjectUtils.defaultIfNull(subCourse.getLevel(), null));
//        response.setMinStudent(ObjectUtils.defaultIfNull(subCourse.getMinStudent(), 0));
//        response.setMaxStudent(ObjectUtils.defaultIfNull(subCourse.getMaxStudent(), 0));
//        response.setPrice(ObjectUtils.defaultIfNull(subCourse.getPrice(), BigDecimal.ZERO));
//        response.setEndDateExpected(ObjectUtils.defaultIfNull(subCourse.getEndDateExpected(), null));
//        response.setStartDateExpected(ObjectUtils.defaultIfNull(subCourse.getStartDateExpected(), null));
//        response.setNumberOfSlot(ObjectUtils.defaultIfNull(subCourse.getNumberOfSlot(), null));
//        List<OrderDetail> orderDetails = subCourse.getOrderDetails();
//        response.setFinalStudent(orderDetails.size());
//
//        if (subCourse.getImage() != null) {
//            response.setImageUrl(subCourse.getImage().getUrl());
//        }


//        if (subCourse.getMentor() != null) {
//            response.setMentorName(subCourse.getMentor().getFullName());
//        }
//
//        List<TimeInWeek> timeInWeeks = subCourse.getTimeInWeeks();
//        if (subCourse.getTimeInWeeks() != null) {
//            List<TimeInWeekDTO> timeInWeekDtoList = new ArrayList<>();
//            for (TimeInWeek timeInWeek : timeInWeeks) {
//                timeInWeekDtoList.add(convertTimeInWeekToDto(timeInWeek));
//            }
//            response.setTimeInWeek(timeInWeekDtoList);
//        }
//
//
//        return response;
//    }

    public static CourseResponse convertCourseCourseResponsePage(Course course) {


        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setId(course.getId());
        courseResponse.setCourseName(course.getName());
        courseResponse.setCourseCode(course.getCode());
        courseResponse.setCourseDescription(course.getDescription());
        courseResponse.setStatus(course.getStatus());
        courseResponse.setLevel(course.getLevel());
        List<String> mentorName = new ArrayList<>();

        List<Class> classes = course.getClasses();
        List<ImageDto> images = new ArrayList<>();
        classes.forEach(clazz -> {
            if (clazz.getMentor() != null) {
                if (mentorName.isEmpty()) {
                    mentorName.add(clazz.getMentor().getFullName());
                }
            }
            if (clazz.getClassImage() != null) {
                images.add(ObjectUtil.copyProperties(clazz.getClassImage(), new ImageDto(), ImageDto.class));
            }
        });
        courseResponse.setTotalClass(classes.size());
        courseResponse.setMentorName(mentorName);
        courseResponse.setImages(images);


        Subject subject = course.getSubject();
        if (subject != null) {
            SubjectDto subjectDto = convertSubjectToSubjectDto(subject);
            courseResponse.setSubjectResponse(subjectDto);

            Set<Category> categories = subject.getCategories();
            if (!categories.isEmpty()) {
                for (Category category : categories) {
                    CategoryDto categoryDto = convertCategoryToCategoryDto(category);
                    courseResponse.setCategoryResponse(categoryDto);
                }
            }
        }

        return courseResponse;
    }

    public static ManagerGetCourse convertCourseToManagerGetCourse(Course course) {
        ActivityHistory byUserCourse = staticActivityHistoryRepository.findByType(EActivityType.COURSE);

        ManagerGetCourse courseResponse = new ManagerGetCourse();
        if (byUserCourse != null) {
            courseResponse.setCount(byUserCourse.getCount());
            courseResponse.setTimeSendRequest(course.getLastModified());
        }

        courseResponse.setId(course.getId());
        courseResponse.setName(course.getName());
        courseResponse.setCode(course.getCode());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setStatus(course.getStatus());
        courseResponse.setLevel(course.getLevel());
        if (course.getCreator() != null) {
            courseResponse.setMentor(MentorUtil.convertUserToMentorDto(course.getCreator()));
        }


        Subject subject = course.getSubject();
        if (subject != null) {
            courseResponse.setSubjectResponse(ConvertUtil.convertSubjectToSubjectDto(subject));

            Set<Category> categories = subject.getCategories();
            if (!categories.isEmpty()) {
                for (Category category : categories) {

                    courseResponse.setCategoryResponse(ConvertUtil.convertCategoryToCategoryDto(category));
                }
            }
        }
        int totalClassToApproval = 0;
        List<Class> classes = course.getClasses();
        if (classes != null) {
            for (Class aClass : classes) {
                if (aClass.getStatus().equals(ECourseStatus.WAITING)) {
                    ++totalClassToApproval;
                }
            }
        }
        courseResponse.setTotalClass(totalClassToApproval);


        return courseResponse;
    }

    public static MentorGetCourseClassResponse convertCourseToCourseClassResponsePage(Course course) {


        MentorGetCourseClassResponse courseResponse = new MentorGetCourseClassResponse();
        courseResponse.setId(course.getId());
        courseResponse.setName(course.getName());
        courseResponse.setCode(course.getCode());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setStatus(course.getStatus());

        if (course.getCreator() != null) {
            courseResponse.setMentor(MentorUtil.convertUserToMentorDto(course.getCreator()));
        }


        Subject subject = course.getSubject();
        if (subject != null) {
            courseResponse.setSubjectResponse(ConvertUtil.convertSubjectToSubjectDto(subject));

            Set<Category> categories = subject.getCategories();
            if (!categories.isEmpty()) {
                for (Category category : categories) {

                    courseResponse.setCategoryResponse(ConvertUtil.convertCategoryToCategoryDto(category));
                }
            }
        }

        List<ClassDetailResponse> classDetailResponses = new ArrayList<>();
        List<Class> classes = staticClassRepository.findByCourseAndStatus(course, ECourseStatus.WAITING);
        classes.forEach(aClass -> {
            classDetailResponses.add(ClassUtil.convertClassToClassDetailResponse(course.getCreator(), aClass));
        });
        courseResponse.setClasses(classDetailResponses);
        return courseResponse;
    }

    public static TransactionDto convertTransactionToDto(Transaction transaction) {
        TransactionDto transactionDto = ObjectUtil.copyProperties(transaction, new TransactionDto(), TransactionDto.class, true);
        transactionDto.setStatusName(transaction.getStatus().getLabel());
        transactionDto.setTypeName(transaction.getType().getLabel());
        if (Objects.equals(transaction.getStatus(), ETransactionStatus.SUCCESS)) {
            transactionDto.setIconUrl(successIcon);
        } else if (Objects.equals(transaction.getStatus(), ETransactionStatus.FAIL)) {
            transactionDto.setIconUrl(failIcon);
        }
        return transactionDto;
    }

    public static BankDto convertBankToBankDto(Bank bank) {
        return ObjectUtil.copyProperties(bank, new BankDto(), BankDto.class, true);
    }

    public static MentorProfileDTO convertMentorProfileToMentorProfileDto(MentorProfile mentorProfile) {
        MentorProfileDTO mentorProfileDTO = ObjectUtil.copyProperties(mentorProfile, new MentorProfileDTO(), MentorProfileDTO.class);
        if (mentorProfile.getSkills() != null) {
            List<MentorSkillDto> skillList = new ArrayList<>();
            for (MentorSkill mentorSkill : mentorProfile.getSkills()) {
                if (mentorSkill.getStatus()) {
                    MentorSkillDto mentorSkillDto = convertMentorSkillToMentorSkillDto(mentorSkill);
                    skillList.add(mentorSkillDto);
                }

            }
            mentorProfileDTO.setMentorSkills(skillList);
        }
        if(mentorProfile.getUser() != null){
            User temp = mentorProfile.getUser();
            temp.setMentorProfile(null);
            mentorProfileDTO.setUser(convertUsertoUserDto(temp));
        }
        mentorProfileDTO.setWorkingExperience(mentorProfile.getWorkingExperience());
        return mentorProfileDTO;
    }

    public static MentorSkillDto convertMentorSkillToMentorSkillDto(MentorSkill mentorSkill) {
        MentorSkillDto mentorSkillDto = new MentorSkillDto();
        mentorSkillDto.setSkillId(mentorSkill.getSkill().getId());
        mentorSkillDto.setName(mentorSkill.getSkill().getName());
        mentorSkillDto.setYearOfExperiences(mentorSkill.getYearOfExperiences());
        return mentorSkillDto;
    }

    public static CartResponse convertCartToCartResponse(Cart cart) {
        CartResponse cartResponse = ObjectUtil.copyProperties(cart, new CartResponse(), CartResponse.class, true);
        List<CourseCartResponse> cartItemResponses = cart.getCartItems().stream().map(ConvertUtil::convertCartItemToResponse).collect(Collectors.toList());
        cartResponse.getCartItems().addAll(cartItemResponses);
        return cartResponse;
    }

    public static CourseCartResponse convertCartItemToResponse(CartItem cartItem) {
        Class clazz = cartItem.getClazz();
        Course course = clazz.getCourse();

        CourseCartResponse courseCartResponse = convertCourseToCourseCart(course);
        courseCartResponse.setCartItemId(cartItem.getId());
        for (Class chooseClass : course.getClasses()) {
            SubCourseCartResponse subCourseCartResponse = ObjectUtil.copyProperties(chooseClass, new SubCourseCartResponse(), SubCourseCartResponse.class, true);
            if (subCourseCartResponse.getId().equals(clazz.getId())) {
                subCourseCartResponse.setIsChosen(true);
            }
            if (chooseClass.getMentor() != null) {
                subCourseCartResponse.setMentor(convertUsertoUserDto(clazz.getMentor()));
            }
            if (chooseClass.getClassImage() != null) {
                subCourseCartResponse.setImage(ObjectUtil.copyProperties(chooseClass.getClassImage(), new ImageDto(), ImageDto.class));
            }
            courseCartResponse.getSubCourses().add(subCourseCartResponse);
        }
        return courseCartResponse;
    }

    private static CourseCartResponse convertCourseToCourseCart(Course course) {
        CourseCartResponse courseCartResponse = ObjectUtil.copyProperties(course, new CourseCartResponse(), CourseCartResponse.class);
        if (course.getSubject() != null) {
            courseCartResponse.setSubject(convertSubjectToSubjectDto(course.getSubject()));
        }
        return courseCartResponse;
    }

    public static SimpleClassResponse convertClassToSimpleClassResponse(Class clazz) {
        SimpleClassResponse simpleClassResponse = ObjectUtil.copyProperties(clazz, new SimpleClassResponse(), SimpleClassResponse.class);
        Course course = clazz.getCourse();
        if (course != null) {
            simpleClassResponse.setCourse(convertCourseToCourseDTO(course));
        }
        List<StudentClass> studentClasses = clazz.getStudentClasses();
        simpleClassResponse.setNumberOfStudent(studentClasses.size());
        ImageDto imageDto = ConvertUtil.convertClassImageToImageDto(clazz.getClassImage());
        List<TimeInWeekDTO> timeInWeekDTOS = new ArrayList<>();
        clazz.getTimeInWeeks().forEach(timeInWeek -> {
            timeInWeekDTOS.add(ConvertUtil.convertTimeInWeekToDto(timeInWeek));
        });
        simpleClassResponse.setTimeInWeeks(timeInWeekDTOS);
        simpleClassResponse.setImage(imageDto);
        if (clazz.getMentor() != null) {
            simpleClassResponse.setMentor(MentorUtil.convertUserToMentorDto(clazz.getMentor()));
        }
        return simpleClassResponse;
    }

    public static QuestionDto convertQuestionToQuestionDto(Question question) {
        QuestionDto questionDto = ObjectUtil.copyProperties(question, new QuestionDto(), QuestionDto.class, true);
        questionDto.setMentorName(questionDto.getMentorName());
        questionDto.setAnswers(question.getAnswers().stream().map(ConvertUtil::convertAnswerToAnswerDto).collect(Collectors.toList()));
        return questionDto;
    }

    public static AnswerDto convertAnswerToAnswerDto(Answer answer) {
        return ObjectUtil.copyProperties(answer, new AnswerDto(), AnswerDto.class, true);
    }

    public static ActivityDto convertActivityToDto(Activity activity) { // Convert ra detail của activity
        ActivityDto activityDto = ObjectUtil.copyProperties(activity, new ActivityDto(), ActivityDto.class, true);
        Activity parent = activity.getParent();
        if (parent != null) {
            activityDto.setParentActivityId(parent.getId());
        }
        return activityDto;
    }

    public static ActivityDetailDto convertActivityDetailToDto(Activity activity) { // Convert ra đơn giản để show cho user xem
        ActivityDetailDto activityDetailDto = ObjectUtil.copyProperties(activity, new ActivityDetailDto(), ActivityDetailDto.class, true);
        Activity parent = activity.getParent();
        if (parent != null) {
            activityDetailDto.setParentActivityId(parent.getId());
        }

        ECourseActivityType type = activity.getType();
        switch (type) {
            case QUIZ:
                activityDetailDto.setDetail(convertQuizToQuizDto(activity.getQuiz(), false));
                break;
            case ASSIGNMENT:
                activityDetailDto.setDetail(convertAssignmentToDto(activity.getAssignment()));
                break;
            case SECTION:
                // Just return for section -> section work as folder for others activities with no content inside
                break;
            case RESOURCE:
                activityDetailDto.setDetail(convertResourceToDto(activity.getResource()));
                break;
            case ANNOUNCEMENT:
                activityDetailDto.setDetail(convertClassAnnouncementToDto(activity.getAnnouncement()));
                break;
            case LESSON:
                activityDetailDto.setDetail(convertLessonToDto(activity.getLesson()));
                break;
            default:
                throw ApiException.create(HttpStatus.NO_CONTENT).withMessage(messageUtil.getLocalMessage(Constants.ErrorMessage.Invalid.INVALID_ACTIVITY_TYPE) + type);
        }
        return activityDetailDto;
    }

    public static LessonDto convertLessonToDto(Lesson lesson) { // Convert ra đơn giản để show cho user xem
        return ObjectUtil.copyProperties(lesson, new LessonDto(), LessonDto.class, true);
    }

    public static ResourceDto convertResourceToDto(Resource resource) { // Convert ra đơn giản để show cho user xem
        return ObjectUtil.copyProperties(resource, new ResourceDto(), ResourceDto.class, true);
    }

    private static AssignmentDto convertAssignmentToDto(Assignment assignment) {
        AssignmentDto assignmentDto = ObjectUtil.copyProperties(assignment, new AssignmentDto(), AssignmentDto.class, true);
        for (AssignmentFile assignmentFile : assignment.getAssignmentFiles()) {
            assignmentDto.getAssignmentFiles().add(ConvertUtil.convertAssignmentFileToDto(assignmentFile));
        }
        return assignmentDto;
    }

    private static SimpleAssignmentDto convertAssignmentToSimpleDto(Assignment assignment) {
        SimpleAssignmentDto assignmentDto = ObjectUtil.copyProperties(assignment, new SimpleAssignmentDto(), SimpleAssignmentDto.class, true);
        return assignmentDto;
    }

    public static AssignmentSubmitionDto convertAssignmentSubmitToDto(AssignmentSubmition assignmentSubmition) {
        AssignmentSubmitionDto assignmentSubmitionDto = ObjectUtil.copyProperties(assignmentSubmition, new AssignmentSubmitionDto(), AssignmentSubmitionDto.class, true);
        StudentClass studentClass = assignmentSubmition.getStudentClass();
        if (studentClass != null) {
            assignmentSubmitionDto.setStudentClass(convertStudentClassToResponse(studentClass));
        }
        List<AssignmentFileDto> assignmentFileDtos = assignmentSubmition.getAssignmentFiles().stream().map(ConvertUtil::convertAssignmentFileToDto).collect(Collectors.toList());
        assignmentSubmitionDto.setAssignmentFiles(assignmentFileDtos);
        return assignmentSubmitionDto;
    }

    public static AssignmentFileDto convertAssignmentFileToDto(AssignmentFile assignmentFile) {
        return ObjectUtil.copyProperties(assignmentFile, new AssignmentFileDto(), AssignmentFileDto.class, true);
    }

//    public static ActivityTypeDto convertActivityTypeToDto(ActivityType activityType) {
//        ActivityTypeDto activityTypeResponse = new ActivityTypeDto();
//        ObjectUtil.copyProperties(activityType, activityTypeResponse);
//        return activityTypeResponse;
//    }

    public static QuizDto convertQuizToQuizDto(Quiz quiz, boolean isAttempt) {
        QuizDto quizDto = ObjectUtil.copyProperties(quiz, new QuizDto(), QuizDto.class);
        quizDto.setQuestionCount(quiz.getQuizQuestions().size());
        quizDto.setPassword(null);
        if (isAttempt) {
            quizDto.setDefaultPoint(null);
            quizDto.setIsSuffleQuestion(null);
            quizDto.setPassword(null);
            if (quiz.getQuizQuestions() != null || !quiz.getQuizQuestions().isEmpty()) {
                List<QuizQuestionDto> questionDtos = new ArrayList<>();
                for (QuizQuestion question : quiz.getQuizQuestions()) {
                    questionDtos.add(ConvertUtil.convertQuizQuestionToQuizQuestionDto(question, quiz.getIsSuffleQuestion()));
                }
                if (quiz.getIsSuffleQuestion()) {
                    Collections.shuffle(questionDtos);
                }
                quizDto.setQuizQuestions(questionDtos);
            }
        }
        return quizDto;
    }

    public static QuizSubmittionDto convertQuizSubmittionToQuizSubmittionDto(QuizSubmittion quizSubmittion) {
        QuizSubmittionDto quizSubmittionDto = new QuizSubmittionDto();
        quizSubmittionDto.setId(quizSubmittion.getId());
        quizSubmittionDto.setQuizId(quizSubmittion.getQuiz().getId());
        quizSubmittionDto.setPoint(quizSubmittion.getPoint());
        quizSubmittionDto.setStatus(quizSubmittion.getStatus());
        List<QuizSubmitQuestionDto> questionDtos = quizSubmittion.getSubmitQuestions().stream()
                .map(ConvertUtil::convertQuizSubmitQuestionToQuizSubmitQuestionDto)
                .collect(Collectors.toList());
        quizSubmittionDto.setQuestions(questionDtos);
        quizSubmittionDto.setCorrectNumber(quizSubmittion.getCorrectNumber());
        quizSubmittionDto.setIncorrectNumber(quizSubmittion.getIncorrectNumber());
        return quizSubmittionDto;
    }

    public static QuizSubmitQuestionDto convertQuizSubmitQuestionToQuizSubmitQuestionDto(QuizSubmitQuestion quizSubmitQuestion) {
        QuizSubmitQuestionDto questionDto = new QuizSubmitQuestionDto();
        questionDto.setId(quizSubmitQuestion.getId());
        questionDto.setQuestion(quizSubmitQuestion.getQuizQuestion().getQuestion());
        questionDto.setType(quizSubmitQuestion.getQuizQuestion().getType());
        questionDto.setAnswers(ConvertUtil.convertQuizSubmitAnswersToQuizSubmitAnswerDto(quizSubmitQuestion.getQuizSubmitAnswers(), quizSubmitQuestion.getQuizQuestion().getAnswers()));
        return questionDto;
    }

    public static List<QuizSubmitAnswerDto> convertQuizSubmitAnswersToQuizSubmitAnswerDto(List<QuizSubmitAnswer> quizSubmitAnswers, List<QuizAnswer> quizAnswers) {
        List<QuizSubmitAnswerDto> quizSubmitAnswerDtos = new ArrayList<>();
        for (QuizAnswer answer : quizAnswers) {
            QuizSubmitAnswerDto answerDto = new QuizSubmitAnswerDto();
            answerDto.setId(answer.getId());
            answerDto.setAnswer(answer.getAnswer());
            answerDto.setIsRight(answer.getIsRight());
            if (!quizSubmitAnswers.isEmpty()) {
                boolean isChosen = quizSubmitAnswers.stream()
                        .anyMatch(x -> x.getQuizAnswer().equals(answer));
                answerDto.setIsChosen(isChosen);
            }
            quizSubmitAnswerDtos.add(answerDto);
        }
        return quizSubmitAnswerDtos;
    }

    public static QuizQuestionDto convertQuizQuestionToQuizQuestionDto(QuizQuestion quizQuestion, boolean isShuffle) {
        QuizQuestionDto question = ObjectUtil.copyProperties(quizQuestion, new QuizQuestionDto(), QuizQuestionDto.class);
        if (quizQuestion.getAnswers() != null || !question.getAnswers().isEmpty()) {
            List<BaseQuizAnswerDto> answers = new ArrayList<>();
            for (QuizAnswer answer : quizQuestion.getAnswers()) {
                answers.add(ObjectUtil.copyProperties(answer, new BaseQuizAnswerDto(), BaseQuizAnswerDto.class));
            }
            if (isShuffle) {
                Collections.shuffle(answers);
            }
            question.setAnswers(answers);
        }
        return question;
    }

    public static QuizSubmissionResultResponse convertQuizSubmissionToSubmissionResult(QuizSubmittion submittion) {
        QuizSubmissionResultResponse response = new QuizSubmissionResultResponse();
        response.setId(submittion.getId());
        response.setPoint(submittion.getPoint());
        response.setCorrectNumber(submittion.getCorrectNumber());
        response.setTotalQuestion(submittion.getQuiz().getQuizQuestions().size());
        response.setStatus(submittion.getStatus());
        response.setSubmitBy(new QuizSubmissionResultResponse.UserInfo(submittion.getSubmittedBy().getId(), submittion.getSubmittedBy().getFullName()));
        response.setSubmitAt(submittion.getCreated());
        return response;
    }


    public static StudentClassResponse convertStudentClassToResponse(StudentClass studentClass) {
        User student = studentClass.getStudent();
        StudentClassResponse studentClassResponse = new StudentClassResponse();
        List<UserImage> userImages = student.getUserImages();
        List<UserImage> avatar = userImages.stream().filter(userImage -> userImage.getType().equals(EImageType.AVATAR)).collect(Collectors.toList());

        if (avatar.size() > 0) {
            ImageDto imageDto = ConvertUtil.convertUserImageToUserImageDto(avatar.stream().findFirst().get());
            studentClassResponse.setImages(imageDto);
        }

        studentClassResponse.setEmail(student.getEmail());
        studentClassResponse.setId(studentClass.getId());
        studentClassResponse.setName(student.getFullName());
        return studentClassResponse;
    }

    public static ActivityHistoryResponse convertActivityHistoryToActivityHistoryResponse(ActivityHistory activityHistory) {
        return ObjectUtil.copyProperties(activityHistory, new ActivityHistoryResponse(), ActivityHistoryResponse.class);
    }

    public static ClassResponse convertClassToClassResponse(Class clazz, List<Activity> authorizeSectionActivities) {
        ClassResponse classResponse = ObjectUtil.copyProperties(clazz, new ClassResponse(), ClassResponse.class);
        Course course = clazz.getCourse();
        if (course == null) {
            throw ApiException.create(HttpStatus.CONFLICT).withMessage("Lớp không thuộc về bất kì khóa học nào, vui lòng liên hệ với admin");
        }
        classResponse.setCourse(ConvertUtil.convertCourseToCourseDTO(course));
        List<TimeInWeekDTO> timeInWeekDTOS = clazz.getTimeInWeeks().stream().map(ConvertUtil::convertTimeInWeekToDto).collect(Collectors.toList());
        classResponse.setTimeInWeeks(timeInWeekDTOS);
        classResponse.setNumberOfCurrentStudent(clazz.getStudentClasses().size());
        ClassProgressTimeDto percentageOfClassTime = ClassUtil.getPercentageOfClassTime(clazz);
        classResponse.setProgress(percentageOfClassTime);
        User creator = course.getCreator();
        if (creator != null) {
            classResponse.setMentor(convertUsertoUserDto(creator));
        }
        List<ActivityDto> activityDtos = convertActivityAsTree(authorizeSectionActivities, false);
        classResponse.getActivities().addAll(activityDtos);
        return classResponse;
    }

    public static List<ActivityDto> convertActivityAsTree(List<Activity> authorizeSectionActivities, boolean isFixed) {
        List<ActivityDto> activityDtos = new ArrayList<>();
        for (Activity activity : authorizeSectionActivities) {
            if (isFixed && !activity.getFixed()) {
                continue;
            }
            ActivityDto activityDto = convertActivityToDto(activity);
            List<Activity> children = activity.getChildren();
            for (Activity child : children) {
                if (isFixed && !child.getFixed()) {
                    continue;
                }
                activityDto.getSubActivities().add(convertActivityToDto(child));
            }
            activityDtos.add(activityDto);
        }
        return activityDtos;
    }


//    public static ClassModuleDto convertClassModuleToDto(ClassModule classModule) {
//        return ObjectUtil.copyProperties(classModule, new ClassModuleDto(), ClassModuleDto.class, true);
//    }

    public static TimeTableResponse convertTimeTableToResponse(TimeTable timeTable) {
        TimeTableResponse timeTableResponse = ObjectUtil.copyProperties(timeTable, new TimeTableResponse(), TimeTableResponse.class, true);
        SlotDto slotDto = convertSlotToSlotDto(timeTable.getSlot());
        timeTableResponse.setSlot(slotDto);
        return timeTableResponse;
    }

    public static ClassAnnouncementDto convertClassAnnouncementToDto(ClassAnnouncement classAnnouncement) {
        ClassAnnouncementDto classAnnouncementDto = ObjectUtil.copyProperties(classAnnouncement, new ClassAnnouncementDto(), ClassAnnouncementDto.class, true);
        return classAnnouncementDto;
    }

    public static SimpleClassAnnouncementResponse convertClassAnnouncementToSimpleResponse(ClassAnnouncement classAnnouncement) {
        SimpleClassAnnouncementResponse cimpleClassAnnouncementResponse = ObjectUtil.copyProperties(classAnnouncement, new SimpleClassAnnouncementResponse(), SimpleClassAnnouncementResponse.class, true);
        return cimpleClassAnnouncementResponse;
    }

    public static FeedbackTemplateDto convertFeedbackToFeedbackTemplateDto(FeedbackTemplate feedbackTemplate) {
        FeedbackTemplateDto feedbackTemplateDto = ObjectUtil.copyProperties(feedbackTemplate, new FeedbackTemplateDto(), FeedbackTemplateDto.class);
        if (feedbackTemplate.getQuestions() != null) {
            ArrayList<FeedbackTemplateDto.FeedbackQuestionDto> questionDtos = new ArrayList<>();
            for (FeedbackQuestion question : feedbackTemplate.getQuestions()) {
                FeedbackTemplateDto.FeedbackQuestionDto questionDto = ObjectUtil.copyProperties(question, new FeedbackTemplateDto.FeedbackQuestionDto(), FeedbackTemplateDto.FeedbackQuestionDto.class);
                if (question.getAnswers() != null) {
                    ArrayList<FeedbackTemplateDto.FeedbackAnswerDto> answerDtos = new ArrayList<>();
                    for (FeedbackAnswer answer : question.getAnswers()) {
                        FeedbackTemplateDto.FeedbackAnswerDto answerDto = ObjectUtil.copyProperties(answer, new FeedbackTemplateDto.FeedbackAnswerDto(), FeedbackTemplateDto.FeedbackAnswerDto.class);
                        answerDtos.add(answerDto);
                    }
                    questionDto.setAnswers(answerDtos);
                }
                questionDtos.add(questionDto);
            }
            feedbackTemplateDto.setQuestions(questionDtos);
        }
        return feedbackTemplateDto;
    }

    public static FeedbackSubmissionResponse convertFeedbackSubmissionToResponse(FeedbackSubmission feedbackSubmission) {
        FeedbackSubmissionResponse response = ObjectUtil.copyProperties(feedbackSubmission, new FeedbackSubmissionResponse(), FeedbackSubmissionResponse.class);
        if (feedbackSubmission.getSubmitBy() != null) {
            response.setSubmitBy(convertUsertoUserDto(feedbackSubmission.getSubmitBy()));
        }
        List<FeedbackQuestion> feedbackQuestions = feedbackSubmission.getTemplate().getQuestions();
        List<FeedbackSubmissionResponse.FeedbackSubmitQuestion> submitQuestions = new ArrayList<>();
        for (FeedbackQuestion feedbackQuestion : feedbackQuestions) {
            FeedbackSubmissionResponse.FeedbackSubmitQuestion questionDto = new FeedbackSubmissionResponse.FeedbackSubmitQuestion();
            questionDto.setQuestion(feedbackQuestion.getQuestion());
            ArrayList<FeedbackSubmissionResponse.FeedbackSubmitAnswer> answerDtos = new ArrayList<>();
            for (FeedbackAnswer feedbackAnswer : feedbackQuestion.getAnswers()) {
                FeedbackSubmissionResponse.FeedbackSubmitAnswer answerDto = new FeedbackSubmissionResponse.FeedbackSubmitAnswer();
                Boolean isChosen = feedbackSubmission.getAnswers().stream()
                        .anyMatch(x -> x.getAnswer().getId().equals(feedbackAnswer.getId()));
                answerDto.setIsChosen(isChosen);
                answerDto.setAnswer(feedbackAnswer.getAnswer());
                answerDtos.add(answerDto);
            }
            questionDto.setAnswers(answerDtos);
            submitQuestions.add(questionDto);
        }
        response.setQuestions(submitQuestions);
        return response;
    }

    public static FeedbackResponse.FeedbackSubmission convertFeedbackSubmissionToFeedbackResponse(FeedbackSubmission feedbackSubmission){
        FeedbackResponse.FeedbackSubmission submission = new FeedbackResponse.FeedbackSubmission();
        submission.setRate(feedbackSubmission.getRate());
        submission.setSubmitBy(feedbackSubmission.getSubmitBy().getFullName());
        submission.setComment(feedbackSubmission.getComment());
        return submission;
    }
}