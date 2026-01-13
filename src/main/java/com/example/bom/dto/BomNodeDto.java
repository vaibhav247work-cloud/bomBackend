package com.example.bom.dto;

import java.util.ArrayList;
import java.util.List;

public class BomNodeDto {

    private Long id;
    private String partNumber;
    private String name;
    private List<BomNodeDto> children = new ArrayList<>();
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<BomNodeDto> getChildren() {
		return children;
	}
	public void setChildren(List<BomNodeDto> children) {
		this.children = children;
	}


    
}
