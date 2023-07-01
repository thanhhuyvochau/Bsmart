package fpt.project.bsmart.entity.response;


import fpt.project.bsmart.entity.constant.ECourseLevel;
import fpt.project.bsmart.entity.constant.ECourseStatus;
import fpt.project.bsmart.entity.dto.ImageDto;
import fpt.project.bsmart.entity.dto.UserDto;

import java.math.BigDecimal;
import java.time.Instant;


public class SubCourseCartResponse {

    private Long id;
    private ECourseLevel level;

    private String title ;
    private ECourseStatus status;

    private Instant startDateExpected;

    private Instant endDateExpected;

    private BigDecimal price;




    private boolean isChosen = false;
    private UserDto mentor;
    private ImageDto image;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ECourseLevel getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLevel(ECourseLevel level) {
        this.level = level;
    }

    public ECourseStatus getStatus() {
        return status;
    }

    public void setStatus(ECourseStatus status) {
        this.status = status;
    }

    public Instant getStartDateExpected() {
        return startDateExpected;
    }

    public void setStartDateExpected(Instant startDateExpected) {
        this.startDateExpected = startDateExpected;
    }

    public Instant getEndDateExpected() {
        return endDateExpected;
    }

    public void setEndDateExpected(Instant endDateExpected) {
        this.endDateExpected = endDateExpected;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }



    public boolean getIsChosen() {
        return isChosen;
    }

    public void setIsChosen(boolean chosen) {
        isChosen = chosen;
    }

    public UserDto getMentor() {
        return mentor;
    }

    public void setMentor(UserDto mentor) {
        this.mentor = mentor;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }
}
