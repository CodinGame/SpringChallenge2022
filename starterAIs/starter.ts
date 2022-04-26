class Entity {
  TYPE_MONSTER = 0;
  TYPE_MY_HERO = 1;
  TYPE_OTHER_HERO = 2;
  MY_BASE = 1;
  OTHER_BASE = 2;
  distanceFromMyBase: number;
  constructor(
    public id: number,
    public type: number,
    public x: number,
    public y: number,
    public shieldLife: number,
    public isControlled: number,
    public health: number,
    public vx: number,
    public vy: number,
    public nearBase: number,
    public threatFor: number,
    private me: Player
  ) {
    this.distanceFromMyBase = this.getDistanceFrom(
      this.me.basePosX,
      this.me.basePosY
    );
  }
  isDangerousForMyBase = (): boolean => {
    return this.threatFor === this.MY_BASE;
  };
  getDistanceFrom = (x: number, y: number): number => {
    return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
  };
}

class Player {
  constructor(
    public basePosX: number,
    public basePosY: number,
    public baseHealth: number,
    public mana: number
  ) {}
  setHealth = (value: number) => {
    this.baseHealth = value;
  };
  setMana = (value: number) => {
    this.mana = value;
  };
  canCast = (): boolean => {
    return this.mana >= 10;
  };
}

class Game {
  ACTION_WAIT = "WAIT";
  ACTION_MOVE = "MOVE";
  ACTION_SPELL = "SPELL";
  SPELL_WIND = "WIND";
  SPELL_CONTROL = "CONTROL";
  SPELL_SHIELD = "SHIELD";

  me: Player;
  enemy: Player;
  entities: Entity[];

  constructor(baseX: number, baseY: number, private heroes: number) {
    this.me = new Player(baseX, baseY, 3, 0);
    this.enemy = new Player(
      baseX === 0 ? 17630 : 0,
      baseY === 0 ? 9000 : 0,
      3,
      0
    );
    this.entities = [];
  }

  newTurn = (
    health: number,
    mana: number,
    enemyHealth: number,
    enemyMana: number
  ) => {
    this.me.setHealth(health);
    this.me.setMana(mana);
    this.enemy.setHealth(enemyHealth);
    this.enemy.setMana(enemyMana);
    this.entities = [];
  };

  addEntity = (entity: Entity) => {
    this.entities.push(entity);
  };

  nextAction = (hero: number): string => {
    // In the first league: MOVE <x> <y> | WAIT; In later leagues: | SPELL <spellParams>;

    return this.ACTION_WAIT;
  };
  debug = (message: string, ...rest) => {
    console.error(message, ...rest);
  };
}

const [baseX, baseY] = readline().split(" ").map(Number); // The corner of the map representing your base
const heroesPerPlayer: number = Number(readline()); // Always 3
const game = new Game(baseX, baseY, heroesPerPlayer);

// game loop
while (true) {
  const myBaseInput: number[] = readline().split(" ").map(Number);
  const enemyBaseInput: number[] = readline().split(" ").map(Number);
  game.newTurn(
    myBaseInput[0],
    myBaseInput[1],
    enemyBaseInput[0],
    enemyBaseInput[1]
  );

  const entityCount: number = Number(readline()); // Amount of heros and monsters you can see
  for (let i = 0; i < entityCount; i++) {
    const inputs: number[] = readline().split(" ").map(Number);
    game.addEntity(
      new Entity(
        inputs[0], // Unique identifier
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
        game.me
      )
    );
  }
  for (let i = 0; i < heroesPerPlayer; i++) {
    console.log(game.nextAction(i));
  }
}
