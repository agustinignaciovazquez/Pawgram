package ar.edu.itba.pawgram.webapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.pawgram.webapp.form.UserForm;
import ar.edu.itba.pawgram.interfaces.UserService;
import ar.edu.itba.pawgram.model.User;

@Controller
public class MainController {
	@Autowired
	private UserService us;
		
	@RequestMapping("/")
	public ModelAndView index(@ModelAttribute("registerForm") final UserForm form) {
		return new ModelAndView("index");
	}
	
	@RequestMapping("/register")
	public ModelAndView register(@ModelAttribute("registerForm") final UserForm form) {
		return new ModelAndView("register");
	}
	
	@RequestMapping("/user")
	public ModelAndView index(@RequestParam(value = "userId", required = true) final int id) {
		final ModelAndView mav = new ModelAndView("users");
		mav.addObject("user", us.findById(id));
		return mav;
	}
	
	@RequestMapping("/login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}
	
	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm form, final BindingResult errors) {
		if (errors.hasErrors()) {
			return register(form);
		}
		final User u = us.create(form.getName(),form.getSurname(),form.getMail(),form.getPassword());
		return new ModelAndView("redirect:/user?userId="+ u.getId());
	}
}