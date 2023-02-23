package fpt.project.bsmart.entity;


public class VoteNumberReponse {
    private Integer upvoteNumber;
    private Integer downvoteNumber;

    public VoteNumberReponse(Integer upvoteNumber, Integer downvoteNumber) {
        this.upvoteNumber = upvoteNumber;
        this.downvoteNumber = downvoteNumber;
    }

    public VoteNumberReponse() {
    }

    public Integer getUpvoteNumber() {
        return upvoteNumber;
    }

    public void setUpvoteNumber(Integer upvoteNumber) {
        this.upvoteNumber = upvoteNumber;
    }

    public Integer getDownvoteNumber() {
        return downvoteNumber;
    }

    public void setDownvoteNumber(Integer downvoteNumber) {
        this.downvoteNumber = downvoteNumber;
    }
}


