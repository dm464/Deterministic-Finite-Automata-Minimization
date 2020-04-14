/*
Denisse Mendoza
CS 341 Spring 2020
DFA Minimization Program
*/

import java.util.*;
import java.lang.Integer;
import java.lang.Character;

// This implementation of the Pair class was taken from
// https://www.techiedelight.com/implement-pair-class-java/
// and I tweeked it in order to make it comparable

// Pair class
class Pair<U extends Comparable<U>, V extends Comparable<V>> implements Comparable<Pair<U,V>> {
	public final U first;		// first field of a Pair
	public final V second; 	// second field of a Pair

	// Constructs a new Pair with specified values
	public Pair(U first, V second) {
		this.first = first;
		this.second = second;
	}

	@Override
	// Checks specified object is "equal to" current object or not
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		Pair<?, ?> pair = (Pair<?, ?>) o;

		// call equals() method of the underlying objects
		if (!first.equals(pair.first))
			return false;
		return second.equals(pair.second);
	}

	@Override
	// Computes hash code for an object to support hash tables
	public int hashCode() {
		// use hash codes of the underlying objects
		return 31 * first.hashCode() + second.hashCode();
	}

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
    }
    
    @Override
    // Implements compareTo method from Comparable class
    public int compareTo(Pair<U, V> o) {
        if (first.compareTo(o.first) == 0) {
			return second.compareTo(o.second);
		}
		else {
			return first.compareTo(o.first);
		}
    }

	// Factory method for creating a Typed Pair instance
	public static <U extends Comparable<U>, V extends Comparable<V>> Pair <U, V> of(U a, V b)
	{
		// calls public constructor
		return new Pair<>(a, b);
	}
}

