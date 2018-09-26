package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.interfaces.service.SearchZoneService;
import ar.edu.itba.pawgram.interfaces.service.SecurityUserService;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.SearchZone;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import ar.edu.itba.pawgram.model.interfaces.PlainSearchZone;
import ar.edu.itba.pawgram.webapp.exception.ForbiddenException;
import ar.edu.itba.pawgram.webapp.exception.ResourceNotFoundException;
import ar.edu.itba.pawgram.webapp.util.CaseInsensitiveConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequestMapping("/zones")
@Controller
public class SearchZonesController {
    private static final int PAGE_SIZE = 6;
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchZonesController.class);
    @Autowired
    private SearchZoneService searchZoneService;
    @Autowired
    private PostService postService;
    @RequestMapping(value = "/category/{category}")
    public ModelAndView showZonesByCategory(@ModelAttribute("loggedUser") final User loggedUser,
                                                @PathVariable(value = "category") Category category)  {
        LOGGER.debug("Accessed all zones category {} ", category);

        final List<SearchZone> searchZones = searchZoneService.getFullSearchZonesByIdAndCategory(loggedUser, category,1,PAGE_SIZE);
        final List<PlainPost> userPosts = postService.getPlainPostsByUserIdPaged(loggedUser.getId(),category,1,PAGE_SIZE);

        ModelAndView mav = new ModelAndView("zone_all");
        mav.addObject("currentCategory", category);
        mav.addObject("categories", Category.values());
        mav.addObject("searchZones", searchZones);
        mav.addObject("userPosts", userPosts);
        return mav;
    }

    @RequestMapping(value = "/")
    public ModelAndView showZones(@ModelAttribute("loggedUser") final User loggedUser)  {
        LOGGER.debug("Accessed all zones");

        final List<SearchZone> searchZones = searchZoneService.getFullSearchZonesById(loggedUser,1,PAGE_SIZE);
        final List<PlainPost> userPosts = postService.getPlainPostsByUserIdPaged(loggedUser.getId(),1,PAGE_SIZE);

        ModelAndView mav = new ModelAndView("zone_all");
        mav.addObject("categories", Category.values());
        mav.addObject("searchZones", searchZones);
        mav.addObject("userPosts", userPosts);
        return mav;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Category.class,new CaseInsensitiveConverter<>(Category.class));
    }
}
