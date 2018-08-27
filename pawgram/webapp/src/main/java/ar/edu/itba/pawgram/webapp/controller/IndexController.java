package ar.edu.itba.pawgram.webapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.pawgram.interfaces.service.UserService;

@Controller
public class IndexController {
	@Autowired
	private UserService us;

	@RequestMapping("/")
	public ModelAndView index() {
		return new ModelAndView("index");
	}
	
	@RequestMapping("/user")
	public ModelAndView index(@RequestParam(value = "userId", required = true) final int id) {
		final ModelAndView mav = new ModelAndView("users");
		mav.addObject("user", us.findById(id));
		return mav;
	}

	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request) {

		String referrer = request.getHeader("Referer");
		request.getSession().setAttribute("url_prior_login", referrer);
		return new ModelAndView("login");
	}

}