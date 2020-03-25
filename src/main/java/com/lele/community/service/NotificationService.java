package com.lele.community.service;

import com.lele.community.dto.NotificationDTO;
import com.lele.community.dto.PaginationDTO;
import com.lele.community.enums.NotificationStatusEnum;
import com.lele.community.enums.NotificationTypeEnum;
import com.lele.community.exception.CustomizeErrorCode;
import com.lele.community.exception.CustomizeException;
import com.lele.community.mapper.NotificationMapper;
import com.lele.community.mapper.UserMapper;
import com.lele.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO<NotificationDTO> list(Long id, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        /*
         * 设置总页数.
         */
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
        if (totalCount % size == 0 && totalCount != 0){
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
        int offset = size * (page - 1);
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(id);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(
                example,new RowBounds(offset,size));

        if (notifications.isEmpty()) {
            return paginationDTO;
        }
        List<NotificationDTO> list = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            list.add(notificationDTO);
        }
        paginationDTO.setData(list);
        return paginationDTO;
    }

    public Long unReadCount(Long uerId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(uerId)
            .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!notification.getReceiver().equals(user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAILE);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
