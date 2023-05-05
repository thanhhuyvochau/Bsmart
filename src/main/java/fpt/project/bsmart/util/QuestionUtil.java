package fpt.project.bsmart.util;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class QuestionUtil {
    private static final String DELIMITER = "|";
    private static final String DELIMITER_REGEX = "\\|";
    public static final Long MIN_QUESTION_SCORE = 0L;
    public static final Long MAX_QUESTION_SCORE = 5L;
    public static final Integer MIN_ANSWER_IN_QUESTION = 2;
    public static final Integer MAX_ANSWER_IN_QUESTION = 5;
    public static final Integer MIN_QUESTION_IN_TEMPLATE = 2;
    public static final Integer MAX_QUESTION_IN_TEMPLATE = 10;

    public static List<String> convertAnswerStringToAnswerList(String answerString){
        if(StringUtil.isNullOrEmpty(answerString)){
            return null;
        }
        return Arrays.asList(answerString.split(DELIMITER_REGEX));
    }

    public static List<Long> convertScoreStringToScoreList(String scoreString){
        if(StringUtil.isNullOrEmpty(scoreString)){
            return null;
        }
        return Arrays.stream(scoreString.split(DELIMITER_REGEX))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
    public static HashMap<String, Long> convertAnswerAndScoreStringToPossibleAnswer(String answerString, String scoreString){
        if(StringUtil.isNullOrEmpty(answerString) || StringUtil.isNullOrEmpty(scoreString)){
            return null;
        }
        List<String> answers = convertAnswerStringToAnswerList(answerString);
        List<Long> scores = convertScoreStringToScoreList(scoreString);
        if(answers.size() != scores.size()){
            return null;
        }
        HashMap<String, Long> possibleAnswer = new HashMap<>();
        for(int i = 0; i < answers.size(); i++){
            possibleAnswer.put(answers.get(i), scores.get(i));
        }
        return possibleAnswer;
    }
    public static String addNewAnswerToAnswerString(String answerString, String newAnswer){
        if(newAnswer == null){
            newAnswer = "";
        }
        if(answerString.isEmpty()){
            return newAnswer;
        }
        return answerString.join(DELIMITER, newAnswer);
    }
    public static String convertScoresToScoreString(List<Long> scores){
        if (isValidList(scores)){
            return null;
        }
        return String.join(DELIMITER, scores.stream().map(Objects::toString).collect(Collectors.toList()));
    }

    public static String convertAnswersToAnswerString(List<String> answers){
        if(isValidList(answers)){
            return null;
        }
        return String.join(DELIMITER, answers);
    }
    private static boolean isValidList(List list){
        return list == null || list.isEmpty();
    }

}
