package com.example.bom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bom.model.BomLink;
import com.example.bom.model.Part;

public interface BomLinkRepository extends JpaRepository<BomLink, Long> {
	
	 List<BomLink> findByParent(Part parent);

	    void deleteByParentAndChild(Part parent, Part child);
}
