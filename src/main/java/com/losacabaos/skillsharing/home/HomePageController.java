package com.losacabaos.skillsharing.home;

import com.losacabaos.skillsharing.collaboration.CollaborationDao;
import com.losacabaos.skillsharing.login.InternalUser;
import com.losacabaos.skillsharing.login.RoleController;
import com.losacabaos.skillsharing.offer.OfferDao;
import com.losacabaos.skillsharing.request.RequestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
public class HomePageController extends RoleController {

    private CollaborationDao collaborationDao;
    private RequestDao requestDao;

    private OfferDao offerDao;

    @Autowired
    public void setCollaborationDao(CollaborationDao collaborationDao) {
        this.collaborationDao = collaborationDao;
    }

    @Autowired
    public void setRequestDao(RequestDao requestDao) {
        this.requestDao = requestDao;
    }

    @Autowired
    public void setOfferDao(OfferDao offerDao) {
        this.offerDao = offerDao;
    }

    @RequestMapping("list")
    public String listHomePageStudent(HttpSession session, Model model){
        if(session.getAttribute("user") == null){
            model.addAttribute("user", new InternalUser());
            return "redirect:/login";
        }
        InternalUser user = (InternalUser) session.getAttribute("user");
        String username = user.getUsername();

        model.addAttribute("collaborations", collaborationDao.fetchLastThreeCollabs(username));
        model.addAttribute("offers", offerDao.fetchLastThreeOffers(username));
        model.addAttribute("requests", requestDao.fetchLastThreeRequests(username));
        model.addAttribute("student", username);
        return "home/list";
    }


}
