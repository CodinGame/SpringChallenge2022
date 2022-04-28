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
      Ce challenge se déroule en <b>ligues</b>.
    </p>
    <!-- END -->
    <!-- BEGIN level2 level3 level4 -->
    <p style="text-align: center; font-weight: 700; margin-bottom: 6px;">
      <b>Kit de Démarrage</b>
    </p>
    <!-- END -->

    <div class="statement-league-alert-content">
      <!-- BEGIN level1 -->
      Pour ce challenge, plusieurs ligues pour le même jeu seront disponibles. Quand vous aurez prouvé votre valeur
      contre le premier Boss, vous accéderez à la ligue supérieure et débloquerez de nouveaux adversaires.<br> <br>
      <!-- END -->
      Des IAs de base sont disponibles dans le <a target="_blank"
        href="https://github.com/CodinGame/SpringChallenge2022/tree/main/starterAIs">kit de démarrage</a>. Elles peuvent
      vous aider à appréhender votre propre IA.
    </div>
  </div>

  <div style="padding: 20px;
  margin-right: 15px;
  margin-bottom: 10px;
  text-align: left;">
    <p><em>Vidéo d'introduction par <b>Mathis Hammel</b>: <a href="https://youtu.be/MyHjWftmMfQ" rel="noopener"
      target="_blank">https://youtu.be/MyHjWftmMfQ</a></em></p>
    <!-- BEGIN level3 level4 -->
    <p><em>Vidéo "Road to Silver" par <b>Mathis Hammel</b>: <a href="https://youtu.be/_y7Uua5wwsc" rel="noopener"
        target="_blank">https://youtu.be/_y7Uua5wwsc</a></em></p>
    <!-- END -->
  </div>


  <!-- GOAL -->
  <div class="statement-section statement-goal">
    <h2>
      <span class="icon icon-goal">&nbsp;</span>
      <span>Objectif</span>
    </h2>
    <div class="statement-goal-content">
      <span>Protégez votre base de la vague de monstres pendant plus longtemps que votre adversaire.</span>
    </div>
  </div>
  <!-- RULES -->
  <div class="statement-section statement-rules">
    <h2>
      <span class="icon icon-rules">&nbsp;</span>
      <span>Règles</span>
    </h2>

    <div class="statement-rules-content">
      Les deux joueurs contrôlent une équipe de <const>3</const> <strong>héros</strong>. Les équipes commencent aux
      coins opposés de la carte, près de
      leur <strong>base</strong>. Tout au long du jeu, des <strong>monstres</strong> apparaîtront régulièrement sur les
      bords de la carte. Si un <strong>monstre</strong>
      atteint votre base, il infligera un point de <strong>dégât</strong>. Si votre base subit trop de dégâts, vous
      perdez.

      <p>
        Heureusement, vos <strong>héros</strong> peuvent tuer les <strong>monstres</strong>.
      </p>
      <h3 style="font-size: 24px; margin-top: 20px; margin-bottom: 10px; font-weight: 500; line-height: 1.1;">La zone de
        jeu
      </h3>
      <p>
        Une partie se déroule dans une zone rectangulaire où <const>X=0, Y=0</const> est le pixel le plus en haut à
        gauche et <const>X=17630, Y=9000</const> est le pixel le plus en bas à droite.
      </p>

      <!-- BEGIN level2 -->
      <div class="statement-new-league-rule">
        <!-- END -->
        <!-- BEGIN level2 level3 level4 -->
        <p>
          Du brouillard rend impossible de connaître la position de tous les <strong>monstres</strong> et des
          <strong>héros</strong> rivaux. Vous devez les avoir à moins de <const>2200</const> unités d'un de vos
          <strong>héros</strong> ou <const>6000</const> de votre <strong>base</strong>.
        </p>
        <!-- END -->
        <!-- BEGIN level2 -->
      </div>
      <!-- END -->

      <p>
        Chaque <strong>base</strong> peut subir un maximum de <const>3</const> points de dégâts avant d'être détruite.
      </p>
      <!-- BEGIN level2 level3 level4 -->
      <p>
        Plusieurs <strong>entités</strong> (héros et monstres) peuvent occuper les mêmes coordonnées, il n'y a pas de
        collisions.
      </p>
      <!-- END -->
      <h3 style="font-size: 24px; margin-top: 20px; margin-bottom: 10px; font-weight: 500; line-height: 1.1;">Héros
      </h3>
      <p>
        A chaque tour, vous devez fournir un ordre à chaque <strong>héros</strong>. Ils peuvent exécuter l'un des ordres
        suivants&nbsp;:
      </p>
      <ul style="padding-left: 20px;padding-bottom: 0">
        <li>
          <p>
            <const>WAIT</const>, le héros reste où il est.
          </p>
        </li>
        <li>
          <p>
            <const>MOVE</const>, suivi des coordonnées de la carte fera avancer le héros vers ce point d'un maximum de
            <const>800</const> unités.
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
              <const>SPELL</const>, suivi d'une action de sort, comme détaillé dans la section Sorts plus bas.
            </p>
          </li>
        </ul>
        <!-- END -->
        <!-- BEGIN level2 -->
      </div>
      <!-- END -->

      <!-- BEGIN level2 level3 level4 -->
      <p>
        Les <strong>héros</strong> ne peuvent pas être tués et ne peuvent pas quitter la carte.
      </p>
      <!-- END -->

      <!-- BEGIN level2 -->
      <div class="statement-new-league-rule">
        <!-- END -->

        <p>
          Après la phase de déplacement d'un <strong>héros</strong>, tous les <strong>monstres</strong> situés dans un
          rayon de <const>800</const> unités subissent <const>2</const> points de
          <!-- BEGIN level1 -->
          dégâts.
          <!-- END -->
          <!-- BEGIN level2 level3 level4 -->
          dégâts, même s'ils n'ont plus de vie.
          <!-- END -->
        </p>

        <!-- BEGIN level2 -->
      </div>
      <!-- END -->

      <h3 style="font-size: 24px; margin-top: 20px; margin-bottom: 10px; font-weight: 500; line-height: 1.1;">Monstres
      </h3>
      <p>
        Chaque <strong>monstre</strong> apparaît avec une quantité donnée de <strong>vie</strong>. Si à la fin d'un
        tour, la <strong>vie</strong> d'un monstre est tombée à zéro ou moins, le <strong>monstre</strong> est retiré
        du jeu.
      </p>
      <!-- BEGIN level1 -->
      <p>
        Les <strong>monstres</strong> apparaissent de manière aléatoire, avec une direction de déplacement aléatoire.
      </p>
      <!-- END -->
      <!-- BEGIN level2 level3 level4 -->
      <p>
        Les <strong>monstres</strong> apparaissent de manière aléatoire, mais symétriquement à partir des bords de la
        carte, en dehors des bases des joueurs. Ils apparaissent avec une direction de déplacement aléatoire.
      </p>
      <!-- END -->
      <p>
        Les <strong>monstres</strong> avancent toujours en ligne droite à une vitesse de <const>400</const> unités par
        tour.
      </p>
      <p>
        Si un <strong>monstre</strong> se trouve à moins de <const>5000</const> unités d'une <strong>base</strong> à la
        fin d'un tour, il prendra cette base pour <strong>cible</strong>.
      </p>
      <p>
        Lorsqu'il <strong>cible</strong> une base, un monstre se déplace <strong>directement vers</strong> cette
        <strong>base</strong> et ne peut plus quitter la carte.
      </p>

      <!-- BEGIN level2 -->
      <div class="statement-new-league-rule">
        <!-- END -->
        <!-- BEGIN level2 level3 level4 -->
        <p>
          Si un <strong>monstre</strong> est <strong>poussé</strong> (avec une commande <action>WIND</action>) en dehors
          du rayon d'une base <strong>ciblée</strong>, il cessera de cibler et commencera à se déplacer dans une
          direction choisie au hasard.
        </p>
        <!-- END -->
        <!-- BEGIN level2 -->
      </div>
      <!-- END -->

      <p>
        Si un <strong>monstre</strong> se trouve à moins de <const>300</const> unités d'une <strong>base</strong> à la
        fin d'un tour, tant qu'il n'a pas été tué pendant ce tour, il disparaît et inflige <const>1</const> point de
        dégâts à la <strong>base</strong>.
      </p>
      <!-- BEGIN level2 level3 level4 -->
      <p>
        Chaque <strong>monstre</strong> suivant peut avoir une vie de départ légèrement supérieure à celle des
        <strong>monstres</strong> précédents.
      </p>
      <!-- END -->


      <!-- BEGIN level2 -->
      <div class="statement-new-league-rule">
        <!-- END -->
        <!-- BEGIN level2 level3 level4 -->
        <h3 style="font-size: 24px; margin-top: 20px; margin-bottom: 10px; font-weight: 500; line-height: 1.1;">Sorts
        </h3>
        <p>
          Votre équipe acquiert également <const>1</const> point de <strong>mana</strong> par dégât infligé à un
          monstre, même pour les monstres qui n'ont plus de points de vie.
        </p>
        <p>
          Le mana est partagé entre les membres de l'équipe et les héros peuvent dépenser <const>10</const> points de
          mana pour lancer un <strong>sort</strong>.
        </p>
        <p>
          Une commande de sort a des <strong>paramètres</strong>, que vous devez séparer par un espace.
        </p>
        <!-- BEGIN level3 -->
        <div class="statement-new-league-rule">
          <!-- END -->
          <table>
            <thead>
              <tr>
                <th style="text-align: center; padding: 5px">commande</th>
                <th style="text-align: center; padding: 5px">paramètres</th>
                <th style="text-align: center; padding: 5px">effet</th>
                <!-- BEGIN level3 level4 -->
                <th style="text-align: center; padding: 5px">portée</th>
                <!-- END -->
              </tr>
            </thead>
            <tbody>
              <tr>
                <td style="padding: 5px">
                  <action>WIND</action>
                </td>
                <td style="padding: 5px">&lt;x&gt; &lt;y&gt;</td>
                <td style="padding: 5px">
                  Toutes les entités (à l'exception de vos propres héros) dans un rayon de <const>1280</const> unités
                  sont poussées de <const>2200</const> unités dans la direction du lanceur de sorts vers x,y.
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
                <td style="padding: 5px">L'entité ciblée ne peut pas être ciblée par des sorts pendant les <const>12
                  </const> prochains rounds.
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
                <td style="padding: 5px">
                  Remplace la prochaine action de la cible par un pas vers les coordonnées données.
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
          Un héros ne peut lancer un sort que sur des entités qui se trouvent à sa portée.
        </div>
        <!-- END -->

        <!-- BEGIN level4 -->
        <br>
        Un héros ne peut lancer un sort que sur des entités qui se trouvent à sa portée.
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
        <span>Exemple: WIND</span>
      </h2>
      <div class="statement-examples-text">
        Un héros à la position <const>(6000, 6000)</const> lance <action>WIND</action>
        vers <const>(6000, 5000)</const>.
      </div>
      <div class="statement-example-container">
        <div class="statement-example">
          <img src="/servlet/fileservlet?id=20669980430629" />
          <div class="legend">
            <div class="description">Il y a
              <const>2</const> monstres dans les
              <const>1280</const> unités autour du héros.
            </div>
          </div>
        </div>
        <div class="statement-example">
          <img src="/servlet/fileservlet?id=20669992024930" />
          <div class="legend">
            <div class="title">
              <action>SPELL WIND 6000 5000</action>
            </div>
            <div class="description">Le vecteur
              <const style="font-family: 'Courier New', Courier, monospace">(0,-1)</const> décrit la direction entre le
              héros et la position ciblée.
            </div>
          </div>
        </div>
        <div class="statement-example">
          <img src="/servlet/fileservlet?id=20669974783504" />
          <div class="legend">
            <div class="description">Les monstres avancent tous de
              <const>2200</const> dans la direction définie par le vecteur.
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
        <div class="title">Conditions de Victoire</div>
        <div class="text">
          <ul style="padding-top:0; padding-bottom: 0;">
            <li>Les points de vie de la base adverse tombent à zéro.</li>
            <li>Votre base a plus de points de vie que celle de votre adversaire après <strong>
                <const>220</const> tours.
              </strong>
            </li>
            <!-- BEGIN level2 level3 level4 -->
            <li>En cas d'égalité, vous avez acquéri plus de <strong>wild mana</strong>: mana obtenu par vos héros quand ils sont à l'extérieur du
              rayon de votre
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
        <div class="title">Conditions de Défaite</div>
        <div class="text">
          <ul style="padding-top:0; padding-bottom: 0;">
            <li>La vie de votre base tombe à zéro.</li>
            <li>Votre programme ne fourni pas d'instruction valide dans le temps imparti pour chaque héros de votre
              équipe.</li>
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
      🐞 Conseils de débogage</h3>
    <ul>
      <li>Survolez une unité pour voir davantage d'informations</li>
      <li>Ajoutez du texte à la fin d'une instruction pour afficher ce texte au dessus de votre héros</li>
      <li>Cliquez sur la roue dentée pour afficher les options supplémentaires</li>
      <li>Utilisez le clavier pour contrôler l'action : espace pour play / pause, les flèches pour avancer pas à pas
      </li>
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
        <span>Détails Techniques</span>
      </h2>
      <div class="statement-expert-rules-content">
        <ul style="padding-left: 20px;padding-bottom: 0">
          <li>
            <p>
              Lorsqu'une entité se rapproche d'un point, ses coordonnées sont <strong>tronquées</strong> (lorsqu'elle se
              trouve en dessous de la moitié de la carte) ou <strong>arrondies</strong> (lorsqu'elle se trouve au-dessus
              de la moitié de la carte). Ce n'est qu'à ce moment-là que les calculs basés sur la distance sont effectués
              (comme les dégâts infligés par les monstres).
            </p>
          </li>
          <li>
            <p>
              Les sorts sont lancés dans l'ordre du résultat reçu. Cela signifie qu'un sort peut être
              <strong>annulé</strong> si un autre héros a dépensé le mana nécessaire plus tôt dans le tour.
            </p>
          </li>
          <li>
            <p>
              Si une entité est déplacée via un <action>CONTROL</action> depuis plusieurs sources à la fois, elle se
              déplacera vers la moyenne de toutes les destinations calculées.
            </p>
          </li>
          <li>
            <p>
              Si une entité est déplacée par un <action>WIND</action> provenant de plusieurs sources à la fois, elle se
              déplacera vers la somme de toutes les directions données.
            </p>
          </li>
          <li>
            <p>
              Le <action>SHIELD</action> protège également les entités contre la réception d'un nouveau <action>SHIELD
              </action>.
            </p>
          </li>
          <li>
            <p>
              L'utilisation d'un sort contre une entité protégée par un bouclier coûte toujours du mana.
            </p>
          </li>
          <li>
            <p>
              Les joueurs ne reçoivent pas les coordonnées des monstres situés en dehors de la carte et ne peuvent pas leur lancer de sorts.
            </p>
          </li>
          <li>
            <p>Un monstre peut être poussé vers l'extérieur de la zone de jeu, à moins qu'il ne soit dans le rayon d'une
              base. Dans ce cas là son déplacement sera interrompu au bord.</p>
          </li>
          <li>
            <p>
              En cas d'égalité, le joueur qui a gagné la plus grande quantité de <strong>mana</strong> en dehors du
              rayon de sa <strong>base</strong> gagnera. c'est ce qu'on appelle le <em>wild mana</em>.
            </p>
          </li>
          <li>
            <p>
              Vous pouvez voir le code source du jeu sur
              <a rel="nofollow" target="_blank" href="https://github.com/CodinGame/SpringChallenge2022">ce repo
                GitHub</a>.
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
        <span>Ordre d'action pour un tour</span>
      </h2>
      <div class="statement-examples-text">
        <ol>
          <li>
            On attend que les <b>deux</b> joueurs fournissent <const>3</const> instructions chacun.
          </li>
          <li>
            Les sorts <action>CONTROL</action> sont appliqués aux cibles et ne seront effectifs qu'au prochain tour,
            après la prochaine série d'instructions.
          </li>
          <li>
            <p>
              Les sorts <action>SHIELD</action> sont appliqués aux cibles et ne seront effectifs qu'au prochain tour,
              après le prochain lot d'instructions. Ils ne protègent donc <strong>pas</strong> contre un sort de ce même
              tour.
            </p>
          </li>
          <li>
            <p>
              Les héros font leur déplacements <action>MOVE</action>.
            </p>
          </li>
          <li>
            <p>Les héros attaquent les monstres à portée et produisent du mana pour chaque dégat infligé.</p>
          </li>
          <li>
            <p>
              Les sorts de <action>WIND</action> s'appliquent aux entités à portée.
            </p>
          </li>
          <li>
            <p>
              Déplacements des monstres selon leur trajectoire actuelle, à moins qu'ils n'aient été <b>poussés</b> par
              un <action>WIND</action> pendant ce tour.
            </p>
          </li>
          <li>
            <p>
              Les décomptes du <action>SHIELD</action> sont décrémentés.
            </p>
          </li>
          <li>
            <p>Les nouveaux monstres apparaissent. Les monstres morts sont retirés du jeu.</p>
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
      <span>Entrées / Sorties du Jeu</span>
    </h2>
    <!-- Protocol block -->
    <div class="blk">
      <div class="title">Entrées d'Initialisation</div>
      <div class="text">
        <span class="statement-lineno">Ligne 1&nbsp;:</span> deux entiers
        <var>baseX</var> et
        <var>baseY</var> pour les coordonnées de votre base. La base adverse se situe à l'opposé de la zone de jeu.
        <br>
        <span class="statement-lineno">Ligne 2:</span> l'entier <var>heroesPerPlayer</var> qui vaut toujours <const>3</const> pour le nombre de héros par joueur.
        <br>
      </div>
    </div>
    <!-- Protocol block -->
    <div class="blk">
      <div class="title">Entrées pour un Tour de Jeu</div>
      <div class="text">
        <span class="statement-lineno">2 premières lignes&nbsp;:</span> deux entiers
        <var>baseHealth</var> et
        <var>mana</var> pour la vie restante de la base et la quantité de points de mana des deux joueurs.
        Vos données sont toujours fournies en premier.
        <!-- BEGIN level1 -->
        <em>Vous pouvez ignorer la mana pour cette ligue.</em>
        <!-- END -->
        <br>
        <span class="statement-lineno">Ligne suivante&nbsp;:</span>
        <!-- BEGIN level1 -->
        <var>entityCount</var> pour le nombre d'entités en jeu.
        <!-- END -->
        <!-- BEGIN level2 level3 level4 -->
        <var>entityCount</var> pour le nombre d'entités qui vous sont visibles.
        <!-- END -->
        <br>
        <!-- BEGIN level3 -->
        <div class="statement-new-league-rule">
          <!-- END -->
          <span class="statement-lineno"><var>entityCount</var> prochaines lignes&nbsp;:</span> 11 entiers pour décrire
          chaque entité&nbsp;:

          <ul style="padding-left: 20px;padding-top:0;padding-bottom:0px">
            <li>
              <var>id</var>: son identifiant unique.
            </li>
            <li>
              <var>type</var>:
              <ul style="padding-left: 20px;padding-top:0;padding-bottom:0px">
                <li>
                  <const>0</const>: pour un monstre
                </li>
                <li>
                  <const>1</const>: pour un de vos héros
                </li>
                <li>
                  <const>2</const>: pour un héros de l'adversaire
                </li>
              </ul>
            </li>
            <li>
              <var>x</var> &
              <var>y</var>: sa position.
            </li>
            <!-- BEGIN level1 level2 -->
            <li>
              <var>shieldLife</var>: <em>ignorez pour cette league</em>.
            </li>
            <li>
              <var>isControlled</var>: <em>ignorez pour cette league</em>.
            </li>
            <!-- END -->
            <!-- BEGIN level3 level4 -->
            <li>
              <var>shieldLife</var>: le nombre de tours restants avant que l'effet d'un <const>SHIELD</const> sur cette
              entité ne s'estompe. <const>0</const> dans le cas où cet effet n'est pas actif.
            </li>
            <li>
              <var>isControlled</var>:
              <const>1</const> si l'entité est sous l'effet d'un sort <action>CONTROL</action>,
              <const>0</const> sinon.
            </li>
            <!-- END -->
          </ul>
          <!-- BEGIN level3 -->
        </div>
        <!-- END -->
        Les
        <span class="statement-lineno">5 prochains</span> entiers ne s'applique qu'aux monstres (leur valeur sera
        <const>-1</const> pour un héros).
        <ul style="padding-left: 20px;padding-top:0;padding-bottom:0px">
          <li>
            <var>health</var>: ses points de vie restants.
          </li>
          <li>
            <var>vx</var> &
            <var>vy</var>: les composantes de son vecteur vitesse, le monstre ajoutera cette valeur à sa position
            actuelle pour son prochain mouvement.
          </li>
          <li>
            <var>nearBase</var>:
            <const>1</const>: si le monstre <b>cible</b> une base, <const>0</const> sinon.
          </li>
          <li>
            <var>threatFor</var>:
            <ul style="padding-left: 20px;padding-top:0;padding-bottom:0px">
              <li>Étant donnée la trajectoire actuelle de ce monstre &mdash; si
                <var>nearBase</var> vaut
                <const>0</const>:
                <ul style="padding-left: 20px;padding-top:0;padding-bottom:0px">
                  <li style="list-style-type: circle">
                    <const>0</const>: il n'atteindra jamais une base.
                  </li>
                  <li style="list-style-type: circle">
                    <const>1</const>: il atteindra à un moment le rayon de votre base.
                  </li>
                  <li style="list-style-type: circle">
                    <const>2</const>: il atteindra à un moment le rayon de la base adverse.
                  </li>
                </ul>
              </li>
              <li>si
                <var>nearBase</var> vaut
                <const>1</const>:
                <const>1</const> si ce monstre
                <b>cible</b> votre base,
                <const>2</const> s'il s'agit de la base adverse.
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
    <!-- Protocol block -->
    <div class="blk">
      <div class="title">Sorties pour un Tour de Jeu</div>
      <div class="text">
        <!-- BEGIN level2 -->
        <div class="statement-new-league-rule">
          <!-- END -->
          <span class="statement-lineno">3 lignes,</span> une par héros, contenant l'une des actions suivantes&nbsp;:
          <ul style="padding-left: 20px;padding-top: 0">
            <li>
              <action>WAIT</action>: le héros reste sur place.
            </li>
            <li>
              <action>MOVE</action> suivi de deux entiers (x,y): le héros avance de
              <const>800</const> unités vers le point donné.
            </li>
            <!-- BEGIN level2 level3 level4 -->
            <li>
              <action>SPELL</action> suivi d'une commande de sort&nbsp;: le héros tente de lancer le sort donné.
            </li>
            <!-- END -->
          </ul>
          <!-- BEGIN level2 -->
        </div>
        <!-- END -->
        Ajoutez du texte à la fin d'une instruction pour afficher ce texte au dessus de votre héros.
        <br><br> Exemples: <ul style="padding-top:0; padding-bottom: 0;">
          <li>
            <action>MOVE 8000 4500</action>
          </li>
          <!-- BEGIN level2 level3 level4 -->
          <li>
            <action>SPELL WIND 80 40 je lance un sort !</action>
          </li>
          <!-- END -->
          <!-- BEGIN level3 level4 -->
          <li>
            <action>SPELL SHIELD 1</action>
          </li>
          <!-- END -->
          <li>
            <action>WAIT rien à faire...</action>
          </li>
        </ul>
        <!-- BEGIN level3 level4 -->
        <!-- BEGIN level3 -->
        <div class="statement-new-league-rule">
          <!-- END -->
          Vous devez fournir une instruction valide à tous vos héros par tour, même s'ils sont contrôlés par votre
          adversaire.
          <!-- BEGIN level3 -->
        </div>
        <!-- END -->
        <!-- END -->
      </div>
    </div>
    <div class="blk">
      <div class="title">Contraintes</div>
      <div class="text">
        Temps de réponse par tour ≤ <const>50</const>ms<br>
        Temps de réponse au premier tour ≤ <const>1000</const>ms
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
      Qu'est-ce qui m'attend dans les prochaines ligues&nbsp;?
    </div>
    Les nouvelles règles débloquées dans les prochaines ligues sont&nbsp;:
    <ul>
      <!-- BEGIN level1 -->
      <li>Les héros peuvent lancer des sorts</li>
      <li>Brouillard de guerre</li>
      <!-- END -->
      <!-- BEGIN level2 -->
      <li>Nouveau sort : <action>CONTROL</action></li>
      <li>Nouveau sort : <action>SHIELD</action></li>
      <!-- END -->
    </ul>
  </div>
  <!-- END -->
</div>
<!-- SHOW_SAVE_PDF_BUTTON -->
