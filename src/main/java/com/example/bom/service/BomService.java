package com.example.bom.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bom.audit.AuditService;
import com.example.bom.dto.BomLinkRequestDto;
import com.example.bom.dto.BomNodeDto;
import com.example.bom.model.BomLink;
import com.example.bom.model.Part;
import com.example.bom.repository.BomLinkRepository;
import com.example.bom.repository.PartRepository;

@Service
@Transactional
public class BomService {

    private static final int MAX_DEPTH = 5;

    private final PartRepository partRepository;
    private final BomLinkRepository bomLinkRepository;
    private final AuditService auditService;

    public BomService(
            PartRepository partRepository,
            BomLinkRepository bomLinkRepository,
            AuditService auditService
    ) {
        this.partRepository = partRepository;
        this.bomLinkRepository = bomLinkRepository;
        this.auditService = auditService;
    }

    public BomNodeDto getBom(Long rootPartId, int depth) {
        if (depth > MAX_DEPTH) {
            throw new IllegalArgumentException(
                    "BOM expansion limit exceeded (max depth = " + MAX_DEPTH + ")"
            );
        }

        Part root = partRepository.findById(rootPartId)
                .orElseThrow(() -> new RuntimeException("Root part not found"));

        return buildTree(root, depth);
    }

    private BomNodeDto buildTree(Part part, int depth) {
        BomNodeDto node = toNode(part);

        if (depth == 0) {
            return node;
        }

        List<BomLink> childrenLinks = bomLinkRepository.findByParent(part);
        for (BomLink link : childrenLinks) {
            node.getChildren().add(
                    buildTree(link.getChild(), depth - 1)
            );
        }

        return node;
    }

    public void createLink(BomLinkRequestDto dto) {
        Part parent = partRepository.findById(dto.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent part not found"));

        Part child = partRepository.findById(dto.getChildId())
                .orElseThrow(() -> new RuntimeException("Child part not found"));

        BomLink link = new BomLink();
        link.setParent(parent);
        link.setChild(child);

        bomLinkRepository.save(link);

        auditService.log(
                "BOM_LINK_CREATED",
                "BOM",
                parent.getId(),
                "Linked parent " + parent.getPartNumber() +
                " to child " + child.getPartNumber()
        );
    }

    public void removeLink(BomLinkRequestDto dto) {
        Part parent = partRepository.findById(dto.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent part not found"));

        Part child = partRepository.findById(dto.getChildId())
                .orElseThrow(() -> new RuntimeException("Child part not found"));

        bomLinkRepository.deleteByParentAndChild(parent, child);

        auditService.log(
                "BOM_LINK_REMOVED",
                "BOM",
                parent.getId(),
                "Removed link from parent " + parent.getPartNumber() +
                " to child " + child.getPartNumber()
        );
    }

    private BomNodeDto toNode(Part part) {
        BomNodeDto dto = new BomNodeDto();
        dto.setId(part.getId());
        dto.setPartNumber(part.getPartNumber());
        dto.setName(part.getName());
        return dto;
    }
}
