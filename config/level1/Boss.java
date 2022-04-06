import java.util.*;
import java.util.stream.Collectors;
/**
 * @author Dimitri Sergeant
 */



class Player {
    static Random random = new Random(9800);
    
    public static void main(String args[]) {
        new Player();
    }
    List<Entity> heroes;
    List<Entity> enemies;
    List<Entity> monsters;
    Team myTeam;
    Base myBase;
    Team enemyTeam;
    Base enemyBase;

    public Player() {
        Scanner in = new Scanner(System.in);
        Vector base = new Vector(in.nextInt(), in.nextInt());
        myBase = new Base(base, Constants.HEROES);
        enemyBase = new Base(new Vector(Constants.MAP_WIDTH - base.getX(), Constants.MAP_HEIGHT - base.getY()), Constants.ENNEMY);
        
        
        int indexBase =  myBase.baseCenter.getX() < enemyBase.baseCenter.getX() ? 1 : -1;

        Vector[] heroAssiociatedVector = new Vector[] {
            new Vector(indexBase * 2000, indexBase * 2000),
            new Vector(indexBase * 4000, indexBase * 1000),
            new Vector(indexBase * 2500, indexBase * 3000)
        };
        
        int heroesPerPlayer = in.nextInt();
        int turn = 0;

        while (true) {
            turn += 1;
            heroes = new ArrayList<>();
            enemies = new ArrayList<>();
            monsters = new ArrayList<>();
            
            // get status of the team with health and mana 
            myTeam = new Team(in.nextInt(), in.nextInt());
            enemyTeam = new Team(in.nextInt(), in.nextInt());

            int entityCount = in.nextInt();
            for (int i = 0; i < entityCount; i++) {
                int id = in.nextInt();
                int type = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                int shieldLife = in.nextInt();
                int isControlled = in.nextInt();
                int health = in.nextInt();
                int vx = in.nextInt();
                int vy = in.nextInt();
                int insideAttractionZone = in.nextInt();
                int playerTargeted = in.nextInt();

                Entity entity = new Entity(id, type, x, y, shieldLife, isControlled, health, vx, vy, insideAttractionZone, playerTargeted);

                if (type == Constants.HEROES) {
                    heroes.add(entity);
                } else if (type == Constants.ENNEMY) {
                    enemies.add(entity);
                } else {
                    monsters.add(entity);
                }
            }
            
            
            List<Entity> threateningMonsters = getOrderedMonstersThreateningBase(monsters, myBase);
                    
            int manaSoonUsed = 0;
            for (int i = 0; i < heroesPerPlayer; i++) {
                
                Entity hero = heroes.get(i);
                String action = "MOVE " + myBase.attractionCenter.add(heroAssiociatedVector[i]);
                
                List<Entity> heroCloseMonsters = orderEntitiesByDistance(
                    getMonstersInDistance(hero, monsters, Constants.HERO_VIEW_RADIUS), 
                    hero.pos
                );
    
                if (!threateningMonsters.isEmpty() && i == 0 && turn < 100) {
                    Entity monsterTargeted = threateningMonsters.get(0);
                    action = "MOVE " + monsterTargeted.pos.add(monsterTargeted.velocity); 
                } else if (!threateningMonsters.isEmpty() && i == 1 && turn < 45) {
                    Entity monsterTargeted = threateningMonsters.get(0);
                    action = "MOVE " + monsterTargeted.pos.add(monsterTargeted.velocity); 
                } else if (!threateningMonsters.isEmpty() && i == 2 && turn < 15) {
                    Entity monsterTargeted = threateningMonsters.get(0);
                    action = "MOVE " + monsterTargeted.pos.add(monsterTargeted.velocity);
                } else {
                    int randomx = random.nextInt(Constants.MAP_WIDTH + 1);
                    int randomy = random.nextInt(Constants.MAP_HEIGHT + 1);
                    action = "MOVE " + new Vector(randomx, randomy);
                }
                
                System.out.println(action);
            }   
        }
    }
    
    
    List<Entity> getOrderedMonstersThreateningBase(List<Entity> monsters, Base base) {
        List<Entity> monstersThreateningBase = new ArrayList<>();
        for (Entity monster : monsters) {
            if (monster.playerTargeted == base.playerBase) {
                monstersThreateningBase.add(monster);
            }
        }
        List<Entity> ordered = orderEntitiesByDistance(monstersThreateningBase, base.baseCenter);
        return ordered;
    }
    
    
    List<Entity> orderEntitiesByDistance(List<Entity> entities, Vector v) {
        return entities.stream()
            .sorted((a, b) -> {
                return (int) (new Vector(a.pos, v).lengthSquared() - new Vector(b.pos, v).lengthSquared());
            })
            .collect(Collectors.toList());
    }

