package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.pet.*;
import seedu.address.model.pet.Pet;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Pet objects.
 */
public class PetBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_OWNERNAME = "Emma Lee";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Phone phone;
    private OwnerName ownerName;
    private Address address;
    private Set<Tag> tags;

    /**
     * Creates a {@code PetBuilder} with the default details.
     */
    public PetBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        ownerName = new OwnerName(DEFAULT_OWNERNAME);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PetBuilder with the data of {@code petToCopy}.
     */
    public PetBuilder(Pet petToCopy) {
        name = petToCopy.getName();
        phone = petToCopy.getPhone();
        ownerName = petToCopy.getOwnerName();
        address = petToCopy.getAddress();
        tags = new HashSet<>(petToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Pet} that we are building.
     */
    public PetBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Pet} that we are building.
     */
    public PetBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Pet} that we are building.
     */
    public PetBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Pet} that we are building.
     */
    public PetBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code OwnerName} of the {@code Pet} that we are building.
     */
    public PetBuilder withOwnerName(String ownerName) {
        this.ownerName = new OwnerName(ownerName);
        return this;
    }

    public Pet build() {
        return new Pet(name, phone, ownerName, address, tags);
    }

}
