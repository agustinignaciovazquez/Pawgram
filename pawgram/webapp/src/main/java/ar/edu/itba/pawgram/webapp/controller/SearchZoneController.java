package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.interfaces.service.SearchZoneService;
import ar.edu.itba.pawgram.interfaces.service.SecurityUserService;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.SearchZone;
import ar.edu.itba.pawgram.model.User;
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

@RequestMapping("/zone")
@Controller
public class SearchZoneController {
    private static final int PAGE_SIZE = 20;
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchZoneController.class);
    @Autowired
    private SearchZoneService searchZoneService;
    @Autowired
    private SecurityUserService securityUserService;

    @RequestMapping(value={"","/"})
    public ModelAndView index(@ModelAttribute("loggedUser") final User loggedUser) {
        final List<PlainSearchZone> searchZones = searchZoneService.getPlainSearchZonesByUser(loggedUser);

        ModelAndView mav = new ModelAndView("zones");
        mav.addObject("searchZones", searchZones);
        return mav;
    }

    @RequestMapping(value = "/{zone}/category/{category}")
    public ModelAndView showPostForZoneCategory(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                @ModelAttribute("loggedUser") final User loggedUser,
                                                @PathVariable(value = "category") Category category,
                                                @PathVariable(value = "zone") long zoneId) throws ResourceNotFoundException, ForbiddenException {
        LOGGER.debug("Accessed category {} with page {}", category, page);

        final SearchZone searchZone = searchZoneService.getFullSearchZoneByIdAndCategory(zoneId,category,page,PAGE_SIZE);
        if(searchZone == null)
            throw new ResourceNotFoundException();
        if(!loggedUser.equals(searchZone.getUser())){
            LOGGER.warn("User not authorized: {}", loggedUser.getId());
            throw new ForbiddenException();
        }
        if (page < 1 || page > searchZone.getMax_page() && searchZone.getMax_page() > 0) {
            LOGGER.warn("Category page out of bounds: {}", page);
            throw new ResourceNotFoundException();
        }

        ModelAndView mav = new ModelAndView("zone_category");
        mav.addObject("currentCategory", category);
        mav.addObject("categories", Category.values());
        mav.addObject("searchZone", searchZone);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", searchZone.getMax_page());//should remove this
        return mav;
    }

    @RequestMapping(value = "/{zone}")
    public ModelAndView showPostForZone(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                        @ModelAttribute("loggedUser") final User loggedUser,
                                        @PathVariable(value = "zone") long zoneId) throws ResourceNotFoundException, ForbiddenException {
        LOGGER.debug("Accessed zone (all categories) with page {}", page);

        final SearchZone searchZone = searchZoneService.getFullSearchZoneById(zoneId,page,PAGE_SIZE);
        if(searchZone == null)
            throw new ResourceNotFoundException();
        if(!loggedUser.equals(searchZone.getUser())){
            LOGGER.warn("User not authorized: {}", loggedUser.getId());
            throw new ForbiddenException();
        }
        if (page < 1 || page > searchZone.getMax_page() && searchZone.getMax_page() > 0) {
            LOGGER.warn("Page out of bounds: {}", page);
            throw new ResourceNotFoundException();
        }

        ModelAndView mav = new ModelAndView("zone_category");
        mav.addObject("categories", Category.values());
        mav.addObject("searchZone", searchZone);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", searchZone.getMax_page());//should remove this
        return mav;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Category.class,new CaseInsensitiveConverter<>(Category.class));
    }
}
