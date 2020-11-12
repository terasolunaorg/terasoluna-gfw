package org.terasoluna.gfw.common.validator.constraints;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.BeforeClass;
import org.junit.Test;
import org.terasoluna.gfw.common.validator.constraints.ByteMaxTest.ByteMaxTestForm;
import org.terasoluna.gfw.common.validator.constraints.ByteMinTest.ByteMinTestForm;
import org.terasoluna.gfw.common.validator.constraints.ByteSizeTest.ByteSizeTestForm;
import org.terasoluna.gfw.common.validator.constraints.CompareTest.CompareTestForm;

/**
 * Test class of {@code ContributorValidationMessages_ja.properties}
 */
public class ContributorValidationMessagesJaTest {

    private static Validator validator;

    @BeforeClass
    public static void beforeClass() {
        Locale.setDefault(Locale.JAPANESE);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * expect {@link ByteMin} japanese message.
     */
    @Test
    public void testMessageByteMin() {

        ByteMinTest.ByteMinTestForm form = new ByteMinTest.ByteMinTestForm();
        form.setStringProperty("あaa");

        Set<ConstraintViolation<ByteMinTestForm>> violations = validator
                .validate(form);
        assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                String.format("%d バイト以上のサイズにしてください", 6)))));
    }

    /**
     * expect {@link ByteMax} japanese message.
     */
    @Test
    public void testMessageByteMax() {

        ByteMaxTest.ByteMaxTestForm form = new ByteMaxTest.ByteMaxTestForm();
        form.setStringProperty("ああa");

        Set<ConstraintViolation<ByteMaxTestForm>> violations = validator
                .validate(form);
        assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                String.format("%d バイト以下のサイズにしてください", 6)))));
    }

    /**
     * expect {@link ByteSize} japanese message.
     */
    @Test
    public void testMessageByteSize() {

        ByteSizeTest.ByteSizeTestForm form = new ByteSizeTest.ByteSizeTestForm();
        form.setStringProperty("aa");

        Set<ConstraintViolation<ByteSizeTestForm>> violations = validator
                .validate(form);
        assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                String.format("%d から %d バイトの間のサイズにしてください", 3, 6)))));
    }

    /**
     * expect {@link Compare} japanese message.
     */
    @Test
    public void testMessageCompare() {

        CompareTest.CompareTestForm form = new CompareTest.CompareTestForm();
        form.setLeft(100);
        form.setRight(99);

        Set<ConstraintViolation<CompareTestForm>> violations = validator
                .validate(form);
        assertThat(violations, containsInAnyOrder(hasProperty("message", is(
                String.format("正しくない %s と %s の組合せです", "left", "right")))));
    }

}
