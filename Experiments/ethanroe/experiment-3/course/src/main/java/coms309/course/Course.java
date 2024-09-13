package coms309.course;

public class Course {
	private String name;
	
	private int courseTime;
	
	public Course(String name, int courseTime) {
		this.name = name;
		this.courseTime = courseTime;
	}
	
	public Course() {
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getCourseTime() {
		return courseTime;
	}
	
	public void setCourseTime(int courseTime) {
		this.courseTime = courseTime;
	}
	
	@Override
    public String toString() {
        return name + " "
                + courseTime + " minutes";
    }
}
