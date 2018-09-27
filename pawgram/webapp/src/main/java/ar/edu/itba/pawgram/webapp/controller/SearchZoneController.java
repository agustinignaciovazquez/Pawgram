package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.SearchZoneService;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.SearchZone;
import ar.edu.itba.pawgram.model.User;
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

@RequestMapping("/zone")
@Controller
public class SearchZoneController {
    private static final int PAGE_SIZE = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchZoneController.class);
    @Autowired
    private SearchZoneService searchZoneService;

    @RequestMapping(value={"","/"})
    public ModelAndView index() {
        return new ModelAndView("redirect:/zones/");
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
        LOGGER.debug("Quantity of post {} in category {} with page {} ", searchZone.getPosts().size(),category,page);

        ModelAndView mav = new ModelAndView("zone_detail");
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

        LOGGER.debug("Quantity of post {} in all categories with page {} ", searchZone.getPosts().size(),page);

        ModelAndView mav = new ModelAndView("zone_detail");
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
