package com.lele.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questionDTOS;
    /**
     * 向前按钮
     * 第一页按钮
     */
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;

    private Integer totalPage;
    private Integer currentPage;
    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer page) {

        this.currentPage = page;

        pages.add(page);
        for (int i = 1; i <= 3; i++){
            if (page - i > 0) {
                pages.add(0,page - i);
            }
            if (page + i <= this.totalPage) {
                pages.add(page + i);
            }
        }

        if (page == 1){
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        if (page == this.totalPage){
            showNext = false;
        } else {
            showNext = true;
        }

        if (pages.contains(1)){
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        if (pages.contains(this.totalPage)){
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
