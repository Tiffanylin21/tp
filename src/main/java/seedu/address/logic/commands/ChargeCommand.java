package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.attendance.AttendanceEntry;
import seedu.address.model.charge.Charge;
import seedu.address.model.pet.AttendanceHashMap;
import seedu.address.model.pet.Name;
import seedu.address.model.pet.Pet;


/**
 * Computes a month's charge of a pet identified using it's displayed index from the address book.
 */
public class ChargeCommand extends Command {

    public static final String COMMAND_WORD = "charge";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Computes a month's charge of the pet identified "
            + "by the index number used in the last pet listing.\n"
            + "Parameters: INDEX (must be a positive integer)"
            + "m/[MM-yyyy] c/[COST]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "m/03-2022 c/200";
    public static final String MESSAGE_INVALID_DATE_FORMAT = "Charge date should be formatted as MM-yyyy!";

    public static final String MESSAGE_COMPUTE_CHARGE_SUCCESS = "%s should be charged $%.2f for the month of %s.";
    private final Index index;
    private final YearMonth chargeDate;
    private final Charge charge;

    /**
     * @param index of the pet in the filtered pets list to compute the charges of.
     * @param chargeDate the month in the specified year to calculate amount chargeable.
     */
    public ChargeCommand(Index index, YearMonth chargeDate, Charge charge) {
        requireAllNonNull(index, chargeDate);
        this.index = index;
        this.chargeDate = chargeDate;
        this.charge = charge;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Pet> lastShownList = model.getFilteredPetList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
        }

        Pet petToCharge = lastShownList.get(index.getZeroBased());
        double amountChargeable = 0.0;
        // check current charge
        if (this.charge.getCharge() == null) {
            throw new CommandException(String.format(Messages.MESSAGE_NO_CHARGE_SET, ChargeCommand.MESSAGE_USAGE));
        }
        // calculate charge based on number of days in month
        AttendanceHashMap map = petToCharge.getAttendanceHashMap();
        LocalDate day = this.chargeDate.atDay(1);
        LocalDate firstDayNextMonth = this.chargeDate.atEndOfMonth().plusDays(1);
        while (day.isBefore(firstDayNextMonth)) {
            boolean hasAttendance = map.getAttendance(day).isPresent();
            if (hasAttendance) {
                AttendanceEntry entry = map.getAttendance(day).get();
                boolean isPresent = entry.getIsPresent().orElse(false);
                if (isPresent) {
                    amountChargeable += this.charge.getCharge();
                }
            }
            day = day.plusDays(1);
        }

        return new CommandResult(generateSuccessMessage(petToCharge.getName(),
                amountChargeable, getMonthName()));
    }

    /**
     * Returns the month to charge the pet on
     * @return month as a String.
     */
    public String getMonthName() {
        DateFormatSymbols symbol = new DateFormatSymbols();
        return symbol.getMonths()[this.chargeDate.getMonthValue() - 1];
    }

    /**
     * Generates a command execution success message
     * {@code petToEdit}.
     */
    private String generateSuccessMessage(Name petName, double amountChargeable, String month) {
        return String.format(MESSAGE_COMPUTE_CHARGE_SUCCESS, petName.toString(), amountChargeable, month);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ChargeCommand)) {
            return false;
        }

        // state check
        ChargeCommand e = (ChargeCommand) other;
        return index.equals(e.index)
                && chargeDate.equals(e.chargeDate)
                && charge.equals(e.charge);
    }

}
