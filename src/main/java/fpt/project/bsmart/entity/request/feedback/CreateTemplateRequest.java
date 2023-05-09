package fpt.project.bsmart.entity.request.feedback;

import fpt.project.bsmart.entity.constant.EQuestionType;

import java.util.Hashtable;

public class CreateTemplateRequest {
    private String question;
    private EQuestionType questionType;
    private Hashtable<Double, String> possibleAnswer = new Hashtable<>();
}
