import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.math.*;

/**
 * @Author Simon Fuet, Julien Poulton
 * 
 *         Auto-generated code below aims at helping you parse the standard input according to the problem statement.
 **/
class Player {

    static class Unit {
        int id;
        int type;
        int x;
        int y;
        int shieldLife;
        int isControlled;
        int health;
        int vx;
        int vy;
        int nearBase;
        int threatFor;

        Unit(int id, int type, int x, int y, int shieldLife, int isControlled, int health, int vx, int vy, int nearBase, int threatFor) {
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
        int baseX = in.nextInt();
        int baseY = in.nextInt();
        int heroesPerPlayer = in.nextInt();

        int[] postX = baseX == 0 ? new int[] { 3000, 7000, 5500 } : new int[] { baseX - 3000, baseX - 7000, baseX - 5500 };
        int[] postY = baseY == 0 ? new int[] { 6500, 1500, 4000 } : new int[] { baseY - 6500, baseY - 1500, baseY - 4000 };

        // game loop
        while (true) {
            int myHealth = 0;
            int mana = 0;

            myHealth = in.nextInt();
            mana = in.nextInt();

            in.nextInt();
            in.nextInt();

            int entityCount = in.nextInt();
            Unit[] units = new Unit[entityCount];
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
                int nearBase = in.nextInt();
                int threatFor = in.nextInt();
                units[i] = new Unit(id, type, x, y, shieldLife, isControlled, health, vx, vy, nearBase, threatFor);
            }
            Unit[] myHeroes = Arrays.stream(units).filter(unit -> unit.type == 1).toArray(s -> new Unit[s]);

            int nbHeroesRoaming = 2;

            Unit closestEnemyToBase = null;
            double minDistToBase = Double.POSITIVE_INFINITY;
            for (Unit unit : units) {
                if (unit.type != 0) continue;
                double curDist = computeDist(baseX, baseY, unit.x, unit.y);
                if (curDist < minDistToBase) {
                    minDistToBase = curDist;
                    closestEnemyToBase = unit;
                }
            }

            for (int i = 0; i < nbHeroesRoaming; i++) {
                Unit hero = myHeroes[i];
                
                Unit target = null;
                double minDist = Double.POSITIVE_INFINITY;
                for (Unit unit : units) {
                    if (unit.type != 0 || unit == closestEnemyToBase) continue;
                    double curDist = computeDist(hero, unit);
                    if (curDist < minDist) {
                        minDist = curDist;
                        target = unit;
                    }
                }
                if (target == null) {
                    System.out.println("MOVE " + postX[i] + " " + postY[i]);
                } else {
                    if (canWind(mana) && computeDist(hero.x, hero.y, target.x, target.y) < 1280) {
                        System.out.println("SPELL WIND " + (17630-baseX) + " " + (9000-baseY));
                    } else {
                        System.out.println("MOVE " + target.x + " " + target.y);
                    }
                }
            }

            for (int i = nbHeroesRoaming; i < heroesPerPlayer; i++) {
                Unit hero = myHeroes[i];
                Unit target = closestEnemyToBase;
                
                if (target == null || minDistToBase > 5000) {
                    System.out.println("WAIT");
                } else {
                    if (canWind(mana) && computeDist(hero.x, hero.y, target.x, target.y) < 1280) {
                        System.out.println("SPELL WIND " + postX[i] + " " + postY[i]);
                    } else {
                        System.out.println("MOVE " + target.x + " " + target.y);
                    }
                }
            }
        }
        in.close();
    }

    private static boolean canWind(int mana) {
		return mana >= 10;
	}

	static double computeDist(Unit u1, Unit u2) {
        return Math.sqrt((u1.x - u2.x) * (u1.x - u2.x) + (u1.y - u2.y) * (u1.y - u2.y));
    }

    static double computeDist(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}