import {ViewModule, api} from './graphics/ViewModule.js'
import {TooltipModule} from './tooltip/TooltipModule.js'
import {EndScreenModule} from './endscreen/EndScreenModule.js'

// List of viewer modules that you want to use in your game
export const modules = [
  ViewModule,
  TooltipModule,
  EndScreenModule
]

export const playerColors = [
  '#22a1e4', // curious blue
  '#ff1d5c' // radical red
]

export const gameName = 'Spring2022'

export const options = [{
  title: 'DEBUG OVERLAY',
  get: function () {
    return api.options.debugMode
  },
  set: function (value) {
    api.options.debugMode = value
    api.setDebug(value)
    api.renderFog()
  },
  values: {
    'ON': true,
    'OFF': false
  }
}, {
  title: 'FOG OF WAR',
  get: function () {
    return api.options.fogOfWar
  },
  set: function (value) {
    api.options.fogOfWar = value
    api.renderFog()
  },
  enabled: function () {
    return api.options.enableFog
  },
  values: {
    'OFF': -1,
    '1': 0,
    '2': 1,
    'BOTH': 2
  }
}, {
  title: 'MY MESSAGES',
  get: function () {
    return api.options.showMyMessages
  },
  set: function (value) {
    api.options.showMyMessages = value
  },
  enabled: function () {
    return api.options.meInGame
  },
  values: {
    'ON': true,
    'OFF': false
  }
}, {
  title: 'OTHERS\' MESSAGES',
  get: function () {
    return api.options.showOthersMessages
  },
  set: function (value) {
    api.options.showOthersMessages = value
  },

  values: {
    'ON': true,
    'OFF': false
  }
}]
