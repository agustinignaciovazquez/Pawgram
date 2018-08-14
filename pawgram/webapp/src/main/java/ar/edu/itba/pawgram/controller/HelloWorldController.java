package ar.edu.itba.pawgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.pawgram.interfaces.UserService;
import ar.edu.itba.pawgram.model.User;

@Controller
public class HelloWorldController {
	@Autowired
	private UserService us;
	
	@RequestMapping("/")
	public ModelAndView index(@RequestParam(value = "userId", required = true) final int id) {
	final ModelAndView mav = new ModelAndView("index");
	mav.addObject("user", us.findById(id));
	return mav;
	}
	
	@RequestMapping("/create")
	public ModelAndView create(@RequestParam(value = "name", required = true) final String username) {
	final User u = us.create(username,"TEST","TEST","TEST");
	return new ModelAndView("redirect:/?userId=" + u.getId());
	}
}