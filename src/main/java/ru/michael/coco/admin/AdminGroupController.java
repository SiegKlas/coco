package ru.michael.coco.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.michael.coco.bank.Bank;
import ru.michael.coco.bank.BankMapper;
import ru.michael.coco.bank.BankService;
import ru.michael.coco.bank.tree.*;
import ru.michael.coco.group.Group;
import ru.michael.coco.group.GroupMapper;
import ru.michael.coco.group.GroupService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserMapper;
import ru.michael.coco.user.UserService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/groups")
public class AdminGroupController {
    private final GroupService groupService;
    private final UserService userService;
    private final BankService bankService;
    private final GroupMapper groupMapper;
    private final BankMapper bankMapper;
    private final UserMapper userMapper;
    private final BankStructureService bankStructureService;
    private final BankLevelService bankLevelService;
    private final BankTopicService bankTopicService;
    @Value("${file.groups-dir}")
    private String groupsDir;

    @Autowired
    public AdminGroupController(GroupService groupService, UserService userService, BankService bankService, GroupMapper groupMapper, BankMapper bankMapper, UserMapper userMapper, BankStructureService bankStructureService, BankLevelService bankLevelService, BankTopicService bankTopicService) {
        this.groupService = groupService;
        this.userService = userService;
        this.bankService = bankService;
        this.groupMapper = groupMapper;
        this.bankMapper = bankMapper;
        this.userMapper = userMapper;
        this.bankStructureService = bankStructureService;
        this.bankLevelService = bankLevelService;
        this.bankTopicService = bankTopicService;
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

        BankStructure bankStructure = new BankStructure();
        bankStructure.setBank(bank);
        bankStructureService.saveBankStructure(bankStructure);

        return "redirect:/admin/groups/banks?groupid=" + groupId;
    }

    @PostMapping("/banks/make-active")
    public String makeBankActive(@RequestParam Long groupid, @RequestParam Long bankid) {
        groupService.setActiveBank(groupid, bankid);
        return "redirect:/admin/groups/banks?groupid=" + groupid;
    }

    @PostMapping("/banks/delete")
    public String deleteBank(@RequestParam Long groupid, @RequestParam Long bankid) {
        Group group = groupService.findById(groupid).orElseThrow(() -> new RuntimeException("Group not found"));
        if (group.getActiveBank() != null && group.getActiveBank().getId().equals(bankid)) {
            group.setActiveBank(null);
            groupService.saveGroup(group);
        }

        Bank bank = bankService.findById(bankid).orElseThrow(() -> new RuntimeException("Bank not found"));
        bankStructureService.deleteByBank(bank);
        bankService.deleteById(bankid);

        return "redirect:/admin/groups/banks?groupid=" + groupid;
    }

    @GetMapping("/banks/edit")
    public String editBank(@RequestParam Long bankid, Model model) {
        Bank bank = bankService.findById(bankid).orElseThrow(() -> new RuntimeException("Bank not found"));
        BankStructure bankStructure = bankStructureService.findByBank(bank).orElseThrow(() -> new RuntimeException("Bank structure not found"));
        model.addAttribute("bankId", bankid);
        model.addAttribute("bankStructureId", bankStructure.getId());
        model.addAttribute("topics", bankStructure.getTopics());
        return "admin/edit-bank";
    }

    @PostMapping("/banks/generate")
    @ResponseBody
    public ResponseEntity<String> generateBank(@RequestParam Long bankid) {
        Bank bank = bankService.findById(bankid).orElseThrow(() -> new RuntimeException("Bank not found"));
        Long groupId = bank.getGroup().getId();
        String bankDir = groupsDir + "\\" + groupId + "\\" + bank.getName();

        File bankDirFile = new File(bankDir);
        if (!bankDirFile.exists()) {
            bankDirFile.mkdirs();
        }

        generateBDFFile(bank, bankDir);
        sendBankToPythonServer(bankDir);

        return ResponseEntity.ok("Bank generated successfully");
    }

