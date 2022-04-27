
  class Entity  {
    constructor (id, type, x, y, shieldLife, isControlled, health, vx, vy, nearBase, threatFor, me) {
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
        this.me = me;
        this.TYPE_MONSTER = 0;
        this.TYPE_MY_HERO = 1;
        this.TYPE_OTHER_HERO = 2;
        this.MY_BASE = 1;
        this.OTHER_BASE = 2;
        this.isDangerousForMyBase = function () {
            return this.threatFor === this.MY_BASE;
        };
        this.getDistanceFrom = function (x, y) {
            return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
        };
        this.distanceFromMyBase = this.getDistanceFrom(this.me.basePosX, this.me.basePosY);
    }
}

class Player  {
    constructor (basePosX, basePosY, baseHealth, mana) {
        var _this = this;
        this.basePosX = basePosX;
        this.basePosY = basePosY;
        this.baseHealth = baseHealth;
        this.mana = mana;
        this.setHealth = function (value) {
            _this.baseHealth = value;
        };
        this.setMana = function (value) {
            _this.mana = value;
        };
        this.canCast = function () {
            return _this.mana >= 10;
        };
    }
}

class Game {
    constructor (baseX, baseY, heroes) {
        this.heroes = heroes;
        this.ACTION_WAIT = "WAIT";
        this.ACTION_MOVE = "MOVE";
        this.ACTION_SPELL = "SPELL";
        this.SPELL_WIND = "WIND";
        this.SPELL_CONTROL = "CONTROL";
        this.SPELL_SHIELD = "SHIELD";
        this.newTurn = function (health, mana, enemyHealth, enemyMana) {
            this.me.setHealth(health);
            this.me.setMana(mana);
            this.enemy.setHealth(enemyHealth);
            this.enemy.setMana(enemyMana);
            this.entities = [];
        };

        this.addEntity = function (entity) {
            this.entities.push(entity);
        };
       
        this.me = new Player(baseX, baseY, 3, 0);
        this.enemy = new Player(baseX === 0 ? 17630 : 0, baseY === 0 ? 9000 : 0, 3, 0);
        this.entities = [];
    }

    nextAction (hero) {
        // In the first league: MOVE <x> <y> | WAIT; In later leagues: | SPELL <spellParams>;
    
        return this.ACTION_WAIT;
    };
    
    debug (...message) {
        console.error(...message);
    };
}


var _a = readline().split(" ").map(Number), baseX = _a[0], baseY = _a[1]; // The corner of the map representing your base
var heroesPerPlayer = Number(readline()); // Always 3
var game = new Game(baseX, baseY, heroesPerPlayer);

// game loop
while (true) {
    var myBaseInput = readline().split(" ").map(Number);
    var enemyBaseInput = readline().split(" ").map(Number);
    game.newTurn(myBaseInput[0], myBaseInput[1], enemyBaseInput[0], enemyBaseInput[1]);

    var entityCount = Number(readline()); // Amount of heros and monsters you can see
    for (var i = 0; i < entityCount; i++) {
        var inputs = readline().split(" ").map(Number);
        game.addEntity(new Entity(inputs[0], // Unique identifier
        inputs[1], // 0=monster, 1=your hero, 2=opponent hero
        inputs[2], // Position of this entity
        inputs[3],
        inputs[4], // Ignore for this league; Count down until shield spell fades
        inputs[5], // Ignore for this league; Equals 1 when this entity is under a control spell
        inputs[6], // Remaining health of this monster
        inputs[7], // Trajectory of this monster
        inputs[8],
        inputs[9], // 0=monster with no target yet, 1=monster targeting a base
        inputs[10], // Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither
        game.me));
    }
    for (var i = 0; i < heroesPerPlayer; i++) {
        console.log(game.nextAction(i));
    }
}

