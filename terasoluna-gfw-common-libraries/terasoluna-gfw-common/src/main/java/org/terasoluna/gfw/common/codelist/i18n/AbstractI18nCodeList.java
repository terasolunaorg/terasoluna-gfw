/*
 * Copyright(c) 2024 NTT DATA Group Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.terasoluna.gfw.common.codelist.i18n;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.terasoluna.gfw.common.codelist.AbstractCodeList;

/**
 * Abstract extended implementation of {@link AbstractCodeList}. Adds Internationalization support to {@link AbstractCodeList}
 * by implementing {I18nCodeList} interface.
 */
public abstract class AbstractI18nCodeList extends AbstractCodeList implements
                                           I18nCodeList, InitializingBean {

    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(
            AbstractI18nCodeList.class);

    /**
     * The locales codelist available.
     */
    private Set<Locale> codeListLocales;

    /**
     * The default locale as fallback.<br>
     * if extend this and override default value of fallbackTo, affects {@link #afterPropertiesSet afterPropertiesSet}.
     * @since 5.5.1
     */
    protected Locale fallbackTo;

    /**
     * Sets the default locale as fallback.
     * @param fallbackTo the default locale as fallback
     * @since 5.5.1
     */
    public void setFallbackTo(Locale fallbackTo) {
        Assert.notNull(fallbackTo, "fallbackTo must not be null");
        this.fallbackTo = fallbackTo;
    }

    /**
     * <p>
     * Returns a codelist as map for the LocaleContextHolder's locale.
     * </p>
     * @see org.terasoluna.gfw.common.codelist.CodeList#asMap()
     * @see org.springframework.context.i18n.LocaleContextHolder#getLocale()
     */
    @Override
    public Map<String, String> asMap() {
        return asMap(LocaleContextHolder.getLocale());
    }

    /**
     * <p>
     * Returns codelist for the specified locale.<br>
     * if there is no codelist for the specified locale, returns it by {@code fallbackTo} locale.
     * </p>
     * @param locale locale of codelist
     * @see org.terasoluna.gfw.common.codelist.i18n.I18nCodeList#asMap(java.util.Locale)
     */
    @Override
    public Map<String, String> asMap(Locale locale) {
        Assert.notNull(locale, "locale is null");
        return obtainMap(locale);
    }

    /**
     * This method is called after the properties of the codelist are set.
     * <p>
     * check whether codelist of fallbackTo locale is defined.<br>
     * fallbackTo locale provided by {@link #fallbackTo fallbackTo} or default locale using {@link Locale#getDefault
     * Locale#getDefault}.<br>
     * default locale is fallbackTo to it's language locale.
     * </p>
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() {
        codeListLocales = registerCodeListLocales();
        Assert.notNull(codeListLocales, "codeListLocales must not null.");

        if (fallbackTo == null) {
            Locale defaultLocale = Locale.getDefault();
            fallbackTo = resolveLocale(defaultLocale);
            Assert.notNull(fallbackTo, "No codelist for default locale ('"
                    + defaultLocale + "' and '" + defaultLocale.getLanguage()
                    + "'). Please define codelist for default locale or set locale already defined in codelist to fallbackTo.");
        } else {
            Assert.isTrue(codeListLocales.contains(fallbackTo),
                    "No codelist found for fallback locale '" + fallbackTo
                            + "', it must be defined.");
        }
    }

    /**
     * Returns the locale resolved in the following order.
     * <ol>
     * <li>Returns the specified locale if defined corresponding codelist.</li>
     * <li>Returns the language part of the specified locale if defined corresponding codelist.</li>
     * <li>Returns the {@code fallbackTo} locale.</li>
     * </ol>
     * @param locale locale for codelist
     * @return resolved locale
     */
    protected Locale resolveLocale(Locale locale) {
        if (codeListLocales.contains(locale)) {
            logger.debug("Found codelist for specified locale '{}'.", locale);
            return locale;
        }

        String lang = locale.getLanguage();
        if (StringUtils.hasLength(lang) && !lang.equals(locale.toString())) {
            Locale langOnlyLocale = new Locale(lang);
            if (codeListLocales.contains(langOnlyLocale)) {
                logger.debug(
                        "Found codelist for specified locale '{}' (language only).",
                        locale);
                return langOnlyLocale;
            }
        }

        logger.debug(
                "There is no codelist for specified locale '{}'. Use '{}' as fallback.",
                locale, fallbackTo);
        return fallbackTo;
    }

    /**
     * Register the locales codelist available.
     * <p>
     * Called from {@link #afterPropertiesSet()}
     * </p>
     * @return Set available locales
     */
    abstract protected Set<Locale> registerCodeListLocales();

    /**
     * Obtain the codelist of specified locale and returns it as a map.
     * @param locale locale of codelist
     * @return Map codelist information
     */
    abstract protected Map<String, String> obtainMap(Locale locale);
}
