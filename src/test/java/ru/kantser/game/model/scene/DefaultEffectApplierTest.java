package ru.kantser.game.model.scene;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kantser.game.model.scene.choice.Effect;
import ru.kantser.game.model.state.PlayerState;
import ru.kantser.game.service.game.DefaultEffectApplier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultEffectApplierTest {

    @InjectMocks
    private DefaultEffectApplier effectApplier;

    @Mock
    private PlayerState playerState;

    @Test
    void apply_shouldHandleNullEffect() {
        // When
        effectApplier.apply(null, playerState);

        // Then
        verifyNoInteractions(playerState);
    }

    @Test
    void apply_shouldHandleNullPlayerState() {
        // Given
        Effect effect = new Effect();
        effect.setFreeMinutesDelta(10);

        // When
        effectApplier.apply(effect, null);

        // Then - no exception should be thrown
        assertDoesNotThrow(() -> effectApplier.apply(effect, null));
    }

    @Test
    void apply_shouldUpdateFreeMinutes() {
        // Given
        Effect effect = new Effect();
        effect.setFreeMinutesDelta(5);

        when(playerState.getFreeMinutes()).thenReturn(10);

        // When
        effectApplier.apply(effect, playerState);

        // Then
        verify(playerState).setFreeMinutes(15);
    }

    @Test
    void apply_shouldUpdateEnergy() {
        // Given
        Effect effect = new Effect();
        effect.setEnergyDelta(-3);

        when(playerState.getEnergy()).thenReturn(20);

        // When
        effectApplier.apply(effect, playerState);

        // Then
        verify(playerState).setEnergy(17);
    }

    @Test
    void apply_shouldHandleNoteWithoutStateModification() {
        // Given
        Effect effect = new Effect();
        effect.setNote("Test note");

        // When
        effectApplier.apply(effect, playerState);

        // Then - no state modifications should occur
        verify(playerState, never()).getFreeMinutes();
        verify(playerState, never()).getEnergy();
        verify(playerState, never()).setFreeMinutes(anyInt());
        verify(playerState, never()).setEnergy(anyInt());
    }

    @Test
    void apply_shouldHandleAllEffectsSimultaneously() {
        // Given
        Effect effect = new Effect();
        effect.setFreeMinutesDelta(2);
        effect.setEnergyDelta(5);
        effect.setNote("Combined effect");

        when(playerState.getFreeMinutes()).thenReturn(5);
        when(playerState.getEnergy()).thenReturn(10);

        // When
        effectApplier.apply(effect, playerState);

        // Then
        verify(playerState).setFreeMinutes(7);
        verify(playerState).setEnergy(15);
    }

    @Test
    void apply_shouldHandleNullDeltas() {
        // Given
        Effect effect = new Effect();
        effect.setFreeMinutesDelta(null);
        effect.setEnergyDelta(null);
        effect.setNote(null);

        // When
        effectApplier.apply(effect, playerState);

        // Then - no interactions should occur
        verify(playerState, never()).getFreeMinutes();
        verify(playerState, never()).getEnergy();
        verify(playerState, never()).setFreeMinutes(anyInt());
        verify(playerState, never()).setEnergy(anyInt());
    }

    @Test
    void apply_shouldHandleNegativeDeltas() {
        // Given
        Effect effect = new Effect();
        effect.setFreeMinutesDelta(-5);
        effect.setEnergyDelta(-10);

        when(playerState.getFreeMinutes()).thenReturn(8);
        when(playerState.getEnergy()).thenReturn(15);

        // When
        effectApplier.apply(effect, playerState);

        // Then
        verify(playerState).setFreeMinutes(3);
        verify(playerState).setEnergy(5);
    }

    @Test
    void apply_shouldHandleZeroDeltas() {
        // Given
        Effect effect = new Effect();
        effect.setFreeMinutesDelta(0);
        effect.setEnergyDelta(0);

        when(playerState.getFreeMinutes()).thenReturn(10);
        when(playerState.getEnergy()).thenReturn(20);

        // When
        effectApplier.apply(effect, playerState);

        // Then
        verify(playerState).setFreeMinutes(10); // 10 + 0 = 10
        verify(playerState).setEnergy(20); // 20 + 0 = 20
    }
}