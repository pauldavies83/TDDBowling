package com.example.pauldavies.tddbowling

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test


class BowlingTest {

    lateinit var game: Game

    @BeforeEach
    fun setUp() {
        game = Game()
    }

    @Test
    fun when_a_game_is_incomplete_i_throw_an_exception() {
        val game = Game()

        assertThat({game.score()}, throws<GameNotOverException>())
    }

    @Test
    fun when_i_roll_twenty_gutter_balls_the_score_is_zero() {
        rollTwentyBallsOfTheSameScore(0)

        assertThat(game.score(), equalTo(0))
    }

    private fun rollTwentyBallsOfTheSameScore(score: Int) {
        for (i in 1..20) {
            game.roll(score)
        }
    }

    @Test
    fun when_i_roll_twenty_ones_the_score_is_twenty() {
        rollTwentyBallsOfTheSameScore(1)

        assertThat(game.score(), equalTo(20))
    }

    @Test
    fun whenIRollASpare_theBonusIsApplied() {
        game.roll(3)
        game.roll(7)
        game.roll(6)
        for (i in 4..20) {
            game.roll(0)
        }

        assertThat(game.score(), equalTo(22))
    }

    @Test
    fun whenIRollASpareNoATheStart_theBonusIsApplied() {
        game.roll(0)
        game.roll(0)
        game.roll(6)
        game.roll(4)
        game.roll(1)
        for (i in 6..20) {
            game.roll(0)
        }

        assertThat(game.score(), equalTo(12))
    }

    @Test
    fun whenIRollASpareAtTheEndOfTheGame_theBonusIsCorrectlyApplied() {
        for (i in 1..18) {
            game.roll(0)
        }
        game.roll(8)
        game.roll(2)
        game.roll(9)

        assertThat(game.score(), equalTo(19))
    }


}

class GameNotOverException : Throwable()

class Game {
    var gameScore = IntArray(21)

    var rolls = 0

    fun score(): Int {
        if (rolls < 20) throw GameNotOverException()

        var runningScore = 0

        var i = 0
        while (i < 19) {

            val firstBall = gameScore[i]
            val secondBall = gameScore[(++i)]

            runningScore += (firstBall + secondBall)

            if (firstBall + secondBall == 10) {
                runningScore += gameScore[i + 1]
            }

            i++
        }

        return runningScore
    }

    fun roll(pins: Int) {
        gameScore[rolls] = pins
        rolls++
    }

}
