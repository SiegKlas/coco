package ru.michael.coco.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.michael.coco.bank.Bank;
import ru.michael.coco.bank.BankDTO;
import ru.michael.coco.bank.BankMapper;
import ru.michael.coco.bank.BankService;
import ru.michael.coco.group.Group;
import ru.michael.coco.group.GroupMapper;
import ru.michael.coco.group.GroupService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserMapper;
import ru.michael.coco.user.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin/groups")
public class AdminGroupController {
    private final GroupService groupService;
    private final UserService userService;
    private final BankService bankService;
    private final GroupMapper groupMapper;
    private final BankMapper bankMapper;
    private final UserMapper userMapper;

    @Autowired
    public AdminGroupController(GroupService groupService, UserService userService, BankService bankService, GroupMapper groupMapper, BankMapper bankMapper, UserMapper userMapper) {
        this.groupService = groupService;
        this.userService = userService;
        this.bankService = bankService;
        this.groupMapper = groupMapper;
        this.bankMapper = bankMapper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String getAdminGroupsPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User admin = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        List<Group> groups = groupService.findGroupsByTeacher(admin);
        model.addAttribute("groups", groups);
        return "admin/groups";
    }

    @GetMapping("/view")
    public String viewGroupStudents(@RequestParam("groupid") Long groupId, Model model) {
        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        List<User> students = group.getStudents();
        model.addAttribute("students", students);
        return "admin/view-group-students";
    }

    @GetMapping("/banks")
    public String getBanksPage(@RequestParam("groupid") Long groupId, Model model) {
        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        List<Bank> banks = group.getBanks();
        model.addAttribute("groupId", groupId);
        model.addAttribute("banks", banks);
        model.addAttribute("activeBank", group.getActiveBank());
        return "admin/banks";
    }

    @PostMapping("/banks/create")
    public String createBank(@RequestParam("groupid") Long groupId, @RequestParam("name") String name) {
        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        Bank bank = new Bank(group);
        bank.setName(name);
        bankService.saveBank(bank);
        return "redirect:/admin/groups/banks?groupid=" + groupId;
    }

    @PostMapping("/banks/make-active")
    public String makeBankActive(@RequestParam("groupid") Long groupId, @RequestParam("bankid") Long bankId) {
        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        Bank bank = bankService.findById(bankId).orElseThrow(() -> new RuntimeException("Bank not found"));
        group.setActiveBank(bank);
        groupService.saveGroup(group);
        return "redirect:/admin/groups/banks?groupid=" + groupId;
    }

    @GetMapping("/banks/edit")
    public String editBank(@RequestParam("groupid") Long groupId, @RequestParam("bankid") Long bankId, Model model) {
        Bank bank = bankService.findById(bankId).orElseThrow(() -> new RuntimeException("Bank not found"));
        model.addAttribute("bank", bankMapper.toDTO(bank));
        model.addAttribute("groupId", groupId);
        return "admin/edit-bank";
    }

    @PostMapping("/banks/update")
    public String updateBank(@ModelAttribute BankDTO bankDTO) {
        Bank bank = bankService.findById(bankDTO.getId()).orElseThrow(() -> new RuntimeException("Bank not found"));
        bank.setName(bankDTO.getName());
        bankService.saveBank(bank);
        return "redirect:/admin/groups/banks?groupid=" + bankDTO.getGroupId();
    }
}
