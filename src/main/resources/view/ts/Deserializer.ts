function parsePositions (values) {
  const positions = {}
  for (let i = 0; i < values.length; i += 3) {
    const id = values[i]
    const x = +values[i + 1]
    const y = +values[i + 2]
    positions[id] = { x, y }
  }
  return positions
}

function parseMessages (values) {
  if (!values.length) {
    return {}
  }

  values = values.join(' ').split(';')
  const messages = {}
  for (let i = 0; i < values.length; i += 2) {
    const id = values[i]
    const string = values[i + 1]
    messages[id] = string
  }
  return messages
}
function parseIntList (values) {
  return values.map(v => +v)
}
function parseIntMap (values) {
  const ints = {}
  for (let i = 0; i < values.length; i += 2) {
    const id = values[i]
    const int = values[i + 1]
    ints[id] = +int
  }
  return ints
}

function parseFromTokens (tokens, callback) {
  return (values) => {
    const res = []
    for (let i = 0; i < values.length; i += tokens) {
      res.push(callback(values.slice(i, i + tokens)))
    }
    return res
  }
}

function parseSpawns (values) {
  return parseFromTokens(3, ([id, type, health]) => {
    return {
      id: +id,
      type: +type,
      health: +health
    }
  })(values)
}

function parseAttacks (values) {
  return parseFromTokens(2, ([hero, mobs]) => {
    return {
      hero: +hero,
      mobs: mobs.split(';').map(v => +v)
    }
  })(values)
}
function parseBaseAttacks (values) {
  return parseFromTokens(2, ([player, mob]) => {
    return {
      player: +player,
      mob: +mob
    }
  })(values)
}
function parseSpellUses (values) {
  return parseFromTokens(5, ([hero, spell, target, x, y]) => {
    return {
      hero: +hero,
      spell,
      target: +target,
      destination: {
        x: +x,
        y: +y
      }
    }
  })(values)
}

export function parseData (raw) {
  const data = {} as any
  let idx = 0
  const lines = raw.split('\n').map(line => line === '' ? [] : line.split(' '))

  // States
  data.positions = parsePositions(lines[idx++])
  data.messages = parseMessages(lines[idx++])
  data.controlled = parseIntList(lines[idx++])
  data.pushed = parseIntList(lines[idx++])
  data.shielded = parseIntList(lines[idx++])

  // Diffs
  data.mana = parseIntMap(lines[idx++])
  data.baseHealth = parseIntMap(lines[idx++])
  data.mobHealth = parseIntMap(lines[idx++])

  // Events
  data.spawns = parseSpawns(lines[idx++])
  data.attacks = parseAttacks(lines[idx++])
  data.baseAttacks = parseBaseAttacks(lines[idx++])
  data.spellUses = parseSpellUses(lines[idx++])

  return data
}

export function parseGlobalData (raw) {
  const data = {} as any
  let idx = 0
  const lines = raw.split('\n').map(line => line === '' ? [] : line.split(' '))
  const general = lines[idx++]
  const base0 = lines[idx++]
  const base1 = lines[idx++]

  let gIdx = 0
  data.width = +general[gIdx++]
  data.height = +general[gIdx++]
  data.baseAttractionRadius = +general[gIdx++]
  data.baseRadius = +general[gIdx++]
  data.baseViewRadius = +general[gIdx++]
  data.heroViewRadius = +general[gIdx++]
  data.heroesPerPlayer = +general[gIdx++]
  data.enableSpells = general[gIdx++] === '1'
  data.enableFog = general[gIdx++] === '1'
  data.demo = general[gIdx] === 'DEMO'
  data.basePositions = [
    {
      x: +base0[0],
      y: +base0[1]
    }, {
      x: +base1[0],
      y: +base1[1]
    }
  ]
  return data
}
