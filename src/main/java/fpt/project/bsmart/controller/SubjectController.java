package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.request.subject.SubjectRequest;
import fpt.project.bsmart.service.ISubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final ISubjectService subjectService;

    public SubjectController(ISubjectService subjectService){
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<Subject>> GetAll(){
        return ResponseEntity.ok(subjectService.FindAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Subject> GetSubjectById(@PathVariable @NotNull @Min(0) Long id){
        return ResponseEntity.ok(subjectService.FindSubjectById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Subject> GetSubjectByName(@PathVariable @NotNull @NotBlank String name){
        return ResponseEntity.ok(subjectService.FindSubjectByName(name));
    }

    @PostMapping
    public ResponseEntity<Long> AddNewSubject(@Valid @RequestBody SubjectRequest subjectRequest){
        return ResponseEntity.ok(subjectService.AddSubject(subjectRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> UpdateSubject(@PathVariable @NotNull @Min(0) Long id, @Valid @RequestBody SubjectRequest subjectRequest){
        return ResponseEntity.ok(subjectService.UpdateSubject(id, subjectRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeleteSubject(@PathVariable @NotNull @Min(0) Long id){
        subjectService.DeleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}
