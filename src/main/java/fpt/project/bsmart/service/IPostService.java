package fpt.project.bsmart.service;


import fpt.project.bsmart.entity.common.EPageContent;
import fpt.project.bsmart.entity.request.PageContentRequest;
import fpt.project.bsmart.entity.response.PageContentResponse;

public interface IPostService {


    Long createContentIntroPage(PageContentRequest content);

    PageContentResponse getContentIntroPage(Long id);

    PageContentResponse updateContentIntroPage(Long id, PageContentRequest content);

    PageContentResponse renderIntroPage(EPageContent type );
}