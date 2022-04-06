declare module '../core/*.js' {
  export const WIDTH: number
  export const HEIGHT: number
  export const BASE_FRAME_DURATION: number
  export function bell (t: number): number
  export function elastic (t: number): number
  export function ease (t: number): number
  export function easeIn (t: number): number
  export function easeOut (t: number): number
  export function easeInOut (t: number): number
  export function randInt (a: number, b?: number): number
  export function lerp (a: number, b: number, u: number): number
  export function unlerpUnclamped (a: number, b: number, v: number): number
  export function lerpAngle (start: number, end: number, amount: number, maxDelta?: number): number
  export function lerpPosition (from: {x: number, y: number}, to: {x: number, y: number}, p: number): {x: number, y: number}
  export function lerpColor (start: number, end: number, amount: number): number
  export function unlerp (a: number, b: number, v): number
  export function pushAll<T> (self: {push: (item: T) => void}, arr: T[]): void
  export function fitAspectRatio (srcWidth: number, srcHeight: number, maxWidth: number, maxHeight: number, padding?: number): number
  export function paddingString (word: string, width: number, char: string): string
  export function flagForDestructionOnReinit (destroyable: any): void
  export function getRenderer(): PIXI.Renderer
}
