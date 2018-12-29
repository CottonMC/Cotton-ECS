package io.github.cottonmc.ecs.api;

public interface ComponentWrapper extends Component {
    /**
     * Gives you the attribute that you're going to wrap.
     * */
    void wrap(Component attribute);

    /**
     * Return the attribute that you are wrapping.
     * YOU CAN NOT RETURN NULL!!
     * */
    Component getWrapped();
}
