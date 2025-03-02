package woofareyou.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static woofareyou.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import woofareyou.commons.core.GuiSettings;
import woofareyou.logic.commands.exceptions.CommandException;
import woofareyou.model.Model;
import woofareyou.model.PetBook;
import woofareyou.model.ReadOnlyPetBook;
import woofareyou.model.ReadOnlyUserPrefs;
import woofareyou.model.pet.Pet;
import woofareyou.testutil.PetBuilder;

public class AddCommandTest {

    @Test
    public void constructor_nullPet_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_petAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPetAdded modelStub = new ModelStubAcceptingPetAdded();
        Pet validPet = new PetBuilder().build();

        CommandResult commandResult = new AddCommand(validPet).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validPet), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPet), modelStub.petsAdded);
    }

    @Test
    public void execute_duplicatePet_throwsCommandException() {
        Pet validPet = new PetBuilder().build();
        AddCommand addCommand = new AddCommand(validPet);
        ModelStub modelStub = new ModelStubWithPet(validPet);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PET, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePetDifferentTag_throwsCommandException() {
        Pet validPet = new PetBuilder().withTags("Poodle").build();
        AddCommand addCommand = new AddCommand(new PetBuilder().build());
        ModelStub modelStub = new ModelStubWithPet(validPet);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PET, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePetDifferentDiet_throwsCommandException() {
        Pet validPet = new PetBuilder().withDiet("Vegetarian").build();
        AddCommand addCommand = new AddCommand(new PetBuilder().build());
        ModelStub modelStub = new ModelStubWithPet(validPet);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PET, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePetDifferentAppointment_throwsCommandException() {
        Pet validPet = new PetBuilder().withAppointment("05-05-2022 09:00", "NUS Vet Clinic").build();
        AddCommand addCommand = new AddCommand(new PetBuilder().build());
        ModelStub modelStub = new ModelStubWithPet(validPet);

        assertThrows(CommandException.class, AddCommand.MESSAGE_DUPLICATE_PET, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Pet alice = new PetBuilder().withName("Alice").build();
        Pet bob = new PetBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different pet -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getPetBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPetBookFilePath(Path petBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPet(Pet pet) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortPetList(String field) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPetBook(ReadOnlyPetBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyPetBook getPetBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPet(Pet pet) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePet(Pet target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPet(Pet target, Pet editedPet) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Predicate<Pet> getLastUsedPredicate() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Pet> getFilteredPetList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPetList(Predicate<Pet> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPetList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPetListToFullPetList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyPetBook undo() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single pet.
     */
    private class ModelStubWithPet extends ModelStub {
        private final Pet pet;

        ModelStubWithPet(Pet pet) {
            requireNonNull(pet);
            this.pet = pet;
        }

        @Override
        public boolean hasPet(Pet pet) {
            requireNonNull(pet);
            return this.pet.isSamePet(pet);
        }
    }

    /**
     * A Model stub that always accept the pet being added.
     */
    private class ModelStubAcceptingPetAdded extends ModelStub {
        final ArrayList<Pet> petsAdded = new ArrayList<>();

        @Override
        public boolean hasPet(Pet pet) {
            requireNonNull(pet);
            return petsAdded.stream().anyMatch(pet::isSamePet);
        }

        @Override
        public void addPet(Pet pet) {
            requireNonNull(pet);
            petsAdded.add(pet);
        }

        @Override
        public ReadOnlyPetBook getPetBook() {
            return new PetBook();
        }
    }

}
