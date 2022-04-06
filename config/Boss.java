import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Julien Poulton
 */

class Vector {
  private final double x, y;

  public Vector(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Vector(Vector a, Vector b) {
    this.x = b.x - a.x;
    this.y = b.y - a.y;
  }

  public Vector(double angle) {
    this.x = Math.cos(angle);
    this.y = Math.sin(angle);
  }

  public Vector rotate(double angle) {
    double nx = (x * Math.cos(angle)) - (y * Math.sin(angle));
    double ny = (x * Math.sin(angle)) + (y * Math.cos(angle));

    return new Vector(nx, ny);
  };

  public boolean equals(Vector v) {
    return v.getX() == x && v.getY() == y;
  }

  public Vector round() {
    return new Vector((int) Math.round(this.x), (int) Math.round(this.y));
  }

  public Vector truncate() {
    return new Vector((int) this.x, (int) this.y);
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double distance(Vector v) {
    return Math.sqrt((v.x - x) * (v.x - x) + (v.y - y) * (v.y - y));
  }

  public boolean inRange(Vector v, double range) {
    return (v.x - x) * (v.x - x) + (v.y - y) * (v.y - y) <= range * range;
  }

  public Vector add(Vector v) {
    return new Vector(x + v.x, y + v.y);
  }

  public Vector mult(double factor) {
    return new Vector(x * factor, y * factor);
  }

  public Vector sub(Vector v) {
    return new Vector(this.x - v.x, this.y - v.y);
  }

  public double length() {
    return Math.sqrt(x * x + y * y);
  }

  public double lengthSquared() {
    return x * x + y * y;
  }

  public Vector normalize() {
    double length = length();
    if (length == 0)
      return new Vector(0, 0);
    return new Vector(x / length, y / length);
  }

  public double dot(Vector v) {
    return x * v.x + y * v.y;
  }

  public double angle() {
    return Math.atan2(y, x);
  }

  @Override
  public String toString() {
    return "[" + x + ", " + y + "]";
  }

  public String toIntString() {
    return (int) x + " " + (int) y;
  }

  public Vector project(Vector force) {
    Vector normalize = this.normalize();
    return normalize.mult(normalize.dot(force));
  }

  public final Vector cross(double s) {
    return new Vector(-s * y, s * x);
  }

  public Vector hsymmetric(double center) {
    return new Vector(2 * center - this.x, this.y);
  }

  public Vector vsymmetric(double center) {
    return new Vector(this.x, 2 * center - this.y);
  }

  public Vector vsymmetric() {
    return new Vector(this.x, -this.y);
  }

  public Vector hsymmetric() {
    return new Vector(-this.x, this.y);
  }

  public Vector symmetric() {
    return symmetric(new Vector(0, 0));
  }

  public Vector symmetric(Vector center) {
    return new Vector(center.x * 2 - this.x, center.y * 2 - this.y);
  }

  public boolean withinBounds(double minx, double miny, double maxx, double maxy) {
    return x >= minx && x < maxx && y >= miny && y < maxy;
  }

  public boolean isZero() {
    return x == 0 && y == 0;
  }

  public Vector symmetricTruncate(Vector origin) {
    return sub(origin).truncate().add(origin);
  }

}

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
class Configuration {
  public static int MAP_WIDTH = 17630;
  public static int MAP_HEIGHT = 9000;

  public static int BASE_ATTRACTION_RADIUS = 5000;
  public static int BASE_VIEW_RADIUS = 6000;
  public static int BASE_RADIUS = 300;

  public static int HERO_MOVE_SPEED = 800;
  public static int HEROES_PER_PLAYER = 3;
  public static int HERO_VIEW_RADIUS = 2200;
  public static int HERO_ATTACK_RANGE = 800;
  public static int HERO_ATTACK_DAMAGE = 2;

  public static int MAX_MANA = -1;
  public static int STARTING_MANA = 0;
  public static int STARTING_BASE_HEALTH = 3;

  public static int MOB_MOVE_SPEED = 400;

  public static final double MOB_SPAWN_MAX_DIRECTION_DELTA = 5 * Math.PI / 12;
  public static int MOB_SPAWN_RATE = 5;
  public static int MOB_STARTING_MAX_ENERGY = 10;
  public static double MOB_GROWTH_MAX_ENERGY = 0.5;

  public static int SPELL_PUSH_COST = 10;
  public static int SPELL_CONTROL_COST = 10;
  public static int SPELL_PROTECT_COST = 10;
  public static int SPELL_PROTECT_DURATION = 12;
  public static int SPELL_PUSH_DISTANCE = 2200;
  public static int SPELL_PUSH_RADIUS = 1280;

}

class Unit {

  int id, type, shieldLife, isControlled, health, state, target;

  Vector position, speed;

  public Unit(int id, int type, Vector position, int isControlled, int health, Vector speed, int state, int target,
      int shieldLife) {
    this.id = id;
    this.type = type;
    this.shieldLife = shieldLife;
    this.isControlled = isControlled;
    this.health = health;
    this.speed = speed;
    this.state = state;
    this.target = target;
    this.position = position;
  }

}

class Player {

  public static final int MY_HERO = 1;
  public static final int ME = 1;
  public static final int HIS_HERO = 2;
  public static final int MOB = 0;

  static Vector base;
  static List<Unit> heroes;
  private static int reservedMana, mana;

  static class FogMob {
    Unit unit;
    boolean updated;

    public FogMob() {

    }

    @Override
    public String toString() {
      return String.valueOf(unit);
    }
  }

  private static boolean shouldSee(Vector position) {
    if (position.inRange(base, Configuration.BASE_VIEW_RADIUS)) {
      return true;
    }
    if (heroes.stream().anyMatch(hero -> hero.position.inRange(position, Configuration.HERO_VIEW_RADIUS))) {
      return true;
    }
    return false;
  }

  public static void main(String args[]) {
    Vector[] guardPosts = new Vector[] { new Vector(4995, 3750), new Vector(5581, 1439), new Vector(1849, 5813) };

    try (Scanner in = new Scanner(System.in)) {
      base = new Vector(in.nextInt(), in.nextInt());
      int heroesPerPlayer = in.nextInt();
      Vector enemyBase = new Vector(Configuration.MAP_WIDTH - base.getX(), Configuration.MAP_HEIGHT - base.getY());

      if (base.getX() > enemyBase.getX()) {
        guardPosts = new Vector[] { new Vector(Configuration.MAP_WIDTH - 4995, Configuration.MAP_HEIGHT - 3750),
            new Vector(Configuration.MAP_WIDTH - 5581, Configuration.MAP_HEIGHT - 1439),
            new Vector(Configuration.MAP_WIDTH - 1849, Configuration.MAP_HEIGHT - 5813) };
      }

      Vector center = new Vector(Configuration.MAP_WIDTH / 2, Configuration.MAP_HEIGHT / 2);

      Map<Integer, FogMob> mobLookup = new HashMap<>();

      int stupidity = 15;
      int stupidityCountDown = stupidity;

      // game loop
      while (true) {
        mobLookup.forEach((k, v) -> v.updated = false);

        in.nextInt();
        mana = in.nextInt();
        for (int i = 0; i < 2; ++i) {
          in.nextInt();
        }
        int entityCount = in.nextInt();
        List<Unit> mobs = new ArrayList<>();
        heroes = new ArrayList<>();
        List<Unit> foes = new ArrayList<>();
        for (int i = 0; i < entityCount; ++i) {
          int entityId = in.nextInt();

          int type = in.nextInt();
          int x = in.nextInt();
          int y = in.nextInt();
          // Mobs
          // <id> <type> <x> <y> <shieldLife> <isControlled> <health> <vx> <vy> <state>
          // <target>
          int shieldLife = in.nextInt();

          int isControlled = in.nextInt();
          int health = in.nextInt();
          int vx = in.nextInt();
          int vy = in.nextInt();
          int state = in.nextInt();
          int target = in.nextInt();

          Unit u = new Unit(entityId, type, new Vector(x, y), isControlled, health, new Vector(vx, vy), state, target,
              shieldLife);
          if (type == MY_HERO) {
            heroes.add(u);
          } else if (type == MOB) {
            mobs.add(u);
            FogMob fogMob = mobLookup.get(entityId);
            if (fogMob == null) {
              fogMob = new FogMob();
              mobLookup.put(entityId, fogMob);
            }
            fogMob.unit = u;
            fogMob.updated = true;
            mobLookup.get(entityId).updated = true;
          } else if (type == HIS_HERO) {
            foes.add(u);
          }
        }

        mobLookup.values().stream().filter(fm -> !fm.updated).forEach(fm -> {
          fm.unit.position = fm.unit.position.add(fm.unit.speed);
        });
        mobLookup = mobLookup.entrySet().stream().filter(e -> {
          FogMob fm = e.getValue();
          if (!fm.unit.position.inRange(center, Configuration.MAP_WIDTH)) {
            // Gone outside
            return false;
          }
          if (!fm.updated && shouldSee(fm.unit.position)) {
            // Should be visible
            return false;
          }
          return true;
        }).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        List<Unit> threats = mobs.stream().filter(mob -> mob.target == ME).sorted((a, b) -> {
          return (int) (new Vector(a.position, base).lengthSquared() - new Vector(b.position, base).lengthSquared());
        }).collect(toList());

        reservedMana = 0;

        for (int i = 0; i < heroesPerPlayer; ++i) {
          Unit hero = heroes.get(i);
          String action = null;

          if (i == 0) {
            // Attacker
            if (hero.position.distance(enemyBase) > Configuration.BASE_ATTRACTION_RADIUS) {
              action = move( enemyBase, "go to enemy");
            } else {
              if (canPush()) {
                Optional<Unit> optMob = mobs.stream()
                    .filter(m -> m.position.distance(hero.position) < Configuration.SPELL_PUSH_RADIUS).findFirst();
                if (optMob.isPresent() && optMob.get().shieldLife == 0) {
                  if (canShield() && optMob.get().position.inRange(enemyBase, Configuration.BASE_ATTRACTION_RADIUS / 2)
                      && optMob.get().health > 10) {
                    action = shield(optMob.get(), "shield");
                  } else {
                    action = push(enemyBase, "aggro push");
                  }

                } else {
                  /*
                   * Optional<Unit> optFoe = foes.stream() .filter(f ->
                   * f.position.distance(hero.position) < (Configuration.SPELL_PUSH_RADIUS -
                   * Configuration.HERO_MOVE_SPEED)).findFirst(); if (optFoe.isPresent()) { action
                   * = push(enemyBase, "harass push"); } else {
                   */
                  optMob = mobs.stream().filter(mob -> mob.shieldLife == 0)
                      .filter(closeEnoughTo(enemyBase, Configuration.BASE_ATTRACTION_RADIUS))
                      .sorted(byDistanceFrom(hero)).findFirst();
                  if (optMob.isPresent()) {
                    action = move(optMob.get().position, "prep aggro push");
                    
                  }
                  /* } */
                }

              }
              if (action == null) {
                Optional<Unit> optFoe = foes.stream()
                    .filter(closeEnoughTo(enemyBase, Configuration.BASE_ATTRACTION_RADIUS)).sorted(byDistanceFrom(hero))
                    .findFirst();
                if (optFoe.isPresent()) {
                  action = move( optFoe.get().position, "harass");
                }
              }
              if (action == null) {
                action = join("WAIT");
              }
            }
          } else if (threats.size() > i) {
            Unit target = threats.get(i);

            if (mana >= Configuration.SPELL_CONTROL_COST - reservedMana
                && !target.position.inRange(base, Configuration.BASE_ATTRACTION_RADIUS - Configuration.MOB_MOVE_SPEED)
                && target.position.inRange(hero.position, Configuration.HERO_VIEW_RADIUS)
                && target.isControlled == 0) {

              action = control( target, enemyBase,"");
              reservedMana += Configuration.SPELL_CONTROL_COST;

            } else if (mana >= Configuration.SPELL_PUSH_COST + reservedMana && !target.position.inRange(base,
                Configuration.BASE_ATTRACTION_RADIUS - Configuration.SPELL_PUSH_DISTANCE)) {

              reservedMana += Configuration.SPELL_PUSH_COST;
              if (!hero.position.inRange(target.position, Configuration.SPELL_PUSH_RADIUS - 60)) {
                // Get into position
                action = move( target.position, "Preping a push");
              } else {
                // Cast
                action = push( enemyBase, "PUSH");
              }
            }
          }

          if (action == null) {
            if (!threats.isEmpty()) {
              if (canPush() && threats.stream().filter(closeEnoughTo(hero.position, Configuration.SPELL_PUSH_RADIUS))
                  .findFirst().isPresent()) {
                action = push(enemyBase, "defend push");
              } else {
                Unit target = threats.get(0);
                action = move( target.position, "defend");
              }

            } else if (!mobLookup.isEmpty()) {
              Unit mob = mobLookup.values().stream().findFirst().get().unit;
              action = move( mob.position,"");
            } else {
              action = move( guardPosts[i],"");
            }
          }

          if (--stupidityCountDown == 0) {
            System.out.println("WAIT");
            stupidityCountDown = stupidity;
          } else {
            System.out.println(action);
          }
        }

      }

    }

  }

  private static String control(Unit unit,Vector v, String message) {
    reservedMana += Configuration.SPELL_CONTROL_COST;
    return join("SPELL CONTROL", unit.id, v.toIntString());
  }

  private static String shield(Unit unit, String message) {
    reservedMana += Configuration.SPELL_PROTECT_COST;
    return join("SPELL SHIELD", unit.id);
  }
  
  private static String move(Vector pos  ,String message) {
      reservedMana += Configuration.SPELL_PROTECT_COST;
      return join("MOVE", pos.toIntString());
    }

  private static String push(Vector enemyBase, String message) {
    reservedMana += Configuration.SPELL_PUSH_COST;
    return join("SPELL WIND", enemyBase.toIntString());
  }

  private static Predicate<? super Unit> closeEnoughTo(Vector v, int dist) {
    return unit -> unit.position.inRange(v, dist);
  }

  private static Comparator<? super Unit> byDistanceFrom(Unit u) {
    return (a, b) -> {
      return (int) (new Vector(a.position, u.position).lengthSquared()
          - new Vector(b.position, u.position).lengthSquared());
    };

  }

  private static boolean canShield() {
    return mana - reservedMana >= Configuration.SPELL_PROTECT_COST;
  }

  private static boolean canPush() {
    return mana - reservedMana >= Configuration.SPELL_PUSH_COST;
  }

  static public String join(Object... args) {
    return Stream.of(args).map(String::valueOf).collect(Collectors.joining(" "));
  }
}