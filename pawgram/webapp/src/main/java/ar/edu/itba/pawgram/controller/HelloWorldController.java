package ar.edu.itba.pawgram.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {
@RequestMapping("/")
public ModelAndView helloWorld() {
	final ModelAndView mav = new ModelAndView("index");
	mav.addObject("greeting", "PAW");
	return mav;
}
}