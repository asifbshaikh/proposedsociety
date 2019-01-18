package validation;

import javax.validation.TraversableResolver;
import javax.validation.spi.ConfigurationState;

import org.hibernate.validator.internal.engine.ValidatorFactoryImpl;

public class ProposedSocietyValidatorFactory extends ValidatorFactoryImpl {
    private TraversableResolver resolver;
    public ProposedSocietyValidatorFactory(ConfigurationState configurationState) {
        super(configurationState);
    }

    @Override
    public TraversableResolver getTraversableResolver() {
        if (resolver == null) {
            resolver = new ProposedSocietyTraversableResolver(super.getTraversableResolver());
        }
        return resolver;
    }
}
