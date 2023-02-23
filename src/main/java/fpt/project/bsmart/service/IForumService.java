package fpt.project.bsmart.service;

import fpt.project.bsmart.entity.common.ApiPage;
import fpt.project.bsmart.entity.common.EForumType;
import fpt.project.bsmart.entity.dto.ForumDto;
import fpt.project.bsmart.entity.dto.SimpleForumDto;
import fpt.project.bsmart.entity.request.UpdateForumRequest;
import fpt.project.bsmart.moodle.response.MoodleSectionResponse;
import org.springframework.data.domain.Pageable;

public interface IForumService {
    ForumDto createForumForClass(Long classId, MoodleSectionResponse moodleSectionResponse);

    ForumDto createForumForSubject(Long subjectId);

    ForumDto updateForum(Long id, UpdateForumRequest request);

    ForumDto getForumByClass(Long classId);

    ForumDto getForumBySubject(Long subjectId,String q);

    ApiPage<SimpleForumDto> getAllForumByTypes(Pageable pageable,EForumType forumType);

    Boolean synchronizeLessonForum(Long classId);

}
