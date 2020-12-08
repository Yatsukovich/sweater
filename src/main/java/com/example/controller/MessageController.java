package com.example.controller;

import com.example.domain.Message;
import com.example.domain.User;
import com.example.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Controller
public class MessageController {
    @Autowired
    private MessageRepo messageRepo;
    @Value("${upload.path}")
    private String uploadPath;
    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
                               @PathVariable User user,
                               Model model,
            @RequestParam(required = false) Message message
    ) {
         Set<Message> messages = user.getMessages();
         model.addAttribute("userChannel", user);
         model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
         model.addAttribute("subscribersCount", user.getSubscribers().size());
         model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
         model.addAttribute("messages", messages);
         model.addAttribute("message", message);
         model.addAttribute("isCurrentUser", currentUser.equals(user));
        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String editMessage(
            @AuthenticationPrincipal User currentUser,
                              @PathVariable Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (message.getAuthor().equals(currentUser)){
            if(!StringUtils.isEmpty(text)){
             message.setText(text);
            }
            if(!StringUtils.isEmpty(tag)){
                message.setTag(tag);
            }
            saveFile(message,file);
         messageRepo.save(message);

        }

        return "redirect:/user-messages/"+user;
    }
    private void saveFile(Message message, MultipartFile file) throws IOException {
        if (file !=null && !file.getOriginalFilename().isEmpty()){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile+"."+ file.getOriginalFilename();
            file.transferTo(new File(uploadPath+"/"+resultFileName));
            message.setFilename(resultFileName);
        }
    }
}
