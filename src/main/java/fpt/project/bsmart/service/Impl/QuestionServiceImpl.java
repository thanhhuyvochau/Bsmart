package fpt.project.bsmart.service.Impl;

import fpt.project.bsmart.entity.Question;
import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.repository.QuestionRepository;
import fpt.project.bsmart.repository.SubjectRepository;
import fpt.project.bsmart.service.IQuestionService;
import fpt.project.bsmart.util.QuestionUtil;
import fpt.project.bsmart.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class QuestionServiceImpl implements IQuestionService {
    private final QuestionRepository questionRepository;
    private final SubjectRepository subjectRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, SubjectRepository subjectRepository) {
        this.questionRepository = questionRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Boolean importQuestion(MultipartFile file, Long subjectId) throws IOException {
        User currentUser = SecurityUtil.getCurrentUser();
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy môn mà bạn muốn import câu hỏi, vui lòng thử lại!"));
        List<Question> questions = QuestionUtil.convertAikenToQuestion(file, currentUser, subject);
        questionRepository.saveAll(questions);
        return true;
    }
}
