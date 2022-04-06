import { flagForDestructionOnReinit } from '../core/rendering.js'

/* global PIXI */

export function wordWrap (text) {
  var wordWrapWidth = this._style.wordWrapWidth
  var self = this
  text = text.replace(/\w+/g, function (text) {
    if (self.context.measureText(text).width > wordWrapWidth) {
      var list = []
      while (text.length > 0) {
        var length = 1
        while (length <= text.length && self.context.measureText(text.slice(0, length)).width < wordWrapWidth) {
          length++
        }
        list.push(text.slice(0, length - 1))
        text = text.slice(length - 1)
      }
      return list.join('\n')
    }
    return text
  })
  return this._wordWrap(text)
}

export function renderMessageContainer (step) {
  var drawer = this.drawer
  var stepFactor = Math.pow(0.993, step)
  var targetMessageAlpha = 0
  targetMessageAlpha = (!drawer.hideMessages && ((drawer.showMyMessages() && drawer.playerInfo[this.player].isMe) || (drawer.showOthersMessages() && !drawer.playerInfo[this.player].isMe))) ? 1
    : 0
  this.alpha = this.alpha * stepFactor + targetMessageAlpha * (1 - stepFactor)
};

export const messageBox = {
  width: 200,
  offset: {
    x: 0,
    y: -50
  }
}

export function initMessages (layer) {
  this.messages = []

  const scope = {
    hudColor: 0x0,
    messageBox: messageBox
  }

  for (let i = 0; i < this.globalData.playerCount; ++i) {
    this.messages[i] = []
    const messageGroup = new PIXI.Container()
    const options = this.api.options
    const playerInfo = this.globalData.players
    Object.defineProperty(messageGroup, 'visible', {
      get: () => {
        return (options.showMyMessages && playerInfo[i].isMe) || (options.showOthersMessages && !playerInfo[i].isMe)
      }
    })

    for (let k = 0; k < this.globalData.heroesPerPlayer; ++k) {
      var messageContainer = new PIXI.Container() as PIXI.Container & {
        player: any
        drawer: any
        messageBackground: any
        messageText: any
        animate(): void
      }
      var messageBackground = new PIXI.Graphics()
      messageBackground.beginFill(scope.hudColor, /* scope.hudAlpha */0)
      messageBackground.drawRect(-scope.messageBox.width / 2, -40, scope.messageBox.width, 80)
      messageBackground.endFill()
      messageContainer.addChild(messageBackground)
      var textContainer = new PIXI.Container()
      messageContainer.addChild(textContainer)

      var messageText = new PIXI.Text('', {
        fontFamily: 'Lato',
        fontWeight: 700,
        fontSize: 40,
        fill: this.globalData.players[i].color,
        align: 'center',
        strokeThickness: 4,
        wordWrap: true,
        wordWrapWidth: 180
      }) as PIXI.Text & {

      }

      // TODO: remove this hack?
      messageText.style._wordWrap = messageText.style.wordWrap
      messageText.style.wordWrap = wordWrap

      messageText.anchor.x = 0.5
      messageText.anchor.y = 0.5
      messageText.x = 3
      messageText.y = -6

      flagForDestructionOnReinit(messageText)

      messageContainer.player = i

      messageContainer.messageText = messageText
      messageContainer.messageBackground = messageBackground

      messageContainer.addChild(messageText)
      messageContainer.scale.x = scope.messageBox.width / messageContainer.width
      messageContainer.scale.y = messageContainer.scale.x
      // messageContainer.render = renderMessageContainer;
      messageContainer.animate = () => {}
      messageContainer.drawer = this
      // scope.renderables.push(messageContainer);
      this.messages[i].push(messageContainer)

      messageGroup.addChild(messageContainer)
    }
    layer.addChild(messageGroup)
  }
};
