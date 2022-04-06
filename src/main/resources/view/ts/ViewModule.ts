import { WIDTH, HEIGHT } from '../core/constants.js'
import * as utils from '../core/utils.js'
import { messageBox, initMessages } from './MessageBoxes.js'
import { parseData, parseGlobalData } from './Deserializer.js'
import { getRenderer, flagForDestructionOnReinit } from '../core/rendering.js'
import {
  EFFECT_HIT,
  HIT_FRAMES,
  EFFECT_SHIELD,
  EFFECT_BEAM,
  EFFECT_GUST,
  EFFECT_GLINT,
  DISC_RADIUS,
  GLINT_FRAMES,
  BEAM_FRAMES,
  GUST_FRAMES,
  SHIELD_FRAMES,
  HERO_FRAMES,
  MOB_1_FRAMES,
  MOB_2_FRAMES,
  MOB_3_FRAMES,
  HERO_ANCHOR_RUN,
  HERO_ANCHOR_STAND,
  HERO_ANCHOR_CAST,
  FOG_SCALE,
  HERO_ANCHOR_COMBAT,
  LIFE_FRAMES,
  MOB_DEATH_FRAMES,
  PENTAGRAM_ANIMATION_PAD,
  PENTAGRAM_ANIMATION_SCALE
} from './assetConstants.js'

export interface Animatable {
  animate: (step: any) => boolean | void
}
export interface Updatable {
  update: (from: any, to: any, progress: number) => void
}

export interface Entity extends PIXI.Container, Animatable {
  pentagram: PIXI.Sprite
  mouseover: any
  mousemove: any
  mouseout: any
  targetPosition: PIXI.IPoint
  targetRotation: number
  debug: PIXI.Container
  casting: any
  attacking: boolean
  walking: any
  time: number
  shield: any
  hitbox: PIXI.Sprite
}
export interface Hero extends Entity {
  walk: PIXI.AnimatedSprite
  idle: PIXI.Sprite
  cast: PIXI.Sprite
  combat: PIXI.AnimatedSprite
  debug: PIXI.Container
  update: (previousData: any, currentData: any, progress: any) => void
}
export interface Mob extends Entity, Updatable {
  debug: PIXI.Container
  body: PIXI.Container
  walk: PIXI.AnimatedSprite
  time: number
  remove(): any
}

export interface Effect extends PIXI.DisplayObject {
  busy?: boolean
}

export const TYPE_HERO_0 = 0
export const TYPE_HERO_1 = 1
export const TYPE_MOB = 2

/* global PIXI */

const api: any = {
  toPixel: 1,
  globalData: {},
  options: {
    // Fog
    enableFog: true,
    fogOfWar: 2,
    debugMode: false,

    // Messages
    showOthersMessages: true,
    meInGame: false,
    showMyMessages: true
  },
  renderFog: () => {},
  setDebug: () => {},
  atLeastOnePixel: (width) => {
    if (width > 0 && width < api.toPixel) {
      return api.toPixel
    }
    return width
  }
}

export { api }

export class ViewModule {
  canvasMode: boolean
  states: any[]
  globalData: {[key: string]: any}
  entityMap: {}
  pool: {}
  api: any
  impactLayer: any
  effectsLayer: any
  playerSpeed: any
  frameDelta: number
  updatables: Updatable[]
  hud: any
  entities: any
  padding: any
  hudHeight: any
  oversampling: any
  container: any
  renderables: Animatable[]
  fogLayer: PIXI.Container
  heroLayer: PIXI.Container
  mobLayer: PIXI.Container
  uiLayer: PIXI.Container
  pentagramLayer: PIXI.Container
  pentagramContainer: PIXI.Container
  effectContainer: PIXI.Container
  impactContainer: PIXI.Container
  debugPentagramContainer: PIXI.Container
  debugImpactContainer: PIXI.Container
  debugEffectContainer: PIXI.Container
  renderFog: () => void
  bases: any[]
  messages: any
  debugMobLayer: PIXI.Container
  debugHeroLayer: any
  debugBasesLayer: any
  constructor (assets) {
    this.canvasMode = getRenderer().type === PIXI.RENDERER_TYPE.CANVAS
    if (this.canvasMode) {
      console.log('canvasMode')
      api.options.enableFog = false
      api.options.fogOfWar = -1
    }

    this.states = []
    this.globalData = {}
    this.entityMap = {}
    this.pool = {
    }
    window.module = this

    // Graphic constants
    api.toPixel = 1

    api.unconvertPosition = (pos) => this.unconvertPosition(pos)
    api.entityMap = this.entityMap
    this.api = api
  }

  static get moduleName () {
    return 'graphics'
  }

  getFromPool (type) {
    if (!this.pool[type]) {
      this.pool[type] = []
    }

    for (const e of this.pool[type]) {
      if (!e.busy) {
        e.busy = true
        return e
      }
    }

    const e = this.createEffect(type)
    this.pool[type].push(e)
    if (type === EFFECT_HIT) {
      this.impactLayer.addChild(e)
    } else {
      this.effectsLayer.addChild(e)
    }
    e.busy = true
    return e
  };

  createEffect (type): Effect {
    if (type === EFFECT_BEAM) {
      const frames = BEAM_FRAMES.slice(0, 15)
      const fx = PIXI.AnimatedSprite.fromFrames(frames)
      return fx
    } else if (type === EFFECT_GUST) {
      return PIXI.AnimatedSprite.fromFrames(GUST_FRAMES)
    } else if (type === EFFECT_SHIELD) {
      const frames = SHIELD_FRAMES
      const fx = PIXI.AnimatedSprite.fromFrames(frames)
      fx.anchor.set(0.5, 0.5)
      fx.loop = true
      fx.animationSpeed = 0.28
      return fx
    } else if (type === EFFECT_GLINT) {
      const frames = GLINT_FRAMES.slice()
      for (let i = GLINT_FRAMES.length - 2; i >= 0; --i) {
        frames.push(GLINT_FRAMES[i])
      }

      const fx = PIXI.AnimatedSprite.fromFrames(frames)
      fx.blendMode = PIXI.BLEND_MODES.ADD
      fx.anchor.set(0.5, 0.5)
      return fx
    } else if (type === EFFECT_HIT) {
      const fx = PIXI.AnimatedSprite.fromFrames(HIT_FRAMES)
      fx.anchor.set(0.5)
      fx.scale.set(1.5)
      return fx
    }
    const graphics = new PIXI.Graphics()
    return graphics
  }

