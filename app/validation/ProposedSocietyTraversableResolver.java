package validation;

import java.lang.annotation.ElementType;

import javax.validation.Path;
import javax.validation.TraversableResolver;
import javax.validation.Path.Node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProposedSocietyTraversableResolver implements TraversableResolver {
	private static final Logger LOG = LoggerFactory.getLogger(ProposedSocietyTraversableResolver.class);
    private final TraversableResolver delegate;
    private Class<?> targetGroup;
    public ProposedSocietyTraversableResolver(TraversableResolver delegate,Class<?> groups) {
        super();
        this.delegate = delegate;
        this.targetGroup = groups;
    }
    public ProposedSocietyTraversableResolver(TraversableResolver delegate){
    		this.delegate = delegate;
    }
    @Override
    public boolean isReachable(Object traversableObject, Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject,
            ElementType elementType) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("isReachable() invoked");
        }
        return delegate.isReachable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType);
    }

    @Override
    public boolean isCascadable(Object traversableObject, Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject,
            ElementType elementType) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("isCascadable() invoked with obj: " + traversableObject + " class: " + rootBeanType.getName() + " property: " + traversableProperty.getName());
        }
        
        if (traversableObject == null) {
            return false;
        }

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Checking cascade on: " + traversableProperty.getName());
            }
            
            OptionalCascade optionalCascade = traversableObject.getClass().getField(traversableProperty.getName()).getAnnotation(OptionalCascade.class);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Found property " + traversableProperty.getName() + " field object");
            }
            try {
                if (optionalCascade != null) {
                    Condition condition = optionalCascade.condition().newInstance();
                    Class<?>[] groups = optionalCascade.groups();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Found condition: " + condition.shouldCascade(traversableObject));
                    }
                    
                    //Execute only if the group is given
                    if( groups != null && targetGroup != null){
                    	if(hasMatchingGroup(groups,targetGroup)){
                    		return condition.shouldCascade(traversableObject)
                                    && delegate.isCascadable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType);
                        }else{
                        	return false;
                        }
                    }
                    if(groups.length != 0 && targetGroup == null){
                    	return false;
                    }
                    //Execute only if group is not given
                    return condition.shouldCascade(traversableObject)
                            && delegate.isCascadable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType);
                    
                }
            } catch (InstantiationException ie) {
                throw new RuntimeException("Failed to instantiate condition class: " + optionalCascade.condition().getName()
                        + " while validation bean of type: " + rootBeanType.getName(), ie);
            } catch (IllegalAccessException iae) {
                throw new RuntimeException("Failed to instantiate condition class: " + optionalCascade.condition().getName()
                        + " while validating bean of type:" + rootBeanType.getName() + " Make sure you have correct security policy set.", iae);
            }

        } catch (SecurityException se) {
            throw new RuntimeException("Could not access annotation information on property " + traversableProperty.getName() + " on class "
                    + rootBeanType.getName(), se);
        } catch (NoSuchFieldException e) {
            // Should never happen!
        	LOG.warn("Field not found! " + traversableProperty.toString() + " root bean type: " + rootBeanType.getName() + " traversable object type: " + traversableObject.getClass().getName());
            return false;
        }

        return delegate.isCascadable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType);
    }

    
	public  boolean hasMatchingGroup(Class<?>[] groups,Class<?> targetGroup) {
		
			for(int i = 0 ; i < groups.length ;i++){
				if(groups[i].getName().equals(targetGroup.getName())){
					LOG.debug("Found Target Group  " +targetGroup.getName());
					LOG.debug("Found Source Group  " +groups[i].getName());
					return true ;
				} 
			}
		return false;
	}
}
