package org.terasoluna.gfw.common.codepoints.validator;

import java.util.List;

import org.terasoluna.gfw.common.codepoints.ConsistOf;

public class Name_Collection {
    private List<@ConsistOf(AtoF.class) String> firstNameList;

    private List<@ConsistOf(AtoF.class) String> lastNameList;

    public Name_Collection(List<String> firstNameList,
            List<String> lastNameList) {
        this.firstNameList = firstNameList;
        this.lastNameList = lastNameList;
    }

    public Name_Collection() {
    }

    public List<String> getFirstNameList() {
        return firstNameList;
    }

    public void setFirstNameList(List<String> firstNameList) {
        this.firstNameList = firstNameList;
    }

    public List<String> getLastNameList() {
        return lastNameList;
    }

    public void setLastNameList(List<String> lastNameList) {
        this.lastNameList = lastNameList;
    }
}
