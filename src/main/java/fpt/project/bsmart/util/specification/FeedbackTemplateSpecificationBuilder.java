package fpt.project.bsmart.util.specification;

import fpt.project.bsmart.entity.FeedbackTemplate;
import fpt.project.bsmart.entity.FeedbackTemplate_;
import fpt.project.bsmart.entity.constant.EFeedbackType;
import fpt.project.bsmart.util.StringUtil;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeedbackTemplateSpecificationBuilder {
    public static FeedbackTemplateSpecificationBuilder feedbackTemplateSpecificationBuilder(){
        return new FeedbackTemplateSpecificationBuilder();
    }
    private List<Specification<FeedbackTemplate>> specifications = new ArrayList<>();

    public FeedbackTemplateSpecificationBuilder filterByType(EFeedbackType type){
        if(type == null){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(FeedbackTemplate_.TYPE), type));
        return this;
    }

    public FeedbackTemplateSpecificationBuilder filterByName(String name){
        if(StringUtil.isNullOrEmpty(name)){
            return this;
        }
        specifications.add((root, query, criteriaBuilder) -> {
            String search = name.replaceAll("\\s\\s+", " ").trim();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("%").append(search).append("%");
            return criteriaBuilder.like(root.get(FeedbackTemplate_.NAME), stringBuilder.toString());
        });
        return this;
    }

    public Specification<FeedbackTemplate> build(){
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(all(), Specification::and);
    }

    public Specification<FeedbackTemplate> all(){return  Specification.where(null);}
}
