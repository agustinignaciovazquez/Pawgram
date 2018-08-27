package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.interfaces.service.SecurityUserService;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Location;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import ar.edu.itba.pawgram.webapp.exception.InvalidQueryException;
import ar.edu.itba.pawgram.webapp.exception.PostNotFoundException;
import ar.edu.itba.pawgram.webapp.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;


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
                                      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                      @RequestParam(value = "latitude", required = false) final Optional<Double> latitude,
                                      @RequestParam(value = "longitude", required = false) final Optional<Double> longitude) throws InvalidQueryException, ResourceNotFoundException {

        if (query == null || query.length() < MIN_QUERY) {
            throw new InvalidQueryException();
        } else if (query.length() > MAX_QUERY) {
            throw new InvalidQueryException();
        }

        final User user = securityUserService.getLoggedInUser();
        final List<PlainPost> posts;
        final long max_page = postService.getMaxPageByKeyword(PAGE_SIZE,query);
        if (page < 1 || page > max_page && max_page > 0) {
            throw new ResourceNotFoundException();
        }

        if(longitude.isPresent() && latitude.isPresent()) {
            posts = postService.getPlainPostsByKeywordPaged(query, new Location(longitude.get(),latitude.get()),page,PAGE_SIZE);
        }else{
            posts = postService.getPlainPostsByKeywordPaged(query,page,PAGE_SIZE);
        }

        final ModelAndView mav = new ModelAndView("search-results");
        mav.addObject("posts", posts);
        mav.addObject("max_page",max_page);
        mav.addObject("user", user);
        mav.addObject("categories", Category.values());
        mav.addObject("queryText", query);
        return mav;
    }

    @RequestMapping("/category/{category}")
    public ModelAndView searchResultsByCategory(@RequestParam(value = "query") String query,
                                                @PathVariable(value = "category") Category category,
                                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                @RequestParam(value = "latitude", required = false) final Optional<Double> latitude,
                                                @RequestParam(value = "longitude", required = false) final Optional<Double> longitude) throws InvalidQueryException, ResourceNotFoundException {

        if (query == null || query.length() < MIN_QUERY) {
            throw new InvalidQueryException();
        } else if (query.length() > MAX_QUERY) {
            throw new InvalidQueryException();
        }

        final User user = securityUserService.getLoggedInUser();
        final List<PlainPost> posts;
        final long max_page = postService.getMaxPageByKeyword(PAGE_SIZE,query,category);
        if (page < 1 || page > max_page && max_page > 0) {
            throw new ResourceNotFoundException();
        }

        if(longitude.isPresent() && latitude.isPresent()) {
            posts = postService.getPlainPostsByKeywordPaged(query, new Location(longitude.get(),latitude.get()),category,page,PAGE_SIZE);
        }else{
            posts = postService.getPlainPostsByKeywordPaged(query,category,page,PAGE_SIZE);
        }

        final ModelAndView mav = new ModelAndView("search-results");
        mav.addObject("posts", posts);
        mav.addObject("max_page",max_page);
        mav.addObject("user", user);
        mav.addObject("currentCategory", category);
        mav.addObject("categories", Category.values());
        mav.addObject("queryText", query);
        return mav;
    }
}
