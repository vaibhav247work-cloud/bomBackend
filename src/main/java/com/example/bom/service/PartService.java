package com.example.bom.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bom.audit.AuditService;
import com.example.bom.dto.PartRequestDto;
import com.example.bom.dto.PartResponseDto;
import com.example.bom.model.Part;
import com.example.bom.repository.PartRepository;

@Service
@Transactional
public class PartService {

    private final PartRepository partRepository;
    private final AtomicLong sequence;
    private final AuditService auditService;

    public PartService(PartRepository partRepository,AuditService auditService) {
        this.partRepository = partRepository;
        this.auditService = auditService;
        this.sequence = new AtomicLong(initSequence());
    }

    public PartResponseDto createPart(PartRequestDto dto) {
        Part part = new Part();

        if (dto.getPartNumber() == null || dto.getPartNumber().isBlank()) {
            part.setPartNumber(generatePartNumber());
        } else {
            part.setPartNumber(dto.getPartNumber());
        }

        part.setName(dto.getName());
        part.setDescription(dto.getDescription());

        Part saved = partRepository.save(part);

        auditService.log(
                "PART_CREATED",
                "PART",
                saved.getId(),
                "Created part " + saved.getPartNumber()
        );

        return toDto(saved);
    }

    public PartResponseDto updatePart(Long id, PartRequestDto dto) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found"));

        part.setName(dto.getName());
        part.setDescription(dto.getDescription());

        auditService.log(
                "PART_UPDATED",
                "PART",
                part.getId(),
                "Updated part " + part.getPartNumber()
        );

        return toDto(part);
    }

    public PartResponseDto getPart(Long id) {
        return partRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Part not found"));
    }

    public List<PartResponseDto> search(String query) {
        return partRepository
                .findByPartNumberContainingIgnoreCaseOrNameContainingIgnoreCase(query, query)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private String generatePartNumber() {
        return String.format("PRT-%06d", sequence.getAndIncrement());
    }

    private PartResponseDto toDto(Part part) {
        PartResponseDto dto = new PartResponseDto();
        dto.setId(part.getId());
        dto.setPartNumber(part.getPartNumber());
        dto.setName(part.getName());
        dto.setDescription(part.getDescription());
        return dto;
    }
    
    private long initSequence() {
        String max = partRepository.findMaxPartNumber();

        if (max == null) {
            return 1;
        }
        return Long.parseLong(max.substring(4)) + 1;
    }
}
