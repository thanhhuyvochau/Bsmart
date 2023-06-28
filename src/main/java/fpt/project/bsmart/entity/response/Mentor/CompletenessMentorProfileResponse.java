package fpt.project.bsmart.entity.response.Mentor;

import java.util.List;

public class CompletenessMentorProfileResponse {

    private Integer percentComplete;

    private List<MissingInformation> missingInformation;

    private Boolean allowSendingApproval;

    public static class MissingInformation {

        public RequiredInfo requiredInfo;

        public OptionalInfo optionalInfo;

        public static class RequiredInfo {
            List<Field> fields;

            public static class Field {
                String field;
                String name;

                public String getField() {
                    return field;
                }

                public void setField(String field) {
                    this.field = field;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public List<Field> getFields() {
                return fields;
            }

            public void setFields(List<Field> fields) {
                this.fields = fields;
            }
        }


        public static class OptionalInfo {
            List<OptionalInfo.Field> fields;

            public static class Field {
                String field;
                String name;

                public String getField() {
                    return field;
                }

                public void setField(String field) {
                    this.field = field;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public List<Field> getFields() {
                return fields;
            }

            public void setFields(List<Field> fields) {
                this.fields = fields;
            }
        }

        public RequiredInfo getRequiredInfo() {
            return requiredInfo;
        }

        public void setRequiredInfo(RequiredInfo requiredInfo) {
            this.requiredInfo = requiredInfo;
        }

        public OptionalInfo getOptionalInfo() {
            return optionalInfo;
        }

        public void setOptionalInfo(OptionalInfo optionalInfo) {
            this.optionalInfo = optionalInfo;
        }
    }


    public Integer getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(Integer percentComplete) {
        this.percentComplete = percentComplete;
    }

    public List<MissingInformation> getMissingInformation() {
        return missingInformation;
    }

    public void setMissingInformation(List<MissingInformation> missingInformation) {
        this.missingInformation = missingInformation;
    }

    public Boolean getAllowSendingApproval() {
        return allowSendingApproval;
    }

    public void setAllowSendingApproval(Boolean allowSendingApproval) {
        this.allowSendingApproval = allowSendingApproval;
    }
}
