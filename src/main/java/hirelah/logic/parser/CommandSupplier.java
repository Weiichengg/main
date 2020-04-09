package hirelah.logic.parser;

import hirelah.logic.commands.Command;
import hirelah.logic.parser.exceptions.ParseException;

/*
 * CommandSupplier
 *
 * CS2103 AY19/20 Semester 2
 * Team Project
 * HireLah!
 *
 * 13 Mar 2020
 *
 */

/**
 * CommandSupplier is a functional interface to abstract the supplying
 * of the command after parsing the command word.
 */

@FunctionalInterface
public interface CommandSupplier {
    Command getCommand(String args) throws ParseException;
}
