package com.github.lxgang.centos.activemq.bean;

import java.io.Serializable;

public class BeanParent  implements Serializable{
	private static final long serialVersionUID = -7964992715105323087L;
	
	public Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
