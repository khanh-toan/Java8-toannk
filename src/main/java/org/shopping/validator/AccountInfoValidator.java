package org.shopping.validator;


import lombok.RequiredArgsConstructor;
import org.shopping.form.AccountInfoForm;
import org.shopping.service.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class AccountInfoValidator implements Validator {
    private final AccountService accountService;
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == AccountInfoForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountInfoForm accountInfoForm = (AccountInfoForm) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "NotEmpty.accountInfoForm.address");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.accountInfoForm.username");
        if(accountInfoForm.getFileData().isEmpty()) {
            errors.rejectValue("fileData", "NotEmpty.accountInfoForm.fileData");
        }
    }

}
