package net.sf.gazpachoquest.services;

import net.sf.gazpachoquest.domain.core.QuestionOption;
import net.sf.gazpachoquest.domain.core.embeddables.QuestionOptionLanguageSettings;
import net.sf.gazpachoquest.domain.i18.QuestionOptionTranslation;

public interface QuestionOptionService extends
        LocalizedPersistenceService<QuestionOption, QuestionOptionTranslation, QuestionOptionLanguageSettings> {
}
