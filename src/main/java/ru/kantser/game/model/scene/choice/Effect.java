package ru.kantser.game.model.scene.choice;

public class Effect {
    // изменения: положительные или отрицательные (в минутах/единицах)
    private Integer freeMinutesDelta; // изменение свободного времени в минутах (может быть + / -)
    private Integer energyDelta;      // изменение энергии (может быть + / -)
    private String note;              // короткое текстовое описание эффекта, если нужно

    public Effect() {}

    public Integer getFreeMinutesDelta() { return freeMinutesDelta; }
    public void setFreeMinutesDelta(Integer freeMinutesDelta) { this.freeMinutesDelta = freeMinutesDelta; }

    public Integer getEnergyDelta() { return energyDelta; }
    public void setEnergyDelta(Integer energyDelta) { this.energyDelta = energyDelta; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
