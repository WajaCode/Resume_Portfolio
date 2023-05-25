package enigma;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import static enigma.EnigmaException.error;



/** Class that represents a complete enigma machine.
 *  @author Wajahat Khan
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = new HashMap<String, Rotor>();
        for (Rotor aRotor : allRotors) {
            _allRotors.put(aRotor.name(), aRotor);
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        if (_numRotors < 1) {
            throw new EnigmaException("Size of _numRotors is small");
        }
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        int pawls = 0;
        for (int i = 0; i < _rotorsList.size(); i++) {
            Rotor tempRotor = _rotorsList.get(i);
            if (tempRotor.rotates()) {
                pawls += 1;
            }
        }
        if (pawls != _numPawls) {
            throw new EnigmaException("One to many pawls bruv");
        }
        return _numPawls;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        return _rotorsList.get(k);
    }
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        try {
            _rotorsList = new ArrayList<Rotor>();
            for (String rotorName : rotors) {
                _rotorsList.add(_allRotors.get(rotorName));
            }
            if (!_rotorsList.get(0).reflecting()) {
                throw error("Refelector missing");
            }
            if (_rotorsList.size() != numRotors()) {
                throw error(" Rotors size doesn't match");
            }

        } catch (IndexOutOfBoundsException excp) {
            throw error("Settings doesn't match the Machine");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */

    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("wrong settings");
        }

        try {
            int intSettings = 0;
            Rotor tempArotor;
            char chractervalue;
            for (int i = 0; i < setting.length(); i++) {
                chractervalue = setting.charAt(i);
                if (!_alphabet.contains(chractervalue)) {
                    throw new EnigmaException("Not in Alphabet");
                }
                intSettings = _alphabet.toInt(setting.charAt(i));
                tempArotor = _rotorsList.get(i + 1);
                tempArotor.set(intSettings);
            }
        } catch (IndexOutOfBoundsException excp) {
            throw error("PUT IN THE FUCKING RIGHT SETTINGS!!!!!");
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugboard;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        if (!_alphabet.contains(c)) {
            throw new EnigmaException("Not in Alphabet");
        }
        advanceRotors();
        if (Main.verbose()) {
            System.err.printf("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */
    private void advanceRotors() {
        ArrayList<Boolean> rotorsAtNotch = new ArrayList<Boolean>();
        for (Rotor aRotor : _rotorsList) {
            rotorsAtNotch.add(Boolean.valueOf(aRotor.atNotch()));
        }
        Rotor currentRotor;
        _rotorsList.get(_rotorsList.size() - 1).advance();
        boolean rightRotorAdvanced = true;
        for (int i = _rotorsList.size() - 2; i > 0; i--) {
            currentRotor = _rotorsList.get(i);
            if (rotorsAtNotch.get(i + 1) && rightRotorAdvanced) {
                currentRotor.advance();
            } else {
                rightRotorAdvanced = false;
            }
        }
    }

    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    private int applyRotors(int c) {
        for (int i = _rotorsList.size() - 1; i > 0; i--) {
            c = _rotorsList.get(i).convertForward(c);
        }
        c = _rotorsList.get(0).permutation().permute(c);
        for (int i = 1; i < _rotorsList.size(); i++) {
            c = _rotorsList.get(i).convertBackward(c);
        }
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String emptyString = "";
        char msgChar;
        int msgToInt;
        int convert;
        char msgCharEnd;
        for (int i = 0; i < msg.length(); i++) {
            msgChar = msg.charAt(i);
            msgToInt = _alphabet.toInt(msgChar);
            convert = convert(msgToInt);
            msgCharEnd = _alphabet.toChar(convert);
            emptyString += msgCharEnd;
        }
        return emptyString;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Common alphabet of my rotors. */
    private int _numRotors;

    /** Common alphabet of my rotors. */
    private int _numPawls;

    /** Common alphabet of my rotors. */
    private HashMap<String, Rotor> _allRotors = new HashMap<>();

    /** HashMap of ROTORS. */
    private ArrayList<Rotor> _rotorsList;


    /** Object of plugboard. */
    private Permutation _plugboard;
}
