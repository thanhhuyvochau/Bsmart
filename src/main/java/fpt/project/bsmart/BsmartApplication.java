package fpt.project.bsmart;

import com.fasterxml.jackson.core.JsonProcessingException;
import fpt.project.bsmart.config.notify.NotificationProperties;
import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.DayOfWeek;
import fpt.project.bsmart.entity.common.*;
import fpt.project.bsmart.moodle.repository.MoodleCourseRepository;
import fpt.project.bsmart.moodle.request.CreateCategoryRequest;
import fpt.project.bsmart.moodle.request.GetCategoryRequest;
import fpt.project.bsmart.moodle.response.MoodleCategoryResponse;
import fpt.project.bsmart.repository.*;
import fpt.project.bsmart.service.IMoodleService;
import fpt.project.bsmart.util.ObjectUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fpt.project.bsmart.entity.common.ESubjectCode.*;


@SpringBootApplication
public class BsmartApplication {

    private final Environment evn;
    private final NotificationProperties notificationProperties;

    private final RoleRepository roleRepository;


    private final SubjectRepository subjectRepository;


    private final MoodleCourseRepository moodleCourseRepository;

    private final SlotRepository slotRepository;

    private final DayOfWeekRepository dayOfWeekRepository;

    private final ClassRepository classRepository;
    private final ForumRepository forumRepository;

    private final ClassLevelRepository classLevelRepository;

    private final RequestTypeRepository requestTypeRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final IMoodleService moodleService;

    private final VoiceRepository voiceRepository ;

    public BsmartApplication(Environment evn, NotificationProperties notificationProperties, RoleRepository roleRepository, SubjectRepository subjectRepository, RequestTypeRepository requestTypeRepository, MoodleCourseRepository moodleCourseRepository, SlotRepository slotRepository, DayOfWeekRepository dayOfWeekRepository, ClassRepository classRepository, ForumRepository forumRepository, ClassLevelRepository classLevelRepository, NotificationTypeRepository notificationTypeRepository, IMoodleService moodleService, VoiceRepository voiceRepository) {
        this.evn = evn;
        this.notificationProperties = notificationProperties;
        this.roleRepository = roleRepository;
        this.subjectRepository = subjectRepository;
        this.requestTypeRepository = requestTypeRepository;
        this.moodleCourseRepository = moodleCourseRepository;
        this.slotRepository = slotRepository;
        this.dayOfWeekRepository = dayOfWeekRepository;
        this.classRepository = classRepository;
        this.forumRepository = forumRepository;
        this.classLevelRepository = classLevelRepository;
        this.notificationTypeRepository = notificationTypeRepository;

        this.moodleService = moodleService;
        this.voiceRepository = voiceRepository;
    }


    public static void main(String[] args) throws ParseException {

        SpringApplication.run(BsmartApplication.class, args);


    }


    @EventListener(ApplicationReadyEvent.class)
    public void intiDataRole() throws JsonProcessingException {

        List<Role> roles = roleRepository.findAll();
        Map<EAccountRole, Role> roleMap = roles.stream().collect(Collectors.toMap(Role::getCode, Function.identity()));
        for (EAccountRole value : EAccountRole.values()) {
            Role role = roleMap.get(value);
            Role newRole = new Role();
            newRole.setCode(value);
            newRole.setName(value.getLabel());

            if (role == null) {
                roles.add(newRole);
            } else if (!Objects.equals(newRole, role)) {
                role.setCode(value);
                role.setName(value.getLabel());
            }
        }
        roleRepository.saveAll(roles);
        moodleService.synchronizedRoleFromMoodle();
    }


