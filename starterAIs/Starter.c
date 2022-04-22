#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <math.h>

typedef enum    T_ENTITY_TYPE {
				ENTITY_TYPE_MONSTER = 0,
				ENTITY_TYPE_MY_HERO = 1,
				ENTITY_TYPE_OP_HERO = 2
}               E_ENTITY_TYPE;

typedef struct  t_entity {
    int 		id;
    int 		type;
    int 		x;
    int 		y;
    int 		shield_life;
    int 		is_controlled;
    int 		health;
    int 		vx;
    int 		vy;
    int 		near_base;
    int 		threat_for;
}               s_entity;


int dbg_print_entity(s_entity *e) {
    fprintf(stderr, "================\n");
    switch (e->type) {
        case ENTITY_TYPE_MY_HERO: fprintf(stderr, "My hero %d:\n", e->id); break;
        case ENTITY_TYPE_OP_HERO: fprintf(stderr, "Op hero %d:\n", e->id); break;
        case ENTITY_TYPE_MONSTER: fprintf(stderr, "Monster %d:\n", e->id); break;
        default : fprintf(stderr, "print error: undefined entity type: %d\n", e->type);
    }
    fprintf(stderr, "x: %d   y: %d\n", e->x, e->y);
    fprintf(stderr, "shield_life: %d\n", e->shield_life);
    fprintf(stderr, "is_controlled: %d\n", e->is_controlled);
    fprintf(stderr, "health: %d\n", e->health);
    fprintf(stderr, "vx: %d   vy: %d\n", e->vx, e->vy);
    fprintf(stderr, "near_base: %d\n", e->near_base);
    fprintf(stderr, "threat_for: %d\n", e->threat_for);
    fprintf(stderr, "================\n\n");
    return (1);
}

int main()
{
    // The corner of the map representing your base
    int base_x;
    int base_y;
    scanf("%d%d", &base_x, &base_y);
	
    // Always 3
    int heroes_per_player;
    scanf("%d", &heroes_per_player);

    // game loop
    while (1) {
        for (int i = 0; i < 2; i++) {
            // Your base health
            int health;
            // Ignore in the first league; Spend ten mana to cast a spell
            int mana;
            scanf("%d%d", &health, &mana);
        }
        // Amount of heros and monsters you can see
        int entity_count;
        scanf("%d", &entity_count);

		//Keep track of how many monsters we've read
        int nb_monsters = 0;
        s_entity *entities = malloc(sizeof(s_entity) * entity_count);
        s_entity **monsters = malloc(sizeof(s_entity*) * entity_count);
        s_entity *my_heroes[heroes_per_player];
		s_entity *op_heroes[heroes_per_player];

        for (int i = 0; i < entity_count; i++) {
            //id: Unique identifier
            //type: 0=monster, 1=your hero, 2=opponent hero
            //x: Position of this entity
            //shieldLife: Ignore for this league; Count down until shield spell fades
            //isControlled: Ignore for this league; Equals 1 when this entity is under a control spell
            //health: Remaining health of this monster
            //vx: Trajectory of this monster
            //nearBase: 0=monster with no target yet, 1=monster targeting a base
            //threatFor: Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither
            scanf("%d%d%d%d%d%d%d%d%d%d%d",
                &(entities[i].id), &(entities[i].type), &(entities[i].x), &(entities[i].y),
                &(entities[i].shield_life), &(entities[i].is_controlled), &(entities[i].health),
                &(entities[i].vx), &(entities[i].vy), &(entities[i].near_base), &(entities[i].threat_for));

            switch (entities[i].type) {
                case ENTITY_TYPE_MONSTER: monsters[nb_monsters++] = &(entities[i]); break;
                case ENTITY_TYPE_MY_HERO: my_heroes[entities[i].id%heroes_per_player] = &(entities[i]); break;
                case ENTITY_TYPE_OP_HERO: op_heroes[entities[i].id%heroes_per_player] = &(entities[i]); break;
                default: fprintf(stderr, "scan error: undefined entity type: %d\n", entities[i].type);
            }
            
        }
		//print every entities
        for (int i = 0; i < entity_count; i++) {
            dbg_print_entity(&(entities[i]));
        }

        for (int i = 0; i < heroes_per_player; i++) {
            // To debug: fprintf(stderr, "Debug messages...\n");
            s_entity *target = NULL;

            if (nb_monsters > 0) {
                target = monsters[i % nb_monsters];
            }
            if (target) {
                printf("MOVE %d %d\n", target->x, target->y);
            } else {
                printf("WAIT\n");
            }
        }
        free(entities);
        free(monsters);
    }
    return 0;
}