
export const EFFECT_HIT = 0
export const EFFECT_SHIELD = 2
export const EFFECT_BEAM = 3
export const EFFECT_GUST = 4
export const EFFECT_GLINT = 5
export const DISC_RADIUS = 218

export const GLINT_FRAMES = ['MagicLight.mov_0001', 'MagicLight.mov_0002', 'MagicLight.mov_0003', 'MagicLight.mov_0004', 'MagicLight.mov_0005', 'MagicLight.mov_0006', 'MagicLight.mov_0007']
export const BEAM_FRAMES = ['spell12.mov_0001', 'spell12.mov_0002', 'spell12.mov_0003', 'spell12.mov_0004', 'spell12.mov_0005', 'spell12.mov_0006', 'spell12.mov_0007', 'spell12.mov_0008', 'spell12.mov_0009', 'spell12.mov_0010', 'spell12.mov_0011', 'spell12.mov_0012', 'spell12.mov_0013', 'spell12.mov_0014', 'spell12.mov_0015']
export const GUST_FRAMES = ['gust_0002', 'gust_0003', 'gust_0004', 'gust_0005', 'gust_0006', 'gust_0007', 'gust_0008', 'gust_0009', 'gust_0010', 'gust_0011', 'gust_0012', 'gust_0013', 'gust_0014', 'gust_0015', 'gust_0016', 'gust_0017', 'gust_0018', 'gust_0019', 'gust_0020', 'gust_0021', 'gust_0022', 'gust_0023', 'gust_0024', 'gust_0025', 'gust_0026', 'gust_0027', 'gust_0028', 'gust_0029', 'gust_0030', 'gust_0031', 'gust_0032', 'gust_0033', 'gust_0034']
export const SHIELD_FRAMES = ['ShieldOrb.mov_0011', 'ShieldOrb.mov_0012', 'ShieldOrb.mov_0013', 'ShieldOrb.mov_0068', 'ShieldOrb.mov_0069', 'ShieldOrb.mov_0070', 'ShieldOrb.mov_0071', 'ShieldOrb.mov_0072', 'ShieldOrb.mov_0073', 'ShieldOrb.mov_0074', 'ShieldOrb.mov_0075', 'ShieldOrb.mov_0076', 'ShieldOrb.mov_0077', 'ShieldOrb.mov_0078', 'ShieldOrb.mov_0079', 'ShieldOrb.mov_0080', 'ShieldOrb.mov_0081', 'ShieldOrb.mov_0082', 'ShieldOrb.mov_0083', 'ShieldOrb.mov_0084', 'ShieldOrb.mov_0085', 'ShieldOrb.mov_0086', 'ShieldOrb.mov_0087', 'ShieldOrb.mov_0088', 'ShieldOrb.mov_0089', 'ShieldOrb.mov_0090', 'ShieldOrb.mov_0091', 'ShieldOrb.mov_0092', 'ShieldOrb.mov_0093', 'ShieldOrb.mov_0094', 'ShieldOrb.mov_0095', 'ShieldOrb.mov_0096', 'ShieldOrb.mov_0097']
export const LIFE_FRAMES = ['heart_OK0001', 'heart_OK0002', 'heart_OK0003', 'heart_OK0004', 'heart_OK0005', 'heart_OK0006', 'heart_OK0007', 'heart_OK0008', 'heart_OK0009', 'heart_OK0010', 'heart_OK0011', 'heart_OK0012', 'heart_OK0013', 'heart_OK0014', 'heart_OK0015', 'heart_OK0016', 'heart_OK0017', 'heart_OK0018', 'heart_OK0019', 'heart_OK0020', 'heart_OK0021', 'heart_OK0022', 'heart_OK0023', 'heart_OK0024', 'heart_OK0025', 'heart_OK0026', 'heart_OK0027', 'heart_OK0028', 'heart_OK0029', 'heart_OK0030', 'heart_OK0031', 'heart_OK0032', 'heart_OK0033', 'heart_OK0034', 'heart_OK0035', 'heart_OK0036', 'heart_OK0037', 'heart_OK0038', 'heart_OK0039', 'heart_OK0040', 'heart_OK0041', 'heart_OK0042', 'heart_OK0043']

const HERO_FRAME_NAMES = {
  RUN: ['Chasseur_$_OK0001', 'Chasseur_$_OK0002', 'Chasseur_$_OK0003', 'Chasseur_$_OK0004', 'Chasseur_$_OK0005', 'Chasseur_$_OK0006', 'Chasseur_$_OK0007', 'Chasseur_$_OK0008', 'Chasseur_$_OK0009', 'Chasseur_$_OK0010', 'Chasseur_$_OK0011', 'Chasseur_$_OK0012', 'Chasseur_$_OK0013', 'Chasseur_$_OK0014', 'Chasseur_$_OK0015', 'Chasseur_$_OK0016', 'Chasseur_$_OK0017', 'Chasseur_$_OK0018', 'Chasseur_$_OK0019', 'Chasseur_$_OK0020'],
  COMBAT: ['Chasseur_$_OK_Attak0003', 'Chasseur_$_OK_Attak0004', 'Chasseur_$_OK_Attak0005', 'Chasseur_$_OK_Attak0006', 'Chasseur_$_OK_Attak0007', 'Chasseur_$_OK_Attak0008', 'Chasseur_$_OK_Attak0009', 'Chasseur_$_OK_Attak0010', 'Chasseur_$_OK_Attak0011', 'Chasseur_$_OK_Attak0012', 'Chasseur_$_OK_Attak0013', 'Chasseur_$_OK_Attak0014', 'Chasseur_$_OK_Attak0015', 'Chasseur_$_OK_Attak0016', 'Chasseur_$_OK_Attak0017', 'Chasseur_$_OK_Attak0018', 'Chasseur_$_OK_Attak0019', 'Chasseur_$_OK_Attak0020', 'Chasseur_$_OK_Attak0021', 'Chasseur_$_OK_Attak0022', 'Chasseur_$_OK_Attak0023'],
  IDLE: 'Chasseur_$_OK_Stand',
  CAST: 'Chasseur_$_OK_Magik'
}
export const HERO_FRAMES = {} as {
  RUN: string[][]
  COMBAT: string[][]
  IDLE: string[]
  CAST: string[]
}

