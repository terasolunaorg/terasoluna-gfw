package org.terasoluna.gfw.common.codepoints.validator;

import java.util.List;

import org.terasoluna.gfw.common.codepoints.ConsistOf;

public class Name_Collection {
    private List<@ConsistOf(AtoF.class) String> firstNames;

    private List<@ConsistOf(AtoF.class) String> lastNames;

    public Name_Collection(List<String> firstNames, List<String> lastNames) {
        this.firstNames = firstNames;
        this.lastNames = lastNames;
    }

    public Name_Collection() {
    }

    public List<String> getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(List<String> firstNames) {
        this.firstNames = firstNames;
    }

    public List<String> getLastNames() {
        return lastNames;
    }

    public void setLastNames(List<String> lastNames) {
        this.lastNames = lastNames;
    }
}
