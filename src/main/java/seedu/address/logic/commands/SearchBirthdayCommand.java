package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.Person;

/**
 * Searches for clients whose birthdays are on the specified date.
 */
public class SearchBirthdayCommand extends Command {
    public static final String COMMAND_WORD = "search birthday";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Searches for clients whose birthdays are on the specified date.\n"
            + "Parameters: DATE (must be in MM-dd format)\n"
            + "Example: " + COMMAND_WORD + " 04-25";

    public static final String MESSAGE_SUCCESS = "Listed all clients with birthdays on %s";
    private final String date;

    public SearchBirthdayCommand(String date) {
        requireNonNull(date);
        this.date = date;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Predicate<Person> predicate = person -> {
            Birthday birthday = person.getBirthday();
            if (birthday == null) {
                return false;
            }
            String birthdayMonthDay = birthday.toString().substring(5);
            return birthdayMonthDay.equals(date);
        };
        model.updateFilteredPersonList(predicate);
        return new CommandResult(String.format(MESSAGE_SUCCESS, date));
    }
}