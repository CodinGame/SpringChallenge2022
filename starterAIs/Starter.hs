import System.IO
import Control.Monad
import Debug.Trace

data Entity 
    = Monster EntityData MonsterData
    | MyHero EntityData
    | EnemyHero EntityData
  deriving Show

data EntityData =
  EntityData
    { entityId :: Int
    , entityX :: Int
    , entityY :: Int
    , entityShieldLife :: Int
    , entityIsControlled :: Bool
    }
  deriving Show

data MonsterData =
  MonsterData
    { monsterHealth :: Int
    , monsterVx :: Int
    , monsterVy :: Int
    , monsterNearBase :: Bool
    , monsterThreatFor :: ThreatFor
    }
  deriving Show

data ThreatFor = Neutral | MyBase | EnemyBase deriving (Show, Eq, Enum)

data Command = WAIT | MOVE Int Int | SPELL deriving Show

readInt :: IO Int
readInt = readLn

readInts :: IO [Int]
readInts = map read . words <$> getLine

makeEntity :: [Int] -> Entity
makeEntity [id_, type_, x, y, shieldLife, isControlled, health, vx, vy, nearBase, threatFor] =
    let
        properties = EntityData id_ x y shieldLife (isControlled == 1)
        monster = MonsterData health vx vy (nearBase == 1) (toEnum threatFor)
    in
    case type_ of
        0 -> Monster properties monster
        1 -> MyHero properties
        2 -> EnemyHero properties

main :: IO ()
main = do
    hSetBuffering stdout NoBuffering -- DO NOT REMOVE
    [baseX, baseY] <- readInts
    heroesPerPlayer <- readInt
    gameLoop heroesPerPlayer

gameLoop :: Int -> IO ()
gameLoop heroesPerPlayer = do
    [health, mana] <- readInts
    [enemyHealth, enemyMana] <- readInts
    entityCount <- readInt
    entities <- replicateM entityCount (makeEntity <$> readInts)
    -- mapM_ traceShowM entities
    replicateM heroesPerPlayer $ do
        putStrLn $ show WAIT
    gameLoop heroesPerPlayer
