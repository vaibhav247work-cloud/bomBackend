package com.example.bom.dto;

public class BomLinkRequestDto {

    private Long parentId;
    private Long childId;
    
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Long getChildId() {
		return childId;
	}
	public void setChildId(Long childId) {
		this.childId = childId;
	}


    
}
