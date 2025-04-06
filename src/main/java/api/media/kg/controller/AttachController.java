package api.media.kg.controller;

import api.media.kg.dto.AttachDTO;
import api.media.kg.service.AttachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attach")
@Tag(name = "Attach", description = "Attach Controller")
public class AttachController {
    @Autowired
    private AttachService attachService;

    @PostMapping("/upload")
    @Operation(summary = "Upload file", description = "Upload file")
    public ResponseEntity<AttachDTO> create(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachService.upload(file));
    }
    @GetMapping("/open/{fileName}")
    @Operation(summary = "Open file", description = "Open file")
    public ResponseEntity<Resource> open(@PathVariable String fileName) {
        return attachService.open(fileName);
    }
}