  asLayer (func) {
    const layer = new PIXI.Container()
    func.bind(this)(layer)
    return layer
  }

  updateScene (previousData, currentData, progress, playerSpeed) {
    this.playerSpeed = playerSpeed
    this.frameDelta = api.currentData ? (api.currentData.number - currentData.number) : 0

    this.resetEffects()
    this.updateEntities(previousData, currentData, progress)
    this.updateEffects(previousData, currentData, progress)
    this.updateHud(previousData, currentData, progress)

    for (const updatable of this.updatables) {
      updatable.update(previousData, currentData, progress)
    }

    api.previousData = previousData
    api.currentData = currentData
    api.progress = progress
  }

  animateHeart (heart, progress) {
    let idx = Math.floor(progress * heart.textures.length)
    idx = Math.min(heart.textures.length - 1, idx)
    heart.gotoAndStop(idx)
  }

  heartShouldBreak (data, i, k) {
    return data.previous.baseHealth[i] > data.baseHealth[i] && data.baseHealth[i] <= k && data.previous.baseHealth[i] > k
  }

  updateHud (previousData, currentData, progress) {
    const lastFrame = currentData === this.states[this.states.length - 1]

    const data = (progress === 1) ? currentData : previousData
    for (let i = 0; i < this.globalData.playerCount; ++i) {
      this.hud[i].mana.text = `${data.mana[i]}`
      for (let k = 0; k < 3; ++k) {
        const heart = this.hud[i].hearts[k]
        heart.visible = true

        if (currentData.baseHealth[i] > k) {
          heart.gotoAndStop(0)
        } else if (this.heartShouldBreak(currentData, i, k)) {
          if (this.playerSpeed === 0) {
            if (heart.currentFrame === 0) {
              heart.play()
            }
          } else {
            this.animateHeart(heart, lastFrame ? progress : progress / 2)
          }
        } else if (this.heartShouldBreak(previousData, i, k)) {
          if (this.playerSpeed === 0) {
            if (heart.currentFrame !== heart.totalFrames - 1) {
              heart.play()
            }
          } else if (!lastFrame) {
            this.animateHeart(heart, 0.5 + progress / 2)
          }
        } else {
          heart.visible = false
          heart.gotoAndStop(heart.totalFrames - 1)
        }
      }
    }
  }

  resetEffects () {
    for (const type in this.pool) {
      for (const effect of this.pool[type]) {
        effect.visible = false
        effect.alpha = 1
        effect.busy = false
      }
    }
  }

  updateControlSpell (use, progress) {
    const target = this.entities[use.target]
    const hero = this.entities[use.hero]
    if (!target || !hero) {
      return
    }

    const delta = 0.3

    const glint = this.getFromPool(EFFECT_GLINT)
    glint.scale.set(2)
    const p = utils.unlerp(1 - delta, 1, progress)
    let idx = Math.floor(p * glint.textures.length)
    idx = Math.min(glint.textures.length - 1, idx)
    glint.gotoAndStop(idx)
    glint.position.copyFrom(target.targetPosition)
    glint.visible = p > 0

    const fx = this.getFromPool(EFFECT_BEAM)
    fx.visible = true
    fx.anchor.set(0, 0.5)
    fx.position.copyFrom(hero.targetPosition)

    const w = Math.max(1e-6, distance(fx, target.targetPosition) / fx.textures[0].width)
    fx.scale.set(w, 2)
    idx = Math.floor(progress * fx.textures.length)
    idx = Math.min(fx.textures.length - 1, idx)
    fx.gotoAndStop(idx)
    fx.rotation = Math.atan2(target.targetPosition.y - hero.targetPosition.y, target.targetPosition.x - hero.targetPosition.x)
    fx.alpha = 1 - utils.unlerp(0.7, 1, progress)
  }

  updateEffects (previousData, currentData, progress) {
    for (const k in this.entities) {
      this.entities[k].attacking = false
    }

    for (const attack of currentData.attacks) {
      const hero = this.entities[attack.hero]

      for (const mobId of attack.mobs) {
        const mob = this.entities[mobId]
        const discharge = this.getFromPool(EFFECT_HIT)
        const p = utils.unlerp(0.25, 1, progress)

        if (p === 0) {
          discharge.visible = false
        } else {
          discharge.position.copyFrom(mob.targetPosition)
          discharge.visible = true
          let idx = Math.floor(p * discharge.textures.length)
          idx = Math.min(discharge.textures.length - 1, idx)
          discharge.gotoAndStop(idx)
        }
      }

      hero.attacking = true
    }

    for (const attack of currentData.baseAttacks) {
      const mob = this.entities[attack.mob]
      mob.attacking = attack
    }

    for (let k = 0; k < this.globalData.playerCount * this.globalData.heroesPerPlayer; ++k) {
      delete this.entities[k].casting
    }

    // Stopping animations
    for (const use of currentData.previous.spellUses) {
      if (use.spell === 'CONTROL') {
        // Remaining 30% during first (3 divided by 7)% of second frame
        this.updateControlSpell(use, utils.lerp(0.7, 1, utils.unlerp(0, 3 / 7, progress)))
      } else if (use.spell === 'WIND') {
        const end = 3 / 7
        if (progress <= end) {
          this.updatePushSpell(use, utils.lerp(0.7, 1, utils.unlerp(0, end, progress)))
        }
      }
    }

    // Starting animations
    for (const use of currentData.spellUses) {
      const hero = this.entities[use.hero]
      hero.casting = use

      if (use.spell === 'CONTROL') {
        // 70% of the animation during first frame
        this.updateControlSpell(use, utils.lerp(0, 0.7, progress))
      } else if (use.spell === 'SHIELD') {
        const fx = this.getFromPool(EFFECT_SHIELD)
        fx.visible = true
        const target = this.entities[use.target]
        const caster = this.entities[use.hero]
        fx.position.copyFrom(utils.lerpPosition(caster.targetPosition, target.targetPosition, progress))
        fx.scale.set(progress * 0.9)

        let idx = Math.floor(progress * fx.textures.length)
        idx = Math.min(fx.textures.length - 1, idx)
        fx.gotoAndStop(idx)
      } else if (use.spell === 'WIND') {
        this.updatePushSpell(use, utils.lerp(0, 0.7, progress))
      }
    }
  }

