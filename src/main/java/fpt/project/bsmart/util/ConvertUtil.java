package fpt.project.bsmart.util;


import fpt.project.bsmart.entity.Class;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.EQuestionType;
import fpt.project.bsmart.entity.constant.ETransactionStatus;
import fpt.project.bsmart.entity.constant.ETypeLearn;
import fpt.project.bsmart.entity.dto.*;
import fpt.project.bsmart.entity.response.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ConvertUtil {
    private static MessageUtil messageUtil;
    private static String successIcon;

    private static String failIcon;

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
        SubjectDto subjectDto = ObjectUtil.copyProperties(subject, new SubjectDto(), SubjectDto.class);
        if (subject.getCategory() != null) {
            subjectDto.setCategoryId(subject.getCategory().getId());
        }
        return subjectDto;
    }


    public static SectionDto convertSectionToSectionDto(Section section) {
        SectionDto sectionDto = ObjectUtil.copyProperties(section, new SectionDto(), SectionDto.class);
        if (section.getCourse() != null) {
            sectionDto.setCourseId(section.getCourse().getId());
        }
        if (!section.getModules().isEmpty()) {
            List<ModuleDto> moduleDtoList = new ArrayList<>();
            for (Module module : section.getModules()) {
                moduleDtoList.add(convertModuleToModuleDto(module));
            }
            sectionDto.setModules(moduleDtoList);
        }
        return sectionDto;
    }


    public static WalletDto convertWalletToWalletDto(Wallet wallet) {
        WalletDto walletDto = ObjectUtil.copyProperties(wallet, new WalletDto(), WalletDto.class);
        if (wallet.getOwner() != null) {
            walletDto.setOwner_id(wallet.getOwner().getId());
        }
        return walletDto;
    }

    public static ImageDto convertImageToImageDto(Image image) {
        ImageDto imageDto = ObjectUtil.copyProperties(image, new ImageDto(), ImageDto.class);
        if (image.getUser() != null) {
//            imageDto.setUserId(image.getUser().getId());
        }
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
        return new TimeInWeekDTO(dayOfWeekDTO, null, slotDto);
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
            for (Image image : user.getUserImages()) {
                if (image.isStatus()) {
                    imageDtoList.add(convertImageToImageDto(image));
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
        }

        return userDto;
    }

    public static ModuleDto convertModuleToModuleDto(Module module) {
        ModuleDto moduleDto = ObjectUtil.copyProperties(module, new ModuleDto(), ModuleDto.class);
        return moduleDto;
    }


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
            if (course.getSubject().getCategory() != null) {
                response.setCategoryDto(convertCategoryToCategoryDto(course.getSubject().getCategory()));
            }
        }


        return response;
    }

    public static SubCourseDetailResponse convertSubCourseToSubCourseDetailResponse(User userLogin, SubCourse subCourse) {
        SubCourseDetailResponse subCourseDetailResponse = ObjectUtil.copyProperties(subCourse, new SubCourseDetailResponse(), SubCourseDetailResponse.class);

        if (userLogin != null) {
            List<Order> orders = userLogin.getOrder();
            orders.forEach(order -> {
                List<OrderDetail> orderDetails = order.getOrderDetails();
                orderDetails.forEach(orderDetail -> {
                    SubCourse subCourse1 = orderDetail.getSubCourse();
                    if (subCourse1.equals(subCourse)) {
                        subCourseDetailResponse.setPurchase(true);
                    }
                });
            });
        }


        subCourseDetailResponse.setImage(ObjectUtil.copyProperties(subCourse.getImage(), new ImageDto(), ImageDto.class));
        List<TimeInWeek> timeInWeeks = subCourse.getTimeInWeeks();

        List<TimeInWeekDTO> timeInWeekDTOS = new ArrayList<>();
        timeInWeeks.forEach(timeInWeek -> {
            timeInWeekDTOS.add(convertTimeInWeekToDto(timeInWeek));
        });
        subCourseDetailResponse.setTimeInWeeks(timeInWeekDTOS);
        return subCourseDetailResponse;
    }

    public static CourseSubCourseDetailResponse convertCourseSubCourseToCourseSubCourseDetailResponse(Course course) {
        CourseSubCourseDetailResponse response = ObjectUtil.copyProperties(course, new CourseSubCourseDetailResponse(), CourseSubCourseDetailResponse.class);

        Subject subject = course.getSubject();
        if (subject != null) {
            response.setSubject(convertSubjectToSubjectDto(subject));
            Category category = subject.getCategory();
            if (category != null) {
                response.setCategory(convertCategoryToCategoryDto(category));
            }
        }

        return response;
    }

    public static CourseSubCourseResponse subCourseToCourseSubCourseResponseConverter(SubCourse subCourse) {
        Course course = subCourse.getCourse();
        CourseSubCourseResponse response = new CourseSubCourseResponse();
        if (course != null) {
            response.setCourseId(course.getId());
            response.setCourseCode(course.getCode());
            response.setCourseName(course.getName());
            response.setCourseDescription(course.getDescription());
            Subject subject = course.getSubject();
            if (subject != null) {
                SubjectDto subjectDto = convertSubjectToSubjectDto(subject);
                response.setSubject(subjectDto);
                Category category = subject.getCategory();
                if (category != null) {
                    CategoryDto categoryDto = convertCategoryToCategoryDto(category);
                    response.setCategory(categoryDto);
                }
            }
        }

        response.setSubCourseId(ObjectUtils.defaultIfNull(subCourse.getId(), null));
        response.setStatus(ObjectUtils.defaultIfNull(subCourse.getStatus(), null));
        response.setTypeLearn(ObjectUtils.defaultIfNull(subCourse.getTypeLearn(), null));
        response.setSubCourseTitle(ObjectUtils.defaultIfNull(subCourse.getTitle(), null));
        response.setLevel(ObjectUtils.defaultIfNull(subCourse.getLevel(), null));
        response.setMinStudent(ObjectUtils.defaultIfNull(subCourse.getMinStudent(), 0));
        response.setMaxStudent(ObjectUtils.defaultIfNull(subCourse.getMaxStudent(), 0));
        response.setPrice(ObjectUtils.defaultIfNull(subCourse.getPrice(),BigDecimal.ZERO));
        response.setEndDateExpected(ObjectUtils.defaultIfNull(subCourse.getEndDateExpected(), null));
        response.setStartDateExpected(ObjectUtils.defaultIfNull(subCourse.getStartDateExpected(), null));
        response.setNumberOfSlot(ObjectUtils.defaultIfNull(subCourse.getNumberOfSlot(), null));
        List<OrderDetail> orderDetails = subCourse.getOrderDetails();
        response.setFinalStudent(orderDetails.size());

        if (subCourse.getImage() != null) {
            response.setImageUrl(subCourse.getImage().getUrl());
        }



        if (subCourse.getMentor() != null) {
            response.setMentorName(subCourse.getMentor().getFullName());
        }

        List<TimeInWeek> timeInWeeks = subCourse.getTimeInWeeks();
        if (subCourse.getTimeInWeeks() != null) {
            List<TimeInWeekDTO> timeInWeekDtoList = new ArrayList<>();
            for (TimeInWeek timeInWeek : timeInWeeks) {
                timeInWeekDtoList.add(convertTimeInWeekToDto(timeInWeek));
            }
            response.setTimeInWeek(timeInWeekDtoList);
        }


        return response;
    }

    public static CourseResponse convertCourseCourseResponsePage(Course course) {


        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setId(course.getId());
        courseResponse.setCourseName(course.getName());
        courseResponse.setCourseCode(course.getCode());
        courseResponse.setCourseDescription(course.getDescription());
//        if (course.getSubCourses()!= null) {
//            courseResponse.setTotalSubCourse(course.getSubCourses().size());
//        }

        List<String> mentorName = new ArrayList<>();
        List<ETypeLearn> learns = new ArrayList<>();
        List<SubCourse> subCourses = course.getSubCourses();
        List<ImageDto> images = new ArrayList<>();
        subCourses.forEach(subCourse -> {
            if (subCourse.getMentor() != null) {
                mentorName.add(subCourse.getMentor().getFullName());
            }
            learns.add(subCourse.getTypeLearn());
            if (subCourse.getImage() != null) {
                images.add(ObjectUtil.copyProperties(subCourse.getImage(), new ImageDto(), ImageDto.class));
            }
        });
        courseResponse.setMentorName(mentorName);
        courseResponse.setImages(images);
        courseResponse.setLearns(learns);


        Subject subject = course.getSubject();
        if (subject != null) {
            courseResponse.setSubjectId(subject.getId());
            courseResponse.setSubjectName(subject.getName());
            Category category = subject.getCategory();
            if (category != null) {
                courseResponse.setCategoryId(category.getId());
                courseResponse.setCategoryName(category.getName());
            }


        }
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
                MentorSkillDto mentorSkillDto = convertMentorSkillToMentorSkillDto(mentorSkill);
                skillList.add(mentorSkillDto);
            }
            mentorProfileDTO.setMentorSkills(skillList);
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
        SubCourse chooseSubCourse = cartItem.getSubCourse();
        Course course = chooseSubCourse.getCourse();

        CourseCartResponse courseCartResponse = convertCourseToCourseCart(course);
        courseCartResponse.setCartItemId(cartItem.getId());
        for (SubCourse subCourse : course.getSubCourses()) {
            SubCourseCartResponse subCourseCartResponse = ObjectUtil.copyProperties(subCourse, new SubCourseCartResponse(), SubCourseCartResponse.class, true);
            if (subCourseCartResponse.getId().equals(chooseSubCourse.getId())) {
                subCourseCartResponse.setIsChosen(true);
            }
            if (subCourse.getMentor() != null) {
                subCourseCartResponse.setMentor(convertUsertoUserDto(subCourse.getMentor()));
            }
            if (subCourse.getImage() != null) {
                subCourseCartResponse.setImage(convertImageToImageDto(subCourse.getImage()));
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

    public static FeedbackQuestionDto convertFeedbackQuestionToFeedbackQuestionDto(FeedbackQuestion question) {
        FeedbackQuestionDto feedbackQuestionDto = ObjectUtil.copyProperties(question, new FeedbackQuestionDto(), FeedbackQuestionDto.class);
        if (feedbackQuestionDto.getQuestionType() == EQuestionType.MULTIPLE_CHOICE) {
            ArrayList<FeedbackAnswerDto> answer = new ArrayList<>();
            for(FeedbackAnswer feedbackAnswer : question.getAnswers()){
                answer.add(ObjectUtil.copyProperties(feedbackAnswer, new FeedbackAnswerDto(), FeedbackAnswerDto.class));
            }
        }
        return feedbackQuestionDto;
    }

    public static FeedbackTemplateDto convertTemplateToTemplateDto(FeedbackTemplate feedbackTemplate) {
        FeedbackTemplateDto feedbackTemplateDto = ObjectUtil.copyProperties(feedbackTemplate, new FeedbackTemplateDto(), FeedbackTemplateDto.class);
        if (feedbackTemplate.getQuestions() != null) {
            List<FeedbackQuestionDto> questions = new ArrayList<>();
            for (FeedbackQuestion feedbackQuestion : feedbackTemplate.getQuestions()) {
                questions.add(convertFeedbackQuestionToFeedbackQuestionDto(feedbackQuestion));
            }
            feedbackTemplateDto.setQuestions(questions);
        }
        return feedbackTemplateDto;
    }


    public static ClassResponse convertClassToClassResponse(Class clazz) {
        ClassResponse classResponse = ObjectUtil.copyProperties(clazz, new ClassResponse(), ClassResponse.class);
        if (clazz.getSubCourse() != null) {
            classResponse.setSubCourseName(clazz.getSubCourse().getTitle());
        }
        if (clazz.getSubCourse().getMentor() != null) {
            classResponse.setMentorName(clazz.getSubCourse().getMentor().getFullName());
        }
        return classResponse;
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

    public static ActivityDto convertActivityToDto(Activity activity) {
        ActivityType type = activity.getType();
        switch (type.getCode()) {
            case "ASSIGNMENT":
                ActivityDto<AssignmentDto> activityDto = ObjectUtil.copyProperties(activity, new ActivityDto<>(), ActivityDto.class, true);
                activityDto.setType(ConvertUtil.convertActivityTypeToDto(activity.getType()));
                AssignmentDto assignmentDto = convertAssignmentToDto(activity.getAssignment());
                activityDto.setActivityDetail(assignmentDto);
                return activityDto;
            case "QUIZ":
                return null; // convert quiz activity ở đây tương tự như trên
            default:
                throw ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy loại hoạt động!");
        }
    }

    private static AssignmentDto convertAssignmentToDto(Assignment assignment) {
        AssignmentDto assignmentDto = ObjectUtil.copyProperties(assignment, new AssignmentDto(), AssignmentDto.class, true);
        for (AssignmentFile assignmentFile : assignment.getAssignmentFiles()) {
            assignmentDto.getAssignmentFiles().add(ConvertUtil.convertAssignmentFileToDto(assignmentFile));
        }
        return assignmentDto;
    }

    public static AssignmentFileDto convertAssignmentFileToDto(AssignmentFile assignmentFile) {
        AssignmentFileDto assignmentFileDto = ObjectUtil.copyProperties(assignmentFile, new AssignmentFileDto(), AssignmentFileDto.class, true);
        assignmentFileDto.setUser(ConvertUtil.convertUsertoUserDto(assignmentFile.getUser()));
        return assignmentFileDto;
    }

    public static ActivityTypeDto convertActivityTypeToDto(ActivityType activityType) {
        ActivityTypeDto activityTypeResponse = new ActivityTypeDto();
        ObjectUtil.copyProperties(activityType, activityTypeResponse);
        return activityTypeResponse;
    }

    public static QuizDto convertQuizToQuizDto(Quiz quiz) {
        QuizDto quizDto = ObjectUtil.copyProperties(quiz, new QuizDto(), QuizDto.class);
        if (quiz.getQuizQuestions() != null || !quiz.getQuizQuestions().isEmpty()) {
            List<QuizQuestionDto> questionDtos = new ArrayList<>();
            for (QuizQuestion question : quiz.getQuizQuestions()) {
                questionDtos.add(ConvertUtil.convertQuizQuestionToQuizQuestionDto(question));
            }
            quizDto.setQuizQuestions(questionDtos);
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
            answerDto.setRight(answerDto.getRight());
            if (!quizSubmitAnswers.isEmpty()) {
                boolean isChosen = quizSubmitAnswers.stream()
                        .anyMatch(x -> x.getQuizAnswer().equals(answer));
                answerDto.setChosen(isChosen);
            }
        }
        return quizSubmitAnswerDtos;
    }

    public static QuizQuestionDto convertQuizQuestionToQuizQuestionDto(QuizQuestion quizQuestion) {
        QuizQuestionDto question = ObjectUtil.copyProperties(quizQuestion, new QuizQuestionDto(), QuizQuestionDto.class);
        if (quizQuestion.getAnswers() != null || !question.getAnswers().isEmpty()) {
            List<QuizAnswerDto> answers = new ArrayList<>();
            for (QuizAnswer answer : quizQuestion.getAnswers()) {
                answers.add(ConvertUtil.convertQuizAnswerToQuizAnswerDto(answer));
            }
            question.setAnswers(answers);
        }
        return question;
    }

    public static QuizAnswerDto convertQuizAnswerToQuizAnswerDto(QuizAnswer quizAnswer) {
        return ObjectUtil.copyProperties(quizAnswer, new QuizAnswerDto(), QuizAnswerDto.class);
    }
}