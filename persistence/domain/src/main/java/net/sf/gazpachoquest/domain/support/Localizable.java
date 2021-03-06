/*
 * Copyright (c) 2014 antoniomariasanchez at gmail.com.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     antoniomaria - initial API and implementation
 */
package net.sf.gazpachoquest.domain.support;

import java.util.Map;

import net.sf.gazpachoquest.types.Language;

public interface Localizable<LS extends LanguageSettings, TR extends Translation<LS>> extends Persistable {

    Language getLanguage();

    void setLanguage(Language language);

    LS getLanguageSettings();

    Map<Language, TR> getTranslations();

    void setLanguageSettings(LS languageSettings);

}
