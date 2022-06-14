package com.losacabaos.skillsharing.request;

import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class RequestValidator implements Validator {
    @Override
    public boolean supports(@NotNull Class<?> aClass) {
        return Request.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(@NotNull Object o, @NotNull Errors errors) {
        Request request = (Request) o;

        if (request.getDescription().length() == 0 || request.getDescription().trim().length() == 0)
            errors.rejectValue("description", "no description","Description is required");

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();
        if (endDate != null && endDate.compareTo(startDate) <= 0)
            errors.rejectValue("endDate", "consistency",
                    "The end date must be after the start date");
        if (startDate.compareTo(LocalDate.now()) < 0)
            errors.rejectValue("startDate", "consistency",
                    "The start date must be today or after");
    }
}
