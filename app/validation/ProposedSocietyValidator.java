package validation;

import javax.validation.ValidatorFactory;
import javax.validation.spi.ConfigurationState;

import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProposedSocietyValidator extends HibernateValidator {
    private static final Logger LOG = LoggerFactory.getLogger(ProposedSocietyValidator.class);

    public ProposedSocietyValidator() {
        LOG.info("Loading ProposedSociety custom validator.");
    }

    @Override
    public ValidatorFactory buildValidatorFactory(ConfigurationState configurationState) {
        return new ProposedSocietyValidatorFactory(configurationState);
    }
}
