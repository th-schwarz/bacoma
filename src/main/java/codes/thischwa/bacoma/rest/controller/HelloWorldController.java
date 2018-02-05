package codes.thischwa.bacoma.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloWorldController {

	@RequestMapping(path = "/hello", method = RequestMethod.GET)
	public ResponseEntity<String> hello() {
		return ResponseEntity.ok("Hello World!");
	}
}
