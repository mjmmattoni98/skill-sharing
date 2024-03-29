package com.losacabaos.skillsharing.request;

import com.losacabaos.skillsharing.exceptions.SkillSharingException;
import com.losacabaos.skillsharing.login.InternalUser;
import com.losacabaos.skillsharing.login.RoleController;
import com.losacabaos.skillsharing.skill.SkillDao;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/request")
public class RequestController extends RoleController {
    private RequestDao requestDao;
    private SkillDao skillDao;
    private static final RequestValidator validator = new RequestValidator();
    private static final RequestUpdateValidator updateValidator = new RequestUpdateValidator();

    @Autowired
    public void setRequestDao(RequestDao requestDao) {
        this.requestDao = requestDao;
    }

    @Autowired
    public void setSkillDao(SkillDao skillDao) {
        this.skillDao = skillDao;
    }

    @RequestMapping("/paged_list")
    public String listRequestsPaged(Model model, @RequestParam("page") Optional<Integer> page) {
        model.addAttribute("request_filter", new RequestFilter());
        HashMap<String, String> skill = new HashMap<>();
        return getRequestsPaged(model, page.orElse(0), "", "", skill, "");
    }

    @PostMapping("/paged_list/username")
    public String postListRequestsPagedByName(Model model, @ModelAttribute("request_filter") RequestFilter requestFilter) {
        model.addAttribute("request_filter", requestFilter);
        HashMap<String, String> skill = new HashMap<>();
        return getRequestsPaged(model, 0, requestFilter.getUsername(), "", skill, "");
    }

    @GetMapping("/paged_list/username")
    public String getListRequestsPagedByName(Model model, @RequestParam("username") Optional<String> username,
                                             @RequestParam("page") Optional<Integer> page) {
        RequestFilter requestFilter = new RequestFilter();
        requestFilter.setUsername(username.orElse(""));
        model.addAttribute("request_filter", requestFilter);
        HashMap<String, String> skill = new HashMap<>();
        return getRequestsPaged(model, page.orElse(0), requestFilter.getUsername(), "", skill, "");
    }

