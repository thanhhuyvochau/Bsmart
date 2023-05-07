package fpt.project.bsmart.entity;

import javax.persistence.*;

@Entity
@Table(name = "class_module")
public class ClassModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "class_section_id")
    private ClassSection classSection;

    public ClassModule(String name, ClassSection classSection) {
        this.name = name;
        this.classSection = classSection;
    }

    public ClassModule() {
    }

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

    public ClassSection getClassSection() {
        return classSection;
    }

    public void setClassSection(ClassSection classSection) {
        this.classSection = classSection;
    }
}
