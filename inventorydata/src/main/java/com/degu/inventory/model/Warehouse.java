package com.degu.inventory.model;
import lombok.Data;
import java.util.Date;

@Data
public class Warehouse {
	private int id;
	private String name;
	private String location;
	private String address;
	private String manager;
	private Date lastUpdated;
	private Date created;
}