  updatePushSpell (use, progress) {
    const hero = this.entities[use.hero]
    const fx = this.getFromPool(EFFECT_GUST)
    fx.visible = true
    fx.scale.set(1)
    fx.anchor.set(0.5)
    fx.alpha = 0.95
    const towards = this.convertPosition(use.destination)
    fx.rotation = Math.atan2(towards.y - hero.targetPosition.y, towards.x - hero.targetPosition.x)

    const from = {
      x: hero.targetPosition.x + Math.cos(fx.rotation) * -60,
      y: hero.targetPosition.y + Math.sin(fx.rotation) * -60
    }
    const to = {
      x: hero.targetPosition.x + Math.cos(fx.rotation) * 110,
      y: hero.targetPosition.y + Math.sin(fx.rotation) * 110
    }

    fx.position.copyFrom(utils.lerpPosition(from, to, progress))

    let idx = Math.floor(progress * fx.textures.length)
    idx = Math.min(fx.textures.length - 1, idx)
    fx.gotoAndStop(idx)
  }

  updateEntities (previousData, currentData, progress) {
    // New entities
    for (const _id in currentData.positions) {
      const id = +_id
      if (!this.entities[id]) {
        const entity = this.initEntity(id)
        this.entities[id] = entity
      }
    }

    const removedEntites = []

    for (const _id in this.entities) {
      const id = +_id
      const entity = this.entities[id]
      const from = previousData.positions[id]
      const to = currentData.positions[id]

      const pushed = currentData.pushed.includes(id)

      let newPosition = entity.targetPosition || entity.position

      entity.walking = false

      if (!to) {
        if (!from) {
          removedEntites.push(id)
        } else {
          // Fade out corpse
          entity.alpha = 1 - progress
        }
      } else if (!from) {
        // Spawn animation
        newPosition = this.convertPosition(to)
        entity.alpha = progress
        entity.walk.animationSpeed = 0
      } else {
        // Move animation
        const pos = utils.lerpPosition(from, to, progress)
        if (!pushed && (to.y !== from.y || to.x !== from.x)) {
          if (entity.targetRotation === null) {
            entity.rotation = entity.targetRotation
          }
          const angle = Math.atan2(to.y - from.y, to.x - from.x)
          if (entity.targetRotation === null) {
            entity.rotation = angle
          }
          entity.targetRotation = angle

          entity.walking = true
          if (entity.walk) {
            if (this.playerSpeed === 0) {
              entity.walk.animationSpeed = 0
              entity.walk.gotoAndPlay(entity.walk.currentFrame + 4 * Math.sign(this.frameDelta))
            } else {
              entity.walk.animationSpeed = 0.33 * Math.min(5, this.playerSpeed || 1)
            }
          }
        } else {
          entity.walk.animationSpeed = 0
        }
        newPosition = this.convertPosition(pos)
        entity.alpha = 1
      }

      if (!entity.targetPosition) {
        entity.targetPosition = new PIXI.Point()
        entity.position.copyFrom(newPosition)
      }
      entity.targetPosition.copyFrom(newPosition)

      // Pentagram animation
      entity.pentagram.visible = false
      const before = previousData.controlled.includes(id)
      const now = currentData.controlled.includes(id)
      if (before && !now) {
        entity.pentagram.visible = true
        let scale = 0
        if (progress <= PENTAGRAM_ANIMATION_PAD) {
          scale = utils.unlerp(PENTAGRAM_ANIMATION_PAD, 0, progress)
        }
        scale = Math.max(1e-8, scale)
        entity.pentagram.scale.set(scale * PENTAGRAM_ANIMATION_SCALE)
      } else if (!before && now) {
        entity.pentagram.visible = true
        let scale = 1
        if (progress <= PENTAGRAM_ANIMATION_PAD) {
          scale = utils.unlerp(0, PENTAGRAM_ANIMATION_PAD, progress)
        }
        scale = Math.max(1e-8, scale)
        entity.pentagram.scale.set(scale * PENTAGRAM_ANIMATION_SCALE)
      } else if (now && before) {
        entity.pentagram.visible = true
        entity.pentagram.scale.set(PENTAGRAM_ANIMATION_SCALE)
      }

      entity.shield = null
      if (currentData.shielded.includes(id) || previousData.shielded.includes(id)) {
        const fx = this.getFromPool(EFFECT_SHIELD)
        fx.visible = true
        entity.shield = fx

        fx.scale.set(1)
        fx.play()
        if (!currentData.shielded.includes(id)) {
          fx.alpha = 1 - progress
        }
      }

      if (entity.update) {
        entity.update(previousData, currentData, progress)
      }
    }
    for (const id of removedEntites) {
      const e = this.entities[id]
      if (e.parent) {
        e.parent.removeChild(e)
        e.pentagram.parent.removeChild(e.pentagram)
        e.debug.parent.removeChild(e.debug)
        e.debug.destroy(true)
        e.pentagram.destroy()
        if (e.remove) {
          e.remove()
        }
        e.destroy()
        e.animate = () => true
      }
      delete this.entities[id]
    }
  }

  unconvertPosition (pos) {
    const padding = this.padding ?? { top: 0, left: 0, right: 0, bottom: 0 }
    return {
      x: ((pos.x - padding.left) / (WIDTH - padding.left - padding.right)) * this.globalData.width,
      y: ((pos.y - padding.top) / (HEIGHT - padding.top - padding.bottom)) * this.globalData.height
    }
  }

