#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <math.h>

enum EntityType
{
    Monster = 0,
    Hero,
    OpponentsHero
};

enum ThreatType
{
    Neither = 0,
    Yours,
    Opponent
};
struct Entity
{
    int ID = 0; // Unique identifier
    EntityType Type = Monster; // 0=monster, 1=your hero, 2=opponent hero
    int X = 0; // Position of this entity
    int Y = 0;
    int ShieldLife = 0; // Ignore for this league; Count down until shield spell fades
    int IsControlled = 0; // Ignore for this league; Equals 1 when this entity is under a control spell
    int Health = 0; // Remaining health of this monster
    int TrajectoryX = 0; // Trajectory of this monster
    int TrajectoryY = 0;
    int NearBase = 0; // 0=monster with no target yet, 1=monster targeting a base
    ThreatType Threat = Neither; // Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither
};

struct BaseType
{
    int x = 0;
    int y = 0;
};

std::vector<Entity> Entities;
BaseType Base;

// Distance between two positions
// 
float Distance(int ax1, int ay1, int ax2, int ay2)
{
    return static_cast<float>(sqrt(pow(ax2 - ax1, 2) + pow(ay2 - ay1, 2)));
}


bool ClosestToBase(Entity* aEntity1, Entity* aEntity2)
{
    return Distance(Base.x, Base.y, aEntity1->X, aEntity1->Y) < Distance(Base.x, Base.y, aEntity2->X, aEntity2->Y);
}

int main()
{
    std::cin >> Base.x >> Base.y; std::cin.ignore();
    int heroesPerPlayer; // Always 3
    std::cin >> heroesPerPlayer; std::cin.ignore();

    while (true)
    {
        for (int i = 0; i < 2; i++)
        {
            int health; // Your base health
            int mana; // Ignore in the first league; Spend ten mana to cast a spell
            std::cin >> health >> mana; std::cin.ignore();
        }

        int entityCount; // Amount of heros and monsters you can see
        std::cin >> entityCount; std::cin.ignore();

        std::vector<Entity*> ClosestEnemies;
        for (int i = 0; i < entityCount; i++)
        {
            Entity entity;
            int type = 0;
            int threatType = 0;
            std::cin >> entity.ID >> type >> entity.X >> entity.Y >> entity.ShieldLife >> entity.IsControlled >> entity.Health >> entity.TrajectoryX >> entity.TrajectoryY >> entity.NearBase >> threatType; std::cin.ignore();
            entity.Type = static_cast<EntityType>(type);
            entity.Threat = static_cast<ThreatType>(threatType);

            Entities.push_back(entity);

            if (entity.Type == EntityType::Monster && entity.Threat == ThreatType::Yours)
            {
                ClosestEnemies.push_back(&Entities.back());
            }
        }

        // Sort the enemies to who is closest to the base
        std::sort(ClosestEnemies.begin(), ClosestEnemies.end(), ClosestToBase);

        for (int i = 0; i < heroesPerPlayer; i++) {

            if (ClosestEnemies.size() > i)
            {
                Entity& closestEnemy = *ClosestEnemies[i];
                // In the first league: MOVE <x> <y> | WAIT; In later leagues: | SPELL <spellParams>;
                std::cout << "MOVE " << closestEnemy.X << " " << closestEnemy.Y << std::endl;
                std::cerr << "DISTANCE: " << Distance(Base.x, Base.y, closestEnemy.X, closestEnemy.Y) << " Base: " << Base.x << ", " << Base.y << " Enemy: " << closestEnemy.X << ", " << closestEnemy.Y << std::endl;
            }
            else
            {
                std::cout << "WAIT " << std::endl;
            }
        }
    }
}