for (const key in HERO_FRAME_NAMES) {
  if (typeof HERO_FRAME_NAMES[key] === 'string') {
    HERO_FRAMES[key] = [
      HERO_FRAME_NAMES[key].replace(/\$/g, 'B'),
      HERO_FRAME_NAMES[key].replace(/\$/g, 'R')
    ]
  } else {
    HERO_FRAMES[key] = [
      HERO_FRAME_NAMES[key].map(v => v.replace(/\$/g, 'B')),
      HERO_FRAME_NAMES[key].map(v => v.replace(/\$/g, 'R'))
    ]
  }
}

export const MOB_1_FRAMES = ['M1_OK0001', 'M1_OK0002', 'M1_OK0003', 'M1_OK0004', 'M1_OK0005', 'M1_OK0006', 'M1_OK0007', 'M1_OK0008', 'M1_OK0009', 'M1_OK0010', 'M1_OK0011', 'M1_OK0012', 'M1_OK0013', 'M1_OK0014', 'M1_OK0015', 'M1_OK0016', 'M1_OK0017', 'M1_OK0018', 'M1_OK0019', 'M1_OK0020']
export const MOB_2_FRAMES = ['M2_OK0001', 'M2_OK0002', 'M2_OK0003', 'M2_OK0004', 'M2_OK0005', 'M2_OK0006', 'M2_OK0007', 'M2_OK0008', 'M2_OK0009', 'M2_OK0010', 'M2_OK0011', 'M2_OK0012', 'M2_OK0013', 'M2_OK0014', 'M2_OK0015', 'M2_OK0016', 'M2_OK0017', 'M2_OK0018', 'M2_OK0019', 'M2_OK0020']
export const MOB_3_FRAMES = ['M3_OK0001', 'M3_OK0002', 'M3_OK0003', 'M3_OK0004', 'M3_OK0005', 'M3_OK0006', 'M3_OK0007', 'M3_OK0008', 'M3_OK0009', 'M3_OK0010', 'M3_OK0011', 'M3_OK0012', 'M3_OK0013', 'M3_OK0014', 'M3_OK0015', 'M3_OK0016', 'M3_OK0017', 'M3_OK0018', 'M3_OK0019', 'M3_OK0020']

export const MOB_DEATH_FRAMES = [
  ['death_10001', 'death_10002', 'death_10003', 'death_10004', 'death_10005', 'death_10006', 'death_10007', 'death_10008', 'death_10009', 'death_10010', 'death_10011', 'death_10012', 'death_10013', 'death_10014', 'death_10015', 'death_10016', 'death_10017', 'death_10018', 'death_10019', 'death_10020', 'death_10021', 'death_10022', 'death_10023', 'death_10024', 'death_10025', 'death_10026', 'death_10027', 'death_10028'],
  ['death_20001', 'death_20002', 'death_20003', 'death_20004', 'death_20005', 'death_20006', 'death_20007', 'death_20008', 'death_20009', 'death_20010', 'death_20011', 'death_20012', 'death_20013', 'death_20014', 'death_20015', 'death_20016', 'death_20017', 'death_20018', 'death_20019', 'death_20020', 'death_20021', 'death_20022', 'death_20023', 'death_20024', 'death_20025', 'death_20026', 'death_20027', 'death_20028'],
  ['death0001', 'death0002', 'death0003', 'death0004', 'death0005', 'death0006', 'death0007', 'death0008', 'death0009', 'death0010', 'death0011', 'death0012', 'death0013', 'death0014', 'death0015', 'death0016', 'death0017', 'death0018', 'death0019', 'death0020', 'death0021', 'death0022', 'death0023', 'death0024', 'death0025', 'death0026', 'death0027', 'death0028']
]

export const HERO_ANCHOR_RUN = { x: 0.5, y: 0.5 }
export const HERO_ANCHOR_STAND = { x: 0.5, y: 0.5 }
export const HERO_ANCHOR_CAST = { x: 0.5, y: 0.5 }
export const HERO_ANCHOR_COMBAT = { x: 0.5, y: 0.5 }

export const FOG_SCALE = 0.4
export const PENTAGRAM_ANIMATION_PAD = 0.2
export const PENTAGRAM_ANIMATION_SCALE = 1.5

export const HIT_FRAMES = ['hit0001', 'hit0002', 'hit0003', 'hit0004', 'hit0005', 'hit0006', 'hit0007', 'hit0008', 'hit0009', 'hit0010', 'hit0011', 'hit0012', 'hit0013', 'hit0014', 'hit0015', 'hit0016', 'hit0017', 'hit0018', 'hit0019', 'hit0020', 'hit0021', 'hit0022']
