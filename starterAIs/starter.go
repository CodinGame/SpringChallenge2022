package main

import "fmt"

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/

const (
	TYPE_MONSTER = iota
	TYPE_MY_HERO
	TYPE_OP_HERO
)

type Entity struct {
	ID, Type, X, Y, ShieldLife, IsControlled, Health, VX, VY, NearBase, ThreatFor int
}

func main() {
	// baseX: The corner of the map representing your base
	var baseX, baseY int
	fmt.Scan(&baseX, &baseY)

	// heroesPerPlayer: Always 3
	var heroesPerPlayer int
	fmt.Scan(&heroesPerPlayer)

	for {
		var myHealth, myMana, oppHealth, oppMana int
		fmt.Scan(&myHealth, &myMana)
		fmt.Scan(&oppHealth, &oppMana)

		// entityCount: Amount of heros and monsters you can see
		var entityCount int
		fmt.Scan(&entityCount)

		var monsters, myHeroes, oppHeroes []Entity

		for i := 0; i < entityCount; i++ {
			// id: Unique identifier
			// _type: 0=monster, 1=your hero, 2=opponent hero
			// x: Position of this entity
			// shieldLife: Ignore for this league; Count down until shield spell fades
			// isControlled: Ignore for this league; Equals 1 when this entity is under a control spell
			// health: Remaining health of this monster
			// vx: Trajectory of this monster
			// nearBase: 0=monster with no target yet, 1=monster targeting a base
			// threatFor: Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither
			var id, _type, x, y, shieldLife, isControlled, health, vx, vy, nearBase, threatFor int
			fmt.Scan(&id, &_type, &x, &y, &shieldLife, &isControlled, &health, &vx, &vy, &nearBase, &threatFor)

			entity := Entity{id, _type, x, y, shieldLife, isControlled, health, vx, vy, nearBase, threatFor}

			switch _type {
			case TYPE_MONSTER:
				monsters = append(monsters, entity)
			case TYPE_MY_HERO:
				myHeroes = append(myHeroes, entity)
			case TYPE_OP_HERO:
				oppHeroes = append(oppHeroes, entity)
			}
		}
		for i := 0; i < heroesPerPlayer; i++ {
			var target *Entity

			if len(monsters) > 0 {
				target = &monsters[i%len(monsters)]
			}

			if target != nil {
				fmt.Println(fmt.Sprintf("MOVE %d %d", target.X, target.Y))
			} else {
				fmt.Println("WAIT")
			}
		}
		// fmt.Fprintln(os.Stderr, "Debug messages...")
	}
}