  convertPosition (pos) {
    const padding = this.padding ?? { top: 0, left: 0, right: 0, bottom: 0 }
    return {
      x: pos.x / this.globalData.width * (WIDTH - padding.left - padding.right) + padding.left,
      y: pos.y / this.globalData.height * (HEIGHT - padding.top - padding.bottom) + padding.top
    }
  }

  convertDistance (x) {
    const padding = this.padding ?? { top: 0, left: 0, right: 0, bottom: 0 }
    return x / this.globalData.width * (WIDTH - padding.left - padding.right)
  }

  handleFrameData (frameInfo, raw) {
    const data = parseData(raw)
    return this._handleFrameData(frameInfo, data)
  }

  _handleFrameData (frameInfo, {
    positions,
    spawns,
    attacks,
    baseAttacks,
    spellUses,
    mana,
    mobHealth,
    baseHealth,
    messages,
    controlled,
    pushed,
    shielded
  }) {
    const previous = this.states[this.states.length - 1] || {
      mana: {},
      mobHealth: {},
      baseHealth: {},
      controlled: [],
      spellUses: []
    }
    const state = {
      number: frameInfo.number,
      spawns,
      positions,
      attacks,
      baseAttacks,
      controlled,
      pushed,
      shielded,
      mana: {
        ...previous.mana,
        ...mana
      },
      mobHealth: {
        ...previous.mobHealth,
        ...mobHealth
      },
      baseHealth: {
        ...previous.baseHealth,
        ...baseHealth
      },
      spellUses,
      messages,
      previous
    }
    for (const e of spawns) {
      this.entityMap[e.id] = e
    }
    this.states.push(state)
    return state
  }

  newLayer () {
    return new PIXI.Container()
  }

  initDebugMask () {
    const mask = new PIXI.Graphics()
    const topLeft = this.convertPosition({ x: 0, y: 0 })
    mask.lineStyle(api.atLeastOnePixel(2))
    mask.beginFill()
    mask.drawRect(topLeft.x, topLeft.y, this.convertDistance(this.globalData.width), this.convertDistance(this.globalData.height))
    mask.endFill()
    return mask
  }

  initDebugOverlay (layer) {
    const veil = new PIXI.Sprite(PIXI.Texture.WHITE)
    const gameZone = new PIXI.Graphics()
    const topLeft = this.convertPosition({ x: 0, y: 0 })

    gameZone.lineStyle(api.atLeastOnePixel(2), 0xFFFFFF, 1)

    gameZone.drawRect(topLeft.x, topLeft.y, this.convertDistance(this.globalData.width), this.convertDistance(this.globalData.height))

    veil.width = WIDTH
    veil.height = HEIGHT
    veil.tint = 0x0
    veil.alpha = 0.5

    Object.defineProperty(layer, 'visible', { get: () => this.isDebugMode() })
    Object.defineProperty(this.debugMobLayer, 'visible', { get: () => this.isDebugMode() })
    Object.defineProperty(this.debugHeroLayer, 'visible', { get: () => this.isDebugMode() })
    Object.defineProperty(this.debugBasesLayer, 'visible', { get: () => this.isDebugMode() })

    this.debugBasesLayer.alpha = 0.35

    layer.addChild(veil)
    layer.addChild(gameZone)
  }

  initDebugBases (layer) {
    for (let i = 0; i < this.globalData.playerCount; ++i) {
      const base = new PIXI.Graphics()
      const pos = this.convertPosition(this.globalData.basePositions[i])
      base.beginFill(this.globalData.players[i].color)
      base.drawCircle(pos.x, pos.y, this.convertDistance(this.globalData.baseAttractionRadius))
      base.endFill()
      base.beginFill(0xffffff)
      base.drawCircle(pos.x, pos.y, this.convertDistance(this.globalData.baseRadius))
      base.endFill()
      layer.addChild(base)
    }
  }

  reinitAspectRatio () {
    this.padding = {
      top: 130,
      bottom: 25
    }
    this.hudHeight = this.padding.top

    const desiredRatio = this.globalData.width / this.globalData.height
    const calculatedWidth = (HEIGHT - this.padding.top - this.padding.bottom) * desiredRatio
    const sidePadding = (WIDTH - calculatedWidth) / 2

    this.padding.left = this.padding.right = sidePadding
  }