    List<Entity> getMonstersInDistance(Entity hero, List<Entity> monsters, int distance) {
        List<Entity> monstersInDistance = new ArrayList<>();
        for (Entity monster : monsters) {
            if (hero.pos.distance(monster.pos) < distance) {
                monstersInDistance.add(monster);
            }
        }
        return monstersInDistance;
    }
}

class Constants {
    public static final int HEROES = 1;
    public static final int ENNEMY = 2;
    public static final int MONSTERS = 0;

    public static final int MAP_WIDTH = 17630;
    public static final int MAP_HEIGHT = 9000;
    public static final Vector MAP_CENTER = new Vector (MAP_WIDTH/2, MAP_HEIGHT/2);

    public static final int BASE_ATTRACTION_RADIUS = 5000;
    public static final int BASE_VIEW_RADIUS = 6000;
    public static final int BASE_RADIUS = 300;

    public static final int HERO_MOVE_SPEED = 800;
    public static final int HEROES_PER_PLAYER = 3;
    public static final int HERO_VIEW_RADIUS = 2200;
    public static final int HERO_ATTACK_RANGE = 800;
    public static final int HERO_ATTACK_DAMAGE = 2;

    public static final int MAX_MANA = -1;
    public static final int STARTING_MANA = 0;
    public static final int STARTING_BASE_HEALTH = 3;

    public static final int MOB_MOVE_SPEED = 400;
    public static final int MOB_SPAWN_RATE = 10;
    public static final int MOB_STARTING_MAX_ENERGY = 10;
    public static final float MOB_GROWTH_MAX_ENERGY = 0.5f;

    public static final int SPELL_PUSH_COST = 10;
    public static final int SPELL_PUSH_DISTANCE = 2200;
    public static final int SPELL_PUSH_RADIUS = 1280;
    public static final int SPELL_PROTECT_COST = 10;
    public static final int SPELL_PROTECT_DURATION = 12;
    public static final int SPELL_CONTROL_COST = 10;
}

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

    public Vector div(int factor) {
        return new Vector(x / factor, y / factor);
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

    public Vector barycenter(List<Vector> vectors) {
        for (Vector v : vectors) {
            this.add(v);
        }
        return this.div(vectors.size());
    }

    public Vector symmetricTruncate(Vector origin) {
        return sub(origin).truncate().add(origin);
    }

}

class Team {
    int health;
    int mana;

    public Team(int health, int mana) {
        super();
        this.health = health;
        this.mana = mana;
    }
}

class Base {
    public int playerBase;
    public Vector baseCenter;
    public Vector attractionCenter;

    public Base(Vector base, int playerBase) {
        super();
        this.baseCenter = base;
        this.attractionCenter = baseCenter.add(new Vector(1,1)).normalize().mult(Constants.BASE_ATTRACTION_RADIUS / 2); 
        if (base.getY() > Constants.MAP_HEIGHT / 2) {
            this.attractionCenter = this.attractionCenter.symmetric(Constants.MAP_CENTER);
        }
        this.playerBase = playerBase;
    }

}

class Entity {
    int id;
    int type;
    Vector pos;
    int shieldLife;
    int isControlled;
    int health;
    Vector velocity;
    boolean insideAttractionZone;
    int playerTargeted;

    public Entity(
        int id, int type, int x, int y, int shieldLife, int isControlled, int health, int vx, int vy, 
        int insideAttractionZone, int playerTargeted
    ) {
        super();
        this.id = id;
        this.type = type;
        this.pos = new Vector(x, y);
        this.shieldLife = shieldLife;
        this.isControlled = isControlled;
        this.health = health;
        this.velocity = new Vector(vx, vy);
        this.insideAttractionZone = insideAttractionZone == 1 ? true : false;
        this.playerTargeted = playerTargeted;
    }
}