class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // Get user input for alphabet, states, and accept states
        System.out.println("Enter alphabet symbols as a space separated list (e.g. a b c)");
        char[] alphabet = scan.nextLine().replaceAll(" ", "").toCharArray();
        System.out.println("Enter space separated list of state numbers (0 is assumed to be start state)");
        String[] statesArr = scan.nextLine().split(" ");
        HashSet<Integer> states = new HashSet<>();
        for (String state: statesArr) {
            if (!state.equals("")) {
                states.add(Integer.parseInt(state));
            }
        }
        System.out.println("Enter accepting states as a space separated list");
        String[] acceptArr = scan.nextLine().split(" ");
        HashSet<Integer> accept = new HashSet<>();
        for (String state: acceptArr) {
            if (!states.equals("")) {
                if (states.contains(Integer.parseInt(state))) {
                    accept.add(Integer.parseInt(state));
                }
                else {
                    System.out.println(state + " is not a state (excluded).");
                }
            }
        }
        // Create transitions map where a pair (state, character) is mapped to an output state
        HashMap<Pair<Integer, Character>, Integer> transitions = new HashMap<>();
        System.out.println("Enter next state for each transition when prompted");
        for (Integer state: states) {
            for (int i = 0; i < alphabet.length; i++) {
                System.out.print("Transition for (" + state + "," + alphabet[i] +") : ");
                int trans = scan.nextInt();
                if (states.contains(trans)) {
                    transitions.put(Pair.of(state, alphabet[i]), trans);
                }
                else {
                    System.out.println("Invalid state " + trans);
                    i--;
                }
            }
        }
        scan.nextLine();

        // Minimize DFA
        minimizeDFA(transitions, alphabet, states, accept);
        // Print Minimum State DFA facts
        System.out.println("Minimum state DFA is given as follows:");
        printDFA(transitions, alphabet, states, accept);

        // Continually checks if given strings are accepted by the minimized DFA
        while(true){
            System.out.print("Enter a string (Type \"exit\" to exit): ");
            String msg = scan.nextLine();
            if (msg.equals("exit")) {
                break;
            }
            accepted(transitions, accept, msg);
        }
    }
    // minimizeDFA() minimizes the DFA with transisions, alphabet, states, and accept states passed as parameters,
    // eliminating classes of indistinguishable states using Myhill-Nerode Theorem's tabulation method
    static void minimizeDFA(HashMap<Pair<Integer, Character>, Integer> transitions, char[] alphabet, HashSet<Integer> states, HashSet<Integer> accept) {
        Integer[] s = states.toArray(new Integer[0]);
	Arrays.sort(s);
        HashMap<Pair<Integer, Integer>, String> dist = new HashMap<>();
        // Determines whether state pairs are distinguishable based on whether they're an accept state or not
        for (int i = 0; i < s.length - 1; i++) {
            for (int j = i + 1; j < s.length; j++) {
                boolean aIsAccept = accept.contains(s[i]);
                boolean bIsAccept = accept.contains(s[j]);
                if (aIsAccept != bIsAccept) {
                    dist.put(Pair.of(s[i], s[j]), "distinguishable");
                }
                else {
                    dist.put(Pair.of(s[i], s[j]), "");
                }
            }
        }
        // Determines whether state pairs are distinguishable based on whether their transitions (for the same character)
        // lead to dinstinguishable states
        int count;
        do {
            count = 0;
            for (int i = 0; i < s.length - 1; i++) {
                for (int j = i + 1; j < s.length; j++) {
                    if (dist.get(Pair.of(s[i], s[j])).equals("distinguishable")) {
                        continue;
                    }
                    for (char c: alphabet) {
                        Integer transA = transitions.get(Pair.of(s[i], c));
                        Integer transB = transitions.get(Pair.of(s[j], c));
			if (transA == transB) continue;
                        Pair<Integer, Integer> pair = (transA < transB ? Pair.of(transA, transB): Pair.of(transB, transA));
                        if (dist.get(pair).equals("distinguishable")) {
                            dist.replace(Pair.of(s[i], s[j]), "distinguishable");
                            count++;
                            break;
                        }
                    }
                }
            }
        } while(count != 0);
        // Create set of indistinguishable states
        HashSet<Pair<Integer, Integer>> indist = new HashSet<>();
        for (Map.Entry<Pair<Integer, Integer>, String> entry: dist.entrySet()) {
            if (entry.getValue().equals("")) {
                indist.add(entry.getKey());
            }
        }
        // If there are no indistingishable states, DFA is already minimized and remains unchanged
        if (indist.isEmpty()) {
            return;
        }
        // Removing duplicate states
        Object[] dupPairs = indist.toArray(new Object[1]);
        Arrays.sort(dupPairs, Collections.reverseOrder());
        for (Object dupPair: dupPairs) {

            Integer dupState = ((Pair<Integer, Integer>) dupPair).second;
            Integer replacementState = ((Pair<Integer, Integer>) dupPair).first;
            states.remove(dupState);
            //remove duplicate state from set of states
            if (accept.contains(dupState)) {
                accept.remove(dupState);
            }
            // remove all transitions with duplicate state as input (inState) and
            // if a transition is mapped to a duplicate states (outState), replace with the replacement state
            Iterator<Map.Entry<Pair<Integer, Character>, Integer>> iterator = transitions.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Pair<Integer, Character>, Integer> trans = iterator.next();
                Integer inState = trans.getKey().first;
                Integer outState = trans.getValue();
                if (inState.equals(dupState)) {
                    iterator.remove();
                    continue;
                }
                else if (outState.equals(dupState)) {
                    transitions.replace(trans.getKey(), replacementState);
                }
            }
        }
        return;
    }

    // accepted() determines whether a string is accepted by a DFA
    // while printing the transitions
    static void accepted(HashMap<Pair<Integer, Character>, Integer> transitions, HashSet<Integer> accept, String str) {
        // Checks empty string
        if (str.equals("")) {
            System.out.println("0");
            System.out.println("String " + (accept.contains(0)? "" : "not ") + "accepted.");
            return;
        }
        System.out.print("(0," + str.charAt(0) + ")");
        Integer nextState = transitions.get(Pair.of(0, str.charAt(0)));
        // Checks for invalid first character
        if (nextState == null) {
            System.out.print(" -> null\n");
            System.out.println("String not accepted.");
            return;
        }
        for (int i = 1; i < str.length(); i++) {
            System.out.print(" -> (" +nextState +"," + str.charAt(i) + ")");
            nextState = transitions.get(Pair.of(nextState, str.charAt(i)));
            if (nextState == null) {
                System.out.print(" -> null\n");
                System.out.println("String not accepted.");
                return;
            }
        }
        System.out.println(" -> " + nextState);
        System.out.println("String " + (accept.contains(nextState)? "" : "not ") + "accepted.");
    }

    static void printDFA(HashMap<Pair<Integer, Character>, Integer> transitions, char[] alphabet, HashSet<Integer> states, HashSet<Integer> accept) {
        System.out.print("States: ");
        Integer[] s = states.toArray(new Integer[0]);
        Arrays.sort(s);
        for (Integer state: s) {
            System.out.print(state + " ");
        }
        System.out.println();
        System.out.print("Accepting States: ");
        Integer[] a = accept.toArray(new Integer[0]);
        Arrays.sort(a);
        for (Integer state: a) {
            System.out.print(state + " ");
        }
        System.out.println();
        System.out.println("Transitions: ");
        for (int i = 0; i < s.length; i++){
            for (int j = 0; j < alphabet.length; j++) {
                System.out.print("(" + s[i] + "," + alphabet[j] + ") -> ");
                System.out.println(transitions.get(Pair.of(s[i], alphabet[j])));
            }
        }
    }
}
