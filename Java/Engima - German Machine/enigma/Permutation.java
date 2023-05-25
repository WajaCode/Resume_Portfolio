package enigma;

import java.util.ArrayList;
import java.util.HashMap;

import static enigma.EnigmaException.error;


/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Wajahat khan
 */
class Permutation {
    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        ArrayList<String> cycle = new ArrayList<>();
        _alphabet = alphabet;
        _cycles = cycles;
        Character char1, char2;
        if (_cycles.equals("")) {
            for (int i = 0; i < _alphabet.size(); i++) {
                char1 = Character.valueOf(_alphabet.toChar(i));
                _frontHashMap.put(char1, char1);
                _backHashMap.put(char1, char1);
            }
        }
        for (int i = 0; i < _cycles.length(); i++) {
            String empty = "";
            if (_cycles.charAt(i) == '(') {
                i += 1;
                while (_cycles.charAt(i) != ')') {
                    empty += String.valueOf(_cycles.charAt(i));
                    i += 1;
                }
                if (empty.equals("") || empty.matches("\\s+")) {
                    throw error("Wrong format of permutation");
                }
                cycle.add(empty);
            }
        }
        for (int i = 0; i < cycle.size(); i++) {
            String cycle1 = cycle.get(i);
            for (int j = 0; j < cycle1.length() - 1; j++) {
                char1 = Character.valueOf(cycle1.charAt(j));
                char2 = Character.valueOf(cycle1.charAt(j + 1));
                _frontHashMap.put(char1, char2);
                _backHashMap.put(char2, char1);
            }
            int holder = cycle1.length() - 1;
            _frontHashMap.put(cycle1.charAt(holder), cycle1.charAt(0));
            _backHashMap.put(cycle1.charAt(0),
                    cycle1.charAt(cycle1.length() - 1));
        }
        for (int key = 0; key < _alphabet.size(); key++) {
            if (!_frontHashMap.containsKey(_alphabet.toChar(key))) {
                _frontHashMap.put(_alphabet.toChar(key), _alphabet.toChar(key));
            }
            if (!_backHashMap.containsKey(_alphabet.toChar(key))) {
                _backHashMap.put(_alphabet.toChar(key), _alphabet.toChar(key));
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles += cycle;
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char permuteChar = _frontHashMap.get(_alphabet.toChar(wrap(p)));
        return _alphabet.toInt(permuteChar);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char invertAlphabet = _backHashMap.get(_alphabet.toChar(wrap(c)));
        return _alphabet.toInt(invertAlphabet);

    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        char permute2Char = _alphabet.toChar(wrap(_alphabet.toInt(p)));
        return _frontHashMap.get(permute2Char);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        char holder = _alphabet.toChar(wrap(_alphabet.toInt(c)));
        return _backHashMap.get(holder);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (_alphabet.toChar(i)
                    == _frontHashMap.get(_alphabet.toChar(i))
                    || _alphabet.toChar(i)
                    == _backHashMap.get(_alphabet.toChar(i))) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Alphabet of this permutation. */
    private String _cycles;

    /**
     * HashMap front.
     */
    private HashMap<Character, Character> _frontHashMap = new HashMap<>();

    /**HashMap back.
     */
    private HashMap<Character, Character> _backHashMap = new HashMap<>();
}



