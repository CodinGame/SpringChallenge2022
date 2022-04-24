import java.util.*
import java.io.*
import java.math.*

const val TYPE_MONSTER = 0
const val TYPE_MY_HERO = 1
const val TYPE_OP_HERO = 2

fun main(args: Array<String>) {
    val `in` = Scanner(System.`in`)
    // base_x,base_y: The corner of the map representing your base
    val baseX = `in`.nextInt()
    val baseY = `in`.nextInt()
    // heroesPerPlayer: Always 3
    val heroesPerPlayer = `in`.nextInt()

    // game loop
    while (true) {
        val myHealth = `in`.nextInt() // Your base health
        val myMana = `in`.nextInt() // Ignore in the first league; Spend ten mana to cast a spell
        val oppHealth = `in`.nextInt()
        val oppMana = `in`.nextInt()
        val entityCount = `in`.nextInt() // Amount of heros and monsters you can see
        val myHeroes: MutableList<Entity> = ArrayList(entityCount)
        val oppHeroes: MutableList<Entity> = ArrayList(entityCount)
        val monsters: MutableList<Entity> = ArrayList(entityCount)
        for (i in 0 until entityCount) {
            val id = `in`.nextInt() // Unique identifier
            val type = `in`.nextInt() // 0=monster, 1=your hero, 2=opponent hero
            val x = `in`.nextInt() // Position of this entity
            val y = `in`.nextInt()
            val shieldLife = `in`.nextInt() // Ignore for this league; Count down until shield spell fades
            val isControlled =
                `in`.nextInt() // Ignore for this league; Equals 1 when this entity is under a control spell
            val health = `in`.nextInt() // Remaining health of this monster
            val vx = `in`.nextInt() // Trajectory of this monster
            val vy = `in`.nextInt()
            val nearBase = `in`.nextInt() // 0=monster with no target yet, 1=monster targeting a base
            val threatFor =
                `in`.nextInt() // Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither
            val entity = Entity(
                id, type, x, y, shieldLife, isControlled, health, vx, vy, nearBase, threatFor
            )
            when (type) {
                TYPE_MONSTER -> monsters.add(entity)
                TYPE_MY_HERO -> myHeroes.add(entity)
                TYPE_OP_HERO -> oppHeroes.add(entity)
            }
        }
        for (i in 0 until heroesPerPlayer) {
            var target: Entity? = null
            if (!monsters.isEmpty()) {
                target = monsters[i % monsters.size]
            }
            if (target != null) {
                println(String.format("MOVE %d %d", target.x, target.y))
            } else {
                println("WAIT")
            }
        }
    }
}

class Entity(
    var id: Int,
    var type: Int,
    var x: Int,
    var y: Int,
    var shieldLife: Int,
    var isControlled: Int,
    var health: Int,
    var vx: Int,
    var vy: Int,
    var nearBase: Int,
    var threatFor: Int
)
