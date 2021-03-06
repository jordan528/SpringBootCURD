package com.syed.springboot.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.syed.springboot.model.User;
import com.syed.springboot.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MainController {

	@Autowired
	UserService userService;

	// Rest calls for add client/pani wala
	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	public ResponseEntity<Object> createUser(@RequestBody User userbean) {

		User userExists = userService.findUserByEmail(userbean.getEmail());

		Map<String, Object> response = new HashMap<>();
		try {
			if (userExists != null) {
				response.put("status", "Failure");
				response.put("message", "Email already exist");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			} else {
				System.out.println("clientdata" + userbean);
				userService.saveClient(userbean);

				response.put("user", userbean);
				response.put("status", "Success");
				return new ResponseEntity<Object>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "Failure");
			response.put("message", "Something went wrong Please try again....");
			log.debug("stack trace-->" + e);
			return new ResponseEntity<Object>(response, HttpStatus.OK);

		}

	}

	@GetMapping("/test")
	@ResponseBody
	public String welcome(Map<String, Object> model) {
		model.put("message", "test");
		return "test";
	}

	@RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllUsers() {
		Map<String, Object> response = new HashMap<>();

		List<User> isExists = userService.findAllUsers();
		if (isExists == null) {
			response.put("status", "failed");
			response.put("message", "UserNotFound");

			return new ResponseEntity<Object>(response, HttpStatus.OK);

		} else {
			response.put("status", "success");
			response.put("data", isExists);
		}

		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}

	@RequestMapping(value = "/findUserByEmail", method = RequestMethod.POST)
	public ResponseEntity<Object> findUserByEmail(@RequestBody User userbean) {
		Map<String, Object> response = new HashMap<>();

		User isExists = userService.findUserByEmail(userbean.getEmail());
		if (isExists == null) {
			response.put("status", "failed");
			response.put("message", "UserNotFound");

			return new ResponseEntity<Object>(response, HttpStatus.OK);

		} else {
			response.put("status", "success");
			response.put("data", isExists);
		}

		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/updateUserByEmail", method = RequestMethod.POST)
	public ResponseEntity<Object> updateUserByEmail(@RequestBody User userbean) {
		Map<String, Object> response = new HashMap<>();

		User isExists = userService.findUserByEmail(userbean.getEmail());
		if (isExists == null) {
			response.put("status", "failed");
			response.put("message", "UserNotFound");

			return new ResponseEntity<Object>(response, HttpStatus.OK);

		} else {
			if(userbean.getAddress()!=null)
			isExists.setAddress(userbean.getAddress());
			if(userbean.getDob()!=null)
			isExists.setDob(userbean.getDob());
			if(userbean.getMobile()!=null)
			isExists.setMobile(userbean.getMobile());
			if(userbean.getUserName()!=null)
			isExists.setUserName(userbean.getUserName());
			userService.updateUser(isExists);
			response.put("status", "success");
			response.put("data", isExists);
		}

		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
}
