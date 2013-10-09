package net.sf.gazpachosurvey.domain.core;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;

import net.sf.gazpachosurvey.domain.core.embeddables.MailMessageTemplateLanguageSettings;
import net.sf.gazpachosurvey.domain.i18.MailMessageTemplateTranslation;
import net.sf.gazpachosurvey.domain.support.AbstractLocalizable;
import net.sf.gazpachosurvey.types.Language;

@Entity
public class MailMessageTemplate
        extends
        AbstractLocalizable<MailMessageTemplateTranslation, MailMessageTemplateLanguageSettings> {
    private static final long serialVersionUID = 8115847063538607577L;

    @Enumerated(EnumType.STRING)
    private Language language;

    private String fromAddress;

    private String replyTo;

    @Embedded
    private MailMessageTemplateLanguageSettings languageSettings;

    @OneToMany(mappedBy = "mailMessageTemplate", fetch = FetchType.LAZY)
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "language", insertable = false, updatable = false)
    private Map<Language, MailMessageTemplateTranslation> translations;

    public MailMessageTemplate() {
        super();
    }

    @Override
    public Language getLanguage() {
        return language;
    }

    @Override
    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String from) {
        fromAddress = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    @Override
    public MailMessageTemplateLanguageSettings getLanguageSettings() {
        return languageSettings;
    }

    @Override
    public void setLanguageSettings(
            MailMessageTemplateLanguageSettings languageSettings) {
        this.languageSettings = languageSettings;
    }

    @Override
    public Map<Language, MailMessageTemplateTranslation> getTranslations() {
        if (translations == null) {
            translations = new HashMap<Language, MailMessageTemplateTranslation>();
        }

        return translations;
    }

    public void setTranslations(
            Map<Language, MailMessageTemplateTranslation> translations) {
        this.translations = translations;
    }

    @Override
    public void addTranslation(Language language,
            MailMessageTemplateTranslation translation) {
        translation.setMailMessageTemplate(this);
        getTranslations().put(language, translation);
    }

    public static Builder with(){
        return new Builder();
    }
    public static class Builder {
        private Language language;
        private String fromAddress;
        private String replyTo;
        private MailMessageTemplateLanguageSettings languageSettings;
        private Map<Language, MailMessageTemplateTranslation> translations;

        public Builder language(Language language) {
            this.language = language;
            return this;
        }

        public Builder fromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
            return this;
        }

        public Builder replyTo(String replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        public Builder languageSettings(
                MailMessageTemplateLanguageSettings languageSettings) {
            this.languageSettings = languageSettings;
            return this;
        }

        public Builder translations(
                Map<Language, MailMessageTemplateTranslation> translations) {
            this.translations = translations;
            return this;
        }

        public MailMessageTemplate build() {
            MailMessageTemplate mailMessageTemplate = new MailMessageTemplate();
            mailMessageTemplate.language = language;
            mailMessageTemplate.fromAddress = fromAddress;
            mailMessageTemplate.replyTo = replyTo;
            mailMessageTemplate.languageSettings = languageSettings;
            mailMessageTemplate.translations = translations;
            return mailMessageTemplate;
        }
    }
}