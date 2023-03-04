package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.Subject;
import fpt.project.bsmart.entity.dto.SubjectDTO;
import fpt.project.bsmart.entity.request.subject.SubjectRequest;
import fpt.project.bsmart.service.ISubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final ISubjectService subjectService;

    public SubjectController(ISubjectService subjectService){
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> GetAll(){
        return ResponseEntity.ok(subjectService.FindAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> GetSubjectById(@PathVariable Long id){
        return ResponseEntity.ok(subjectService.FindSubjectById(id));
    }

    @PostMapping
    public ResponseEntity<Long> AddNewSubject(@Valid @RequestBody SubjectRequest subjectRequest){
        return ResponseEntity.ok(subjectService.AddSubject(subjectRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> UpdateSubject(@PathVariable Long id, @Valid @RequestBody SubjectRequest subjectRequest){
        return ResponseEntity.ok(subjectService.UpdateSubject(id, subjectRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> DeleteSubject(@PathVariable Long id){
        subjectService.DeleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}
