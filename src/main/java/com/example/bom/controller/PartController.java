package com.example.bom.controller;

import com.example.bom.dto.PartRequestDto;
import com.example.bom.dto.PartResponseDto;
import com.example.bom.service.PartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
@CrossOrigin
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    @PostMapping
    public PartResponseDto create(@RequestBody PartRequestDto dto) {
        return partService.createPart(dto);
    }

    @PutMapping("/{id}")
    public PartResponseDto update(
            @PathVariable Long id,
            @RequestBody PartRequestDto dto
    ) {
        return partService.updatePart(id, dto);
    }

    @GetMapping("/{id}")
    public PartResponseDto get(@PathVariable Long id) {
        return partService.getPart(id);
    }

    @GetMapping("/search")
    public List<PartResponseDto> search(@RequestParam String query) {
        return partService.search(query);
    }
}
