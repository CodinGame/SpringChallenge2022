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
    private myBase: Base
  ) {
    this.distanceFromMyBase = this.getDistanceFrom(myBase.x, myBase.y);
  }
  isDangerousForMyBase = () => {
    return this.threatFor === this.MY_BASE;
  };
  getDistanceFrom(x, y) {
    return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
  }
}

class Base {
  constructor(
    public x: number,
    public y: number,
    private health: number,
    private mana: number
  ) {}
  setHealth(value: number) {
    this.health = value;
  }
  setMana(value: number) {
    this.mana = value;
  }
}

class Game {
  ACTION_WAIT = "WAIT";
  ACTION_MOVE = "MOVE";
  entities: Entity[];
  constructor(private myBase: Base, private heroes: number) {}
  newTurn(health: number, mana: number) {
    this.myBase.setHealth(health);
    this.myBase.setMana(mana);
    this.entities = [];
  }
  addEntity(entity: Entity) {
    this.entities.push(entity);
  }

  nextAction(hero: number) {
    // In the first league: MOVE <x> <y> | WAIT; In later leagues: | SPELL <spellParams>;
    return this.ACTION_WAIT;
  }
  debug(message, ...rest) {
    console.error(message, ...rest);
  }
}

var inputs: string[] = readline().split(" ");
const baseX: number = parseInt(inputs[0]); // The corner of the map representing your base
const baseY: number = parseInt(inputs[1]);
const heroesPerPlayer: number = parseInt(readline()); // Always 3
const myBase = new Base(baseX, baseY, 3, 0);

const game = new Game(myBase, heroesPerPlayer);

// game loop
while (true) {
  for (let i = 0; i < 2; i++) {
    var inputs: string[] = readline().split(" ");
    game.newTurn(parseInt(inputs[0]), parseInt(inputs[1]));
  }

  const entityCount: number = parseInt(readline()); // Amount of heros and monsters you can see
  for (let i = 0; i < entityCount; i++) {
    var inputs: string[] = readline().split(" ");
    game.addEntity(
      new Entity(
        parseInt(inputs[0]), // Unique identifier
        parseInt(inputs[1]), // 0=monster, 1=your hero, 2=opponent hero
        parseInt(inputs[2]), // Position of this entity
        parseInt(inputs[3]),
        parseInt(inputs[4]), // Ignore for this league; Count down until shield spell fades
        parseInt(inputs[5]), // Ignore for this league; Equals 1 when this entity is under a control spell
        parseInt(inputs[6]), // Remaining health of this monster
        parseInt(inputs[7]), // Trajectory of this monster
        parseInt(inputs[8]),
        parseInt(inputs[9]), // 0=monster with no target yet, 1=monster targeting a base
        parseInt(inputs[10]), // Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither
        myBase
      )
    );
  }
  for (let i = 0; i < heroesPerPlayer; i++) {
    console.log(game.nextAction(i));
  }
}
