package ar.edu.itba.pawgram.webapp.controller;

import javax.servlet.http.HttpServletRequest;

import ar.edu.itba.pawgram.interfaces.service.SearchZoneService;
import ar.edu.itba.pawgram.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.pawgram.interfaces.service.UserService;

@Controller
public class IndexController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	@Autowired
	private UserService us;
	@Autowired
	private SearchZoneService searchZoneService;

	@RequestMapping(value={"","/"})
	public ModelAndView index(@ModelAttribute("loggedUser") final User loggedUser) {
		final long searchZonesQuantity = searchZoneService.getTotalSearchZonesByUser(loggedUser);
		if(searchZonesQuantity > 0)
			return new ModelAndView("redirect:/zones/");
		return new ModelAndView("redirect:/my_zones/create");
	}
	
	/*
	DEPRECATED
	@RequestMapping("/user")
	public ModelAndView index(@RequestParam(value = "userId", required = true) final int id) {
		final ModelAndView mav = new ModelAndView("users");
		mav.addObject("user", us.findById(id));
		return mav;
	}*/

	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request) {
		LOGGER.debug("Accessed login");
		String referrer = request.getHeader("Referer");
		request.getSession().setAttribute("url_prior_login", referrer);
		return new ModelAndView("login");
	}

}