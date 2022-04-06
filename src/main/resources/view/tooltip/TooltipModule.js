import { WIDTH, HEIGHT } from '../core/constants.js'
import {api as ViewModule} from '../graphics/ViewModule.js'
/* global PIXI */

const TYPE_MOB = 2

ViewModule.getMouseOverFunc = function (id, tooltip) {
  return function () {
    tooltip.inside[id] = true
  }
}

ViewModule.getMouseOutFunc = function (id, tooltip) {
  return function () {
    delete tooltip.inside[id]
  }
}

function getMouseMoveFunc (tooltip, container, module) {
  return function (ev) {
    if (tooltip) {
      var pos = ev.data.getLocalPosition(container)
      tooltip.x = pos.x
      tooltip.y = pos.y

      var point = ViewModule.unconvertPosition(pos)
      point.x = Math.round(point.x)
      point.y = Math.round(point.y)
      // point.y = Math.min(ViewModule.globalData.height, Math.max(0, Math.round(point.y)))

      const showing = []
      const ids = Object.keys(tooltip.inside).map(n => +n)
      const tooltipBlocks = []

      for (let id of ids) {
        if (tooltip.inside[id]) {
          const entity = ViewModule.entities[id]
          if (!entity) {
            delete tooltip.inside[id]
          } else {
            showing.push(id)
          }
        }
      }

      if (showing.length) {
        for (let show of showing) {
          const entity = ViewModule.entities[show]
          if (entity) {
            let data = (ViewModule.progress === 1 ? ViewModule.currentData : ViewModule.previousData) || ViewModule.currentData
            let tooltipBlock
            if (ViewModule.entityMap[show].type === TYPE_MOB) {
              tooltipBlock = 'MONSTER ' + show
              tooltipBlock += '\nhealth: ' + data.mobHealth[show] + ' / ' + ViewModule.entityMap[show].health
            } else {
              tooltipBlock = 'HERO ' + show
            }
            if (data.positions[show] != null) {
              tooltipBlock += '\nx: ' + data.positions[show].x + '\ny: ' + data.positions[show].y
            }

            if (data.controlled.includes(show)) {
              tooltipBlock += '\nmind controlled'
            }
            if (data.shielded.includes(show)) {
              tooltipBlock += '\nshielded'
            }
            if (entity.casting) {
              const spell = entity.casting.spell
              tooltipBlock += '\ncasting ' + spell
              if (spell === 'CONTROL' || spell === 'WIND') {
                tooltipBlock += ' ' + entity.casting.destination.x + ' ' + entity.casting.destination.y
              }
              if (spell === 'CONTROL' || spell === 'SHIELD') {
                tooltipBlock += ' on ' + entity.casting.target
              }
            }

            tooltipBlocks.push(tooltipBlock)
          }
        }
      } else {
        if (point.y > 0 && point.y <= 9000) {
          tooltipBlocks.push('X: ' + point.x +
          '\nY: ' + point.y)
        }
      }

      if (tooltipBlocks.length) {
        tooltip.label.text = tooltipBlocks.join('\n──────────\n')
        tooltip.visible = true
      } else {
        tooltip.visible = false
      }

      tooltip.background.width = tooltip.label.width + 20
      tooltip.background.height = tooltip.label.height + 20

      tooltip.pivot.x = -30
      tooltip.pivot.y = -50

      if (tooltip.y - tooltip.pivot.y + tooltip.height > HEIGHT) {
        tooltip.pivot.y = 10 + tooltip.height
        tooltip.y -= tooltip.y - tooltip.pivot.y + tooltip.height - HEIGHT
      }

      if (tooltip.x - tooltip.pivot.x + tooltip.width > WIDTH) {
        tooltip.pivot.x = tooltip.width
      }
    }
  }
};

export class TooltipModule {
  constructor (assets) {
    this.interactive = {}
    this.previousFrame = {
      registrations: {},
      extra: {}
    }
    this.lastProgress = 1
    this.lastFrame = 0
  }

  static get moduleName () {
    return 'tooltips'
  }

  updateScene (previousData, currentData, progress) {
    this.currentFrame = currentData
    this.currentProgress = progress
  }

  handleFrameData (frameInfo) {
    return {}
  }

  reinitScene (container, canvasData) {
    this.tooltip = this.initTooltip()
    ViewModule.tooltip = this.tooltip
    this.container = container
    container.interactive = true
    container.mousemove = getMouseMoveFunc(this.tooltip, container, this)
    container.addChild(this.tooltip)
  }

  generateText (text, size, color, align) {
    var textEl = new PIXI.Text(text, {
      fontSize: Math.round(size / 1.2) + 'px',
      fontFamily: 'Lato',
      fontWeight: 'bold',
      fill: color
    })

    textEl.lineHeight = Math.round(size / 1.2)
    if (align === 'right') {
      textEl.anchor.x = 1
    } else if (align === 'center') {
      textEl.anchor.x = 0.5
    }

    return textEl
  };

  initTooltip () {
    var tooltip = new PIXI.Container()
    var background = tooltip.background = new PIXI.Graphics()
    var label = tooltip.label = this.generateText('', 36, 0xFFFFFF, 'left')

    background.beginFill(0x0, 0.7)
    background.drawRect(0, 0, 200, 185)
    background.endFill()
    background.x = -10
    background.y = -10

    tooltip.visible = false
    tooltip.inside = {}

    tooltip.addChild(background)
    tooltip.addChild(label)

    tooltip.interactiveChildren = false
    return tooltip
  };

  animateScene (delta) {

  }

  handleGlobalData (players, globalData) {

  }
}
