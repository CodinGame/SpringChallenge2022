import System.IO
import Control.Monad
import Debug.Trace

data Entity =
  Entity
    { entityId :: Int
    , entityType :: EntityType
    , entityX :: Int
    , entityY :: Int
    , entityShieldLife :: Int
    , entityIsControlled :: Bool
    , entityHealth :: Int
    , entityVx :: Int
    , entityVy :: Int
    , entityNearBase :: Bool
    , entityThreatFor :: Int
    }
    deriving Show

data EntityType = Monster | MyHero | EnemyHero deriving (Show, Eq, Enum)
data EntityThreatFor = Neutral | MyBase | EnemyBase deriving (Show, Eq, Enum)

data Command = WAIT | MOVE Int Int | SPELL deriving Show

readInt :: IO Int
readInt = readLn

readInts :: IO [Int]
readInts = map read . words <$> getLine

makeEntity :: [Int] -> Entity
makeEntity [id_, type_, x, y, shieldLife, isControlled, health, vx, vy, nearBase, threatFor] =
    Entity id_ (toEnum type_) x y shieldLife (isControlled == 1) health vx vy (nearBase == 1) (toEnum threatFor)

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
    -- traceShowM entities
    replicateM heroesPerPlayer $ do
        putStrLn $ show WAIT
    gameLoop heroesPerPlayer