  reinitScene (container, canvasData) {
    this.oversampling = canvasData.oversampling

    this.reinitAspectRatio()
    if (!this.globalData.enableFog) {
      api.options.enableFog = false
      api.options.fogOfWar = -1
    }

    api.toPixel = (WIDTH / canvasData.width) * canvasData.oversampling
    this.container = container
    api.entities = this.entities = {}
    this.pool = {}
    this.renderables = []
    this.updatables = []

    const background = PIXI.Sprite.from('Background.jpg')
    // const hudBackground = PIXI.Sprite.from('HUD_background.png')
    const hudBackground = new PIXI.Container()

    const sideData = [
      {
        sprite: 'left',
        x: 0,
        y: HEIGHT,
        anchor: {
          x: 0,
          y: 1
        }
      },
      {
        sprite: 'right',
        x: WIDTH,
        y: 180,
        anchor: {
          x: 1,
          y: 0
        }
      },
      {
        sprite: 'top',
        rotation: -Math.PI / 2,
        x: WIDTH,
        y: 0,
        anchor: {
          x: 1,
          y: 1
        }
      },
      {
        sprite: 'bottom',
        rotation: -Math.PI / 2,
        x: 0,
        y: HEIGHT,
        anchor: {
          x: 0,
          y: 0
        }
      }]
    for (let i = 0; i < 4; ++i) {
      const data = sideData[i]
      const side = PIXI.Sprite.from(data.sprite)
      hudBackground.addChild(side)
      side.x = data.x
      side.y = data.y
      side.rotation = data.rotation || 0
      side.anchor.x = data.anchor.x
      side.anchor.y = data.anchor.y
      window.module[data.sprite] = side
    }

    const hudAvatarFrames = [PIXI.Sprite.from('HUD_avatar_frames_left'), PIXI.Sprite.from('HUD_avatar_frames_right')]
    hudAvatarFrames[1].anchor.x = 1
    hudAvatarFrames[1].x = WIDTH

    const messageLayer = this.asLayer(initMessages)

    this.fogLayer = this.asLayer(this.initFog)
    Object.defineProperty(this.fogLayer, 'visible', { get: () => api.options.fogOfWar >= 0 })

    this.heroLayer = this.newLayer()
    this.mobLayer = this.newLayer()
    this.uiLayer = this.newLayer()
    this.pentagramLayer = this.newLayer()
    this.effectsLayer = this.newLayer()
    this.impactLayer = this.newLayer()
    this.pentagramContainer = this.newLayer()
    this.effectContainer = this.newLayer()
    this.impactContainer = this.newLayer()
    const hudLayer = this.asLayer(this.initHud)

    /* debug mode */
    const debugMask = this.initDebugMask()
    this.debugHeroLayer = this.newLayer()
    this.debugMobLayer = this.newLayer()
    this.debugBasesLayer = this.asLayer(this.initDebugBases)
    this.debugPentagramContainer = this.newLayer()
    this.debugImpactContainer = this.newLayer()
    this.debugEffectContainer = this.newLayer()
    const debugOverlay = this.asLayer(this.initDebugOverlay)
    this.debugMobLayer.mask = debugMask
    this.debugBasesLayer.mask = debugMask
    debugOverlay.mask = debugMask
    /*           */

    this.container.addChild(background)
    this.container.addChild(this.pentagramContainer)
    this.container.addChild(this.mobLayer)
    this.container.addChild(this.impactContainer)
    this.container.addChild(this.heroLayer)
    this.container.addChild(this.effectContainer)
    this.container.addChild(hudBackground)

    this.container.addChild(debugOverlay)
    this.container.addChild(this.debugBasesLayer)
    this.container.addChild(this.fogLayer)
    this.container.addChild(this.debugPentagramContainer)
    this.container.addChild(this.debugEffectContainer)
    this.container.addChild(this.debugImpactContainer)
    this.container.addChild(this.uiLayer)
    this.container.addChild(this.debugMobLayer)
    this.container.addChild(this.debugHeroLayer)
    this.container.addChild(debugMask)

    this.container.addChild(messageLayer)
    this.container.addChild(hudLayer)
    this.container.addChild(hudAvatarFrames[0])
    this.container.addChild(hudAvatarFrames[1])

    messageLayer.interactiveChildren = false
    hudLayer.interactiveChildren = false
    this.uiLayer.interactiveChildren = false
    this.effectsLayer.interactiveChildren = false
    this.impactLayer.interactiveChildren = false
    this.fogLayer.interactiveChildren = false

    api.setDebug = (value) => {
      const swaps = [
        {
          container: this.impactContainer,
          debugContainer: this.debugImpactContainer,
          layer: this.impactLayer
        },
        {
          container: this.effectContainer,
          debugContainer: this.debugEffectContainer,
          layer: this.effectsLayer
        },
        {
          container: this.pentagramContainer,
          debugContainer: this.debugPentagramContainer,
          layer: this.pentagramLayer
        }
      ]

      for (const swap of swaps) {
        swap.container.removeChild(swap.layer)
        swap.debugContainer.removeChild(swap.layer)
        const containerToAddTo = value ? swap.debugContainer : swap.container
        containerToAddTo.addChild(swap.layer)
      }
    }

    api.setDebug(this.isDebugMode())
  }

  isDebugMode () {
    return this.globalData?.demo ? false : api.options.debugMode
  }

  getIsFogActiveDescriptor (index) {
    return {
      get: () => api.options.fogOfWar === 2 || api.options.fogOfWar === index
    }
  }

  initDisc (radius) {
    return (layer) => {
      const normal = PIXI.Sprite.from('disc_black')
      const debug = new PIXI.Graphics()

      debug.beginFill()
      debug.drawCircle(0, 0, this.convertDistance(radius))
      debug.endFill()

      Object.defineProperty(normal, 'visible', { get: () => !this.isDebugMode() })
      Object.defineProperty(debug, 'visible', { get: () => this.isDebugMode() })

      layer.addChild(normal)
      layer.addChild(debug)
      normal.scale.set(this.convertDistance(radius) / DISC_RADIUS)
      normal.anchor.set(0.5)
    }
  }

  initFog (layer) {
    if (this.canvasMode) {
      return
    }

    const cloudLayer = this.asLayer((layer) => {
      const fog = PIXI.TilingSprite.from('fog_alpha', { width: WIDTH, height: HEIGHT }) as (PIXI.TilingSprite & Animatable)
      fog.alpha = 0.55
      fog.tint = 0xEDEDED

      const debugFog = new PIXI.Sprite(PIXI.Texture.WHITE)
      debugFog.width = this.convertDistance(this.globalData.width)
      debugFog.height = this.convertDistance(this.globalData.height)
      debugFog.tint = 0
      debugFog.alpha = 0.8
      debugFog.position.copyFrom(this.convertPosition({ x: 0, y: 0 }))

      Object.defineProperty(fog, 'visible', { get: () => !this.isDebugMode() })
      Object.defineProperty(debugFog, 'visible', { get: () => this.isDebugMode() })

      fog.animate = function (step) {
        this.tilePosition.x += step / 100
      }
      this.renderables.push(fog)
      layer.addChild(fog)
      layer.addChild(debugFog)
    })

    const discLayer = this.newLayer()
    const backdrop = new PIXI.Sprite(PIXI.Texture.WHITE)
    const discs = [[], []]
    const texture = PIXI.RenderTexture.create({ width: WIDTH * FOG_SCALE, height: HEIGHT * FOG_SCALE })
    flagForDestructionOnReinit(backdrop)
    const mask = new PIXI.Sprite(texture) as PIXI.Sprite & Updatable

    backdrop.width = WIDTH
    backdrop.height = HEIGHT
    discLayer.addChild(backdrop)
    discLayer.scale.set(FOG_SCALE)

    for (let i = 0; i < this.globalData.playerCount; ++i) {
      const baseDisc = this.asLayer(this.initDisc(this.globalData.baseViewRadius))
      const pos = this.convertPosition({
        x: this.globalData.width * i,
        y: this.globalData.height * i
      })
      baseDisc.position.copyFrom(pos)
      discLayer.addChild(baseDisc)
      Object.defineProperty(baseDisc, 'visible', this.getIsFogActiveDescriptor(i))

      for (let k = 0; k < this.globalData.heroesPerPlayer; ++k) {
        const disc = this.asLayer(this.initDisc(this.globalData.heroViewRadius))
        discs[i][k] = disc
        discLayer.addChild(disc)
        Object.defineProperty(disc, 'visible', this.getIsFogActiveDescriptor(i))
      }
    }
    mask.scale.set(1 / FOG_SCALE)
    this.updatables.push(mask)

    this.renderFog = () => {
      if (!(backdrop as any)._destroyed) {
        getRenderer().render(discLayer, texture)
      }
    }
    this.api.renderFog = this.renderFog

    mask.update = (from, to, progress) => {
      for (let i = 0; i < this.globalData.playerCount; ++i) {
        for (let k = 0; k < this.globalData.heroesPerPlayer; ++k) {
          const id = k + this.globalData.heroesPerPlayer * i
          const pos = utils.lerpPosition(from.positions[id], to.positions[id], progress)
          const disc = discs[i][k]
          disc.position.copyFrom(this.convertPosition(pos))
        }
      }

      this.renderFog()
    }

    layer.addChild(cloudLayer)
    layer.addChild(mask)
    cloudLayer.mask = mask
  }