    @PostMapping("/topics/create")
    public String createTopic(@RequestParam Long bankStructureId, @RequestParam String name) {
        BankStructure bankStructure = bankStructureService.findById(bankStructureId).orElseThrow(() -> new RuntimeException("Bank structure not found"));
        BankTopic bankTopic = new BankTopic();
        bankTopic.setBankStructure(bankStructure);
        bankTopic.setName(name);
        bankTopicService.saveBankTopic(bankTopic);

        return "redirect:/admin/groups/banks/edit?bankid=" + bankStructure.getBank().getId();
    }

    @GetMapping("/topics/edit")
    public String editTopic(@RequestParam Long topicId, Model model) {
        BankTopic topic = bankTopicService.findById(topicId).orElseThrow(() -> new RuntimeException("Topic not found"));

        // Получение списка директорий как строки
        File baseDir = new File("D:\\diploma\\robot\\exfiles");
        File[] directories = baseDir.listFiles(File::isDirectory);
        List<String> directoryNames = Arrays.stream(directories)
                .map(File::getName)
                .collect(Collectors.toList());

        model.addAttribute("topic", topic);
        model.addAttribute("levels", topic.getLevels());
        model.addAttribute("topicId", topic.getId());
        model.addAttribute("directories", directoryNames);  // Передаем список директорий как строки

        return "admin/edit-topic";
    }

    @PostMapping("/levels/add")
    public String addLevel(@RequestParam Long topicid) {
        BankTopic topic = bankTopicService.findById(topicid).orElseThrow(() -> new RuntimeException("Topic not found"));
        BankLevel bankLevel = new BankLevel();
        bankLevel.setBankTopic(topic);
        bankLevelService.saveBankLevel(bankLevel);

        return "redirect:/admin/groups/topics/edit?topicId=" + topicid;
    }

    @PostMapping("/levels/edit")
    public String editLevel(@RequestParam Long levelId, @RequestParam String exFiles) {
        BankLevel level = bankLevelService.findById(levelId).orElseThrow(() -> new RuntimeException("Level not found"));
        level.setExFiles(new ArrayList<>(Arrays.asList(exFiles.split("\\n")))); // Создаем новый изменяемый список
        bankLevelService.saveBankLevel(level);

        return "redirect:/admin/groups/topics/edit?topicId=" + level.getBankTopic().getId();
    }

    @GetMapping("/levels/{id}")
    @ResponseBody
    public ResponseEntity<BankLevel> getLevel(@PathVariable Long id) {
        BankLevel level = bankLevelService.findById(id).orElseThrow(() -> new RuntimeException("Level not found"));
        return ResponseEntity.ok(level);
    }

    private void generateBDFFile(Bank bank, String bankDir) {
        File bdfFile = new File(bankDir, bank.getName() + ".bdf");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bdfFile))) {
            writer.write(bank.getName());
            writer.newLine();
            writer.newLine();
            BankStructure bankStructure = bankStructureService.findByBank(bank).orElseThrow(() -> new RuntimeException("Bank Structure not found"));
            for (BankTopic topic : bankStructure.getTopics()) {
                writer.write("::" + topic.getName());
                writer.newLine();
                for (int i = 0; i < topic.getLevels().size(); i++) {
                    BankLevel level = topic.getLevels().get(i);
                    List<String> exFilePaths = level.getExFiles().stream()
                            .map(this::getRelativePath)
                            .collect(Collectors.toList());
                    writer.write(String.join(",", exFilePaths));
                    if (i < topic.getLevels().size() - 1) {
                        writer.write(";");
                    }
                }
                writer.newLine();
                writer.newLine();
            }
            writer.write("::");
        } catch (IOException e) {
            throw new RuntimeException("Error generating BDF file", e);
        }
    }

    private String getRelativePath(String filePath) {
        return filePath.replace("\\", "/").trim();
    }

    private void sendBankToPythonServer(String bankDir) {
        try {
            URL url = new URL("http://python-server-address/api/bank");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{\"bankDir\": \"" + bankDir + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = connection.getResponseCode();
            System.out.println("Response Code : " + code);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
