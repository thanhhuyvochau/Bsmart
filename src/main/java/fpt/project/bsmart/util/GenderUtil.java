package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.constant.EGenderType;
import fpt.project.bsmart.entity.response.GenderResponse;

import java.util.ArrayList;
import java.util.List;

public class GenderUtil {
    public static List<GenderResponse> getGendersAsList() {
        List<GenderResponse> genderResponses = new ArrayList<>();
        for (EGenderType gender : EGenderType.values()) {
            GenderResponse genderResponse = new GenderResponse();
            genderResponse.setCode(gender.name());
            genderResponse.setName(gender.getLabel());
            genderResponses.add(genderResponse);
        }
        return genderResponses;
    }

    public static EGenderType getGenderByCode(String genderName) {
        if (genderName == null) {
            return EGenderType.OTHER;
        }
        switch (genderName) {
            case "MALE":
                return EGenderType.MALE;
            case "FEMALE":
                return EGenderType.FEMALE;
            default:
                return EGenderType.OTHER;
        }
    }
}
