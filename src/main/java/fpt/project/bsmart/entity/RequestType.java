package fpt.project.bsmart.entity;


import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "request_type")
public class RequestType {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name ;

    @OneToMany(mappedBy = "requestType")
    private List<Request> requests;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

}



