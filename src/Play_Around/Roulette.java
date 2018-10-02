package Play_Around;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Roulette {

	static Scanner in = new Scanner(System.in);
	static ArrayList<String> picked = new ArrayList<String>();
	static Random rand = new Random();

	public static void main(String[] args) {

		final int START_CASH = 1000;
		int playersCash, betAmount = 0, countColor = 0, randomNum;
		int inputInt, betInt, roundCounter = 0;
		boolean stop = false, deletedRound = false;
		String inputChosen, betColor, again;

		playersCash = START_CASH;

		System.out.println("Hello to a round of Roulette");
		System.out.println("You begin with " + START_CASH + " CHF.");
		System.out.println("Let's start with your first bet:");

		do {
			countColor = 0;
			picked.clear();
			roundCounter++;
			betAmount = 0;
			do {
				// Start of the betting round
				System.out
						.println("\nPlease tell me, how much money you want to punt (out of " + playersCash + " CHF):");
				inputInt = getPlayersBet(playersCash);
				betAmount += inputInt;
				playersCash -= inputInt;

				System.out.println("\nNow tell me on what you want to bet your money (" + inputInt + "CHF)");
				if (countColor < 2) {
					System.out.println("Do you want to bet for a color or for a number?:");
					inputChosen = colorOrNumber();
				} else {
					System.out.println("What number do you want to pick?");
					inputChosen = "number";
				}

				if (inputChosen.equalsIgnoreCase("number")) {
					betInt = getNumber(picked);
					picked.add("number[" + betInt + "]" + inputInt);
				} else {
					countColor++;
					betColor = getColor(picked);
					betColor = betColor.toLowerCase();
					picked.add("color[" + betColor + "]" + inputInt);
				}

				System.out.println("\nDo you want to bet on something else?"
						+ " (type in \"yes\" or \"no\" or \"delete\" to reset all your bettings for this round):");
				do {
					again = in.nextLine();
					if (again.equalsIgnoreCase("yes") || again.equalsIgnoreCase("no")) {
						stop = true;
					} else if (again.equalsIgnoreCase("delete")) {
						stop = true;
						deletedRound = true;
						roundCounter = roundCounter - 1;
						picked.clear();
						playersCash += betAmount;
					} else {
						System.out.println("Please type in \"yes\" or \"no\" or \"delete\"");
						stop = false;
					}
				} while (stop == false);

			} while (again.equalsIgnoreCase("yes") && playersCash > 0);
			if (playersCash == 0) {
				System.out.println("\nYou have no money left for another round, let's turn the wheel:");
				again = "no";
			}

			// Results and turning of the roulette here
			if (deletedRound == false) {

				if (again.equalsIgnoreCase("no")) {
					System.out.println("\nThe wheel is turning, please wait a moment");
					try {
						Thread.sleep(3500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("The wheel is still turning...");
					try {
						Thread.sleep(4500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

// get the result			
				randomNum = rand.nextInt(37);
				System.out.print("\nThe ball stops at number " + randomNum);
				playersCash += result(picked, randomNum);
				System.out.println("Your account is now at " + playersCash + " CHF..");
			}
			deletedRound = false;

			// Play again?
			if (playersCash > 0) {
				System.out.println("\nDo you want to play another round of roulette? (\"yes\" or \"no\")");
				betAmount = 0;
				boolean newRound = false;
				do {
					again = in.nextLine();
					if (again.equalsIgnoreCase("yes")) {
						stop = false;
						newRound = true;
					} else if (again.equalsIgnoreCase("no")) {
						stop = true;
						newRound = true;
					} else {
						System.out.println("Please type in \"yes\" or \"no\"");
						newRound = false;
					}
				} while (newRound == false);
			} else {
				stop = true;
			}
		} while (stop == false);

		System.out.println(
				"\n\n" + roundCounter + " rounds have been played and your account is at " + playersCash + " CHF");
		if (playersCash == START_CASH) {
			System.out.println("You haven't won or lost any money");
		} else if (playersCash < START_CASH) {
			System.out.println("Sadly, you have lost " + (START_CASH - playersCash) + " CHF. Better luck next time..");
		} else {
			System.out.println("You won " + (playersCash - START_CASH) + " CHF. Congratulation!");
		}

		System.out.println("\nThanks for playing!");

		in.close();
	}

	// Method section
	public static int getPlayersBet(int account) {
		int amount = 0;
		boolean num = false;

		while (amount == 0 || amount > account) {
			do {
				try {
					in = new Scanner(System.in);
					amount = in.nextInt();
					num = true;
				} catch (Exception e) {
					System.out.println("Please type in an integer:");
					num = false;
				}
			} while (num == false);

			if (num == true && (amount == 0 || amount > account)) {
				System.out.println(
						"Please type in an integer over 0 and lower or equal to your account (" + account + "CHF):");
			}
		}
		return amount;
	}

	public static String colorOrNumber() {
		String decision = null;
		boolean right = true;
		while (right) {
			decision = in.nextLine();
			if (!decision.equalsIgnoreCase("color") && !decision.equalsIgnoreCase("number")) {
				System.out.println("Please chose between \"color\" or \"number\":");
			} else {
				right = false;
			}
		}
		return decision;
	}

	public static int getNumber(ArrayList<String> list) {
		int amount = 100;
		boolean num = false;
		System.out.println("Please type in an integer between 0 and 36:");
		while (amount > 36) {
			do {
				try {
					in = new Scanner(System.in);
					amount = in.nextInt();
					num = true;
					in.nextLine();
				} catch (Exception e) {
					System.out.println("Please type in an integer:");
					num = false;
				}
			} while (num == false);
			if (num == true && (amount > 36)) {
				System.out.println("Please type in an integer between 0 and 36:");
			} else if (isUsed(list, amount)) {
				System.out.println("You have already chosen this number, please choose another one:");
				amount = 100;
			}
		}
		return amount;
	}

	public static String getColor(ArrayList<String> list) {
		String decision = null;
		boolean right = true;
		for (String a : list) {
			if (a.contains("black")) {
				System.out.println("\nYou already have chosen black, so your bet ist now on red.");
				decision = "red";
			} else if (a.contains("red")) {
				System.out.println("\nYou already have chosen red, so your bet ist now on black.");
				decision = "black";
			}
		}
		if (decision == null) {
			System.out.println("\nPlease chose between \"red\" or \"black\":");
			while (right) {
				decision = in.nextLine();
				if (!decision.equalsIgnoreCase("red") && !decision.equalsIgnoreCase("black")) {
					System.out.println("Please chose between \"red\" or \"black\":");
				} else {
					right = false;
				}
			}
		}
		return decision;
	}

	public static boolean isUsed(ArrayList<String> list, int pickedNow) {
		boolean used = false;
		String check;
		for (String a : list) {
			check = a.substring(a.indexOf("[") + 1, a.indexOf("]"));
			if (check.equalsIgnoreCase(Integer.toString(pickedNow))) {
				used = true;
			}
		}
		return used;
	}

	public static int result(ArrayList<String> list, int random) {
		// Random num shoud be shown as a result!
		// if 0 = black and red doesn't count
		int cash = 0, bet, allBets = 0;
		String color, check;

		if (random == 0) {
			color = "green";
			System.out.print(" (no color)\n");
		} else if ((random % 2) == 0) {
			// gerade zahlen
			color = "black";
			System.out.print(" (black)\n");
		} else {
			color = "red";
			System.out.print(" (red)\n");
		}
//		bet = Integer.parseInt(a.substring(a.indexOf("]" + 1, a.indexOf(a.length() - 1))));
		for (String a : list) {
			check = a.substring(a.indexOf("[") + 1, a.indexOf("]"));
			if (check.equalsIgnoreCase("black") && color.equalsIgnoreCase("black")) {
				bet = Integer.parseInt(a.substring(a.indexOf("]") + 1));
				allBets += bet;
				cash = bet * 2;
				System.out.println("You got the right color (\"black\"), so your bet (" + bet + ") will be doubled.");
				System.out.println("this means you win " + cash + " CHF for this bet.");
			} else if (check.equalsIgnoreCase("red") && color.equalsIgnoreCase("red")) {
				bet = Integer.parseInt(a.substring(a.indexOf("]") + 1));
				allBets += bet;
				cash = bet * 2;
				System.out.println("You got the right color (\"red\"), so your bet (" + bet + ") will be doubled.");
				System.out.println("this means you win " + cash + " CHF for this bet.");
			} else if (check.equalsIgnoreCase(Integer.toString(random))) {
				bet = Integer.parseInt(a.substring(a.indexOf("]") + 1));
				allBets += bet;
				cash = bet * 35;
				System.out.println("You got the right number (\"" + random + "\"), so your bet (" + bet
						+ ") will be multiplied by 35.");
				System.out.println("this means you win " + cash + " CHF for this bet.");
			} else {
				bet = Integer.parseInt(a.substring(a.indexOf("]") + 1));
				allBets += bet;
			}
		}
		if (cash == 0) {
			System.out.println("\nYou haven't won in this round, while betting " + allBets + " CHF");
		} else {
			System.out.println("\nYou have won " + cash + " CHF, while betting " + allBets + " CHF");
		}
		return cash;
	}

} // Additional time 3h 40 minutes
