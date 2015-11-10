package com.github.lxgang.centos.activemq.util;

import java.io.Serializable;

public class TestBean implements Serializable{
	private static final long serialVersionUID = -6368068210570412311L;
	
	private String name;
	private int age;
	private String sex;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "TestBean [name=" + name + ", age=" + age + ", sex=" + sex + "]";
	}
}
