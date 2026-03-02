package com.android.one

// ═══════════════════════════════════════════════════════════════════════════════
// Battle Arena — Console Prototype
// README_03.MD
//
// Two players each create a team of 3 characters.
// They take turns choosing a character, then an action (attack / heal).
// The game ends when one team is fully eliminated.
// ═══════════════════════════════════════════════════════════════════════════════

// ─────────────────────────────────────────────────────────────────────────────
// Interfaces — define shared capabilities (polymorphism via interface)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Any character that can attack implements this interface.
 * Forces implementing classes to expose an attack method.
 */
interface Attacker {
    /** Attacks [target] and returns the actual damage dealt. */
    fun attack(target: Character): Int
}

/**
 * Only characters with healing capability implement this interface.
 * Forces implementing classes to expose a heal method.
 */
interface Healer {
    /** Heals [ally] and returns the amount of HP restored. */
    fun heal(ally: Character): Int
}

// ─────────────────────────────────────────────────────────────────────────────
// Weapon — composition: a Character *has* a Weapon
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Represents a weapon carried by a character.
 *
 * @param name   Display name of the weapon.
 * @param power  Base damage or healing power of the weapon.
 */
data class Weapon(val name: String, val power: Int)

// ─────────────────────────────────────────────────────────────────────────────
// Character — abstract base class (encapsulation + forced subclass contract)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Abstract base for every fighter in the game.
 * Encapsulates health management so subclasses cannot break the HP invariant.
 *
 * @param name      Unique name chosen by the player.
 * @param typeName  Display label for the character type (e.g. "Warrior").
 * @param maxHp     Maximum (and starting) hit points.
 * @param weapon    The weapon this character carries (composition).
 */
abstract class Character(
    val name: String,
    val typeName: String,
    val maxHp: Int,
    val weapon: Weapon
) {
    // ── Encapsulation: HP is managed entirely inside the class ──────────────
    private var currentHp: Int = maxHp

    /** Whether this character is still alive. */
    val isAlive: Boolean get() = currentHp > 0

    /** Current HP — readable but not directly settable from outside. */
    val hp: Int get() = currentHp

    /**
     * Reduces this character's HP by [damage].
     * HP cannot go below 0.
     */
    fun takeDamage(damage: Int) {
        currentHp = maxOf(0, currentHp - damage)
    }

    /**
     * Restores this character's HP by [amount].
     * HP cannot exceed [maxHp].
     */
    fun receiveHeal(amount: Int) {
        currentHp = minOf(maxHp, currentHp + amount)
    }

    /**
     * Abstract action method — subclasses must define what their character
     * can do on their turn.  (polymorphism via abstract method)
     */
    abstract fun action()

    /** Single-line status summary. */
    override fun toString(): String =
        "[$typeName] $name — HP: $currentHp/$maxHp | Weapon: ${weapon.name} (${weapon.power} pwr)"
}

// ─────────────────────────────────────────────────────────────────────────────
// Subclasses — concrete character types (inheritance)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Warrior — balanced attacker.
 * HP: medium (120) | Weapon power: medium (25)
 * Can ONLY attack (does not implement Healer).
 */
class Warrior(name: String) :
    Character(name, "Warrior", maxHp = 120, weapon = Weapon("Sword", power = 25)),
    Attacker {

    override fun action() {
        println("  ⚔️  $name readies their Sword for a balanced strike!")
    }

    /**
     * Deals [weapon.power] damage to [target].
     */
    override fun attack(target: Character): Int {
        val damage = weapon.power
        target.takeDamage(damage)
        println("  ⚔️  $name attacks ${target.name} with ${weapon.name} for $damage damage!")
        return damage
    }
}

/**
 * Magus — support/healer with weak attack.
 * HP: high (150) | Weapon power: low (15) | Heal power: 30
 * Can ATTACK and HEAL.
 */
class Magus(name: String) :
    Character(name, "Magus", maxHp = 150, weapon = Weapon("Staff", power = 15)),
    Attacker, Healer {

    private val healPower = 30

    override fun action() {
        println("  🔮 $name channels arcane energy!")
    }

    override fun attack(target: Character): Int {
        val damage = weapon.power
        target.takeDamage(damage)
        println("  🔮 $name blasts ${target.name} with ${weapon.name} for $damage damage!")
        return damage
    }

    /**
     * Restores [healPower] HP to [ally].
     */
    override fun heal(ally: Character): Int {
        ally.receiveHeal(healPower)
        println("  💚 $name heals ${ally.name} for $healPower HP!")
        return healPower
    }
}