    @RequestMapping("/paged_list/student")
    public String listRequestsStudentPaged(HttpSession session, Model model, @RequestParam("page") Optional<Integer> page) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("user", new InternalUser());
            return "login";
        }
        InternalUser user = (InternalUser) session.getAttribute("user");

        model.addAttribute("student", user.getUsername());
        model.addAttribute("request_filter", new RequestFilter());
        HashMap<String, String> skill = new HashMap<>();
        return getRequestsPaged(model, page.orElse(0), "", "", skill, user.getUsername());
    }

    @PostMapping("/paged_list/student/skill")
    public String postListRequestsStudentPagedByName(HttpSession session, Model model, @ModelAttribute("request_filter") RequestFilter requestFilter) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("user", new InternalUser());
            return "login";
        }
        InternalUser user = (InternalUser) session.getAttribute("user");

        model.addAttribute("student", user.getUsername());
        model.addAttribute("request_filter", requestFilter);
        HashMap<String, String> skill = new HashMap<>();
        return getRequestsPaged(model, 0, "", requestFilter.getSkill(), skill, user.getUsername());
    }

    @GetMapping("/paged_list/student/skill")
    public String getListRequestsStudentPagedByName(HttpSession session, Model model, @RequestParam("name") Optional<String> name,
                                                    @RequestParam("page") Optional<Integer> page) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("user", new InternalUser());
            return "login";
        }
        InternalUser user = (InternalUser) session.getAttribute("user");

        model.addAttribute("student", user.getUsername());
        RequestFilter requestFilter = new RequestFilter();
        requestFilter.setSkill(name.orElse(""));
        model.addAttribute("request_filter", requestFilter);
        HashMap<String, String> skill = new HashMap<>();
        return getRequestsPaged(model, page.orElse(0), "", requestFilter.getSkill(), skill, user.getUsername());
    }

    @RequestMapping("/paged_list/skill/{name}/{level}")
    public String listRequestsSkillPaged(Model model, @PathVariable String name, @PathVariable String level, @RequestParam("page") Optional<Integer> page) {
        model.addAttribute("skill", name);
        model.addAttribute("level", level);
        model.addAttribute("request_filter", new RequestFilter());
        HashMap<String, String> skill = new HashMap<>();
        skill.put("name", name);
        skill.put("level", level);
        return getRequestsPaged(model, page.orElse(0), "", "", skill, "");
    }

    @PostMapping("/paged_list/skill/{name}/{level}/username")
    public String postListRequestsSkillPagedByName(@PathVariable String name, @PathVariable String level, Model model,
                                                   @ModelAttribute("request_filter") RequestFilter requestFilter) {
        model.addAttribute("skill", name);
        model.addAttribute("level", level);
        model.addAttribute("request_filter", requestFilter);
        HashMap<String, String> skill = new HashMap<>();
        skill.put("name", name);
        skill.put("level", level);
        return getRequestsPaged(model, 0, requestFilter.getUsername(), "", skill, "");
    }

    @GetMapping("/paged_list/skill/{name}/{level}/username")
    public String getListRequestsSkillPagedByName(Model model, @PathVariable String name, @PathVariable String level,
                                                  @RequestParam("username") Optional<String> username,
                                                  @RequestParam("page") Optional<Integer> page) {
        model.addAttribute("skill", name);
        model.addAttribute("level", level);
        RequestFilter requestFilter = new RequestFilter();
        requestFilter.setUsername(username.orElse(""));
        model.addAttribute("request_filter", requestFilter);
        HashMap<String, String> skill = new HashMap<>();
        skill.put("name", name);
        skill.put("level", level);
        return getRequestsPaged(model, page.orElse(0), requestFilter.getUsername(), "", skill, "");
    }

    @NotNull
    private String getRequestsPaged(Model model, int page, String username, String name, Map<String, String> skill, String student) {
        List<Request> requests = new LinkedList<>();
        if (skill.isEmpty() && student.isEmpty()) {
            model.addAttribute("username", username);
            if (username.equals("")) {
                requests = requestDao.getRequests();
            } else {
                requests = requestDao.getRequestsByUsername(username);
            }
        }
        if (!skill.isEmpty()) {
            model.addAttribute("username", username);
            if (username.equals("")) {
                requests = requestDao.getRequestsSkill(skill.get("name"), skill.get("level"));
            } else {
                requests = requestDao.getRequestsSkillByUsername(skill.get("name"), skill.get("level"), username);
            }
        }
        if (!student.isEmpty()) {
            model.addAttribute("name", name);
            if (name.equals("")) {
                requests = requestDao.getRequestsStudent(student);
            } else {
                requests = requestDao.getRequestsStudentBySkill(student, name);
            }
        }
        Collections.sort(requests);

        List<List<Request>> requestsPaged = new ArrayList<>();
        int start = 0;
        int pageLength = 8;
        int end = pageLength;
        while (end < requests.size()) {
            requestsPaged.add(new ArrayList<>(requests.subList(start, end)));
            start += pageLength;
            end += pageLength;
        }
        requestsPaged.add(new ArrayList<>(requests.subList(start, requests.size())));
        model.addAttribute("requests_paged", requestsPaged);

        int totalPages = requestsPaged.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumbers);
        }

        model.addAttribute("selected_page", page);

        return "request/paged_list";
    }

    @RequestMapping(value = "/add/{name}/{level}")
    public String addRequestSkill(HttpSession session, Model model, @PathVariable String name, @PathVariable String level) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("user", new InternalUser());
            return "login";
        }
        InternalUser user = (InternalUser) session.getAttribute("user");

        Request request = new Request();
        request.setFromSkill(true);
        request.setUsername(user.getUsername());
        request.setName(name);
        request.setLevel(level);
        model.addAttribute("request", request);
        return "request/add";
    }

    @RequestMapping(value = "/add")
    public String addRequest(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("user", new InternalUser());
            return "login";
        }
        InternalUser user = (InternalUser) session.getAttribute("user");

        Request request = new Request();
        request.setFromSkill(false);
        request.setUsername(user.getUsername());
        model.addAttribute("request", request);
        model.addAttribute("skills", skillDao.getAvailableSkills());
        return "request/add";
    }

    @PostMapping(value = "/add")
    public String processAddRequest(@ModelAttribute("request") Request request, Model model,
                                    BindingResult bindingResult) {
        validator.validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            if (!request.isFromSkill())
                model.addAttribute("skills", skillDao.getAvailableSkills());
            return "request/add";
        }
        try {
            requestDao.addRequest(request);
        } catch (DataAccessException e) {
            throw new SkillSharingException("Error accessing the database\n" + e.getMessage(),
                    "ErrorAccessingDatabase", "/");
        }
        return "redirect:paged_list/student";
    }

    @GetMapping(value = "/update/{id}")
    public String updateRequest(HttpSession session, Model model, @PathVariable int id) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("user", new InternalUser());
            return "login";
        }
        InternalUser user = (InternalUser) session.getAttribute("user");

        Request request = requestDao.getRequest(id);
        if (!request.getUsername().equals(user.getUsername()))
            throw new SkillSharingException("You are not allowed to update this request", "NotAllowed", "/");
        model.addAttribute("request", request);
        return "request/update";
    }

    @PostMapping(value = "/update")
    public String processUpdateSubmit(@ModelAttribute("request") Request request,
                                      BindingResult bindingResult) {
        updateValidator.validate(request, bindingResult);
        if (bindingResult.hasErrors()) return "request/update";
        requestDao.updateRequest(request);
        return "redirect:paged_list/student";
    }

    @RequestMapping(value = "/cancel/{id}")
    public String processCancelRequest(HttpSession session, Model model, @PathVariable int id) {
        if (session.getAttribute("user") == null) {
            model.addAttribute("user", new InternalUser());
            return "login";
        }
        InternalUser user = (InternalUser) session.getAttribute("user");

        Request request = requestDao.getRequest(id);
        if (!request.getUsername().equals(user.getUsername()))
            throw new SkillSharingException("You are not allowed to cancel this request", "NotAllowed", "/");
        request.setCanceled(true);
        requestDao.updateRequest(request);
        return "redirect:../paged_list/student";
    }

}
