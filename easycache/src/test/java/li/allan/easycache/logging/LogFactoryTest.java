/**
 *    Copyright 2009-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package li.allan.easycache.logging;

import li.allan.easycache.logging.commons.JakartaCommonsLoggingImpl;
import li.allan.easycache.logging.jdk14.Jdk14LoggingImpl;
import li.allan.easycache.logging.log4j.Log4jImpl;
import li.allan.easycache.logging.log4j2.Log4j2Impl;
import li.allan.easycache.logging.nologging.NoLoggingImpl;
import li.allan.easycache.logging.slf4j.Slf4jImpl;
import li.allan.easycache.logging.stdout.StdOutImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogFactoryTest {

  @Test
  public void shouldUseCommonsLogging() {
    LogFactory.useCommonsLogging();
    Log log = LogFactory.getLog(Object.class);
    logSomething(log);
    assertEquals(log.getClass().getName(), JakartaCommonsLoggingImpl.class.getName());
  }

  @Test
  public void shouldUseLog4J() {
    LogFactory.useLog4JLogging();
    Log log = LogFactory.getLog(Object.class);
    logSomething(log);
    assertEquals(log.getClass().getName(), Log4jImpl.class.getName());
  }

  @Test
  public void shouldUseLog4J2() {
    LogFactory.useLog4J2Logging();
    Log log = LogFactory.getLog(Object.class);
    logSomething(log);
    assertEquals(log.getClass().getName(), Log4j2Impl.class.getName());
  }
  
  @Test
  public void shouldUseJdKLogging() {
    LogFactory.useJdkLogging();
    Log log = LogFactory.getLog(Object.class);
    logSomething(log);
    assertEquals(log.getClass().getName(), Jdk14LoggingImpl.class.getName());
  }

  @Test
  public void shouldUseSlf4j() {
    LogFactory.useSlf4jLogging();
    Log log = LogFactory.getLog(Object.class);
    logSomething(log);
    assertEquals(log.getClass().getName(), Slf4jImpl.class.getName());
  }

  @Test
  public void shouldUseStdOut() {
    LogFactory.useStdOutLogging();
    Log log = LogFactory.getLog(Object.class);
    logSomething(log);
    assertEquals(log.getClass().getName(), StdOutImpl.class.getName());
  }

  @Test
  public void shouldUseNoLogging() {
    LogFactory.useNoLogging();
    Log log = LogFactory.getLog(Object.class);
    logSomething(log);
    assertEquals(log.getClass().getName(), NoLoggingImpl.class.getName());
  }

  private void logSomething(Log log) {
    log.warn("Warning message.");
    log.debug("Debug message.");
    log.error("Error message.");
    log.error("Error with Exception.", new Exception("Test exception."));
  }

}