    @EventListener(ApplicationReadyEvent.class)
    public void intiDataSubject() {
        List<Subject> allSubject = subjectRepository.findAll();
        boolean existToan = false;
        boolean existVatLy = false;
        boolean existHoaHoc = false;
        boolean existTiengAnh = false;
        boolean existSinhHoc = false;
        boolean existNguVan = false;
        boolean existTinHoc = false;


        for (Subject subject : allSubject) {
            if (subject.getCode().equals(Toan)) {
                existToan = true;
            }
            if (subject.getCode().equals(VatLy)) {
                existVatLy = true;
            }
            if (subject.getCode().equals(HoaHoc)) {
                existHoaHoc = true;
            }
            if (subject.getCode().equals(TiengAnh)) {
                existTiengAnh = true;
            }
            if (subject.getCode().equals(SinhHoc)) {
                existSinhHoc = true;
            }
            if (subject.getCode().equals(NguVan)) {
                existNguVan = true;
            }
            if (subject.getCode().equals(TinHoc)) {
                existTinHoc = true;
            }
        }
        List<Subject> subjectList = new ArrayList<>();
        if (!existToan) {
            Subject subject = new Subject();
            subject.setCode(ESubjectCode.Toan);
            subject.setName(Toan.label);
            createSubjectForum(subject);
            subjectList.add(subject);
        }
        if (!existVatLy) {
            Subject subject = new Subject();
            subject.setCode(ESubjectCode.VatLy);
            subject.setName(VatLy.label);
            createSubjectForum(subject);
            subjectList.add(subject);
        }
        if (!existHoaHoc) {
            Subject subject = new Subject();
            subject.setCode(ESubjectCode.HoaHoc);
            subject.setName(HoaHoc.label);
            createSubjectForum(subject);
            subjectList.add(subject);
        }
        if (!existTiengAnh) {
            Subject subject = new Subject();
            subject.setCode(ESubjectCode.TiengAnh);
            subject.setName(TiengAnh.label);
            createSubjectForum(subject);
            subjectList.add(subject);
        }
        if (!existSinhHoc) {
            Subject subject = new Subject();
            subject.setCode(ESubjectCode.SinhHoc);
            subject.setName(SinhHoc.label);
            createSubjectForum(subject);
            subjectList.add(subject);
        }
        if (!existNguVan) {
            Subject subject = new Subject();
            subject.setCode(ESubjectCode.NguVan);
            subject.setName(NguVan.label);
            createSubjectForum(subject);
            subjectList.add(subject);
        }
        if (!existTinHoc) {
            Subject subject = new Subject();
            subject.setCode(ESubjectCode.TinHoc);
            subject.setName(TinHoc.label);
            createSubjectForum(subject);
            subjectList.add(subject);
        }


        subjectRepository.saveAll(subjectList);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void intiSubjectToMoodle() throws JsonProcessingException {

        GetCategoryRequest request = new GetCategoryRequest();
        List<GetCategoryRequest.MoodleCategoryBody> moodleCategoryBodyList = new ArrayList<>();
        GetCategoryRequest.MoodleCategoryBody moodleCategoryBody = new GetCategoryRequest.MoodleCategoryBody();
        moodleCategoryBody.setKey("id");
        moodleCategoryBody.setValue("");
        moodleCategoryBodyList.add(moodleCategoryBody);
        request.setCriteria(moodleCategoryBodyList);
        List<MoodleCategoryResponse> category = moodleCourseRepository.getCategories(request);

        List<String> allNameCategory = category.stream().map(MoodleCategoryResponse::getName).collect(Collectors.toList());


        List<Subject> allSubject = subjectRepository.findAll();
        List<String> allNameSubject = allSubject.stream().map(subject -> subject.getCode().name()).collect(Collectors.toList());

        List<String> collect = allNameSubject.stream().filter(s -> !allNameCategory.contains(s)).filter(Objects::nonNull).collect(Collectors.toList());

//        List<String> checkSubjectMoodleToVuonDau = allNameSubject.stream().filter(allNameCategory::contains).collect(Collectors.toList());


        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();

        List<CreateCategoryRequest.CreateCategoryBody> createCategoryBodyList = new ArrayList<>();

        for (String s : collect) {
            CreateCategoryRequest.CreateCategoryBody set = new CreateCategoryRequest.CreateCategoryBody();
            set.setName(s);
            set.setParent(0L);
            set.setIdnumber("");
            set.setDescription("");
            set.setDescriptionformat(0L);

            createCategoryBodyList.add(set);
        }


        createCategoryRequest.setCategories(createCategoryBodyList);
        moodleCourseRepository.createCategory(createCategoryRequest);

    }

    @EventListener(ApplicationReadyEvent.class)
    public void intiMoodleCategoryIdToSubject() throws JsonProcessingException {
        GetCategoryRequest request = new GetCategoryRequest();
        List<GetCategoryRequest.MoodleCategoryBody> moodleCategoryBodyList = new ArrayList<>();
        GetCategoryRequest.MoodleCategoryBody moodleCategoryBody = new GetCategoryRequest.MoodleCategoryBody();
        moodleCategoryBody.setKey("id");
        moodleCategoryBody.setValue("");
        moodleCategoryBodyList.add(moodleCategoryBody);
        request.setCriteria(moodleCategoryBodyList);
        List<MoodleCategoryResponse> category = moodleCourseRepository.getCategories(request);

        List<String> allNameCategory = category.stream().map(MoodleCategoryResponse::getName).collect(Collectors.toList());


        List<Subject> allSubject = subjectRepository.findAll();
        List<String> allNameSubject = allSubject.stream().map(subject -> subject.getCode().name()).collect(Collectors.toList());

        List<String> collect = allNameSubject.stream().filter(allNameCategory::contains).filter(Objects::nonNull).collect(Collectors.toList());

        List<Subject> subjectList = new ArrayList<>();
        for (String s : collect) {
            ESubjectCode eSubjectCode = ESubjectCode.valueOf(s);
            Subject subject = subjectRepository.findByCode(eSubjectCode);
            for (MoodleCategoryResponse moodleCategoryResponse : category) {
                if (moodleCategoryResponse.getName().equals(subject.getCode().name())) {
                    subject.setCategoryMoodleId(moodleCategoryResponse.getId());
                    subjectList.add(subject);
                }
            }

        }
        subjectRepository.saveAll(subjectList);
    }

/**TODO: fix bug null pointer exception this function @Dang*/
//    @EventListener(ApplicationReadyEvent.class)
//    public void intiClassMoodleOntoToClass() throws JsonProcessingException {
//
//        MoodleMasterDataRequest request = new MoodleMasterDataRequest();
//
//        List<MoodleCourseResponse> courseMoodle = moodleCourseRepository.getCourses(request);
//
//        List<Class> allClass = classRepository.findAll();
//        List<Class> classList = new ArrayList<>();
//        courseMoodle.stream().map(moodleClassResponse -> {
//            for (Class aClass : allClass) {
//                if (aClass.getCode().equals(moodleClassResponse.getShortname())) {
//                    aClass.setMoodleClassId(moodleClassResponse.getId());
//                    classList.add(aClass);
//                }
//            }
//            return moodleClassResponse;
//        }).collect(Collectors.toList());
//
//        courseMoodle.forEach(moodleClassResponse -> {
//            Class byCode = classRepository.findByCode(moodleClassResponse.getShortname());
//            if (byCode != null) {
//                byCode.setMoodleClassId(moodleClassResponse.getId());
//
//                classList.add(byCode);
//            }
//            if (byCode == null) {
////                Subject byCategoryMoodleId = subjectRepository.findByCategoryMoodleId(moodleClassResponse.getCategoryid());
//                Class aClass = new Class();
//                aClass.setCode(moodleClassResponse.getShortname());
//                aClass.setName(moodleClassResponse.getFullname());
//
//                aClass.setActive(true);
//                classList.add(aClass);
//            }
//        });
//
//        classRepository.saveAll(classList);
//    }


    @EventListener(ApplicationReadyEvent.class)
    public void intiDatSlot() {

        List<Slot> allSlot = slotRepository.findAll();
        Boolean existSlot1 = false;
        Boolean existSlot2 = false;
        Boolean existSlot3 = false;
        Boolean existSlot4 = false;
        Boolean existSlot5 = false;
        Boolean existSlot6 = false;
        Boolean existSlot7 = false;
        Boolean existSlot8 = false;
        Boolean existSlot9 = false;
        Boolean existSlot10 = false;
        for (Slot slot : allSlot) {
            if (slot.getCode().equals(ESlotCode.SLOT1)) {
                existSlot1 = true;
            }
            if (slot.getCode().equals(ESlotCode.SLOT2)) {
                existSlot2 = true;
            }
            if (slot.getCode().equals(ESlotCode.SLOT3)) {
                existSlot3 = true;
            }
            if (slot.getCode().equals(ESlotCode.SLOT4)) {
                existSlot4 = true;
            }
            if (slot.getCode().equals(ESlotCode.SLOT5)) {
                existSlot5 = true;
            }
            if (slot.getCode().equals(ESlotCode.SLOT6)) {
                existSlot6 = true;
            }
            if (slot.getCode().equals(ESlotCode.SLOT7)) {
                existSlot7 = true;
            }
            if (slot.getCode().equals(ESlotCode.SLOT8)) {
                existSlot8 = true;
            }
            if (slot.getCode().equals(ESlotCode.SLOT9)) {
                existSlot9 = true;
            }
            if (slot.getCode().equals(ESlotCode.SLOT10)) {
                existSlot10 = true;
            }
        }

        List<Slot> slotList = new ArrayList<>();
        if (!existSlot1) {
            Slot slotOne = new Slot();
            slotOne.setCode(ESlotCode.SLOT1);
            slotOne.setName("slot One");
            slotOne.setStartTime("07:00");
            slotOne.setEndTime("08:30");
            slotList.add(slotOne);
        }
        if (!existSlot2) {
            Slot slotTwo = new Slot();
            slotTwo.setCode(ESlotCode.SLOT2);
            slotTwo.setName("slot Two");
            slotTwo.setStartTime("08:30");
            slotTwo.setEndTime("10:00");
            slotList.add(slotTwo);
        }

        if (!existSlot3) {
            Slot slotThree = new Slot();
            slotThree.setCode(ESlotCode.SLOT3);
            slotThree.setName("slot Three");
            slotThree.setStartTime("10:00");
            slotThree.setEndTime("11:30");
            slotList.add(slotThree);
        }

        if (!existSlot4) {
            Slot slotFour = new Slot();
            slotFour.setCode(ESlotCode.SLOT4);
            slotFour.setName("slot Four");
            slotFour.setStartTime("11:30");
            slotFour.setEndTime("13:00");
            slotList.add(slotFour);
        }

        if (!existSlot5) {
            Slot slotFive = new Slot();
            slotFive.setCode(ESlotCode.SLOT5);
            slotFive.setName("slot Fine");
            slotFive.setStartTime("13:00");
            slotFive.setEndTime("14:30");
            slotList.add(slotFive);
        }

        if (!existSlot6) {
            Slot slotSix = new Slot();
            slotSix.setCode(ESlotCode.SLOT6);
            slotSix.setName("slot Six");
            slotSix.setStartTime("14:30");
            slotSix.setEndTime("16:00");
            slotList.add(slotSix);
        }

        if (!existSlot7) {
            Slot slotSeven = new Slot();
            slotSeven.setCode(ESlotCode.SLOT7);
            slotSeven.setName("slot Seven");
            slotSeven.setStartTime("16:00");
            slotSeven.setEndTime("17:30");
            slotList.add(slotSeven);
        }


        if (!existSlot8) {
            Slot slotEight = new Slot();
            slotEight.setCode(ESlotCode.SLOT8);
            slotEight.setName("slot Eight");
            slotEight.setStartTime("17:30");
            slotEight.setEndTime("19:00");
            slotList.add(slotEight);
        }


        if (!existSlot9) {
            Slot slotNine = new Slot();
            slotNine.setCode(ESlotCode.SLOT9);
            slotNine.setName("slot Nine");
            slotNine.setStartTime("19:00");
            slotNine.setEndTime("20:30");
            slotList.add(slotNine);
        }
        slotRepository.saveAll(slotList);


    }


    @EventListener(ApplicationReadyEvent.class)
    public void intiDataDateOfWeek() {

        List<DayOfWeek> allDayOfWeeks = dayOfWeekRepository.findAll();
        Boolean existDay1 = false;
        Boolean existDay2 = false;
        Boolean existDay3 = false;
        Boolean existDay4 = false;
        Boolean existDay5 = false;
        Boolean existDay6 = false;
        Boolean existDay7 = false;

        for (DayOfWeek dayOfWeek : allDayOfWeeks) {
            if (dayOfWeek.getCode().equals(EDayOfWeekCode.SUNDAY)) {
                existDay1 = true;
            }
            if (dayOfWeek.getCode().equals(EDayOfWeekCode.MONDAY)) {
                existDay2 = true;
            }
            if (dayOfWeek.getCode().equals(EDayOfWeekCode.TUESDAY)) {
                existDay3 = true;
            }
            if (dayOfWeek.getCode().equals(EDayOfWeekCode.WEDNESDAY)) {
                existDay4 = true;
            }
            if (dayOfWeek.getCode().equals(EDayOfWeekCode.THURSDAY)) {
                existDay5 = true;
            }
            if (dayOfWeek.getCode().equals(EDayOfWeekCode.FRIDAY)) {
                existDay6 = true;
            }
            if (dayOfWeek.getCode().equals(EDayOfWeekCode.SATURDAY)) {
                existDay7 = true;
            }


        }

        List<DayOfWeek> dayOfWeekList = new ArrayList<>();
        if (!existDay1) {
            DayOfWeek dayOfWeek = new DayOfWeek();
            dayOfWeek.setCode(EDayOfWeekCode.SUNDAY);
            dayOfWeek.setName("Chủ Nhật");
            dayOfWeekList.add(dayOfWeek);
        }
        if (!existDay2) {
            DayOfWeek dayOfWeek = new DayOfWeek();
            dayOfWeek.setCode(EDayOfWeekCode.MONDAY);
            dayOfWeek.setName("Thứ hai");
            dayOfWeekList.add(dayOfWeek);
        }
        if (!existDay3) {
            DayOfWeek dayOfWeek = new DayOfWeek();
            dayOfWeek.setCode(EDayOfWeekCode.TUESDAY);
            dayOfWeek.setName("Thứ Ba");
            dayOfWeekList.add(dayOfWeek);
        }

        if (!existDay4) {
            DayOfWeek dayOfWeek = new DayOfWeek();
            dayOfWeek.setCode(EDayOfWeekCode.WEDNESDAY);
            dayOfWeek.setName("Thứ Tư");
            dayOfWeekList.add(dayOfWeek);
        }

        if (!existDay5) {
            DayOfWeek dayOfWeek = new DayOfWeek();
            dayOfWeek.setCode(EDayOfWeekCode.THURSDAY);
            dayOfWeek.setName("Thứ Năm");
            dayOfWeekList.add(dayOfWeek);
        }

        if (!existDay6) {
            DayOfWeek dayOfWeek = new DayOfWeek();
            dayOfWeek.setCode(EDayOfWeekCode.FRIDAY);
            dayOfWeek.setName("Thứ Sáu");
            dayOfWeekList.add(dayOfWeek);
        }

        if (!existDay7) {
            DayOfWeek dayOfWeek = new DayOfWeek();
            dayOfWeek.setCode(EDayOfWeekCode.SATURDAY);
            dayOfWeek.setName("Thứ Bảy");
            dayOfWeekList.add(dayOfWeek);
        }


        dayOfWeekRepository.saveAll(dayOfWeekList);

    }

    private Boolean createSubjectForum(Subject subject) {
        Forum forum = new Forum();
        forum.setName(subject.getName());
        forum.setCode(subject.getCode().name());
        forum.setSubject(subject);
        forum.setType(EForumType.SUBJECT);
        return true;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void intiDataAccountAdmin() {

        List<ClassLevel> all = classLevelRepository.findAll();
        Boolean existClass10 = false;
        Boolean existClass11 = false;
        Boolean existClass12 = false;

        for (ClassLevel classType : all) {
            if (classType.getCode().equals(EClassLevel.TEN)) {
                existClass10 = true;
            }

        }

        List<ClassLevel> classTypeList = new ArrayList<>();
        if (!existClass10) {
            ClassLevel classLevel = new ClassLevel();
            classLevel.setCode(EClassLevel.TEN);
            classLevel.setName("Lớp 10");

            classTypeList.add(classLevel);
        }


        classLevelRepository.saveAll(classTypeList);


    }

    @EventListener(ApplicationReadyEvent.class)
    public void intiDataClassLevel() {

        List<ClassLevel> all = classLevelRepository.findAll();
        Boolean existClass10 = false;
        Boolean existClass11 = false;
        Boolean existClass12 = false;

        for (ClassLevel classType : all) {
            if (classType.getCode().equals(EClassLevel.TEN)) {
                existClass10 = true;
            }
            if (classType.getCode().equals(EClassLevel.ELEVENT)) {
                existClass11 = true;
            }
            if (classType.getCode().equals(EClassLevel.TWELFTH)) {
                existClass12 = true;
            }
        }

        List<ClassLevel> classTypeList = new ArrayList<>();
        if (!existClass10) {
            ClassLevel classLevel = new ClassLevel();
            classLevel.setCode(EClassLevel.TEN);
            classLevel.setName("Lớp 10");

            classTypeList.add(classLevel);
        }
        if (!existClass11) {

            ClassLevel classLevel = new ClassLevel();
            classLevel.setCode(EClassLevel.ELEVENT);
            classLevel.setName("Lớp 11");

            classTypeList.add(classLevel);

        }

        if (!existClass12) {
            ClassLevel classLevel = new ClassLevel();
            classLevel.setCode(EClassLevel.TWELFTH);
            classLevel.setName("Lớp 12");

            classTypeList.add(classLevel);
        }

        classLevelRepository.saveAll(classTypeList);


    }

    @EventListener(ApplicationReadyEvent.class)
    public void intiDataRequestType() {

        List<RequestType> all = requestTypeRepository.findAll();


        Boolean application1 = false;
        Boolean application2 = false;
        Boolean application3 = false;
        Boolean application4 = false;

        for (RequestType requestType : all) {
            if (requestType.getName().equals("Đơn đề nghỉ cấp bảng điêm quá trình")) {
                application1 = true;
            }
            if (requestType.getName().equals("Đơn xin nhâp học lại")) {
                application2 = true;
            }
            if (requestType.getName().equals("Đơn đánh giá giáo viên")) {
                application3 = true;
            }
            if (requestType.getName().equals("Các loại đơn khác")) {
                application4 = true;
            }
        }

        List<RequestType> requestTypeList = new ArrayList<>();
        if (!application1) {
            RequestType requestType = new RequestType();
            requestType.setName("Đơn đề nghỉ cấp bảng điêm quá trình");
            requestTypeList.add(requestType);
        }
        if (!application2) {

            RequestType requestType = new RequestType();
            requestType.setName("Đơn xin nhâp học lại");
            requestTypeList.add(requestType);

        }

        if (!application3) {
            RequestType requestType = new RequestType();
            requestType.setName("Đơn đánh giá giáo viên");

            requestTypeList.add(requestType);
        }
        if (!application4) {
            RequestType requestType = new RequestType();
            requestType.setName("Các loại đơn khác");
            requestTypeList.add(requestType);

        }
        requestTypeRepository.saveAll(requestTypeList);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void intiDataVoice() {

        List<Voice> all = voiceRepository.findAll();


        Boolean mienNam = false;
        Boolean mienBac = false;
        Boolean mienTrung = false;
        Boolean khac = false;

        for (Voice voice : all) {
            if (voice.getName().equals("Miền nam")) {
                mienNam = true;
            }
            if (voice.getName().equals("Miền bắc")) {
                mienBac = true;
            }
            if (voice.getName().equals("Miền trung")) {
                mienTrung = true;
            }
            if (voice.getName().equals("Giọng Khác")) {
                khac = true;
            }
        }

        List<Voice> voiceList = new ArrayList<>();
        if (!mienNam) {
            Voice voice = new Voice();
            voice.setName("Miền nam");
            voiceList.add(voice);
        }
        if (!mienBac) {

            Voice voice = new Voice();
            voice.setName("Miền bắc");
            voiceList.add(voice);
        }

        if (!mienTrung) {
            Voice voice = new Voice();
            voice.setName("Miền trung");
            voiceList.add(voice);
        }
        if (!khac) {
            Voice voice = new Voice();
            voice.setName("Giọng Khác");
            voiceList.add(voice);
        }
        voiceRepository.saveAll(voiceList);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initNotificationType() {
        List<NotificationProperties.NotifyDetail> configNotifies = notificationProperties.getNotify();
        List<NotificationType> notificationTypes = notificationTypeRepository.findAll();
        Map<String, NotificationType> notificationTypeMap = notificationTypes.stream().collect(Collectors.toMap(NotificationType::getCode, Function.identity()));
        for (NotificationProperties.NotifyDetail configNotify : configNotifies) {
            NotificationType newNotificationType = ObjectUtil.copyProperties(configNotify, new NotificationType(), NotificationType.class, true);
            NotificationType existedNotifyType = notificationTypeMap.get(configNotify.getCode());
            if (existedNotifyType == null) {
                notificationTypes.add(newNotificationType);
            } else if (!Objects.equals(newNotificationType, existedNotifyType)) {
                existedNotifyType.setEntity(newNotificationType.getEntity());
                existedNotifyType.setTemplate(newNotificationType.getTemplate());
                existedNotifyType.setTitle(newNotificationType.getTitle());
            }
        }
        notificationTypeRepository.saveAll(notificationTypes);
    }
}