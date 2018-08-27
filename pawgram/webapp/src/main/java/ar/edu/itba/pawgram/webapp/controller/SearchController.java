package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.interfaces.service.SecurityUserService;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Location;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.exception.InvalidQueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@RequestMapping("/search")
@Controller
public class SearchController {
    private static final int MIN_QUERY = 3;
    private static final int MAX_QUERY = 64;
    private static final int PAGE_SIZE = 20;

    @Autowired
    private PostService postService;
    @Autowired
    private SecurityUserService securityUserService;

    @RequestMapping(value = {"", "/"})
    public ModelAndView index() {
        final User user = securityUserService.getLoggedInUser();
        ModelAndView mav = new ModelAndView("post");
        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping("/all")
    public ModelAndView searchResults(@RequestParam(value = "query") String query,
                                      @RequestParam(value = "latitude", required = false, defaultValue = "0") double latitude,
                                      @RequestParam(value = "longitude", required = false, defaultValue = "0") double longitude) throws InvalidQueryException {

        if (query == null || query.length() < MIN_QUERY) {
            throw new InvalidQueryException();
        }
        else if (query.length() > MAX_QUERY) {
            throw new InvalidQueryException();
        }
        final User user = securityUserService.getLoggedInUser();

        final ModelAndView mav = new ModelAndView("search-results");
        mav.addObject("posts", postService.getPlainPostsByKeyword(query, new Location(longitude,latitude)));
        mav.addObject("max_page",postService.getMaxPageByKeyword(PAGE_SIZE,query));
        mav.addObject("user", user);
        mav.addObject("categories", Category.values());
        mav.addObject("queryText", query);
        return mav;
    }

    @RequestMapping("/category/{category}")
    public ModelAndView searchResultsByCategory(@RequestParam(value = "query") String query,@PathVariable(value = "category") Category category,
                                      @RequestParam(value = "latitude", required = false, defaultValue = "0") double latitude,
                                      @RequestParam(value = "longitude", required = false, defaultValue = "0") double longitude) throws InvalidQueryException {

        if (query == null || query.length() < MIN_QUERY) {
            throw new InvalidQueryException();
        }
        else if (query.length() > MAX_QUERY) {
            throw new InvalidQueryException();
        }
        final User user = securityUserService.getLoggedInUser();

        final ModelAndView mav = new ModelAndView("search-results");
        mav.addObject("posts", postService.getPlainPostsByKeyword(query, new Location(longitude,latitude),category));
        mav.addObject("max_page",postService.getMaxPageByKeyword(PAGE_SIZE,query,category));
        mav.addObject("user", user);
        mav.addObject("currentCategory", category);
        mav.addObject("categories", Category.values());
        mav.addObject("queryText", query);
        return mav;
    }
}
