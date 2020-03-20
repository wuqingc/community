package com.lele.community.service;

import com.lele.community.dto.PaginationDTO;
import com.lele.community.dto.QuestionDTO;
import com.lele.community.mapper.QuestionMapper;
import com.lele.community.mapper.UserMapper;
import com.lele.community.model.Question;
import com.lele.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuan
 * 组装功能.
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {


        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        if (totalCount % size == 0){
            paginationDTO.setTotalPage(totalCount / size);
        } else {
            paginationDTO.setTotalPage(totalCount / size + 1);
        }


        int totalPage = paginationDTO.getTotalPage();
        if (page < 1){
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(page);

        /*
         * 算出偏移量.每页有几条数据.
         * 根据每页有几条数据 + 页数 查到想要的记录.
         */
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findId(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setQuestionDTOS(questionDTOS);

        return paginationDTO;
    }
}
