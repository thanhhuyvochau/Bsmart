package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.common.ApiPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;


public class PageUtil {

    public static <T> ApiPage<T> convert(Page<T> page) {
        ApiPage<T> apiPage = new ApiPage<>();
        apiPage.setTotalItems(page.getTotalElements());
        apiPage.setTotalPages(page.getTotalPages());
        apiPage.setCurrentPage(page.getNumber());
        apiPage.setPageSize(page.getSize());
        apiPage.setPageItemSize(page.getNumberOfElements());
        apiPage.setFirst(page.isFirst());
        apiPage.setLast(page.isLast());
        apiPage.setItems(page.getContent());
        return apiPage;
    }

    public static <T> Page<T> toPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        if (start > list.size())
            return new PageImpl<>(new ArrayList<>(), pageable, list.size());
        PageImpl<T> pagingData = new PageImpl<>(list.subList(start, end), pageable, list.size());
        Sort sort = pagingData.getSort();

        return pagingData;
    }
}
