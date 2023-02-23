package fpt.project.bsmart.service.Impl;


import fpt.project.bsmart.entity.*;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.dto.InfoFindTutorDto;
import fpt.project.bsmart.entity.response.AccountResponse;
import fpt.project.bsmart.entity.response.InfoFindTutorResponse;
import fpt.project.bsmart.entity.response.SubjectResponse;
import fpt.project.bsmart.repository.AccountRepository;
import fpt.project.bsmart.repository.InfoFindTutorRepository;
import fpt.project.bsmart.repository.SubjectRepository;
import fpt.project.bsmart.service.IInfoFindTutorService;
import fpt.project.bsmart.util.ConvertUtil;
import fpt.project.bsmart.util.ObjectUtil;
import fpt.project.bsmart.util.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class InfoFindTutorServiceImpl implements IInfoFindTutorService {

    private final SubjectRepository subjectRepository;
    private final AccountRepository accountRepository;

    private final InfoFindTutorRepository infoFindTutorRepository;

    public InfoFindTutorServiceImpl(SubjectRepository subjectRepository, AccountRepository accountRepository, InfoFindTutorRepository infoFindTutorRepository) {
        this.subjectRepository = subjectRepository;
        this.accountRepository = accountRepository;
        this.infoFindTutorRepository = infoFindTutorRepository;
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    @Override
    public Boolean registerFindTutor(InfoFindTutorDto infoFindTutorDto) {
        InfoFindTutor infoFindTutor = new InfoFindTutor();
        infoFindTutor.setFullName(infoFindTutorDto.getFullName());
        if (isValidEmailAddress(infoFindTutorDto.getAddress())) {
            infoFindTutor.setAddress(infoFindTutorDto.getAddress());
        }
        infoFindTutor.setDescription(infoFindTutorDto.getDescription());
        infoFindTutor.setEmail(infoFindTutorDto.getEmail());
        infoFindTutor.setPhone(infoFindTutorDto.getPhone());
        infoFindTutor.setClassLevel(infoFindTutorDto.getClassLevel());

        // SET  Info_Find_Tutor_Subject
        List<InfoFindTutorSubject> informationFindTutorSubjectList = new ArrayList<>();

        infoFindTutorDto.getSubjectId().forEach(subjectId -> {
            Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(("Khong tim thay subject") + subjectId));
            InfoFindTutorSubject findTutorSubject = new InfoFindTutorSubject();

            // set id
            InformationFindTutorSubjectKey inforSubjectkey = new InformationFindTutorSubjectKey();
            inforSubjectkey.setSubjectId(subjectId);
            inforSubjectkey.setInfoFindTutorId(infoFindTutor.getId());
            findTutorSubject.setId(inforSubjectkey);

            findTutorSubject.setInfoFindTutor(infoFindTutor);
            findTutorSubject.setSubject(subject);
            informationFindTutorSubjectList.add(findTutorSubject);
        });
        infoFindTutor.setInfoFindTutorSubjects(informationFindTutorSubjectList);


        // Info_Find_Tutor_Account

        List<InfoFindTutorAccount> infoFindTutorAccountList = new ArrayList<>();
        infoFindTutorDto.getTeacherId().forEach(teacherId -> {
            Account teacher = accountRepository.findById(teacherId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage(("Khong tim thay giao vien") + teacherId));
            InfoFindTutorAccount infoFindTutorAccount = new InfoFindTutorAccount();
            InfoFindTutorAccountKey infoFindTutorAccountKey = new InfoFindTutorAccountKey();
            infoFindTutorAccountKey.setTeacherId(teacherId);
            infoFindTutorAccountKey.setInfoFindTutorId(infoFindTutor.getId());
            infoFindTutorAccount.setId(infoFindTutorAccountKey);
            infoFindTutorAccount.setTeacher(teacher);
            infoFindTutorAccount.setInfoFindTutor(infoFindTutor);
            infoFindTutorAccountList.add(infoFindTutorAccount);
        });
        infoFindTutor.setInfoFindTutorAccounts(infoFindTutorAccountList);
        infoFindTutorRepository.save(infoFindTutor);
        return true;
    }

    @Override
    public ApiPage<InfoFindTutorResponse> getAllRegisterFindTutor(Pageable pageable) {
        Page<InfoFindTutor> all = infoFindTutorRepository.findAll(pageable);
        return PageUtil.convert(all.map(this::convertInfoFindTutorToInfoFindTutorResponse));
    }

    public InfoFindTutorResponse convertInfoFindTutorToInfoFindTutorResponse(InfoFindTutor infoFindTutor) {
        InfoFindTutorResponse infoFindTutorResponse = ObjectUtil.copyProperties(infoFindTutor, new InfoFindTutorResponse(), InfoFindTutorResponse.class);

        //set List Teacher
        List<InfoFindTutorAccount> infoFindTutorAccounts = infoFindTutor.getInfoFindTutorAccounts();
        List<AccountResponse> accountResponses = new ArrayList<>();
        infoFindTutorAccounts.forEach(infoFindTutorAccount -> {
            accountResponses.add(ConvertUtil.doConvertEntityToResponse(infoFindTutorAccount.getTeacher()));
        });
        infoFindTutorResponse.setTeachers(accountResponses);


        // Set List Subject
        List<SubjectResponse> subjectResponses = new ArrayList<>();
        List<InfoFindTutorSubject> infoFindTutorSubjects = infoFindTutor.getInfoFindTutorSubjects();
        infoFindTutorSubjects.forEach(infoFindTutorSubject -> {
            subjectResponses.add(ConvertUtil.doConvertEntityToResponse(infoFindTutorSubject.getSubject()));
        });
        infoFindTutorResponse.setSubjects(subjectResponses);

        return infoFindTutorResponse;
    }

}
