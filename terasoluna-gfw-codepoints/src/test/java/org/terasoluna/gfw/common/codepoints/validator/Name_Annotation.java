package org.terasoluna.gfw.common.codepoints.validator;

public class Name_Annotation {
    @AsciiPrintable
    private String firstName;

    @AsciiPrintable
    private String lastName;

    public Name_Annotation(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Name_Annotation() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
