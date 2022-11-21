package com.example.demo;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;


public class blackjack {

    public static final int pradiniai_pinigai = 100;
    public static final String[] zenklas = {"Širdžiu", "Vynai", "Dobilas", "Deimantu"};
    public static final String[] kortos = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};

    public static void main(String[] arg)	{
        Scanner console = new Scanner(System.in);
        List<Card> kalade = new ArrayList<Card>();
        kalade = kaladeskurimas(zenklas, kortos);
        int pinigai = pradiniai_pinigai;
        intro(pinigai);
        boolean play = true;

        while (pinigai > 0 && play == true) {

            int zaidejosum = 0;
            int Namosum = 0;

            List<Card> zaidejokortos = new ArrayList<Card>();
            List<Card> namokortos = new ArrayList<Card>();

            kalade = kaladesmaisymas(kalade);
            int roundBet = statymas(pinigai, console);

            System.out.print("Pirma korta: ");
            zaidejosum += drawCard(kalade, zaidejosum, zaidejokortos);

            System.out.print("Antra korta: ");
            zaidejosum += drawCard(kalade, zaidejosum, zaidejokortos);
            System.out.println();

            System.out.println("namas rodo: ");
            Namosum += drawCard(kalade, Namosum, namokortos);
            System.out.println("namas turi: " + Namosum);

            boolean kita_korta = true;
            while (zaidejosum < 21 && kita_korta){
                kita_korta = Hit(zaidejosum, console);
                if (zaidejosum > 21 || zaidejosum == 21 || !kita_korta) {
                    break;
                } else {
                    zaidejosum += drawCard(kalade, Namosum, namokortos);
                }

                for(int i = 0; i < zaidejokortos.size(); i++){
                    if (zaidejokortos.get(i).isAce() && zaidejosum > 21) {
                        zaidejosum -= 10;
                    }
                }
            }

            while (Namosum < 17 && zaidejosum < 21) {
                System.out.println("namas rodo: " + Namosum);
                Card dealerCard = kalade.remove(0);

                System.out.println("namas gauna: ");
                dealerCard.printCard();
                Namosum += dealerCard.giveValue(Namosum);
                namokortos.add(dealerCard);

                for(int i = 0; i < namokortos.size(); i++){
                    if (namokortos.get(i).isAce() && Namosum > 21) {
                        zaidejosum -= 10;
                    }
                }
            }

            System.out.println();
            pinigai += winCheck(zaidejosum, Namosum, roundBet);
            play = playAgain(console, pinigai);
        }
    }

    public static int drawCard(List<Card> newDeck, int playerTotal, List<Card> playersCards){
        int total = 0;
        Card playerCard1 = newDeck.remove(0);
        playerCard1.printCard();
        total += playerCard1.giveValue(playerTotal);
        playersCards.add(playerCard1);
        return total;
    }


    public static List<Card> kaladeskurimas(String[] suites, String[] name){
        List<Card> deck = new ArrayList<Card>();
        for (int i = 0; i < suites.length; i++){
            for (int j = 0; j < name.length; j++){
                Card k = new Card(name[j], suites[i]);
                deck.add(k);
            }
        }
        return deck;
    }


    public static List<Card> kaladesmaisymas(List<Card> deck){
        List<Card> shuffledDeck = new ArrayList<Card>();
        int r = 0;
        while (deck.size() > 0){
            Random card = new Random();
            r = card.nextInt(deck.size());
            Card temp = deck.remove(r);
            shuffledDeck.add(temp);
        }
        return shuffledDeck;
    }


    public static int winCheck(int suma, int namas, int to_play) {
        int gains_losses = 0;
        if (suma == 21) {
            System.out.println("tu turi: " + suma);
            System.out.println("tu gavai Blackjack!  laimejai!");
            gains_losses = 2 * to_play;
        } else if (suma > 21) {
            System.out.println("tu turi: " + suma);
            System.out.println("tu pralaimejai");
            gains_losses = -1 * to_play;
        } else if (suma == namas) {
            System.out.println("Tu turi: " + suma);
            System.out.println("namas turi: " + namas);
            System.out.println("Push.");
            gains_losses = 0;
        } else if (namas > 21) {
            System.out.println("namas turi: " + namas);
            System.out.println("namas iskrito.  tu laimejai!");
            gains_losses = 2 * to_play;
        } else if (suma < namas) {
            System.out.println("tu turi: " + suma);
            System.out.println("namas turi: " + namas);
            System.out.println("namas laimejo.");
            gains_losses = -1 * to_play;
        } else {
            System.out.println("tu turi: " + suma);
            System.out.println("namas turi: " + namas);
            System.out.println("tu nugalejai nama!");
            System.out.println("tu laimejai!");
            gains_losses = 2 * to_play;
        }
        return gains_losses;
    }


    public static int statymas(int money, Scanner console) {
        System.out.println("Kiek nori pastatyti?");
        int bet = Math.abs(console.nextInt());
        while(bet > money || bet < 10){
            if (bet < 10) {
                System.out.println("minimali zaidimo kaina $10");
            } else {
                System.out.println("neturi tiek pinigu.");
            }
            System.out.println("kiek nori pastatyti?");
            bet = console.nextInt();
        }
        return bet;
    }


    public static boolean Hit(int totaliai, Scanner console){
        boolean ans = false;
        System.out.println();
        System.out.println("tu turi: " + totaliai);
        System.out.println("ar nori hitinti ?");
        String answer = console.next();
        if (answer.indexOf("y") == 0 || answer.indexOf("Y") == 0) {
            ans = true;
        } else if (answer.indexOf("n") == 0 || answer.indexOf("N") == 0) {
            ans = false;
        }	else {
            System.out.println();
            ans = false;
        }
        return ans;
    }


    public static boolean playAgain(Scanner console, int pinigai){
        boolean ans;
        System.out.println("Tu turi: $" + pinigai);
        if (pinigai == 0) {
            System.out.println("tu be pinigu.  Namas laimejo.");
            ans = false;
            return ans;
        }
        System.out.println("Ar nori zaisti vel?");
        String answer = console.next();
        if (answer.indexOf("y") == 0 || answer.indexOf("Y") == 0) {
            ans = true;
            return ans;
        } else if (answer.indexOf("n") == 0 || answer.indexOf("N") == 0) {
            ans = false;
            if (pinigai > 100) {
                System.out.println("Sveikinu! tu laimejai: $" + (pinigai - 100));
            } else {
                System.out.println("Tu pralaimejai: $" + (100 - pinigai));
            }
            return ans;
        } else {
            System.out.println();
            ans = false;
        }
        return ans;
    }


    public static void intro(int pinigai) {
        System.out.println("sveiki atvyke i BlackJack!");
        System.out.println();
        System.out.println("tu turi: $" + pinigai);
    }


    public static class Card{
        private int value;
        private String name;
        private String suite;
        private boolean Ace;


        public Card(String name, String suite){
            this.name = name;
            this.suite = suite;
            this.value = determineCardValue(name);
        }


        public void printCard(){
            System.out.println(this.name + " of " + this.suite);
        }


        public int giveValue(int playerTotal){
            return this.value;
        }

        public boolean isAce(){
            return Ace;
        }


        private int determineCardValue(String name) throws NumberFormatException{
            int value = 0;
            try{
                value = Integer.parseInt(name.substring(0,1));
                return value;
            } catch (NumberFormatException e){
                if (name.charAt(0) == 'K' || name.charAt(0) == 'J' || name.charAt(0) == 'Q' || name.charAt(0) == '0'){
                    value = 10;
                } else if (name.charAt(0) =='A'){
                    value = 11;
                    this.Ace = true;
                } else {
                    value = Integer.parseInt(name.substring(0, 1));
                }
                return value;
            }
        }
    }
}