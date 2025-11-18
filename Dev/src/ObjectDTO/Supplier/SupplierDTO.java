package ObjectDTO.Supplier;



import java.util.ArrayList;
import java.util.List;

public class SupplierDTO {
    private String id;
    private String name;
    private List<ContactDTO> contact;
    private String bankAccount;
    private DaysDTO daySup;
    private boolean Fixed_delivery_days;

    public SupplierDTO(String id, String name, String bankAccount, List<ContactDTO> contact, DaysDTO daySup) {
        this.id = id;
        this.name = name;
        this.contact = (contact == null) ? new ArrayList<>() : new ArrayList<>(contact);
        this.bankAccount = bankAccount;
        this.Fixed_delivery_days = (daySup != null);
        this.daySup = daySup;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ContactDTO> getContact() {
        return contact;
    }

    public void setContact(List<ContactDTO> contact) {
        this.contact = (contact == null) ? new ArrayList<>() : new ArrayList<>(contact);
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public DaysDTO getDaySup() {
        return daySup;
    }

    public void setDaySup(DaysDTO daySup) {
        this.daySup = daySup;
    }

    public boolean getFixed_delivery_days() {
        return Fixed_delivery_days;
    }

    public void setFixed_delivery_days(boolean fixed_delivery_days) {
        Fixed_delivery_days = fixed_delivery_days;
    }

    @Override
    public String toString() {
        return "SupplierDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", daySup=" + daySup + '\'' +
                ", contacts=" + contact +
                '}';
    }
}
