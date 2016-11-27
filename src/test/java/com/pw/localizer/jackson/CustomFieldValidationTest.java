package com.pw.localizer.jackson;

import static org.junit.Assert.*;
import com.pw.localizer.model.dto.UserDTO;
import org.junit.Test;

/**
 * Created by Patryk on 2016-11-07.
 */

public class CustomFieldValidationTest {

    private CustomFieldsValidation customFieldsValidation = new CustomFieldsValidation();

    @Test
    public void shouldReturnTrue() {
        assertTrue(customFieldsValidation.validateField(UserDTO.class, "id"));
    }

    @Test
    public void shouldReturnFalse() {
        assertFalse(customFieldsValidation.validateField(UserDTO.class, "id_invalid"));
    }
}
