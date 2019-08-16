package util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

/**
 * @author Y.Kyan
 *
 * 2016/02/20
 */
public class LogbackUtils {

  @Getter
  @AllArgsConstructor
  public enum LoggerNameType {
    ROOT(Logger.ROOT_LOGGER_NAME);

    private String value;
  }

  @Getter
  @AllArgsConstructor
  public enum LayoutPatternType {
    DEFAULT("%date{yyyy-MM-dd HH:mm:ss} [%thread] %level %file:%line - %msg%n"),;

    private String value;
  }

  public static void addRootLoggerToFileAppender(final Path filePath, final Level logLevel, final boolean additive) {
    final Logger rootLogger = (Logger) LoggerFactory.getLogger(LoggerNameType.ROOT.getValue());
    addFileAppendaer(rootLogger, filePath, logLevel, additive, LayoutPatternType.DEFAULT.getValue(), StandardCharsets.UTF_8);
  }

  public static void addFileAppendaer(
      final Logger logger,
      final Path filePath,
      final Level logLevel,
      final boolean additive,
      final String pattern,
      final Charset charset) {

    final LoggerContext loggerContext = logger.getLoggerContext();

    final PatternLayoutEncoder encoder = new PatternLayoutEncoder();
    encoder.setContext(loggerContext);
    encoder.setCharset(charset);
    encoder.setPattern(pattern);
    encoder.start();

    final FileAppender<ILoggingEvent> fileAppendaer = new FileAppender<ILoggingEvent>();
    fileAppendaer.setContext(loggerContext);
    fileAppendaer.setEncoder(encoder);
    fileAppendaer.setAppend(true);
    fileAppendaer.setFile(filePath.toString().replace("\\", "/"));
    fileAppendaer.start();

    logger.addAppender(fileAppendaer);
    logger.setAdditive(additive);
    logger.setLevel(logLevel);
  }
}
