package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Predicate;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Person;

/**
 * Searches for clients who have appointments on the specified date.
 */
public class SearchAppointmentCommand extends Command {
    public static final String COMMAND_WORD = "search appointment";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Searches for clients who have appointments on the specified date.\n"
            + "Parameters: DATE (must be in yyyy-MM-dd format)\n"
            + "Example: " + COMMAND_WORD + " 2023-12-31";

    public static final String MESSAGE_SUCCESS = "Listed all clients with appointments on %s";

    private final String dateStr;

    public SearchAppointmentCommand(String dateStr) {
        requireNonNull(dateStr);
        this.dateStr = dateStr;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        requireNonNull(model);

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new CommandException("Invalid date format. Use 'yyyy-MM-dd'.");
        }

        Predicate<Person> predicate = person -> {
            Appointment appointment = person.getAppointmentDate();
            if (appointment == null) {
                return false; // No appointment, exclude this person
            }
            LocalDate appointmentDate = LocalDate.parse(appointment.toString(), formatter);
            return appointmentDate.equals(date);
        };

        model.updateFilteredPersonList(predicate);
        return new CommandResult(String.format(MESSAGE_SUCCESS, dateStr));
    }
}
