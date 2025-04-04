package com.htt.elearning.spellcheck;

import java.util.List;

public interface SpellCheckService {
    List<LanguageToolResponseDTO> checkSpelling(String text, String language);
}
