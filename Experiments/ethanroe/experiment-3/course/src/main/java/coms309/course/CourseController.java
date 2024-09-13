package coms309.course;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class CourseController {
	
	HashMap<String, Course> courseList = new HashMap<>();
	
	
	//List Operation
	@GetMapping("/courses")
	public @ResponseBody HashMap<String,Course> getAllCourses(){
		return courseList;
	}
	
	//Create Operation
	@PostMapping("/courses")
	public @ResponseBody String createCourse(@RequestBody Course course) {
		courseList.put(course.getName(), course);
		return "New course " + course.getName() + " Saved";
	}
	
	//Read Operation
	@GetMapping("/courses/{courseName}")
	public @ResponseBody Course getCourse(@PathVariable String courseName) {
		return courseList.get(courseName);
	}
	
	//Update Operation
	@PutMapping("/courses/{courseName}")
	public @ResponseBody Course updateCourse(@PathVariable String courseName, @RequestBody Course c) {
		Course course = courseList.replace(courseName, c);
		return course;
	}
	
	//Delete Operation
	@DeleteMapping("/courses/{courseName}")
	public @ResponseBody HashMap<String, Course> deleteCourse(@PathVariable String courseName){
		courseList.remove(courseName);
		return courseList;
	}
}
