package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.SearchZoneService;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.SearchZone;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.interfaces.PlainSearchZone;
import ar.edu.itba.pawgram.webapp.exception.ForbiddenException;
import ar.edu.itba.pawgram.webapp.exception.ZoneNotFoundException;
import ar.edu.itba.pawgram.webapp.form.SearchZoneForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/my_zones")
@Controller
public class MySearchZonesController {
    private static final int MAX_SEARCH_ZONES = 3;

    private static final Logger LOGGER = LoggerFactory.getLogger(MySearchZonesController.class);
    @Autowired
    private SearchZoneService searchZoneService;

    @ModelAttribute("searchZoneForm")
    public SearchZoneForm formSearchZone() {
        return new SearchZoneForm();
    }

    @RequestMapping(value={"","/"})
    public ModelAndView showMyZones(@ModelAttribute("loggedUser") final User loggedUser)  {
        LOGGER.debug("Accessed my search zones");

        final List<PlainSearchZone> searchZones = searchZoneService.getPlainSearchZonesByUser(loggedUser);

        ModelAndView mav = new ModelAndView("my_zones");
        mav.addObject("categories", Category.values());
        mav.addObject("maxSearchZones", MAX_SEARCH_ZONES);
        mav.addObject("searchZones", searchZones);
        return mav;
    }

    @RequestMapping("/delete/{zoneId}")
    public ModelAndView deleteMyZone(@PathVariable final long zoneId,
                                      @ModelAttribute("loggedUser") final User loggedUser) throws ZoneNotFoundException, ForbiddenException {
        LOGGER.debug("Accessed delete search zones with id {}",zoneId);

        final SearchZone searchZone = searchZoneService.getFullSearchZonesByIdWithoutPosts(zoneId);
        if (searchZone == null) {
            LOGGER.warn("Failed to delete zone with id {}: zone not found", zoneId);
            throw new ZoneNotFoundException();
        }
        if(!loggedUser.equals(searchZone.getUser())){
            LOGGER.warn("Failed to delete zone with id {}: logged user with id {} is not zone creator with id {}",
                    zoneId, loggedUser.getId(), searchZone.getUser().getId());
            throw new ForbiddenException();
        }

        if (searchZoneService.deleteZoneById(zoneId))
            LOGGER.info("Product with id {} deleted by user with id {}", zoneId, loggedUser.getId());

        return new ModelAndView("redirect:/my_zones/");
    }

    @RequestMapping("/create")
    public ModelAndView createMyZone(@ModelAttribute("loggedUser") final User loggedUser)  {
        LOGGER.debug("Accessed create search zones");

        final List<PlainSearchZone> searchZones = searchZoneService.getPlainSearchZonesByUser(loggedUser);
        if(searchZones.size() >= MAX_SEARCH_ZONES){
            return new ModelAndView("redirect:/my_zones/");
        }

        ModelAndView mav = new ModelAndView("create_zone");
        mav.addObject("categories", Category.values());
        mav.addObject("searchZones", searchZones);
        mav.addObject("userTotalSearchZones",searchZones.size());
        return mav;
    }

    @RequestMapping(value = "/create/process", method = RequestMethod.POST)
    public ModelAndView createMyZone (@ModelAttribute("loggedUser") final User loggedUser,
                                     @Valid @ModelAttribute("searchZoneForm") final SearchZoneForm form,
                                     final BindingResult errors,
                                     final RedirectAttributes attr){

        LOGGER.debug("User with id {} accessed create search zone POST", loggedUser.getId());

        final long userTotalSearchZones = searchZoneService.getTotalSearchZonesByUser(loggedUser);

        final ModelAndView mav = new ModelAndView("redirect:/my_zones/create");

        if (errors.hasErrors() || userTotalSearchZones >= MAX_SEARCH_ZONES) {
            LOGGER.warn("User {} failed to create new search zone: form has errors: {}", loggedUser.getId(), errors.getAllErrors());
            if(userTotalSearchZones >= MAX_SEARCH_ZONES)
                LOGGER.warn("User {} failed to create new search zone: user has achieve maximum number of search zones (current) {} (max) {} ",
                        loggedUser.getId(), userTotalSearchZones, MAX_SEARCH_ZONES);
            setErrorState(form, errors, attr);
            return mav;
        }

        searchZoneService.createSearchZone(form.getLocation(),form.getRangeInMeters(),loggedUser.getId());

        return new ModelAndView("redirect:/my_zones/");
    }

    private void setErrorState(SearchZoneForm form, final BindingResult errors, RedirectAttributes attr) {
        attr.addFlashAttribute("org.springframework.validation.BindingResult.searchZoneForm", errors)
                .addFlashAttribute("searchZoneForm", form);
    }

}
