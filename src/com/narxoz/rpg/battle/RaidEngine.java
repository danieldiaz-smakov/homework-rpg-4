package com.narxoz.rpg.battle;

import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.CombatNode;

import java.util.Random;

public class RaidEngine {
    private static final int MAX_ROUNDS = 1000;

    private Random random = new Random(1L);

    public RaidEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public RaidResult runRaid(CombatNode teamA, CombatNode teamB, Skill teamASkill, Skill teamBSkill) {
        RaidResult result = new RaidResult();
        if (teamA == null || teamB == null || teamASkill == null || teamBSkill == null) {
            result.setRounds(0);
            result.setWinner("Invalid");
            result.addLine("Raid aborted: null team or skill");
            return result;
        }
        if (!teamA.isAlive() || !teamB.isAlive()) {
            result.setRounds(0);
            result.setWinner("TBD");
            result.addLine("Raid aborted: one or both teams already dead");
            return result;
        }

        int round = 0;
        while (round < MAX_ROUNDS) {
            round++;
            result.addLine("--- Round " + round + " ---");

            if (teamA.isAlive()) {
                result.addLine(teamA.getName() + " uses " + teamASkill.getSkillName() + " (" + teamASkill.getEffectName() + ") on " + teamB.getName());
                teamASkill.cast(teamB);
                if (!teamB.isAlive()) {
                    result.setRounds(round);
                    result.setWinner(teamA.getName());
                    result.addLine(teamB.getName() + " defeated. Winner: " + teamA.getName());
                    return result;
                }
            }

            if (teamB.isAlive()) {
                result.addLine(teamB.getName() + " uses " + teamBSkill.getSkillName() + " (" + teamBSkill.getEffectName() + ") on " + teamA.getName());
                teamBSkill.cast(teamA);
                if (!teamA.isAlive()) {
                    result.setRounds(round);
                    result.setWinner(teamB.getName());
                    result.addLine(teamA.getName() + " defeated. Winner: " + teamB.getName());
                    return result;
                }
            }
        }

        result.setRounds(MAX_ROUNDS);
        result.setWinner("Draw");
        result.addLine("Max rounds reached; battle ended in a draw.");
        return result;
    }
}
