package domainLayer.Supplier.Objects;

import ObjectDTO.Supplier.ContactDTO;

//todo:check maybe you ba3sta the id to string
public class Contact{
    private String id;
    private String name;
    private String phone;
    private String email;

    public Contact(String id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Contact(ContactDTO contactDTO) {

        this.id = contactDTO.getId();

        this.name = contactDTO.getName();
        this.phone = contactDTO.getPhoneNumber();
        this.email = contactDTO.getEmail();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        return id == contact.id;
    }


    public boolean Strongequals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        return id == contact.id &&
                name.equals(contact.name) &&
                phone.equals(contact.phone) &&
                email.equals(contact.email);
    }

    //convert this contact to dto
    public ContactDTO ConvertToDTO() {
        return new ContactDTO(
                String.valueOf(this.id),
                this.name,
                this.phone,
                this.email
        );
    }

}