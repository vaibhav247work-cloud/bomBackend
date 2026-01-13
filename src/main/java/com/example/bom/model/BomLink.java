package com.example.bom.model;

import jakarta.persistence.*;

@Entity
public class BomLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Part parent;

    @ManyToOne
    private Part child;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Part getParent() {
		return parent;
	}

	public void setParent(Part parent) {
		this.parent = parent;
	}

	public Part getChild() {
		return child;
	}

	public void setChild(Part child) {
		this.child = child;
	}

   
}
