#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <unistd.h>

using namespace std;

class entity<%
	public: 
		int id; // Unique identifier
		int type; // 0=monster, 1=your hero, 2=opponent hero
		int x; // Position of this entity
		int y;
		int shield_life; // Ignore for this league; Count down until shield spell fades
		int is_controlled; // Ignore for this league; Equals 1 when this entity is under a control spell
		int health; // Remaining health of this monster
		int vx; // Trajectory of this monster
		int vy;
		int near_base; // 0=monster with no target yet, 1=monster targeting a base
		int threat_for; // Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither
%>;

int main()
<%
	int base_x; // The corner of the map representing your base
	int base_y;
	cin >> base_x >> base_y; cin.ignore();

	int heroes_per_player; // Always 3
	cin >> heroes_per_player; cin.ignore();

	vector<entity> my_monsters;
	vector<entity> my_heros;
	vector<entity> enemy_heros;
	vector<entity> enemy_monsters;
	vector<entity> neutral_monsters;
	// game loop
	while (1) <%
		int health; // Your base health
		int mana; // Ignore in the first league; Spend ten mana to cast a spell
		for (int i = 0; i < 2; i++) <%
			cin >> health >> mana; cin.ignore();
		%>
		int entity_count; // Amount of heros and monsters you can see
		cin >> entity_count; cin.ignore();
		for (int i = 0; i < entity_count; i++) <%
			entity e;
			cin >> e.id >> e.type >> e.x >> e.y >> e.shield_life >> e.is_controlled >> e.health >> e.vx >> e.vy >> e.near_base >> e.threat_for; cin.ignore();
			if (e.type == 1)
				my_heros.push_back(e);
			if (e.type == 2)
				enemy_heros.push_back(e);
			if (e.type == 0 && e.threat_for == 0)
				neutral_monsters.push_back(e);
			if (e.type == 0 && e.threat_for == 1)
				my_monsters.push_back(e);
			if (e.type == 0 && e.threat_for == 2)
				enemy_monsters.push_back(e);            
		%>

		for (int i = 0; i < heroes_per_player; i++) <%
			// WRITE YOUR MOVEMENTS / SPELLS CODE HERE
			// You can use the vectors my_heros, enemy_heros, my_monsters, enemy_monsters, neutral_monsters
				std::cout << "WAIT" << std::endl;
			%>

		my_monsters.clear();
		enemy_monsters.clear();
		my_heros.clear();
		enemy_heros.clear();
		neutral_monsters.clear();

	%>
%>