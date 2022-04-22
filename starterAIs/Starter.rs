use std::convert::TryFrom;

fn main() {
  let [
    base_x, // The corner of the map representing your base
    base_y
  ] = <[i32; 2]>::try_from(input_vec()).ok().unwrap();

  let heroes_per_player = input!(usize); // Always 3

  // game loop
  loop {
    for _ in 0..2 as usize {
      let [
        health, // Each player's base health
        mana // Ignore in the first league; Spend ten mana to cast a spell
      ] = <[i32; 2]>::try_from(input_vec()).ok().unwrap();
    }

    let mut monster_vec: Vec<Entity> = vec![];
    let mut hero_my_vec: Vec<Entity> = vec![];
    let mut hero_opp_vec: Vec<Entity> = vec![];

    let entity_count = input!(usize);
    for _ in 0..entity_count {
      let [
        id, // Unique identifier
        typ, // 0=monster, 1=your hero, 2=opponent hero
        x, // Position of this entity
        y,
        shield_life, // Ignore for this league; Count down until shield spell fades
        is_controlled, // Ignore for this league; Equals 1 when this entity is under a control spell
        health, // Remaining health of this monster
        vx, // Trajectory of this monster
        vy,
        near_base, // 0=monster with no target yet, 1=monster targeting a base
        threat_for // Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither
      ] = <[i32; 11]>::try_from(input_vec()).ok().unwrap();
      
      // assign typ to enum value
      let typ = match typ {
        0 => Type::Monster,
        1 => Type::HeroMy,
        2 => Type::HeroOpp,
        _ => unreachable!()
      };

      // fill the struct with given values
      let entity = Entity {
        id,
        typ: typ.clone(),
        x,
        y,
        shield_life,
        is_controlled,
        health,
        vx,
        vy,
        near_base,
        threat_for,
      };

      match typ {
        Type::Monster => monster_vec.push(entity),
        Type::HeroMy => hero_my_vec.push(entity),
        Type::HeroOpp => hero_opp_vec.push(entity),
      }
    }

    let monster_vec_len = monster_vec.len();

    for i in 0..heroes_per_player {
      match monster_vec_len {
        0 => println!("WAIT"),
        _ => {
          match monster_vec.clone().into_iter().nth(i % monster_vec_len) {
            Some(target) => println!("MOVE {} {}", target.x, target.y),
            None => unreachable!()
          }
        }
      }
    }
  }
}

#[derive(Debug, Clone)]
enum Type {
  Monster,
  HeroMy,
  HeroOpp
}

// declare the struct with debug (printable using debug format {:#?} or {:?})
#[derive(Debug, Clone)]
struct Entity {
  id: i32,
  typ: Type,
  x: i32,
  y: i32,
  shield_life: i32,
  is_controlled: i32,
  health: i32,
  vx: i32,
  vy: i32,
  near_base: i32,
  threat_for: i32,
}

// function to read an input vector of i32
fn input_vec() -> Vec<i32> {
  input!()
    .split(" ")
    .map(|s| s.parse::<i32>().unwrap())
    .collect::<Vec<_>>()
}

// general macro to read stdin (less clunky)
#[macro_export]
macro_rules! input {
  () => {{
    input!(String)
  }};

  ($t:ty) => {{
    let input = &mut "".into();
    std::io::stdin().read_line(input).unwrap();
    if input.ends_with("\n") {
      input.pop();
    };
    input.parse::<$t>().unwrap()
  }};
}

