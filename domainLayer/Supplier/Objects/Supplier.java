package domainLayer.Supplier.Objects;

import ObjectDTO.Supplier.ContactDTO;
import ObjectDTO.Supplier.DaysDTO;
import ObjectDTO.Supplier.SupplierDTO;

import java.util.ArrayList;
import java.util.List;

import static domainLayer.Supplier.Logger.printE;
import static domainLayer.Supplier.Logger.printSuccess;

public class Supplier {
    private String id;
    private String name;
    private List<Contact> contact; // or List<Contact>
    private String bankAccount;
    private Days daySup;
    private boolean Fixed_delivery_days;

    // Constructor using SupplierDTO
    public Supplier(SupplierDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("SupplierDTO cannot be null");
        }
        this.id = dto.getId();
        this.name = dto.getName();
        this.bankAccount = dto.getBankAccount();
        this.Fixed_delivery_days = dto.getFixed_delivery_days();
        if( dto.getDaySup()!=null)
            this.daySup = Days.valueOf(dto.getDaySup().toString());
        else
            this.daySup=null;
        if(dto.getContact()==null)
            this.contact=new ArrayList<>();
        else {
            ArrayList<Contact> contactsToAdd = new ArrayList<>();
            for (ContactDTO contactDTO : dto.getContact()) {
                contactsToAdd.add(new Contact(contactDTO));
            }
            this.contact = contactsToAdd;
        }
        //this.contact = (dto.getContact() == null) ? new ArrayList<>() : new ArrayList<>(dto.getContact());
    }

    public Supplier(String id, String name, String bankAccount, List<Contact> contact, Days daySup) {
        this.id = id;
        this.name = name;
        this.contact = (contact == null) ? new ArrayList<>() : new ArrayList<>(contact);
        this.bankAccount = bankAccount;
        this.Fixed_delivery_days = (daySup != null);
        this.daySup = daySup;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bank) {
        this.bankAccount = bank;
    }

    public boolean isFixed_delivery_days() {
        return Fixed_delivery_days;
    }

    public boolean getFixed_delivery_days() {
        return Fixed_delivery_days;
    }

    public void setFixed_delivery_days(boolean fixed_delivery_days) {
        Fixed_delivery_days = fixed_delivery_days;
    }

    public Days getDaySup() {
        return daySup;
    }

    public void setDaySup(Days daySup) {
        this.daySup = daySup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Contact> getContact() {
        return contact;
    }

    public void setContact(List<Contact> contact) {
        this.contact = (contact == null) ? new ArrayList<>() : new ArrayList<>(contact);
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", daySup=" + daySup +
                ", contacts=" + contact +
                '}';
    }

    public void addContact(List<Contact> contactsToAdd) {
        if (contactsToAdd == null || contactsToAdd.isEmpty()) {
            printE("No contacts to add.");
            return;
        }

        if (this.contact == null) {
            this.contact = new ArrayList<>();
        }

        List<Contact> duplicates = new ArrayList<>();
        for (Contact existing : this.contact) {
            if (contactsToAdd.contains(existing)) {
                printE("Contact already exists: " + existing);
                duplicates.add(existing);
            }
        }

        List<Contact> modifiableContactsToAdd = new ArrayList<>(contactsToAdd);
        modifiableContactsToAdd.removeAll(duplicates);

        if (modifiableContactsToAdd.isEmpty()) {
            printE("No new contacts were added.");
            return;
        }
        
        this.contact.addAll(modifiableContactsToAdd);
        printSuccess("Contact(s) added successfully.");
    }

    public void addContact(Contact newContact) {
        if (newContact == null) {
            return;
        }

        if (this.contact == null) {
            this.contact = new ArrayList<>();
        }

        this.contact.add(newContact);
    }

    public SupplierDTO ConvertToDTO() {
        ArrayList<ContactDTO> contactDTOlst = new ArrayList<>();
        for (Contact c : this.contact) {
            contactDTOlst.add(c.ConvertToDTO());
        }


        return new SupplierDTO(
                id,
                name,
                bankAccount,
                contactDTOlst,
                DaysDTO.fromString(daySup.toString())
        );

    }
}
