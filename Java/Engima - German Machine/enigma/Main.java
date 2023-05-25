package enigma;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import ucb.util.CommandArgs;
import static enigma.EnigmaException.error;

/** Enigma simulator.
 *  @author Wajahat Khan
 */
public final class Main {
    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                    new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                        + "[INPUT [OUTPUT]]");
            }
            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
     *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));
        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }
        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output.
     *  */

    private void process() {

        Machine machine = readConfig();

        while (_input.hasNext()) {
            String nextstring = _input.next();
            if(nextstring.isEmpty())
            if (nextstring.equals("*")) {
                setUp(machine, _input.nextLine());

                if (!_input.hasNext()) {
                    throw new EnigmaException("Empty message");
                }

                while (!_input.hasNext("\\*") && _input.hasNext()) {
                    String msg = "";
                    msg = msg.concat(_input.nextLine());
                    msg = msg.replace(" ", "");
                    printParagraphLine(machine.convert(msg));
                }
            } else {
                throw new EnigmaException("Bad input");
            }
        }
    }



    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int numRotors = Integer.parseInt(_config.next("[0-9]"));
            int numPawls = Integer.parseInt(_config.next("[0-9]"));
            Collection<Rotor> rotorsList = new ArrayList<Rotor>();

            Rotor nextRotor;
            while (_config.hasNextLine()) {
                nextRotor = readRotor();
                if (nextRotor != null) {
                    rotorsList.add(nextRotor);
                } else {
                    break;
                }
            }
            Machine testMachine = new Machine(_alphabet,
                    numRotors, numPawls, rotorsList);
            return testMachine;
        } catch (EnigmaException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            if (!_config.hasNext()) {
                return null;
            } else {
                String nameRotor = _config.next();
                String notch = _config.next();
                String differentRotor = String.valueOf(notch.charAt(0));
                String cyclesString = "";
                Boolean tempNext;
                while (_config.hasNext(".*[\\(|\\)]+.*")) {
                    cyclesString = cyclesString.concat(_config.next());
                }
                if (differentRotor.equals("M")) {
                    String notchSet = notch.substring(1);
                    return new MovingRotor(nameRotor,
                            new Permutation(cyclesString, _alphabet), notchSet);
                } else if (differentRotor.equals("N")) {
                    return new FixedRotor(nameRotor,
                            new Permutation(cyclesString, _alphabet));
                } else {
                    return new Reflector(nameRotor,
                            new Permutation(cyclesString, _alphabet));
                }
            }
        } catch (EnigmaException excp) {
            throw error("bad rotor description");
        }
    }


    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        settings = settings.trim();
        String[] settingsArray = settings.split("\\s");
        String[] rotorName = new String[M.numRotors()];
        for (int i = 0; i < M.numRotors(); i++) {
            rotorName[i] = settingsArray[i];
        }
        M.insertRotors(rotorName);
        M.setRotors(settingsArray[M.numRotors()]);
        if (settingsArray.length > M.numRotors()) {
            int plugboardPermutationCount =
                    settingsArray.length - M.numRotors();
            String plugBoardCycles = "";
            for (int i = 0; i < plugboardPermutationCount; i++) {
                plugBoardCycles += settingsArray[M.numRotors() + i];
            }
            M.setPlugboard(new Permutation(plugBoardCycles, _alphabet));

        } else {
            M.setPlugboard(new Permutation("", _alphabet));
        }
    }


    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printParagraphLine(String msg) {
        String[] splitString = msg.split("");
        for (int i = 0; i < splitString.length; i++) {
            if ((i > 0) && (i % 5 == 0)) {
                _output.print(" ");
            }
            _output.print(splitString[i]);
        }
        _output.print("\n");
    }

    private void printSingleLine(String msg){
        String[] splitString = msg.split("");
        for (int i = 0; i < splitString.length; i++) {
            if ((i > 0) && (i % 5 == 0)) {
                _output.print(" ");
            }
            _output.print(splitString[i]);
        }
    }



    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;

    /** Private */


}






