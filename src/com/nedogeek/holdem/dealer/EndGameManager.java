package com.nedogeek.holdem.dealer;

import com.nedogeek.holdem.GameSettings;
import com.nedogeek.holdem.PlayerStatus;
import com.nedogeek.holdem.combinations.PlayerCardCombination;
import com.nedogeek.holdem.gamingStuff.Player;
import com.nedogeek.holdem.gamingStuff.PlayersList;

/**
 * User: Konstantin Demishev
 * Date: 22.11.12
 * Time: 1:35
 */
public class EndGameManager {
    private final Dealer dealer;
    private final PlayersList playersList;


    public EndGameManager(Dealer dealer, PlayersList playersList) {
        this.dealer = dealer;
        this.playersList = playersList;
    }

    public void endGame() {
        Player winner = findWinner();
        dealer.setPlayerWin(winner);
        giveMoneyToWinner(winner);
        dealer.setGameEnded();
    }

    private void giveMoneyToWinner(Player winner) {

        int prize = 0;
        for (Player player : playersList) {
            prize += player.getBet();
            player.setBet(0);
            if (player.getBalance() == 0) {
                player.setBalance(GameSettings.COINS_AT_START);
            }
        }
        winner.setBalance(winner.getBalance() + prize);
        System.out.println("Winner: " + winner + " prize " + prize);
    }

    private Player findWinner() {
        Player winCandidate = null;
        for (Player player : playersList) {
            winCandidate = bestFromPlayers(winCandidate, player);
        }
        System.out.println(winCandidate.getCardCombination().toString());
        return winCandidate;
    }

    private Player bestFromPlayers(Player firstPlayer, Player secondPlayer) {
        if (!isActivePlayer(firstPlayer)) {
            return secondPlayer;
        }
        if (!isActivePlayer(secondPlayer)) {
            return firstPlayer;
        }

        PlayerCardCombination firstPlayerCardCombination = firstPlayer.getCardCombination();
        PlayerCardCombination secondPlayerCardCombination = secondPlayer.getCardCombination();

        return (firstPlayerCardCombination.compareTo(secondPlayerCardCombination) > 0) ?
                firstPlayer : secondPlayer;
    }

    private boolean isActivePlayer(Player player) {
        return player != null && player.getStatus() != PlayerStatus.Fold;
    }
}
