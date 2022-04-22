class InputParser
    def getBaseLoc
        return gets.split(" ").collect { |coord| coord.to_i }
    end

    def getHeroesPerPlayer
        return gets.to_i
    end

    def getBaseDetails
        return gets.split(" ").collect { |detail| detail.to_i }
    end

    def getEntityCount
        return gets.to_i
    end

    def getEntityDetails
        return gets.split(" ").collect { |detail| detail.to_i }
    end
end

# ======================================================================================

class Base
    attr_accessor :x, :y, :health, :mana
    # @x        => x coord
    # @y        => y coord
    # @health   => base health
    # @mana     => Ignore in the first league; Spend ten mana to cast a spell

    def initialize(coords)
        @x, @y = coords
        @health = 0
        @mana = 0
    end

    def updateStats(stats)
        @health, @mana = stats
    end
end

# ======================================================================================

class Monster
    attr_accessor :id, :x, :y, :vx, :vy, :health, :locked_on, :threatening
    # @id           => identifier
    # @x            => x coord
    # @y            => y coord
    # @vx           => x trajectory
    # @vy           => y trajectory
    # @health       => remaining health
    # @locked_on    => 0=no target yet, 1=targeting a base
    # @threatening  => Given this monster's trajectory, it is a threat to 1=your base, 2=your opponent's base, 0=neither

    def initialize(stats)
        @id, @x, @y, @health, @vx, @vy, @locked_on, @threatening = stats
    end

    def is_threat?
        @threatening == 1
    end

end

# ======================================================================================

class Hero
    attr_accessor :id, :x, :y, :health
    # @id       => identifier
    # @x        => x coord
    # @y        => y coord
    # @health   =>  remaining health

    def initialize(stats)
        @id, @x, @y, @healthg = stats
    end

    def pickTarget(monsters)
        # replace below with your target picking logic here
        return monsters.first
    end
end

# ======================================================================================

class Game
    attr_accessor :input_parser, :base, :enemy_base, :monsters, :heroes

    def initialize()
        @inputParser = InputParser.new()
        @base = Base.new(@inputParser.getBaseLoc)
        @enemyBase = Base.new([17630, 9000]) # enemy base is in opposite corner in starting League
    end

    def playGame
        heroes_per_player = @inputParser.getHeroesPerPlayer

        # game loop
        loop do
            @monsters = [] # clear out the list of monsters from last turn
            @heroes = [] # clear out the list of heroes from last turn
            @base.updateStats(@inputParser.getBaseDetails)
            @enemyBase.updateStats(@inputParser.getBaseDetails)

            entity_count = @inputParser.getEntityCount # Amount of heros and monsters you can see
            entity_count.times do
                # id: Unique identifier
                # type: 0=monster, 1=your hero, 2=opponent hero
                # x: Position of this entity
                # shield_life: Ignore for this league; Count down until shield spell fades
                # is_controlled: Ignore for this league; Equals 1 when this entity is under a control spell
                # health: Remaining health of this monster
                # vx: Trajectory of this monster
                # near_base: 0=monster with no target yet, 1=monster targeting a base
                # threat_for: Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither
                id, type, x, y, shield_life, is_controlled, health, vx, vy, locked_on, threatening = @inputParser.getEntityDetails
                
                if type == 0
                    @monsters << Monster.new([id, x, y, health, vx, vy, locked_on, threatening])
                end

                if type == 1
                    @heroes << Hero.new([id, x, y, health]) # may need to pass more in later leagues
                end

                if type == 2
                    # no op for now, we don't care about where enemy heroes are
                end

            end

            # To debug: STDERR.puts "Debug messages..."
            # Write an action using puts
            # In the first league: MOVE <x> <y> | WAIT; In later leagues: | SPELL <spellParams>;
            @heroes.each do |hero|
                if @monsters.size > 0
                    target = hero.pickTarget(@monsters)
                    puts "MOVE #{target.x} #{target.y} Attacking #{target.id} at (#{target.x},#{target.y})"
                else
                    puts "WAIT All clear, no monsters!"
                end
            end
        end
    end
end

STDOUT.sync = true # DO NOT REMOVE
game = Game.new()
game.playGame
