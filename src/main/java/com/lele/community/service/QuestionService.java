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
 * 当一个功能写在Controller中较为复杂时,就应当将其抽象出来.
 * 内部功能就是Service,外部接口应该使用Provider.
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 查找问题列表
     * @param page
     * @param size
     * @return 当前页面的数据,将其封装在一个DTO中.
     */
    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        /*
         * 设置总页数.
         */
        Integer totalCount = questionMapper.count();
        if (totalCount % size == 0){
            paginationDTO.setTotalPage(totalCount / size);
        } else {
            paginationDTO.setTotalPage(totalCount / size + 1);
        }

        /*
         * 当页数超出限制时采取的方式.
         */
        int totalPage = paginationDTO.getTotalPage();
        if (page < 1){
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        paginationDTO.setPagination(page);

        /*
         * 具体的查询操作:
         * 算出偏移量,然后与页数一起作为参数查找,返回符合条件的问题列表.
         */
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.list(offset,size);

        /*
         * 封装之后返回当前页对象.
         */
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
