/*******************************************************************************
 * Copyright (c) 2014 antoniomariasanchez at gmail.com.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     antoniomaria - initial API and implementation
 ******************************************************************************/
package net.sf.gazpachoquest.jpa.eclipselink;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This is a wrapper class for SLF4J. It is used when messages need to be logged
 * through SLF4J.
 * </p>
 * <p>
 * Para usar SLF4j para los logs de EclipseLink configuramos la propiedad
 * <code>eclipselink.logging.logger</code> con el valor
 * <code>org.eclipse.persistence.logging.Slf4jSessionLogger</code>
 * </p>
 * <p>
 * La configuración del nivel de los logs no se realiza en EclipseLink (con la
 * propiedad eclipselink.logging.level), sino en la implementación de SLF4J.
 * </p>
 * Se puede usar el resto de las propiedades de logging de EclipseLink
 * (eclipselink.logging.timestamp, eclipselink.logging.thread,
 * eclipselink.logging.session, eclipselink.logging.connection y
 * eclipselink.logging.parameters) para configurar el formato de salida.
 * <p>
 * Se usan las siguientes categorias de log:
 * </p>
 * <ul>
 * <li>org.eclipse.persistence.logging.default
 * <li>org.eclipse.persistence.logging.sql
 * <li>org.eclipse.persistence.logging.transaction
 * <li>org.eclipse.persistence.logging.event
 * <li>org.eclipse.persistence.logging.connection
 * <li>org.eclipse.persistence.logging.query
 * <li>org.eclipse.persistence.logging.cache
 * <li>org.eclipse.persistence.logging.propagation
 * <li>org.eclipse.persistence.logging.sequencing
 * <li>org.eclipse.persistence.logging.ejb
 * <li>org.eclipse.persistence.logging.ejb_or_metadata
 * <li>org.eclipse.persistence.logging.weaver
 * <li>org.eclipse.persistence.logging.properties
 * <li>org.eclipse.persistence.logging.server
 * </ul>
 * <p>
 * Los niveles de log de EclipseLink y SLF4J son distintos, se realiza la
 * siguiente correspondencia:
 * </p>
 * <ul>
 * <li>ALL,FINER,FINEST to TRACE
 * <li>FINE to DEBUG
 * <li>CONFIG,INFO to INFO
 * <li>WARNING to WARN
 * <li>SEVERE to ERROR
 * </ul>
 * 
 * @author Miguel Angel Sosvilla Luis.
 * 
 */
public class Slf4jSessionLogger extends AbstractSessionLog {

    enum LogLevel {
        DEBUG, ERROR, INFO, OFF, TRACE, WARN
    }

    public static final String DEFAULT_CATEGORY = "default";

    public static final String DEFAULT_ECLIPSELINK_NAMESPACE = "org.eclipse.persistence.logging" + "."
            + DEFAULT_CATEGORY;

    public static final String ECLIPSELINK_NAMESPACE = "org.eclipse.persistence.logging";

    private final Map<String, Logger> categoryLoggers = new HashMap<String, Logger>();

    private Map<Integer, LogLevel> mapLevels;

    public Slf4jSessionLogger() {
        super();
        createCategoryLoggers();
        initMapLevels();
    }

    @Override
    public void log(final SessionLogEntry entry) {
        if (!shouldLog(entry.getLevel(), entry.getNameSpace())) {
            return;
        }

        Logger logger = getLogger(entry.getNameSpace());
        LogLevel logLevel = getLogLevel(entry.getLevel());

        StringBuilder message = new StringBuilder();

        message.append(getSupplementDetailString(entry));
        message.append(formatMessage(entry));

        switch (logLevel) {
        case TRACE:
            logger.trace(message.toString());
            break;
        case DEBUG:
            logger.debug(message.toString());
            break;
        case INFO:
            logger.info(message.toString());
            break;
        case WARN:
            logger.warn(message.toString());
            break;
        case ERROR:
            logger.error(message.toString());
            break;
        default:
            throw new IllegalStateException("Not supported" + logLevel);
        }
    }

    /**
     * Return true if SQL logging should log visible bind parameters. If the
     * shouldDisplayData is not set, return false.
     */
    @Override
    public boolean shouldDisplayData() {
        if (shouldDisplayData != null) {
            return shouldDisplayData.booleanValue();
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldLog(final int level) {
        return shouldLog(level, "default");
    }

    @Override
    public boolean shouldLog(final int level, final String category) {
        Logger logger = getLogger(category);
        boolean resp = false;

        LogLevel logLevel = getLogLevel(level);

        switch (logLevel) {
        case TRACE:
            resp = logger.isTraceEnabled();
            break;
        case DEBUG:
            resp = logger.isDebugEnabled();
            break;
        case INFO:
            resp = logger.isInfoEnabled();
            break;
        case WARN:
            resp = logger.isWarnEnabled();
            break;
        case ERROR:
            resp = logger.isErrorEnabled();
            break;
        default:
            throw new IllegalStateException("Not supported" + logLevel);
        }

        return resp;
    }

    /**
     * INTERNAL: Add Logger to the categoryLoggers.
     */
    private void addLogger(final String loggerCategory, final String loggerNameSpace) {
        categoryLoggers.put(loggerCategory, LoggerFactory.getLogger(loggerNameSpace));
    }

    /**
     * Initialize loggers eagerly
     */
    private void createCategoryLoggers() {
        for (String category : SessionLog.loggerCatagories) {
            addLogger(category, ECLIPSELINK_NAMESPACE + "." + category);
        }
        // Logger default para cuando no hay categoría.
        addLogger(DEFAULT_CATEGORY, DEFAULT_ECLIPSELINK_NAMESPACE);
    }

    /**
     * INTERNAL: Return the Logger for the given category
     */
    private Logger getLogger(String category) {

        if (StringUtils.isBlank(category) || !categoryLoggers.containsKey(category)) {
            category = DEFAULT_CATEGORY;
        }

        return categoryLoggers.get(category);

    }

    /**
     * Return the corresponding Slf4j Level for a given EclipseLink level.
     */
    private LogLevel getLogLevel(final Integer level) {
        LogLevel logLevel = mapLevels.get(level);

        if (logLevel == null) {
            logLevel = LogLevel.OFF;
        }

        return logLevel;
    }

    /**
     * Relación de los niveles de log de EclipseLink y los de SLF4J
     */
    private void initMapLevels() {
        mapLevels = new HashMap<Integer, LogLevel>();

        mapLevels.put(SessionLog.ALL, LogLevel.TRACE);
        mapLevels.put(SessionLog.FINEST, LogLevel.TRACE);
        mapLevels.put(SessionLog.FINER, LogLevel.TRACE);
        mapLevels.put(SessionLog.FINE, LogLevel.DEBUG);
        mapLevels.put(SessionLog.CONFIG, LogLevel.INFO);
        mapLevels.put(SessionLog.INFO, LogLevel.INFO);
        mapLevels.put(SessionLog.WARNING, LogLevel.WARN);
        mapLevels.put(SessionLog.SEVERE, LogLevel.ERROR);
    }

}