  initHud (layer) {
    this.hud = [{}, {}]

    const overlay = PIXI.Sprite.from('HUD')
    layer.addChild(overlay)

    const logo = PIXI.Sprite.from('codingame.png')
    layer.addChild(logo)
    logo.anchor.x = 0.5
    logo.scale.set(0.5)
    logo.position.set(WIDTH / 2 - 2, 32)

    for (let i = 0; i < this.globalData.playerCount; ++i) {
      const playerHud = this.asLayer(this.initPlayerHud(i))
      playerHud.position.set(i * WIDTH, 0)

      layer.addChild(playerHud)
    }
  }

  initPlayerHud (index) {
    const player = this.globalData.players[index]
    return (layer) => {
      const flip = index ? -1 : 1

      const avatar = PIXI.Sprite.from('$' + index)
      avatar.position.set(5 * flip, 5)
      avatar.width = 93
      avatar.height = 93
      avatar.anchor.x = index

      const bannerWidth = 340

      const name = new PIXI.Text(player.name)
      name.style.fill = 0xFFFFFF
      name.style.fontWeight = 'bold'
      name.style.fontSize = 34
      name.style.fontFamily = 'Lato'
      name.anchor.x = 0.5
      name.anchor.y = 0.5
      name.x = (154 + bannerWidth / 2) * flip
      name.y = 18

      const coeff = utils.fitAspectRatio(name.width, name.height, bannerWidth, 40)
      name.scale.set(Math.min(1, coeff))

      const lifeBlock = new PIXI.Container()

      const lifeLabel = new PIXI.Text('Life')
      lifeLabel.style.fill = 0xFFFFFF
      lifeLabel.style.fontSize = 26
      lifeLabel.style.fontFamily = 'Lato'
      lifeLabel.position.set(0, 14)
      lifeLabel.anchor.y = 0.5

      const manaBlock = new PIXI.Container()

      const manaLabel = new PIXI.Text('Mana')
      manaLabel.style.fill = 0xFFFFFF
      manaLabel.style.fontSize = 26
      manaLabel.style.fontFamily = 'Lato'
      manaLabel.position.set(0, 0)
      manaLabel.anchor.y = 0.5

      const mana = new PIXI.Text('0')
      mana.style.fontWidth = 'bold'
      mana.style.fill = 0x00aef1
      mana.style.fontSize = 42
      mana.style.fontFamily = 'Lato'
      mana.position.set(138, 22)
      mana.anchor.set(1)

      if (index === 0) {
        lifeBlock.position.set(157, 64)
        manaBlock.position.set(356, 78)
      } else {
        lifeBlock.position.set(-494, 64)
        manaBlock.position.set(-294, 78)
      }

      this.hud[index].mana = mana
      this.hud[index].hearts = []
      const hearts = new PIXI.Container()
      for (let i = 0; i < 3; ++i) {
        const heart = PIXI.AnimatedSprite.fromFrames(LIFE_FRAMES);
        (heart.textures as PIXI.Texture[]).push(PIXI.Texture.EMPTY)
        heart.loop = false
        heart.anchor.x = heart.anchor.y = 0.5
        heart.position.set(82 + i * 34, 21)
        this.hud[index].hearts.push(heart)
        hearts.addChild(heart)
      }

      lifeBlock.addChild(lifeLabel)
      lifeBlock.addChild(hearts)
      manaBlock.addChild(manaLabel)
      manaBlock.addChild(mana)

      layer.addChild(avatar)
      layer.addChild(name)
      layer.addChild(lifeBlock)
      layer.addChild(manaBlock)
    }
  }

  initBases (layer) {
    this.bases = []
    for (let i = 0; i < this.globalData.playerCount; ++i) {
      const pos = this.globalData.basePositions[i]
      const base = new PIXI.Graphics() as PIXI.Graphics & {index: number}
      base.lineStyle(4, 0xFFFFFF, 1)
      base.drawCircle(0, 0, this.convertDistance(this.globalData.baseRadius))
      base.index = i
      base.position.copyFrom(this.convertPosition(pos))

      base.beginFill(this.globalData.players[i].color, 0.2)
      base.drawCircle(0, 0, this.convertDistance(this.globalData.baseAttractionRadius))
      base.endFill()
      this.bases.push(base)
      layer.addChild(base)
    }
  }

  isHero (entity: Mob | Hero, type: number): entity is Hero {
    return (type === TYPE_HERO_0 || type === TYPE_HERO_1)
  }