/**
 * Colossus — tank with very high HP and moderate weapon.
 * HP: very high (200) | Weapon power: medium (20)
 * Can ONLY attack.
 */
class Colossus(name: String) :
    Character(name, "Colossus", maxHp = 200, weapon = Weapon("War Hammer", power = 20)),
    Attacker {

    override fun action() {
        println("  🪨 $name raises their War Hammer, shaking the ground!")
    }

    override fun attack(target: Character): Int {
        val damage = weapon.power
        target.takeDamage(damage)
        println("  🪨 $name crushes ${target.name} with ${weapon.name} for $damage damage!")
        return damage
    }
}

/**
 * Dwarf — glass cannon: very low HP, very high damage.
 * HP: low (70) | Weapon power: very high (45)
 * Can ONLY attack.
 */
class Dwarf(name: String) :
    Character(name, "Dwarf", maxHp = 70, weapon = Weapon("Battle Axe", power = 45)),
    Attacker {

    override fun action() {
        println("  🪓 $name lets out a fierce war cry and charges!")
    }

    override fun attack(target: Character): Int {
        val damage = weapon.power
        target.takeDamage(damage)
        println("  🪓 $name cleaves ${target.name} with ${weapon.name} for $damage damage!")
        return damage
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Player — holds a team and a name
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Represents a human player with a name and a team of characters.
 *
 * @param name       Display name of the player.
 * @param characters The player's team (3 characters).
 */
class Player(val name: String, val characters: MutableList<Character> = mutableListOf()) {

    /** Living members of this player's team. */
    val aliveCharacters: List<Character> get() = characters.filter { it.isAlive }

    /** True when the entire team has been eliminated. */
    val isDefeated: Boolean get() = aliveCharacters.isEmpty()

    /** Print the current status of all characters in this team. */
    fun printTeamStatus() {
        println("  🛡️  ${name}'s team:")
        characters.forEach { c ->
            val status = if (c.isAlive) "alive" else "💀 dead"
            println("     • $c [$status]")
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Game helpers
// ─────────────────────────────────────────────────────────────────────────────

/** Reads a non-blank line from stdin (trims whitespace). */
fun readInput(prompt: String): String {
    print(prompt)
    return readln().trim()
}

/**
 * Prompts the user to pick a number from a numbered list.
 * Keeps asking until a valid index is entered.
 *
 * @param options  The list of options to display.
 * @param prompt   Text shown to the user.
 * @return         The chosen item from [options].
 */
fun <T> pickFrom(options: List<T>, prompt: String, display: (T) -> String = { it.toString() }): T {
    options.forEachIndexed { i, item -> println("  ${i + 1}. ${display(item)}") }
    while (true) {
        val input = readInput(prompt)
        val idx = input.toIntOrNull()
        if (idx != null && idx in 1..options.size) return options[idx - 1]
        println("  ⚠️  Invalid choice. Enter a number between 1 and ${options.size}.")
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Team creation
// ─────────────────────────────────────────────────────────────────────────────

/**
 * All available character type labels — each type may only appear once per team.
 */
val CHARACTER_TYPES = listOf("Warrior", "Magus", "Colossus", "Dwarf")

/**
 * Builds a character of the given [type] with the given [name].
 */
fun buildCharacter(type: String, name: String): Character = when (type) {
    "Warrior"  -> Warrior(name)
    "Magus"    -> Magus(name)
    "Colossus" -> Colossus(name)
    "Dwarf"    -> Dwarf(name)
    else       -> throw IllegalArgumentException("Unknown type: $type")
}

/**
 * Interactively creates a team of 3 characters for [playerName].
 * Enforces:
 *  - Each type is unique within the team.
 *  - Each character name is unique across the entire game (tracked in [usedNames]).
 */
fun createTeam(playerName: String, usedNames: MutableSet<String>): MutableList<Character> {
    println("\n── Creating team for $playerName ──────────────────────────────")
    val team = mutableListOf<Character>()
    val usedTypes = mutableSetOf<String>()

    repeat(3) { slot ->
        println("\n  Slot ${slot + 1}/3")

        // Choose a type not yet used in this team
        val availableTypes = CHARACTER_TYPES.filter { it !in usedTypes }
        println("  Choose a character type:")
        val chosenType = pickFrom(availableTypes, "  > ") { it }
        usedTypes.add(chosenType)

        // Choose a unique name
        var name: String
        while (true) {
            name = readInput("  Enter a unique name for your $chosenType: ")
            when {
                name.isBlank()       -> println("  ⚠️  Name cannot be empty.")
                name in usedNames    -> println("  ⚠️  '$name' is already taken. Choose another.")
                else                 -> break
            }
        }
        usedNames.add(name)

        val character = buildCharacter(chosenType, name)
        team.add(character)
        println("  ✅ ${character.typeName} '$name' created! (HP: ${character.maxHp}, Weapon: ${character.weapon.name} [${character.weapon.power}])")
    }

    return team
}

// ─────────────────────────────────────────────────────────────────────────────
// Turn logic
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Executes a single turn for [activePlayer] against [enemyPlayer].
 * The active player:
 *  1. Chooses a living character from their team.
 *  2. Chooses an action (attack / heal if possible).
 *  3. Chooses a target.
 */
fun playTurn(activePlayer: Player, enemyPlayer: Player) {
    println("\n  ${activePlayer.name}'s turn — choose your fighter:")
    val fighter = pickFrom(activePlayer.aliveCharacters, "  > ") { c -> "$c" }

    // Determine available actions
    val canAttack = fighter is Attacker
    val canHeal   = fighter is Healer && activePlayer.aliveCharacters.isNotEmpty()

    val actions = mutableListOf<String>()
    if (canAttack) actions.add("Attack")
    if (canHeal)   actions.add("Heal ally")

    println("  Choose an action:")
    val action = pickFrom(actions, "  > ") { it }

    when (action) {
        "Attack" -> {
            println("  Choose an enemy target:")
            val target = pickFrom(enemyPlayer.aliveCharacters, "  > ") { c -> "$c" }
            (fighter as Attacker).attack(target)
            if (!target.isAlive) println("  💀 ${target.name} has been defeated!")
        }
        "Heal ally" -> {
            println("  Choose an ally to heal:")
            val ally = pickFrom(activePlayer.aliveCharacters, "  > ") { c -> "$c" }
            (fighter as Healer).heal(ally)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Main entry point — game loop
// ─────────────────────────────────────────────────────────────────────────────

fun main() {
    println("╔══════════════════════════════════════════════╗")
    println("║          ⚔️   BATTLE  ARENA  ⚔️              ║")
    println("║           Console Prototype v1.0             ║")
    println("╚══════════════════════════════════════════════╝\n")

    // ── Step 1: Get player names ────────────────────────────────────────────
    val player1Name = readInput("Enter name for Player 1: ")
    val player2Name = readInput("Enter name for Player 2: ")

    // ── Step 2: Team creation ───────────────────────────────────────────────
    val usedNames = mutableSetOf<String>()
    val player1 = Player(player1Name, createTeam(player1Name, usedNames))
    val player2 = Player(player2Name, createTeam(player2Name, usedNames))

    println("\n╔══════════════════════════════════════════════╗")
    println("║               ⚔️   BATTLE TIME!              ║")
    println("╚══════════════════════════════════════════════╝")

    // ── Step 3: Game loop ───────────────────────────────────────────────────
    var turn = 1
    var currentPlayer  = player1
    var opponentPlayer = player2

    while (!player1.isDefeated && !player2.isDefeated) {
        println("\n══════════════════════ TURN $turn ══════════════════════")

        println("\n📊 Current Status:")
        player1.printTeamStatus()
        player2.printTeamStatus()

        playTurn(currentPlayer, opponentPlayer)

        // Swap active player
        val temp = currentPlayer
        currentPlayer  = opponentPlayer
        opponentPlayer = temp

        turn++
    }

    // ── Step 4: End screen ──────────────────────────────────────────────────
    println("\n╔══════════════════════════════════════════════╗")
    println("║                🏆  GAME OVER  🏆             ║")
    println("╚══════════════════════════════════════════════╝")

    val winner = if (!player1.isDefeated) player1 else player2
    println("\n🎉  ${winner.name} WINS after ${turn - 1} turn(s)!\n")

    println("📊 Final character status:")
    println()
    player1.printTeamStatus()
    println()
    player2.printTeamStatus()
}
