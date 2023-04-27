package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.Answer;
import fpt.project.bsmart.entity.Question;
import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.constant.QuestionType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class QuestionUtil {
    public static List<Question> convertAikenToQuestion(MultipartFile file, User owner, Subject subject) throws IOException {
        List<Question> questions = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (Character.isLetter(line.charAt(0)) && line.charAt(1) == '.') {
                    int index = line.indexOf(".");
                    String answerContent = line.substring(index + 1).trim();
                    Answer answer = new Answer();
                    answer.setAnswer(answerContent);
                    answer.setKey(String.valueOf(line.charAt(0)));
                    questions.get(questions.size() - 1).getAnswers().add(answer);
                } else if (line.startsWith("ANSWER:")) {
                    String answerKey = line.substring(line.indexOf(":") + 1).trim();
                    Optional<Question> questionOptional = Optional.ofNullable(questions.get(questions.size() - 1));
                    questionOptional.ifPresent(question -> {
                        Optional<Answer> rightAnswerOptional = question.getAnswers().stream().filter(answer -> Objects.equals(answer.getKey(), answerKey)).findFirst();
                        if (rightAnswerOptional.isPresent()) {
                            rightAnswerOptional.get().setIsRight(true);
                        } else {
                            throw ApiException.create(HttpStatus.BAD_REQUEST).withMessage("Import câu hỏi thất bại do sai định dạng, vui lòng kiểm tra lại định dạng AIKEN đã được giới thiệu!");
                        }
                    });
                } else {
                    String question = line.trim();
                    Question q = new Question();
                    q.setQuestion(question);
                    q.setQuestionType(QuestionType.SINGLE);
                    q.setMentor(owner);
                    q.setSubject(subject);
                    questions.add(q);
                }
            }
        } catch (Exception e) {
            throw ApiException.create(HttpStatus.INTERNAL_SERVER_ERROR).withMessage(e.getMessage());
        }
        return questions;
    }
}
