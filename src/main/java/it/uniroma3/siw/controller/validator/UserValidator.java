package it.uniroma3.siw.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import it.uniroma3.siw.model.User;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // Logica di validazione per l'utente, se necessaria (es. nome e cognome non vuoti, ecc.)
        // Attualmente le annotazioni @NotBlank nel modello gestiscono le validazioni base.
    }
}
