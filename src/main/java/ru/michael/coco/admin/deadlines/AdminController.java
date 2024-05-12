package ru.michael.coco.admin.deadlines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.michael.coco.chat.Chat;
import ru.michael.coco.chat.ChatService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    // Отображение главной страницы администратора с обзором всех чатов
    @GetMapping
    public String getAdminPage(Model model) {
        List<Chat> chats = chatService.getAllChats();
        model.addAttribute("chats", chats);
        return "admin/main";
    }

    // Отображение страницы с уведомлениями о непрочитанных сообщениях
    @GetMapping("/notifications")
    public String getNotificationsPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User admin = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        List<Chat> chats = chatService.getAllChatsWithUnreadMessages(admin);
        model.addAttribute("chats", chats);
        return "admin/notifications";
    }
}
