package com.losacabaos.skillsharing.offer;

import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class OfferValidator implements Validator {
    @Override
    public boolean supports(@NotNull Class<?> aClass) {
        return Offer.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(@NotNull Object o, @NotNull Errors errors) {
        Offer offer = (Offer) o;

        if (offer.getDescription().length() == 0 || offer.getDescription().trim().length() == 0)
            errors.rejectValue("description", "no description","Description is required");

        LocalDate startDate = offer.getStartDate();
        LocalDate endDate = offer.getEndDate();
        if (endDate != null && endDate.compareTo(startDate) <= 0)
            errors.rejectValue("endDate", "consistency",
                    "The finish date must be after the start date");
        if (startDate.compareTo(LocalDate.now()) < 0)
            errors.rejectValue("startDate", "consistency",
                    "The start date must be today or after");

    }
}