  initEntity (id) {
    const entityData = this.entityMap[id]
    const type = entityData.type
    const pentagram = PIXI.Sprite.from('controlled')
    this.pentagramLayer.addChild(pentagram)

    pentagram.anchor.set(0.5)
    pentagram.alpha = 0.8

    let entity: Hero | Mob
    if (this.isHero(entity, type)) {
      entity = this.initHero(id, entityData)
    } else if (type === TYPE_MOB) {
      entity = this.initMob(id, entityData)
    } else {
      console.error('unrecognised type', type)
    }
    entity.pentagram = pentagram
    entity.interactive = true
    const mouseover = api.getMouseOverFunc(id, api.tooltip) 
    const mouseout = api.getMouseOutFunc(id, api.tooltip)
    entity.mousemove = (event) => {
      if (entity.hitbox.containsPoint(event.data.global)) {
        mouseover()
      } else {
        mouseout()
      }
    }
    entity.targetPosition = null
    entity.targetRotation = null

    entity.animate = (step) => {
      if (this.playerSpeed === 0 && Math.abs(this.frameDelta) <= 6) {
        const stepFactor = Math.pow(0.983, step)
        entity.x = entity.x * stepFactor + entity.targetPosition.x * (1 - stepFactor)
        entity.y = entity.y * stepFactor + entity.targetPosition.y * (1 - stepFactor)
      } else {
        entity.position.copyFrom(entity.targetPosition)
      }
      entity.debug.position.copyFrom(entity.position)
      if (entity.rotation !== entity.targetRotation) {
        const eps = 0.02
        let r = utils.lerpAngle(entity.rotation, entity.targetRotation, 0.133)
        if (angleDiff(r, entity.targetRotation) < eps) {
          r = entity.targetRotation
        }
        entity.rotation = r
      }

      if (this.isHero(entity, type)) {
        entity.walk.visible = false
        entity.idle.visible = false
        entity.cast.visible = false
        entity.combat.visible = false

        if (entity.casting) {
          const towards = entity.casting.target ? this.entities[entity.casting.target].position : this.convertPosition(entity.casting.destination)
          const angle = Math.atan2(towards.y - entity.y, towards.x - entity.x)
          if (entity.targetRotation === null) {
            entity.rotation = angle
          }
          entity.targetRotation = angle
        }

        const combatStartsAt = 0.25
        if (entity.attacking && api.progress > combatStartsAt) {
          entity.combat.visible = true
          let idx = Math.floor(utils.lerp(0, entity.combat.totalFrames + 1, utils.unlerp(combatStartsAt, 1, api.progress)))
          if (idx === entity.combat.totalFrames) {
            idx--
          }
          entity.combat.gotoAndStop(idx)
        } else if (entity.casting) {
          entity.cast.visible = true
        } else if (entity.walking) {
          entity.walk.visible = true
        } else {
          entity.idle.visible = true
        }
      } else {
        if (entity.attacking) {
          const scale = utils.lerp(1, 1.5, utils.unlerp(-1, 1, Math.cos(((this.playerSpeed * 10) || 10) * entity.time / 400 - Math.PI)))
          entity.time += step
          entity.scale.set(scale)
          entity.debug.scale.set(scale)
        } else {
          entity.time = 0
          entity.scale.set(1)
          entity.debug.scale.set(1)
        }
      }

      if (entity.pentagram && entity.pentagram.visible) {
        entity.pentagram.rotation += step / 900
        entity.pentagram.position.copyFrom(entity.position)
      }

      if (entity.shield) {
        entity.shield.position.copyFrom(entity.position)
      }
      return false
    }

    this.renderables.push(entity)

    return entity
  }

  initDebugEntity (data) {
    const radius = 18 / 2
    const squareRadius = 40 / 2
    const id = data.id
    let color

    if (data.type === TYPE_MOB) {
      const colorIndex = this.getMobColorIndex(data)
      color = [0xfbd83f, 0xd33fe5, 0xe8a6a7][colorIndex]
    } else {
      color = this.globalData.players[data.type].color
    }

    return (layer) => {
      const background = new PIXI.Graphics()
      background.lineStyle(api.atLeastOnePixel(1), 0x0, 1)
      background.beginFill(color, 1)
      if (data.type === TYPE_MOB) {
        background.drawRect(-squareRadius, -squareRadius, squareRadius, squareRadius)
        background.pivot.set(-squareRadius / 2)
        background.rotation = (Math.PI / 32) * id
      } else {
        background.drawCircle(0, 0, radius)
      }
      background.endFill()

      layer.addChild(background)
    }
  }

  getMobColorIndex (entityData) {
    let colorIndex
    if (entityData.health > 18) {
      colorIndex = 2
    } else if (entityData.health > 13) {
      colorIndex = 1
    } else {
      colorIndex = 0
    }
    return colorIndex
  }

  initHero (id, entityData) {
    const hero = new PIXI.Container() as Hero
    const body = new PIXI.Container()
    const walk = PIXI.AnimatedSprite.fromFrames(HERO_FRAMES.RUN[entityData.type])
    const idle = PIXI.Sprite.from(HERO_FRAMES.IDLE[entityData.type])
    const cast = PIXI.Sprite.from(HERO_FRAMES.CAST[entityData.type])
    const combat = PIXI.AnimatedSprite.fromFrames(HERO_FRAMES.COMBAT[entityData.type])
    const debug = this.asLayer(this.initDebugEntity(entityData))

    idle.anchor.copyFrom(HERO_ANCHOR_STAND)
    walk.anchor.copyFrom(HERO_ANCHOR_RUN)
    cast.anchor.copyFrom(HERO_ANCHOR_CAST)
    combat.anchor.copyFrom(HERO_ANCHOR_COMBAT)
    body.rotation = Math.PI / 2
    walk.gotoAndPlay(utils.randInt(walk.textures.length))

    const hitbox = PIXI.Sprite.from('M2_OK0017')
    hitbox.anchor.set(0.5)
    hitbox.scale.set(1.5)
    hitbox.alpha = 0
    body.addChild(hitbox)
    hero.hitbox = hitbox
    hero.walk = walk
    hero.idle = idle
    hero.cast = cast
    hero.combat = combat
    hero.debug = debug

    body.addChild(walk)
    body.addChild(idle)
    body.addChild(cast)
    body.addChild(combat)
    body.hitArea = new PIXI.Circle(0, 0, 20)
    this.heroLayer.addChild(hero)
    this.debugHeroLayer.addChild(debug)
    hero.addChild(body)

    // XXX: This will break as soon as entities are ordered any differently
    const playerIdx = id < this.globalData.heroesPerPlayer ? 0 : 1
    const heroIdx = id % this.globalData.heroesPerPlayer

    hero.update = (previousData, currentData, progress) => {
      const message = this.messages[playerIdx][heroIdx]
      message.position.copyFrom(hero.targetPosition)
      message.x += messageBox.offset.x
      message.y += messageBox.offset.y
      var minPad = 10
      var scale = {
        x: (message.messageText.width + minPad) / messageBox.width,
        y: (message.messageText.height + minPad) / 80
      }

      message.messageBackground.scale.x = Math.max(1, scale.x)
      message.messageBackground.scale.y = Math.max(1, scale.y)

      var text = currentData.messages[id]
      if (text) {
        message.messageText.text = text

        // Shorten message if it doesn't fit in two lines
        let shortened = false
        while (message.messageText.height > 100) {
          message.messageText.text = message.messageText.text.substring(0, message.messageText.text.length - 1)
          shortened = true
        }
        if (shortened) {
          message.messageText.text = message.messageText.text.substring(0, message.messageText.text.length - 2) + '...'
        }

        message.visible = true
      } else {
        message.visible = false
      }
    }

    return hero
  }

