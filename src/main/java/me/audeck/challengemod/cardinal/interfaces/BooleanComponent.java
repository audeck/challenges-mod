package me.audeck.challengemod.cardinal.interfaces;

import nerdhub.cardinal.components.api.component.Component;

public interface BooleanComponent extends Component {
    boolean getValue();
    void setValue(boolean value);
    void flipValue();
}
