package codes.thischwa.bacoma.ui.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import codes.thischwa.bacoma.model.pojo.requestcycle.Greeting;

@Controller
public class GreetingController {

	@RequestMapping(path = "/greeting", method = RequestMethod.GET)
	public ResponseEntity<Greeting> greeting() {
		Greeting g = new Greeting();
		g.setGreeting("Welcome ...");
		g.setSubGreeting("... to BaCoMa!");
		return ResponseEntity.ok(g);
	}
}
