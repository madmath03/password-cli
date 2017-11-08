package com.github.madmath03.passwordcli;

import com.github.madmath03.password.Passwords;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A Command Line Interface for passwords.
 */
public final class PasswordCli {

  /**
   * Logger for {@code PasswordCli}.
   */
  private static final Logger LOG = LogManager.getLogger(PasswordCli.class);

  /**
   * Usage command.
   */
  private static final String USAGE;

  static {
    String tempUsage;
    try {
      final Path path = Paths.get(PasswordCli.class.getProtectionDomain()
          .getCodeSource().getLocation().getPath());

      tempUsage = "java -jar " + path.getFileName();
    } catch (Exception e) {
      if (LOG.isTraceEnabled()) {
        LOG.trace(e);
      }
      tempUsage = "java " + PasswordCli.class.getSimpleName() + ".class";
    }

    USAGE = tempUsage;
  }


  /**
   * Help option.
   * 
   * <p>
   * There is a cohabitation issue between help and required options... See stackoverflow.com:
   * "apache-cli-required-options-contradicts-with-help-option" for more information.
   * </p>
   */
  public static final Option HELP;

  static {
    HELP = new Option("h", "help", false, "Print this message");
  }

  /**
   * Quiet mode.
   */
  public static final Option QUIET;

  static {
    QUIET = new Option("quiet", "Be extra quiet");
  }


  /**
   * Hash a given password.
   */
  public static final Option HASH;

  static {
    HASH = new Option("ha", "hash", true, "Hash a given password");
  }

  /**
   * Check a password with a given hash.
   */
  public static final Option CHECK;

  static {
    final String description =
        "Check a password with a given hash. A hashed password must be provided through the \""
            + HASH.getOpt() + "\" option";
    CHECK = new Option("c", "check", true, description);
  }

  /**
   * Generate a random password.
   */
  public static final Option RANDOM;

  static {
    RANDOM = new Option("r", "random", false, "Generate a random password");
  }


  /**
   * Command line options.
   */
  private static final Options OPTIONS = new Options();

  static {
    // Generic options
    OPTIONS.addOption(HELP);
    OPTIONS.addOption(QUIET);

    // Cipher options
    OPTIONS.addOption(HASH);
    OPTIONS.addOption(CHECK);
    OPTIONS.addOption(RANDOM);
  }


  /**
   * Private constructor.
   */
  private PasswordCli() {}

  /**
   * Main.
   * 
   * @param args Command line arguments.
   */
  public static void main(final String... args) {
    System.exit(run(args));
  }

  /**
   * Run patch catch creation.
   * 
   * @param args Command line arguments.
   * 
   * @return the command line return code.
   */
  public static int run(final String... args) {
    int code;

    final CommandLineParser parser = new DefaultParser();
    try {

      if (checkForHelp(args)) {
        // Print help and ignore everything else...
        printHelp();

        code = 0;
      } else {
        // (Really) parse the command line arguments
        final CommandLine line = parser.parse(OPTIONS, args);

        code = readCommandLine(line);
      }

    } catch (ParseException e) {
      // oops, something went wrong
      if (LOG.isErrorEnabled()) {
        LOG.error("Command line parsing failed: " + e.getLocalizedMessage());
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug(e);
      }
      code = -1;

      printHelp();
    }

    return code;
  }

  /**
   * Check if the help option is present in the given args.
   * 
   * <p>
   * There is a cohabitation issue between help and required options... See stackoverflow.com:
   * "apache-cli-required-options-contradicts-with-help-option" for more information.
   * </p>
   * 
   * @param args Command line arguments.
   * @return {@code true} if the help option is present, {@code false} otherwise.
   */
  public static boolean checkForHelp(final String... args) {
    boolean hasHelp = false;

    final Options options = new Options();

    try {
      options.addOption(HELP);

      final CommandLineParser parser = new DefaultParser();

      final CommandLine cmd = parser.parse(options, args);

      hasHelp = cmd.hasOption(HELP.getOpt());

    } catch (ParseException e) {
      if (LOG.isTraceEnabled()) {
        LOG.trace(
            "Help command line parsing failed: " + e.getLocalizedMessage());
      }
    }

    return hasHelp;
  }

  /**
   * Read the command line.
   * 
   * @param line the command line.
   * 
   * @return the command line return code.
   */
  private static int readCommandLine(final CommandLine line) {
    int code = 0;

    if (line.hasOption(HASH.getOpt()) || line.hasOption(CHECK.getOpt())
        || line.hasOption(RANDOM.getOpt())) {
      final boolean quiet = line.hasOption(QUIET.getOpt());

      if (line.hasOption(RANDOM.getOpt())) {

        final char[] rand = Passwords.generateRandomPassword();
        if (line.hasOption(HASH.getOpt())) {
          System.out.println(Passwords.getHash(rand));
        } else {
          System.out.println(rand);
        }

      } else if (line.hasOption(CHECK.getOpt())) {

        if (!line.hasOption(HASH.getOpt())) {
          if (!quiet && LOG.isErrorEnabled()) {
            LOG.error("A hashed password must be provided through the \"" + HASH
                + "\" option");
          }
          code = -1;
        } else {
          final char[] password =
              line.getOptionValue(CHECK.getOpt()).toCharArray();
          final String expectedHash = line.getOptionValue(HASH.getOpt());

          final boolean matches =
              Passwords.isExpectedPassword(password, expectedHash);
          System.out.println(matches);

          code = matches ? 0 : -1;
        }

      } else if (line.hasOption(HASH.getOpt())) {

        final char[] password =
            line.getOptionValue(HASH.getOpt()).toCharArray();

        System.out.println(Passwords.getHash(password));

      }

    } else {
      if (!line.hasOption(HELP.getOpt())) {
        code = -1;
      }
      printHelp();
    }

    return code;
  }

  /**
   * Automatically generate and display on console the help statement.
   */
  private static void printHelp() {
    final HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(USAGE, OPTIONS, true);
  }
}
