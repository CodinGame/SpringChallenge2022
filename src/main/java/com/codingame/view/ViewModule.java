package com.codingame.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.codingame.game.Referee;
import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.Module;
import com.codingame.view.FrameViewData.EntityData;
import com.google.inject.Inject;

public class ViewModule implements Module {

    private GameManager<AbstractPlayer> gameManager;
    private Referee referee;

    @Inject
    ViewModule(GameManager<AbstractPlayer> gameManager) {
        this.gameManager = gameManager;
        gameManager.registerModule(this);
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    @Override
    public final void onGameInit() {
        sendGlobalData();
        sendFrameData();
    }

    private void sendFrameData() {
        FrameViewData data = referee.getCurrentFrameData();
        gameManager.setViewData("graphics", serialize(data));
    }

    private void sendGlobalData() {
        GlobalViewData data = referee.getGlobalData();
        gameManager.setViewGlobalData("graphics", serialize(data));

    }

    @Override
    public final void onAfterGameTurn() {
        sendFrameData();
    }

    @Override
    public final void onAfterOnEnd() {
        sendFrameData();
    }

    private String serialize(GlobalViewData data) {
        List<String> lines = new ArrayList<>();
        lines.add(
            Referee.join(
                data.width,
                data.height,
                data.baseAttractionRadius,
                data.baseRadius,
                data.baseViewRadius,
                data.heroViewRadius,
                data.heroesPerPlayer,
                data.enableSpells ? 1 : 0,
                data.enableFog ? 1 : 0
            )
        );
        lines.add(serialize(data.basePositions.get(0)));
        lines.add(serialize(data.basePositions.get(1)));
        return lines.stream().collect(Collectors.joining("\n"));
    }

    private String serialize(Coord coord) {
        return coord == null ? "0 0" : coord.x + " " + coord.y;
    }

    private String serialize(FrameViewData data) {
        List<String> lines = new ArrayList<>();
        // States
        lines.add(serializeIntToCoord(data.positions));
        lines.add(serializeIntToText(data.messages));
        lines.add(serializeIntList(data.controlled));
        lines.add(serializeIntList(data.pushed));
        lines.add(serializeIntList(data.shielded));
        // Diffs
        lines.add(serializeIntToString(data.mana));
        lines.add(serializeIntToString(data.baseHealth));
        lines.add(serializeIntToString(data.mobHealth));
        // Events
        lines.add(serializeSpawnList(data.spawns));
        lines.add(serializeAttackList(data.attacks));
        lines.add(serializeBaseAttackList(data.baseAttacks));
        lines.add(serializeSpellUseList(data.spellUses));
        // End of data marker (to avoid trims) 
        if (data.spellUses.isEmpty()) {
            lines.add("X");
        }

        return lines.stream().collect(Collectors.joining("\n"));
    }

    private String serializeIntToText(Map<Integer, String> messages) {
        return messages.entrySet().stream()
            .map(e -> e.getKey() + ";" + e.getValue())
            .collect(Collectors.joining(";"));
    }

    private String serializeSpellUseList(List<SpellUse> spellUses) {
        return spellUses.stream().map(e -> serialize(e)).collect(Collectors.joining(" "));
    }

    private String serialize(SpellUse e) {
        return Referee.join(e.hero, e.spell, e.target, serialize(e.destination));
    }

    private String serializeBaseAttackList(List<BaseAttack> baseAttacks) {
        return baseAttacks.stream().map(e -> serialize(e)).collect(Collectors.joining(" "));
    }

    private String serialize(BaseAttack e) {
        return Referee.join(e.player, e.mob);
    }

    private String serializeAttackList(List<Attack> attacks) {
        return attacks.stream().map(e -> serialize(e)).collect(Collectors.joining(" "));
    }

    private String serialize(Attack a) {
        return Referee.join(a.hero, serializeIntList(a.mobs, ";"));
    }

    private String serializeSpawnList(List<EntityData> spawns) {
        return spawns.stream().map(e -> serialize(e)).collect(Collectors.joining(" "));
    }

    private String serialize(EntityData e) {
        return Referee.join(e.id, e.type, e.health);
    }

    private <T> String serializeIntToString(Map<Integer, T> mana) {
        return mana.entrySet().stream()
            .map(e -> Referee.join(e.getKey(), e.getValue()))
            .collect(Collectors.joining(" "));
    }

    private String serializeIntList(List<Integer> ints) {
        return serializeIntList(ints, " ");
    }

    private String serializeIntList(List<Integer> ints, String delimiter) {
        return ints.stream().map(String::valueOf).collect(Collectors.joining(delimiter));
    }

    private String serializeIntToCoord(Map<Integer, Coord> intToCoord) {
        return intToCoord.entrySet().stream()
            .map(e -> Referee.join(e.getKey(), serialize(e.getValue())))
            .collect(Collectors.joining(" "));
    }

}