  initLifeBar (mob) {
    return (layer) => {
      const frame = new PIXI.Graphics()
      frame.lineStyle(api.atLeastOnePixel(2), 0x0, 1)
      frame.drawRect(0, 0, mob.health * 70 / 24, 8)
      frame.visible = this.oversampling >= 2

      const back = new PIXI.Sprite(PIXI.Texture.WHITE)
      back.tint = 0xed1c24
      back.height = 8
      back.width = frame.width - 2

      const bar = new PIXI.Sprite(PIXI.Texture.WHITE) as PIXI.Sprite & {baseWidth: number}
      bar.tint = 0x00ff00
      bar.height = 8
      bar.baseWidth = frame.width - 1

      layer.bar = bar
      layer.pivot.set(frame.width / 2, 46)

      layer.addChild(back)
      layer.addChild(bar)
      layer.addChild(frame)
    }
  }

  initMob (id, entityData) {
    const mob = new PIXI.Container() as Mob
    let spiderAnchor = { x: 0.5, y: 0.5 }

    const colorIndex = this.getMobColorIndex(entityData)
    if (colorIndex === 0) {
      spiderAnchor = { x: 0.47, y: 0.45 }
    }
    const frames = [MOB_1_FRAMES, MOB_2_FRAMES, MOB_3_FRAMES][colorIndex]

    const body = new PIXI.Container()
    const walk = PIXI.AnimatedSprite.fromFrames(frames)
    const death = PIXI.AnimatedSprite.fromFrames(MOB_DEATH_FRAMES[colorIndex])
    const life = this.asLayer(this.initLifeBar(entityData)) as PIXI.Container & {bar: PIXI.Sprite & {baseWidth: number}}

    const debug = this.asLayer(this.initDebugEntity(entityData))
    
    walk.anchor.copyFrom(spiderAnchor)
    walk.animationSpeed = 0.5
    walk.gotoAndPlay(utils.randInt(walk.textures.length))
    body.rotation = 3 * Math.PI / 2
    body.scale.set(1.5)

    death.anchor.copyFrom(spiderAnchor)
    death.scale.set(1.5)
    death.animationSpeed = 0.5
    death.visible = false

    mob.debug = debug
    mob.body = body
    const hitbox = PIXI.Sprite.from('M2_OK0017')
    hitbox.anchor.set(0.5)
    hitbox.alpha = 0
    body.addChild(hitbox)
    mob.hitbox = hitbox
    mob.walk = walk
    mob.time = 0

    body.addChild(walk)
    body.hitArea = new PIXI.Circle(0, 0, 20)
    mob.addChild(body)
    mob.addChild(death)
    this.mobLayer.addChild(mob)
    this.uiLayer.addChild(life)
    this.debugMobLayer.addChild(debug)

    mob.update = (previousData, currentData, progress) => {
      life.visible = false
      mob.body.visible = true
      death.visible = false

      const prevHealth = previousData.mobHealth[id]
      const curHealth = currentData.mobHealth[id]

      // Life bar
      if (curHealth < entityData.health && prevHealth > 0) {
        life.visible = true
        const prevHealth = previousData.mobHealth[id]
        const showHealth = utils.lerp(prevHealth, curHealth, progress)
        life.bar.width = utils.unlerp(0, entityData.health, showHealth) * life.bar.baseWidth
        life.position.copyFrom(mob.targetPosition)
      }

      // Splatter
      mob.body.alpha = 1
      mob.debug.alpha = 1
      if (curHealth <= 0 && prevHealth > 0) {
        mob.body.alpha = 1 - progress
        mob.body.visible = true
        death.visible = true
        let idx = Math.floor(progress * death.textures.length)
        idx = Math.min(death.textures.length - 1, idx)
        death.gotoAndStop(idx)
      } else if (curHealth <= 0 && prevHealth <= 0) {
        mob.debug.alpha = 0
        mob.body.visible = false
        death.visible = true
        death.gotoAndStop(death.totalFrames - 1)
      }
    }

    mob.remove = () => {
      life.parent.removeChild(life)
      life.destroy({ children: true })
    }

    return mob
  }

  animateScene (delta) {
    const next = []
    for (const renderable of this.renderables) {
      if (!renderable.animate(delta)) {
        next.push(renderable)
      }
    }
    this.renderables = next
  }

  handleGlobalData (players, raw) {
    const globalData = parseGlobalData(raw)

    return this._handleGlobalData(players, globalData)
  }

  _handleGlobalData (players, globalData) {
    this.globalData = {
      ...globalData,
      players: players,
      playerCount: players.length
    }
    api.globalData = globalData
    api.options.meInGame = !!players.find(p => p.isMe)
  }
}

function distance (a, b) {
  return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2))
}

function angleDiff (a, b) {
  return Math.abs(utils.lerpAngle(a, b, 0) - utils.lerpAngle(a, b, 1))
}
