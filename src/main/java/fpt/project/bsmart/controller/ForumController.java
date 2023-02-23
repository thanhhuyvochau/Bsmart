package fpt.project.bsmart.controller;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.ApiResponse;
import fpt.project.bsmart.entity.common.EForumType;
import fpt.project.bsmart.entity.dto.ForumDto;
import fpt.project.bsmart.entity.dto.SimpleForumDto;
import fpt.project.bsmart.entity.request.UpdateForumRequest;
import fpt.project.bsmart.repository.SubjectRepository;
import fpt.project.bsmart.service.IForumService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forums")
public class ForumController {
    private final IForumService forumService;
    private final SubjectRepository subjectRepository;

    public ForumController(IForumService forumService, SubjectRepository subjectRepository) {
        this.forumService = forumService;
        this.subjectRepository = subjectRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ApiPage<SimpleForumDto>>> getAllForum(Pageable pageable, @RequestParam EForumType forumType) {
        return ResponseEntity.ok(ApiResponse.success(forumService.getAllForumByTypes(pageable, forumType)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ForumDto>> updateForum(@PathVariable Long id, @RequestBody UpdateForumRequest request) {
        return ResponseEntity.ok(ApiResponse.success(forumService.updateForum(id, request)));
    }


    /**
     * Temporary API will be deleted in future
     */
    @GetMapping("/synchronize-lesson")
    public ResponseEntity<ApiResponse<Boolean>> synchronizeLessonForum(@RequestParam Long classId) {
        return ResponseEntity.ok(ApiResponse.success(forumService.synchronizeLessonForum(classId)));
    }


}
