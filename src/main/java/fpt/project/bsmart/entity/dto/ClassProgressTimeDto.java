package fpt.project.bsmart.entity.dto;

public class ClassProgressTimeDto {
    private Integer currentSlot;
    private Double percentage;

    public ClassProgressTimeDto(Integer currentSlot, Double percentage) {
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

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
