# Deterministic-Finite-Automata-Minimization
DFA PROJECT JAVA PROGRAM

## MOTIVATION
This program minimizes the number of states of a Deterministic Finite
Automata (DFA). This program uses tabulation method of Myhill-Nerode Theorem
to generate an equivalent DFA to that input but the user. The user is then
able input strings and the program determines whether the string is accepted or
not by the minimized DFA.

## HOW TO USE?
There are two parts ot this program:
1. For the first part the user is prompted to input the DFA. Part of a session
is as follows:

    Enter alphabet symbols as a blank separated list (e.g. a b c)
    a b
    Enter blank separated list of state numbers (0 is assumed to be start state)
    0 1 2 3
    Enter accepting states as a blank separate list
    1 2
    Enter next state for each transition when prompted
    Transition for (0,a) : 1
    Transition for (0,b) : 4
    Invalid state 4. Enter again
    Transition for (0,b) : 3
    ...

    * ALPHABET INPUT: The alphabet symbols should be any character, each separated by a
    space (used as a delimiter).  Duplicates will be treated as one.
    * STATES INPUT: The states can be any set of integers, separated by only one space.
    Duplicates will be treated as one state.
    * ACCEPT STATES INPUT: Accept states must be any set of integers that are contained
    in the set of states. Duplicates will be treated as one state. Each integer for the
    accept state must have already been included in the states, otherwise it will be
    excluded from accept states.
    * TRANSITIONS INPUT: The user will be shown all pairs from the cross product of the
    states and alphabet.  This essentially represents all inputs for the delta transition
    function of a DFA. For each pair, the user will be prompted for the output state. If
    the output state is not a state, the user will be prompted for that transition again.

    The program then computes and prints the minimum state DFA in the same format: states,
    accepting states, and transitions.

2. For the second part the user is continually prompted for a string of symbols. The
minimum state DFA is used to print the successive states and whether the string is
accepted or rejected. An example session is as follows:

    Enter a string (Type "exit" to exit): abba
    (0,a) -> (1,b) -> (0,b) -> (0,a) -> 1
    String accepted.
    Enter a string (Type "exit" to exit): abab
    (0,a) -> (1,b) -> (0,a) -> (1,b) -> 0
    String not accepted.

    This part of the program is looped, so in order to exit and end the program, the user
    is to input "exit" in order to stop.

## HOW TO COMPILE
javac Main.java

## HOW TO RUN
java Main

No command line arguments are needed.  All input is taken from standard input.

## ACKNOWLEDGEMENTS
In order to implement my program, I needed a Pair Object, which I was able to get
from https://www.techiedelight.com/implement-pair-class-java/. I did tweek this code
in order to make the class implement Comparable which was necessary in order to use
sorting methods on objects containing Pairs.

## AUTHOR
Denisse Mendoza
