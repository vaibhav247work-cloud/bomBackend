package com.example.bom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bom.model.Part;

public interface PartRepository extends JpaRepository<Part, Long> {
	
	 List<Part> findByPartNumberContainingIgnoreCaseOrNameContainingIgnoreCase(
	            String partNumber,
	            String name
	    );

	    long count();
	    
	    @Query("select max(p.partNumber) from Part p where p.partNumber like 'PRT-%'")
	    	String findMaxPartNumber();
	    
	    Optional<Part> findByPartNumber(String partNumber);
}
