import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.math.*;

class Player {

    static final int TYPE_MONSTER = 0;
    static final int TYPE_MY_HERO = 1;
    static final int TYPE_OP_HERO = 2;

    static class Entity {
        int id;
        int type;
        int x, y;
        int shieldLife;
        int isControlled;
        int health;
        int vx, vy;
        int nearBase;
        int threatFor;

        Entity(int id, int type, int x, int y, int shieldLife, int isControlled, int health, int vx, int vy, int nearBase, int threatFor) {
            this.id = id;
            this.type = type;
            this.x = x;
            this.y = y;
            this.shieldLife = shieldLife;
            this.isControlled = isControlled;
            this.health = health;
            this.vx = vx;
            this.vy = vy;
            this.nearBase = nearBase;
            this.threatFor = threatFor;
        }
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        // base_x,base_y: The corner of the map representing your base
        int baseX = in.nextInt();
        int baseY = in.nextInt();
        // heroesPerPlayer: Always 3
        int heroesPerPlayer = in.nextInt();     

        // game loop
        while (true) {
            int myHealth = in.nextInt(); // Your base health
            int myMana = in.nextInt(); // Ignore in the first league; Spend ten mana to cast a spell
            int oppHealth = in.nextInt();
            int oppMana = in.nextInt();
            int entityCount = in.nextInt(); // Amount of heros and monsters you can see
            
            List<Entity> myHeroes = new ArrayList<>(entityCount);
            List<Entity> oppHeroes = new ArrayList<>(entityCount);
            List<Entity> monsters = new ArrayList<>(entityCount);
            for (int i = 0; i < entityCount; i++) {
                int id = in.nextInt();              // Unique identifier      
                int type = in.nextInt();            // 0=monster, 1=your hero, 2=opponent hero        
                int x = in.nextInt();               // Position of this entity       
                int y = in.nextInt();
                int shieldLife = in.nextInt();      // Ignore for this league; Count down until shield spell fades      
                int isControlled = in.nextInt();    // Ignore for this league; Equals 1 when this entity is under a control spell        
                int health = in.nextInt();          // Remaining health of this monster      
                int vx = in.nextInt();              // Trajectory of this monster      
                int vy = in.nextInt();
                int nearBase = in.nextInt();        // 0=monster with no target yet, 1=monster targeting a base        
                int threatFor = in.nextInt();       // Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither       
                
                Entity entity = new Entity(
                    id, type, x, y, shieldLife, isControlled, health, vx, vy, nearBase, threatFor
                );
                switch (type) {
                    case TYPE_MONSTER:
                        monsters.add(entity);
                        break;
                    case TYPE_MY_HERO:
                        myHeroes.add(entity);
                        break;
                    case TYPE_OP_HERO:
                        oppHeroes.add(entity);
                        break;
                }                
            }

            for (int i = 0; i < heroesPerPlayer; i++) {
                Entity target = null;
                
                if (!monsters.isEmpty()) {
                    target = monsters.get(i % monsters.size());
                }

                if (target != null) {
                    System.out.println(String.format("MOVE %d %d", target.x , target.y));
                } else {
                    System.out.println("WAIT");
                }
            }
        }
    }
}