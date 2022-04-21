<?php

$TYPE_MONSTER = 0;
$TYPE_MY_HERO = 1;
$TYPE_OP_HERO = 2;

// $baseX: The corner of the map representing your base
fscanf(STDIN, "%d %d", $baseX, $baseY);
// $heroesPerPlayer: Always 3
fscanf(STDIN, "%d", $heroesPerPlayer);

// game loop
while (TRUE)
{
    for ($i = 0; $i < 2; $i++)
    {
        // $health: Your base health
        // $mana: Spend ten mana to cast a spell
        fscanf(STDIN, "%d %d", $health, $mana);
    }

    // $entityCount: Amount of heroes and monsters you can see
    fscanf(STDIN, "%d", $entityCount);

    $monsters = [];
    $my_heroes = [];
    $opp_heroes = [];

    for ($i = 0; $i < $entityCount; $i++)
    {
        // $id: Unique identifier
        // $type: 0=monster, 1=your hero, 2=opponent hero
        // $x: Position of this entity
        // $shieldLife: Ignore for this league; Count down until shield spell fades
        // $isControlled: Ignore for this league; Equals 1 when this entity is under a control spell
        // $health: Remaining health of this monster
        // $vx: Trajectory of this monster
        // $nearBase: 0=monster with no target yet, 1=monster targeting a base
        // $threatFor: Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither
        fscanf(STDIN, "%d %d %d %d %d %d %d %d %d %d %d", $id, $type, $x, $y, $shieldLife, $isControlled, $health, $vx, $vy, $nearBase, $threatFor);

        $entity = new Entity($id, $type, $x, $y, $shieldLife, $isControlled, $health, $vx, $vy, $nearBase, $threatFor);

        if ($type == $TYPE_MONSTER) {
            $monsters[] = $entity;
        }
        elseif ($type == $TYPE_MY_HERO) {
            $my_heroes[] = $entity;
        }
        elseif ($type == $TYPE_OP_HERO) {
            $opp_heroes[] = $entity;
        }
    }
    for ($i = 0; $i < $heroesPerPlayer; $i++)
    {
        // To debug: error_log(var_export($var, true)); (equivalent to var_dump)

        $target = NULL;

        if ($monsters) {
            $target = $monsters[$i % count($monsters)];
        }
        // In the first league: MOVE <x> <y> | WAIT; In later leagues: | SPELL <spellParams>;
        if ($target) {
            echo("MOVE {$target->x} {$target->y}" . PHP_EOL);
        }
        else {
            echo("WAIT" . PHP_EOL);
        }

    }

}

class Entity {
    public $id, $type, $x, $y, $shieldLife, $isControlled, $health, $vx, $vy, $nearBase, $threatFor;

    public function __construct($id, $type, $x, $y, $shieldLife, $isControlled, $health, $vx, $vy, $nearBase, $threatFor) {
        $this->id = $id;
        $this->type = $type;
        $this->x = $x;
        $this->y = $y;
        $this->shieldLife = $shieldLife;
        $this->isControlled = $isControlled;
        $this->health = $health;
        $this->vx = $vx;
        $this->vy = $vy;
        $this->nearBase = $nearBase;
        $this->threatFor = $threatFor;
    }

}
