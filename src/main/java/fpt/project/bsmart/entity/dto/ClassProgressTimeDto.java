package fpt.project.bsmart.entity.dto;

public class ClassProgressTimeDto {
    private Integer currentSlot;
    private Integer percentage;

    public ClassProgressTimeDto(Integer currentSlot, Integer percentage) {
        this.currentSlot = currentSlot;
        this.percentage = percentage;
    }

    public ClassProgressTimeDto() {
    }

    public Integer getCurrentSlot() {
        return currentSlot;
    }

    public void setCurrentSlot(Integer currentSlot) {
        this.currentSlot = currentSlot;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
}
