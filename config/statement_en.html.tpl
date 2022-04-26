<!-- LEAGUES level1 level2 level3 level4 -->
<div id="statement_back" class="statement_back" style="display: none"></div>
<div class="statement-body">

  <!-- LEAGUE ALERT -->
  <div style="color: #7cc576; 
background-color: rgba(124, 197, 118,.1);
padding: 20px;
margin-right: 15px;
margin-left: 15px;
margin-bottom: 10px;
text-align: left;">
    <div style="text-align: center; margin-bottom: 6px">
      <img src="//cdn.codingame.com/smash-the-code/statement/league_wood_04.png" />
    </div>

    <!-- BEGIN level1 -->
    <p style="text-align: center; font-weight: 700; margin-bottom: 6px;">
      This is a <b>league based</b> challenge.
    </p>
    <!-- END -->
    <!-- BEGIN level2 level3 level4 -->
    <p style="text-align: center; font-weight: 700; margin-bottom: 6px;">
      <b>Starter Kit</b>
    </p>
    <!-- END -->

    <div class="statement-league-alert-content">
      <!-- BEGIN level1 -->
      For this challenge, multiple leagues for the same game are available. Once you have proven yourself against the
      first Boss, you will access a higher league and extra rules will be available.<br><br>
      <!-- END -->

      Starter AIs are available in the
      <a target="_blank" href="https://github.com/CodinGame/SpringChallenge2022/tree/main/starterAIs">Starter Kit</a>.
      They can help you get started with your own bot.
      <br><br>

    </div>

  </div>


  <div style="padding: 20px;
    margin-right: 15px;
    margin-bottom: 10px;
    text-align: left;">
    <p><em>Introductory video by <b>Mathis Hammel</b>: <a href="https://youtu.be/MyHjWftmMfQ" rel="noopener"
        target="_blank">https://youtu.be/MyHjWftmMfQ</a></em></p>
    <!-- BEGIN level3 level4 -->
    <p><em>"Road to Silver" video by <b>Mathis Hammel</b>: <a href="https://youtu.be/_y7Uua5wwsc" rel="noopener"
        target="_blank">https://youtu.be/_y7Uua5wwsc</a></em></p>
    <!-- END -->
  </div>



  <!-- GOAL -->
  <div class="statement-section statement-goal">
    <h2>
      <span class="icon icon-goal">&nbsp;</span>
      <span>The Goal</span>
    </h2>
    <div class="statement-goal-content">
      <span>Protect your base from monster attacks and outlive your opponent.</span>
    </div>
  </div>
  <!-- RULES -->
  <div class="statement-section statement-rules">
    <h2>
      <span class="icon icon-rules">&nbsp;</span>
      <span>Rules</span>
    </h2>

    <div class="statement-rules-content">
      <p>Both players controls a team of
        <const>3</const>
        <strong>heroes</strong>. The teams start out at opposite corners of the map, near their
        <strong>base</strong>. Throughout the game
        <strong>monsters</strong> will appear regularly on the edges of the map. If a
        <strong>monster</strong> reaches your base, it will deal
        <strong>damage</strong>. If your base takes too much damage, you lose.
      </p>
      <p>Thankfully, your
        <strong>heroes</strong> can kill
        <strong>monsters</strong>.
      </p>
      <h3 style="font-size: 24px; margin-top: 20px; margin-bottom: 10px; font-weight: 500; line-height: 1.1;">The map
      </h3>
      <p>The game is played on a rectangular map where the coordinate
        <const>X=0, Y=0</const> is the top-left pixel and <const>X=17630, Y=9000</const> is the bottom-right pixel.
      </p>




      <!-- BEGIN level2 -->
      <div class="statement-new-league-rule">
        <!-- END -->
        <!-- BEGIN level2 level3 level4 -->
        <p>Fog makes it impossible to know the positions of all
          <strong>monsters</strong> and rival
          <strong>heroes</strong>. You need to have them within
          <const>2200</const> units from one of your
          <strong>heroes</strong> or
          <const>6000</const> from your
          <strong>base</strong>.
        </p>
        <!-- END -->
        <!-- BEGIN level2 -->
      </div>
      <!-- END -->

      <p>Each
        <strong>base</strong> can take a maximum of
        <const>3</const> points of damage before being destroyed.
      </p>
      <!-- BEGIN level2 level3 level4 -->
      <p>Multiple
        <strong>entities</strong> (heroes & monsters) can occupy the same coordinates, there is no collision.
      </p>
      <!-- END -->
      <h3 style="font-size: 24px; margin-top: 20px; margin-bottom: 10px; font-weight: 500; line-height: 1.1;">Heroes
      </h3>
      <p>Every turn, you must provide a command for each
        <strong>hero</strong>. They may perform any of the following commands:
      </p>
      <ul style="padding-left: 20px;padding-bottom: 0">
        <li>
          <p>
            <const>WAIT</const>, the hero remains where they are.
          </p>
        </li>
        <li>
          <p>
            <const>MOVE</const>, followed by map coordinates will make the hero advance towards that point by a maximum
            of
            <const>800</const> units.
          </p>
        </li>
      </ul>
      <!-- BEGIN level2 -->
      <div class="statement-new-league-rule">
        <!-- END -->
        <!-- BEGIN level2 level3 level4 -->
        <ul>
          <li>
            <p>
              <const>SPELL</const>, followed by a spell action, as detailed in the Spells section further below.
            </p>
          </li>
        </ul>
        <!-- END -->
        <!-- BEGIN level2 -->
      </div>
      <!-- END -->

      <!-- BEGIN level2 level3 level4 -->
      <p>
        <strong>Heroes</strong> cannot be killed and cannot leave the map.
      </p>
      <!-- END -->

      <!-- BEGIN level2 -->
      <div class="statement-new-league-rule">
        <!-- END -->

        <p>After a
          <strong>hero</strong>'s move phase, any
          <strong>monsters</strong> within
          <const>800</const> units will suffer
          <const>2</const> points of damage.
        </p>

        <!-- BEGIN level2 -->
      </div>
      <!-- END -->

      <h3 style="font-size: 24px; margin-top: 20px; margin-bottom: 10px; font-weight: 500; line-height: 1.1;">Monsters
      </h3>
      <p>Every
        <strong>monster</strong> appears with a given amount of
        <strong>health</strong>. If at the end of a turn, a monster&apos;s
        <strong>health</strong> has dropped to zero or below, the
        <strong>monster</strong> is removed from the game.
      </p>
      <!-- BEGIN level1 -->
      <p>
        <strong>Monsters</strong> will appear randomly, with a random moving direction.
      </p>
      <!-- END -->
      <!-- BEGIN level2 level3 level4 -->
      <p>
        <strong>Monsters</strong> will appear randomly, but symmetrically from the map edges outside of the
        player&apos;s bases.
        They appear with a random moving direction.
      </p>
      <!-- END -->
      <p>
        <strong>Monsters</strong> will always advance in a straight line at a speed of
        <const>400</const> units per turn.
      </p>
      <p>If a
        <strong>monster</strong> comes within
        <const>5000</const> units of a
        <strong>base</strong> at the end of a turn, it will
        <strong>target</strong> that base.
      </p>
      <p>When
        <strong>targeting</strong> a base, a monster will move
        <strong>directly towards</strong> that
        <strong>base</strong> and can no longer leave the map.
      </p>

      <!-- BEGIN level2 -->
      <div class="statement-new-league-rule">
        <!-- END -->
        <!-- BEGIN level2 level3 level4 -->
        <p>If a
          <strong>monster</strong> is
          <b>pushed</b> (with a <action>WIND</action> command) outside the radius of a
          <strong>targeted</strong> base, it will stop targeting and start moving in a randomly selected direction.
        </p>
        <!-- END -->
        <!-- BEGIN level2 -->
      </div>
      <!-- END -->

      <p>If a
        <strong>monster</strong> comes within
        <const>300</const> units of a
        <strong>base</strong> at the end of a turn, as long as it has not been killed on this turn, it will disappear
        and deal the
        <strong>base</strong>
        <const>1</const> point of damage.
      </p>
      <!-- BEGIN level2 level3 level4 -->
      <p>Each subsequent
        <strong>monster</strong> may have slightly more starting health than any previous
        <strong>monster</strong>.
      </p>
      <!-- END -->


      <!-- BEGIN level2 -->
      <div class="statement-new-league-rule">
        <!-- END -->
        <!-- BEGIN level2 level3 level4 -->
        <h3 style="font-size: 24px; margin-top: 20px; margin-bottom: 10px; font-weight: 500; line-height: 1.1;">Spells
        </h3>
        <p>Your team will also acquire
          <const>1</const> point of
          <strong>mana</strong> per damage dealt to a monster, even from monsters with no health left.
        </p>
        <p>
          Mana is shared across the team and heroes can spend <const>10</const> mana points to cast a
          <strong>spell</strong>.
        </p>
        <p>A spell command has
          <strong>parameters</strong>, which you must separate with a space.
        </p>
        <!-- BEGIN level3 -->
        <div class="statement-new-league-rule">
          <!-- END -->
          <table>
            <thead>
              <tr>
                <th style="text-align: center; padding: 5px">command</th>
                <th style="text-align: center; padding: 5px">parameters</th>
                <th style="text-align: center; padding: 5px">effect</th>
                <!-- BEGIN level3 level4 -->
                <th style="text-align: center; padding: 5px">range</th>
                <!-- END -->
              </tr>
            </thead>
            <tbody>
              <tr>
                <td style="padding: 5px">
                  <action>WIND</action>
                </td>
                <td style="padding: 5px">&lt;x&gt; &lt;y&gt;</td>
                <td style="padding: 5px">All entities (except your own heroes) within
                  <const>1280</const> units are
                  <b>pushed</b>
                  <const>2200</const> units in the direction from the spellcaster to x,y.
                </td>
                <!-- BEGIN level3 level4 -->
                <td style="padding: 5px">
                  <const>1280</const>
                </td>
                <!-- END -->
              </tr>
              <!-- BEGIN level3 level4 -->
              <tr>
                <td style="padding: 5px">
                  <action>SHIELD</action>
                </td>
                <td style="padding: 5px">&lt;entityId&gt;</td>
                <td style="padding: 5px">The target entity cannot be targeted by spells for the next
                  <const>12</const> rounds.
                </td>
                <td style="padding: 5px">
                  <const>2200</const>
                </td>
              </tr>
              <tr>
                <td style="padding: 5px">
                  <action>CONTROL</action>
                </td>
                <td style="padding: 5px">&lt;entityId&gt; &lt;x&gt; &lt;y&gt;</td>
                <td style="padding: 5px">Override the target&apos;s next action with a step towards the given
                  coordinates.
                </td>
                <td style="padding: 5px">
                  <const>2200</const>
                </td>
              </tr>
              <!-- END -->
            </tbody>
          </table>
          <!-- BEGIN level3 -->
          <br>
          A hero may only cast a spell on entities that are within
          the spell's range from the hero.
        </div>
        <!-- END -->

        <!-- BEGIN level4 -->
        <br>
        A hero may only cast a spell on entities that are within
        the spell's range from the hero.
        <!-- END -->
        <!-- END -->
        <!-- BEGIN level2 -->
      </div>
      <!-- END -->
    </div>
  </div>
  <!-- BEGIN level2 level3 level4 -->
  <!-- EXAMPLES -->
  <div class="statement-section statement-examples">

    <!-- BEGIN level2 -->
    <div class="statement-new-league-rule">
      <!-- END -->

      <h2>
        <span class="icon icon-example">&nbsp;</span>
        <span>WIND Example</span>
      </h2>
      <div class="statement-examples-text">
        A hero uses WIND at position
        <const>(6000, 6000)</const> towards
        <const>(6000, 5000)</const>.
      </div>
      <div class="statement-example-container">

        <div class="statement-example">
          <img src="/servlet/fileservlet?id=20669980430629" />
          <div class="legend">
            <div class="description">There are
              <const>2</const> monsters within
              <const>1280</const> units around the hero.
            </div>
          </div>
        </div>
        <div class="statement-example">
          <img src="/servlet/fileservlet?id=20669992024930" />
          <div class="legend">
            <div class="title">
              <action>SPELL WIND 6000 5000</action>
            </div>
            <div class="description">The vector
              <const style="font-family: 'Courier New', Courier, monospace">(0,-1)</const> describes the direction
              between
              the hero and the target point.
            </div>
          </div>
        </div>
        <div class="statement-example">
          <img src="/servlet/fileservlet?id=20669974783504" />
          <div class="legend">
            <div class="description">The monsters all move
              <const>2200</const> in the direction defined by the vector.
            </div>
          </div>
        </div>
        <div class="statement-example statement-example-empty" style="height: 0; margin-top: 0; margin-bottom: 0"></div>
        <div class="statement-example statement-example-empty" style="height: 0; margin-top: 0; margin-bottom: 0"></div>
        <div class="statement-example statement-example-empty" style="height: 0; margin-top: 0; margin-bottom: 0"></div>
        <div class="statement-example statement-example-empty" style="height: 0; margin-top: 0; margin-bottom: 0"></div>
      </div>
      <!-- BEGIN level2 -->
    </div>
    <!-- END -->
  </div>
  <!-- END -->

  <div class="statement-section statement-rules">
    <!-- Victory conditions -->
    <div class="statement-victory-conditions">
      <div class="icon victory"></div>
      <div class="blk">
        <div class="title">Victory Conditions</div>
        <div class="text">
          <ul style="padding-top:0; padding-bottom: 0;">
            <li>Your opponent's base health has dropped to zero.</li>
            <li>You have more base health points than your opponent after <strong>
                <const>220</const> turns.
              </strong>
            </li>
            <!-- BEGIN level2 level3 level4 -->
            <li>In case of a tie, you have gained the highest amount of <strong>wild mana</strong>: mana gained outside
              the radius of your
              <strong>base</strong>.
            </li>
            <!-- END -->
          </ul>
        </div>
      </div>
    </div>
    <!-- Lose conditions -->
    <div class="statement-lose-conditions">
      <div class="icon lose"></div>
      <div class="blk">
        <div class="title">Defeat Conditions</div>
        <div class="text">
          <ul style="padding-top:0; padding-bottom: 0;">
            <li>Your base's health reaches zero.</li>
            <li>Your program does not provide a valid command in time.</li>
          </ul>
        </div>
      </div>
    </div>
    <br>
    <h3 style="font-size: 14px;
                      font-weight: 700;
                      padding-top: 15px;
    color: #838891;
                      padding-bottom: 15px;">
      🐞 Debugging tips</h3>
    <ul>
      <li>Hover over an entity to see extra information about it</li>
      <li>Append text after any command and that text will appear above your hero</li>
      <li>Press the gear icon on the viewer to access extra display options</li>
      <li>Use the keyboard to control the action: space to play/pause, arrows to step 1 frame at a time</li>
    </ul>
  </div>

  <!-- BEGIN level3 level4 -->
  <!-- EXPERT RULES -->
  <div class="statement-section statement-expertrules">
    <!-- BEGIN level3 -->
    <div class="statement-new-league-rule">
      <!-- END -->
      <h2>
        <span class="icon icon-expertrules">&nbsp;</span>
        <span>Technical Details</span>
      </h2>
      <div class="statement-expert-rules-content">
        <ul style="padding-left: 20px;padding-bottom: 0">
          <li>
            <p>After an entity moves towards a point, its coordinates are
              <strong>truncated</strong> (when below halfway across the map) or
              <strong>rounded up</strong> (when above halfway across the map), only then are distance-based calculations
              performed
              (such as monster damage).
            </p>
          </li>
          <li>
            <p>Spells are cast in the order of the received output. This means a spell may be
              <strong>cancelled</strong> if another hero spent the necessary mana earlier in the turn.
            </p>
          </li>
          <li>
            <p>If an entity is being moved via a
              <action>CONTROL</action> from multiple sources at once, it will move to the average of all computed
              destinations.
            </p>
          </li>
          <li>
            <p>If an entity is being moved via a
              <action>WIND</action> from multiple sources at once, it will move to the sum of all given directions.
            </p>
          </li>
          <li>
            <p>
              <action>SHIELD</action> also protects entities from receiving a new
              <action>SHIELD</action>.
            </p>
          </li>
          <li>
            <p>Using a spell against a shielded entity still costs mana.</p>
          </li>
          <li>
            <p>Players are not given the coordinates of monsters outside the map and they cannot be targetted by spells.</p>
          </li>
          <li>
            <p>A monster can be pushed outside of the map, unless it is within a base radius, in which case it will will
              be moved no further than the border.</p>
          </li>
          <li>
            <p>In case of a tie, the player who gained the highest amount of
              <strong>mana</strong> outside the radius of their
              <strong>base</strong> will win. this is called <em>wild mana</em>.
            </p>
          </li>
          <li>
            <p>The source code of this game is available <a rel="nofollow" target="_blank"
                href="https://github.com/CodinGame/SpringChallenge2022">on this GitHub repo</a>.
            </p>
          </li>
        </ul>
      </div>
      <!-- BEGIN level3 -->
    </div>
    <!-- END -->
  </div>

  <!-- ACTION ORDER -->
  <div class="statement-section statement-examples">
    <!-- BEGIN level3 -->
    <div class="statement-new-league-rule">
      <!-- END -->
      <h2>
        <span class="icon icon-example">&nbsp;</span>
        <span>Action order for one turn</span>
      </h2>
      <div class="statement-examples-text">
        <ol>
          <li>
            Wait for <b>both</b> players to output <const>3</const> commands.
          </li>
          <li>
            <action>CONTROL</action> spells are applied to the targets and will only be effective on the next turn,
            after
            the next batch of commands.
          </li>
          <li>
            <p>
              <action>SHIELD</action> spells are applied to the targets and will only be effective on the next turn,
              after
              the next batch of commands. Does
              <strong>not</strong> protect from a spell from this same turn.
            </p>
          </li>
          <li>
            <p>
              <action>MOVE</action> all heroes.
            </p>
          </li>
          <li>
            <p>Heroes attack monsters in range and produce mana for each hit.</p>
          </li>
          <li>
            <p>
              <action>WIND</action> spells are applied to entities in range.
            </p>
          </li>
          <li>
            <p>
              <action>MOVE</action> all monsters according to their current speed, unless they were
              <b>pushed</b> by a wind on this turn.
            </p>
          </li>
          <li>
            <p>
              <action>SHIELD</action> countdowns are decremented.
            </p>
          </li>
          <li>
            <p>New monsters appear. Dead monsters are removed.</p>
          </li>
        </ol>
      </div>
      <!-- BEGIN level3 -->
    </div>
    <!-- END -->
  </div>
  <!-- END -->

  <!-- PROTOCOL -->
  <div class="statement-section statement-protocol">
    <h2>
      <span class="icon icon-protocol">&nbsp;</span>
      <span>Game Input/Output</span>
    </h2>
    <!-- Protocol block -->
    <div class="blk">
      <div class="title">Initialization Input</div>
      <div class="text">
        <span class="statement-lineno">Line 1:</span> two integers
        <var>baseX</var> and
        <var>baseY</var> for the coordinates of your base. The enemy base will be at the opposite side of the map.
        <br>
        <span class="statement-lineno">Line 2:</span> the integer <var>heroesPerPlayer</var> which is always <const>3</const>.
        <br>
      </div>
    </div>
    <!-- Protocol block -->
    <div class="blk">
      <div class="title">Input for One Game Turn</div>
      <div class="text">
        <span class="statement-lineno">First 2 lines:</span> two integers
        <var>baseHealth</var> and
        <var>mana</var> for the remaining health and mana for both players. Your data is always given first.
        <!-- BEGIN level1 -->
        <em>You may ignore the mana for this league.</em>
        <!-- END -->
        <br>
        <span class="statement-lineno">Next line:</span>
        <!-- BEGIN level1 -->
        <var>entityCount</var> for the amount of game entities currently in play.
        <!-- END -->
        <!-- BEGIN level2 level3 level4 -->
        <var>entityCount</var> for the amount of game entities currently visible to you.
        <!-- END -->
        <br>
        <!-- BEGIN level3 -->
        <div class="statement-new-league-rule">
          <!-- END -->
          <span class="statement-lineno">Next
            <var>entityCount</var> lines:</span> 11 integers to describe each entity:

          <ul style="padding-left: 20px;padding-top:0;padding-bottom:0px">
            <li>
              <var>id</var>: entity's unique id.
            </li>
            <li>
              <var>type</var>:
              <ul style="padding-left: 20px;padding-top:0;padding-bottom:0px">
                <li>
                  <const>0</const>: a monster
                </li>
                <li>
                  <const>1</const>: one of your heroes
                </li>
                <li>
                  <const>2</const>: one of your opponent's heroes
                </li>
              </ul>
            </li>
            <li>
              <var>x</var> &
              <var>y</var>: the entity's position.
            </li>
            <!-- BEGIN level1 level2 -->
            <li>
              <var>shieldLife</var>: <em>ignore for this league</em>.
            </li>
            <li>
              <var>isControlled</var>: <em>ignore for this league</em>.
            </li>
            <!-- END -->
            <!-- BEGIN level3 level4 -->
            <li>
              <var>shieldLife</var>: the number of rounds left until entity's shield is no longer active. <const>0
              </const>
              when no shield is
              active.
            </li>
            <li>
              <var>isControlled</var>:
              <const>1</const> if this entity is under a
              <action>CONTROL</action> spell,
              <const>0</const> otherwise.
            </li>
            <!-- END -->
          </ul>
          <!-- BEGIN level3 -->
        </div>
        <!-- END -->
        The next
        <span class="statement-lineno">5</span> integers only apply to monsters (will be
        <const>-1</const> for heroes).
        <ul style="padding-left: 20px;padding-top:0;padding-bottom:0px">
          <li>
            <var>health</var>: monster's remaining health points.
          </li>
          <li>
            <var>vx</var> &
            <var>vy</var>: monster's current speed vector, they will add this to their position for their next movement.
          </li>
          <li>
            <var>nearBase</var>:
            <const>1</const>: if monster is
            <b>targeting</b> a base,
            <const>0</const> otherwise.
          </li>
          <li>
            <var>threatFor</var>:
            <ul style="padding-left: 20px;padding-top:0;padding-bottom:0px">
              <li>With the monster's current trajectory &mdash; if
                <var>nearBase</var> is
                <const>0</const>:
                <ul style="padding-left: 20px;padding-top:0;padding-bottom:0px">
                  <li style="list-style-type: circle">
                    <const>0</const>: it will never reach a base.
                  </li>
                  <li style="list-style-type: circle">
                    <const>1</const>: it will eventually reach your base.
                  </li>
                  <li style="list-style-type: circle">
                    <const>2</const>: it will eventually reach your opponent's base.
                  </li>
                </ul>
              </li>
              <li>If
                <var>nearBase</var> is
                <const>1</const>:
                <const>1</const> if this monster is
                <b>targeting</b> your base,
                <const>2</const> otherwise.
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
    <!-- Protocol block -->
    <div class="blk">
      <div class="title">Output for One Game Turn</div>
      <div class="text">
        <!-- BEGIN level2 -->
        <div class="statement-new-league-rule">
          <!-- END -->
          <span class="statement-lineno">3 lines,</span> one for each hero, containing one of the following actions:
          <ul style="padding-left: 20px;padding-top: 0">
            <li>
              <action>WAIT</action>: the hero does nothing.
            </li>
            <li>
              <action>MOVE</action> followed by two integers (x,y): the hero moves
              <const>800</const> towards the given point.
            </li>
            <!-- BEGIN level2 level3 level4 -->
            <li>
              <action>SPELL</action> followed by a spell command: the hero attempts to cast the given spell.
            </li>
            <!-- END -->
          </ul>
          <!-- BEGIN level2 -->
        </div>
        <!-- END -->
        You may append text to a command to have it displayed in the viewer above your hero.
        <br><br> Examples: <ul style="padding-top:0; padding-bottom: 0;">
          <li>
            <action>MOVE 8000 4500</action>
          </li>
          <!-- BEGIN level2 level3 level4 -->
          <li>
            <action>SPELL WIND 80 40 casting a wind spell!</action>
          </li>
          <!-- END -->
          <!-- BEGIN level3 level4 -->
          <li>
            <action>SPELL SHIELD 1</action>
          </li>
          <!-- END -->
          <li>
            <action>WAIT nothing to do...</action>
          </li>
        </ul>
        <!-- BEGIN level3 level4 -->
        <!-- BEGIN level3 -->
        <div class="statement-new-league-rule">
          <!-- END -->
          You must provide a valid command to all heroes each turn, even if they are being controlled by your opponent.
          <!-- BEGIN level3 -->
        </div>
        <!-- END -->
        <!-- END -->
      </div>
    </div>
    <div class="blk">
      <div class="title">Constraints</div>
      <div class="text">
        Response time per turn ≤
        <const>50</const>ms
        <br>Response time for the first turn ≤
        <const>1000</const>ms
      </div>
    </div>
  </div>


  <!-- BEGIN level1 level2 -->
  <!-- LEAGUE ALERT -->
  <div style="color: #7cc576; 
                      background-color: rgba(124, 197, 118,.1);
                      padding: 20px;
                      margin-top: 10px;
                      text-align: left;">
    <div style="text-align: center; margin-bottom: 6px"><img
        src="//cdn.codingame.com/smash-the-code/statement/league_wood_04.png" /></div>

    <div style="text-align: center; font-weight: 700; margin-bottom: 6px;">
      What is in store for me in the higher leagues?
    </div>
    Extra rules available in higher leagues are:
    <ul>
      <!-- BEGIN level1 -->
      <li>Heroes can cast spells</li>
      <li>Fog of war</li>
      <!-- END -->
      <!-- BEGIN level2 -->
      <li>New spell: <action>CONTROL</action></li>
      <li>New spell: <action>SHIELD</action></li>
      <!-- END -->
    </ul>
  </div>
  <!-- END -->
</div>
<!-- SHOW_SAVE_PDF_BUTTON -->