package me.audeck.challengemod.cardinal;

import dev.onyxstudios.cca.api.v3.component.level.LevelComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.component.level.LevelComponentInitializer;
import me.audeck.challengemod.cardinal.components.ChallengePropertiesComponent;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.util.Identifier;

public class LevelComponents implements LevelComponentInitializer {
    //public static final ComponentType<SpeedrunEligibleComponent> SPEEDRUN_ELIGIBLE = ComponentRegistry.INSTANCE.registerStatic(new Identifier("audeck:speedrun_eligible"), SpeedrunEligibleComponent.class);
    public static final ComponentType<ChallengePropertiesComponent> CHALLENGE_PROPERTIES = ComponentRegistry.INSTANCE.registerStatic(new Identifier("audeck:challenge_properties"), ChallengePropertiesComponent.class);

    @Override
    public void registerLevelComponentFactories(LevelComponentFactoryRegistry registry) {
        //registry.register(SPEEDRUN_ELIGIBLE, properties -> new SpeedrunEligibleComponent());
        registry.register(CHALLENGE_PROPERTIES, properties -> new ChallengePropertiesComponent());
    }
}
