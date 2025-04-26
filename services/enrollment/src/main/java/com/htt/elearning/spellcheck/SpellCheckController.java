package com.htt.elearning.spellcheck;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/spell-check")
public class SpellCheckController {
    private final SpellCheckService spellCheckService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> checkSpelling (
            @RequestParam String text,
            @RequestParam(defaultValue = "en-US") String language
    ){
        return ResponseEntity.ok(spellCheckService.checkSpelling(text, language));
    }
}
