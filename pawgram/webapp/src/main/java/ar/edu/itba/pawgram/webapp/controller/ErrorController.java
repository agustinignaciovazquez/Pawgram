package ar.edu.itba.pawgram.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
@RequestMapping("/errors")
@Controller
public class ErrorController {

	@RequestMapping("/400")
	public ModelAndView badRequest() {
		return new ModelAndView("400");
	}

	@RequestMapping("/401")
	public ModelAndView unauthorized() {
		return new ModelAndView("401");
	}

	@RequestMapping("/403")
	public ModelAndView forbidden() {
		return new ModelAndView("redirect:/");
	}

	@RequestMapping("/404")
	public ModelAndView noSuchRequestHandler() {
		return new ModelAndView("404");
	}


}