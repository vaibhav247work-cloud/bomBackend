package com.example.bom.controller;

import com.example.bom.dto.BomLinkRequestDto;
import com.example.bom.dto.BomNodeDto;
import com.example.bom.service.BomService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bom")
@CrossOrigin
public class BomController {

    private final BomService bomService;

    public BomController(BomService bomService) {
        this.bomService = bomService;
    }

    @GetMapping("/{rootPartId}")
    public BomNodeDto getBom(
            @PathVariable Long rootPartId,
            @RequestParam(defaultValue = "1") int depth
    ) {
        return bomService.getBom(rootPartId, depth);
    }

    @PostMapping("/link")
    public void createLink(@RequestBody BomLinkRequestDto dto) {
        bomService.createLink(dto);
    }

    @DeleteMapping("/link")
    public void removeLink(@RequestBody BomLinkRequestDto dto) {
        bomService.removeLink(dto);
    }
}
