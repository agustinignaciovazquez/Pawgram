package ar.edu.itba.pawgram.webapp.form.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;
// based on https://stackoverflow.com/questions/40482252/validation-of-a-date-with-hibernate
public class DateRuleValidator implements ConstraintValidator<DateRule, Date> {

    public final void initialize(final DateRule annotation) {}

    public final boolean isValid(final Date value,
                                 final ConstraintValidatorContext context) {
        if(value == null)
            return false;

        // Only use the date for comparison
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        calendar.add(Calendar.YEAR, -1);
        Date previousYear = calendar.getTime();

        // Your date must be after previous year and before tomorrow
        return value.after(previousYear) && value.before(tomorrow);

    }
}
