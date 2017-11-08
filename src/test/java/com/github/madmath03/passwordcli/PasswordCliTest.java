package com.github.madmath03.passwordcli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * PasswordCliTest.
 * 
 * @author brunotm
 */
public class PasswordCliTest {

  /**
   * Logger for {@code PasswordCli}.
   */
  private static final Logger LOG = LogManager.getLogger(PasswordCliTest.class);

  /**
   * @throws java.lang.Exception If anything occurs while setting up the test.
   */
  @Before
  public void setUp() throws Exception {}

  /**
   * @throws java.lang.Exception If anything occurs while tearing down the test.
   */
  @After
  public void tearDown() throws Exception {}

  /**
   * Test method for {@link com.github.madmath03.passwordcli.PasswordCli#run(java.lang.String[])}.
   */
  @Test
  public final void testRunNoArgs() {
    String msg;

    msg = "Null Args should display help and return error";
    LOG.debug(msg);
    org.junit.Assert.assertTrue(msg, PasswordCli.run((String[]) null) != 0);

    msg = "No Args should display help and return success";
    LOG.debug(msg);
    org.junit.Assert.assertTrue(msg, PasswordCli.run(new String[] {}) != 0);
  }

  /**
   * Test method for {@link com.github.madmath03.passwordcli.PasswordCli#run(java.lang.String[])}.
   */
  @Test
  public final void testRunHelp() {
    String msg;

    msg = "Help Arg should display help and return successfully";
    LOG.debug(msg);
    org.junit.Assert.assertTrue(msg,
        PasswordCli.run(new String[] {"-" + PasswordCli.HELP.getOpt(),}) == 0);
  }

  /**
   * Test method for {@link com.github.madmath03.passwordcli.PasswordCli#run(java.lang.String[])}.
   */
  @Test
  public final void testRunRandom() {
    String msg;

    msg = "Check arg should display random password and return successfully";
    LOG.debug(msg);
    org.junit.Assert.assertTrue(msg,
        PasswordCli.run(new String[] {"-" + PasswordCli.RANDOM.getOpt()}) == 0);
  }

  /**
   * Test method for {@link com.github.madmath03.passwordcli.PasswordCli#run(java.lang.String[])}.
   */
  @Test
  public final void testRunRandomHash() {
    String msg;

    msg = "Hash arg should display hashed password and return successfully";
    LOG.debug(msg);
    org.junit.Assert.assertTrue(msg,
        PasswordCli.run(new String[] {"-" + PasswordCli.RANDOM.getOpt(),
            "-" + PasswordCli.HASH.getOpt(), "password"}) == 0);
  }

  /**
   * Test method for {@link com.github.madmath03.passwordcli.PasswordCli#run(java.lang.String[])}.
   */
  @Test
  public final void testRunHashNoValueArg() {
    String msg;

    msg = "Hash arg with no value should display help and return error";
    LOG.debug(msg);
    org.junit.Assert.assertTrue(msg,
        PasswordCli.run(new String[] {"-" + PasswordCli.HASH.getOpt()}) != 0);
  }

  /**
   * Test method for {@link com.github.madmath03.passwordcli.PasswordCli#run(java.lang.String[])}.
   */
  @Test
  public final void testRunHash() {
    String msg;

    msg = "Hash arg should display hashed password and return successfully";
    LOG.debug(msg);
    org.junit.Assert.assertTrue(msg, PasswordCli
        .run(new String[] {"-" + PasswordCli.HASH.getOpt(), "password"}) == 0);
  }

  /**
   * Test method for {@link com.github.madmath03.passwordcli.PasswordCli#run(java.lang.String[])}.
   */
  @Test
  public final void testRunCheckNoValueArg() {
    String msg;

    msg = "Check arg with no value should display help and return error";
    LOG.debug(msg);
    org.junit.Assert.assertTrue(msg,
        PasswordCli.run(new String[] {"-" + PasswordCli.CHECK.getOpt()}) != 0);
  }

  /**
   * Test method for {@link com.github.madmath03.passwordcli.PasswordCli#run(java.lang.String[])}.
   */
  @Test
  public final void testRunCheckNoHashArg() {
    String msg;

    msg = "Check with missing Hash arg should return error";
    LOG.debug(msg);
    org.junit.Assert.assertTrue(msg, PasswordCli
        .run(new String[] {"-" + PasswordCli.CHECK.getOpt(), "password"}) != 0);
  }

  /**
   * Test method for {@link com.github.madmath03.passwordcli.PasswordCli#run(java.lang.String[])}.
   */
  @Test
  public final void testRunCheckHash() {
    String msg;

    msg =
        "Check arg should display that hash matches password and return successfully";
    LOG.debug(msg);

    final StringBuilder hashBuilder = new StringBuilder();
    hashBuilder.append("$31").append("$16")
        .append(
            "$wnsEnS1xv-_3AmKXvK9iHO1NHFd8uUBIiFpsImecAch8Xt7mKnIWNcg1Iyuh5mcHMXoDm68vUyF5k")
        .append(
            "FRwswItzsRQb-DIP9VhdAkrtj-4Zi2WXESSl4HOQIsTR3RG8ieZtK6FgVZpbub3Qw39OM2zh4eQ6BS")
        .append("LPi6IpyoHRgN3KYE");

    org.junit.Assert
        .assertTrue(msg,
            PasswordCli.run(new String[] {"-" + PasswordCli.CHECK.getOpt(),
                "password", "-" + PasswordCli.HASH.getOpt(),
                hashBuilder.toString()}) == 0);

    org.junit.Assert
        .assertTrue(msg,
            PasswordCli.run(new String[] {"-" + PasswordCli.CHECK.getOpt(),
                "wrong_password", "-" + PasswordCli.HASH.getOpt(),
                hashBuilder.toString()}) != 0);
  }

}
