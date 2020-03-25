package com.lele.community.controller;

import com.lele.community.dto.NotificationDTO;
import com.lele.community.enums.NotificationTypeEnum;
import com.lele.community.model.Notification;
import com.lele.community.model.User;
import com.lele.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String notification(HttpServletRequest request,
                               @PathVariable(name = "id") Long id){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id,user);

        if (notificationDTO.getType() == NotificationTypeEnum.RERPLY_COMMENT.getType()
            || notificationDTO.getType() == NotificationTypeEnum.REPLY_QUESTION.getType()) {

            return "redirect:/question/" + notificationDTO.getOuterId();
        }
        return "redirect:/";
    }
}
