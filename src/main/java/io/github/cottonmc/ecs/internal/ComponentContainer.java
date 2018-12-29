package io.github.cottonmc.ecs.internal;


import io.github.cottonmc.ecs.api.Component;
import io.github.cottonmc.ecs.api.ComponentWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class ComponentContainer implements ComponentContainerInterface {
    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    private Map<String, AttributeEntry> attributes;

    public ComponentContainer() {
        attributes = new HashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getAttribute(String key, Class<T> type) {
        if (attributes.containsKey(key)) {
            AttributeEntry attribute = attributes.get(key);
            if (type == attribute.type) {
                return Optional.of((T)attribute.getAttribute());
            } else {
                logger.warning("Invalid type " + attribute.getClass().getCanonicalName() + " for " + key);
            }
        }
        return Optional.empty();
    }

    @Override
    public void addAttribute(String key, Component attribute, Class type) {
        if (attributes.containsKey(key)) {
            if (attribute instanceof ComponentWrapper) {
                final AttributeEntry existingAttribute = attributes.get(key);
                if(attribute.getClass().isInstance(existingAttribute)){
                    ((ComponentWrapper) attribute).wrap(existingAttribute.attribute);
                    attributes.put(key,new AttributeEntry(attribute, type));
                }else{
                    logger.warning("Attribute type mismatch "+type.getCanonicalName() + " is not matching "+existingAttribute.type.getCanonicalName()+". Changes have been discarded.");
                }
            } else {
                logger.warning(attribute.getClass().getCanonicalName() + " can not wrap other attachments, and the key is already in use. Changes have been discarded.");
            }
        } else {
            attributes.put(key, new AttributeEntry(attribute, type));
        }
    }

    @Override
    public boolean removeAttribute(String key){
        return attributes.remove(key) != null;
    }

    class AttributeEntry{
        private final Component attribute;
        private final Class type;

        /**
         * @param attribute the object we want to use
         * @param type  the type that we're going to treat our attribute. MUST BE AN INTERFACE!
         * */
        AttributeEntry(Component attribute, Class type){
            this.type = type;
            if(!type.isInterface())
                throw new ComponentException(type.getCanonicalName()+" is not an interface!");

            if(type.isInstance(attribute)){
                this.attribute = attribute;
            }else{
                throw new ComponentException(attribute.getClass().getCanonicalName()+" is not an instance of "+type.getCanonicalName()+"!");
            }

        }

        public Component getAttribute() {
            return attribute;
        }

        public Class getType() {
            return type;
        }

        @Override
        public String toString() {
            return "AttributeEntry{" +
                    "attribute=" + attribute +
                    ", type=" + type +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AttributeEntry that = (AttributeEntry) o;
            return Objects.equals(getAttribute(), that.getAttribute()) &&
                    Objects.equals(getType(), that.getType());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getAttribute(), getType());
        }
    }

}
