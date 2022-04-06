package com.codingame.game;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.codingame.game.action.Action;
import com.codingame.game.action.ActionException;
import com.codingame.game.action.ActionType;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.view.Attack;
import com.codingame.view.BaseAttack;
import com.codingame.view.Coord;
import com.codingame.view.FrameViewData;
import com.codingame.view.FrameViewData.EntityData;
import com.codingame.view.GlobalViewData;
import com.codingame.view.SpellUse;
import com.codingame.view.ViewModule;
import com.codingame.view.endscreen.EndScreenModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Referee extends AbstractReferee {

    public static final int TYPE_MY_HERO = 0;
    public static final int TYPE_ENEMY_HERO = 1;
    public static final int TYPE_MOB = 2;
    public static final int INPUT_TYPE_MOB = 0;
    public static final int INPUT_TYPE_MY_HERO = 1;
    public static final int INPUT_TYPE_ENEMY_HERO = 2;

    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private ViewModule viewModule;
    @Inject private EndScreenModule endScreenModule;

    int playerCount;
    long seed;
    Random random;
    private List<Hero> allHeroes = new ArrayList<>();
    private List<Mob> allMobs = new ArrayList<>();
    private Set<Mob> mobRemovals = new HashSet<>();
    private MobSpawner mobSpawner;
    private List<GameEntity> newEntities = new ArrayList<>();
    private List<Attack> attacks = new ArrayList<>();
    private List<SpellUse> spellUses = new ArrayList<>();
    private List<BaseAttack> baseAttacks = new ArrayList<>();
    private Map<ActionType, List<Hero>> intentMap = new HashMap<>();
    private Map<Set<Vector>, Double> positionKeyMap = new HashMap<>();

    Vector[] corners = new Vector[] { new Vector(0, 0), new Vector(Configuration.MAP_WIDTH, Configuration.MAP_HEIGHT) };
    Vector[] startDirections = { new Vector(1, 1).normalize(), new Vector(-1, -1).normalize() };
    List<Vector> basePositions = new ArrayList<>();

    private Supplier<Stream<? extends GameEntity>> allEntities = () -> Stream.concat(allHeroes.stream(), allMobs.stream());
    private Vector symmetryOrigin;

    @Override
    public void init() {

        viewModule.setReferee(this);
        symmetryOrigin = new Vector(Configuration.MAP_WIDTH / 2, Configuration.MAP_HEIGHT / 2);
        this.seed = gameManager.getSeed();

        computeConfiguration(gameManager.getGameParameters());

        random = new Random(this.seed);

        mobSpawner = new MobSpawner(
            random,
            Configuration.MOB_SPAWN_LOCATIONS,
            Configuration.MOB_SPAWN_MAX_DIRECTION_DELTA,
            Configuration.MOB_SPAWN_RATE
        );

        try {
            playerCount = gameManager.getPlayerCount();

            for (ActionType type : ActionType.values()) {
                intentMap.put(type, new ArrayList<>());
            }
            initPlayers();

            gameManager.setFrameDuration(500);
            gameManager.setMaxTurns(220);
            gameManager.setTurnMaxTime(50);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Referee failed to initialize");
            abort();
        }
    }

    private void computeConfiguration(Properties gameParameters) {

        switch (gameManager.getLeagueLevel()) {
        case 1:
            // Wood 2
            Configuration.ENABLE_TIE_BREAK = false;
            Configuration.ENABLE_WIND = false;
            Configuration.ENABLE_CONTROL = false;
            Configuration.ENABLE_SHIELD = false;
            Configuration.ENABLE_FOG = false;
            break;
        case 2:
            // Wood 1
            Configuration.ENABLE_WIND = true;
            Configuration.ENABLE_CONTROL = false;
            Configuration.ENABLE_SHIELD = false;
            Configuration.ENABLE_FOG = true;
            break;
        }

        Configuration.take(gameParameters);
    }

    private void abort() {
        gameManager.endGame();

    }

    private Vector snapToGameZone(Vector v) {
        double snapX = v.getX();
        double snapY = v.getY();

        if (snapX < 0)
            snapX = 0;
        if (snapX >= Configuration.MAP_WIDTH)
            snapX = Configuration.MAP_WIDTH - 1;
        if (snapY < 0)
            snapY = 0;
        if (snapY >= Configuration.MAP_HEIGHT)
            snapY = Configuration.MAP_HEIGHT - 1;
        return new Vector(snapX, snapY);
    };

    /**
     * Computes the intersection between two segments.
     *
     * @param x1
     *            Starting point of Segment 1
     * @param y1
     *            Starting point of Segment 1
     * @param x2
     *            Ending point of Segment 1
     * @param y2
     *            Ending point of Segment 1
     * @param x3
     *            Starting point of Segment 2
     * @param y3
     *            Starting point of Segment 2
     * @param x4
     *            Ending point of Segment 2
     * @param y4
     *            Ending point of Segment 2
     * @return Vector where the segments intersect, or null if they don't
     */
    public Vector intersection(
        double x1, double y1, double x2, double y2,
        double x3, double y3, double x4, double y4
    ) {
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0) return null;

        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        Vector p = new Vector(xi, yi);
        if (xi < Math.min(x1, x2) || xi > Math.max(x1, x2)) return null;
        if (xi < Math.min(x3, x4) || xi > Math.max(x3, x4)) return null;
        return p;
    }

    public Vector intersection(Vector a, Vector b, Vector a2, Vector b2) {
        return intersection(
            a.getX(), a.getY(), b.getX(), b.getY(),
            a2.getX(), a2.getY(), b2.getX(), b2.getY()
        );
    }

    private void initPlayers() {
        // Generate heroes
        int spawnOffset = 1600;
        int spaceBetweenHeroes = 400;

        for (int i = 0; i < playerCount; ++i) {
            Player player = gameManager.getPlayer(i);
            Vector vector = (i < 2 ? new Vector(1, -1) : new Vector(1, 1)).normalize();
            if (i % 2 == 1) {
                vector = vector.mult(-1);
            }

            Vector startPoint = corners[i];

            basePositions.add(startPoint);
            double[] offsets = new double[] { 0, 1, -1, 2, -2, 3, -3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            for (int j = 0; j < Configuration.HEROES_PER_PLAYER; ++j) {
                double offset = offsets[j + (1 - Configuration.HEROES_PER_PLAYER % 2)];

                Vector position = vector.mult(offset * (spaceBetweenHeroes)).add(startPoint).add(startDirections[i].mult(spawnOffset))
                    .round();
                position = snapToGameZone(position);
                Hero hero = new Hero(j, position, player, startDirections[i].angle());
                player.addHero(hero);
                allHeroes.add(hero);
                newEntities.add(hero);
            }
        }
        sendGlobalInfo();
    }

    private void sendGlobalInfo() {
        for (Player player : gameManager.getActivePlayers()) {
            // <baseX> <baseY>
            player.sendInputLine(basePositions.get(player.getIndex()).toIntString());
            // <heroesPerPlayer>
            player.sendInputLine(String.valueOf(Configuration.HEROES_PER_PLAYER));
        }
    }

    @Override
    public void gameTurn(int turn) {
        resetGameTurnData();

        // Give input to players
        for (Player player : gameManager.getActivePlayers()) {
            sendGameStateFor(player);
            player.execute();
        }
        // Get output from players
        handlePlayerCommands();

        performGameUpdate(turn);

        for (Player player : gameManager.getPlayers()) {
            if (player.getBaseHealth() == 0) {
                player.deactivate("Base destroyed!");
            }
        }

        if (gameManager.getActivePlayers().size() < 2) {
            abort();
        }
    }

    private void performGameUpdate(int turn) {
        doControl();
        doShield();
        moveHeroes();
        Map<Player, Integer[]> manaGain = performCombat();
        doPush();
        moveMobs();
        shieldDecay();
        spawnNewMobs(turn);

        manaGain.forEach((player, amount) -> {
            player.gainMana(amount);
        });
    }

    private void shieldDecay() {
        allEntities.get().forEach(e -> {
            if (e.shieldDuration > 0) {
                e.shieldDuration--;
            }
        });
    }

    private void doPush() {
        Map<GameEntity, List<Vector>> directionMap = new HashMap<>();

        for (Hero hero : intentMap.get(ActionType.WIND)) {
            try {
                if (hero.owner.getMana() < Configuration.SPELL_WIND_COST) {
                    throw new ActionException("Not enough mana");
                }
                hero.owner.spendMana(Configuration.SPELL_WIND_COST);
                Action push = hero.intent;
                recordSpellUse(hero);

                Stream<? extends GameEntity> enemies = getAllEnemyUnitsAround(hero, Configuration.SPELL_WIND_RADIUS)
                    .filter(e -> !e.hasActiveShield());
                Vector dir = new Vector(hero.position, push.getDestination()).normalize().mult(Configuration.SPELL_WIND_DISTANCE);
                enemies.forEach(e -> {
                    directionMap.putIfAbsent(e, new ArrayList<>());
                    directionMap.get(e).add(dir);
                });
            } catch (ActionException e) {
                gameManager.addToGameSummary(hero.owner.getNicknameToken() + " failed a WIND: " + e.getMessage());
            }
        }

        positionKeyMap.clear();

        //Calculate sum of pushes
        directionMap.forEach((entity, directions) -> {
            Vector sum = directions.stream().reduce((a, b) -> a.add(b)).get();
            Vector predictedPosition = entity.position.add(sum).symmetricTruncate(symmetryOrigin);

            Vector baseWallIntersection = baseWallIntersection(entity.position, predictedPosition);
            if (entity instanceof Hero || baseWallIntersection != null) {
                predictedPosition = snapToGameZone(baseWallIntersection == null ? predictedPosition : baseWallIntersection);
            } else if (entity.type == TYPE_MOB && isInBaseAttractionZone(entity.position) && !isInBaseAttractionZone(predictedPosition)) {
                HashSet<Vector> pair = new HashSet<>();
                pair.add(predictedPosition);
                pair.add(predictedPosition.symmetric(symmetryOrigin));

                double randomDouble;
                Double existingRandom = positionKeyMap.get(pair);

                if (existingRandom == null) {
                    randomDouble = random.nextDouble();
                    positionKeyMap.put(pair, randomDouble);
                } else {
                    randomDouble = existingRandom;
                }

                double randomDirection = randomDouble * Math.PI * 2;
                if (existingRandom != null) {
                    randomDirection += Math.PI;
                }

                ((Mob) entity).setSpeed(new Vector(randomDirection).normalize().mult(Configuration.MOB_MOVE_SPEED));
            }

            entity.pushTo(predictedPosition);
        });

    }

    private void recordSpellUse(Hero hero) {
        Action push = hero.intent;
        SpellUse su = new SpellUse();
        su.hero = hero.id;
        su.spell = push.getType().name();
        su.target = push.getTarget();
        if (push.getDestination() != null) {
            su.destination = new Coord((int) push.getDestination().getX(), (int) push.getDestination().getY());
        }
        spellUses.add(su);

    }

    private Vector baseWallIntersection(Vector from, Vector to) {
        int w = Configuration.MAP_WIDTH - 1;
        int h = Configuration.MAP_HEIGHT - 1;
        int baseRadius = Configuration.BASE_ATTRACTION_RADIUS;
        Vector intersection = null;
        if (to.getY() >= h) {
            intersection = intersection(from.getX(), from.getY(), to.getX(), to.getY(), w - baseRadius, h, w, h);
        } else if (to.getY() < 0) {
            intersection = intersection(from.getX(), from.getY(), to.getX(), to.getY(), 0, 0, baseRadius, 0);
        }
        if (intersection == null) {
            if (to.getX() >= w) {
                intersection = intersection(from.getX(), from.getY(), to.getX(), to.getY(), w, h - baseRadius, w, h);
            } else if (to.getX() < 0) {
                intersection = intersection(from.getX(), from.getY(), to.getX(), to.getY(), 0, 0, 0, baseRadius);
            }
        }
        return intersection != null ? intersection.symmetricTruncate(symmetryOrigin) : null;
    }

    private boolean isInBaseAttractionZone(Vector v) {
        for (Vector basePosition : basePositions) {
            if (v.inRange(basePosition, Configuration.BASE_ATTRACTION_RADIUS)) {
                return true;
            }
        }
        return false;
    }

    private void doShield() {
        // A protective bubble will appear around target on next turn
        for (Hero hero : intentMap.get(ActionType.SHIELD)) {
            Action control = hero.intent;
            try {
                if (hero.owner.getMana() < Configuration.SPELL_PROTECT_COST) {
                    throw new ActionException("Not enough mana");
                }
                Optional<? extends GameEntity> targeted = allEntities.get()
                    .filter(other -> other.id == control.getTarget())
                    .findFirst();

                GameEntity entity = targeted.orElseThrow(() -> new ActionException("Could not find entity " + control.getTarget()));

                if (canSee(hero, entity)) {
                    hero.owner.spendMana(Configuration.SPELL_PROTECT_COST);
                    recordSpellUse(hero);
                    if (!entity.hasActiveShield()) {
                        entity.applyShield();
                    } else {
                        throw new ActionException("Entity " + entity.id + " already has a shield up");
                    }

                } else {
                    if (canSee(hero.getOwner(), entity)) {
                        throw new ActionException("Entity " + entity.id + " is not within range of Hero " + hero.id);
                    } else {
                        throw new ActionException("Hero " + hero.id + " doesn't know where entity " + entity.id + " is");
                    }
                }
            } catch (ActionException e) {
                gameManager.addToGameSummary(hero.owner.getNicknameToken() + " failed a SHIELD: " + e.getMessage());
            }
        }

    }

    private void doControl() {
        // Incept next action into victim's mind
        for (Hero hero : intentMap.get(ActionType.CONTROL)) {
            Action control = hero.intent;
            try {
                if (hero.owner.getMana() < Configuration.SPELL_CONTROL_COST) {
                    throw new ActionException("Not enough mana");
                }
                Optional<? extends GameEntity> targeted = allEntities.get()
                    .filter(other -> other.id == control.getTarget())
                    .findFirst();

                GameEntity victim = targeted.orElseThrow(() -> new ActionException("Could not find entity " + control.getTarget()));

                if (canSee(hero, victim)) {

                    hero.owner.spendMana(Configuration.SPELL_CONTROL_COST);

                    recordSpellUse(hero);

                    if (!victim.hasActiveShield()) {
                        victim.applyControl(control.getDestination());
                    } else {
                        throw new ActionException("Entity " + victim.id + " has a shield up");
                    }

                } else {
                    if (canSee(hero.getOwner(), victim)) {
                        throw new ActionException("Entity " + victim.id + " is not within range of Hero " + hero.id);
                    } else {
                        throw new ActionException("Hero " + hero.id + " doesn't know where entity " + victim.id + " is");
                    }
                }
            } catch (ActionException e) {
                gameManager.addToGameSummary(hero.owner.getNicknameToken() + " failed a CONTROL: " + e.getMessage());
            }
        }
    }

    private boolean canSee(Hero hero, GameEntity entity) {
        if (Configuration.ENABLE_FOG) {
            return hero.position.inRange(entity.position, Configuration.HERO_VIEW_RADIUS);
        }
        return true;
    }

    private boolean canSee(Player player, GameEntity entity) {
        if (!insideVisibleMap(entity.position)) {
            return false;
        }
        if (entity.getOwner() == player) {
            return true;
        }
        if (entity.position.inRange(basePositions.get(player.getIndex()), Configuration.BASE_VIEW_RADIUS)) {
            return true;
        }
        if (player.heroes.stream().anyMatch(hero -> canSee(hero, entity))) {
            return true;
        }
        return false;
    }

    private void spawnNewMobs(int turn) {
        List<Mob> newMobs = mobSpawner.update(turn);
        allMobs.addAll(newMobs);
        newEntities.addAll(newMobs);
    }

    private void moveMobs() {
        for (Mob mob : allMobs) {
            if (!insideMap(mob.position)) {
                removeMob(mob);
                continue;
            }

            if (!mob.moveCancelled()) {
                if (!mob.activeControls.isEmpty()) {
                    Vector computedDestination = computeControlResult(mob, Configuration.MOB_MOVE_SPEED);
                    Vector newSpeed = new Vector(mob.position, computedDestination);

                    Vector baseWallIntersection = baseWallIntersection(mob.position, computedDestination);
                    computedDestination = snapToGameZone(baseWallIntersection == null ? computedDestination : baseWallIntersection);

                    mob.position = computedDestination.symmetricTruncate(symmetryOrigin);
                    if (!newSpeed.isZero()) {
                        mob.setSpeed(newSpeed.normalize().mult(Configuration.MOB_MOVE_SPEED).truncate());
                    }
                } else {
                    mob.position = mob.position.add(mob.getSpeed()).symmetricTruncate(symmetryOrigin);
                }
            }
            for (int idx = 0; idx < basePositions.size(); ++idx) {
                Vector base = basePositions.get(idx);
                if (mob.position.inRange(base, Configuration.BASE_RADIUS) && mob.getHealth() > 0) {
                    removeMob(mob);
                    Player p = gameManager.getPlayer(idx);
                    p.damageBase();
                    if (p.getBaseHealth() > 0) {
                        gameManager.addTooltip(p, "Base attacked!");
                    }
                    BaseAttack a = new BaseAttack();
                    a.player = idx;
                    a.mob = mob.id;
                    baseAttacks.add(a);
                    continue;
                }

                if (mobCanDetectBase(mob, base)) {
                    Vector v = new Vector(mob.position, base);
                    int distanceToStep = (int) Math.min(v.length(), Configuration.MOB_MOVE_SPEED);
                    mob.setSpeed(base.sub(mob.position).normalize().mult(distanceToStep).truncate());
                    gameManager.getPlayer(idx).spotted.add(mob.id);
                } else if (mob.position.inRange(base, Configuration.BASE_ATTRACTION_RADIUS)) {
                    Vector objective = new Vector(1, 1);
                    if (mob.position.getX() < 0 || mob.position.getY() < 0) {
                        objective = objective.mult(-1);
                    }
                    mob.setSpeed(objective.normalize().mult(Configuration.MOB_MOVE_SPEED).truncate());

                }
            }
        }

    }

    private boolean removeMob(Mob mob) {
        return mobRemovals.add(mob);
    }

    private Map<Player, Integer[]> performCombat() {
        Set<Mob> killedMobs = new HashSet<>();
        Map<Player, Integer[]> manaGain = new HashMap<>();

        //Deal hero damage to mobs
        for (Hero h : allHeroes) {
            Stream<Mob> mobs = getAllAround(h, Configuration.HERO_ATTACK_RANGE, allMobs.stream());
            List<Integer> mobsHit = new ArrayList<>();
            boolean isOutsideBaseRadius = !h.position.inRange(basePositions.get(h.owner.getIndex()), Configuration.BASE_ATTRACTION_RADIUS);

            mobs.forEach(mob -> {
                mob.hit(Configuration.HERO_ATTACK_DAMAGE);

                manaGain.compute(h.owner, (k, v) -> {
                    if (v == null) {
                        return new Integer[] { Configuration.HERO_ATTACK_DAMAGE, isOutsideBaseRadius ? Configuration.HERO_ATTACK_DAMAGE : 0 };
                    } else {
                        v[0] += Configuration.HERO_ATTACK_DAMAGE;
                        v[1] += isOutsideBaseRadius ? Configuration.HERO_ATTACK_DAMAGE : 0;
                    }
                    return v;
                });

                if (!mob.isAlive()) {
                    killedMobs.add(mob);
                }
                mobsHit.add(mob.id);
            });

            if (!mobsHit.isEmpty()) {
                Attack a = new Attack();
                attacks.add(a);
                a.hero = h.id;
                a.mobs = mobsHit;
            }
        }

        for (Mob m : killedMobs) {
            removeMob(m);
        }
        return manaGain;
    }

    private void moveHeroes() {
        //Handle hero MOVES
        for (Hero h : intentMap.get(ActionType.MOVE)) {
            Action move = h.intent;
            h.position = snapToGameZone(move.getDestination());
        }

    }

    private void handlePlayerCommands() {
        for (Player player : gameManager.getActivePlayers()) {
            try {
                handleCommands(player, player.getOutputs());
            } catch (TimeoutException e) {
                player.deactivate("Timeout!");
                gameManager.addToGameSummary(player.getNicknameToken() + " has not provided " + player.getExpectedOutputLines() + " lines in time");
            }
        }

    }

    /**
     * Called before player outputs are handled
     */
    private void resetGameTurnData() {
        // Reset intentions
        for (Hero h : allHeroes) {
            h.intent = Action.IDLE;
            h.message = null;
        }
        for (ActionType type : ActionType.values()) {
            intentMap.get(type).clear();
        }

        // Remove dead mobs
        for (Mob mob : mobRemovals) {
            allMobs.remove(mob);
        }
        mobRemovals.clear();

        // Reset mobs
        for (Mob mob : allMobs) {
            mob.reset();
        }

        // Reset view info
        newEntities.clear();
        attacks.clear();
        spellUses.clear();
        baseAttacks.clear();
        gameManager.getPlayers().stream()
            .forEach(Player::resetViewData);

    }

    private Stream<? extends GameEntity> getAllEnemyUnitsAround(Hero hero, int range) {
        return getAllAround(
            hero, range, allEntities.get().filter(e -> e.getOwner() != hero.owner)
        );
    }

    private <T extends GameEntity> Stream<T> getAllAround(GameEntity e, int range, Stream<T> stream) {
        return stream
            .filter(other -> other.position.inRange(e.position, range));
    }

    private boolean mobCanDetectBase(Mob mob, Vector base) {
        return insideVisibleMap(mob.position) && mob.position.inRange(base, Configuration.BASE_ATTRACTION_RADIUS);
    }

    static final Pattern PLAYER_MOVE_PATTERN = Pattern.compile(
        "^MOVE\\s+(?<x>-?\\d+)\\s+(?<y>-?\\d+)"
            + "(?:\\s+(?<message>.+))?"
            + "\\s*$"
    );
    static final Pattern PLAYER_WAIT_PATTERN = Pattern.compile(
        "^WAIT"
            + "(?:\\s+(?<message>.+))?"
            + "\\s*$"
    );
    static final Pattern PLAYER_WIND_PATTERN = Pattern.compile(
        "^SPELL\\s+"
            + "WIND\\s+(?<x>-?\\d+)\\s+(?<y>-?\\d+)"
            + "(?:\\s+(?<message>.+))?"
            + "\\s*$"
    );
    static final Pattern PLAYER_SHIELD_PATTERN = Pattern.compile(
        "^SPELL\\s+"
            + "(SHIELD\\s+(?<id>-?\\d+))"
            + "(?:\\s+(?<message>.+))?"
            + "\\s*$"
    );
    static final Pattern PLAYER_CONTROL_PATTERN = Pattern.compile(
        "^SPELL\\s+"
            + "(CONTROL\\s+(?<id>-?\\d+)\\s+(?<x>-?\\d+)\\s+(?<y>-?\\d+))"
            + "(?:\\s+(?<message>.+))?"
            + "\\s*$"
    );

    static String EXPECTED = Configuration.ENABLE_WIND || Configuration.ENABLE_CONTROL || Configuration.ENABLE_WIND
        ? "MOVE <x> <y> | SPELL <spell_command> | WAIT"
        : "MOVE <x> <y> | WAIT";

    private void handleCommands(Player player, List<String> lines) {
        int i = 0;
        for (String line : lines) {
            Hero hero = player.heroes.get(i++);
            if (!hero.activeControls.isEmpty()) {
                Vector computedDestination = computeControlResult(hero, Configuration.HERO_MOVE_SPEED);

                Action intent = new Action(ActionType.MOVE);
                intent.setForced(true);
                intent.setDestination(computedDestination.symmetricTruncate(symmetryOrigin));
                hero.activeControls.clear();
                recordIntention(hero, intent);
                hero.setMessage(null);
                continue;
            }
            try {
                Matcher match = PLAYER_WAIT_PATTERN.matcher(line);
                if (match.matches()) {
                    //Message
                    matchMessage(hero, match);
                    continue;
                }

                match = PLAYER_MOVE_PATTERN.matcher(line);
                if (match.matches()) {
                    int x = Integer.valueOf(match.group("x"));
                    int y = Integer.valueOf(match.group("y"));
                    if (hero.position.getX() != x || hero.position.getY() != y) {
                        Action intent = new Action(ActionType.MOVE);
                        int speed = Configuration.HERO_MOVE_SPEED;
                        Vector target = stepTo(hero.position, new Vector(x, y), speed);

                        // Don't use doubles for internal positions else players won't be able to determine state N+1 from state N.
                        intent.setDestination(
                            target.symmetricTruncate(symmetryOrigin)
                        );
                        recordIntention(hero, intent);
                    }
                    //Message
                    matchMessage(hero, match);
                    continue;
                }

                if (Configuration.ENABLE_WIND) {
                    match = PLAYER_WIND_PATTERN.matcher(line);
                    if (match.matches()) {
                        int x = Integer.valueOf(match.group("x"));
                        int y = Integer.valueOf(match.group("y"));
                        Action intent = new Action(ActionType.WIND);
                        intent.setDestination(new Vector(x, y));
                        recordIntention(hero, intent);
                        //Message
                        matchMessage(hero, match);
                        continue;
                    }
                }

                if (Configuration.ENABLE_SHIELD) {
                    match = PLAYER_SHIELD_PATTERN.matcher(line);
                    if (match.matches()) {
                        int entityId = Integer.valueOf(match.group("id"));
                        Action intent = new Action(ActionType.SHIELD);
                        intent.setTarget(entityId);
                        recordIntention(hero, intent);
                        //Message
                        matchMessage(hero, match);
                        continue;
                    }
                }
                if (Configuration.ENABLE_CONTROL) {
                    match = PLAYER_CONTROL_PATTERN.matcher(line);
                    if (match.matches()) {
                        int entityId = Integer.valueOf(match.group("id"));
                        int x = Integer.valueOf(match.group("x"));
                        int y = Integer.valueOf(match.group("y"));
                        Action intent = new Action(ActionType.CONTROL);
                        intent.setTarget(entityId);
                        intent.setDestination(new Vector(x, y));
                        recordIntention(hero, intent);
                        //Message
                        matchMessage(hero, match);
                        continue;
                    }
                }

                throw new InvalidInputException(EXPECTED, line);

            } catch (InvalidInputException e) {
                player.deactivate(e.getMessage());
                gameManager.addToGameSummary("Bad command");
                return;
            } catch (Exception e) {
                player.deactivate(new InvalidInputException(e.toString(), EXPECTED, line).getMessage());
                gameManager.addToGameSummary("Bad command");
                return;
            }

        }
    }

    private Vector computeControlResult(GameEntity e, int moveSpeed) {
        return e.activeControls.stream()
            .map(v -> stepTo(e.position, v, moveSpeed))
            .reduce((a, b) -> a.add(b))
            .get()
            .mult(1d / e.activeControls.size());
    }

    private void matchMessage(Hero hero, Matcher match) {
        String message = match.group("message");
        if (message != null) {
            String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
            String messageWithoutEmojis = message.replaceAll(characterFilter, "");
            hero.setMessage(messageWithoutEmojis);
        }
    }

    private Vector stepTo(Vector position, Vector destination, int speed) {
        Vector v = new Vector(position, destination);
        Vector target;
        if (v.lengthSquared() <= speed * speed) {
            target = v;
        } else {
            target = v.normalize().mult(speed);
        }
        return position.add(target);
    }

    private void recordIntention(Hero hero, Action intent) {
        hero.intent = intent;
        intentMap.compute(intent.getType(), (key, value) -> {
            if (value == null) {
                value = new ArrayList<Hero>();
            }
            value.add(hero);
            return value;
        });
    }

    private void sendGameStateFor(Player player) {
        List<String> entityLines = new ArrayList<>();

        allHeroes.stream().filter(hero -> canSee(player, hero)).forEach(hero -> {
            entityLines.add(
                join(
                    hero.id,
                    hero.type == player.getIndex() ? INPUT_TYPE_MY_HERO : INPUT_TYPE_ENEMY_HERO,
                    hero.position.toIntString(),
                    hero.shieldDuration,
                    hero.isControlled() ? 1 : 0,
                    "-1 -1 -1 -1 -1"
                )
            );
        });
        allMobs.stream().filter(mob -> canSee(player, mob)).forEach(mob -> {
            entityLines.add(
                join(
                    mob.id,
                    INPUT_TYPE_MOB,
                    mob.position.toIntString(),
                    mob.shieldDuration,
                    mob.isControlled() ? 1 : 0,

                    mob.getHealth(),
                    mob.getSpeed().toIntString(),
                    getMobStatus(mob).toStringFor(player)
                )
            );
        });

        // <health> <mana>
        player.sendInputLine(join(player.getBaseHealth(), player.getMana()));
        gameManager.getPlayers().stream()
            .filter(p -> p != player)
            .forEach(p -> {
                player.sendInputLine(join(p.getBaseHealth(), p.getMana()));
            });

        // <entityCount>
        player.sendInputLine(String.valueOf(entityLines.size()));
        for (String line : entityLines) {
            //Mobs
            // <id> <type> <x> <y> <shieldLife> <isControlled> <health> <vx> <vy> <state> <target>
            //Heroes
            // <id> <type> <x> <y> <shieldLife> <isControlled> -1 -1 -1 -1 -1
            player.sendInputLine(line);
        }
    }

    static final int WANDERING = 0;
    static final int ATTACKING = 1;

    private MobStatus getMobStatus(Mob mob) {
        if (mob.status == null || !mob.activeControls.isEmpty()) {
            Vector mobSpeed;

            if (!mob.activeControls.isEmpty()) {
                Vector computedDestination = computeControlResult(mob, Configuration.MOB_MOVE_SPEED);

                Vector newSpeed = new Vector(mob.position, computedDestination);
                mobSpeed = newSpeed;
            } else {
                mobSpeed = mob.getSpeed();
            }

            if (mobSpeed.isZero()) {
                mob.status = new MobStatus(WANDERING, null, 0);
            } else {
                Vector cur = mob.position;
                boolean stop = false;
                int turns = 0;

                while (!stop && turns < 8000) {
                    // Am I inside an attraction zone?
                    for (int idx = 0; idx < basePositions.size(); ++idx) {
                        Vector base = basePositions.get(idx);
                        if (cur.inRange(base, Configuration.BASE_ATTRACTION_RADIUS)) {
                            mob.status = new MobStatus(turns == 0 ? ATTACKING : WANDERING, gameManager.getPlayer(idx), turns);
                            stop = true;
                            break;
                        }
                    }
                    // Am I outside the map?
                    if (!insideMap(cur)) {
                        mob.status = new MobStatus(WANDERING, null, turns);
                        stop = true;
                    }
                    turns++;
                    cur = cur.add(mobSpeed).symmetricTruncate(symmetryOrigin);
                }
                if (!stop) {
                    // Failsafe
                    mob.status = new MobStatus(WANDERING, null, 0);
                }
            }
        }
        return mob.status;
    }

    static public String join(Object... args) {
        return Stream.of(args).map(String::valueOf).collect(Collectors.joining(" "));
    }

    private boolean insideMap(Vector p) {
        return p.withinBounds(
            -Configuration.MAP_LIMIT, -Configuration.MAP_LIMIT,
            Configuration.MAP_WIDTH + Configuration.MAP_LIMIT, Configuration.MAP_HEIGHT + Configuration.MAP_LIMIT
        );
    }

    private boolean insideVisibleMap(Vector p) {
        return p.withinBounds(0, 0, Configuration.MAP_WIDTH, Configuration.MAP_HEIGHT);
    }

    @Override
    public void onEnd() {
        boolean tie = false;
        if (gameManager.getPlayers().size() == 2) {
            Player a = gameManager.getPlayer(0);
            Player b = gameManager.getPlayer(1);
            if (a.isActive() && !b.isActive()) {
                a.setScore(1);
                b.setScore(0);
            } else if (!a.isActive() && b.isActive()) {
                a.setScore(0);
                b.setScore(1);
            } else if (a.getBaseHealth() != b.getBaseHealth() || !Configuration.ENABLE_TIE_BREAK) {
                a.setScore(a.getBaseHealth());
                b.setScore(b.getBaseHealth());
            } else {
                // Tie breaker
                tie = true;
                a.setScore(a.getManaGainedOutsideOfBase());
                b.setScore(b.getManaGainedOutsideOfBase());
                if (a.getManaGainedOutsideOfBase() == b.getManaGainedOutsideOfBase()) {
                    gameManager.addToGameSummary("Tie!");
                } else {
                    Player winner = a.getManaGainedOutsideOfBase() > b.getManaGainedOutsideOfBase() ? a : b;
                    gameManager.addToGameSummary(
                        winner.getNicknameToken() +
                            " won the game because their heroes gained more mana outside of their base:"
                    );
                    gameManager.addToGameSummary(a.getNicknameToken() + ": " + a.getManaGainedOutsideOfBase() + " mana");
                    gameManager.addToGameSummary(b.getNicknameToken() + ": " + b.getManaGainedOutsideOfBase() + " mana");

                }
            }
        } else {
            for (Player p : gameManager.getPlayers()) {
                p.setScore(p.getBaseHealth());
            }
        }
        endScreenModule.setScores(gameManager.getPlayers().stream().mapToInt(Player::getScore).toArray(), tie);
    }

    private static EntityData asViewData(GameEntity entity) {
        EntityData res = new EntityData();
        res.type = entity.type;
        res.id = entity.id;
        if (entity.type == Referee.TYPE_MOB) {
            res.health = ((Mob) entity).getHealth();
        }
        return res;
    }

    private static Coord asCoord(GameEntity entity) {
        int x = (int) entity.position.getX();
        int y = (int) entity.position.getY();
        return new Coord(x, y);
    }

    public FrameViewData getCurrentFrameData() {
        FrameViewData data = new FrameViewData();
        data.positions = allEntities.get()
            .collect(
                toMap(
                    GameEntity::getId,
                    Referee::asCoord
                )
            );
        data.spawns = newEntities.stream().map(Referee::asViewData).collect(toList());
        data.attacks = attacks;
        data.spellUses = spellUses;
        data.baseAttacks = baseAttacks;
        data.mana = gameManager.getPlayers().stream()
            .filter(Player::manaHasChanged)
            .collect(
                Collectors.toMap(Player::getIndex, Player::getMana)
            );
        data.mobHealth = allMobs.stream()
            .filter(Mob::healthHasChanged)
            .collect(
                Collectors.toMap(Mob::getId, Mob::getHealth)
            );
        data.baseHealth = gameManager.getPlayers().stream()
            .filter(Player::baseHealthHasChanged)
            .collect(
                Collectors.toMap(Player::getIndex, Player::getBaseHealth)
            );
        data.messages = allHeroes.stream()
            .filter(h -> h.message != null)
            .collect(
                toMap(
                    h -> h.getId(),
                    h -> h.message
                )
            );

        data.controlled = allEntities.get().filter(e -> e.isControlled()).map(GameEntity::getId).collect(toList());
        data.pushed = allEntities.get().filter(e -> e.gotPushed()).map(GameEntity::getId).collect(toList());
        data.shielded = allEntities.get().filter(e -> e.hadActiveShield()).map(GameEntity::getId).collect(toList());
        return data;
    }

    public GlobalViewData getGlobalData() {
        GlobalViewData data = new GlobalViewData();
        data.width = Configuration.MAP_WIDTH;
        data.height = Configuration.MAP_HEIGHT;
        data.heroesPerPlayer = Configuration.HEROES_PER_PLAYER;
        data.baseRadius = Configuration.BASE_RADIUS;
        data.baseAttractionRadius = Configuration.BASE_ATTRACTION_RADIUS;
        data.basePositions = basePositions.stream().map(pos -> {
            int x = (int) pos.getX();
            int y = (int) pos.getY();
            return new Coord(x, y);
        }).collect(toList());
        data.heroViewRadius = Configuration.HERO_VIEW_RADIUS;
        data.baseViewRadius = Configuration.BASE_VIEW_RADIUS;

        data.enableSpells = Configuration.ENABLE_CONTROL || Configuration.ENABLE_WIND || Configuration.ENABLE_SHIELD;
        data.enableFog = Configuration.ENABLE_FOG;
        return data;
    }
}
