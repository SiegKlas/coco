package ru.michael.coco.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.michael.coco.bank.Bank;
import ru.michael.coco.bank.BankService;
import ru.michael.coco.group.Group;
import ru.michael.coco.group.GroupService;

import java.util.ArrayList;

@Controller
@RequestMapping("/admin/banks")
public class AdminBankController {
    private final BankService bankService;
    private final GroupService groupService;

    @Autowired
    public AdminBankController(BankService bankService, GroupService groupService) {
        this.bankService = bankService;
        this.groupService = groupService;
    }

    @GetMapping("/create")
    public String createBankPage(Model model) {
        model.addAttribute("groups", groupService.findAllGroups());
        return "admin/createBank";
    }

    @PostMapping("/create")
    public String createBank(@RequestParam String groupName, @RequestParam String bankName) {
        Group group = groupService.findByName(groupName).orElseThrow();
        Bank bank = new Bank(group);
        bank.setName(bankName);
        bank.setTopicDescriptions(new ArrayList<>());
        bankService.saveBank(bank);
        return "redirect:/admin/banks";
    }

    @GetMapping("/edit")
    public String editBankPage(@RequestParam Long bankId, Model model) {
        Bank bank = bankService.findById(bankId).orElseThrow();
        model.addAttribute("bank", bank);
        return "admin/editBank";
    }

    @PostMapping("/update")
    public String updateBank(@RequestParam Long bankId, @RequestParam String bankName) {
        Bank bank = bankService.findById(bankId).orElseThrow();
        bank.setName(bankName);
        bankService.saveBank(bank);
        return "redirect:/admin/banks";
    }

    @PostMapping("/delete")
    public String deleteBank(@RequestParam Long bankId) {
        bankService.deleteById(bankId);
        return "redirect:/admin/banks";
    }
}

