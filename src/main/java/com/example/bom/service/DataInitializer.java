package com.example.bom.service;

import com.example.bom.model.BomLink;
import com.example.bom.model.Part;
import com.example.bom.repository.BomLinkRepository;
import com.example.bom.repository.PartRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(PartRepository partRepo, BomLinkRepository bomRepo) {
        return args -> {

            // Root
            Part root = createPart(partRepo, "PRT-000001", "ROOT_ASSY");

            // Level 1
            List<Part> level1 = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                Part p = createPart(partRepo, "PRT-00000" + (i + 1), "L1_PART_" + i);
                level1.add(p);
                bomRepo.save(createLink(root, p));
            }

            // Level 2
            List<Part> level2 = new ArrayList<>();
            int counter = 5;
            for (Part parent : level1) {
                for (int i = 1; i <= 3; i++) {
                    Part child = createPart(
                            partRepo,
                            "PRT-0000" + counter++,
                            parent.getName() + "_CHILD_" + i
                    );
                    level2.add(child);
                    bomRepo.save(createLink(parent, child));
                }
            }

            // Level 3 (attach to first few level-2 parts)
            for (int i = 0; i < 6; i++) {
                Part child = createPart(
                        partRepo,
                        "PRT-0000" + counter++,
                        "L3_PART_" + i
                );
                bomRepo.save(createLink(level2.get(i), child));
            }

            System.out.println("✅ Sample BOM data created successfully");
        };
    }

    private Part createPart(PartRepository repo, String number, String name) {
        Part p = new Part();
        p.setPartNumber(number);
        p.setName(name);
        p.setDescription("Sample part " + name);
        return repo.save(p);
    }

    private BomLink createLink(Part parent, Part child) {
        BomLink link = new BomLink();
        link.setParent(parent);
        link.setChild(child);
        return link;
    }
}

