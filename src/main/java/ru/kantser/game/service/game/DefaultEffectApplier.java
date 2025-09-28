package ru.kantser.game.service.game;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.model.scene.choice.Effect;
import ru.kantser.game.model.state.PlayerState;


@Getter
@Setter
public class DefaultEffectApplier implements EffectApplier {
    private static final Logger log = LoggerFactory.getLogger(DefaultEffectApplier.class);

    @Override
    public void apply(Effect effect, PlayerState playerState) {
        if (effect == null || playerState == null) {
            return;
        }

        if (effect.getFreeMinutesDelta() != null) {
            int cur = playerState.getFreeMinutes();
            int delta = effect.getFreeMinutesDelta();
            playerState.setFreeMinutes(cur + delta);
            log.debug("Applied freeMinutesDelta: {} -> {} (delta={})", cur, playerState.getFreeMinutes(), delta);
        }

        if (effect.getEnergyDelta() != null) {
            int cur = playerState.getEnergy();
            int delta = effect.getEnergyDelta();
            playerState.setEnergy(cur + delta);
            log.debug("Applied energyDelta: {} -> {} (delta={})", cur, playerState.getEnergy(), delta);
        }

        if (effect.getNote() != null) {
            log.debug("Effect note: {}", effect.getNote());
        }
    }
}
