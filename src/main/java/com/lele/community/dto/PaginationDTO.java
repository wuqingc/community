package com.lele.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questionDTOS;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;

    private Integer totalPage;
    private Integer currentPage;
    private List<Integer> pages = new ArrayList<>();

    public void setPagination(Integer page) {
        this.currentPage = page;

        /*
         * 设置显示的分页按钮列表.
         */
        pages.add(page);
        for (int i = 1; i <= 3; i++){
            if (page - i > 0) {
                pages.add(0,page - i);
            }
            if (page + i <= this.totalPage) {
                pages.add(page + i);
            }
        }

        /*
         * 设置前进后退选项的可见性.
         */
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
